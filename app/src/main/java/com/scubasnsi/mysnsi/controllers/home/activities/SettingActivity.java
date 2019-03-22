package com.scubasnsi.mysnsi.controllers.home.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.LengthType;
import com.scubasnsi.mysnsi.model.PressureType;
import com.scubasnsi.mysnsi.model.TemperatureType;
import com.scubasnsi.mysnsi.model.WeightType;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.listeners.OnUpdateHeaderListener;

import static com.scubasnsi.R.id.backText;


public class SettingActivity extends AppCompatActivity implements OnUpdateHeaderListener, RadioGroup.OnCheckedChangeListener {


    private Context mContext;
    private UserSession mUserSession;

    @BindString(R.string.title_settings)
    protected String mHeaderTitle;

    @BindView(R.id.img_back_arrow)
    protected ImageView mImVBack;
    @BindView(backText)
    protected TextView mTVBackText;
    @BindView(R.id.title_text)
    protected TextView mTVTitle;

    //Length
    @BindView(R.id.radio_group_length)
    protected RadioGroup mRBLength;


    //Pressure
    @BindView(R.id.radio_group_pressure)
    protected RadioGroup mRBPressure;


    //Temperature
    @BindView(R.id.radio_group_temperature)
    protected RadioGroup mRBTemp;


    //Weight
    @BindView(R.id.radio_group_weight)
    protected RadioGroup mRBWeight;


    //User Preferences
    private UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = this;
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        ButterKnife.bind(this);
        OnUpdateHeader(true, mHeaderTitle, true);

        mUserPreference = mUserSession.getUserPreference();
        setRBLength(mUserPreference.getLengthType());
        setRBPressure(mUserPreference.getPressureType());
        setRBTemp(mUserPreference.getTemperatureType());
        setRBWeight(mUserPreference.getWeightType());


        mRBLength.setOnCheckedChangeListener(this);
        mRBPressure.setOnCheckedChangeListener(this);
        mRBTemp.setOnCheckedChangeListener(this);
        mRBWeight.setOnCheckedChangeListener(this);

    }


    @Override
    public void OnUpdateHeader(boolean isShown, String title, boolean backgroundWithLogo) {
        mTVBackText.setVisibility(View.VISIBLE);
        mImVBack.setVisibility(View.VISIBLE);
        mTVTitle.setText(title);
    }

    @OnClick({R.id.img_back_arrow, R.id.backText})
    protected void back(View view) {
        finish();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (radioGroup.getId()) {
            case R.id.radio_group_length:
                updateRBLength(id);
                break;
            case R.id.radio_group_pressure:
                updateRBPressure(id);
                break;
            case R.id.radio_group_temperature:
                updateRBTemp(id);
                break;
            case R.id.radio_group_weight:
                updateRBWeight(id);
                break;
        }
    }

    //Set Length
    private void updateRBLength(int id) {
        switch (id) {
            case R.id.radio_btn_length_m:
                mUserPreference.setLengthType(LengthType.METER);
                break;
            case R.id.radio_btn_length_ft:
                mUserPreference.setLengthType(LengthType.FEET);
                break;
        }
        saveUserPreference();
    }

    private void setRBLength(LengthType length) {
        switch (length) {
            case METER:
                ((RadioButton) mRBLength.getChildAt(0)).setChecked(true);
                break;
            case FEET:
                ((RadioButton) mRBLength.getChildAt(1)).setChecked(true);
                break;
        }
    }

    //Set Pressure
    private void updateRBPressure(int id) {
        switch (id) {
            case R.id.radio_btn_pressure_bar:
                mUserPreference.setPressureType(PressureType.BAR);
                break;
            case R.id.radio_btn_pressure_psi:
                mUserPreference.setPressureType(PressureType.PSI);
                break;
        }
        saveUserPreference();
    }

    private void setRBPressure(PressureType pressure) {
        switch (pressure) {
            case BAR:
                ((RadioButton) mRBPressure.getChildAt(0)).setChecked(true);
                break;
            case PSI:
                ((RadioButton) mRBPressure.getChildAt(1)).setChecked(true);
                break;
        }
    }

    //Set Temperature
    private void updateRBTemp(int id) {
        switch (id) {
            case R.id.radio_btn_temperature_c:
                mUserPreference.setTemperatureType(TemperatureType.CELSIUS);
                break;
            case R.id.radio_btn_temperature_f:
                mUserPreference.setTemperatureType(TemperatureType.FAHRENHEIT);
                break;
        }
        saveUserPreference();
    }

    private void setRBTemp(TemperatureType temp) {
        switch (temp) {
            case CELSIUS:
                ((RadioButton) mRBTemp.getChildAt(0)).setChecked(true);
                break;
            case FAHRENHEIT:
                ((RadioButton) mRBTemp.getChildAt(1)).setChecked(true);
                break;
        }
    }

    //Set Weight
    private void updateRBWeight(int id) {
        switch (id) {
            case R.id.radio_btn_weight_kg:
                mUserPreference.setWeightType(WeightType.KILOGARM);
                break;
            case R.id.radio_btn_weight_lb:
                mUserPreference.setWeightType(WeightType.POUND);
                break;
        }
        saveUserPreference();
    }

    private void setRBWeight(WeightType temp) {
        switch (temp) {
            case KILOGARM:
                ((RadioButton) mRBWeight.getChildAt(0)).setChecked(true);
                break;
            case POUND:
                ((RadioButton) mRBWeight.getChildAt(1)).setChecked(true);
                break;
        }
    }

    // Private save the
    private void saveUserPreference() {
        mUserSession.saveUserPreference(mUserPreference);

    }
}
