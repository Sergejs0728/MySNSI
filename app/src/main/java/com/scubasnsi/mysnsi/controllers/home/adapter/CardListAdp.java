package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;
import com.scubasnsi.mysnsi.rest.RestApiInterface;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shiva.joshi.common.listeners.OnItemClickListener;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.AppDirectoryImpl;


/**
 * Created by Joshi on 02-09-2016.
 */
public class CardListAdp extends RecyclerView.Adapter<CardListAdp.ViewHolder> {
    private final String TAG = CardListAdp.class.getName();


    private List<C_Cards> mCardsList;
    private Context mContext;

    private ImageLoader mImageLoader;
    private OnItemClickListener mOnItemClickListener;
    private AppDirectoryImpl mAppDirectory;


    public CardListAdp(List<C_Cards> notifications, Context context) {
        this.mCardsList = notifications;
        this.mContext = context;
        mAppDirectory = new AppDirectoryImpl(mContext,AppDirectoryImpl.MODE_CACHE, mContext.getString(R.string.app_name), mContext.getResources().getStringArray(R.array.directory_sub_folders));
        File file = new File(mAppDirectory.getPath(mContext.getString(R.string.directory_sub_folders_images_c_cards)));
        mImageLoader = MyApplication.getApplicationInstance().getImageLoaderInstance(mContext,file, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.card_items, viewGroup, false);
        return new ViewHolder(v, viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        try {
            C_Cards cCards = mCardsList.get(position);
            if (cCards.getCourseUrl() != null && !cCards.getCourseUrl().isEmpty()) {

                mImageLoader.displayImage(RestApiInterface.IMAGE_URL + cCards.getCourseUrl(), viewHolder.mImVCardImage, MyApplication.getApplicationInstance().getDisplayImageOptions(R.drawable.ic_card_default_image), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

    }

    @Override
    public int getItemCount() {
        return mCardsList.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.card_image_view)
        ImageView mImVCardImage;
        @BindView(R.id.card_progress_bar)
        ProgressBar mProgressBar;

        public ViewHolder(View drawerItem, int itemType) {
            super(drawerItem);
            ButterKnife.bind(ViewHolder.this, drawerItem);
            drawerItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mOnItemClickListener == null)
                throw new RuntimeException("OnItemClickListener is null,need to set from class.");

            mOnItemClickListener.onItemClickListener(getAdapterPosition(), mCardsList.get(getAdapterPosition()));
        }
    }

}