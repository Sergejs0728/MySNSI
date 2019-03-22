package com.scubasnsi.mysnsi.rest.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author - J.K.Joshi
 * Date -  21/03/2017.
 */

/**
 * "STATUS": true,
 "CODE": "200",
 "MESSAGE": "Authenticated",
 "DATA":
 */

public class ResponseDto implements Serializable {

    private static final long serialVersionUID = 1;

    @SerializedName("STATUS")
    private boolean mStatus;

    @SerializedName("CODE")
    private int mResponseCode;

    @SerializedName("DATA")
    private Object mResponseBody;

    @SerializedName("MESSAGE")
    private String mResponseMessage;

    public int getResponseCode() {
        return mResponseCode;
    }

    public Object getResponseBody() {
        return mResponseBody;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public boolean isStatus() {
        return mStatus;
    }
}
