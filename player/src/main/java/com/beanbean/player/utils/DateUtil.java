package com.beanbean.player.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    /**
     * 毫秒转成时分秒
     *
     * @param time
     * @return
     */
    public static String timestampToDate(long time) {
        if (time < 0){
            return "--:--:--";
        }
        SimpleDateFormat formatter =  new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(time);
    }
}
