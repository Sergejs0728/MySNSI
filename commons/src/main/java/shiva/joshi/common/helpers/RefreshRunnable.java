package shiva.joshi.common.helpers;

import android.os.Handler;

import shiva.joshi.common.callbacks.GenericRunnableHandler;


/**
 * Author - J.K.Joshi
 * Date -  07-12-2016.
 */

public class RefreshRunnable implements Runnable {

    private static final long TIME_REPEAT_INTERVAL = 1000 * 60;
    private Handler mHandler;
    private GenericRunnableHandler mGenericRunnableHandler;
    private long mTimeInterval;

    public RefreshRunnable(GenericRunnableHandler genericRunnableHandler) {
        mGenericRunnableHandler = genericRunnableHandler;
        this.mHandler = new Handler();
    }

    @Override
    public void run() {
        try {
            mGenericRunnableHandler.onRun();
        } finally {
            // 100% guarantee that this always happens, even if
            // your update method throws an exception
            if (mTimeInterval == 0)
                mHandler.postDelayed(this, TIME_REPEAT_INTERVAL);
            else
                mHandler.postDelayed(this, mTimeInterval);
        }
    }

    public void stopRunnable() {
        mHandler.removeCallbacks(this);
    }

    public void setTimeInterval(long timeInterval) {
        mTimeInterval = timeInterval;
    }
}
