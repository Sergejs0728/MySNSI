package com.scubasnsi.mysnsi.app;

import android.content.Context;

import com.google.gson.Gson;
import com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler;
import com.scubasnsi.mysnsi.model.DataBase.impl.DatabaseContractor;
import com.scubasnsi.mysnsi.model.DataBase.impl.DatabaseHandlerImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;
import com.scubasnsi.mysnsi.model.sessions.impl.UserSessionImpl;
import com.scubasnsi.mysnsi.rest.RestApiInterface;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shiva.joshi.common.LibApp;
import shiva.joshi.common.receivers.ConnectivityReceiver;
import shiva.joshi.common.rest.LoggingInterceptor;
import shiva.joshi.common.utilities.PreferencesUtil;


public class MyApplication extends LibApp {
    public static MyApplication sApplicationInstance;
    private PreferencesUtil mPreferencesUtil;
    private Retrofit mRetrofitClient;
    private UserSession mUserSession;
    private DatabaseHandler mDatabaseHandler;
    HttpLoggingInterceptor mHttpLoggingInterceptor;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationInstance = this;

    }

    public static synchronized MyApplication getApplicationInstance() {
        return sApplicationInstance;
    }

    // get shared preference
    public PreferencesUtil getPreferencesUtil() {
        if (mPreferencesUtil == null)
            mPreferencesUtil = new PreferencesUtil(getApplicationInstance().getSharedPreferences(AppGlobalConstant.PREFERENCE_NAME, AppGlobalConstant.PREFERENCE_MODE));
        return mPreferencesUtil;
    }


    // get User sessions
    public UserSession getUserSession() {
        if (mUserSession == null) {
            mUserSession = new UserSessionImpl(getPreferencesUtil(), getGsonBuilder());
        }
        return mUserSession;
    }


    // get Retrofit object
    public Retrofit getRetrofitClient(Gson gson) {
        if (mRetrofitClient == null) {
            mHttpLoggingInterceptor = new HttpLoggingInterceptor();
            mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    //.addInterceptor(new LoggingInterceptor())
                    .addInterceptor(mHttpLoggingInterceptor)
                    .build();
            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(RestApiInterface.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return mRetrofitClient;
    }

    public DatabaseHandler getDatabaseHandler(){
        if(mDatabaseHandler ==null){
            mDatabaseHandler = new DatabaseHandlerImpl(getContext(), DatabaseContractor.DATABASE_NAME);
        }
        return mDatabaseHandler;
    }


    // setting receiver
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;

    }


    //Clear cache
    public void clearCache(Context context, File file ){
      getApplicationInstance().getImageLoaderInstance(context,file, null).clearDiskCache();

       /* getImageLoaderInstance(context,null).clearMemoryCache();
        getImageLoaderInstance(context,null).clearDiskCache();*/
    }
}