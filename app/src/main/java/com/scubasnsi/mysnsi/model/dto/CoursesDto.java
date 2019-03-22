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
public class CoursesDto extends ActionDto{

    public CoursesDto() {
        super("fetch_courses");


    }
}
