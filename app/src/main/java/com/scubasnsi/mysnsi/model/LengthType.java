package com.scubasnsi.mysnsi.model;

/**
 * Author -
 * Date -  14-04-2017.
 */

public enum LengthType {
    METER("m"),
    FEET("ft");

    private String mValue;

    LengthType(String value) {
        this.mValue = value;
    }
}
