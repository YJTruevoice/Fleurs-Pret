package com.arthur.commonlib.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * @author biaodi
 * 时间格式化工具
 */
public class DateUtil {

    public static Calendar getNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public static Calendar getTarget(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(source);
            Calendar calendar = Calendar.getInstance();
            if (date != null) {
                calendar.setTime(date);
                return calendar;
            } else {
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseReverseDateStr(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(source);
            if (date != null) {
                return simpleDateFormat.format(date);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long targetTimeMillis(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(source);
            if (date != null) {
                return date.getTime();
            } else {
                return 0L;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
        String week = sdf.format(date).replace("星期", "周");
        return week;
    }

    public static String getDayFormatStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getSecondFormatStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
    public static String getSecondFormatStr(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(new Date(time));
    }

    public static String getSecondFormatStrV2(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getDayFormatStrMD(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getHomeFormatStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String getHourFormatStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * 根据日期获取 年/月/日/时/分/秒
     *
     * @param source   传入的日期数据
     * @param timeType 传入的时间类型代码：{@link Calendar#YEAR,Calendar#MONTH,Calendar#DAY_OF_MONTH}
     * @return 返回 年/月/日/时/分/秒
     */
    public static int getYearFromFormatStr(String source, int timeType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(source, Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(source);
            Calendar calendar = Calendar.getInstance();
            if (date != null) {
                calendar.setTime(date);
                return calendar.get(timeType);
            } else {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getDateWithFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    public static String getDateWithFormat(long timeMillis, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static int getDayDiffer(Date startDate, Date endDate) throws ParseException {
        //判断是否跨年
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String startYear = yearFormat.format(startDate);
        String endYear = yearFormat.format(endDate);
        if (startYear.equals(endYear)) {
            /*  使用Calendar跨年的情况会出现问题    */
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int startDay = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(endDate);
            int endDay = calendar.get(Calendar.DAY_OF_YEAR);
            return endDay - startDay;
        } else {
            /*  跨年不会出现问题，需要注意不满24小时情况（2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 0）  */
            //  只格式化日期，消除不满24小时影响
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long startDateTime = Objects.requireNonNull(dateFormat.parse(dateFormat.format(startDate))).getTime();
            long endDateTime = Objects.requireNonNull(dateFormat.parse(dateFormat.format(endDate))).getTime();
            return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
        }
    }

    /**
     * 判断当前系统时间是否在指定时间段内
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        // 跨天的特殊情况(比如22:00-8:00)
        if (!startTime.before(endTime)) {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            //普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    //是不是今天
    public static boolean isToday(Date date) {
        long time = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        int tmp = 86400000;
        return time > today && time - today < tmp;
    }

    //是不是昨天
    public static boolean isYesterday(Date date) {
        long time = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        int tmp = 86400000;
        return today > time && today - time < tmp;
    }

    // 是不是今年
    public static boolean isToYear(Date date) {
        if (date == null) {
            return false;
        }
        Calendar now = getNow();
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        return now.get(Calendar.YEAR) == cDate.get(Calendar.YEAR);
    }
}
