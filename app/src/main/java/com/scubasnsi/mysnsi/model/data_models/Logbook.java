package com.scubasnsi.mysnsi.model.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scubasnsi.mysnsi.model.dto.ActionDto;

import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_TEMP_VALUE;

/**
 * Author -
 * Date -  17-04-2017.
 */
/*
* "action":"add_dive",
"user_id":"4131",
"max_depth" : "500",
"dive_for" : "fun",
"dive_type" : "technical",

"date"	: "2017-01-18",
"dive_time_in" : "10:30",
"dive_time_out" : "11:15",
"dive_time_total" : "45",

"location":"weeee",
"latitude" : "0.23445454545",
"longitude" : "0.23445454545",


"dive_site" : "site name",
"buddy_name" : "Buddy name"
"instructor_flag" : "Instructor",
"instructor_info" : "Instructor name",
"dive_center_name" : "Scuba diving",
"note" : "Note here",

"tank_pressure_in": "45",
"tank_pressure_out" : "30",
"tank_pressure_used" : "15",
"equipment_weight" : "26",
"equipment_suit" : "lorem ipsom",
"equipment_tank" : "test",  //What is this 



"wheather" : "Sunny",
"visibility" : "clear",
"air_temperature" : "25",
"water_temperature" : "15",


"gas_optional" : "yes", //Pending
"decompression_optional" : "no",  //Pending


}


      "equipment_tank": "", //What is this
      "gasType": "air-21-78", //What is this
      "decompression": "",  //Pending
      "date_added": "2017-04-18 02:33:32" //Pending


*
* */
public class Logbook extends ActionDto implements Cloneable {

    @Expose
    @SerializedName("user_id")
    private long mUserId;

    @Expose
    @SerializedName("id")
    //commented by me barinder
    private long mLogBackId;
    //private long mLogBackId=0;
    /*Dive type */
    @Expose
    @SerializedName("max_depth")
    private String mMaxDepth;
    @Expose
    @SerializedName("dive_for")
    private String mDiveFor;
    @Expose
    @SerializedName("dive_type")
    private String mDiveType;
    /*Date */
    @Expose
    @SerializedName("date")
    private String mDiveDate;

    @Expose(serialize = false)
    @SerializedName("date_added")
    private String mDiveCreatedDate;

    @Expose
    @SerializedName("dive_time_in")
    private String mDiveStartTime;
    @Expose
    @SerializedName("dive_time_out")
    private String mDiveEndTime;
    @Expose
    @SerializedName("dive_time_total")
    private String mDiveTotalTime;
    /*Location */
    @Expose
    @SerializedName("location")
    private String mLocationName;
    @Expose
    @SerializedName("latitude")
    private String mLatitude = "0";
    @Expose
    @SerializedName("longitude")
    private String mLongitude = "0";

    /*Dive site info  */
    @Expose
    @SerializedName("dive_site")
    private String mDiveSiteName;
    @Expose
    @SerializedName("buddy_name")
    private String mBuddyName;
    @Expose
    @SerializedName("instructor_flag")
    private String mInstructorFlag;
    @Expose
    @SerializedName("instructor_info")
    private String mInstructorName;
    @Expose
    @SerializedName("dive_center_name")
    private String mDiveCenterName;

    /*Dive  info  */
    @Expose
    @SerializedName("tank_pressure_in")
    private String mTankPressureStart;
    @Expose
    @SerializedName("tank_pressure_out")
    private String mTankPressureEnd;
    @Expose
    @SerializedName("tank_pressure_used")
    private String mTankPressureUsed; //How it used to calculate
    @Expose
    @SerializedName("equipment_weight")
    private String mEquipmentWeight;
    @Expose
    @SerializedName("equipment_suit")
    private String mEquipmentSuit;
    @Expose
    @SerializedName("gasType")
    private String mGasType;
    //// TODO: 17-04-2017 There is no option for GAS TYPE
    // TODO: 17-04-2017   - Nitrox  - Oxygen %
    /*Weather Info   */
    @Expose
    @SerializedName("air_temperature")
    private String mAirTemperature;
    @Expose
    @SerializedName("water_temperature")
    private String mWaterTemperature;
    @Expose
    @SerializedName("wheather")
    private String mWeather;
    @Expose
    @SerializedName("visibility")
    private String mVisibility;

    //// TODO: 17-04-2017 There is no option for pic upload in Add dive .
    @Expose(serialize = false)
    @SerializedName("dive_center_logo")
    private String mDiveCenterLogo;

    private boolean isSync = true;
    private boolean isEdit = false;


    public Logbook() {
        super("add_dive");
    }
public Logbook(String action) {
        super(action);
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public long getLogBackId() {
        return mLogBackId;
    }

    public void setLogBackId(long logBackId) {
        mLogBackId = logBackId;
    }

    public float getMaxDepth() {
        if (mMaxDepth == null || mMaxDepth.isEmpty())
            return 0;
        return Float.parseFloat(mMaxDepth);
    }

    public void setMaxDepth(float maxDepth) {
        mMaxDepth = String.valueOf(maxDepth);
    }

    public String getDiveFor() {
        return mDiveFor;
    }

    public void setDiveFor(String diveFor) {
        if (diveFor != null && !diveFor.isEmpty())
            diveFor = diveFor.toLowerCase();
        mDiveFor = diveFor;
    }

    public String getDiveType() {
        return mDiveType;
    }

    public void setDiveType(String diveType) {
        if (diveType != null && !diveType.isEmpty())
            diveType = diveType.toLowerCase();
        mDiveType = diveType;
    }

    public String getDiveDate() {
        if (mDiveDate == null)
            mDiveDate = "";
        return mDiveDate;
    }

    public void setDiveDate(String diveDate) {
        mDiveDate = diveDate;
    }

    public String getDiveStartTime() {
        if (mDiveStartTime == null)
            mDiveStartTime = "";
        return mDiveStartTime;
    }

    public void setDiveStartTime(String diveStartTime) {
        mDiveStartTime = diveStartTime;
    }

    public String getDiveEndTime() {
        if (mDiveEndTime == null)
            mDiveEndTime = "";
        return mDiveEndTime;
    }

    public void setDiveEndTime(String diveEndTime) {
        mDiveEndTime = diveEndTime;
    }

    public String getDiveTotalTime() {
        return mDiveTotalTime;
    }

    public void setDiveTotalTime(String diveTotalTime) {
        mDiveTotalTime = diveTotalTime;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public double getLatitude() {
        if (mLatitude == null || mLatitude.isEmpty())
            return 0;
        return Double.parseDouble(mLatitude);
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        if (mLongitude == null || mLongitude.isEmpty())
            return 0;
        return Double.parseDouble(mLongitude);
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getDiveSiteName() {
        return mDiveSiteName;
    }

    public void setDiveSiteName(String diveSiteName) {
        mDiveSiteName = diveSiteName;
    }

    public String getBuddyName() {
        return mBuddyName;
    }

    public void setBuddyName(String buddyName) {
        mBuddyName = buddyName;
    }

    public String getInstructorFlag() {
        return mInstructorFlag;
    }

    public void setInstructorFlag(String instructorFlag) {
        mInstructorFlag = instructorFlag;
    }

    public String getInstructorName() {
        return mInstructorName;
    }

    public void setInstructorName(String instructorName) {

        mInstructorName = instructorName;
    }

    public String getDiveCenterName() {
        return mDiveCenterName;
    }

    public void setDiveCenterName(String diveCenterName) {
        mDiveCenterName = diveCenterName;
    }

    public float getTankPressureStart() {
        if (mTankPressureStart == null || mTankPressureStart.isEmpty())
            return DEFAULT_TEMP_VALUE;
        return Float.parseFloat(mTankPressureStart);
    }

    public void setTankPressureStart(String tankPressureStart) {
        mTankPressureStart = tankPressureStart;
    }

    public float getTankPressureEnd() {
        if (mTankPressureEnd == null || mTankPressureEnd.isEmpty())
            return DEFAULT_TEMP_VALUE;
        return Float.parseFloat(mTankPressureEnd);
    }

    public void setTankPressureEnd(String tankPressureEnd) {
        mTankPressureEnd = tankPressureEnd;
    }

    public float getTankPressureUsed() {
        if (mTankPressureUsed == null || mTankPressureUsed.isEmpty())
            return DEFAULT_TEMP_VALUE;
        return Float.parseFloat(mTankPressureUsed);
    }

    public void setTankPressureUsed(float tankPressureUsed) {
        mTankPressureUsed = String.valueOf(tankPressureUsed);
    }

    public int getEquipmentWeight() {
        if (mEquipmentWeight == null || mEquipmentWeight.isEmpty())
            return 0;
        return Integer.parseInt(mEquipmentWeight);
    }

    public void setEquipmentWeight(int equipmentWeight) {
        mEquipmentWeight = String.valueOf(equipmentWeight);
    }

    public String getEquipmentSuit() {
        return mEquipmentSuit;
    }

    public void setEquipmentSuit(String equipmentSuit) {
        mEquipmentSuit = equipmentSuit;
    }

    public String getGasType() {
        if (mGasType == null)
            mGasType = "";
        return mGasType;
    }

    public void setGasType(String gasType) {
        mGasType = gasType;
    }

    public float getAirTemperature() {
        if (mAirTemperature == null || mAirTemperature.isEmpty())
            return DEFAULT_TEMP_VALUE;
        return Float.parseFloat(mAirTemperature);
    }

    public void setAirTemperature(String airTemperature) {
        mAirTemperature = airTemperature;
    }

    public float getWaterTemperature() {
        if (mWaterTemperature == null || mWaterTemperature.isEmpty())
            return DEFAULT_TEMP_VALUE;
        return Float.parseFloat(mWaterTemperature);
    }

    public void setWaterTemperature(String waterTemperature) {
        mWaterTemperature = waterTemperature;
    }

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        if (weather != null && !weather.isEmpty())
            weather = weather.toLowerCase();
        mWeather = weather;
    }

    public String getVisibility() {
        return mVisibility;
    }

    public void setVisibility(String visibility) {
        if (visibility != null && !visibility.isEmpty())
            visibility = visibility.toLowerCase();
        mVisibility = visibility;
    }

    public String getDiveCenterLogo() {
        if (mDiveCenterLogo == null)
            mDiveCenterLogo = "";
        return mDiveCenterLogo;
    }

    public void setDiveCenterLogo(String diveCenterLogo) {
        mDiveCenterLogo = diveCenterLogo;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    @Override
    public String toString() {
        return "Logbook{" +
                "mUserId=" + mUserId +
                ", mLogBackId=" + mLogBackId +
                ", mMaxDepth='" + mMaxDepth + '\'' +
                ", mDiveFor='" + mDiveFor + '\'' +
                ", mDiveType='" + mDiveType + '\'' +
                ", mDiveDate='" + mDiveDate + '\'' +
                ", mDiveStartTime='" + mDiveStartTime + '\'' +
                ", mDiveEndTime='" + mDiveEndTime + '\'' +
                ", mDiveTotalTime='" + mDiveTotalTime + '\'' +
                ", mLocationName='" + mLocationName + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mDiveSiteName='" + mDiveSiteName + '\'' +
                ", mBuddyName='" + mBuddyName + '\'' +
                ", mInstructorFlag='" + mInstructorFlag + '\'' +
                ", mInstructorName='" + mInstructorName + '\'' +
                ", mDiveCenterName='" + mDiveCenterName + '\'' +
                ", mTankPressureStart='" + mTankPressureStart + '\'' +
                ", mTankPressureEnd='" + mTankPressureEnd + '\'' +
                ", mTankPressureUsed='" + mTankPressureUsed + '\'' +
                ", mEquipmentWeight='" + mEquipmentWeight + '\'' +
                ", mEquipmentSuit='" + mEquipmentSuit + '\'' +
                ", mGasType='" + mGasType + '\'' +
                ", mAirTemperature='" + mAirTemperature + '\'' +
                ", mWaterTemperature='" + mWaterTemperature + '\'' +
                ", mWeather='" + mWeather + '\'' +
                ", mVisibility='" + mVisibility + '\'' +
                ", mDiveCenterLogo='" + mDiveCenterLogo + '\'' +
                ", isSync=" + isSync +
                ", isEdit=" + isEdit +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
