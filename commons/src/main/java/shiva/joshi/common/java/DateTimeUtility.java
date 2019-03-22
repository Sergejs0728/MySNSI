package shiva.joshi.common.java;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import shiva.joshi.common.LibApp;
import shiva.joshi.common.logger.AppLogger;

public final class DateTimeUtility {
    private static String TAG = DateTimeUtility.class.getName();
    public static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");
    //Local formatting String
    public static final String LOCAL_DF_TIME12_1 = "dd-MM-yyyy h:mm a";
    public static final String LOCAL_DF_TIME24_1 = "dd-MM-yyyy H:mm";

    public static final String LOCAL_DF_TIME12_COMMAS = "MMM dd,yyyy h:mm a";
    public static final String LOCAL_DF_TIME12_DD_MMM_YYYY_COMMAS = "dd MMM, yyyy h:mm a";
    public static final String LOCAL_DF_TIME24_COMMAS = "MMM dd,yyyy H:mm";

    //UTC formatted String
    public static final String UTC_DF_TIME24_1 = "yyyy-MM-dd H:mm";
    public static final String UTC_DF_TIME12_1 = "yyyy-MM-dd h:mm";


    //Simple Date Format
    public static final String DATE_F_MMM_DD_YYYY = "MMM-dd-yyyy";
    public static final String DATE_F_DD_MMM_YYYY = "dd MMM yyyy";
    public static final String DATE_F_MMM_DD_YYYY_COMMAS = "MMM dd,yyyy";
    public static final String DATE_F_DD_MMM_YYYY_COMMAS = "dd MMM, yyyy";
    public static final String DATE_F_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DATE_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_F_YYYY_MM_DD = "yyyy-MM-dd";
    /* private static final SimpleDateFormat SDF =
             new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
             Thu Apr 27 00:00:00 GMT+05:30 2017
             */
    //TIME FORMAT
    public static final String TIME_24 = "HH:mm";
    public static final String TIME_24_ss = "HH:mm:ss";
    public static final String TIME_12 = "hh:mm a";


    public static String convertUTCFormatToLocal(String utcDateString, String currentUtcDateFormat, String requiredDateFormat) {
        //Convert UTC : 2016-11-22 10:39 To local : 22-11-2016 4:09 PM
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentUtcDateFormat);
        simpleDateFormat.setTimeZone(utcTZ);
        Date date = null;
        try {
            date = simpleDateFormat.parse(utcDateString);
            utcDateString = new SimpleDateFormat(requiredDateFormat).format(date);
        } catch (ParseException ex) {
            LibApp.showException(TAG, ex);
        }
        return utcDateString;
    }

    public static String convertLocalToUTC(String utcDateString, String currentLocalDateFormat, String requiredDateFormat) {
        //Convert local to UTC format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentLocalDateFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(utcDateString);
            SimpleDateFormat utcDateFormat = new SimpleDateFormat(requiredDateFormat);
            utcDateFormat.setTimeZone(utcTZ);
            utcDateString = utcDateFormat.format(date);
        } catch (ParseException ex) {
            LibApp.showException(TAG, ex);
        }
        return utcDateString;
    }

    public static String getCurrentUTCDate(String requiredDateFormat) {
        //Get Current date in UTC FORMAT
        final Date currentTime = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat(requiredDateFormat);
        sdf.setTimeZone(utcTZ);
        return sdf.format(currentTime);
    }

    public static String getCurrentLocalDate(String requiredDateFormat) {
        //Get Current date in UTC FORMAT
        final Date currentTime = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat(requiredDateFormat);
        return sdf.format(currentTime);
    }

    public static String convertDateToRequiredFormat(Date date, String requiredDateFormat) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(requiredDateFormat);
            return sdf.format(date);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
        return getCurrentLocalDate(requiredDateFormat);
    }

    // change from dd-MM-yyyy to MM-dd-YYYY
    public static String convertDateFromOldToNewFormat(String dateString, String oldFormat, String newFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        try {
            Date date = sdf.parse(dateString);
            sdf.applyPattern(newFormat);
            return sdf.format(date);
        } catch (ParseException ex) {
            LibApp.showException(TAG, ex);
        }
        return dateString;
    }

    // Time conversion 24 HH to 12
    public static String convertTimeFormat(String timeString, String oldTimeFormat, String newTimeFormat) {
        try {
            SimpleDateFormat oldTimeFormatter = new SimpleDateFormat(oldTimeFormat);
            SimpleDateFormat newTimeFormatter = new SimpleDateFormat(newTimeFormat);
            Date newDate = oldTimeFormatter.parse(timeString);
            return newTimeFormatter.format(newDate);
        } catch (Exception ex) {
            LibApp.showException(TAG, ex);
        }
        return timeString;
    }

    public static String buildDate(int y, int m, int d, String requiredFormat) {
        String buildSting = d + "-" + (m + 1) + "-" + y;
        return convertDateFromOldToNewFormat(buildSting, DATE_F_DD_MM_YYYY, requiredFormat);
    }

    public static String buildTime(int h, int m, String requiredFormat) {
        String buildSting = h + ":" + m;
        return convertTimeFormat(buildSting, TIME_24, requiredFormat);
    }

    public static int getTimeDifference(String time1, String time2, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        try {
            Date Date1 = format.parse(time1);
            Date Date2 = format.parse(time2);
            long mills = Date2.getTime() - Date1.getTime();
            return /*Math.abs(*/(int) (mills / (1000 * 60))/*)*/;

        } catch (Exception ex) {

        }
        return 0;
    }

    public static String getCurrentTime(String time, String currentFormat, String requiredFormat, int extendedHours, int min) {
        //Get Current date in UTC FORMAT
        SimpleDateFormat currentSdf = new SimpleDateFormat(currentFormat);
        Date date;
        if (time != null && !time.isEmpty()) {
            try {
                date = currentSdf.parse(time);
            } catch (ParseException e) {
                date = new Date();
            }
        } else {
            date = new Date();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, extendedHours);
        calendar.add(Calendar.MINUTE, min);
        SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat);
        try {
            return sdf.format(calendar.getTime());
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

        return sdf.format(date);
    }


}