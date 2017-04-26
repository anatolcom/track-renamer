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
public class SimpleDateFormater implements DateFormater {

    private final String format;
    private final TimeZone timezone;
    private final Locale locale;

//   java.util.SimpleTimeZone timezone = new java.util.SimpleTimeZone(4 * 3600000, "UTC+4");//Europe/Moscow UTC=GTM 14400000
//   java.util.SimpleTimeZone timezone = new java.util.SimpleTimeZone(3 * 3600000, "UTC+3");//Europe/Moscow UTC=GTM 14400000
    public SimpleDateFormater(String format) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = null;
        this.locale = null;
    }

    public SimpleDateFormater(String format, TimeZone timezone) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = timezone;
        this.locale = null;
    }

    public SimpleDateFormater(String format, Locale locale) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = null;
        this.locale = locale;
    }

    public SimpleDateFormater(String format, TimeZone timezone, Locale locale) {
        if (format == null) {
            throw new IllegalArgumentException("format = null");
        }
        this.format = format;
        this.timezone = timezone;
        this.locale = locale;
    }

    @Override
    public String format(Date date) throws IllegalArgumentException {
        if (date == null) {
            throw new IllegalArgumentException("date = null");
        }
        final DateFormat dateFormat;
        if (locale != null) {
            dateFormat = new SimpleDateFormat(format, locale);
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        if (timezone != null) {
            dateFormat.setTimeZone(timezone);
        }
        return dateFormat.format(date);
    }

}
