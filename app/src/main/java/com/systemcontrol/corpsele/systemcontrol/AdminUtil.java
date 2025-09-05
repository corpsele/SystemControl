package com.systemcontrol.corpsele.systemcontrol;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.time.temporal.ChronoUnit;

public class AdminUtil extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        //设备管理可用
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        //设备管理不可用
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
    }

}

class GlobalUtil {
    public static boolean isEnableRepeatService = false;
    public static boolean isMainServiceRunning = false;



    public static String getCurrentDateTimeInChina() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime now = LocalDateTime.now(zoneId);

        // 格式化日期和时间
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINA);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.CHINA);

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        // 获取星期几
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String weekDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA);

        return String.format("%s %s %s", date, weekDay, time);
    }

    public static String getWeekDayInChina() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime now = LocalDateTime.now(zoneId);
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA);
    }

    public static String getWeekDayInChinaLegacy() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Calendar calendar = Calendar.getInstance(timeZone);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[dayOfWeek];
    }

    public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(new Date(timestamp));
    }
}

class DateTimeUtils {

    // 获取当前中国时间
    public static String getCurrentDateTime() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime now = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return now.format(formatter);
    }

    // 日期加减天数
    public static String addDaysToDate(int days) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate today = LocalDate.now(zoneId);
        LocalDate newDate = today.plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINA);
        return newDate.format(formatter);
    }

    // 计算两个日期之间的天数差
    public static long daysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    // 时间加减小时
    public static String addHoursToTime(int hours) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime now = LocalDateTime.now(zoneId);
        LocalDateTime newTime = now.plusHours(hours);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.CHINA);
        return newTime.format(formatter);
    }

    // 计算两个时间之间的分钟差
    public static long minutesBetweenTimes(LocalDateTime startTime, LocalDateTime endTime) {
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    // 判断是否是同一天
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    // 获取当前星期几
    public static String getCurrentDayOfWeek() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate today = LocalDate.now(zoneId);
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA);
    }
}

class DateTimeUtilsLegacy {

    // 获取当前中国时间
    public static String getCurrentDateTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        sdf.setTimeZone(timeZone);
        return sdf.format(calendar.getTime());
    }

    // 日期加减天数
    public static String addDaysToDate(int days) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        sdf.setTimeZone(timeZone);
        return sdf.format(calendar.getTime());
    }

    // 计算两个日期之间的天数差
    public static long daysBetweenDates(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return diffInMillis / (1000 * 60 * 60 * 24);
    }

    // 时间加减小时
    public static String addHoursToTime(int hours) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        sdf.setTimeZone(timeZone);
        return sdf.format(calendar.getTime());
    }

    // 获取当前星期几
    public static String getCurrentDayOfWeek() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Calendar calendar = Calendar.getInstance(timeZone);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekDays[dayOfWeek - 1];
    }
}