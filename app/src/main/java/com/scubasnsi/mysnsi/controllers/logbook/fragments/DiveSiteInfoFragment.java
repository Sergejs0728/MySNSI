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
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.logbook.OnNextListener;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.utilities.StringsOperations;

import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;

public class DiveSiteInfoFragment extends Fragment implements OnNextListener {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveSiteInfoFragment";

    private Context mContext;
    private UserSession mUserSession;


    @BindString(R.string.dive_site_name_title)
    protected String mTitle;
    @BindView(R.id.logback_dive_site_name_et)
    protected EditText mETSiteName;

    @BindView(R.id.radio_group_guide_or_instructor)
    protected RadioGroup mRGGuide;

    @BindView(R.id.logback_dive_site_buddy_name_et)
    protected EditText mETBuddyName;
    @BindView(R.id.logback_dive_site_guide_name_et)
    protected EditText mETGuideName;
    @BindView(R.id.logback_dive_site_center_name_et)
    protected EditText mETDiveCenterName;

    private Logbook mLogbook;
    private UpdateLogbookHeader mUpdateLogbookHeader;


    public static DiveSiteInfoFragment newInstance(Logbook logbook) {
        DiveSiteInfoFragment fragment = new DiveSiteInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.f_dive_site_info, container, false);

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

        setDiveSiteInfo();
        radioButtonSelection();
        mRGGuide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_btn_guide:
                        mLogbook.setInstructorFlag(getString(R.string.dive_site_dive_guide));
                        break;
                    case R.id.radio_btn_instructor:
                        mLogbook.setInstructorFlag(getString(R.string.dive_site_instructor).toLowerCase());
                        break;
                }
            }
        });
    }

    // set dive site info
    private void setDiveSiteInfo() {
        mETSiteName.setText(mLogbook.getDiveSiteName());
        mETBuddyName.setText(mLogbook.getBuddyName());
        mETGuideName.setText(mLogbook.getInstructorName());
        mETDiveCenterName.setText(mLogbook.getDiveCenterName());
    }


    // Setting of radio buttons
    private void radioButtonSelection() {

        if(getString(R.string.dive_for_training).equalsIgnoreCase(mLogbook.getDiveFor())){
            ((RadioButton) mRGGuide.getChildAt(1)).setChecked(true);
            ((RadioButton) mRGGuide.getChildAt(0)).setEnabled(false);
            ((RadioButton) mRGGuide.getChildAt(0)).setEnabled(false);
            mLogbook.setInstructorFlag(getString(R.string.dive_site_instructor).toLowerCase());

        }else{

            if (getString(R.string.dive_site_instructor).equalsIgnoreCase(mLogbook.getInstructorFlag())) {
                ((RadioButton) mRGGuide.getChildAt(1)).setChecked(true);
            } else {
                ((RadioButton) mRGGuide.getChildAt(0)).setChecked(true);
                mLogbook.setInstructorFlag(getString(R.string.dive_site_dive_guide)); // set log back dive for fun DeFault
            }
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }

    @Override
    public Logbook next() {
        String diveSiteName = StringsOperations.getTextFromEditText(mETSiteName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(diveSiteName, R.string.verify_dive_site_name, mContext)) {
            return null;
        }
        mLogbook.setDiveSiteName(diveSiteName);
        mLogbook.setBuddyName(mETBuddyName.getText().toString().trim());
        mLogbook.setInstructorName(mETGuideName.getText().toString().trim());
        mLogbook.setDiveCenterName(mETDiveCenterName.getText().toString().trim());
        return mLogbook;
    }

}
