package com.scubasnsi.mysnsi.controllers.home.adapter;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.scubasnsi.R;


public class IntroPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        ImageView imageView=(ImageView)view.findViewById(R.id.bannerimage);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]
            imageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }



    }
}
