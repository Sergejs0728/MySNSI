package com.scubasnsi.mysnsi.model.sessions.impl;


import com.google.gson.Gson;
import com.scubasnsi.mysnsi.model.dao.CardListDao;
import com.scubasnsi.mysnsi.model.dao.impl.CardDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.PreferencesUtil;

/**
 * Author -
 * Date -  03-04-2017
 */

public class UserSessionImpl implements UserSession {
    private final static String TAG = UserSessionImpl.class.getName();
    private String IS_LOGIN = "isLogin";
    private String USER_DATA = "userData";
    private String USER_PREFERENCE = "user_preference";


    private PreferencesUtil mPreferencesUtil;
    private Gson mGson;
    private CardListDao mCardListDao;


    public UserSessionImpl(PreferencesUtil preferencesUtil, Gson gson) {
        AppLogger.Logger.debug("UserSession", "Cons");
        this.mPreferencesUtil = preferencesUtil;
        this.mGson = gson;

    }

    @Override
    public void login(User user) {
        mPreferencesUtil.savePreferencesBoolean(IS_LOGIN, true);
        mPreferencesUtil.savePreferences(USER_DATA, mGson.toJson(user));
    }

    @Override
    public User getLoggedUserData() {
        User user = null;
        try {
            user = mGson.fromJson(mPreferencesUtil.getPreferences(USER_DATA), User.class);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
        return user;
    }

    @Override
    public long getUserID() {
        User user = getLoggedUserData();
        if (user == null)
            return 0;
        return user.getUserId();
    }

    @Override
    public void logout() {
        mPreferencesUtil.savePreferencesBoolean(IS_LOGIN, false);
        mPreferencesUtil.savePreferences(USER_DATA, null);
        //Clearing card table
        mCardListDao = new CardDaoImpl();
        mCardListDao.clearTable();
    }

    @Override
    public boolean isLogin() {
        return mPreferencesUtil.getPreferencesBoolean(IS_LOGIN);
    }

    @Override
    public UserPreference getUserPreference() {
        UserPreference userPreference = null;
        try {
            userPreference = mGson.fromJson(mPreferencesUtil.getPreferences(USER_PREFERENCE), UserPreference.class);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
        if (userPreference == null)
            userPreference = new UserPreference();
        return userPreference;
    }

    @Override
    public void saveUserPreference(UserPreference userPreference) {
        mPreferencesUtil.savePreferences(USER_PREFERENCE, mGson.toJson(userPreference));
    }

}
