package com.scubasnsi.mysnsi.controllers.login.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.receivers.ConnectivityReceiver;

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = SplashActivity.class.getName();
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Context mContext;
    private UserSession mUserSession = MyApplication.getApplicationInstance().getUserSession();
    private Bundle mBundle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        if (getIntent() != null && getIntent().getExtras() != null) {
            mBundle = getIntent().getExtras();
            if (mBundle.getString("notification_type") == null)
                mBundle = null;

        }

        splashHandler();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getApplicationInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    // SPLASH HANDLER : TIME OUT 3 SEC ;
    private void splashHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                routing();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void routing() {
        if (mUserSession.isLogin()) {
            ChangeActivityHelper.changeActivity(this, HomeActivity.class, true, mBundle);
        } else {
            ChangeActivityHelper.changeActivity(this,  WelcomeSlidesActivity.class, true);
        }
    }
}
