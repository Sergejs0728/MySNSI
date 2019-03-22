package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.model.TemperatureType;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.utilities.StringsOperations;

import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_TEMP_VALUE;
import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;

public class DiveWeatherInfoFragment extends Fragment implements OnNextListener {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveWeatherInfoFragment";

    private Context mContext;
    private UserSession mUserSession;
    private UserPreference mUserPreference;


    @BindString(R.string.weather_info_title)
    protected String mTitle;

    @BindView(R.id.logback_dive_site_weather_air_temp_et)
    protected EditText mETWeatherAirTemp;

    @BindView(R.id.logback_dive_site_weather_water_temp_et)
    protected EditText mETWeatherWaterTemp;

    /*Weather conditions */
    @BindView(R.id.radio_group_weather_info)
    protected RadioGroup mRGWeatherInfo;
    @BindView(R.id.radio_group_visibility)
    protected RadioGroup mRGVisibility;

    private Logbook mLogbook;
    private UpdateLogbookHeader mUpdateLogbookHeader;


    public static DiveWeatherInfoFragment newInstance(Logbook logbook) {
        DiveWeatherInfoFragment fragment = new DiveWeatherInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mUpdateLogbookHeader != null)
            mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);
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
        mUserPreference = mUserSession.getUserPreference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_weather_info, container, false);

        ButterKnife.bind(this, view);
        mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);
        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUnitsAsPerUserPreference();

        /*Weather */
        setUpWeatherConditions();
        updateWeatherInfo();

        /*Visibility*/
        setUpVisibility();
        updateVisibility();
    }

    private void setUpUnitsAsPerUserPreference() {
        /*Pressure part */
        float airTemp = DEFAULT_TEMP_VALUE;
        float waterTemp = DEFAULT_TEMP_VALUE;
        switch (mUserPreference.getTemperatureType()) {
            case CELSIUS:
                mETWeatherAirTemp.setHint(getString(R.string.weather_info_air_temp_hint) + " " + TemperatureType.CELSIUS.getValue());
                mETWeatherWaterTemp.setHint(getString(R.string.weather_info_water_temp_hint) + " " + TemperatureType.CELSIUS.getValue());


                if (mLogbook.getAirTemperature() != DEFAULT_TEMP_VALUE) {
                    airTemp = mLogbook.getAirTemperature();
                }

                if (mLogbook.getWaterTemperature() != DEFAULT_TEMP_VALUE) {
                    waterTemp = mLogbook.getWaterTemperature();
                }
                break;

            case FAHRENHEIT:
                mETWeatherAirTemp.setHint(getString(R.string.weather_info_air_temp_hint) + " " + TemperatureType.FAHRENHEIT.getValue());
                mETWeatherWaterTemp.setHint(getString(R.string.weather_info_water_temp_hint) + " " + TemperatureType.FAHRENHEIT.getValue());

                if (mLogbook.getAirTemperature() != DEFAULT_TEMP_VALUE) {
                    airTemp = Float.parseFloat(JavaUtility.formatUptoTwoDigit(JavaUtility.celsiusToFahrenheit(mLogbook.getAirTemperature())));
                }

                if (mLogbook.getWaterTemperature() != DEFAULT_TEMP_VALUE) {
                    waterTemp = Float.parseFloat(JavaUtility.formatUptoTwoDigit(JavaUtility.celsiusToFahrenheit(mLogbook.getWaterTemperature())));
                }
                break;
        }
        mETWeatherAirTemp.setText((airTemp == DEFAULT_TEMP_VALUE) ? "" : String.valueOf(airTemp));
        mETWeatherWaterTemp.setText((waterTemp == DEFAULT_TEMP_VALUE) ? "" : String.valueOf(waterTemp));
    }


    /*Weather conditions*/
    private void updateWeatherInfo() {
        mRGWeatherInfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_group_weather_info_sunny:
                        mLogbook.setWeather(getString(R.string.weather_info_weather_sunny));
                        break;
                    case R.id.radio_group_weather_info_cloudy:
                        mLogbook.setWeather(getString(R.string.weather_info_weather_cloudy));
                        break;
                    case R.id.radio_group_weather_info_rainy:
                        mLogbook.setWeather(getString(R.string.weather_info_weather_rainy));
                        break;
                    case R.id.radio_group_weather_info_windy:
                        mLogbook.setWeather(getString(R.string.weather_info_weather_windy));
                        break;
                }
            }
        });
    }

    private void setUpWeatherConditions() {
        if (getString(R.string.weather_info_weather_windy).equalsIgnoreCase(mLogbook.getWeather())) {
            ((RadioButton) mRGWeatherInfo.getChildAt(3)).setChecked(true);
        } else if (getString(R.string.weather_info_weather_rainy).equalsIgnoreCase(mLogbook.getWeather())) {
            ((RadioButton) mRGWeatherInfo.getChildAt(2)).setChecked(true);
        } else if (getString(R.string.weather_info_weather_cloudy).equalsIgnoreCase(mLogbook.getWeather())) {
            ((RadioButton) mRGWeatherInfo.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mRGWeatherInfo.getChildAt(0)).setChecked(true);
            mLogbook.setWeather(getString(R.string.weather_info_weather_sunny)); // set log back equipment suit Default
        }

    }


    /*Visibility conditions*/
    private void updateVisibility() {
        mRGVisibility.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_group_visibility_excellent:
                        mLogbook.setVisibility(getString(R.string.weather_info_visibility_excellent));
                        break;
                    case R.id.radio_group_visibility_good:
                        mLogbook.setVisibility(getString(R.string.weather_info_visibility_good));
                        break;
                    case R.id.radio_group_visibility_poor:
                        mLogbook.setVisibility(getString(R.string.weather_info_visibility_poor));
                        break;
                    case R.id.radio_group_visibility_no_visibility:
                        mLogbook.setVisibility(getString(R.string.weather_info_visibility_very_poor));
                        break;
                }
            }
        });
    }

    private void setUpVisibility() {
        if (getString(R.string.weather_info_visibility_no_visible).equalsIgnoreCase(mLogbook.getVisibility()) ||
                getString(R.string.weather_info_visibility_very_poor).equalsIgnoreCase(mLogbook.getVisibility())) {
            ((RadioButton) mRGVisibility.getChildAt(3)).setChecked(true);
        } else if (getString(R.string.weather_info_visibility_poor).equalsIgnoreCase(mLogbook.getVisibility())) {
            ((RadioButton) mRGVisibility.getChildAt(2)).setChecked(true);
        } else if (getString(R.string.weather_info_visibility_good).equalsIgnoreCase(mLogbook.getVisibility())) {
            ((RadioButton) mRGVisibility.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mRGVisibility.getChildAt(0)).setChecked(true);
            mLogbook.setVisibility(getString(R.string.weather_info_visibility_excellent)); // set log back equipment suit Default
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }


    @Override
    public Logbook next() {

        String tempAir = StringsOperations.getTextFromEditText(mETWeatherAirTemp);
        String tempWater = StringsOperations.getTextFromEditText(mETWeatherWaterTemp);


        switch (mUserPreference.getTemperatureType()) {
            case CELSIUS:
                if (tempAir.isEmpty())
                    mLogbook.setAirTemperature(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setAirTemperature(tempAir);

                if (tempWater.isEmpty())
                    mLogbook.setWaterTemperature(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setWaterTemperature(tempWater);

                break;
            case FAHRENHEIT:
                if (tempAir.isEmpty())
                    mLogbook.setAirTemperature(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setAirTemperature(JavaUtility.formatUptoTwoDigit(JavaUtility.fahrenheitToCelsius(Double.parseDouble(tempAir))));

                if (tempWater.isEmpty())
                    mLogbook.setWaterTemperature(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setWaterTemperature(JavaUtility.formatUptoTwoDigit(JavaUtility.fahrenheitToCelsius(Double.parseDouble(tempWater))));

                break;
        }

        return mLogbook;
    }
}
