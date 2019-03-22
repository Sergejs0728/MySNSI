package com.scubasnsi.mysnsi.model.services.impl;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.services.ProfileUpdateService;
import com.scubasnsi.mysnsi.rest.RestApiInterface;
import com.scubasnsi.mysnsi.rest.RestParser;
import com.scubasnsi.mysnsi.rest.dto.ResponseDto;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.callbacks.onServerResponseCallBack;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.logger.AppLogger;

/**
 * Created by macrew on 2/27/2018.
 */

public class ProfileUpdateServiceImp implements ProfileUpdateService {
    private final String TAG = LoginServicesImpl.class.getName();
    private Gson mGson;
    private Retrofit mRetrofit;
    private RestApiInterface restApiInterface;
    public ProfileUpdateServiceImp()
    {
        mGson = MyApplication.getApplicationInstance().getGsonBuilder();
        try {
            if (mGson == null) {
                throw new RuntimeException("Gson object is null");
            }
            mRetrofit = MyApplication.getApplicationInstance().getRetrofitClient(mGson);
            if (mRetrofit == null)
                throw new RuntimeException("Retrofit object is null");

            restApiInterface = mRetrofit.create(RestApiInterface.class);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void uloadPassportImage(final ResponseCallBackHandler responseCallBackHandler, Logbook userid, File file) {
        try {
            AppLogger.Logger.debug(TAG, "uploadPassport -:  " + mGson.toJson(userid));
            MultipartBody.Part part = prepareFilePart("user_passport", null, file);
            Call<ResponseDto> call = restApiInterface.uploadPassport(userid, part);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                        User logbook = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<User>() {
                        }.getType());

                        AppLogger.Logger.debug(TAG, "uploadPassport Response -:  " + mGson.toJson(logbook));
                        responseHandler.setValue(logbook);

                        AppLogger.Logger.debug(TAG, "uploadPassport Response -:  " + mGson.toJson(responseHandler.getValue().toString()));

                    }
                    if (responseCallBackHandler != null)
                        responseCallBackHandler.returnResponse(responseHandler);
                }
            });
        } catch (Exception ex) {
            if (responseCallBackHandler != null)
                responseCallBackHandler.returnResponse(new ResponseHandler());
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path, File file) {
        // use the FileUtils to get the actual file by uri
        if (path != null) {
            file = new File(path);
        }
        if (file == null || !file.exists())
            return null;
        // create RequestBody instance from file
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

}
