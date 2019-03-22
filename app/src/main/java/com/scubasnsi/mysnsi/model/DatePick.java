package com.scubasnsi.mysnsi.model;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;

import com.scubasnsi.mysnsi.controllers.login.activities.SignUpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by macrew on 2/21/2018.
 */

public class DatePick {DatePickerDialog.OnDateSetListener date;
    final Calendar myCalendar = Calendar.getInstance();

    public DatePick() {
        date();




    }

    public DatePickerDialog.OnDateSetListener date() {
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        return date;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Log.e("date",">>"+sdf.format(myCalendar.getTime()));
        SignUpActivity.mBirthDate.setText(sdf.format(myCalendar.getTime()));
    }
}
