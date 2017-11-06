
package com.glodon.bim.basic.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description : Created by 程磊 on 2016/7/12. Job number：139268 Phone
 * ：13141390126 Email：chenglei@syswin.com Person in charge : Leader：李晓
 */
public class DateUtils {
    public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final String FORMAT_MONTH_DAY = "MM-dd";

    public static final String FORMAT_MONTH_DAY_HOUR_MINUTE = "MM-dd HH:mm";

    public static final String FORMAT_HOUR_MINUTE = "HH:mm";

    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static final String YESTERDAY = "昨天";

    public static boolean isSameYear(Calendar current, Calendar record) {
        return (current.get(Calendar.YEAR) == record.get(Calendar.YEAR));
    }

    public static boolean isMonth(String sdate) {
        if (sdate != null && sdate.length() > 0 && !isToday(sdate) && !isYesterday(sdate)) {
            String month = null;
            String year = null;
            try {
                Calendar c = Calendar.getInstance(Locale.CHINA);
                c.setTime(new Date(System.currentTimeMillis()));
                month = c.get(Calendar.MONTH) + "";
                year = c.get(Calendar.YEAR) + "";
                c.setTime(new Date(Long.parseLong(sdate)));
                String s_month = c.get(Calendar.MONTH) + "";
                String s_year = c.get(Calendar.YEAR) + "";
                if (month.equals(s_month) && year.equals(s_year)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isSameDay(Calendar current, Calendar record) {
        return (current.get(Calendar.YEAR) == record.get(Calendar.YEAR))
                && (current.get(Calendar.MONTH) == record.get(Calendar.MONTH))
                && (current.get(Calendar.DAY_OF_MONTH) == record.get(Calendar.DAY_OF_MONTH));
    }

    public static boolean isToday(String sdate) {
        if (sdate != null && sdate.length() > 0) {
            String current_date = null;
            try {
                SimpleDateFormat sDateFormat = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY,
                        Locale.CHINA);
                Calendar c = Calendar.getInstance(Locale.CHINA);
                current_date = sDateFormat.format(c.getTime());
                String s_date = sDateFormat.format(new Date(Long.parseLong(sdate)));
                if (current_date.equals(s_date)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isYesterday(Calendar current, Calendar record) {
        if ((current.get(Calendar.YEAR) == record.get(Calendar.YEAR))
                && (current.get(Calendar.MONTH) == record.get(Calendar.MONTH)) && (current
                        .get(Calendar.DAY_OF_MONTH) == (record.get(Calendar.DAY_OF_MONTH) + 1))) {
            return true;
        }
        if ((current.get(Calendar.YEAR) == record.get(Calendar.YEAR))) {
            if ((current.get(Calendar.MONTH) == record.get(Calendar.MONTH)) && (current
                    .get(Calendar.DAY_OF_MONTH) == (record.get(Calendar.DAY_OF_MONTH) + 1))) {
                return true;
            } else if ((current.get(Calendar.MONTH) == (record.get(Calendar.MONTH)) + 1)
                    && (current.get(Calendar.DAY_OF_MONTH) == 1)
                    && record.get(Calendar.DAY_OF_MONTH) == record
                            .getActualMaximum(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为同一年
     */
    public static boolean isSameYear(String sdata) {
        if (sdata != null && sdata.length() > 0 && !isMonth(sdata)) {
            String year = null;
            try {
                Calendar c = Calendar.getInstance(Locale.CHINA);
                c.setTime(new Date(System.currentTimeMillis()));
                year = c.get(Calendar.YEAR) + "";
                c.setTime(new Date(Long.parseLong(sdata)));
                String s_year = c.get(Calendar.YEAR) + "";
                if (year.equals(s_year)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断是否是昨天
     */
    public static boolean isYesterday(String sdate) {
        if (sdate != null && sdate.length() > 0) {
            String yesterday_date = null;
            try {
                SimpleDateFormat sDateFormat = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY,
                        Locale.CHINA);
                Calendar c = Calendar.getInstance(Locale.CHINA);
                yesterday_date = sDateFormat.format(new Date((c.getTimeInMillis() - 86400000)));
                String s_date = sDateFormat.format(new Date(Long.parseLong(sdate)));
                if (yesterday_date.equals(s_date)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getCurrentTime(String format) {

        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    public static String getStringTime(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getStringTime(Long date, String format) {
        if ((date / 10000000000L) == 0) {
            date *= 1000;
        }
        return getStringTime(getDateTime(date), format);
    }

    public static Date getDateTime(Long date) {
        if ((date / 10000000000L) == 0) {
            date *= 1000;
        }
        return new Date(date);
    }

    public static Date getDateTime(String time, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(time);
    }

    public static Long getLongTime(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return date.getTime();
    }

    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHourOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMiute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * long类型时间格式化
     *
     * @param time 时间毫秒数
     * @param format 时间显示格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertToTime(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * 显示时间(在同一天内，显示二十四小时制时间，不在同一天则显示年月日)
     *
     * @param time 时间戳字符串
     * @param isaddlattime 是否在"昨天"之后添加具体时间显示,且在非今天和昨天的时间显示日期之后的具体时间
     */
    public static String getTimeshow(String time, boolean isaddlattime) {
        if (time != null && time.length() > 0) {
            try {
                if (isToday(time)) {
                    return new SimpleDateFormat(FORMAT_HOUR_MINUTE)
                            .format(new Date(Long.parseLong(time)));
                } else if (isYesterday(time)) {
                    String s_date = new SimpleDateFormat(FORMAT_HOUR_MINUTE)
                            .format(new Date(Long.parseLong(time)));
                    if (isaddlattime) {
                        s_date = YESTERDAY + s_date;
                    } else {
                        s_date = YESTERDAY;
                    }
                    return s_date;
                } else {
                    String s_date = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY)
                            .format(new Date(Long.parseLong(time)));
                    if (isaddlattime) {
                        s_date = new SimpleDateFormat(FORMAT_DATE)
                                .format(new Date(Long.parseLong(time)));
                    }
                    return s_date;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return time;
            }
        }
        return null;
    }

    /**
     * 聊天列表时间显示规则
     */
    public static String getChatTime(String date) {
        try {
            long currentTime = System.currentTimeMillis();
            long recordTime = Long.valueOf(date);
            Date currentDate = new Date(currentTime);
            Date recordDate = new Date(recordTime);
            Calendar current = Calendar.getInstance();
            Calendar record = Calendar.getInstance();
            current.setTime(currentDate);
            record.setTime(recordDate);

            if (!isSameYear(current, record)) {
                return new SimpleDateFormat(FORMAT_DATE).format(recordTime);
            }

            if (isSameDay(current, record)) {
                return new SimpleDateFormat(FORMAT_HOUR_MINUTE).format(recordTime);
            }

            if (isYesterday(current, record)) {
                return "昨天  " + new SimpleDateFormat(FORMAT_HOUR_MINUTE).format(recordTime);
            }

            return new SimpleDateFormat(FORMAT_MONTH_DAY_HOUR_MINUTE).format(recordTime);

        } catch (Exception e) {
            return date + "";
        }

    }

    /**
     * 沟通列表时间显示规则
     */
    public static String getCommonTime(String date) {
        try {
            long currentTime = System.currentTimeMillis();
            long recordTime = Long.parseLong(date);
            Date currentDate = new Date(currentTime);
            Date recordDate = new Date(recordTime);
            Calendar current = Calendar.getInstance();
            Calendar record = Calendar.getInstance();
            current.setTime(currentDate);
            record.setTime(recordDate);

            if (isSameDay(current, record)) {
                return new SimpleDateFormat("HH:mm").format(recordTime);
            }

            if (isYesterday(current, record)) {
                return "昨天  ";
            }

            return new SimpleDateFormat("yyyy-MM-dd").format(recordTime);

        } catch (Exception e) {
            return date;
        }

    }

}
