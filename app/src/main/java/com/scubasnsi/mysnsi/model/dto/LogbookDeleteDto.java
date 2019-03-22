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
public class LogbookDeleteDto extends ActionDto{

    @Expose
    @SerializedName("user_id")
    private long mUserId;
    @Expose
    @SerializedName("log_id")
    private long mLogId;
    public LogbookDeleteDto(long userId,long logbookId) {
        super("delete_log");
        this.mUserId = userId;
        this.mLogId=logbookId;

    }
}
