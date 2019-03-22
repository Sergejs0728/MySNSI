package com.scubasnsi.mysnsi.model;

/**
 * Author -
 * Date -  14-04-2017.
 */

public enum WeightType {
    KILOGARM("kg"),
    POUND("lbs");

    private String mValue;

    WeightType(String value) {
        this.mValue = value;
    }
}
