package com.ys.sm.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * tools
 * Created by ys on 2015/12/16.
 */
public class Tool {

    /**
     * 获取所给时间的当前起始时间
     *
     * @param time
     * @return
     */
    public static long getDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    static long s = 1000;
    static long m = 60 * s;
    static long h = 60 * m;
    static long d = 24 * h;

    /**
     * 把ms级时间长短转换为xxh xxm xxs的时间格式
     *
     * @param time
     * @return
     */
    public static String getDuation(long time) {
        if (time < s) {
            return time + "ms";
        }
        if (s <= time && time < m) {
            return time / s + "s " + time % s + "ms ";
        }
        if (m <= time && time < h) {
            return time / (m) + "m " + time % m / s + "s ";
        }
        if (h <= time && time < d) {
            return time / (h) + "h " + time % (h) / m + "m ";
        }
        if (d <= time) {
            return time / d + "d " + time / (h) + "h ";
        }

        return time / 1000 + "s ";
    }
}
