package com.scubasnsi.mysnsi.app.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.controllers.logbook.TimePickerValues;
import com.scubasnsi.mysnsi.controllers.login.activities.SignUpActivity;

import java.util.regex.Pattern;

import shiva.joshi.common.callbacks.GenericConfirmationDialogBoxCallback;
import shiva.joshi.common.callbacks.GenericInformativeDialogBoxCallback;
import shiva.joshi.common.callbacks.GenericInputDialogBoxCallback;
import shiva.joshi.common.java.DateTimeUtility;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.CommonUtilities;

import static shiva.joshi.common.utilities.CommonUtilities.isGreaterThanMarshMallow;

public class GenericDialogs  extends TimePickerValues {

    private final static String TAG = GenericDialogs.class.getName();


    String date;
    WheelDatePicker mDatepicker;
    public final static String CAMERA_ERROR = "Unable capture image by camera";
    public final static String IMAGE_UPLOAD_ERROR = "Unable to upload image.";
    public static String OK = "Ok";
    public static String CANCEL = "Cancel";
    private final static String APP_EXIT = "Press back again to exit!";

    /***
     * Informative Alert Box
     *
     * @param title    the title
     * @param messageResourceId  the message resource id
     * @param mContext the context
     * @description giving information about particular task
     * @parameters title - title for alert box message      - message to be show in alert box mContext     - context of Activity
     */
    public static void showInformativeDialog(String title, int messageResourceId, Context mContext) {
        try {
            int imageResource = R.mipmap.ic_launcher;
            Drawable image;
            if (isGreaterThanMarshMallow())
                image = mContext.getDrawable(imageResource);
            else
                image = mContext.getResources().getDrawable(imageResource);

            title = mContext.getString(R.string.app_name);
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(mContext.getString(messageResourceId))
                    .setTitle(title)
                    .setIcon(image)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            alert = builder.create();
            alert.show();
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }
    public static void showInformativeDialog(String  message, Context mContext) {
        try {
            int imageResource = R.mipmap.ic_launcher;
            Drawable image;
            if (isGreaterThanMarshMallow())
                image = mContext.getDrawable(imageResource);
            else
                image = mContext.getResources().getDrawable(imageResource);

            String title = mContext.getString(R.string.app_name);
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(message)
                    .setTitle(title)
                    .setIcon(image)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            alert = builder.create();
            alert.show();
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }


    public static void showInformativeDialogWithTitle(String title,String message, Context mContext) {
        try {

            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(message)
                    .setTitle(title)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            alert = builder.create();
            alert.show();
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }
    /***
     * Generic Informative dialog box with single button
     * <p>
     * Confirmation box with message
     *
     * @param mContext - Context of activity.
     *                 title                         - Title for confirmation box.
     *                 message                       - Message to be show in alert box.
     *                 positiveBtnCaption            - Positive button string.
     *                 isCancelable                  - Sets whether the dialog is cancelable or not. Default is true.
     *                 dialogBoxInterface            - Interface object that handles its click.
     */
    public static void getGenericInformativeDialogBoxWithSingleButton(Context mContext, String title, String message, String positiveBtnCaption, boolean isCancelable, final GenericInformativeDialogBoxCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        int imageResource = R.mipmap.ic_launcher;
        Drawable image;
        if (isGreaterThanMarshMallow())
            image = mContext.getDrawable(imageResource);
        else
            image = mContext.getResources().getDrawable(imageResource);

        //AppName
        title = mContext.getString(R.string.app_name);

        builder.setTitle(title).setMessage(message)
                .setIcon(image)
                .setCancelable(false)
                .setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogBoxInterface.PositiveMethod(dialog, id);
                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialogBoxInterface.PositiveMethod(dialog, 0);
                }
            });
        }
    }

    /***
     * Generic confirmation dialog box
     *
     * @@param mContext                      - Context of activity.
     * title                         - Title for confirmation box.
     * message                       - Message to be show in alert box.
     * positiveBtnCaption            - Positive button string.
     * negativeBtnCaption            - Negative button string.
     * isCancelable                  - Sets whether the dialog is cancelable or not. Default is true.
     * dialogBoxInterface            - Interface object that handles its click.
     */
    public static void getGenericConfirmDialog(Context mContext, String title, String message, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final GenericConfirmationDialogBoxCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        int imageResource = R.mipmap.ic_launcher;
        Drawable image;
        if (isGreaterThanMarshMallow())
            image = mContext.getDrawable(imageResource);
        else
            image = mContext.getResources().getDrawable(imageResource);

        //AppName
        if (title == null || title.isEmpty())
            title = mContext.getString(R.string.app_name);

        builder.setTitle(title).setMessage(message)
                .setIcon(image)
                .setCancelable(false)
                .setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogBoxInterface.PositiveMethod(dialog, id);
                    }
                })
                .setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialogBoxInterface.NegativeMethod(dialog, id);
                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialogBoxInterface.NegativeMethod(dialog, 0);
                }
            });
        }
    }

    /***
     * Generic Input dialog box
     *
     * @@param mContext                      - Context of activity.
     * title                         - Title for confirmation box.
     * message                       - Message to be show in alert box.
     * positiveBtnCaption            - Positive button string.
     * negativeBtnCaption            - Negative button string.
     * isCancelable                  - Sets whether the dialog is cancelable or not. Default is true.
     * dialogBoxInterface            - Interface object that handles its click.
     */
    public static void getGenericInputDialog(Context mContext, String title, String message, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final GenericInputDialogBoxCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        int imageResource = R.mipmap.ic_launcher;
        Drawable image = mContext.getResources().getDrawable(imageResource);

        //AppName
        if (title == null || title.isEmpty())
            title = mContext.getString(R.string.app_name);

        //Input field
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.alert_input_box, null, false);
        final EditText input = (EditText) view.findViewById(R.id.alert_item);
        builder.setTitle(title).setMessage(message)
                .setIcon(image)
                .setCancelable(false)
                .setPositiveButton(positiveBtnCaption, null)
                .setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialogBoxInterface.NegativeMethod(dialog, id);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setView(view);
        alert.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = "";
                if (input != null)
                    value = input.getText().toString().trim();
                dialogBoxInterface.PositiveMethod(alert, 0, value);

            }
        });


        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialogBoxInterface.NegativeMethod(dialog, 0);
                }
            });
        }
    }


    public static void underDevelopment(Context context) {
        showInformativeDialog("", R.string.under_development, context);
    }


    // check if field is valid or not , if not show appropriate meaasge
    public static boolean isFieldValidAndShowValidMessage(String fieldValue, int message, Context context) {
        if (fieldValue.isEmpty()) {
            showInformativeDialog("",message, context);
            return false;
        }
        return true;
    }

    // check if DATE is valid or not , if not show appropriate meaasge
    public static boolean isdateValid(String fieldValue, int message, Context context) {
        if (fieldValue.isEmpty()||fieldValue.equals("Birthdate")) {
            showInformativeDialog("",message, context);
            return false;
        }
        return true;
    }

    // check if EMAIL is valid or not , if not show appropriate meaasge
    public static boolean isEmailValid(String fieldValue, int message, Context context) {
        if (!Patterns.EMAIL_ADDRESS.matcher(fieldValue).matches()) {
            showInformativeDialog("",message, context);
            return false;
        }
        return true;
    }

    // check if SelectCourse is valid or not , if not show appropriate meaasge
    public static boolean isCourse(String fieldValue, int message, Context context) {
        if (fieldValue.equalsIgnoreCase("0") ) {
            showInformativeDialog("",message, context);
            return false;
        }
        return true;
    }

    // check if SelectCountry is valid or not , if not show appropriate meaasge
    public static boolean isCoutrySelected(String fieldValue, int message, Context context) {
        if (fieldValue.equalsIgnoreCase("Select the country") ) {
            showInformativeDialog("",message, context);
            return false;
        }
        return true;
    }
    //Is greater than lollipop
    public static boolean isGreaterThanVersionCode24() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    // On activity back pressed.
    private static long backPressed;

    public static void onActivityBackPressedExit(Activity activity) {
        if (backPressed + 2000 > System.currentTimeMillis()) activity.finish();
        else
            AppLogger.showToast(TAG, APP_EXIT);
        backPressed = System.currentTimeMillis();
    }



    public  void getGerenericDateDialog(Context mContext)
    {
        final Dialog dialog  =new Dialog(mContext);
        try {date = DateTimeUtility.getCurrentLocalDate(DateTimeUtility.DATE_F_DD_MM_YYYY);}
        catch (Exception e)
        {}

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_layput_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_id);
        Button ok = (Button) dialog.findViewById(R.id.ok_id);

        mDatepicker = (WheelDatePicker) dialog.findViewById(R.id.logback_dive_date_picker);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Done with date
                String date = DateTimeUtility.convertDateToRequiredFormat(mDatepicker.getCurrentDate(), DateTimeUtility.DATE_DD_MM_YYYY);
                Log.e("dateDateDialog",">>"+ date);
                SignUpActivity.mBirthDate.setText(date);
                dialog.cancel();
            }
        });

        mDatepicker.setCyclic(true);
        mDatepicker.setCurved(true);
        mDatepicker.setItemTextColor(CommonUtilities.getColor(mContext, R.color.wheel_normal_text_color));
        mDatepicker.setSelectedItemTextColor(CommonUtilities.getColor(mContext, R.color.wheel_selected_text_color));
        mDatepicker.setAtmospheric(true);
        setDate(date);
        dialog.show();
    }

    // Set Date
    private void setDate(String date) {
        int[] dateArray = getSelectedDate(date);
        mDatepicker.setSelectedDay(dateArray[0]);
        mDatepicker.setYearAndMonth(dateArray[2], dateArray[1]);

    }
}