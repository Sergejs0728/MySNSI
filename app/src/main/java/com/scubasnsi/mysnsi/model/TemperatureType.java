package com.scubasnsi.mysnsi.model;

/**
 * Author -
 * Date -  14-04-2017.
 */

public enum TemperatureType {
    CELSIUS("\u2103"),
    FAHRENHEIT("\u2109");;
    private String mValue;

    TemperatureType(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
