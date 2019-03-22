package shiva.joshi.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Random;

import shiva.joshi.common.logger.AppLogger;

;


/**
 * The type CommonUtilities.
 */
public class CommonUtilities {
    private static final String TAG = CommonUtilities.class.getName();
    private static Random mRandom = new Random();
    private final static String ERROR_INTERNET = "Please check you internet connection.";
    private final static String APP_EXIT = "Press back again to exit!";
    private final static String UNDER_DEVELOPMENT = "This module is under development.";
    public final static int LOAD_MORE_TIME = 2000;
    public final static int visibleThreshold = 2;


    // On activity back pressed.
    private static long backPressed;

    public static void onActivityBackPressedExit(Activity activity) {
        if (backPressed + 2000 > System.currentTimeMillis()) activity.finish();
        else
            AppLogger.showToast(TAG, APP_EXIT);
        backPressed = System.currentTimeMillis();
    }

    // Change integer to string value for formatting for date or time
    public static String changeIntegerToProperString(int value) {
        String stringFormat = value + "";
        if (value < 10) {
            stringFormat = "0" + value;
        }
        return stringFormat;
    }


    //Is greater than lollipop
    public static boolean isGreaterThanLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isGreaterThanMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /* Hide keyBoard */
    public static void hideKeyBoard(View view, Context context) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    // Expand View
    public static void expand(final View v, final int height) {
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(10000);
        v.startAnimation(a);
    }

    //Collapse view
    public static void collapse(final View v, final int height) {
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(10000);
        v.startAnimation(a);
    }

    public static void setEditTextMaxLength(final EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    public static Integer[] getResourcesIdFromDrawables(Context context, int arrayResourceId,int defaultImage) {
        TypedArray typedArray = context.getResources().obtainTypedArray(arrayResourceId);
        Integer[] drawableResourceIds = new Integer[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            drawableResourceIds[i] = typedArray.getResourceId(i, defaultImage);
        }
        typedArray.recycle();
        return drawableResourceIds;
    }


}
