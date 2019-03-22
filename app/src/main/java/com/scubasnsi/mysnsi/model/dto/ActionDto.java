package com.scubasnsi.mysnsi.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author -
 * Date -  14-04-2017.
 */

public class ActionDto implements Serializable {

    @Expose
    @SerializedName("action")
    private String action;

    public ActionDto(String action) {
        this.action = action;
    }
}
