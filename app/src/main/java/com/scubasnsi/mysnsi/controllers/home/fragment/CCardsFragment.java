package com.scubasnsi.mysnsi.controllers.home.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.home.activities.MyAccountActivity;
import com.scubasnsi.mysnsi.controllers.home.adapter.CardListAdp;
import com.scubasnsi.mysnsi.model.dao.CardListDao;
import com.scubasnsi.mysnsi.model.dao.impl.CardDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;
import com.scubasnsi.mysnsi.model.dto.C_CardsListDto;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.listeners.OnFragmentInteractionListener;
import shiva.joshi.common.listeners.OnItemClickListener;
import shiva.joshi.common.utilities.AppDirectoryImpl;


@RuntimePermissions
public class CCardsFragment extends Fragment implements OnItemClickListener {

    public static final String TAG = CCardsFragment.class.getName();


    private Context mContext;
    private UserSession mUserSession;
    private CustomProgressBar mCustomProgressBar;
    private HomeService mHomeService;
    private AppDirectoryImpl mAppDirectory;

    //Header

    @BindString(R.string.c_cards)
    protected String mTitle;

    @BindView(R.id.c_card_swipe_container)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.common_recycler_view_id)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.common_no_data_found_id)
    protected TextView mTVNoData;

    private CardListAdp mCardListAdp;
    private ArrayList<C_Cards> mCardsList = new ArrayList<>();

    private UpdateHomeHeader mUpdateHomeHeader;
    private OnFragmentInteractionListener mOnFragmentInteractionListener;
    int mPageNumber = 0;
    private CardListDao mCardListDao;


    public static CCardsFragment newInstance() {
        CCardsFragment fragment = new CCardsFragment();
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

        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement mOnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mHomeService = new HomeServicesImpl();
        String[] directoryFolders = getResources().getStringArray(R.array.directory_sub_folders);
        mAppDirectory = new AppDirectoryImpl(mContext,AppDirectoryImpl.MODE_CACHE, getString(R.string.app_name),directoryFolders);


        mCustomProgressBar = new CustomProgressBar(mContext);
        mCardListDao = new CardDaoImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ccards, container, false);

        ButterKnife.bind(this, view);

        mUpdateHomeHeader.OnUpdateHeader(R.drawable.ic_user, mTitle, R.drawable.ic_refresh,0);
        return view;

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
                        getCardList(mUserSession.getUserID(), mPageNumber, true);

                    }
                }
        );
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCardListAdp = new CardListAdp(mCardsList, mContext);
        mCardListAdp.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mCardListAdp);

        List<C_Cards> list = mCardListDao.getList();
        if (list == null || list.size() <= 0)
            getCardList(mUserSession.getUserID(), mPageNumber, false);
        else
            updateListView(list);
    }

    //Open My account
    public void openMyAccount() {
        ChangeActivityHelper.changeActivity(getActivity(), MyAccountActivity.class, false);
    }

    //Refresh list
    public void refreshList() {
        getCardList(mUserSession.getUserID(), mPageNumber, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateHomeHeader = null;
    }

    private void saveListToDataBase(List<C_Cards> c_cardsList) {

        try {
            mCardListDao.saveList(c_cardsList);
            MyApplication.getApplicationInstance().clearCache(mContext,new File(mAppDirectory.getPath(getString(R.string.directory_sub_folders_images_c_cards))));
            updateListView(c_cardsList);
        }
        catch (Exception e)
        {

        }

    }

    //getOrder
    private void getCardList(long userId, int pageNumber, boolean isRefresh) {
        C_CardsListDto c_cardsListDto = new C_CardsListDto(userId);
        mCustomProgressBar.showHideProgressBar(!isRefresh, getString(R.string.loading_downloading));
        mHomeService.getC_Cards(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                mSwipeRefreshLayout.setRefreshing(false);
                if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                    saveListToDataBase((List<C_Cards>) responseHandler.getValue());
                    return;
                }
                //commented nby me barinder
               /* GenericDialogs.showInformativeDialogWithTitle(getString(R.string.alert_box_title_header), getString(R.string.card_error), mContext);
                showErrorMessage();*/

               //added by me barinder
                else if(responseHandler.isExecuted() && responseHandler.getValue() == null)
                {
                    GenericDialogs.showInformativeDialogWithTitle(getString(R.string.alert_box_title_header), getString(R.string.card_error), mContext);
                    showErrorMessage();
                }
                GenericDialogs.showInformativeDialogWithTitle(getString(R.string.alert_box_title_header), getString(R.string.no_internet), mContext);
                showErrorMessage();
            }
        }, c_cardsListDto);
    }

    // Update list
    private void updateListView(List<C_Cards> logBacks) {
        if (logBacks.size() <= 0)
            return;
        mCardsList.clear();
        mCardsList.addAll(logBacks);
        mCardListAdp.notifyDataSetChanged();
        showErrorMessage();
    }

    //show error message
    private void showErrorMessage() {
        if (mCardsList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTVNoData.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTVNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClickListener(int position, Object obj) {
        CCardsFragmentPermissionsDispatcher.showCardWithCheck(CCardsFragment.this, position, obj);
    }

    //Ask for permission
    //Requesting permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CCardsFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }



    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    protected void showCard(int position, Object obj) {
        C_Cards cCards = mCardsList.get(position);
        mOnFragmentInteractionListener.onFragmentInteraction(R.id.card_image_view, cCards);

        /*Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_SERIALIZED_OBJECT, cCards);

        ChangeActivityHelper.changeActivity(getActivity(), ShowCardDetailActivity.class, false, bundle);*/

    }
}
