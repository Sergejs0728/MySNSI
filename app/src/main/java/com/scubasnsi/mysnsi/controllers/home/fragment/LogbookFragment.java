package com.scubasnsi.mysnsi.controllers.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.app.utilities.userOnlineInfo;
import com.scubasnsi.mysnsi.controllers.home.activities.MyAccountActivity;
import com.scubasnsi.mysnsi.controllers.home.adapter.LogbackAdp;
import com.scubasnsi.mysnsi.controllers.logbook.activities.LogBookActivity;
import com.scubasnsi.mysnsi.controllers.logbook.activities.LogBookDetailActivity;
import com.scubasnsi.mysnsi.model.dao.LogBookDao;
import com.scubasnsi.mysnsi.model.dao.impl.LogbookDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.dto.LogbookDeleteDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDto;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.CommonConstants;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;

public class LogbookFragment extends Fragment implements LogbackAdp.OnItemOptionListener {

    public static final String TAG = LogbookFragment.class.getName();
    public final int ADD_NEW = 3905;
    private Context mContext;
    private User mUser;


    private UpdateHomeHeader mUpdateHomeHeader;

    @BindView(R.id.logBack_swipe_container)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.common_recycler_view_id)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.common_no_data_found_id)
    protected TextView mTVNoData;

    @BindString(R.string.logbook)
    protected String mTitle;

    private CustomProgressBar mCustomProgressBar;
    private UserSession mUserSession;
    private UserPreference mUserPreference;
    private HomeService mHomeService;
    private LogbackAdp mLogbackAdp;
    private List<Logbook> mLogbookList = new ArrayList<>();
    private int mPageNumber = 0;
    private LogBookDao mLogBookDao;

    public static LogbookFragment newInstance() {
        LogbookFragment fragment = new LogbookFragment();
        Bundle args = new Bundle();
        //args.putSerializable(BUNDLE_SERIALIZED_OBJECT, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateHomeHeader) {
            mUpdateHomeHeader = (UpdateHomeHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateHomeHeader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mLogBookDao = new LogbookDaoImpl();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mUserPreference = mUserSession.getUserPreference();
        mHomeService = new HomeServicesImpl();
        mCustomProgressBar = new CustomProgressBar(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_logback, container, false);
        ButterKnife.bind(this, view);
        mLogbookList.clear();
        updateToolBar();
        return view;

    }
    private void updateToolBar() {
        mUser = mUserSession.getLoggedUserData();
        if (mUser.getmIsStudent() == null || mUser.getmIsStudent().isEmpty())
            return;
        if (mUser.getmIsStudent().equalsIgnoreCase("no"))
        {
            mUpdateHomeHeader.OnUpdateHeader(R.drawable.ic_refresh, mTitle, R.drawable.ic_add,0);

        }
        else
        {
            mUpdateHomeHeader.OnUpdateHeader(R.drawable.ic_refresh, mTitle, R.drawable.ic_add,R.drawable.ic_user);

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
            /*
             * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
             * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getLogbackList(mUserSession.getUserID(), mPageNumber, true, false);
                    }
                }
        );
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mLogbackAdp = new LogbackAdp(mLogbookList, mContext, mUserPreference);
        mLogbackAdp.setOnItemOptionListener(this);

        mRecyclerView.setAdapter(mLogbackAdp);
        if (mLogbookList.size() <= 0)
            getLogbackList(mUserSession.getUserID(), mPageNumber, false, true);
    }


    //Open My account
    public void openMyAccount() {
        ChangeActivityHelper.changeActivity(getActivity(), MyAccountActivity.class, false);
    }
    //getOrder
    /*
        Refresh only : when needed or local stored list has 0 count .
        else show from DB.
     *
     */
    private void getLogbackList(long userId, int pageNumber, boolean isRefresh, boolean showBar) {
        List<Logbook> logbookList = mLogBookDao.getList(userId);
        if (logbookList.size() > 0 && !isRefresh) {
            updateListView(logbookList);
        } else {
            LogbookDto logbookDto = new LogbookDto(userId);
            //commented by me barinder
            mCustomProgressBar.showHideProgressBar(showBar, getString(R.string.loading_get_dives));
            mHomeService.getLogbackDives(new ResponseCallBackHandler() {
                @Override
                public void returnResponse(ResponseHandler responseHandler) {
                    mCustomProgressBar.showHideProgressBar(false, null);
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                        save((List<Logbook>) responseHandler.getValue());
                        return;
                    }
                    else if(responseHandler.isExecuted() && responseHandler.getValue() == null) {
                        GenericDialogs.showInformativeDialog("", R.string.cant_download_logs, mContext);
                        showErrorMessage();
                    }
                    GenericDialogs.showInformativeDialogWithTitle(getString(R.string.alert_box_title_header), getString(R.string.no_internet), mContext);
                    showErrorMessage();
                }
            }, logbookDto);
        }
    }

    private void save(List<Logbook> logbookList) {
        mLogBookDao.save(logbookList);
        updateListView(mLogBookDao.getList(mUserSession.getUserID()));
    }

    // Update list
    private void updateListView(List<Logbook> logBacks) {
       /* if (logBacks.size() <= 0) {
            showErrorMessage();
            return;
        }*/

        mLogbookList.clear();
        mLogbookList.addAll(logBacks);
        mLogbackAdp.notifyDataSetChanged();
        showErrorMessage();

      /*  isLoading = false;
        mPageNumber = mPageNumber + 1;*/
    }

    //show error message
    private void showErrorMessage() {
        if (mLogbookList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTVNoData.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTVNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateHomeHeader = null;
    }


    //Add new log
    public void addNewLog() {
        ChangeActivityHelper.changeActivityForResultByFragment(this, LogBookActivity.class, ADD_NEW, null);
    }

    //Refresh list
    public void refreshList(boolean isRefresh, boolean showLoader) {
        getLogbackList(mUserSession.getUserID(), mPageNumber, isRefresh, showLoader);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_NEW:
                    getLogbackList(mUserSession.getUserID(), mPageNumber, false, true);
                    break;
            }
        }
    }


    /* Items */
    private void updateListAfterDelete(int pos, Logbook logbook) {
        mLogBookDao.delete(logbook.getUserId(), logbook.getLogBackId());
        mLogbookList.remove(pos);
        mLogbackAdp.notifyItemRemoved(pos);
    }

    @Override
    public void onDelete(final int pos, final Logbook logbook) {
        if (!logbook.isSync() && !logbook.isEdit()) {
            mLogBookDao.delete(logbook.getUserId(), logbook.getLogBackId());
            return;
        }
        LogbookDeleteDto logbookDto = new LogbookDeleteDto(mUserSession.getUserID(), logbook.getLogBackId());
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_deleting));
        mHomeService.deleteLogs(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    mLogBookDao.delete(logbook.getUserId(), logbook.getLogBackId());
                    updateListAfterDelete(pos, logbook);
                    return;
                }

                //added by me barinder
                userOnlineInfo userOnlineInfo = new userOnlineInfo();
                if (!userOnlineInfo.isOnline(getContext()))
                {
                    GenericDialogs.showInformativeDialogWithTitle(getString(R.string.alert_box_title_header), getString(R.string.no_internet), mContext);
                    showErrorMessage();
                 }
                    else
                   GenericDialogs.showInformativeDialog("", R.string.cant_delete_logs, mContext);

               /*commented by me barinder
                GenericDialogs.showInformativeDialog("", R.string.cant_delete_logs, mContext);
                 */


               /*GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);*/
            }
        }, logbookDto);
    }

    @Override
    public void onEdit(int pos, Logbook logbook) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConstants.BUNDLE_SERIALIZED_OBJECT, logbook);
        ChangeActivityHelper.changeActivityForResultByFragment(this, LogBookActivity.class, ADD_NEW, bundle);
    }

    @Override
    public void onShowDetail(int pos, Logbook logbook) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConstants.BUNDLE_SERIALIZED_OBJECT, logbook);
        ChangeActivityHelper.changeActivity((Activity) mContext, LogBookDetailActivity.class, false, bundle);

    }


}