package com.scubasnsi.mysnsi.model.sessions;


import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.data_models.UserPreference;

/**
 * Author - J.K.Joshi
 * Date -  15-02-2017.
 */

public interface UserSession {

    void login(User user);

    User getLoggedUserData();

    long getUserID();

    void logout();

    boolean isLogin();

    UserPreference getUserPreference();

    void saveUserPreference(UserPreference userPreference);




}
