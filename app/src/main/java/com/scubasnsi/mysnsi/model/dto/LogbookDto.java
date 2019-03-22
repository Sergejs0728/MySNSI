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
public class LogbookDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private long mUserId;

    public LogbookDto(long userId) {
        super("list_logs");
        this.mUserId = userId;

    }
}
