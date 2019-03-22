package com.scubasnsi.mysnsi.controllers.login.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.controllers.home.adapter.IntroPageTransformer;
import com.scubasnsi.mysnsi.controllers.home.adapter.ViewPagerAdapter;
import com.scubasnsi.mysnsi.model.data_models.Courses;
import com.scubasnsi.mysnsi.model.dto.CoursesDto;
import com.scubasnsi.mysnsi.model.services.impl.CourcesServicesImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.utilities.CommonUtilities;

public class WelcomeSlidesActivity extends AppCompatActivity {

    private final String TAG = WelcomeSlidesActivity.class.getName();
    private Context mContext;

    @BindView(R.id.welcome_view_pager_id)
    protected ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    CourcesServicesImpl courcesServices;

    ArrayList<Courses> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slides);
        mContext = this;
        ButterKnife.bind(this);
        courcesServices=new CourcesServicesImpl();

        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeView() {

        mPagerAdapter = new ViewPagerAdapter(WelcomeSlidesActivity.this, CommonUtilities.getResourcesIdFromDrawables(mContext, R.array.welcome_screen_banner_images, R.drawable.splash_background));

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(false, new IntroPageTransformer()); //What is the meaning of this
        mViewPager.setCurrentItem(0);
        getCourses();

    }

    @OnClick(R.id.welcome_get_start_with_app_id)
    protected void getStartWithApp() {
        ChangeActivityHelper.changeActivity(WelcomeSlidesActivity.this, LoginOptionsActivities.class, true);
    }
    private void getCourses() {

        CoursesDto coursesDto = new CoursesDto();
        // mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_signup));

        courcesServices.getCources(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                // mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    mCourses = (ArrayList<Courses>) responseHandler.getValue();
                    HomeActivity.setCoursesGlobal(mCourses);
                    //Toast.makeText(SignUpActivity.this,"getCources Successfully",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    //Wrong happned
                    // GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }
        },coursesDto);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
