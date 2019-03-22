package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;

public class ViewPagerAdapter extends PagerAdapter {

    private ImageLoader mImageLoader;
    private Integer[] mResources;
    private Context mContext;
    ImageView imageView;

    public ViewPagerAdapter(Context context, Integer[] images) {
        this.mContext = context;
        this.mResources = images;
        mImageLoader = MyApplication.getApplicationInstance().getImageLoaderInstance(context, MyApplication.getApplicationInstance().getDisplayImageOptions(R.drawable.splash_background));
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);
        imageView = (ImageView) itemView.findViewById(R.id.bannerimage);
        int img = mResources[position];
        mImageLoader.displayImage("drawable://" + img, imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
