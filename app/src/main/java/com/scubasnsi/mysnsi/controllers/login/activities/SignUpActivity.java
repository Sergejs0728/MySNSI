package com.scubasnsi.mysnsi.controllers.login.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.controllers.home.adapter.CorsesAdp;
import com.scubasnsi.mysnsi.controllers.home.adapter.CountriesAdp;
import com.scubasnsi.mysnsi.model.DatePick;
import com.scubasnsi.mysnsi.model.data_models.Courses;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.dto.CoursesDto;
import com.scubasnsi.mysnsi.model.dto.DiverLoginDto;
import com.scubasnsi.mysnsi.model.dto.SignupDto;
import com.scubasnsi.mysnsi.model.services.LoginDiverService;
import com.scubasnsi.mysnsi.model.services.LoginService;
import com.scubasnsi.mysnsi.model.services.impl.CourcesServicesImpl;
import com.scubasnsi.mysnsi.model.services.impl.DiverLoginServicesImpl;
import com.scubasnsi.mysnsi.model.services.impl.LoginServicesImpl;
import com.scubasnsi.mysnsi.model.services.impl.SignupServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.utilities.StringsOperations;

public class SignUpActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener{

    private Context mContext;

    @BindView(R.id.signup_user_given_name_id)
    protected EditText mUserGivenName;
    @BindView(R.id.signup_family_name_id)
    protected EditText mFamilyName;
    @BindView(R.id.signup_user_email_id)
    protected EditText mEmail;
    @BindView(R.id.signup_user_email_line_id)
    protected View mEmailLine;
    //@BindView(R.id.signup_user_birthdate_id)
    public static TextView mBirthDate;
    @BindView(R.id.signup_user_birthdate_lime_id)
    protected View mBirthdateLine;


    @BindView(R.id.signup_user_spinner_country_id)
    protected Spinner mSpinnerCountryName;
    @BindView(R.id.signup_user_spinner_country_line_id)
    protected View mSpinnerCountryName_line;
    @BindView(R.id.signup_user_spinner_course_enroled_id)
    protected Spinner mSpinnerCounseName;

    @BindView(R.id.signup_user_spinner_course_enroled_line_id)
    protected View mSpinnerCounseNameLine;

    @BindView(R.id.login_instructor_name_id)
    protected  EditText mInstructorName;
    @BindView(R.id.signup_instructor_name_line_id)
    protected  View mInstructorline;
    @BindView(R.id.login_card_number_id)
    protected  EditText mCardNumber;
    @BindView(R.id.signup_card_number_line_id)
    protected  View mCardNumberLine;
    @BindView(R.id.signup_signup_btn)
    protected Button mSignUpButton;


    private CustomProgressBar mCustomProgressBar;
    private UserSession mUserSession;

    String[] country,courses;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Bundle bundle;
    String userType;
    SignupServicesImpl signupServices;
    DiverLoginServicesImpl loginDiverService;
    CourcesServicesImpl courcesServices;

    ArrayList<Courses> mCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mContext = this;
        mCustomProgressBar = new CustomProgressBar(mContext);
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        ButterKnife.bind(this);
        signupServices=new SignupServicesImpl();
        courcesServices=new CourcesServicesImpl();
        loginDiverService=new  DiverLoginServicesImpl();

        mBirthDate=(TextView)findViewById(R.id.signup_user_birthdate_id) ;

         bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            if ( bundle.getString("USER_TYPE").equalsIgnoreCase("student")) {
                mBirthDate.setVisibility(View.VISIBLE);
                mBirthdateLine.setVisibility(View.VISIBLE);
                mInstructorName.setVisibility(View.VISIBLE);
                mInstructorline.setVisibility(View.VISIBLE);
                mCardNumber.setVisibility(View.GONE);
                mCardNumberLine.setVisibility(View.GONE);
                mSpinnerCounseName.setVisibility(View.VISIBLE);
                mSpinnerCounseNameLine.setVisibility(View.VISIBLE);
                mSpinnerCountryName.setVisibility(View.VISIBLE);
                mSpinnerCountryName_line.setVisibility(View.VISIBLE);
                userType="register_student";
                mSignUpButton.setText("SIGNUP");
                //getCourses();



            }
            else
            {
                mBirthDate.setVisibility(View.GONE);
                mBirthdateLine.setVisibility(View.GONE);
                mInstructorName.setVisibility(View.GONE);
                mInstructorline.setVisibility(View.GONE);
                mCardNumber.setVisibility(View.VISIBLE);
                mCardNumberLine.setVisibility(View.VISIBLE);
                mSpinnerCounseName.setVisibility(View.GONE);
                mSpinnerCounseNameLine.setVisibility(View.GONE);
                mSpinnerCountryName.setVisibility(View.GONE);
                mSpinnerCountryName_line.setVisibility(View.GONE);
                userType="register_diver";
                mSignUpButton.setText("REQUEST");
            }
        }
        calenderDate();
        mSpinnerCountryName.setOnItemSelectedListener(this);
        mSpinnerCounseName.setOnItemSelectedListener(this);
        setCountryNameSpinner();
        setCourseSpinner();
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
                    setCourseSpinner();
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

    @OnClick(R.id.signup_user_birthdate_id)
    protected  void click()
    {
       /* new DatePickerDialog(SignUpActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
        new GenericDialogs().getGerenericDateDialog(SignUpActivity.this);
    }
    @OnClick(R.id.back)
    protected void back(View view) {
        finish();
    }




    @OnClick(R.id.signup_signup_btn)
    protected  void signup()
    {
        if (bundle.getString("USER_TYPE").equalsIgnoreCase("student"))
        {
            studenValidations();
        }
        else {
            diverValidation();
        }
    }

    private void diverValidation() {

        String username = StringsOperations.getTextFromEditText(mUserGivenName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(username, R.string.validate_given_name, mContext)) {
            return;
        }

        String fname = StringsOperations.getTextFromEditText(mFamilyName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(fname, R.string.validate_gfamily_name, mContext)) {
            return;
        }
       /* String bD = mBirthDate.getText().toString().trim();
        if (!GenericDialogs.isdateValid(bD, R.string.validate_date, mContext)) {
            return;
        }*/
        if (!GenericDialogs.isEmailValid(mEmail.getText().toString().trim(), R.string.validate_email, mContext)) {
            return;
        }
        String cardNumber = StringsOperations.getTextFromEditText(mCardNumber);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(cardNumber, R.string.validate_cardNumber, mContext)) {
            return;
        }


        DiverLoginDto diverLoginDto = new DiverLoginDto(username, fname,mEmail.getText().toString().trim(), cardNumber);
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_signup));

        loginDiverService.loginDiver(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    Toast.makeText(SignUpActivity.this,""+responseHandler.getMessage(),Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                else {
                    //Wrong happned
                    GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }
        },diverLoginDto);
    }

    private void studenValidations() {

        String username = StringsOperations.getTextFromEditText(mUserGivenName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(username, R.string.validate_given_name, mContext)) {
            return;
        }

        String fname = StringsOperations.getTextFromEditText(mFamilyName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(fname, R.string.validate_gfamily_name, mContext)) {
            return;
        }
        String bD = mBirthDate.getText().toString().trim();
        if (!GenericDialogs.isdateValid(bD, R.string.validate_date, mContext)) {
            return;
        }
        if (!GenericDialogs.isEmailValid(mEmail.getText().toString().trim(), R.string.validate_email, mContext)) {
            return;
        }
        String courseId = String.valueOf(mSpinnerCounseName.getSelectedItemPosition());
        if (!GenericDialogs.isCourse(courseId, R.string.validate_course, mContext)) {
            return;
        }


        String CountryName = country[mSpinnerCountryName.getSelectedItemPosition()]/* mSpinnerCountryName.getSelectedItem().toString().trim()*/;
        if (!GenericDialogs.isCoutrySelected(CountryName, R.string.validate_country, mContext)) {
            return;
        }
        String instructor = StringsOperations.getTextFromEditText(mInstructorName);
        if (!GenericDialogs.isFieldValidAndShowValidMessage(instructor, R.string.validate_instructor, mContext)) {
            return;
        }


        SignupDto signupDto = new SignupDto(username, fname,mEmail.getText().toString().trim(),
                bD,courseId,CountryName,mInstructorName.getText().toString().trim(),userType);
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_signup));

        signupServices.signupUser(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    Toast.makeText(SignUpActivity.this,""+responseHandler.getMessage(),Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                else {
                    //Wrong happned
                    GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }
        },signupDto);


    }


    private void calenderDate() {
        myCalendar = Calendar.getInstance();
        DatePick datePick=new DatePick();
        date=datePick.date();
    }


    private void setCourseSpinner() {
        /*courses = getResources().getStringArray(R.array.course_array);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,courses);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCounseName.setAdapter(aa);*/

       // CorsesAdp corsesAdp=new CorsesAdp(getApplicationContext(),mCourses);
        CorsesAdp corsesAdp=new CorsesAdp(getApplicationContext(), HomeActivity.getCoursesGlobal());
        mSpinnerCounseName.setAdapter(corsesAdp);
    }

    private void setCountryNameSpinner() {
        country = getResources().getStringArray(R.array.countries_array);
       /* ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCountryName.setAdapter(aa);*/
        CountriesAdp countriesAdp=new CountriesAdp(getApplicationContext(),country);
        mSpinnerCountryName.setAdapter(countriesAdp);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.signup_user_spinner_country_id)
        {
            //do this
        }
        else if(spinner.getId() == R.id.signup_user_spinner_course_enroled_id)
        {
            //do this
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
