package com.scubasnsi.mysnsi.controllers.login.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.rest.RestApiInterface;

import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.helpers.ChangeActivityHelper;

public class LoginOptionsActivities extends AppCompatActivity {

    private final String TAG = WelcomeSlidesActivity.class.getName();
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_options_login_id)
    protected void goForLogin(View v) {
        ChangeActivityHelper.changeActivity(LoginOptionsActivities.this, LoginActivity.class,false);
    }

    @OnClick(R.id.login_options_more_info_id)
    protected void moreInfo(View v) {
        Uri uri = Uri.parse(RestApiInterface.AD_URL); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
