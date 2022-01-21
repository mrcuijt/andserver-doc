package com.yanzhenjie.andserver.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static String dateFormat(Date paramDate, TimeZone paramTimeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(paramTimeZone);
        String str = "%s GMT";
        return String.format(str, new Object[] { simpleDateFormat.format(paramDate) });
    }

    public static Date bowerCache() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, 7);
        return calendar.getTime();
    }
}
