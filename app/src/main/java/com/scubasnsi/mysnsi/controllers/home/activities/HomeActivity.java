package com.scubasnsi.mysnsi.controllers.home.activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.AppGlobalConstant;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.userOnlineInfo;
import com.scubasnsi.mysnsi.controllers.home.fragment.CCardsFragment;
import com.scubasnsi.mysnsi.controllers.home.fragment.CheckListFragment;
import com.scubasnsi.mysnsi.controllers.home.fragment.LogbookFragment;
import com.scubasnsi.mysnsi.controllers.home.fragment.ShowCardDetailFragment;
import com.scubasnsi.mysnsi.controllers.logbook.background.SyncLogbook;
import com.scubasnsi.mysnsi.model.dao.LogBookDao;
import com.scubasnsi.mysnsi.model.dao.impl.LogbookDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;
import com.scubasnsi.mysnsi.model.data_models.Courses;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.GenericRunnableHandler;
import shiva.joshi.common.helpers.RefreshRunnable;
import shiva.joshi.common.listeners.OnFragmentInteractionListener;
import shiva.joshi.common.logger.AppLogger;


public class HomeActivity extends AppCompatActivity implements UpdateHomeHeader, OnFragmentInteractionListener {

    public static ArrayList<Courses> coursesGlobal=new ArrayList<>();
    private final String TAG = HomeActivity.class.getName();
    private Context mContext;
    private User mUser;

    NotificationManager nManager;
    NotificationCompat.Builder ncomp;
    int id=1;
    @BindView(R.id.toolbar_home_left_image_id)
    ImageView mLeftIcon;
    @BindView(R.id.toolbar_home_left_new_image_id)
    ImageView mLeftIconNew;


    @BindView(R.id.toolbar_logbook_back_layout_id)
    LinearLayout mLeftBack;

    @BindView(R.id.toolbar_home_title_id)
    TextView mTVTitle;

    @BindView(R.id.toolbar_home_right_image_id)
    ImageView mRightIcon;

    @BindView(R.id.home_bottom_navigation_bar)
    BottomNavigationView mBottomNavigationView;

    private CCardsFragment mCCardsFragment;
    private CheckListFragment mCheckListFragment;
    private LogbookFragment mLogbookFragment;
    private ShowCardDetailFragment mShowCardDetailFragment;
    private RefreshRunnable mRefreshRunnable;
    private LogBookDao mLogBookDao;
    private UserSession mUserSession;

    boolean doubleBackToExitPressedOnce = false;
    private Fragment mCurrentFragment;
    private BroadcastReceiver mSyncBroadCastReceiver;
    IntentFilter intentFilter = new IntentFilter(AppGlobalConstant.INTENT_FILTER_SYNC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        ButterKnife.bind(this);
        mLogBookDao = new LogbookDaoImpl();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();


        initializeView();

//Add MenuItem with icon to Menu by me bar to hanle bottom bar for student and diver
       // mBottomNavigationView.getMenu().add(Menu.NONE, 1, Menu.NONE, "Home").setIcon(R.drawable.ic_home_black_24dp);

    }

    private void updateBottomBarAccordingToUser() {
        mUser = mUserSession.getLoggedUserData();
        if (mUser.getmIsStudent() == null || mUser.getmIsStudent().isEmpty())
            return;
      if (mUser.getmIsStudent().equalsIgnoreCase("no"))
      {
          setFragment(mCCardsFragment, mCCardsFragment.TAG, false);

          mBottomNavigationView.getMenu().add(Menu.NONE, 1, Menu.NONE, R.string.c_cards).setIcon(R.drawable.ic_ccards_inactive);
          mBottomNavigationView.getMenu().add(Menu.NONE, 2, Menu.NONE, R.string.logbook).setIcon(R.drawable.ic_logbook_inactive);
          mBottomNavigationView.getMenu().add(Menu.NONE, 3, Menu.NONE, R.string.check_list).setIcon(R.drawable.ic_checklist_inactive);
      }
      else
      {
          setFragment(mLogbookFragment, mLogbookFragment.TAG, false);
          mBottomNavigationView.getMenu().add(Menu.NONE, 2, Menu.NONE, R.string.logbook).setIcon(R.drawable.ic_logbook_inactive);
          mBottomNavigationView.getMenu().add(Menu.NONE, 3, Menu.NONE, R.string.check_list).setIcon(R.drawable.ic_checklist_inactive);
      }
    }


    private void initializeView() {


        mCCardsFragment = CCardsFragment.newInstance();
        mCheckListFragment = CheckListFragment.newInstance();
        mLogbookFragment = LogbookFragment.newInstance();
        updateBottomBarAccordingToUser();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                   /* case R.id.navigation_cards:
                        setFragment(mCCardsFragment, CCardsFragment.TAG, false);
                        return true;
                    case R.id.navigation_logbook:
                        setFragment(mLogbookFragment, CheckListFragment.TAG, false);
                        return true;
                    case R.id.navigation_checklist:
                        setFragment(mCheckListFragment, LogbookFragment.TAG, false);
                        return true;*/
                    case 1:
                        setFragment(mCCardsFragment, CCardsFragment.TAG, false);
                        return true;
                    case 2:
                        setFragment(mLogbookFragment, CheckListFragment.TAG, false);
                        return true;
                    case 3:
                        setFragment(mCheckListFragment, LogbookFragment.TAG, false);
                        return true;

                }
                return false;
            }

        });


        mSyncBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mCurrentFragment instanceof LogbookFragment) {
                    mLogbookFragment.refreshList(false,false);
                    return;
                }
            }
        };


    }






    private void registerReceiver() {
        //registering our receiver
        if (mSyncBroadCastReceiver != null){
            this.registerReceiver(mSyncBroadCastReceiver, intentFilter);
        }

    }

    private void unRegisterReceiver() {
        if (mSyncBroadCastReceiver != null)
            this.unregisterReceiver(this.mSyncBroadCastReceiver);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public void OnUpdateHeader(int leftIcon, String title, int rightIcon,int leftIconNew) {
        mLeftBack.setVisibility(View.GONE);
        mLeftIcon.setVisibility(View.GONE);
        mLeftIconNew.setVisibility(View.GONE);
        /*mLeftIcon.setVisibility(View.INVISIBLE);
        mLeftIconNew.setVisibility(View.INVISIBLE);*/
        mRightIcon.setVisibility(View.INVISIBLE);
        if (leftIcon != 0) {
            mLeftIcon.setVisibility(View.VISIBLE);
            mLeftIcon.setImageResource(leftIcon);
        }
        if (leftIconNew != 0) {
            mLeftIconNew.setVisibility(View.VISIBLE);
            mLeftIconNew.setImageResource(leftIconNew);
        }
        if (rightIcon != 0) {
            mRightIcon.setVisibility(View.VISIBLE);
            mRightIcon.setImageResource(rightIcon);
        }

        if (leftIcon == 0 && leftIconNew == 0) {
            mRightIcon.setVisibility(View.VISIBLE);
            mRightIcon.setImageResource(rightIcon);
            mLeftIconNew.setVisibility(View.INVISIBLE);
        }

        if (mCurrentFragment instanceof ShowCardDetailFragment) {
            mLeftBack.setVisibility(View.VISIBLE);
            mLeftIcon.setVisibility(View.GONE);
            mLeftIconNew.setVisibility(View.GONE);
        }

        mTVTitle.setText(title);
    }

    @OnClick(R.id.toolbar_home_left_image_id)
    protected void leftIconClick(View v) {
        if (mCurrentFragment instanceof CCardsFragment) {
            mCCardsFragment.openMyAccount();
            return;
        }
        if (mCurrentFragment instanceof LogbookFragment) {
            mLogbookFragment.refreshList(true,true);
            return;
        }
        /*if (mCurrentFragment instanceof CheckListFragment) {

            return;
        }*/
    }

    @OnClick(R.id.toolbar_home_left_new_image_id)
    protected void leftNewIconClick(View v) {
        if (mCurrentFragment instanceof LogbookFragment) {
            mLogbookFragment.openMyAccount();
            return;
        }

    }

    @OnClick(R.id.toolbar_home_right_image_id)
    protected void rightIconClick(View v) {
        if (mCurrentFragment instanceof CCardsFragment) {
            mCCardsFragment.refreshList();
            return;
        }
        if (mCurrentFragment instanceof LogbookFragment) {
            mLogbookFragment.addNewLog();
            return;
        }
        if (mCurrentFragment instanceof CheckListFragment) {
            mCheckListFragment.addNewItem();
            return;
        }

        if (mCurrentFragment instanceof ShowCardDetailFragment) {
            mShowCardDetailFragment.refresh();
            return;
        }
    }

    private void setFragment(Fragment fragment, String tag, boolean addToStack) {
        try {
            if (fragment != null) {
                mCurrentFragment = fragment;
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
    public void onFragmentInteraction(int id, Object value) {
        switch (id) {
            case R.id.card_image_view:
                mShowCardDetailFragment = ShowCardDetailFragment.newInstance((C_Cards) value);
                setFragment(mShowCardDetailFragment, ShowCardDetailFragment.TAG, true);
                break;
        }
    }

    @OnClick(R.id.toolbar_logbook_back_layout_id)
    protected void back() {
        mCurrentFragment = mCCardsFragment;
        OnUpdateHeader(R.drawable.ic_user, getString(R.string.c_cards), R.drawable.ic_refresh,0);
        super.onBackPressed();
    }


    //onResume
    @Override
    protected void onResume() {
        super.onResume();
        initializeHandler();
        registerReceiver();
    }

    //onPause
    @Override
    protected void onPause() {
        /*if (mRefreshRunnable != null) {
            mRefreshRunnable.stopRunnable();
            mRefreshRunnable = null;
        }*/
        super.onPause();
        unRegisterReceiver();
    }

    // save Dive here
    private void initializeHandler() {
        if (mUserSession == null || mUserSession.getUserID() <= 0)
            return;

        if (mRefreshRunnable == null) {
            mRefreshRunnable = new RefreshRunnable(new GenericRunnableHandler() {
                @Override
                public void onRun() {
                    // if There is not data to sync remove the runnable
                    if (mLogBookDao.unSyncCount(mUserSession.getUserID(), false) <= 0) {
                        mRefreshRunnable.stopRunnable();
                        return;
                    }
                    userOnlineInfo userOnlineInfo=new userOnlineInfo();
                    if (userOnlineInfo.isOnline(HomeActivity.this))
                    {
                        generateNotification();
                    }
                    AppLogger.Logger.debug(TAG, " Runnable ");
                    Intent intent = new Intent(HomeActivity.this, SyncLogbook.class);
                    startService(intent);

                }
            });
            mRefreshRunnable.run();
            mRefreshRunnable.setTimeInterval(1000 * 10);
        }
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
    public static ArrayList<Courses> getCoursesGlobal() {
        return coursesGlobal;
    }

    public static void setCoursesGlobal(ArrayList<Courses> coursesGlobal) {
        HomeActivity.coursesGlobal = coursesGlobal;
    }

}


