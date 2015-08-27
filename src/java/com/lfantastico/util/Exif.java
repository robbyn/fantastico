/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lfantastico.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author maurice
 */
public class Exif {
    public static int EXIF_MARKER = 0xE1;
    public static int IPTC_MARKER = 0xED;

    public static final int EXIF_IFD = 34665;
    public static final int GPS_IFD = 34853;
    public static final int STATIC_INTEROPERABILITY_IFD = 40965;

    public static final int ORIENTATION = 0x0112;
    public static final int DATETIME_ORIGINAL = 0x9003;
    public static final int IMAGE_WIDTH = 0xA002;
    public static final int IMAGE_HEIGHT = 0xA003;

    private static final String EXIF_ID_CODE = "Exif\0\0";
    private static final String LITTLE_ENDIAN = "II";
    private static final String BIG_ENDIAN = "MM";
    private static final int TIFF_HEADER_START = 6;

    private static final int TYPE_BYTE = 1;
    private static final int TYPE_ASCII = 2;
    private static final int TYPE_SHORT = 3;
    private static final int TYPE_LONG = 4;
    private static final int TYPE_RATIONAL = 5;
    private static final int TYPE_UNDEFINED = 7;
    private static final int TYPE_SLONG = 9;
    private static final int TYPE_SRATIONAL = 10;

    private byte data[];
    private ByteOrder byteOrder;
    private int firstIFDPosition;

    public Exif(byte data[]) throws IOException {
        this.data = data;
        ByteBuffer buf = ByteBuffer.wrap(data);
        String s = getAscii(buf, 6);
        if (!s.equals(EXIF_ID_CODE)) {
            throw new IOException("Invalid exif id code " + s);
        }
        s = getAscii(buf, 2);
        if (s.equals(LITTLE_ENDIAN)) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else if (s.equals(BIG_ENDIAN)) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else {
            throw new IOException("Invalid byte order " + s);
        }
        buf.order(byteOrder);
        if (buf.getShort() != 0x002A) {
            throw new IOException("Invalid TIFF header: expecting 0x002A");
        }
        firstIFDPosition = buf.getInt();
    }

    public Directory getRootDirectory() {
        return new Directory(firstIFDPosition);
    }

    public class Directory {
        private int position;

        private Directory(int position) {
            this.position = position;
        }

        public int getInt(int tag, int def) {
            ByteBuffer buf = locateTag(tag);
            if (buf == null) {
                return def;
            }
            int type = buf.getShort() & 0xFFFF;
            int count = buf.getInt();
            if (count != 1) {
                throw new IllegalArgumentException("Invalid Exif field size: "
                        + count + " expected: 1");
            }
            switch (type) {
                case TYPE_BYTE:
                    return buf.get();
                case TYPE_SHORT:
                    return buf.getShort() & 0xFFFF;
                case TYPE_SLONG:
                    return buf.getInt();
                case TYPE_LONG:
                    int value = buf.getInt();
                    if (value < 0) {
                        throw new ArithmeticException("Integer overflow");
                    }
                    return value;
                default:
                    throw new IllegalArgumentException(
                            "Invalid Exif field type: " + type);
            }
        }

        public void setInt(int tag, int value) {
            ByteBuffer buf = locateTag(tag);
            if (buf == null) {
                return;
            }
            int type = buf.getShort() & 0xFFFF;
            int count = buf.getInt();
            if (count != 1) {
                throw new IllegalArgumentException("Invalid Exif field size: "
                        + count + " expected: 1");
            }
            switch (type) {
                case TYPE_BYTE:
                    if (value < 0 || value > 255) {
                        throw new IllegalArgumentException(
                                "Byte overflow: " + value);
                    }
                    buf.put((byte)value);
                    break;
                case TYPE_SHORT:
                    if (value < 0 || value > 65535) {
                        throw new IllegalArgumentException(
                                "Short overflow: " + value);
                    }
                    buf.putShort((short)value);
                    break;
                case TYPE_SLONG:
                case TYPE_LONG:
                    buf.putInt(value);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Invalid Exif field type: " + type);
            }
        }

        public String getString(int tag) {
            ByteBuffer buf = locateTag(tag);
            if (buf == null) {
                return null;
            }
            int type = buf.getShort() & 0xFFFF;
            if (type != TYPE_ASCII) {
                throw new IllegalArgumentException(
                        "Invalid Exif field type: " + type + " expected: "
                        + TYPE_ASCII);
            }
            int count = buf.getInt();
            buf.position(buf.getInt() + TIFF_HEADER_START);
            byte[] data = new byte[count];
            buf.get(data);
            while (count > 0 && data[count-1] == 0) {
                --count;
            }
            return new String(data, 0, count);
        }

        public Date getDateTime(int tag) {
            String s = getString(tag);
            if (s == null) {
                return null;
            }
            try {
                DateFormat format
                        = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                return format.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new NumberFormatException("Invalid date format: " + s);
            }
        }

        public Directory getDirectory(int tag) {
            ByteBuffer buf = locateTag(tag);
            if (buf == null) {
                return null;
            }
            int type = buf.getShort() & 0xFFFF;
            if (type != TYPE_LONG) {
                throw new IllegalArgumentException("Invalid Exif field type: "
                        + type + " expected: " + TYPE_LONG);
            }
            int count = buf.getInt();
            if (count != 1) {
                throw new IllegalArgumentException("Invalid Exif field size: "
                        + count + " expected: 1");
            }
            return new Directory(buf.getInt());
        }

        public Directory getNext() {
            ByteBuffer buf = getBuffer();
            int count = 0xFFFF & buf.getShort();
            buf.position(buf.position()+count*12);
            int next = buf.getInt();
            return next == 0 ? null : new Directory(next);
        }

        private ByteBuffer getBuffer() {
            ByteBuffer buf = ByteBuffer.wrap(data);
            buf.order(byteOrder);
            buf.position(position + TIFF_HEADER_START);
            return buf;
        }

        private ByteBuffer locateTag(int tag) {
            ByteBuffer buf = getBuffer();
            int count = 0xFFFF & buf.getShort();
            for (int i = 0; i < count; ++i) {
                 int tg = 0xFFFF & buf.getShort();
                 if (tg == tag) {
                     return buf;
                 }
                 buf.position(buf.position()+10);
           }
            return null;
        }
    }

    private static String getAscii(ByteBuffer b, int length) throws UnsupportedEncodingException {
        return getString(b, length, "ASCII");
    }

    private static String getString(ByteBuffer b, int length, String encoding)
            throws UnsupportedEncodingException {
        byte bytes[] = new byte[length];
        b.get(bytes, 0, length);
        return new String(bytes, 0, length, encoding);
    }
}
