package com.scubasnsi.mysnsi.controllers.login.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.SignupDto;
import com.scubasnsi.mysnsi.model.services.LoginService;
import com.scubasnsi.mysnsi.model.services.impl.LoginServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.GenericConfirmationDialogBoxCallback;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.StringsOperations;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = WelcomeSlidesActivity.class.getName();
    private Context mContext;

    @BindView(R.id.login_user_name_id)
    protected EditText mUsername;
    @BindView(R.id.login_user_password_id)
    protected EditText mPassword;

    private CustomProgressBar mCustomProgressBar;
    private LoginService mLoginService;
    private UserSession mUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mCustomProgressBar = new CustomProgressBar(mContext);
        mLoginService = new LoginServicesImpl();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    protected void back(View view) {
        finish();
    }

    @OnClick(R.id.login_login_btn)
    protected void login(View view) {
       //getDummyUser(); //// TODO: 20-04-2017 Please check before sending build
        String username = StringsOperations.getTextFromEditText(mUsername);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(username, R.string.validate_email, mContext)) {
            return;
        }

        String password = StringsOperations.getTextFromEditText(mPassword);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(password, R.string.validate_password, mContext)) {
            return;
        }

        LoginDto loginDto = new LoginDto(username, password);
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_login));
        mLoginService.loginWithEmail(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    mUserSession.login((User) responseHandler.getValue());
                    ChangeActivityHelper.changeActivityClearStack((Activity) mContext, HomeActivity.class, true);
                    return;
                }

                // Not verified
                if (getString(R.string.email_not_verified).equalsIgnoreCase(responseHandler.getMessage())) {
                    GenericDialogs.getGenericConfirmDialog(mContext, "", getString(R.string.login_service_failed),
                            "Verify Email", GenericDialogs.OK, false, new GenericConfirmationDialogBoxCallback() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    Uri uri = Uri.parse("https://mysnsi.scubasnsi.com/verify-email"); // missing 'http://' will cause crashed
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    dialog.dismiss();

                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                } else {
                    //Wrong username password
                    GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }
        }, loginDto);

    }


    @OnClick(R.id.login_student_signup_btn)
    protected void studentSignUp()
    {

        Intent  intent=new Intent(this,SignUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_TYPE", "student");
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @OnClick(R.id.login_diver_signup_btn)
    protected void diverSignUp()
    {
        Intent  intent=new Intent(this,SignUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_TYPE", "diver");
        intent.putExtras(bundle);
        startActivity(intent);

    }


    private void getDummyUser() {

        mUsername.setText("alessio.dallai");
        mPassword.setText("alessio");
        /* mUsername.setText("fulvialami");
        mPassword.setText("250967");*/
        AppLogger.showToastForDebug(TAG, "By Dummy Account");
    }

}

