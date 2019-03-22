package com.scubasnsi.mysnsi.controllers.logbook.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.AppGlobalConstant;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.app.utilities.userOnlineInfo;
import com.scubasnsi.mysnsi.controllers.home.activities.HomeActivity;
import com.scubasnsi.mysnsi.controllers.logbook.background.SyncLogbook;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveDateFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveInfoFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveSiteInfoFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveSiteLocationFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveSitePictureFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveTypeFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveWeatherInfoFragment;
import com.scubasnsi.mysnsi.model.dao.LogBookDao;
import com.scubasnsi.mysnsi.model.dao.impl.LogbookDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.receivers.ConnectivityReceiver;

import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;

public class LogBookActivity extends AppCompatActivity implements UpdateLogbookHeader, ConnectivityReceiver.ConnectivityReceiverListener {


    private final String TAG = LogBookActivity.class.getName();
    private Context mContext;
    //Header

    @BindView(R.id.toolbar_logbook_left_cancel_image_id)
    ImageView mLeftCancel;
    @BindView(R.id.toolbar_logbook_right_share_image_id)
    Button mLeftshare;

    @BindView(R.id.toolbar_logbook_title_id)
    TextView mTVTitle;

    @BindView(R.id.toolbar_logbook_back_layout_id)
    LinearLayout mLLBack;
    @BindView(R.id.logback_next_btn)
    Button mNextButton;

    NotificationManager nManager;
    NotificationCompat.Builder ncomp;
    int id=1;
    /* Logbook fragments */
    private DiveTypeFragment mDiveTypeFragment;
    private DiveDateFragment mDiveDateFragment;
    private DiveSiteLocationFragment mDiveSiteLocationFragment;
    private DiveSiteInfoFragment mDiveSiteInfoFragment;
    private DiveInfoFragment mDiveInfoFragment;
    private DiveWeatherInfoFragment mDiveWeatherInfoFragment;
    private DiveSitePictureFragment mDiveSitePictureFragment;

    private Fragment mCurrentFragment;
    private String mCurrentFragmentTag;
    private Logbook mLogbook;

    private UserSession mUserSession;
    private CustomProgressBar mCustomProgressBar;
    private HomeService mHomeService;
    private LogBookDao mLogBookDao;
    private boolean isForEdit;
    private BroadcastReceiver mSyncBroadCastReceiver;
    IntentFilter intentFilter = new IntentFilter(AppGlobalConstant.INTENT_FILTER_SYNC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook);

        mContext = this;
        ButterKnife.bind(this);
        mCustomProgressBar = new CustomProgressBar(mContext);
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mHomeService = new HomeServicesImpl();
        mLogBookDao = new LogbookDaoImpl();

        MyApplication.getApplicationInstance().setConnectivityListener(this);
        if (getIntent() == null || getIntent().getExtras() == null || getIntent().getExtras().getSerializable(BUNDLE_SERIALIZED_OBJECT) == null) {

            isForEdit = false;
            Logbook logbook = new Logbook();
            setLogbook(logbook);

        } else {
            isForEdit = true;
            Logbook logbook = (Logbook) getIntent().getExtras().getSerializable(BUNDLE_SERIALIZED_OBJECT);
            setLogbook(logbook);
        }
        initializeView();
    }

    private void initializeView() {
        mDiveTypeFragment = DiveTypeFragment.newInstance(getLogbook());
        setFragment(mDiveTypeFragment, mDiveTypeFragment.TAG, false);

        //BroadCast receiver
        mSyncBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Update logbook Id here
                if (getLogbook() != null && intent != null && intent.getExtras() != null) {
                    Bundle bundle = intent.getExtras();
                    if (getLogbook().getLogBackId() == bundle.getLong(AppGlobalConstant.TEMP_LOG_BOOK_ID)) {
                        getLogbook().setLogBackId(bundle.getLong(AppGlobalConstant.LOG_BOOK_ID));
                    }
                }
            }
        };

    }

    private void registerReceiver() {
        //registering our receiver
        if (mSyncBroadCastReceiver != null) {
            this.registerReceiver(mSyncBroadCastReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (mSyncBroadCastReceiver != null)
            this.unregisterReceiver(this.mSyncBroadCastReceiver);
    }


    /* Handling of fragment stack */
    @Override
    public void onBackPressed() {
        switch (mCurrentFragmentTag) {
            case DiveTypeFragment.TAG:
                knowledgeParentActivity();
                return;
            case DiveDateFragment.TAG:
                mCurrentFragmentTag = DiveTypeFragment.TAG;
                break;
            case DiveSiteLocationFragment.TAG:
                mCurrentFragmentTag = DiveDateFragment.TAG;
                break;
            case DiveSiteInfoFragment.TAG:
                mCurrentFragmentTag = DiveSiteLocationFragment.TAG;
                break;
            case DiveInfoFragment.TAG:
                mCurrentFragmentTag = DiveSiteInfoFragment.TAG;
                break;
            case DiveWeatherInfoFragment.TAG:
                mCurrentFragmentTag = DiveInfoFragment.TAG;
                break;
            case DiveSitePictureFragment.TAG:
                mCurrentFragmentTag = DiveWeatherInfoFragment.TAG;
                mNextButton.setText(getString(R.string.next));
                break;
        }
        super.onBackPressed();

    }


    @Override
    public void OnUpdateHeader(boolean isBackShown, String title) {
        mLeftCancel.setVisibility(View.GONE);
        mLeftshare.setVisibility(View.GONE);
        mLLBack.setVisibility(View.GONE);
        if (isBackShown) {
            mLLBack.setVisibility(View.VISIBLE);
        } else {
            mLeftCancel.setVisibility(View.VISIBLE);
            //mLeftshare.setVisibility(View.VISIBLE);
        }
        mTVTitle.setText(title);
    }

    @OnClick(R.id.toolbar_logbook_back_layout_id)
    protected void back(View v) {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_logbook_left_cancel_image_id)
    protected void cancelOrFinish(View v) {
        onBackPressed();
    }


    /* Handling of fragment next functionality  */
    @OnClick(R.id.logback_next_btn)
    protected void next(View v) {
        switch (mCurrentFragmentTag) {
            case DiveTypeFragment.TAG:
                DiveTypeFragmentNext();
                return;
            case DiveDateFragment.TAG:
                DiveDateFragmentNext();
                break;
            case DiveSiteLocationFragment.TAG:
                DiveLocationFragmentNext();
                break;
            case DiveSiteInfoFragment.TAG:
                DiveSiteInfoFragmentNext();
                break;
            case DiveInfoFragment.TAG:
                DiveInfoFragmentNext();
                break;
            case DiveWeatherInfoFragment.TAG:
                DiveWeatherInfoFragmentNext();

                break;
            case DiveSitePictureFragment.TAG:
                DiveSitePictureFragmentNext();

                break;
        }
    }

    /**
     * Next Button Handling
     */
    //Type
    private void DiveTypeFragmentNext() {
        Logbook logbook = mDiveTypeFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveDateFragment = DiveDateFragment.newInstance(getLogbook());
        setFragment(mDiveDateFragment, DiveDateFragment.TAG, true);

    }

    //Date
    private void DiveDateFragmentNext() {
        Logbook logbook = mDiveDateFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveSiteLocationFragment = DiveSiteLocationFragment.newInstance(getLogbook());
        setFragment(mDiveSiteLocationFragment, DiveSiteLocationFragment.TAG, true);
    }

    //Location
    private void DiveLocationFragmentNext() {
        Logbook logbook = mDiveSiteLocationFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveSiteInfoFragment = DiveSiteInfoFragment.newInstance(getLogbook());
        setFragment(mDiveSiteInfoFragment, DiveSiteInfoFragment.TAG, true);
    }

    //site info
    private void DiveSiteInfoFragmentNext() {
        Logbook logbook = mDiveSiteInfoFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveInfoFragment = DiveInfoFragment.newInstance(getLogbook());
        setFragment(mDiveInfoFragment, DiveInfoFragment.TAG, true);
    }

    //dive info
    private void DiveInfoFragmentNext() {
        Logbook logbook = mDiveInfoFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveWeatherInfoFragment = DiveWeatherInfoFragment.newInstance(getLogbook());
        setFragment(mDiveWeatherInfoFragment, DiveWeatherInfoFragment.TAG, true);

    }

    //Weather  info
    private void DiveWeatherInfoFragmentNext() {
        Logbook logbook = mDiveWeatherInfoFragment.next();
        if (logbook == null)
            return;
        setLogbook(logbook);
        mDiveSitePictureFragment = DiveSitePictureFragment.newInstance(logbook);
        setFragment(mDiveSitePictureFragment, DiveSitePictureFragment.TAG, true);
        mNextButton.setText(getString(R.string.done));
    }

    //Image  info
    private void DiveSitePictureFragmentNext() {
        File file = mDiveSitePictureFragment.next();
        Logbook logbook = getLogbook();
       /* if(file ==null &&logbook.getDiveCenterLogo()!=null&&!logbook.getDiveCenterLogo().isEmpty())
            return;*/
        if (file != null && file.exists())
            logbook.setDiveCenterLogo(Uri.fromFile(file).toString());


        logbook.setUserId(mUserSession.getUserID());

      /*  if (isForEdit) {
            delete(logbook, file);
        } else {*/
        addDive(logbook, file);
        // }
    }


    //first add dive before update
    private void addDive(final Logbook logbook, final File file) {

        /* Consider the scenario :
         * 1. user created a new dive  dive , He/she is offline , by default isSync = true and by default isEdit =  false .
         *    means  -new dive added to database.
          * 2. User edit a log :
          *   2.1 already synced log - means update once at server - so by as default -  isSync - true, isEdit -false
           *      if (network is working) - > No issue
           *      else -> as this is already synced so- (isSync - true) ,.. it means server created log is updating here
           *              and
           *              logbook.setSync(false); &   logbook.setEdit(isForEdit); true if it is coming for edit
          *   2.1 un-synced log -
          *         means not update at server -  so by as default -  isSync - true, isEdit -false
          *         if (network is working) - > No issue
          *         else -> unSynced log -> (isSync - false) - not synced at server
          *         so --- logbook.setSync(false); &   logbook.setEdit(false);  set edit always false.
          *
          *         Do this only for offline process.
          * */
        if (!logbook.isSync()) {
            logbook.setEdit(false);
        } else {
            logbook.setEdit(isForEdit);
        }
        logbook.setSync(false);

        // Creating clone here
        try {
            Logbook clonedLogbook = (Logbook) logbook.clone();
            if (!clonedLogbook.isEdit()) {
                clonedLogbook.setLogBackId(0);
            }


            userOnlineInfo userOnlineInfo=new userOnlineInfo();
          if (userOnlineInfo.isOnline(this))
          {
              generateNotification();
          }
            saveLocal(logbook);
            knowledgeParentActivity();
            //Save Local

            //commented by me barinder
          /*  mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_saving));
            mHomeService.addDive(new ResponseCallBackHandler() {
                @Override
                public void returnResponse(ResponseHandler responseHandler) {
                    mCustomProgressBar.showHideProgressBar(false, null);
                    if (responseHandler.isExecuted()) {
                        Log.e("date",">>"+ logbook.getDiveDate());
                        saveLocal((Logbook) responseHandler.getValue());
                        try {
                            if (file != null)
                                file.delete();
                        } catch (Exception ex) {

                        }
                    } else {

                        saveLocal(logbook);
                        //Save Local
                    }
                    knowledgeParentActivity();
                }
            }, clonedLogbook, file);
        */} catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }




    }

    private void knowledgeParentActivity() {
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    //Save to  local database
    private void saveLocal(Logbook logbook) {
        //so Not impact here on
        if (isForEdit) {
            mLogBookDao.delete(logbook.getUserId(), logbook.getLogBackId());
        } else {
            //commented by me barinder
            logbook.setLogBackId(JavaUtility.getRandomId());
        }
        mLogBookDao.save(logbook);
    }


    public Logbook getLogbook() {
        return mLogbook;
    }

    public void setLogbook(Logbook logbook) {
        mLogbook = logbook;
    }

    private void setFragment(Fragment fragment, String tag, boolean addToStack) {
        try {
            if (fragment != null) {
                mCurrentFragment = fragment;
                mCurrentFragmentTag = tag;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.common_content, fragment, tag);
                if (addToStack)
                    ft.addToBackStack(tag);
                ft.commit();
            }
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected)
            GenericDialogs.showInformativeDialog("", R.string.network_error, mContext);
    }

    //onResume
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver();
    }

    //onPause
    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }
    public void generateNotification()
    {
        nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Syncing data");
        ncomp.setSmallIcon(R.mipmap.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify(id,ncomp.build());

    }
}


