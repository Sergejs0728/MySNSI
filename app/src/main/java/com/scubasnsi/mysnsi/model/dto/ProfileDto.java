package com.scubasnsi.mysnsi.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author -
 * Date -  14-04-2017.
 */
/*
{
"action":"check_user_login",
"username":"",
"password":"TayronaDive"
}
 */
public class ProfileDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private String mUserId;


    public ProfileDto(String userId) {
        super("user_info");
        mUserId = userId;

    }
}
