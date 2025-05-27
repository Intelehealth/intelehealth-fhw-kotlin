package org.intelehealth.common.utility;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vaghela Mithun R. on 03-08-2023 - 19:38.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
public class DateTimeUtils {
    public static final String TAG = "DateTimeUtils";
    public static final String DB_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    public static final String MESSAGE_TIME_FORMAT = "h:mm a";
    public static final String MESSAGE_DAY_FORMAT = "EEE, dd MMM yyyy";
    public static final String TIME_ZONE_UTC = "UTC";
//    public static final String TIME_ZONE_ISD = "Asia/Kolkata";

    public static final String TIME_FORMAT = "hh:mm a";

    public static final String YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";
    //    public static final String YYYY_MM_DD_WITH_SPLASH = "dd/MM/yyyy";
//
    public static final String MMM_DD_YYYY_FORMAT = "MMM dd, yyyy";

    public static final String CALL_LOG_TIME_FORMAT = "EEE, dd MMM yyyy HH:mm:ss";
    public static final String LAST_SYNC_DB_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LAST_SYNC_DISPLAY_FORMAT = "hh:mm a dd MMM yyyy";
    public static final String USER_DOB_DB_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
            //1997-10-20T00:00:00.000+0530

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getSimpleDateFormat(String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (timeZone != null)
            sdf.setTimeZone(timeZone);
        return sdf;
    }

//    public static String formatDate(String date, String format) {
//
//    }

    public static String formatToLocalDate(Date date, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format, TimeZone.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = getSimpleDateFormat(format, timeZone);
        return sdf.format(date);
    }

    public static Date parseDate(String date, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = getSimpleDateFormat(format, timeZone);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "parseDate: ", e);
            return getCurrentDate(getUTCTimeZone());
        }
    }

    public static Date parseUTCDate(String date, String format) {
        return parseDate(date, format, getUTCTimeZone());
    }

    public static Date getCurrentDate(TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(timeZone);
        return calendar.getTime();
    }
    public static Date getCustomDate(TimeZone timeZone, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(timeZone);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }


    public static String getCurrentDateWithDBFormat() {
        SimpleDateFormat sdf = getSimpleDateFormat(DB_FORMAT, getUTCTimeZone());
        return sdf.format(getCurrentDate(getUTCTimeZone()));
    }

    public static String getCustomDateWithDBFormat(int days) {
        SimpleDateFormat sdf = getSimpleDateFormat(DB_FORMAT, getUTCTimeZone());
        return sdf.format(getCustomDate(getUTCTimeZone(),days));
    }

    public static boolean isToday(Date date) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return LocalDate.now().compareTo(new LocalDate(date.getTime())) == 0;
//        } else {
        return DateUtils.isToday(date.getTime());
//        }
    }

    public static boolean isYesterday(Date date) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return LocalDate.now().compareTo(LocalDate) == 0;
//        } else {
        return DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS);
//        }
    }

    public static String utcToLocalDate(String utcDate, String utcFormat, String localFormat) {
        SimpleDateFormat localSdf = getSimpleDateFormat(localFormat, TimeZone.getDefault());
        Date date = parseUTCDate(utcDate, utcFormat);
        return localSdf.format(date);
    }

    public static String utcToLocalDate(Date utcDate, String localFormat) {
        SimpleDateFormat localSdf = getSimpleDateFormat(localFormat, TimeZone.getDefault());
        return localSdf.format(utcDate);
    }

    public static Date utcToLocalDate(String utcDate, String utcFormat) {
        Date date = parseUTCDate(utcDate, utcFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(date.getTime());
        return calendar.getTime();
    }

    public static String getCurrentDateInUTC(String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format, getUTCTimeZone());
        return sdf.format(getCurrentDate(getUTCTimeZone()));
    }

    public static long getMinDiffWithCurrentDate(String date, String format) {
        Date encounterDate = DateTimeUtils.parseUTCDate(date, format);
        Date currentDate = DateTimeUtils.getCurrentDate(getUTCTimeZone());
        long difference = currentDate.getTime() - encounterDate.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(difference);
    }

    public static TimeZone getUTCTimeZone() {
        return TimeZone.getTimeZone(TIME_ZONE_UTC);
    }
}
