package shiva.joshi.common.rest.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author - J.K.Joshi
 * Date -  21/03/2017.
 */


public class ResponseDto implements Serializable {

    private static final long serialVersionUID = 1;

    @SerializedName("ResponseCode")
    private int mResponseCode;

    @SerializedName("ResponseBody")
    private Object mResponseBody;

    @SerializedName("ResponseMessage")
    private String mResponseMessage;

    public int getResponseCode() {
        return mResponseCode;
    }

    public Object getResponseBody() {
        return mResponseBody;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }


}
