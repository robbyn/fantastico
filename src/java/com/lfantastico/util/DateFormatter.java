package com.lfantastico.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public String format(Date date, String language, String pattern) {
        DateFormatSymbols symbols
                = DateFormatSymbols.getInstance(new Locale(language));
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setDateFormatSymbols(symbols);
        return format.format(date);
    }
}
