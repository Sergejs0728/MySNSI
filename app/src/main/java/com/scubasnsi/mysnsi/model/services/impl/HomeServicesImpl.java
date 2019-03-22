package com.scubasnsi.mysnsi.model.services.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.dto.C_CardsListDto;
import com.scubasnsi.mysnsi.model.dto.C_CardsPDFDto;
import com.scubasnsi.mysnsi.model.dto.LogCountDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDeleteDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDto;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.rest.RestApiInterface;
import com.scubasnsi.mysnsi.rest.RestParser;
import com.scubasnsi.mysnsi.rest.dto.ResponseDto;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.callbacks.onServerResponseCallBack;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.logger.AppLogger;


/**
 * Author - J.K.Joshi
 * Date -  04-01-2017.
 */

public class HomeServicesImpl implements HomeService {

    private final String TAG = HomeServicesImpl.class.getName();
    private Gson mGson;
    private RestApiInterface restApiInterface;

    public HomeServicesImpl() {
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

    // Login with email
    @Override
    public void getC_Cards(final ResponseCallBackHandler responseCallBackHandler, C_CardsListDto c_cardsListDto) {
        try {
            Call<ResponseDto> call = restApiInterface.getCCardList(c_cardsListDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    AppLogger.Logger.info(TAG, responseHandler.toString());
                    if (responseHandler.isExecuted()) {
                        List<C_Cards> cCardsList = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<List<C_Cards>>() {
                        }.getType());
                        responseHandler.setValue(cCardsList);
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

    @Override
    public void getLogCounts(final ResponseCallBackHandler responseCallBackHandler, LogCountDto logCountDto) {
        try {
            Call<ResponseDto> call = restApiInterface.getLogCounts(logCountDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    AppLogger.Logger.info(TAG, responseHandler.toString());
                    if (responseHandler.isExecuted()) {
                        String count = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<String>() {
                        }.getType());
                        responseHandler.setValue(count);
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

    @Override
    public void addDive(final ResponseCallBackHandler responseCallBackHandler, Logbook logbook, File file) {
        try {
            AppLogger.Logger.debug(TAG, "addDive -:  " + mGson.toJson(logbook));
            MultipartBody.Part part = prepareFilePart("dive_center_logo", null, file);
            Call<ResponseDto> call = restApiInterface.addDive(logbook, part);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                        Logbook logbook = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<Logbook>() {
                        }.getType());
                        AppLogger.Logger.debug(TAG, "addDive Response -:  " + mGson.toJson(logbook));
                        responseHandler.setValue(logbook);
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

    @Override
    public void addDiveSync(final ResponseCallBackHandler responseCallBackHandler, Logbook logbook, File file) {
        try {
            AppLogger.Logger.debug(TAG, "addDive -:  " + mGson.toJson(logbook));
            MultipartBody.Part part = prepareFilePart("dive_center_logo", null, file);
            Call<ResponseDto> call = restApiInterface.addDive(logbook, part);
            RestParser.getSyncRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                        Logbook logbook = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<Logbook>() {
                        }.getType());

                        AppLogger.Logger.debug(TAG, "addDive Response -:  " + mGson.toJson(logbook));
                        responseHandler.setValue(logbook);
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

    @Override
    public void getLogbackDives(final ResponseCallBackHandler responseCallBackHandler, LogbookDto logbookDto) {
        try {
            Call<ResponseDto> call = restApiInterface.getLogbooks(logbookDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted()) {
                        List<Logbook> logbooks = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<List<Logbook>>() {
                        }.getType());
                        responseHandler.setValue(logbooks);
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

    @Override
    public void deleteLogs(final ResponseCallBackHandler responseCallBackHandler, LogbookDeleteDto logbookDeleteDto) {
        try {
            Call<ResponseDto> call = restApiInterface.deleteLog(logbookDeleteDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    if (responseHandler.isExecuted()) {

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

    @Override
    public void getPDFUrl(final ResponseCallBackHandler responseCallBackHandler, C_CardsPDFDto c_cardsPDFDto) {
        try {
            Call<ResponseDto> call = restApiInterface.getPDFUrl(c_cardsPDFDto);
            RestParser.getRequestToServer(call, new onServerResponseCallBack() {
                @Override
                public void onServerResponse(ResponseHandler responseHandler) {
                    AppLogger.Logger.info(TAG, responseHandler.toString());
                    if (responseHandler.isExecuted()) {
                        String url = mGson.fromJson(responseHandler.getValue().toString(), new TypeToken<String>() {
                        }.getType());
                        responseHandler.setValue(url);
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

    @Override
    public void downloadFile(final ResponseCallBackHandler responseCallBackHandler, String url) {
        try {
            Call<ResponseBody> call = restApiInterface.downloadFileWithDynamicUrlSync(url);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseHandler responseHandler = new ResponseHandler("server connection failed!");
                    if (response.isSuccessful()) {
                        responseHandler.setExecuted(true);
                        responseHandler.setValue(response.body());
                        responseHandler.setMessage("server connected and has file");
                    }


                    if (responseCallBackHandler != null)
                        responseCallBackHandler.returnResponse(responseHandler);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error");
                }
            });
        } catch (Exception ex) {
            if (responseCallBackHandler != null)
                responseCallBackHandler.returnResponse(new ResponseHandler());
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }


}
