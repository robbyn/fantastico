package org.tastefuljava.mvc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversion {
    // private constructor to prevent instanciation
    private Conversion() {
    }

    public static Object parse(Class<?> type, String value, String pattern)
            throws ParseException {
        if (type == String.class) {
            return value;
        } else if (value == null || value.length() == 0) {
            return null;
        } else if (Enum.class.isAssignableFrom(type)) {
            @SuppressWarnings("unchecked")
            Object result = Enum.valueOf((Class<? extends Enum>)type, value);
            return result;
        } else if (type == boolean.class || type == Boolean.class) {
            return "true".equals(value);
        } else if (type == byte.class || type == Byte.class) {
            return Byte.valueOf(value);
        } else if (type == short.class || type == Short.class) {
            return Short.valueOf(value);
        } else if (type == int.class || type == Integer.class) {
            return Integer.valueOf(value);
        } else if (type == long.class || type == Long.class) {
            return Long.valueOf(value);
        } else if (type == float.class || type == Float.class) {
            return Float.valueOf(value);
        } else if (type == double.class || type == Double.class) {
            return Double.valueOf(value);
        } else if (type == Date.class) {
            return new SimpleDateFormat(pattern).parse(value);
        } else if (type == BigDecimal.class) {
            DecimalFormat format = getDecimalFormat(
                    pattern == null ? "0.00" : pattern);
            return format.parse(value);
        } else {
            throw new RuntimeException("Cannot convert value of type " + type);
        }
    }

    private static DecimalFormat getDecimalFormat(String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat(pattern);
        format.setParseBigDecimal(true);
        format.setDecimalFormatSymbols(symbols);
        return format;
    }
}
