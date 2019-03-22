package com.scubasnsi.mysnsi.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author -
 * Date -  14-04-2017.
 */
/*
{
"action":"c_card_list",
"user_id":"10047"
}
 */
public class C_CardsPDFDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private long mUserId;
    @Expose
    @SerializedName("Id_certificazioni")
    private String  mCetificationID="";

    @Expose
    @SerializedName("Id_professional_qualifications")
    private String  mProfessionQualificationID="";


    public C_CardsPDFDto(long userId,long professionId,long certificationId) {
        super("c_card_image");
        this.mUserId = userId;
        this.mProfessionQualificationID=(professionId==0)?"":String.valueOf(professionId);
        this.mCetificationID=(certificationId==0)?"":String.valueOf(certificationId);

    }


}
