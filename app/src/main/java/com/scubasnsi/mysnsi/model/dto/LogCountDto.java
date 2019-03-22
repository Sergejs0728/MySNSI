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
public class LogCountDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private long mUserId;

    public LogCountDto(long userId) {
        super("count_logs");
        this.mUserId = userId;

    }
}
