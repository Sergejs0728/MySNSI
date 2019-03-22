package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.utilities.StringsOperations;

import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;

public class DiveTypeFragment extends Fragment implements OnNextListener {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveTypeFragment";
    private Context mContext;
    private UserSession mUserSession;
    private UserPreference mUserPreference;

    @BindString(R.string.dive_type_title)
    protected String mTitle;

    @BindView(R.id.logback_dive_type_unit_tv)
    protected TextView mTVMaxDepthUnit;
    @BindView(R.id.logback_dive_type_max_depth_et)
    protected TextView mETMaxDepth;
    @BindView(R.id.radio_group_dive_for)
    protected RadioGroup mRGDiveFor;
    @BindView(R.id.radio_group_dive_type)
    protected RadioGroup mRGDiveType;

    private UpdateLogbookHeader mUpdateLogbookHeader;
    private Logbook mLogbook;


    public static DiveTypeFragment newInstance(Logbook logbook) {
        DiveTypeFragment diveTypeFragment = new DiveTypeFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        diveTypeFragment.setArguments(args);

        return diveTypeFragment;
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
        mUserPreference = mUserSession.getUserPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_dive_type, container, false);

        ButterKnife.bind(this, view);
        mUpdateLogbookHeader.OnUpdateHeader(false, mTitle);
        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingOfUnitAsPerUserPreference();
        radioButtonSelection();

        mRGDiveFor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_btn_dive_for_fun:
                        mLogbook.setDiveFor(getString(R.string.dive_for_fun));
                        break;
                    case R.id.radio_btn_dive_for_training:
                        mLogbook.setDiveFor(getString(R.string.dive_for_training));
                        break;
                }
            }
        });
        mRGDiveType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_btn_dive_type_recreational:
                        mLogbook.setDiveType(getString(R.string.dive_type_recreational));
                        break;
                    case R.id.radio_btn_dive_type_technical:
                        mLogbook.setDiveType(getString(R.string.dive_type_technical));
                        break;
                }
            }
        });
    }

    private void settingOfUnitAsPerUserPreference() {
        switch (mUserPreference.getLengthType()) {
            case METER:
                mTVMaxDepthUnit.setText(getString(R.string.unit_meter));
                mETMaxDepth.setText((mLogbook.getMaxDepth()==0)?"":String.valueOf(mLogbook.getMaxDepth()));
                break;
            case FEET:
                mTVMaxDepthUnit.setText(getString(R.string.unit_feet));
                mETMaxDepth.setText((mLogbook.getMaxDepth()==0)?"":JavaUtility.formatUptoTwoDigit(JavaUtility.meterToFeet(mLogbook.getMaxDepth())));
        }
    }


    // Setting of radio buttons
    /*

     */

    private void radioButtonSelection() {
        if (getString(R.string.dive_for_training).equalsIgnoreCase(mLogbook.getDiveFor())) {
            ((RadioButton) mRGDiveFor.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mRGDiveFor.getChildAt(0)).setChecked(true);
            mLogbook.setDiveFor(getString(R.string.dive_for_fun)); // set log back dive for fun DeFault
        }

        if (getString(R.string.dive_type_technical).equalsIgnoreCase(mLogbook.getDiveType())) {
            ((RadioButton) mRGDiveType.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mRGDiveType.getChildAt(0)).setChecked(true);
            mLogbook.setDiveType(getString(R.string.dive_type_recreational)); // set log back dive type recreational  DeFault
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }


    @Override
    public Logbook next() {

        String maxDepth = StringsOperations.getTextFromEditText(mETMaxDepth);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(maxDepth, R.string.verify_max_depth, mContext)) {
            return null;
        }
        float maxDepFloat = Float.parseFloat(maxDepth);
        if (maxDepFloat == 0f) {
            GenericDialogs.showInformativeDialog("", R.string.verify_max_depth_more_than_zero, mContext);
            return null;
        }

        //If data is in meter no conversion required , Else convert back to meter
        switch (mUserPreference.getLengthType()) {
            case METER:
                mLogbook.setMaxDepth(maxDepFloat);
                break;
            case FEET:
                mLogbook.setMaxDepth(Float.parseFloat(JavaUtility.formatUptoTwoDigit(JavaUtility.feetToMeter(maxDepFloat))));
                break;
        }

        return mLogbook;
    }


}
