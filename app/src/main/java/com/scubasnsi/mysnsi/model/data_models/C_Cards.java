package com.scubasnsi.mysnsi.model.data_models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * {
 * "Id": "14",
 "Id_professional_qualifications": "380",
 "Anagrafica_corsi_Id": "14",
 "professional_Numero_member": "151",
 "professional_nr_trainer": "001",
 "dataqualformat": "19/06/2010",
 "expformat": "31/12/2017",
 "Numero_member": "001",
 "Nome": "Fulvia",
 "Cognome": "Lami",

 "Nome_corso": "Underwater Photography Instructor",
 "Nick_corso": "UPI"  -course
 "Nick_corso_url": "UPT.png"
 },
 */
public class C_Cards implements Serializable {

    @Expose
    @SerializedName(value = "Id",alternate = {"Id_rich_brev"})
    private long mCardId;

    @Expose
    @SerializedName("Id_professional_qualifications")
    private long mProfessionalQualificationId;
    @Expose
    @SerializedName("Id_certificazioni")
    private long mCertificationId;

    @Expose
    @SerializedName("Anagrafica_corsi_Id")
    private long mMasterCourseId;

    @Expose
    @SerializedName("professional_Numero_member")
    private long mProfessionalMemberNumber;

    @Expose
    @SerializedName("Nome_corso")
    private String mCourseName;

    @Expose
    @SerializedName("Nick_corso")
    private String mCourseNickName;

    @Expose
    @SerializedName("Nick_corso_url")
    private String mCourseUrl;


    public long getCardId() {
        return mCardId;
    }

    public void setCardId(long cardId) {
        mCardId = cardId;
    }

    public long getProfessionalQualificationId() {
        return mProfessionalQualificationId;
    }

    public void setProfessionalQualificationId(long professionalQualificationId) {
        mProfessionalQualificationId = professionalQualificationId;
    }

    public long getMasterCourseId() {
        return mMasterCourseId;
    }

    public void setMasterCourseId(long masterCourseId) {
        mMasterCourseId = masterCourseId;
    }

    public long getProfessionalMemberNumber() {
        return mProfessionalMemberNumber;
    }

    public void setProfessionalMemberNumber(long professionalMemberNumber) {
        mProfessionalMemberNumber = professionalMemberNumber;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public String getCourseNickName() {
        return mCourseNickName;
    }

    public void setCourseNickName(String courseNickName) {
        mCourseNickName = courseNickName;
    }

    public String getCourseUrl() {
        return mCourseUrl;
    }

    public void setCourseUrl(String courseUrl) {
        mCourseUrl = courseUrl;
    }

    public long getCertificationId() {
        return mCertificationId;
    }

    public void setCertificationId(long certificationId) {
        mCertificationId = certificationId;
    }

    @Override
    public String toString() {
        return "C_Cards{" +
                "mCardId=" + mCardId +
                ", mProfessionalQualificationId=" + mProfessionalQualificationId +
                ", mCertificationId=" + mCertificationId +
                ", mMasterCourseId=" + mMasterCourseId +
                ", mProfessionalMemberNumber=" + mProfessionalMemberNumber +
                ", mCourseName='" + mCourseName + '\'' +
                ", mCourseNickName='" + mCourseNickName + '\'' +
                ", mCourseUrl='" + mCourseUrl + '\'' +
                '}';
    }
}
