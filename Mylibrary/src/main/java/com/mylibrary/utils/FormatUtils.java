package com.mylibrary.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;

import com.alipay.api.domain.BankCardInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/4.
 */
public class FormatUtils {

    private static final String formatDefault = "yyyy-MM-dd HH:mm:ss";
    private static final String formatDefault2 = "yyyyMMddHHmmss";

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getTrim(double value, String rules) {
        DecimalFormat df = new DecimalFormat(rules);
        return df.format(value);
    }

    public static String getTrim(double value) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(value);
    }

    public static String getFormatTime(String currtimeStyle, String time, String timeStyle) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(currtimeStyle);
            Date date = format.parse(time);
            return new SimpleDateFormat(timeStyle, Locale.getDefault()).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 修剪浮点类型
     *
     * @param value value
     * @param rules 规则(如:0.00保留2位小数)
     * @return string or "" or value
     */
    public static String getTrim(String value, String rules) {
        if (value == null || value.length() == 0 || rules == null || rules.length() == 0) {
            return "";
        }
        try {
            return getTrim(Double.parseDouble(value), rules);
        } catch (Exception e) {
            return value;
        }
    }

    public static String getFormatDate(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDefault);
        String dateStr = sdf.format(new Date(l * getFormatDateLength(l)));
        return dateStr;
    }

    /**
     * 获取时间戳长度
     *
     * @param l
     * @return
     */
    public static long getFormatDateLength(long l) {
        long dateLength = l + "".length();
        long result = 1;
        for (int i = 0; i < 13 - dateLength; i++) {
            result *= 10;
        }
        return result;
    }

    public static String getFormatDate(long l, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(new Date(l * 1000));
        return dateStr;
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDefault);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String getFormatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 默认为24小时制
     *
     * @param l
     * @return
     */
    public static Date getFormatDateStr(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDefault);
        Date date = null;
        try {
            date = sdf.parse(getFormatDate(l));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getFormatDateStr(long l, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(getFormatDate(l, format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getFormatDateStr(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDefault);
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getFormatDateStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取两点间距离,单位：px
     *
     * @param x1 第一个点
     * @param x2 第二个点
     * @return
     * @formula |AB| = sqrt((X1-X2)^2 + (Y1-Y2)^2)
     */
    public static double getFormatDistance(Point x1, Point x2) {
        return getFormatDistance(x1.x, x2.x, x1.y, x2.y);
    }


    public static double getFormatDistance(float x1, float x2, float y1, float y2) {
        float x = Math.abs(x2 - x1);
        float y = Math.abs(y2 - y1);
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 获得字体高度
     */
    public static int getFontHeight(Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds("正", 0, 1, rect);
        return rect.height();
    }

    /**
     * 获得字体宽
     */
    public static int getFontWidth(Paint paint, String str) {
        if (str == null || str.equals(""))
            return 0;
        Rect rect = new Rect();
        int length = str.length();
        paint.getTextBounds(str, 0, length, rect);
        return rect.width();
    }

    public static String formatNumberWithCommaSplit(double number) {
        return formatNumberWithCommaSplit(number, ",");
    }

    /**
     * 格式化数字，用逗号分割
     *
     * @param number 1000000.7569 to 1,000,000.76 or
     * @return
     */
    public static String formatNumberWithCommaSplit(double number, String splitChar) {
        String firstStr = "";//第一个字符
        String middleStr = "";//中间字符
        String endStr = "00";//小数后两位
        if (number < 0) {
            firstStr = "-";
        } else if (number != 0 && number < 0.1) {
            return number + "";
        }

        NumberFormat format = NumberFormat.getInstance();//解决超大数字直接转换为字符串的问题
        format.setGroupingUsed(false);
        String tempNumberStr = format.format(number) + "00";
        int endIndex = tempNumberStr.lastIndexOf(".");
        if (endIndex != -1) {
            endStr = tempNumberStr.substring(endIndex + 1, endIndex + 3);
        }

        String numberStr = Math.abs((long) number) + "";//取正

        int firstIndex = numberStr.length() % 3;
        int bitCount = numberStr.length() / 3;

        if (firstIndex > 0) {
            middleStr += numberStr.substring(0, firstIndex) + splitChar;
        }
        for (int i = 0; i < bitCount; i++) {
            middleStr += numberStr.substring(firstIndex + i * 3, firstIndex + i * 3 + 3) + splitChar;
        }
        if (middleStr.length() > 1) {
            middleStr = middleStr.substring(0, middleStr.length() - 1);
        }
        return firstStr + middleStr + "." + endStr;
    }

    /**
     * 格式化手机号码，用空格分割
     *
     * @param mobileNumber 12345678910 to 123 4567 8910
     * @return
     */
    public static String formatMobileSpace(String mobileNumber) {
        if (TextUtils.isEmpty(mobileNumber) || mobileNumber.length() < 11) {
            return mobileNumber;
        }
        return mobileNumber.substring(0, 3) + " " + mobileNumber.substring(3, 11 - 4) + " " + mobileNumber.substring(11 - 4, mobileNumber.length());
    }

    /**
     * 格式化手机号码，用空格分割
     *
     * @param mobileNumber 12345678910 to 123****8910
     * @return
     */
    public static String formatMobileStar(String mobileNumber) {
        if (TextUtils.isEmpty(mobileNumber) || mobileNumber.length() < 11 || !MobileCheckUtils.isMobileNo(mobileNumber)) {
            return mobileNumber;
        }
        return mobileNumber.substring(0, 3) + "****" + mobileNumber.substring(11 - 4, mobileNumber.length());
    }

    /**
     * @param idCardNumber 5000909342893289489328 to 5**************************8
     * @return
     */
    public static String formatIdCardStar(String idCardNumber) {
        if (TextUtils.isEmpty(idCardNumber) || !IDCardUtils.IDCardValidate(idCardNumber).equalsIgnoreCase(IDCardUtils.SUCCESS_INFO)) {
            return idCardNumber;
        }
        return idCardNumber.substring(0, 1) + "****************" + idCardNumber.substring(17, 18);
    }

    /**
     * @param email zoidfkdfdsl@gmail.com to z**********@gmail.com
     * @return
     */
    public static String formatEmailStar(String email) {
        if (TextUtils.isEmpty(email) || !AppUtil.isEmail(email)) {
            return email;
        }
        return email.substring(0, 1) + "*******" + email.substring(email.lastIndexOf("@") - 1, email.length());
    }

    /**
     * @param bandCardNumber
     * @return
     */
    public static String formatBankCard(String bandCardNumber) {
        if (TextUtils.isEmpty(bandCardNumber)) {
            return bandCardNumber;
        }
        return "**** **** **** " + bandCardNumber.substring(bandCardNumber.length() - 4, bandCardNumber.length());

    }
}
