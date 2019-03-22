package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.logger.AppLogger;


/**
 * Created by Joshi on 02-09-2016.
 */
public class LogbackAdp extends RecyclerView.Adapter<LogbackAdp.ViewHolder> {
    private final String TAG = LogbackAdp.class.getName();

    public interface OnItemOptionListener {
        void onDelete(int pos, Logbook logbook);

        void onEdit(int pos, Logbook logbook);

        void onShowDetail(int pos, Logbook logbook);
    }

    private List<Logbook> mLogbookList;
    private Context mContext;
    private UserPreference mUserPreference;
    private String mUnitMeter, mUnitFeet;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private OnItemOptionListener mOnItemOptionListener;


    public LogbackAdp(List<Logbook> notifications, Context context, UserPreference userPreference) {
        this.mLogbookList = notifications;
        this.mContext = context;
        this.mUserPreference = userPreference;
        mUnitMeter = mContext.getString(R.string.unit_meter);
        mUnitFeet = mContext.getString(R.string.unit_feet);
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.logback_item, viewGroup, false);
        return new ViewHolder(v, viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        try {
            Logbook logbook = mLogbookList.get(position);
            binderHelper.bind(viewHolder.swipeLayout, String.valueOf(logbook.getLogBackId()));
            viewHolder.mTvDate.setText(logbook.getDiveDate());
            viewHolder.mTvDiveName.setText(logbook.getDiveSiteName());
            switch (mUserPreference.getLengthType()) {
                case METER:
                    viewHolder.mTvMaxDepth.setText((logbook.getMaxDepth() == 0) ? "" : String.valueOf(logbook.getMaxDepth()) + mUnitMeter);
                    break;
                case FEET:
                    viewHolder.mTvMaxDepth.setText((logbook.getMaxDepth() == 0) ? "" : JavaUtility.formatUptoTwoDigit(JavaUtility.meterToFeet(logbook.getMaxDepth())) + mUnitFeet);
            }
            viewHolder.mTvDuration.setText(logbook.getDiveTotalTime() + "\'");


            viewHolder.mTVDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemOptionListener.onDelete(viewHolder.getAdapterPosition(), mLogbookList.get(viewHolder.getAdapterPosition()));

                }
            });
            viewHolder.mTVEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemOptionListener.onEdit(viewHolder.getAdapterPosition(), mLogbookList.get(viewHolder.getAdapterPosition()));
                }
            });

           viewHolder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemOptionListener == null) {
                        new RuntimeException("Please implement OnItemOptionListener to parent class ");
                    }
                    mOnItemOptionListener.onShowDetail(viewHolder.getAdapterPosition(), mLogbookList.get(viewHolder.getAdapterPosition()));
                }
            });


            String gasType = logbook.getGasType();
            if (!gasType.isEmpty()) {
                String[] stringArray = gasType.split("-");
                if (stringArray.length > 0) {
                    gasType = (stringArray[0].isEmpty()) ? "" : (stringArray[0].substring(0, 1).toUpperCase() + stringArray[0].substring(1));
                }
            }
            viewHolder.mTvMix.setText(gasType);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

    }


    public void setOnItemOptionListener(OnItemOptionListener onItemOptionListener) {
        mOnItemOptionListener = onItemOptionListener;
    }



    @Override
    public int getItemCount() {
        return mLogbookList.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logbook_item_swipe_layout)
        protected SwipeRevealLayout swipeLayout;

        @BindView(R.id.logback_item_swipe_option_delete)
        protected TextView mTVDelete;
        @BindView(R.id.logback_item_swipe_option_edit)
        protected TextView mTVEdit;
        @BindView(R.id.item_root_ll)
        protected LinearLayout mRoot;


        @BindView(R.id.logback_item_mix)
        protected TextView mTvMix;
        @BindView(R.id.logback_item_dive_name)
        protected TextView mTvDiveName;
        @BindView(R.id.logback_item_max_depth_tv)
        protected TextView mTvMaxDepth;
        @BindView(R.id.logback_item_duration_tv)
        protected TextView mTvDuration;
        @BindView(R.id.logback_item_date_tv)
        protected TextView mTvDate;

        public ViewHolder(View drawerItem, int itemType) {
            super(drawerItem);
            ButterKnife.bind(ViewHolder.this, drawerItem);

        }

    }

}