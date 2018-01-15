package com.glodon.bim.basic.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 描述：日期处理
 * 作者：zhourf on 2017/9/19
 * 邮箱：zhourf@glodon.com
 */

public class DateUtil {
    private static String FORMAT_BEFORE_YEAR_YMDHM = "yyyy-MM-dd HH:mm";
    private static String FORMAT_BEFORE_YEAR_YMD = "yyyy-MM-dd";
    private static String TODAY = "今天 ";
    private static String YESTERDAY = "昨天 ";

    /**
     * 获取日期时间
     */
    public static String getListTime(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            String time = dateLong + "";
            if (DateUtils.isToday(time)) { //今天
                str = new SimpleDateFormat("HH:mm:ss")
                        .format(new Date(Long.parseLong(time)));
                str = "今天 "+str;
            } else if (DateUtils.isYesterday(time)) {  //昨天
                String s_date = new SimpleDateFormat("HH:mm:ss")
                        .format(new Date(Long.parseLong(time)));
                str = "昨天 " + s_date;
            } else if (isSameYear(time)) {//今年
                str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(Long.parseLong(time)));
            } else {
                str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(Long.parseLong(time)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取日期时间
     */
    public static String getNormalTime(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            String time = dateLong + "";
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(Long.parseLong(time)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    /**
     * 获取日期时间
     */
    public static String getNormalDate(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            String time = dateLong + "";
            str = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(Long.parseLong(time)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }



    //获取日期
    public static String getListDate(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            String time = dateLong + "";
            if (DateUtils.isToday(time)) { //今天

                str = "今天";
            } else if (DateUtils.isYesterday(time)) {  //昨天
                str = "昨天";
            } else {
                str = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date(Long.parseLong(time)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getTime_Circle(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            String time = dateLong + "";
            if (DateUtils.isToday(time)) { //今天
                str = new SimpleDateFormat(DateUtils.FORMAT_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
            } else if (DateUtils.isYesterday(time)) {  //昨天
                String s_date = new SimpleDateFormat(DateUtils.FORMAT_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
                str = YESTERDAY + s_date;
            } else if (isSameYear(time)) {//今年
                str = new SimpleDateFormat(DateUtils.FORMAT_MONTH_DAY_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
            } else {
                str = new SimpleDateFormat(FORMAT_BEFORE_YEAR_YMDHM)
                        .format(new Date(Long.parseLong(time)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 是否为同一年
     */
    private static boolean isSameYear(String sdata) {
        if (sdata != null && sdata.length() > 0) {
            String year;
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
     * 圈子、活动frame页时间戳规则统一
     * 每条消息间隔不超过10分钟内，不显示时间，两条消息超过10分钟以上，显示一次时间。
     * ①当天的消息显示时间：今天 XX：XX 例如：今天 12:10
     * ②昨天的消息：昨天 XX：XX 例如：昨天 05:10
     * ③昨天以前直接显示日期：XX-XX XX:XX 例如：12-02 12:10
     * ④隔年则多加入年份：XXXX-XX-XX 例如：2014-12-02
     */

    public static String getTime_Frame_Circle(Long dateLong, Long lastDateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        if (lastDateLong != null && (dateLong - lastDateLong) < 10 * 60 * 1000) {//10分钟内不显示
            return str;
        }
        try {
            String time = dateLong + "";
            if (DateUtils.isToday(time)) { //今天
                str = new SimpleDateFormat(DateUtils.FORMAT_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
                str = TODAY + str;
            } else if (DateUtils.isYesterday(time)) {  //昨天
                String s_date = new SimpleDateFormat(DateUtils.FORMAT_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
                str = YESTERDAY + s_date;
            } else if (isSameYear(time)) {//今年
                str = new SimpleDateFormat(DateUtils.FORMAT_MONTH_DAY_HOUR_MINUTE)
                        .format(new Date(Long.parseLong(time)));
            } else {
                str = new SimpleDateFormat(FORMAT_BEFORE_YEAR_YMD)
                        .format(new Date(Long.parseLong(time)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 圈子、活动frame页时间戳规则统一
     * 每条消息间隔不超过10分钟内，不显示时间，两条消息超过10分钟以上，显示一次时间。
     * ①当天的消息显示时间：今天 XX：XX 例如：今天 12:10
     * ②昨天的消息：昨天 XX：XX 例如：昨天 05:10
     * ③昨天以前直接显示日期：XX-XX XX:XX 例如：12-02 12:10
     * ④隔年则多加入年份：XXXX-XX-XX 例如：2014-12-02
     */

    public static String getTime_Frame_Event(Long dateLong, Long lastDateLong) {
        if (dateLong != null) {
            dateLong = dateLong * 1000;
        }
        if (lastDateLong != null) {
            lastDateLong = lastDateLong * 1000;
        }
        return getTime_Frame_Circle(dateLong, lastDateLong);
    }


    /**
     * 圈子frame页时间戳规则：
     * ①当天的消息显示时间：XX：XX 例如：12:10
     * ②昨天的消息：昨天 XX：XX 例如：昨天 05:10
     * ③昨天以前直接显示日期：XX-XX XX:XX 例如：12-02 12:10
     * ④隔年则多加入年份：XXXX-XX-XX XX:XX 例如：2014-12-02 12:10
     * <p/>
     * 活动frame页时间戳规则：
     * 时间展示为活动开始时间；开始时间：XXXX-XX-XX XX:XX 例如：开始时间：2014-12-02 12:10
     * 排序规则：活动开始时间排序，开始时间越早，排序越靠前，展示前三条活动。
     */

    public static String getTime_Frame_Circle(Long dateLong) {
        return getTime_Circle(dateLong);
    }

    /**
     *  活动frame页时间戳规则：
     * 时间展示为活动开始时间；开始时间：XXXX-XX-XX XX:XX 例如：开始时间：2014-12-02 12:10
     * 排序规则：活动开始时间排序，开始时间越早，排序越靠前，展示前三条活动。
     * @param dateLong 时间
     * @return XXXX-XX-XX XX:XX
     */
    public static String getTime_Frame_Event(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            str = new SimpleDateFormat(FORMAT_BEFORE_YEAR_YMDHM)
                    .format(new Date(dateLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *  活动frame页时间戳规则：
     * 时间展示为活动开始时间；开始时间：XXXX-XX-XX XX:XX 例如：开始时间：2014-12-02 12:10
     * 排序规则：活动开始时间排序，开始时间越早，排序越靠前，展示前三条活动。
     * @param dateLong 时间
     * @return 开始时间：XXXX-XX-XX XX:XX
     */
    public static String getTime_Frame_Event_Prdfix(Long dateLong) {
        String str = "";
        if (dateLong == null || dateLong == 0) {
            return str;
        }
        try {
            str = new SimpleDateFormat(FORMAT_BEFORE_YEAR_YMDHM)
                    .format(new Date(dateLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "开始时间："+str;
    }

    /**
     * 赞数的处理
     * @param num 具体的数量
     * @return 处理后的值
     */
    public static String getLikeNum(int num){
        if(num==0){
            return "赞";
        }else{
            return getNum(num);
        }
    }
    /**
     * 评论数的处理
     * @param num 具体的数量
     * @return 处理后的值
     */
    public static String getCommentNum(int num){
        if(num==0){
            return "评论";
        }else{
            return getNum(num);
        }
    }
    /**
     * 分享数的处理
     * @param num 具体的数量
     * @return 处理后的值
     */
    public static String getShareNum(int num){
        if(num==0){
            return "分享";
        }else{
            return getNum(num);
        }
    }

    public static String getNum(int num){
        if(num<=9999){
            return String.valueOf(num);
        }else {
            if(num%10000==0){
                return num/10000+"万";
            }else{
                int number = num/1000;
                if(number%10==0){
                    return number/10+"万";
                }else{
                    return number/10.0f+"万";
                }
            }

        }
    }

    /**
     * 将毫秒数转为当前日
     */
    public static String getShowDate(String currentmiions){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(currentmiions)));
    }
}
