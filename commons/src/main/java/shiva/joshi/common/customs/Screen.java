package shiva.joshi.common.customs;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import shiva.joshi.common.logger.AppLogger;

public class Screen {
    private Context context;
    private int heightPixels; // device screen height in pixels
    private int widthPixels;  // device screen width in pixels
    private float density;   // device density
    private int densityDpi;
    private int statusBarHeight; // Status bar's height in pixels

    public Screen(Activity activityContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        activityContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.heightPixels = metrics.heightPixels;
        this.widthPixels = metrics.widthPixels;
        this.density = metrics.density;
        this.densityDpi = metrics.densityDpi;
        this.setContext(activityContext.getApplicationContext());
        this.setStatusBarHeight(getStatusBarHeight(context));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

    public void setHeightPixels(int heightPixels) {
        this.heightPixels = heightPixels;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    // return the status bar's height
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int pixelToDp(int pixel) {
        try {
            int dp = pixel*(160/getDensityDpi());
            return dp;
        } catch (Exception ex) {
            AppLogger.Logger.error("Screen height",ex.getMessage(),ex);
        }
        return pixel;
    }

    public int DpToPixel(int dp) {
        try {
            int pixel = dp*(getDensityDpi()/160);
            return pixel;
        } catch (Exception ex) {
            AppLogger.Logger.error("Screen height",ex.getMessage(),ex);
        }
        return dp;
    }

}