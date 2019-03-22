package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.model.data_models.CheckList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.listeners.OnItemClickListener;
import shiva.joshi.common.logger.AppLogger;


/**
 * Created by Joshi on 02-09-2016.
 */
public class CheckListAdp extends RecyclerView.Adapter<CheckListAdp.ViewHolder> {
    private final String TAG = CheckListAdp.class.getName();

    public interface OnItemOptionListener {
        void onDelete(int pos, CheckList checkList);
    }

    private List<CheckList> mCheckLists;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemOptionListener mOnItemOptionListener;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public CheckListAdp(Context context, List<CheckList> notifications) {
        this.mCheckLists = notifications;
        this.mContext = context;
        binderHelper.setOpenOnlyOne(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.check_list_item, viewGroup, false);
        return new ViewHolder(v, viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        try {
            CheckList checkList = mCheckLists.get(position);
            binderHelper.bind(viewHolder.swipeLayout, String.valueOf(checkList.getListId()));
            viewHolder.mTVName.setText(checkList.getName());
            if (checkList.isChecked()) {
                viewHolder.mImVCheck.setImageResource(R.drawable.ic_checked);
            } else {
                viewHolder.mImVCheck.setImageResource(R.drawable.ic_unchecked);
            }

            viewHolder.mLLItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null)
                        throw new RuntimeException("You need to set OnItemClickListener.");
                    mOnItemClickListener.onItemClickListener(viewHolder.getAdapterPosition(), mCheckLists.get(viewHolder.getAdapterPosition()));

                }
            });
            viewHolder.mTVDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemOptionListener==null)
                        throw new RuntimeException("You need to set mOnItemOptionListener.");
                    mOnItemOptionListener.onDelete(viewHolder.getAdapterPosition(), mCheckLists.get(viewHolder.getAdapterPosition()));
                }
            });
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemOptionListener(OnItemOptionListener onItemOptionListener) {
        mOnItemOptionListener = onItemOptionListener;
    }

    @Override
    public int getItemCount() {
        return mCheckLists.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logbook_item_swipe_layout)
        protected SwipeRevealLayout swipeLayout;

        @BindView(R.id.check_list_item_layout_id)
        LinearLayout mLLItem;

        @BindView(R.id.check_list_item_image_id)
        ImageView mImVCheck;
        @BindView(R.id.check_list_item_name_id)
        TextView mTVName;
        @BindView(R.id.logback_item_swipe_option_delete)
        TextView mTVDelete;

        public ViewHolder(View drawerItem, int itemType) {
            super(drawerItem);
            ButterKnife.bind(ViewHolder.this, drawerItem);

        }

    }

}