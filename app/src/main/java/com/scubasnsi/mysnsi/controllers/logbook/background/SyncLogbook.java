package com.scubasnsi.mysnsi.controllers.logbook.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.AppGlobalConstant;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.model.dao.LogBookDao;
import com.scubasnsi.mysnsi.model.dao.impl.LogbookDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.io.File;
import java.net.URI;
import java.util.List;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.logger.AppLogger;

/**
 * Author -
 * Date -  10-05-2017.
 */

public class SyncLogbook extends IntentService {
    public final static String TAG = SyncLogbook.class.getName();
    private HomeService mHomeService;
    private LogBookDao mLogBookDao;
    private UserSession mUserSession;
    NotificationManager nManager;
    NotificationCompat.Builder ncomp;
    int id=1;


    public SyncLogbook() {
        super(TAG);
        mHomeService = new HomeServicesImpl();
        mLogBookDao = new LogbookDaoImpl();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            saveDiveOffline();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    //first add dive before update
    private void saveDiveOffline() throws CloneNotSupportedException {
        if (mUserSession == null || mUserSession.getUserID() <= 0)
            return;

        List<Logbook> logbookList = mLogBookDao.getList(mUserSession.getUserID(), false);
        for (final Logbook logbook : logbookList) {

            AppLogger.Logger.info(TAG, logbook.toString());
            File file = null;
            if (!logbook.getDiveCenterLogo().isEmpty()) {
                try {
                    file = new File(URI.create(logbook.getDiveCenterLogo()));
                } catch (IllegalArgumentException ex) {
                    AppLogger.Logger.error(TAG, ex.getMessage());
                }
            }

            Logbook clonedLogbook = (Logbook) logbook.clone();
            if (!clonedLogbook.isEdit()) {
                clonedLogbook.setLogBackId(0);
            }

            mHomeService.addDiveSync(new ResponseCallBackHandler() {
                @Override
                public void returnResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                        //Delete local file if exist in folder
                        File file = null;
                        try {
                            file = new File(URI.create(logbook.getDiveCenterLogo()));
                            if (file.exists())
                                file.delete();
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            AppLogger.Logger.error(TAG, ex.getMessage());
                        }
                        mLogBookDao.delete(logbook.getUserId(), logbook.getLogBackId());

                        Logbook original = (Logbook) responseHandler.getValue();
                        mLogBookDao.save(original);
                   clearNotification();
                        refreshView(logbook, original);

                        return;
                    }


                }
            }, clonedLogbook, file);
        }

    }


    public void refreshView(Logbook temp, Logbook original) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putLong(AppGlobalConstant.TEMP_LOG_BOOK_ID, temp.getLogBackId());
        bundle.putLong(AppGlobalConstant.LOG_BOOK_ID, original.getLogBackId());
        intent.putExtras(bundle);
        intent.setAction("com.scubasnsi.mysnsi.SYNC_DIVE_LOGS_REFRESH");
        sendBroadcast(intent);

    }
   /* public void generateNotification()
    {
        nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("My Notification");
        ncomp.setContentText("Notification Listener Service Example");
        ncomp.setTicker("Notification Listener Service Example");
        ncomp.setSmallIcon(R.mipmap.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify(id,ncomp.build());

    }*/
    public void clearNotification()
    {
        nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(id);
    }
}
