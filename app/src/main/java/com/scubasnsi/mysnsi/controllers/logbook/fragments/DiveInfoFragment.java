package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.StringsOperations;

import static com.scubasnsi.R.id.logback_dive_info_nitrox_value_ll;
import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_NITROX;
import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_NITROX_OXYGEN_VALUE;
import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_TEMP_VALUE;
import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;

public class DiveInfoFragment extends Fragment implements OnNextListener {


    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveInfoFragment";

    private Context mContext;
    private UserSession mUserSession;
    private UserPreference mUserPreference;

    @BindString(R.string.dive_info_title)
    protected String mTitle;

    @BindView(R.id.logback_dive_info_pressure_start_et)
    protected EditText mEtPressureStart;
    @BindView(R.id.logback_dive_info_pressure_end_et)
    protected EditText mEtPressureEnd;
    /*equipment Weight */
    @BindView(R.id.logback_dive_info_weight_tv)
    protected TextView mTVWeight;

    @BindView(R.id.logback_dive_info_weight_seek_bar)
    protected SeekBar mSeekBarWeight;
    @BindInt(R.integer.seek_bar_weight_kg_max)
    protected int mSeekBarWeightMax;

    /* Suit*/
    @BindView(R.id.radio_group_suit)
    protected RadioGroup mRGSuit;


    /* Gas type */
    @BindView(R.id.radio_group_gas_type)
    protected RadioGroup mRGGasType;
    @BindView(logback_dive_info_nitrox_value_ll)
    protected LinearLayout mLLNitroxOxygenView;

    @BindView(R.id.logback_dive_info_nitrox_oxygen_tv)
    protected TextView mTVOxygenPercentage;

    @BindView(R.id.logback_dive_info_nitrox_oxygen_seek_bar)
    protected SeekBar mSeekBarNitroxOxygen;


    private Logbook mLogbook;
    private UpdateLogbookHeader mUpdateLogbookHeader;


    public static DiveInfoFragment newInstance(Logbook logbook) {
        DiveInfoFragment fragment = new DiveInfoFragment();
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
        View view = inflater.inflate(R.layout.f_dive_info, container, false);

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
    }

    private void setUpUnitsAsPerUserPreference() {

        /*Pressure part */
        float pressureStart = DEFAULT_TEMP_VALUE;
        float pressureEnd = DEFAULT_TEMP_VALUE;
        switch (mUserPreference.getPressureType()) {
            case BAR:
                mEtPressureStart.setHint(getString(R.string.dive_info_pressure_start_hint) + " " + getString(R.string.unit_pressure_bar));
                mEtPressureEnd.setHint(getString(R.string.dive_info_pressure_end_hint) + " " + getString(R.string.unit_pressure_bar));

                if (mLogbook.getTankPressureStart() != DEFAULT_TEMP_VALUE) {
                    pressureStart = mLogbook.getTankPressureStart();
                }

                if (mLogbook.getTankPressureEnd() != DEFAULT_TEMP_VALUE) {
                    pressureEnd = mLogbook.getTankPressureEnd();
                }

                break;
            case PSI:
                mEtPressureStart.setHint(getString(R.string.dive_info_pressure_start_hint) + " " + getString(R.string.unit_pressure_psi));
                mEtPressureEnd.setHint(getString(R.string.dive_info_pressure_end_hint) + " " + getString(R.string.unit_pressure_psi));
                if (mLogbook.getTankPressureStart() != DEFAULT_TEMP_VALUE) {
                    pressureStart = Float.parseFloat(JavaUtility.formatUptoTwoDigit(JavaUtility.barToPsi(mLogbook.getTankPressureStart())));
                }

                if (mLogbook.getTankPressureEnd() != DEFAULT_TEMP_VALUE) {
                    pressureEnd = Float.parseFloat(JavaUtility.formatUptoTwoDigit(JavaUtility.barToPsi(mLogbook.getTankPressureEnd())));
                }

                break;
        }

        mEtPressureStart.setText((pressureStart == DEFAULT_TEMP_VALUE) ? "" : String.valueOf(pressureStart));
        mEtPressureEnd.setText((pressureEnd == DEFAULT_TEMP_VALUE) ? "" : String.valueOf(pressureEnd));


        /*Weight part */
        setEquipmentWeight(mLogbook.getEquipmentWeight());
        mSeekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {

                switch (mUserPreference.getWeightType()) {
                    case KILOGARM:
                        mTVWeight.setText(progressValue + "" + getString(R.string.unit_weight_kg));
                        break;
                    case POUND:
                        mTVWeight.setText(progressValue + "" + getString(R.string.unit_weight_lbs));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*set Suit values */
        setDiveSuitInfo();
        /*Set Gas values */
        setGasInfo();
        mRGGasType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                boolean isNitroxShown = false;
                if (id == R.id.radio_group_gas_type_nitrox) {
                    isNitroxShown = true;
                }
                showNitroxOxygen(isNitroxShown, DEFAULT_NITROX_OXYGEN_VALUE);
            }
        });
    }


    //Show equipment weight info
    private void setEquipmentWeight(int weight) {
        switch (mUserPreference.getWeightType()) {
            case KILOGARM:
                mSeekBarWeight.setMax(mSeekBarWeightMax);
                mTVWeight.setText(weight + "" + getString(R.string.unit_weight_kg));
                mSeekBarWeight.setProgress(weight);
                break;
            case POUND:
                mSeekBarWeight.setMax((int) JavaUtility.kilogramToPound(mSeekBarWeightMax));
                weight = (int) JavaUtility.kilogramToPound(weight);
                mTVWeight.setText(weight + "" + getString(R.string.unit_weight_lbs));
                mSeekBarWeight.setProgress(weight);
                break;
        }
    }

    /*Suit */
    private void setDiveSuitInfo() {
        if (getString(R.string.dive_info_suit_4).equalsIgnoreCase(mLogbook.getEquipmentSuit())) {
            ((RadioButton) mRGSuit.getChildAt(3)).setChecked(true);
        } else if (getString(R.string.dive_info_suit_3).equalsIgnoreCase(mLogbook.getEquipmentSuit())) {
            ((RadioButton) mRGSuit.getChildAt(2)).setChecked(true);
        } else if (getString(R.string.dive_info_suit_2).equalsIgnoreCase(mLogbook.getEquipmentSuit())) {
            ((RadioButton) mRGSuit.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mRGSuit.getChildAt(0)).setChecked(true);
        }

    }


    /*Gas type */
    private void setGasInfo() {
        boolean isNitroxShown = false;
        int oxygenValue = DEFAULT_NITROX_OXYGEN_VALUE;
        String gasType = mLogbook.getGasType();
        if (!gasType.isEmpty()) {
            String[] gasTypeArray = gasType.split("-");
            // check for oxygen values!
            try {
                if (gasTypeArray.length > 1)
                    oxygenValue = Integer.parseInt(gasTypeArray[1]);
            } catch (NumberFormatException ex) {
                AppLogger.Logger.error(TAG, ex.getMessage(), ex);
            }
            //check local values with gas array
            if (getString(R.string.dive_info_gas_type_nitrox).equalsIgnoreCase(gasTypeArray[0])) {
                ((RadioButton) mRGGasType.getChildAt(1)).setChecked(true);
                isNitroxShown = true;
            } else {
                ((RadioButton) mRGGasType.getChildAt(0)).setChecked(true);
            }
        }
        showNitroxOxygen(isNitroxShown, oxygenValue);
    }


    private void showNitroxOxygen(boolean isShown, int oxygenValue) {
        if (isShown) {
            //Show NitroxView
            mLLNitroxOxygenView.setVisibility(View.VISIBLE);
            mSeekBarNitroxOxygen.setProgress(oxygenValue);
            mSeekBarNitroxOxygen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                    mTVOxygenPercentage.setText(progressValue + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mTVOxygenPercentage.setText(oxygenValue + "%");
        } else {
            mLLNitroxOxygenView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }

    @Override
    public Logbook next() {

        /*Pressure */
        String pressureStart = StringsOperations.getTextFromEditText(mEtPressureStart);
        String pressureEnd = StringsOperations.getTextFromEditText(mEtPressureEnd);

        switch (mUserPreference.getPressureType()) {
            case BAR:
                if (pressureStart.isEmpty())
                    mLogbook.setTankPressureStart(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setTankPressureStart(pressureStart);

                if (pressureEnd.isEmpty())
                    mLogbook.setTankPressureEnd(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setTankPressureEnd(pressureEnd);

                break;
            case PSI:
                if (pressureStart.isEmpty())
                    mLogbook.setTankPressureStart(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setTankPressureStart(JavaUtility.formatUptoTwoDigit(JavaUtility.psiToBar(Double.parseDouble(pressureStart))));

                if (pressureEnd.isEmpty())
                    mLogbook.setTankPressureEnd(String.valueOf(DEFAULT_TEMP_VALUE));
                else
                    mLogbook.setTankPressureEnd(JavaUtility.formatUptoTwoDigit(JavaUtility.psiToBar(Double.parseDouble(pressureEnd))));
                break;
        }

        /*Weight :*/
        int weight = mSeekBarWeight.getProgress();
        switch (mUserPreference.getWeightType()) {
            case KILOGARM:
                mLogbook.setEquipmentWeight(weight);
                break;
            case POUND:
                mLogbook.setEquipmentWeight((int) JavaUtility.poundToKilogram(weight));
                break;
        }


        /*Suit */
        int suitId = mRGSuit.getCheckedRadioButtonId();
        switch (suitId) {
            case R.id.radio_group_suit_1:
                mLogbook.setEquipmentSuit(getString(R.string.dive_info_suit_1));
                break;
            case R.id.radio_group_suit_2:
                mLogbook.setEquipmentSuit(getString(R.string.dive_info_suit_2));
                break;
            case R.id.radio_group_suit_3:
                mLogbook.setEquipmentSuit(getString(R.string.dive_info_suit_3));
                break;
            case R.id.radio_group_suit_4:
                mLogbook.setEquipmentSuit(getString(R.string.dive_info_suit_4));
                break;
        }

        /*GasType :*/
        int gasTypeSelected = mRGGasType.getCheckedRadioButtonId();
        switch (gasTypeSelected) {
            case R.id.radio_group_gas_type_air:
                mLogbook.setGasType(getString(R.string.dive_info_gas_type_air_value));
                break;
            case R.id.radio_group_gas_type_nitrox:
                int nitroxValue = mSeekBarNitroxOxygen.getProgress();
                mLogbook.setGasType(DEFAULT_NITROX + nitroxValue);
                break;
        }


        return mLogbook;
    }


}


