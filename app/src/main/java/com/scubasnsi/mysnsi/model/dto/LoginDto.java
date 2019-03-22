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
public class LoginDto extends ActionDto{

    @Expose
    @SerializedName("username")
    private String mUserName;
    @Expose
    @SerializedName("password")
    private String password;

    public LoginDto(String userName, String password) {
        super("check_user_login");
        mUserName = userName;
        this.password = password;
    }
}
