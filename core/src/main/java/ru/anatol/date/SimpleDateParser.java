/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Anatol
 */
public class SimpleDateParser implements DateParser {

    private final String format;
    private final TimeZone timezone;
    private final Locale locale;

//   java.util.SimpleTimeZone timezone = new java.util.SimpleTimeZone(4 * 3600000, "UTC+4");//Europe/Moscow UTC=GTM 14400000
//   java.util.SimpleTimeZone timezone = new java.util.SimpleTimeZone(3 * 3600000, "UTC+3");//Europe/Moscow UTC=GTM 14400000
    public SimpleDateParser(String format) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = null;
        this.locale = null;
    }

    public SimpleDateParser(String format, TimeZone timezone) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = timezone;
        this.locale = null;
    }

    public SimpleDateParser(String format, Locale locale) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = null;
        this.locale = locale;
    }

    public SimpleDateParser(String format, TimeZone timezone, Locale locale) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = timezone;
        this.locale = locale;
    }

    @Override
    public Date parse(String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("value = null");
        }
        try {
            final DateFormat dateFormat;
            if (locale != null) {
                dateFormat = new java.text.SimpleDateFormat(format, locale);
            } else {
                dateFormat = new java.text.SimpleDateFormat(format);
            }
            if (timezone != null) {
                dateFormat.setTimeZone(timezone);
            }
            java.util.Date date = new java.util.Date(dateFormat.parse(value).getTime());
            return date;
        } catch (java.text.ParseException ex) {
            throw new IllegalArgumentException("value \"" + value + "\" can not by parse as date with format: " + format, ex);
        }
    }

}
