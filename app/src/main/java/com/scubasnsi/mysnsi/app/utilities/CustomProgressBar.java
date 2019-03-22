package com.scubasnsi.mysnsi.app.utilities;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

import shiva.joshi.common.logger.AppLogger;

import static android.content.ContentValues.TAG;

/**
 * Author - J.K.Joshi
 * Date -  04-01-2017.
 */

public class CustomProgressBar {
    private KProgressHUD mKProgressHUD;

    public CustomProgressBar(Context context) {
        if (mKProgressHUD == null)
            mKProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setMaxProgress(100)
                    .setCancellable(true);
    }

    public void showHideProgressBar(boolean showHide, String label) {
        if (mKProgressHUD == null)
            return;
        try {
            if (showHide) {
                mKProgressHUD.setLabel(label);
                mKProgressHUD.show();
            } else {
                if (mKProgressHUD.isShowing())
                    mKProgressHUD.dismiss();
            }
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

    }
}
