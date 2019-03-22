package com.scubasnsi.mysnsi.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.scubasnsi.mysnsi.rest.dto.ResponseDto;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shiva.joshi.common.LibApp;
import shiva.joshi.common.callbacks.onServerResponseCallBack;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.receivers.ConnectivityReceiver;


/**
 * Author - J.K.Joshi
 * Date -  21-02-2017.
 */

public class RestParser {

    private static final String TAG = RestParser.class.getName();
    private static Gson mGson = LibApp.getInstance().getGsonBuilder();
    private static Context mContext = LibApp.getInstance().getContext();
    //private final static String ERROR_INTERNET = "Please check your internet connection.";
    private final static String ERROR_INTERNET = "You are not connected with Internet, please check your internet connection and try again";

    /**
     * Handle response response dto.
     *
     * @param response the response
     * @return the response dto
     */
    static ResponseHandler handleResponse(Response<ResponseDto> response) {
        ResponseHandler responseHandler = new ResponseHandler();
        try {
            if (response == null) {//SERVER RESPONSE : null
                throw new RuntimeException("Invalid Response");
            } else if (!response.isSuccessful() || response.body() == null) {  //SERVER RESPONSE : some error at server or required response body is null
                throw new RuntimeException(response.errorBody().toString());
            }
            //SERVER RESPONSE : get server response ,but there is some error with service (May be authentication , parameter etc.)
            ResponseDto responseDto = response.body();
            if (responseDto.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(responseDto.getResponseMessage());
            }

            if (responseDto.getResponseBody() ==null) {
                throw new RuntimeException(responseDto.getResponseMessage());
            }

            //SERVER RESPONSE : We did it! Successful response.
            AppLogger.Logger.debug(TAG, responseDto.getResponseBody().toString());
            responseHandler.setExecuted(true);
            responseHandler.setMessage(responseDto.getResponseMessage());
            responseHandler.setValue(mGson.toJson(responseDto.getResponseBody()));
        } catch (Exception ex) {
            responseHandler.setMessage(ex.getMessage());
            AppLogger.Logger.debug(TAG, ex.getMessage());
            AppLogger.showToastForDebug(TAG, ex.getMessage());
            return responseHandler;
        }
        return responseHandler;
    }

    // Common rest call

    public static void getRequestToServer(final Call<ResponseDto> call, final onServerResponseCallBack onServerResponseCallBack) {
        if (ConnectivityReceiver.isConnected(mContext)) {
            call.enqueue(new Callback<ResponseDto>() {
                @Override
                public void onResponse(Call<ResponseDto> call, Response<ResponseDto> response) {

                        ResponseHandler responseHandler = handleResponse(response);
                        onServerResponseCallBack.onServerResponse(responseHandler);


                }

                @Override
                public void onFailure(Call<ResponseDto> call, Throwable ex) {
                    AppLogger.Logger.error(TAG, ex.getMessage(), ex);
                    onServerResponseCallBack.onServerResponse(new ResponseHandler());
                }
            });
        } else {
            onServerResponseCallBack.onServerResponse(new ResponseHandler(ERROR_INTERNET));
        }
    }

    public static void getSyncRequestToServer(final Call<ResponseDto> call, final onServerResponseCallBack onServerResponseCallBack) {
        if (ConnectivityReceiver.isConnected(mContext)) {
            Response<ResponseDto> response = null;
            try {
                response = call.execute();
            } catch (IOException ex) {
                AppLogger.Logger.error(TAG, ex.getMessage(), ex);
                onServerResponseCallBack.onServerResponse(new ResponseHandler());
            }
            ResponseHandler responseHandler = handleResponse(response);
            onServerResponseCallBack.onServerResponse(responseHandler);
        } else {
            onServerResponseCallBack.onServerResponse(new ResponseHandler(ERROR_INTERNET));
        }
    }
}
