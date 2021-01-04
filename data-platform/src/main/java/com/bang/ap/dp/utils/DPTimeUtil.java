package com.bang.ap.dp.utils;

import com.bang.ap.dp.cons.DPConstant;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DPTimeUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat(DPConstant.DATE_FORMAT);

    public static void formatDateTime(Map<String, Object> conditionMap) {
        if (conditionMap.get(DPConstant.PARAMETER_START_DATE) != null && StringUtils.isNotBlank(conditionMap.get(DPConstant.PARAMETER_START_DATE) + DPConstant.PARAMETER_EMPTY)) {
            long startDate = Long.valueOf(conditionMap.get(DPConstant.PARAMETER_START_DATE).toString());
            String newstartDate = DPTimeUtil.formatDate(startDate);
            conditionMap.put(DPConstant.PARAMETER_START_DATE, newstartDate);
        }

        if (conditionMap.get(DPConstant.PARAMETER_END_DATE) != null && StringUtils.isNotBlank(conditionMap.get(DPConstant.PARAMETER_END_DATE) + DPConstant.PARAMETER_EMPTY)) {
            long endDate = Long.valueOf(conditionMap.get(DPConstant.PARAMETER_END_DATE).toString());
            String newendDate = DPTimeUtil.formatDate(endDate);
            conditionMap.put(DPConstant.PARAMETER_END_DATE, newendDate);
        }

    }


    /**
     * 获取当前时间
     *
     * @param pattern 时间格式：例如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentLocalDateTime(String pattern) {
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return nowTime.format(formatter);
    }


    /**
     * 按照yyyy-MM-dd HH:mm:ss格式化时间
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        synchronized (sdf) {
            return sdf.format(date);
        }
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        synchronized (simpleDateFormat) {
            return simpleDateFormat.format(date);
        }
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss格式化时间
     *
     * @param date
     * @return
     */
    public static String formatDate(long date) {
        synchronized (sdf) {
            return sdf.format(new Date(date));
        }
    }


    /**
     * 获取今天日期
     *
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取昨天日期
     *
     * @return
     */
    public static Date getYesterday() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        return yesterday;
    }

    public static Date get7DaysAgo() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -7);
        Date result = c.getTime();
        return result;
    }

    public static Date getNDaysAgo(int num) {
        if (num>0){
            return null;
        }
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, num);
        Date result = c.getTime();
        return result;
    }

    public static String getYesterday(String pattern) {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        String yesterdayString = DPTimeUtil.formatDate(yesterday, pattern);
        return yesterdayString;
    }


    /**
     * 获取明天日期
     *
     * @return
     */
    public static Date getTomorrow() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = c.getTime();
        return tomorrow;
    }


    /**
     * 将传入的ISO8601 时间字符串，转成成指定格式的Local时区字(UTC+8)符串
     */
    public static String isoStr2utc8Str(String isoStr, String pattern) {
        org.joda.time.format.DateTimeFormatter isoformat = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        DateTime dt = isoformat.parseDateTime(isoStr);
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        return dt.toString(format);
    }

    /**
     * 将传入的指定格式的Local时区字(UTC+8)符串，转成ISO8601时间字符串
     */
    public static String utc8Str2IsoStr(String isoStr, String pattern) {
        org.joda.time.format.DateTimeFormatter isoformat = DateTimeFormat.forPattern(pattern);
        DateTime dt = isoformat.parseDateTime(isoStr);
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        return dt.toString(format);
    }

    /**
     * 将传入的DateTime，转成ISO8601符串
     */
    public static String dateTime2IsoStr(DateTime dateTime) {
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        return dateTime.toString(format);
    }

    public static String getEarlier(String time1, String time2) {
        org.joda.time.format.DateTimeFormatter isoformat = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        DateTime dt1 = isoformat.parseDateTime(time1);
        DateTime dt2 = isoformat.parseDateTime(time2);
        if (dt1.isAfter(dt2)) {
            return time2;
        }
        return time1;

    }

    public static String getLater(String time1, String time2) {
        org.joda.time.format.DateTimeFormatter isoformat = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        DateTime dt1 = isoformat.parseDateTime(time1);
        DateTime dt2 = isoformat.parseDateTime(time2);
        if (dt1.isBefore(dt2)) {
            return time2;
        }
        return time1;
    }

    public static long getInterval(String time1, String time2) {
        org.joda.time.format.DateTimeFormatter isoformat = DateTimeFormat.forPattern(DPConstant.DATE_FORMAT_IOS8601);
        DateTime dt1 = isoformat.parseDateTime(time1);
        DateTime dt2 = isoformat.parseDateTime(time2);
        long inte = dt2.getMillis() - dt1.getMillis();
        long diffMinutes = inte / (60 * 1000 * 60);
        return diffMinutes;

    }

    /**
     * 获取一天的开始时间
     * @param date 某一天时间
     * @return 某一天的开始时间,比如2005-01-01 00:00:00.000
     */
    public static Date getBeginTimeOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


}
