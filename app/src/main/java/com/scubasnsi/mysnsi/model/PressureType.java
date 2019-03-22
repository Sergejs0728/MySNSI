package com.scubasnsi.mysnsi.model;

/**
 * Author -
 * Date -  14-04-2017.
 */

public enum PressureType {
    BAR("bar"),
    PSI("psi");

    private String mValue;

    PressureType(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
