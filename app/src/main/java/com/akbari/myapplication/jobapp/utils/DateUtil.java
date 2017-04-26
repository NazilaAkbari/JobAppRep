package com.akbari.myapplication.jobapp.utils;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 11/26/2016
 */

public class DateUtil {

    public static String getCurrentPersianDate() {
        return computeDateString(new PersianCalendar());
    }

    public static String getCurrentStringPersianDate() {
        return getPersianTextDate(new PersianCalendar());
    }

    public static String computeDateString(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" +
                StringUtils.leftPad(String.valueOf(month), 2, '0') + "/" +
                StringUtils.leftPad(String.valueOf(day), 2, '0');
    }

    static public PersianCalendar parsePersianDate(String pdate) {
        String[] parts = pdate.split(" ");
        String[] cal = parts[0].split("/");
        Integer year = Integer.valueOf(cal[0]);
        Integer month = Integer.valueOf(cal[1])-1;
        Integer day = Integer.valueOf(cal[2]);
        if (parts.length == 2) {
            String[] time = parts[1].split(":");
            Integer hour = Integer.valueOf(time[0]);
            Integer minute = Integer.valueOf(time[1]);
            Integer second = Integer.valueOf(time[2].substring(0, 2));
            return new PersianCalendar(year, month, day, hour, minute, second);
        }
        return new PersianCalendar(year, month, day);
    }

    static public String getPersianTextDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        String month = Month.values()[calendar.get(Calendar.MONTH)].toString();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return StringUtils.leftPad(String.valueOf(day), 2, '0') + " " +
                month + " " +
                year;
    }
}
