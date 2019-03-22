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
*   * */
public class Courses implements Serializable {

    @Expose
    @SerializedName("Id")
    private long mCourseId;
    @Expose
    @SerializedName("Nome_corso")
    private String mCourseName;

    public Courses(long mCourseId, String mCourseName) {
        this.mCourseId = mCourseId;
        this.mCourseName = mCourseName;
    }

    public long getmCourseId() {return mCourseId;}

    public void setmCourseId(long mCourseId) {this.mCourseId = mCourseId;}

    public String getmCourseName() {return mCourseName;}

    public void setmCourseName(String mCourseName) {this.mCourseName = mCourseName;}


    @Override
    public String toString() {
        return "Courses{" +
                "mCourseId=" + mCourseId +
                ", mCourseName='" + mCourseName + '\'' +
                '}';
    }
}
