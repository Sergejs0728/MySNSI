package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.model.PressureType;
import com.scubasnsi.mysnsi.model.TemperatureType;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shiva.joshi.common.callbacks.GenericInformativeDialogBoxCallback;
import shiva.joshi.common.customs.Screen;
import shiva.joshi.common.helpers.ExternalIntentHelper;
import shiva.joshi.common.java.DateTimeUtility;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.receivers.ConnectivityReceiver;

import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_TEMP_VALUE;
import static com.scubasnsi.mysnsi.app.AppGlobalConstant.MAP_ZOOM;
import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;
import static shiva.joshi.common.java.DateTimeUtility.convertDateFromOldToNewFormat;

public class LogbookDetailFragment extends Fragment {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.LogbookDetailFragment";
    private Context mContext;

    @BindString(R.string.dive_detail_title)
    protected String mTitle;
    @BindView(R.id.logbook_dive_image)
    protected ImageView mImVLogo;

    @BindView(R.id.logbook_location_image)
    protected ImageView mImVLocationImage;


    private UpdateLogbookHeader mUpdateLogbookHeader;
    private Logbook mLogbook;
    private ImageLoader mImageLoader;
    private LayoutInflater mLayoutInflater;
    private UserPreference mUserPreference;

    private View mView;


    public static LogbookDetailFragment newInstance(Logbook logbook) {
        LogbookDetailFragment diveTypeFragment = new LogbookDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        diveTypeFragment.setArguments(args);

        return diveTypeFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mUpdateLogbookHeader != null)
            mUpdateLogbookHeader.OnUpdateHeader(false, mTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLogbookHeader) {
            mUpdateLogbookHeader = (UpdateLogbookHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateHomeHeader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mImageLoader = MyApplication.getApplicationInstance().getImageLoaderInstance(mContext, null);
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mUserPreference = MyApplication.getApplicationInstance().getUserSession().getUserPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.f_logback_detail, container, false);

        ButterKnife.bind(this, mView);
        mUpdateLogbookHeader.OnUpdateHeader(false, mTitle);
        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }
        return mView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mLogbook.getDiveCenterLogo() != null && !mLogbook.getDiveCenterLogo().isEmpty()) {
            mImageLoader.displayImage(mLogbook.getDiveCenterLogo(), mImVLogo,MyApplication.getApplicationInstance().getDisplayImageOptions(R.drawable.ic_card_default_image), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    mImVLogo.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mImVLogo.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    mImVLogo.setVisibility(View.GONE);
                }
            });

        } else {
            mImVLogo.setVisibility(View.GONE);
        }

        //Dive type , Date and time
        setDiveType();
        //Show map Image
        showMapImage();
        //Dive site info
        showDiveSiteInfo();

    }

    @OnClick(R.id.logbook_dive_image)
    protected void showFullImage() {
        if (mLogbook.getDiveCenterLogo() != null && !mLogbook.getDiveCenterLogo().isEmpty()) {
            showImage(mContext, mLogbook.getDiveCenterLogo(), false, new GenericInformativeDialogBoxCallback() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
    }

    private Integer[] getResourcesIdFromDrawables(Context context, int arrayResourceId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(arrayResourceId);
        Integer[] drawableResourceIds = new Integer[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            drawableResourceIds[i] = typedArray.getResourceId(i, R.drawable.ic_arrow_downward);
        }
        typedArray.recycle();
        return drawableResourceIds;
    }

    /*Dive detail -
                   Date
				   Start time
				   End Time
				   Dive Duration
				   Dive for
				   Dive Type
				   Max Depth*/
    private void setDiveType() {
        String[] diveTypeString = getResources().getStringArray(R.array.dive_detail_dive_type);
        LinearLayout linearLayout = ButterKnife.findById(mView, R.id.logbook_detail_dive_type);
        for (int i = 0; i < diveTypeString.length; i++) {
            String value = "";
            switch (i) {
                case 0:
                    if (!mLogbook.getDiveDate().isEmpty()) //Date
                        value = convertDateFromOldToNewFormat(mLogbook.getDiveDate(), DateTimeUtility.DATE_F_YYYY_MM_DD, DateTimeUtility.DATE_F_DD_MM_YYYY);
                    break;
                case 1:
                    if (!mLogbook.getDiveStartTime().isEmpty()) { //Start time
                        value = DateTimeUtility.convertUTCFormatToLocal(mLogbook.getDiveStartTime(), DateTimeUtility.TIME_24_ss, DateTimeUtility.TIME_12);
                    }
                    break;
                case 2:
                    if (!mLogbook.getDiveEndTime().isEmpty()) { //End Time
                        value = DateTimeUtility.convertUTCFormatToLocal(mLogbook.getDiveEndTime(), DateTimeUtility.TIME_24_ss, DateTimeUtility.TIME_12);
                    }
                    break;
                case 3:
                    if (mLogbook.getDiveTotalTime() != null && !mLogbook.getDiveTotalTime().isEmpty())
                        value = mLogbook.getDiveTotalTime() + " " + getString(R.string.unit_time_min); // Total durations
                    break;
                case 4:
                    value = mLogbook.getDiveFor();  // Dive For
                    break;
                case 5:
                    value = mLogbook.getDiveType(); // Dive Type
                    break;
                case 6:
                    switch (mUserPreference.getLengthType()) { //Max depth
                        case METER:
                            value = (mLogbook.getMaxDepth() == 0) ? "" : String.valueOf(mLogbook.getMaxDepth()) + " " + getString(R.string.unit_meter);
                            break;
                        case FEET:
                            value = ((mLogbook.getMaxDepth() == 0) ? "" : JavaUtility.formatUptoTwoDigit(JavaUtility.meterToFeet(mLogbook.getMaxDepth()))) + " " + getString(R.string.unit_feet);
                    }

                    break;
            }
            addItemToLayout(diveTypeString[i], value, null, linearLayout);
        }
    }


    private void addItemToLayout(String name, String value, Drawable icon, LinearLayout linearLayout) {
        // inflate child
        View item = mLayoutInflater.inflate(R.layout.logback_detail_item, null);
        // info name
        TextView tvName = (TextView) item.findViewById(R.id.logback_item_name);
        tvName.setText(name);
        if (icon != null)
            tvName.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        //Info value
        TextView tvInfoValue = (TextView) item.findViewById(R.id.logback_item_value);
        tvInfoValue.setText(value);

        linearLayout.addView(item);
    }

    private void showMapImage() {
        if (mLogbook.getLatitude() != 0 && mLogbook.getLongitude() != 0) {
            String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + mLogbook.getLatitude() + "," + mLogbook.getLongitude() + "&zoom=13&size=1024x300&maptype=roadmap" +
                    "&markers=color:red%7Clabel:C%7C" + mLogbook.getLatitude() + "," + mLogbook.getLongitude() + "%0A&key=" + getString(R.string.google_maps_api_key);
            mImageLoader.displayImage(mapUrl, mImVLocationImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if(!ConnectivityReceiver.isConnected(mContext)){
                        mImVLocationImage.setImageResource(R.drawable.ic_not_download_map);
                        mImVLocationImage.setEnabled(false);
                    }

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mImVLocationImage.setEnabled(true);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if(!ConnectivityReceiver.isConnected(mContext)){
                        mImVLocationImage.setImageResource(R.drawable.ic_not_download_map);
                        mImVLocationImage.setEnabled(false);
                    }
                }
            });
        } else {
            mImVLocationImage.setVisibility(View.GONE);
            //Show no image location
        }

    }

    @OnClick(R.id.logbook_location_image)
    protected void openMap() {
        if (mLogbook.getLatitude() != 0 && mLogbook.getLongitude() != 0) {
            boolean isMapAvaileAble = ExternalIntentHelper.openMap(mContext, mLogbook.getLatitude(), mLogbook.getLongitude(), MAP_ZOOM, mLogbook.getDiveSiteName());
        }

    }

    /* Dive detail Card 4:
            Diving Center - 0
            Dive Site     - 1
            Instructor/guide which is the second now in the Dive Site block we do not need to see (<item>Instructor/Guide</item>)
            Buddy Name    - 2
            Instructor/Guide Name - 3
            Tank pressure start  -4
            Tank pressure end   -5
            Weight (without equipment) -6
            Suit  -7
            Gas Type : -8
             there is a mistake in gas type it should not show numbers when it's air. it should show only Air
            Air Temperature -9
			Water Temperature -10
			Weather  -11
			Visibility -12
      */
    private void showDiveSiteInfo() {
        String[] diveSiteString = getResources().getStringArray(R.array.dive_detail_dive_site_info);
        LinearLayout linearLayout = ButterKnife.findById(mView, R.id.logbook_detail_dive_site_name);
        for (int i = 0; i < diveSiteString.length; i++) {
            String value = "";
            switch (i) {
                case 0:
                    value = mLogbook.getDiveCenterName();
                    break;
                case 1:
                    value = mLogbook.getDiveSiteName();
                    break;
                case 2:
                    value = mLogbook.getBuddyName();
                    break;
                case 3:
                    value = mLogbook.getInstructorName();
                    break;
                case 4:
                    switch (mUserPreference.getPressureType()) {
                        case BAR:
                            if (mLogbook.getTankPressureStart() != DEFAULT_TEMP_VALUE) {
                                value = String.valueOf(mLogbook.getTankPressureStart()) + " " + PressureType.BAR.getValue();
                            }
                            break;
                        case PSI:
                            if (mLogbook.getTankPressureStart() != DEFAULT_TEMP_VALUE) {
                                value = JavaUtility.formatUptoTwoDigit(JavaUtility.barToPsi(mLogbook.getTankPressureStart())) + " " + PressureType.PSI.getValue();
                            }
                            break;
                    }
                    break;
                case 5:
                    switch (mUserPreference.getPressureType()) {
                        case BAR:
                            if (mLogbook.getTankPressureEnd() != DEFAULT_TEMP_VALUE) {
                                value = mLogbook.getTankPressureEnd() + " " + PressureType.BAR.getValue();
                            }
                            break;
                        case PSI:
                            if (mLogbook.getTankPressureEnd() != DEFAULT_TEMP_VALUE) {
                                value = JavaUtility.formatUptoTwoDigit(JavaUtility.barToPsi(mLogbook.getTankPressureEnd())) + " " + PressureType.PSI.getValue();
                            }
                            break;
                    }

                    break;
                case 6:
                    switch (mUserPreference.getWeightType()) {
                        case KILOGARM:
                            value = mLogbook.getEquipmentWeight() + " " + getString(R.string.unit_weight_kg);
                            break;
                        case POUND:
                            int weight = (int) JavaUtility.kilogramToPound(mLogbook.getEquipmentWeight());
                            value = weight + " " + getString(R.string.unit_weight_lbs);
                            break;
                    }
                    break;
                case 7:
                    value = mLogbook.getEquipmentSuit();
                    break;
                case 8:
                    String gasType = mLogbook.getGasType();
                    if (!gasType.isEmpty()) {
                        String[] gasTypeArray = gasType.split("-");
                        //check local values with gas array
                        if (getString(R.string.dive_info_gas_type_nitrox).equalsIgnoreCase(gasTypeArray[0])) {
                            value = mLogbook.getGasType();
                        } else {

                            char ch = Character.toUpperCase(gasTypeArray[0].charAt(0));
                            value = gasTypeArray[0].replace(gasTypeArray[0].charAt(0), ch);
                        }
                    }
                    break;
                case 9:
                    switch (mUserPreference.getTemperatureType()) {
                        case CELSIUS:
                            if (mLogbook.getAirTemperature() != DEFAULT_TEMP_VALUE) {
                                value = mLogbook.getAirTemperature() + " " + TemperatureType.CELSIUS.getValue();
                            }
                            break;

                        case FAHRENHEIT:
                            if (mLogbook.getAirTemperature() != DEFAULT_TEMP_VALUE) {
                                value = JavaUtility.formatUptoTwoDigit(JavaUtility.celsiusToFahrenheit(mLogbook.getAirTemperature())) + TemperatureType.FAHRENHEIT.getValue();
                            }
                            break;
                    }
                    break;
                case 10:
                    switch (mUserPreference.getTemperatureType()) {
                        case CELSIUS:
                            if (mLogbook.getWaterTemperature() != DEFAULT_TEMP_VALUE) {
                                value = mLogbook.getWaterTemperature() + " " + TemperatureType.CELSIUS.getValue();
                            }
                            break;

                        case FAHRENHEIT:
                            if (mLogbook.getWaterTemperature() != DEFAULT_TEMP_VALUE) {
                                value = JavaUtility.formatUptoTwoDigit(JavaUtility.celsiusToFahrenheit(mLogbook.getWaterTemperature())) + " " + TemperatureType.FAHRENHEIT.getValue();
                            }
                            break;
                    }

                    break;
                case 11:
                    value = mLogbook.getWeather();
                    break;
                case 12:
                    value = mLogbook.getVisibility();
                    break;

            }
            addItemToLayout(diveSiteString[i], value, null, linearLayout);
        }
    }

    /***
     * Generic Input dialog box
     *
     * @@param mContext                      - Context of activity.
     * title                         - Title for confirmation box.
     * message                       - Message to be show in alert box.
     * positiveBtnCaption            - Positive button string.
     * negativeBtnCaption            - Negative button string.
     * isCancelable                  - Sets whether the dialog is cancelable or not. Default is true.
     * dialogBoxInterface            - Interface object that handles its click.
     */
    public void showImage(Context mContext, String imageUrl, boolean isCancelable, final GenericInformativeDialogBoxCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String positiveBtnCaption = "DONE";


        Screen screen = new Screen(getActivity());


        //Input field
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.alert_show_full_image, null, false);
        view.setMinimumHeight((int) (screen.getHeightPixels() * 0.9f));

        final ImageView imageView = ButterKnife.findById(view, R.id.alert_image_view_id);
        mImageLoader.displayImage(imageUrl, imageView);
        builder.setCancelable(false)
                .setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setView(view);

        alert.show();


        // retrieve display dimensions
        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        }
    }


}
