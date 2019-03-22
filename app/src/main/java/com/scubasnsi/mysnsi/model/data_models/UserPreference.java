package com.scubasnsi.mysnsi.model.data_models;

import com.scubasnsi.mysnsi.model.LengthType;
import com.scubasnsi.mysnsi.model.PressureType;
import com.scubasnsi.mysnsi.model.TemperatureType;
import com.scubasnsi.mysnsi.model.WeightType;

/**
 * Author -
 * Date -  14-04-2017.
 */


public class UserPreference {

    private LengthType mLengthType;
    private TemperatureType mTemperatureType;
    private WeightType mWeightType;
    private PressureType mPressureType;

    public UserPreference() {
        this.mLengthType=LengthType.METER;
        this.mTemperatureType=TemperatureType.CELSIUS;
        this.mWeightType=WeightType.KILOGARM;
        this.mPressureType=PressureType.BAR;
    }

    public LengthType getLengthType() {
        return mLengthType;
    }

    public void setLengthType(LengthType lengthType) {
        mLengthType = lengthType;
    }

    public TemperatureType getTemperatureType() {
        return mTemperatureType;
    }

    public void setTemperatureType(TemperatureType temperatureType) {
        mTemperatureType = temperatureType;
    }

    public WeightType getWeightType() {
        return mWeightType;
    }

    public void setWeightType(WeightType weightType) {
        mWeightType = weightType;
    }

    public PressureType getPressureType() {
        return mPressureType;
    }

    public void setPressureType(PressureType pressureType) {
        mPressureType = pressureType;
    }
}
