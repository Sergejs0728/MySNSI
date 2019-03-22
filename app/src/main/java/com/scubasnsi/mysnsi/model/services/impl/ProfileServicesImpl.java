package com.scubasnsi.mysnsi.model.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.ProfileDto;
import com.scubasnsi.mysnsi.model.services.LoginService;
import com.scubasnsi.mysnsi.model.services.ProfileService;
import com.scubasnsi.mysnsi.rest.RestApiInterface;
import com.scubasnsi.mysnsi.rest.RestParser;
import com.scubasnsi.mysnsi.rest.dto.ResponseDto;

import retrofit2.Call;
import retrofit2.Retrofit;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.callbacks.onServerResponseCallBack;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.logger.AppLogger;


/**
 * Author - J.K.Joshi
 * Date -  04-01-2017.
 */

public class ProfileServicesImpl implements ProfileService {

    private final String TAG = ProfileServicesImpl.class.getName();
    private Gson mGson;
    private RestApiInterface restApiInterface;

    public ProfileServicesImpl() {
        mGson = MyApplication.getApplicationInstance().getGsonBuilder();
        try {
            if (mGson == null) {
                throw new RuntimeException("Gson object is null");
            }
            Retrofit retrofit = MyApplication.getApplicationInstance().getRetrofitClient(mGson);
            if (retrofit == null)
                throw new RuntimeException("Retrofit object is null");

            restApiInterface = retrofit.create(RestApiInterface.class);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }






    @Override
    public void ProfileService(final ResponseCallBackHandler responseCallBackHandler, ProfileDto loginDto) {

        try {
            Call<ResponseDto> call = restApiInterface.getProfile(loginDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    AppLogger.Logger.info(TAG, responseHandler.toString());
                    if (responseHandler.isExecuted()) {
                        User user = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<User>() {
                        }.getType());
                        responseHandler.setValue(user);
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
}
