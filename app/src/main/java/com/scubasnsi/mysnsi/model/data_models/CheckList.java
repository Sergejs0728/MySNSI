package com.scubasnsi.mysnsi.model.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author -
 * Date -  14-04-2017.
 */

public class CheckList implements Serializable {

    @Expose
    @SerializedName("Id")
    private long mListId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("checked")
    private int mChecked;

    public long getListId() {
        return mListId;
    }

    public void setListId(long listId) {
        mListId = listId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isChecked() {
        return (mChecked == 0) ? false : true;
    }

    public int getChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = (checked) ? 1 : 0;
    }
}
