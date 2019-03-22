package shiva.joshi.common.utilities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Calendar;

import shiva.joshi.common.R;
import shiva.joshi.common.callbacks.GenericImagePickerCallback;
import shiva.joshi.common.logger.AppLogger;

import static shiva.joshi.common.utilities.CommonUtilities.isGreaterThanMarshMallow;

public class CommonGenericDialogs {

    private final static String TAG = CommonGenericDialogs.class.getName();

    public static String OK = "Ok";
    public static String CANCEL = "Cancel";
    public static final int REQUEST_IMAGE_CAPTURE = 1600;
    public static final int RESULT_LOAD_IMAGE = 1601;

    public final static String CAMERA_ERROR = "Unable capture image by camera";
    public final static String IMAGE_UPLOAD_ERROR = "Unable to upload image.";


    public enum DateAllowed {
        PAST,
        FUTURE,
        ALL
    }


    /***
     * Informative Alert Box
     *
     * @param title    the title
     * @param message  the message
     * @param mContext the context
     * @description giving information about particular task
     * @parameters title - title for alert box message      - message to be show in alert box mContext     - context of Activity
     */
    public static void showInformativeDialog(String title, String message, Context mContext) {
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



    /* Photo picker */

    public static void getPicPiker(Context mContext, final GenericImagePickerCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //Custom layout
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.common_layout_photo_picker, null, false);

        final AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.setView(view);
        alert.show();


        LinearLayout mLLCamera = (LinearLayout) view.findViewById(R.id.id_ll_camera);
        mLLCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Camera action
                dialogBoxInterface.pickFromCamera(alert, 0);
            }
        });

        LinearLayout mLLGallery = (LinearLayout) view.findViewById(R.id.id_ll_gallery);
        mLLGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gallery action
                dialogBoxInterface.pickPromGallery(alert, 0);
            }
        });
    }

    // Take picture
    public static String takeAPicture(Context context, Fragment fragment) {
        //Creating default image
        String defaultImageName = "NEW_ITEM_IMAGE.jpg";
        File photoFile = FileUtilities.createTempImageFile(defaultImageName, context);
        if (photoFile == null) {
            AppLogger.Logger.warn(TAG, new Exception("invalid file url"));
            return null;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) == null) {
            AppLogger.Logger.warn(TAG, new Exception("Unable to configure image."));
            return null;
        }

        Uri imageUri = null;
        if (isGreaterThanVersionCode24())
            imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
        else
            imageUri = Uri.fromFile(photoFile);

        if (imageUri == null) {
            return null;
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (fragment != null) {
            fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        return photoFile.getAbsolutePath();
    }


    // Take picture
    public static String takeAPictureforActivity(Context context, AppCompatActivity fragment) {
        //Creating default image
        String defaultImageName = "NEW_ITEM_IMAGE.jpg";
        File photoFile = FileUtilities.createTempImageFile(defaultImageName, context);
        if (photoFile == null) {
            AppLogger.Logger.warn(TAG, new Exception("invalid file url"));
            return null;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) == null) {
            AppLogger.Logger.warn(TAG, new Exception("Unable to configure image."));
            return null;
        }

        Uri imageUri = null;
        if (isGreaterThanVersionCode24())
            imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
        else
            imageUri = Uri.fromFile(photoFile);

        if (imageUri == null) {
            return null;
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (fragment != null) {
            fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        return photoFile.getAbsolutePath();
    }
    public static String takeAPictureByFragment(Fragment fragment, Context context) {
        //Creating default image
        String defaultImageName = "NEW_ITEM_IMAGE.jpg";
        File photoFile = FileUtilities.createTempImageFile(defaultImageName, context);
        if (photoFile == null) {
            AppLogger.Logger.warn(TAG, new Exception("invalid file url"));
            return null;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) == null) {
            AppLogger.Logger.warn(TAG, new Exception("Unable to configure image."));
            return null;
        }

        Uri imageUri = null;
        if (isGreaterThanVersionCode24())
            imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
        else
            imageUri = Uri.fromFile(photoFile);
        if (imageUri == null) {
            return null;
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        return photoFile.getAbsolutePath();
    }

    // Gallery
    public static void getImageFromGallery(Context context) {
        AppLogger.Logger.info(TAG, "Form gallery ");
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public static void getImageFromGalleryByFragment(Fragment fragment) {
        AppLogger.Logger.info(TAG, "Form gallery ");
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public static void getImageFromGalleryByActivity(AppCompatActivity fragment) {
        AppLogger.Logger.info(TAG, "Form gallery ");
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * Show date picker dialog.
     *
     * @param context        the context
     * @param datePickerDate the date picker date
     * @param dateAllowed    the date allowed
     */
//  Date picker
    public static void showDatePickerDialog(Context context, final DatePickerDialog.OnDateSetListener datePickerDate, DateAllowed dateAllowed) {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerDate,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = datePickerDialog.getDatePicker();
        switch (dateAllowed) {
            case PAST:
                dp.setMaxDate(myCalendar.getTimeInMillis());
                break;
            case FUTURE:
                dp.setMinDate(myCalendar.getTimeInMillis());
                break;
            case ALL:
                break;
        }
        datePickerDialog.show();
    }


    public static void getTimePicker(Context context, final TimePickerDialog.OnTimeSetListener timeSetListener) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeSetListener, mHour, mMinute, false);
        timePickerDialog.show();
    }


    //Is greater than lollipop
    public static boolean isGreaterThanVersionCode24() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


}