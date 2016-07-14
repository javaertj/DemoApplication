package com.drivingassisstantHouse.library.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化工具类
 *
 * @author sunji
 * @version 1.0
 **/
public class ToolDateTime {

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式：yyyy.MM.dd
     **/
    public static final String DF_YYYY_MM_DD2 = "yyyy.MM.dd";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";
    /**
     * 日期 11月11日 21:00
     **/
    public static final String DF_MM_DD_HH_MM = "MM月dd日 HH:mm";
    /**
     * 日期格式：yyyy年MM月dd日
     **/
    public static final String DF_CHINESE_YYYY_MM_DD = "yyyy年MM月dd日";

    /**
     * 日期格式：yyyy年MM月dd日 HH:mm
     **/
    public static final String DF_CHINESE_YYYY_MM_DD_HH_MM_ = "yyyy年MM月dd日 HH:mm";

    /**
     * 日期格式：yyyy年MM月dd日 HH时mm分
     **/
    public static final String DF_CHINESE_YYYY_MM_DD_HH_MM = "yyyy年MM月dd日 HH时mm分";

    /**
     * 日期格式：yyyyMMddHH:mm:ss
     **/
    public static final String DF_YYYYMMDDHHMMSS = "yyyyMMddHH:mm:ss";



    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    public final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * Log输出标识
     **/
    private static final String TAG = ToolDateTime.class.getSimpleName();

    /**
     * 根据秒数生成HH:mm格式字符串
     *
     * @param seconds
     * @return
     */
    public static String createTimeStr(int seconds) {
        int hour = seconds / 3600;
        int minute = seconds / 60 % 60;
        String hourStr = hour < 10 ? "0".concat(String.valueOf(hour)) : String.valueOf(hour);
        String minuteStr = minute < 10 ? "0".concat(String.valueOf(minute)) : String.valueOf(minute);

        return String.format("%s:%s", hourStr, minuteStr).concat(hour >= 12 ? "pm" : "am");
    }

    /******************************日期格式化(互相转换)****************************************/

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    public static String formatDateTime(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(dateL);
        return sdf.format(date);
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    public static String formatDateTime(long dateL, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(new Date(dateL));
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param date 日期
     * @return
     */
    public static String formatDateTime(Date date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(date);
    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return 成功则返回java.util.date日期类型，其他则返回null
     */
    public static Date parseDate(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return returnDate;

    }

    /**
     * 将指定格式的日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @param formate 日期格式
     * @return 成功则返回相应的Date对象，其他则返回null
     */
    public static Date parseDate(String strDate, String formate) {
        Date result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
        try {
            result = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***********************************获取当前日期***********************************/
    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static Date gainCurrentDate() {
        return new Date();
    }

    /**
     * 获取系统当前日期(字符串)
     *
     * @return
     */
    public static String gainCurrentDate(String formater) {
        return formatDateTime(new Date(), formater);
    }
    /**
     * 获取系统当前日期的明天(字符串)
     *
     * @return
     */
    public static  String gainTomorrowDate(String formater){
        Date date=new Date();
        Calendar calendar=Calendar.getInstance(Locale.CHINESE);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return  formatDateTime(calendar.getTime(), formater);
    }


    /**********************************日期的比较，加减等操作**************************************/

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 验证日期是否比当前日期早
     *
     * @param target1 比较时间1
     * @param target2 比较时间2
     * @return true 则代表target1比target2晚或等于target2，否则比target2早
     */
    public static boolean compareDate(Date target1, Date target2) {
        boolean flag = false;
        try {
            String target1DateTime = ToolDateTime.formatDateTime(target1,
                    DF_YYYY_MM_DD_HH_MM_SS);
            String target2DateTime = ToolDateTime.formatDateTime(target2,
                    DF_YYYY_MM_DD_HH_MM_SS);
            if (target1DateTime.compareTo(target2DateTime) <= 0) {
                flag = true;
            }
        } catch (Exception e1) {
            System.out.println("比较失败，原因：" + e1.getMessage());
        }
        return flag;
    }

    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }


    /**
     * 根据一个日期，返回是该日期对应的星期几的索引
     *
     * @param sdate  日期字符串
     * @param format 日期字符串的格式
     * @return 若成功则获取到1-7，其他则返回-1
     */
    private static int getWeek(String sdate, String format) {
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        // 这就是直接返回星期几：星期日
        // return new SimpleDateFormat("EEEE").format(c.getTime());
        Date date = parseDate(sdate, format);
        if (date == null)
            return -1;
        Calendar c = Calendar.getInstance(Locale.CHINESE);
        c.setTime(date);
        int dates = c.get(Calendar.DAY_OF_WEEK);
        return dates;

    }

    /**
     * 根据一个日期，返回是该日期对应的星期几的字符串
     *
     * @param sdate  日期字符串
     * @param format 日期字符串的格式
     * @return 若成功则获取到星期一到星期天的字符串，其他则返回空串
     */
    public static String getWeekStr(String sdate, String format) {
        String str = "";
        int index = getWeek(sdate, format);
        switch (index) {
            case 1:
                str = "星期天";
                break;
            case 2:
                str = "星期一";
                break;
            case 3:
                str = "星期二";
                break;
            case 4:
                str = "星期三";
                break;
            case 5:
                str = "星期四";
                break;
            case 6:
                str = "星期五";
                break;
            case 7:
                str = "星期六";
                break;
        }
        return str;
    }


    /**
     * 判断某个日期是否在指定时间区间内
     *
     * @param date      要比较的日期
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @param format    日期格式
     * @return true，date在
     */
    public boolean isBetween(String date, String startDate, String endDate, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date1 = dateFormat.parse(date);
        Date sdate = dateFormat.parse(startDate);
        Date edate = dateFormat.parse(endDate);
        return date1.after(sdate) && date1.before(edate);
    }

    /**
     * 判断某个日期是否在指定时间之后
     *
     * @param date   指定日期
     * @param sdate  要比较的日期
     * @param format 日期格式
     * @return true，date在sdate之前
     */
    public boolean isAfter(String date, String sdate, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date1 = dateFormat.parse(date);
        Date date2 = dateFormat.parse(sdate);
        return date1.before(date2);
    }

    /**
     * 判断某个日期是否在指定时间之前
     *
     * @param date  指定日期
     * @param sdate 要比较的日期
     * @return
     */
    public boolean isBefore(String date, String sdate, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date1 = dateFormat.parse(date);
        Date date2 = dateFormat.parse(sdate);
        return date1.after(date2);
    }


}
