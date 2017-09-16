/*
 *
 *  * yidingliu.com Inc. * Copyright (c) 2016 All Rights Reserved.
 *
 */

package com.mylibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author GaoPeng
 * @Date 2016/11/1 0001
 * @modifyInfo Gao-2016/11/1 0001
 * @modifyContent
 */
public class DateUtil {
    public static final String PATTERN_STANDARD08W = "yyyyMMdd";
    public static final String PATTERN_STANDARD12W = "yyyyMMddHHmm";
    public static final String PATTERN_STANDARD14W = "yyyyMMddHHmmss";
    public static final String PATTERN_STANDARD17W = "yyyyMMddHHmmssSSS";

    public static final String PATTERN_STANDARD10H = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERN_STANDARD10X = "yyyy/MM/dd";
    public static final String PATTERN_STANDARD16X = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_STANDARD19X = "yyyy/MM/dd HH:mm:ss";

    /**
     * @param date
     * @param pattern
     * @return
     * @Title: date2String
     * @Description: 日期格式的时间转化成字符串格式的时间
     */
    public static String date2String(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        pattern = (pattern == null || pattern.equals("")) ? PATTERN_STANDARD19H : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * @param strDate
     * @param pattern
     * @return
     * @Title: string2Date
     * @Description: 字符串格式的时间转化成日期格式的时间
     */
    public static Date string2Date(String strDate, String pattern) {
        if (strDate == null || strDate.equals("")) {
            throw new RuntimeException("strDate is null");
        }
        pattern = (pattern == null || pattern.equals("")) ? PATTERN_STANDARD19H : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * @param format 格式 17位(yyyyMMddHHmmssSSS) (14位:yyyyMMddHHmmss) (12位:yyyyMMddHHmm) (8位:yyyyMMdd)
     * @return
     * @Title: getCurrentTime
     * @Description: 取得当前系统时间
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        String date = formatDate.format(new Date());
        return date;
    }

    /**
     * @param dateStr
     * @param wantFormat
     * @return
     * @Title: getWantDate
     * @Description: 获取想要的时间格式
     */
    public static String getWantDate(String dateStr, String wantFormat) {
        if (!"".equals(dateStr) && dateStr != null) {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch (len) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 10:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD10H : PATTERN_STANDARD10X;
                    break;
                case 16:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD16H : PATTERN_STANDARD16X;
                    break;
                case 19:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD19H : PATTERN_STANDARD19X;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(wantFormat);
            try {
                SimpleDateFormat sdfStr = new SimpleDateFormat(pattern);
                Date date = sdfStr.parse(dateStr);
                dateStr = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

    /**
     * @param dateStr
     * @param minute
     * @return
     * @Title: getAfterTime
     * @Description: 获取该时间的几分钟之后的时间
     */
    public static String getAfterTime(String dateStr, int minute) {
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch (len) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 10:
                    pattern = PATTERN_STANDARD10H;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 16:
                    pattern = PATTERN_STANDARD16H;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 19:
                    pattern = PATTERN_STANDARD19H;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            Date date = null;
            date = formatDate.parse(dateStr);
            Date afterDate = new Date(date.getTime() + (60000 * minute));
            returnStr = formatDate.format(afterDate);
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
        }
        return returnStr;
    }

    /**
     * @param dateStr
     * @param minute
     * @return
     * @Title: getBeforeTime
     * @Description: 获取该时间的几分钟之前的时间
     */
    public static String getBeforeTime(String dateStr, int minute) {
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch (len) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 10:
                    pattern = PATTERN_STANDARD10H;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 16:
                    pattern = PATTERN_STANDARD16H;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 19:
                    pattern = PATTERN_STANDARD19H;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            Date date = null;
            date = formatDate.parse(dateStr);
            Date afterDate = new Date(date.getTime() - (60000 * minute));
            returnStr = formatDate.format(afterDate);
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
        }
        return returnStr;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getWantDate("2011-01-01 23:59:23", "yyyyMMdd"));
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 将毫秒转换成日期格式
     *
     * @param strTime
     * @return
     */
    public static String stringToDate(String strTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Long.parseLong(strTime));
    }
}
