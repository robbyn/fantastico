package org.tastefuljava.mvc;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSonWriter {
    private static final Logger LOG = LoggerFactory.getLogger(JSonWriter.class);

    private static char HEX[] = "0123456789ABCDEF".toCharArray();

    private PrintWriter out;
    private boolean format;
    private boolean bol;
    private List<Block> blockStack = new ArrayList<Block>();

    public JSonWriter(PrintWriter out, boolean format) {
        this.out = out;
        this.format = format;
    }

    public void close() {
        out.close();
    }

    public void printBoolean(boolean value) {
        indent();
        out.print(value);
    }

    public void printByte(byte value) {
        indent();
        out.print(value);
    }

    public void printShort(short value) {
        indent();
        out.print(value);
    }

    public void printInt(int value) {
        indent();
        out.print(value);
    }

    public void printLong(long value) {
        indent();
        out.print(value);
    }

    public void printFloat(float value) {
        indent();
        out.print(value);
    }

    public void printDouble(double value) {
        indent();
        out.print(value);
    }

    public void printString(String value) {
        indent();
        out.print('"');
        for (char c: value.toCharArray()) {
            switch (c) {
                case '\\':
                    out.print("\\\\");
                    break;
                case '\"':
                    out.print("\\\"");
                    break;
                case '\b':
                    out.print("\\b");
                    break;
                case '\f':
                    out.print("\\b");
                    break;
                case '\n':
                    out.print("\\b");
                    break;
                case '\r':
                    out.print("\\b");
                    break;
                case '\t':
                    out.print("\\b");
                    break;
                default:
                    if (c >= 32 && c <= 127) {
                        out.print(c);
                    } else {
                        out.print("\\u");
                        printHex(c, 4);
                    }
            }
        }
        out.print('"');
    }

    public void printValue(Object value) {
        if (value == null) {
            indent();
            out.print("null");
        } else if (value instanceof Boolean) {
            printBoolean((Boolean)value);
        } else if (value instanceof Byte) {
            printByte((Byte)value);
        } else if (value instanceof Short) {
            printShort((Short)value);
        } else if (value instanceof Integer) {
            printInt((Integer)value);
        } else if (value instanceof Long) {
            printLong((Long)value);
        } else if (value instanceof Float) {
            printFloat((Float)value);
        } else if (value instanceof Double) {
            printDouble((Double)value);
        } else if (value instanceof String) {
            printString((String)value);
        } else if (value instanceof Class) {
            printString(((Class)value).getCanonicalName());
        } else {
            Class clazz = value.getClass();
            if (clazz.isArray()) {
                printArray(value);
            } else {
                printObject(value);
            }
        }
    }

    public void printArray(Object value) {
        indent();
        startArray();
        try {
            int length = Array.getLength(value);
            for (int i = 0; i < length; ++i) {
                Object element = Array.get(value, i);
                startElement();
                try {
                    printValue(element);
                } finally {
                    endElement();
                }
            }
        } finally {
            endBlock();
        }
    }

    public void printObject(Object value) {
        indent();
        startObject();
        try {
            for (Method method: value.getClass().getMethods()) {
                if (method.getDeclaringClass() == Object.class) {
                    // skip
                } else if (method.getParameterTypes().length > 0) {
                    // skip
                } else {
                    String name = method.getName();
                    String propName = null;
                    if (name.startsWith("get") && name.length() > 3) {
                        propName = Character.toLowerCase(name.charAt(3))
                                + name.substring(4);
                    } else if (name.startsWith("is") && name.length() > 2) {
                        Class retType = method.getReturnType();
                        if (retType == boolean.class
                                || retType == Boolean.class) {
                            propName = Character.toLowerCase(name.charAt(3))
                                    + name.substring(4);
                        }
                    }
                    if (propName != null) {
                        try {
                            printField(propName, method.invoke(value));
                        } catch (IllegalAccessException ex) {
                            LOG.error("Error getting value of " + propName,
                                    ex);
                        } catch (IllegalArgumentException ex) {
                            LOG.error("Error getting value of " + propName,
                                    ex);
                        } catch (InvocationTargetException ex) {
                            LOG.error("Error getting value of " + propName,
                                    ex);
                        } finally {
                            endElement();
                        }
                    }
                }
            }
        } finally {
            endBlock();
        }
    }

    public void printObject(Object value, String... propNames) {
        indent();
        startObject();
        try {
            printProps(value, propNames);
        } finally {
            endBlock();
        }
    }

    public void printProps(Object value, String... propNames) {
        Class clazz = value.getClass();
        for (String propName: propNames) {
            try {
                String getterName = "get"
                        + Character.toUpperCase(propName.charAt(0))
                        + propName.substring(1);
                @SuppressWarnings("unchecked")
                Method getter = clazz.getMethod(getterName);
                if (getter == null) {
                    getterName = "is"
                        + Character.toUpperCase(propName.charAt(0))
                        + propName.substring(1);
                    @SuppressWarnings("unchecked")
                    Method g = clazz.getMethod(getterName);
                    getter = g;
                    Class retType = getter.getReturnType();
                    if (retType != boolean.class
                            && retType != Boolean.class) {
                        getter = null;
                    }
                }
                if (getter != null) {
                    printField(propName, getter.invoke(value));
                }
            } catch (NoSuchMethodException ex) {
                LOG.error("Error getting value of " + propName,
                        ex);
            } catch (SecurityException ex) {
                LOG.error("Error getting value of " + propName,
                        ex);
            } catch (IllegalAccessException ex) {
                LOG.error("Error getting value of " + propName,
                        ex);
            } catch (IllegalArgumentException ex) {
                LOG.error("Error getting value of " + propName,
                        ex);
            } catch (InvocationTargetException ex) {
                LOG.error("Error getting value of " + propName,
                        ex);
            }
        }
    }

    public void printField(String name, Object value) {
        startElement();
        try {
            indent();
            printFieldName(name);
            out.print(':');
            printValue(value);
        } finally {
            endElement();
        }
    }

    public void startArrayField(String name) {
        startElement();
        indent();
        printFieldName(name);
        out.print(':');
        startArray();
    }

    public void startObjectField(String name) {
        startElement();
        indent();
        printFieldName(name);
        out.print(':');
        startObject();
    }

    public void endField() {
        endBlock();
        endElement();
    }

    public void startArray() {
        indent();
        Block block = new ArrayBlock();
        blockStack.add(block);
        block.start();
    }

    public void startObject() {
        indent();
        Block block = new ObjectBlock();
        blockStack.add(block);
        block.start();
    }

    public void endBlock() {
        Block block = blockStack.remove(blockStack.size()-1);
        block.end();
    }

    public void startElement() {
        Block block = blockStack.get(blockStack.size()-1);
        block.startElement();
    }

    public void endElement() {
        Block block = blockStack.get(blockStack.size()-1);
        block.endElement();
    }

    private void printHex(int value, int digits) {
        char chars[] = new char[digits];
        for (int i = digits; --i >= 0; ) {
            chars[i] = HEX[value % 16];
            value /= 16;
        }
        for (char c: chars) {
            out.print(c);
        }
    }

    private void printFieldName(String name) {
        printString(name);
    }

    private void println() {
        if (format && !bol) {
            out.println();
            bol = true;
        }
    }

    private void indent() {
        if (format && bol) {
            for (int i = 0; i < blockStack.size(); ++i) {
                out.print("    ");
            }
            bol = false;
        }
    }

    private abstract class Block {
        boolean firstElement = true;

        abstract void start();
        abstract void end();
        abstract void separator();

        void startElement() {
            if (firstElement) {
                firstElement = false;
            } else {
                separator();
            }
        }

        void endElement() {
            // empty
        }
    }

    private class ArrayBlock extends Block {

        @Override
        void start() {
            out.print('[');
            println();
        }

        @Override
        void end() {
            println();
            indent();
            out.print(']');
        }

        @Override
        void separator() {
            out.print(',');
            println();
        }

    }

    private class ObjectBlock extends Block {

        @Override
        void start() {
            out.print('{');
            println();
        }

        @Override
        void end() {
            println();
            indent();
            out.print('}');
        }

        @Override
        void separator() {
            out.print(',');
            println();
        }
    }
}
