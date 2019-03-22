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
public class C_CardsListDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private long mUserId;

    public C_CardsListDto(long userId) {
        super("c_card_list");
        this.mUserId = userId;

    }
}
