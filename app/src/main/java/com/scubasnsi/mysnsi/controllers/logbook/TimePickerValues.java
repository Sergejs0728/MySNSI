package com.scubasnsi.mysnsi.controllers.logbook;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import shiva.joshi.common.logger.AppLogger;

/**
 * Author -
 * Date -  27-04-2017.
 */

public abstract class TimePickerValues extends Fragment {

    private final String AM = "AM";
    private final String PM = "PM";
    public final int MAX_HOURS = 12;
    public final int MAX_MINUTES = 60;
    public final int MAX_ITEM_HEIGHT = 80;
    public final int ONE_HOURS_CYCLE_OFF_SET = 80 * MAX_HOURS;


    public List<String> getHours() {
        List list = new ArrayList();
        for (int i = 1; i <= MAX_HOURS; i++) {
            list.add(i);
        }
        return list;
    }

    public List<String> getMinutes() {
        List list = new ArrayList();
        for (int i = 0; i < MAX_MINUTES; i++) {
            StringBuffer s = new StringBuffer(i + "");
            if (i < 10) {
                s.insert(0, "0");
            }
            list.add(s.toString());
        }
        return list;
    }


    public List<String> getAMPM() {
        List list = new ArrayList();
        list.add(AM);
        list.add(PM);

        return list;
    }

    // get selected time interval
    public int[] getSelectedTimeInterval(String time) {
        int[] timeInterval = {0, 0, 0};
        if (time == null && time.isEmpty())
            return timeInterval;
        int hours = Integer.parseInt(time.substring(0, 2)) - 1;
        int min = Integer.parseInt(time.substring(3, 5));
        String am = time.substring(6, 8);
        int amIndex = 0;
        if (am.equalsIgnoreCase(PM)) {
            amIndex = 1;
        }
        timeInterval[0] = hours;
        timeInterval[1] = min;
        timeInterval[2] = amIndex;

        return timeInterval;
    }

    //get Selected Date
    public int[] getSelectedDate(String date) { //dd-mm-yy
        int[] timeInterval = {0, 0, 0};
        if (date == null || date.isEmpty())
            return timeInterval;
        try {
            String[] dateArray = date.split("-");
            for (int i = 0; i < dateArray.length; i++) {
                timeInterval[i] = Integer.parseInt(dateArray[i]);
            }
        } catch (Exception ex) {
            AppLogger.Logger.error("selected date", ex.getMessage(), ex);
        }

        return timeInterval;
    }


    public String buildTime(String hour, String min, String am, String defaultTime) {
        if ((hour != null && !hour.isEmpty()) && (min != null && !min.isEmpty()) && (am != null && !am.isEmpty()))
            return hour + ":" + min + " " + am;

        return defaultTime;
    }

}
