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
public class DiverLoginDto extends ActionDto{

    @Expose
    @SerializedName("name")
    private String mFirstName;
    @Expose
    @SerializedName("family_name")
    private String lastName;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("certification_code")
    private String certificationCode;

    public DiverLoginDto(String firstName, String lastName, String email, String certificationCode ) {

        super("diver_app_login");
        mFirstName = firstName;
        this.lastName = lastName;
        this.email=email;
        this.certificationCode=certificationCode;
    }


    public String getmFirstName() {return mFirstName;}

    public void setmFirstName(String mFirstName) {this.mFirstName = mFirstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getCertificationCode() {return certificationCode;}

    public void setCertificationCode(String certificationCode) {this.certificationCode = certificationCode;}

}
