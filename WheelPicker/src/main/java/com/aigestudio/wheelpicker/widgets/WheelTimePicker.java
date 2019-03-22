package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.model.City;
import com.aigestudio.wheelpicker.model.Province;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * WheelAreaPicker
 * Created by Administrator on 2016/9/14 0014.
 */
public class WheelTimePicker extends LinearLayout implements IWheelTimePicker {
    private static final float ITEM_TEXT_SIZE = 18;
    private static final String SELECTED_ITEM_COLOR = "#353535";
    private static final int PROVINCE_INITIAL_INDEX = 0;

    private Context mContext;

    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<String> mCityName;
    private List<Integer> mHoursList = new ArrayList<>();

    private AssetManager mAssetManager;

    private LayoutParams mLayoutParams;

    private WheelPicker mWPHours, mWPMinutes, mWPAM_PM;
    private List<Integer> mMinutesList = new ArrayList();
    private List<String> mAM_PMList = new ArrayList<>();
    private int mLastHour;

    public WheelTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayoutParams();

        initView(context);

        mProvinceList = getJsonDataFromAssets(mAssetManager);

        obtainProvinceData();

        addListenerToWheelPicker();
    }

    @SuppressWarnings("unchecked")
    private List<Province> getJsonDataFromAssets(AssetManager assetManager) {
        List<Province> provinceList = null;
        try {
            InputStream inputStream = assetManager.open("RegionJsonData.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            provinceList = (List<Province>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceList;
    }

    private void initLayoutParams() {
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(5, 5, 5, 5);
        mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);

        mContext = context;

        mAssetManager = mContext.getAssets();

        mHoursList = new ArrayList<>();
        mCityName = new ArrayList<>();

        mWPHours = new WheelPicker(context);
        mWPMinutes = new WheelPicker(context);
        mWPAM_PM = new WheelPicker(context);

        initWheelPicker(mWPHours, 1.5f);
        initWheelPicker(mWPMinutes, 1.5f);
        initWheelPicker(mWPAM_PM, 1.5f);
        mWPHours.setCyclic(true);
        mWPMinutes.setCyclic(true);

    }

    private void initWheelPicker(WheelPicker wheelPicker, float weight) {
        mLayoutParams.weight = weight;
        wheelPicker.setItemTextSize(dip2px(mContext, ITEM_TEXT_SIZE));
        wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
        wheelPicker.setCurved(true);
        wheelPicker.setLayoutParams(mLayoutParams);
        addView(wheelPicker);
    }

    private void obtainProvinceData() {
        for (int i = 0; i < 12; i++) {
            mHoursList.add(i + 1);
        }
        for (int i = 0; i < 60; i++) {
            mMinutesList.add(i);
        }
        mAM_PMList.add("AM");
        mAM_PMList.add("PM");

        mWPHours.setData(mHoursList);
        mWPMinutes.setData(mMinutesList);
        mWPAM_PM.setData(mAM_PMList);

    }

    private void addListenerToWheelPicker() {
        mWPHours.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int sSelectedHour = (int) data;

                if (mLastHour == 1 && sSelectedHour == 12) {
                    if ((mWPAM_PM.getSelectedItemPosition() - 1) == -1) {
                        int sAmPmPosition = mAM_PMList.size() - 1;
                        mWPAM_PM.setSelectedItemPosition(sAmPmPosition, true);
                    } else {
                        mWPAM_PM.setSelectedItemPosition((mWPAM_PM.getSelectedItemPosition() - 1), true);
                    }
                } else if(mLastHour == 12 && sSelectedHour == 1) {
                    if ((mWPAM_PM.getSelectedItemPosition() + 1) == mAM_PMList.size()) {
                        int sAmPmPosition = 0;
                        mWPAM_PM.setSelectedItemPosition(sAmPmPosition, true);
                    } else {
                        mWPAM_PM.setSelectedItemPosition((mWPAM_PM.getSelectedItemPosition() + 1), true);
                    }

                }
                mLastHour = sSelectedHour;
            }
        });

        mWPMinutes.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {

            }
        });
        mWPAM_PM.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {

            }
        });
    }


    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public int getHour() {
        return mHoursList.get(mWPHours.getCurrentItemPosition());
    }

    @Override
    public int getMinute() {
        return mMinutesList.get(mWPMinutes.getCurrentItemPosition());
    }

    @Override
    public String getAMPM() {
        return mAM_PMList.get(mWPAM_PM.getCurrentItemPosition());
    }
}
