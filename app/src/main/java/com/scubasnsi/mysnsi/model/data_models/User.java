package com.scubasnsi.mysnsi.model.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author -
 * Date -  14-04-2017.
 */

/*
*
*   "ID": "10171",
    "user_login": "2172",
    "user_pass": "$P$BRWBGWAUcS32fA5lmzLkJ0cXX.Xo.O0",
    "user_nicename": "2172",
    "user_email": "cbtayrona@hotmail.com",
    "user_url": "http://www.tayronadivecenter.com",
    "user_registered": "2016-11-29 11:20:11",
    "display_name": "Tayrona Dive Center",
    "Id_affiliati": "2172",
    "profile_pic": "http://dev.mysnsi.scubasnsi.com/wp-content/uploads/dive_centers/2172_2172.jpg"
* */
public class User implements Serializable {

    @Expose
    @SerializedName("ID")
    private long mUserId;
    @Expose
    @SerializedName("user_login")
    private String mUserLogin;
    @Expose
    @SerializedName("user_pass")
    private String mUserPassword;
    @Expose
    @SerializedName("user_nicename")
    private String mNickName;
    @Expose
    @SerializedName("user_email")
    private String mEmail;
    @Expose
    @SerializedName("user_url")
    private String mUserUrl;
    @Expose
    @SerializedName("user_registered")
    private String mRegistrationDate;
    @Expose
    @SerializedName("display_name")
    private String mUserDisplayName;
    @Expose
    @SerializedName("Id_affiliati")
    private String mAffiliatiId;
    @Expose
    @SerializedName("profile_pic")
    private String mProfileImage;
    @SerializedName("is_student")
    private String mIsStudent;
    @SerializedName("passport_pic")
    private String mPassportImg;

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(String userLogin) {
        mUserLogin = userLogin;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUserUrl() {
        return mUserUrl;
    }

    public void setUserUrl(String userUrl) {
        mUserUrl = userUrl;
    }

    public String getRegistrationDate() {
        return mRegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        mRegistrationDate = registrationDate;
    }

    public String getUserDisplayName() {
        return mUserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        mUserDisplayName = userDisplayName;
    }

    public String getAffiliatiId() {
        return mAffiliatiId;
    }

    public void setAffiliatiId(String affiliatiId) {
        mAffiliatiId = affiliatiId;
    }

    public String getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(String profileImage) {
        mProfileImage = profileImage;
    }

    public String getmIsStudent() {return mIsStudent;}

    public void setmIsStudent(String mIsStudent) {this.mIsStudent = mIsStudent;}

    public String getmPassportImg() {return mPassportImg;}

    public void setmPassportImg(String mPassportImg) {this.mPassportImg = mPassportImg;}


    @Override
    public String toString() {
        return "User{" +
                "mUserId=" + mUserId +
                ", mUserLogin='" + mUserLogin + '\'' +
                ", mUserPassword='" + mUserPassword + '\'' +
                ", mNickName='" + mNickName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mUserUrl='" + mUserUrl + '\'' +
                ", mRegistrationDate='" + mRegistrationDate + '\'' +
                ", mUserDisplayName='" + mUserDisplayName + '\'' +
                ", mAffiliatiId='" + mAffiliatiId + '\'' +
                ", mIsStudent='" + mIsStudent + '\'' +
                ", mProfileImage='" + mProfileImage + '\'' +
                ", mPassportImg='" + mPassportImg + '\'' +
                '}';
    }
}
