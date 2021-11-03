package com.iss.cloud.disk.configure;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class DateFormatter implements Formatter<Date> {

    @Override
    public String print(Date object, Locale locale) {
        return new SimpleDateFormat("yyyy-MM-dd").format(object);
    }

    @Override
    public Date parse(String source, Locale argl) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(source);
    }
}