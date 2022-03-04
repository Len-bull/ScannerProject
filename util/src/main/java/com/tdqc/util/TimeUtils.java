package com.tdqc.util;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关
 * Created by chenyen on 2016/12/8.
 */
public class TimeUtils {

    private final static String TAG = "TimeUtils";

    /** 时间单位枚举，getTime()获取相应单位毫秒数 */
    public enum TimeType {
        MSEC(1),
        SEC(1000),
        MIN(60000),
        HOUR(3600000),
        DAY(86400000);
        long time;

        TimeType(int i) {
            time = i;
        }

        public long getTime() {
            return time;
        }
    }

    /** 时间格式枚举 */
    public enum TimeFormat {
        /** yyyy-MM-dd HH:mm:ss */
        YYYY_MM_DD_HH_MM_SS(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)),
        /** yyyy年MM月dd日 HH:mm:ss */
        CN_YYYY_MM_DD_HH_MM_SS(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA)),
        /** MM月dd日 HH:mm */
        CN_MM_DD_HH_MM(new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)),
        /** yyyy-MM-dd */
        YYYY_MM_DD(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)),
        /** MM-dd HH:mm */
        MM_DD_HH_MM(new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)),
        /** HH:mm:ss */
        HH_MM_SS(new SimpleDateFormat("HH:mm:ss", Locale.CHINA)),
        /** HH:mm */
        HH_MM(new SimpleDateFormat("HH:mm", Locale.CHINA));

        SimpleDateFormat value = null;

        TimeFormat(SimpleDateFormat format) {
            value = format;
        }

        public SimpleDateFormat getValue() {
            return value;
        }
    }

    /**
     * 获取当前时间
     *
     * @param timeFormat 返回时间格式
     */
    public static synchronized String getCurrentTimeStr(@NonNull TimeFormat timeFormat) {
        return getTimeStr(System.currentTimeMillis(), timeFormat);
    }

    /**
     * 获取指定时间字符串
     *
     * @param timeFormat 返回时间格式
     */
    public static synchronized String getTimeStr(long timeLong, @NonNull TimeFormat timeFormat) {
        return timeFormat.getValue().format(new Date(timeLong));
    }

    /**
     * 获取指定时间的long时间戳
     *
     * @param timeStr    字符串形式的时间
     * @param timeFormat 传入的timeStr时间的格式
     * @return long时间戳，解析失败返回0
     */
    public static synchronized long getTimeLong(String timeStr, @NonNull TimeFormat timeFormat) {
        long result = 0;
        try {
            result = timeFormat.getValue().parse(timeStr).getTime();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 获取指定时间的calendar对象
     *
     * @param timeStr    指定时间字符串
     * @param timeFormat 传入的时间字符串格式
     * @return calendar
     */
    public static synchronized Calendar getCalendar(String timeStr, @NonNull TimeFormat timeFormat) {
        return getCalendar(getTimeLong(timeStr, timeFormat));
    }

    /**
     * 获取指定时间的calendar对象
     *
     * @param timeLong 指定时间
     * @return calendar
     */
    public static synchronized Calendar getCalendar(long timeLong) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(timeLong);
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 倒计时转化剩余毫秒数成分秒格式
     *
     * @param millisUntilFinished 毫秒数
     * @return xx小时xx分xx秒
     */
    public static String getDownCountStr(long millisUntilFinished) {
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        String dateStr = "";
        if (millisUntilFinished / DateUtils.HOUR_IN_MILLIS > 0) {
            hours = millisUntilFinished / DateUtils.HOUR_IN_MILLIS;
            dateStr = hours + "小时";
        }
        millisUntilFinished -= hours * DateUtils.HOUR_IN_MILLIS;
        if (millisUntilFinished / DateUtils.MINUTE_IN_MILLIS > 0) {
            minutes = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
            dateStr += minutes + "分";
        }
        millisUntilFinished -= minutes * DateUtils.MINUTE_IN_MILLIS;
        if (millisUntilFinished / DateUtils.SECOND_IN_MILLIS > 0) {
            seconds = millisUntilFinished / DateUtils.SECOND_IN_MILLIS;
            dateStr += seconds + "秒";
        }
        return dateStr;
    }

    /**
     * 倒计时转化剩余毫秒数成分秒格式
     *
     * @param millisUntilFinished 毫秒数
     * @return 1:25:32
     */
    public static String getDownCount2(long millisUntilFinished) {
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        StringBuffer buffer = new StringBuffer();
        if (millisUntilFinished / DateUtils.HOUR_IN_MILLIS > 0) {
            hours = millisUntilFinished / DateUtils.HOUR_IN_MILLIS;
            buffer.append(hours).append(":");
        }
        millisUntilFinished -= hours * DateUtils.HOUR_IN_MILLIS;
        if (millisUntilFinished / DateUtils.MINUTE_IN_MILLIS > 0) {
            minutes = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
            if (minutes < 10) {
                buffer.append("0").append(minutes).append(":");
            } else {
                buffer.append(minutes).append(":");
            }
        } else {
            buffer.append("00:");
        }
        millisUntilFinished -= minutes * DateUtils.MINUTE_IN_MILLIS;
        if (millisUntilFinished / DateUtils.SECOND_IN_MILLIS > 0) {
            seconds = millisUntilFinished / DateUtils.SECOND_IN_MILLIS;
            if (seconds < 10) {
                buffer.append("0").append(seconds);
            } else {
                buffer.append(seconds);
            }
        } else {
            buffer.append("00");
        }
        return buffer.toString();
    }

    /**
     * 倒计时转化剩余毫秒数成分秒格式
     *
     * @param millisUntilFinished 毫秒数
     * @return 1:25:32
     */
    public static String getDownCount(long millisUntilFinished) {
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        StringBuffer buffer = new StringBuffer();
        if (millisUntilFinished / DateUtils.HOUR_IN_MILLIS > 0) {
            hours = millisUntilFinished / DateUtils.HOUR_IN_MILLIS;
            buffer.append(hours).append(":");
        }
        millisUntilFinished -= hours * DateUtils.HOUR_IN_MILLIS;
        if (millisUntilFinished / DateUtils.MINUTE_IN_MILLIS > 0) {
            minutes = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
            if (minutes < 10) {
                buffer.append("0").append(minutes).append(":");
            } else {
                buffer.append(minutes).append(":");
            }
        } else {
            buffer.append("00:");
        }
        millisUntilFinished -= minutes * DateUtils.MINUTE_IN_MILLIS;
        if (millisUntilFinished / DateUtils.SECOND_IN_MILLIS > 0) {
            seconds = millisUntilFinished / DateUtils.SECOND_IN_MILLIS;
            if (seconds < 10) {
                buffer.append("0").append(seconds);
            } else {
                buffer.append(seconds);
            }
        } else {
            buffer.append("00");
        }
        return buffer.toString();
    }

    /**
     * 获取友好的时间格式
     * 生成规则：
     * 1分钟内：刚刚
     * 一小时内：X分钟前
     * 一天内：X小时前
     * 昨天： 昨天 HH:MM
     * 前天： 前天HH:MM
     * 前天之前： YYYY:MM:DD HH:MM
     */
    public static synchronized String getFriendlyTime(long timeLong) {
        if (timeLong == 0) {
            return "";
        }
        String result = "";
        try {
            if ((timeLong + "").length() == 10) {
                timeLong = timeLong * 1000;
            }
            Date tagetDate = new Date(timeLong);
            long nowTime = System.currentTimeMillis();
            // 判断是否是同一天
            String nowDateStr = TimeFormat.YYYY_MM_DD.getValue().format(new Date(nowTime));
            String tagetDateStr = TimeFormat.YYYY_MM_DD.getValue().format(tagetDate);
            if (nowDateStr.equals(tagetDateStr)) {
                int hour = (int) ((nowTime - timeLong) / 3600000);
                long minuteAgo = (nowTime - timeLong) / 60000;
                return hour == 0 ? (minuteAgo == 0 ? "刚刚" : (minuteAgo + "分钟前")) : (hour + "小时前");
            }
            long lt = timeLong / 86400000;
            long ct = nowTime / 86400000;
            int days = (int) (ct - lt);
            String tagetHHmm = TimeFormat.HH_MM.getValue().format(tagetDate);
            if (days == 1) {
                result = "昨天 " + tagetHHmm;
            } else if (days == 2) {
                result = "前天 " + tagetHHmm;
            } else {
                result = tagetDateStr + " " + tagetHHmm;
            }
        } catch (Exception e) {
            L.e(TAG, e);
        }
        return result;
    }
    /**获取几天前的日期*/
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    // 获得本月第一天0点时间
    public static String getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date today = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }


    /**获取几天前是几号*/
    public static String getPastDateNum(int mday) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - mday);
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        return i+"";
    }

    /**
     * 取得几天前是周几
     * */
    public static int getPastDayWeek(int past)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }



    /**
     * 取得当月天数
     * */
    public static int getCurrentMonthLastDay()
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    /**
     * 取得当日周几
     * */
    public static int getCurrentMonthOneDayWeek()
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        int week = a.get(Calendar.DAY_OF_WEEK);
        return week;
    }


}
