package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.controllers.logbook.TimePickerValues;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.java.DateTimeUtility;
import shiva.joshi.common.utilities.CommonUtilities;

import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_START_TIME;
import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;
import static shiva.joshi.common.java.DateTimeUtility.convertDateFromOldToNewFormat;

public class DiveDateFragment extends TimePickerValues implements OnNextListener {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveDateFragment";

    private Context mContext;
    private UserSession mUserSession;


    @BindString(R.string.dive_date_title)
    protected String mTitle;

    /* Start Time */
    @BindView(R.id.main_wheel_hours)
    protected WheelPicker mHours;
    @BindView(R.id.main_wheel_minutes)
    protected WheelPicker mMinutes;
    @BindView(R.id.main_wheel_AM_PM)
    protected WheelPicker mAmPm;

    /* End Time */
    @BindView(R.id.dive_end_time_hours)
    protected WheelPicker mDiveEndHours;
    @BindView(R.id.dive_end_time_minutes)
    protected WheelPicker mDiveEndMinutes;
    @BindView(R.id.dive_end_time_AM_PM)
    protected WheelPicker mDiveEndAmPm;

    @BindView(R.id.logback_dive_date_picker)
    protected WheelDatePicker mWheelDatePicker;


    private Logbook mLogbook;
    private UpdateLogbookHeader mUpdateLogbookHeader;
    private int timeStartOffset = 0;


    public static DiveDateFragment newInstance(Logbook logbook) {

        DiveDateFragment diveDateFragment = new DiveDateFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        diveDateFragment.setArguments(args);

        return diveDateFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mUpdateLogbookHeader != null)
            mUpdateLogbookHeader.OnUpdateHeader(false, mTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLogbookHeader) {
            mUpdateLogbookHeader = (UpdateLogbookHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateHomeHeader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_dive_date_time, container, false);
        ButterKnife.bind(this, view);
        mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);

        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }
        initializeTimePicker();
        return view;

    }


    private void initializeTimePicker() {
        mWheelDatePicker.setCyclic(true);
        mWheelDatePicker.setCurved(true);
        mWheelDatePicker.setItemTextColor(CommonUtilities.getColor(mContext, R.color.wheel_normal_text_color));
        mWheelDatePicker.setSelectedItemTextColor(CommonUtilities.getColor(mContext, R.color.wheel_selected_text_color));
        mWheelDatePicker.setAtmospheric(true);


        mHours.setData(getHours());
        mMinutes.setData(getMinutes());
        mAmPm.setData(getAMPM());

        mDiveEndHours.setData(getHours());

        mDiveEndMinutes.setData(getMinutes());
        mDiveEndAmPm.setData(getAMPM());

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String date;
        if (!mLogbook.getDiveDate().isEmpty())
            date = convertDateFromOldToNewFormat(mLogbook.getDiveDate(), DateTimeUtility.DATE_F_YYYY_MM_DD, DateTimeUtility.DATE_F_DD_MM_YYYY);
        else
            date = DateTimeUtility.getCurrentLocalDate(DateTimeUtility.DATE_F_DD_MM_YYYY);


        // Update and converting UTC date to local
        final String startTime;
        final String endTime;
        if (!mLogbook.getDiveStartTime().isEmpty()) {
            startTime = DateTimeUtility.convertUTCFormatToLocal(mLogbook.getDiveStartTime(), DateTimeUtility.TIME_24_ss, DateTimeUtility.TIME_12);
        } else {
            startTime = DateTimeUtility.getCurrentTime(DEFAULT_START_TIME, DateTimeUtility.TIME_12, DateTimeUtility.TIME_12, 0, 0);
        }

        if (!mLogbook.getDiveEndTime().isEmpty()) {
            endTime = DateTimeUtility.convertUTCFormatToLocal(mLogbook.getDiveEndTime(), DateTimeUtility.TIME_24_ss, DateTimeUtility.TIME_12);
        } else {
            endTime = DateTimeUtility.getCurrentTime(DEFAULT_START_TIME, DateTimeUtility.TIME_12, DateTimeUtility.TIME_12, 1, 0);
        }

        mHours.post(new Runnable() {
            @Override
            public void run() {
                setStartTime(startTime);
                setEndTime(endTime);
                setDate(date);
            }
        });

    }


    // Set Date
    private void setDate(String date) {
        int[] dateArray = getSelectedDate(date);
        mWheelDatePicker.setSelectedDay(dateArray[0]);
        mWheelDatePicker.setYearAndMonth(dateArray[2], dateArray[1]);

    }

    private int preOffset = 0;

    private void setStartTime(String startTime) {
        //Set Time
        int[] timeStartInterval = getSelectedTimeInterval(startTime);

        mHours.setSelectedItemPosition(timeStartInterval[0]);
        timeStartOffset = timeStartInterval[0] * MAX_ITEM_HEIGHT;

        mMinutes.setSelectedItemPosition(timeStartInterval[1]);
        mAmPm.setSelectedItemPosition(timeStartInterval[2]);
    }


    private void setEndTime(String endTime) {
        //Set Time
        int[] timeStartInterval = getSelectedTimeInterval(endTime);

        mDiveEndHours.setSelectedItemPosition(timeStartInterval[0]);
        mDiveEndMinutes.setSelectedItemPosition(timeStartInterval[1]);
        mDiveEndAmPm.setSelectedItemPosition(timeStartInterval[2]);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }


    @Override
    public Logbook next() {
        //Done with date
        String date = DateTimeUtility.convertDateToRequiredFormat(mWheelDatePicker.getCurrentDate(), DateTimeUtility.DATE_F_YYYY_MM_DD);
        Log.e("date",">>"+ date);
        mLogbook.setDiveDate(date);


        // start Time

        String startTimeHours = String.valueOf(mHours.getData().get(mHours.getCurrentItemPosition()));
        String startTimeMinutes = String.valueOf(mMinutes.getData().get(mMinutes.getCurrentItemPosition()));
        String startTimeAm = String.valueOf( mAmPm.getData().get(mAmPm.getCurrentItemPosition()));
        String startTime = buildTime(startTimeHours, startTimeMinutes, startTimeAm,
                DateTimeUtility.getCurrentTime(DEFAULT_START_TIME, DateTimeUtility.TIME_12, DateTimeUtility.TIME_12, 0, 0));
        //Convert to UTC
        startTime = DateTimeUtility.convertLocalToUTC(startTime, DateTimeUtility.TIME_12, DateTimeUtility.TIME_24_ss);
        mLogbook.setDiveStartTime(startTime);

        // END Time
        String endTimeHours = String.valueOf(mDiveEndHours.getData().get(mDiveEndHours.getCurrentItemPosition()));
        String endTimeMinutes = String.valueOf(mDiveEndMinutes.getData().get(mDiveEndMinutes.getCurrentItemPosition()));
        String endTimeAm = String.valueOf(mDiveEndAmPm.getData().get(mDiveEndAmPm.getCurrentItemPosition()));
        String endTime = buildTime(endTimeHours, endTimeMinutes, endTimeAm,
                DateTimeUtility.getCurrentTime(DEFAULT_START_TIME, DateTimeUtility.TIME_12, DateTimeUtility.TIME_12, 0, 1));
        endTime = DateTimeUtility.convertLocalToUTC(endTime, DateTimeUtility.TIME_12, DateTimeUtility.TIME_24_ss);
        mLogbook.setDiveEndTime(endTime);


        String totalTime = "0";
        if (!endTime.isEmpty() && !startTime.isEmpty()) {
            totalTime = String.valueOf(DateTimeUtility.getTimeDifference(startTime, endTime, DateTimeUtility.TIME_24_ss));
        }
        mLogbook.setDiveTotalTime(totalTime);
        return mLogbook;
    }


}
