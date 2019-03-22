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
public class SignupDto extends ActionDto{

    @Expose
    @SerializedName("first_name")
    private String mFirstName;
    @Expose
    @SerializedName("last_name")
    private String lastName;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("birth_date")
    private String birthDate;
    @Expose
    @SerializedName("course_id")
    private String courseId;
    @Expose
    @SerializedName("location")
    private String location;
    @Expose
    @SerializedName("instructor_mame")
    private String instructorName;

    public SignupDto(String firstName, String lastName,String email, String birthDate,
                     String courseId, String location,String instructorName,String userType) {

        super(userType);
        mFirstName = firstName;
        this.lastName = lastName;
        this.email=email;
        this.birthDate=birthDate;
        this.courseId=courseId;
        this.location=location;
        this.instructorName=instructorName;

    }
}
