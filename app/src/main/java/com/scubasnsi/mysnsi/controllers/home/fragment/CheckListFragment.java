package com.scubasnsi.mysnsi.controllers.home.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.home.adapter.CheckListAdp;
import com.scubasnsi.mysnsi.model.dao.CheckListDao;
import com.scubasnsi.mysnsi.model.dao.impl.CheckListDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.CheckList;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.callbacks.GenericInputDialogBoxCallback;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.listeners.OnItemClickListener;
import shiva.joshi.common.utilities.CommonGenericDialogs;

public class CheckListFragment extends Fragment implements CheckListAdp.OnItemOptionListener {

    public static final String TAG = CheckListFragment.class.getName();
    private Context mContext;


    private UpdateHomeHeader mUpdateHomeHeader;
    private UserSession mUserSession;

    @BindString(R.string.check_list)
    protected String mTitle;
    @BindView(R.id.common_recycler_view_id)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.common_no_data_found_id)
    protected TextView mTVNoDataFound;

    private ArrayList<CheckList> mCheckList = new ArrayList<>();
    private CheckListAdp mCheckListAdp;
    private CheckListDao mCheckListDao;


    public static CheckListFragment newInstance() {
        CheckListFragment fragment = new CheckListFragment();
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
        mCheckListDao = new CheckListDaoImpl();
        mUserSession = MyApplication.getApplicationInstance().getUserSession();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_list, container, false);
        ButterKnife.bind(this, view);
        mUpdateHomeHeader.OnUpdateHeader(0, mTitle, R.drawable.ic_add,0);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<CheckList> lists = mCheckListDao.getList(mUserSession.getUserID());
        if (lists != null) {
            mCheckList.clear();
            mCheckList.addAll(lists);
        }

        initRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateHomeHeader = null;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCheckListAdp = new CheckListAdp(mContext, mCheckList);
        mRecyclerView.setAdapter(mCheckListAdp);
        mCheckListAdp.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Object obj) {


                itemSelection(position, (CheckList) obj);
            }
        });
        mCheckListAdp.setOnItemOptionListener(this);

    }

    //Update the item checked/unchecked
    private void itemSelection(final int position, CheckList checkList) {
        if (checkList == null)
            return;
        boolean isChecked = !checkList.isChecked();
        checkList.setChecked(isChecked);

        // mCheckListDao.saveOrUpdate(checkList);
        mCheckList.set(position, checkList);
        mCheckListAdp.notifyItemChanged(position);
    }

    //On Item Add
    private void onItemAdd(DialogInterface dialog, String value) {
        if (!GenericDialogs.isFieldValidAndShowValidMessage(value, R.string.check_list_new_item_verify_add, mContext)) {
            return;
        }
        dialog.dismiss();

        CheckList checkList = new CheckList();
        checkList.setChecked(false);
        checkList.setListId(JavaUtility.getRandomId());
        checkList.setName(value);
        mCheckListDao.saveOrUpdate(checkList, mUserSession.getUserID());

        mCheckList.add(checkList);
        mCheckListAdp.notifyItemInserted(mCheckList.size() - 1);

    }

    //On Item Add
    private void onItemDelete(int position) {
        CheckList checkList = mCheckList.get(position);
        mCheckListDao.delete(checkList.getListId(), mUserSession.getUserID());
        mCheckList.remove(position);
        mCheckListAdp.notifyItemRemoved(position);

    }

    //Add new item to list
    public void addNewItem() {
        GenericDialogs.getGenericInputDialog(mContext, getString(R.string.check_list_new_item_header_title), "", CommonGenericDialogs.OK, CommonGenericDialogs.CANCEL, true, new GenericInputDialogBoxCallback() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id, String value) {
                onItemAdd(dialog, value);
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onDelete(int pos, CheckList checkList) {
        onItemDelete(pos);
    }
}
