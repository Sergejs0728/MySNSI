package com.scubasnsi.mysnsi.model.services;


import com.scubasnsi.mysnsi.model.dto.LoginDto;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Author - J.K.Joshi
 * Date -  17-10-2016.
 */

public interface LoginService {

    // LOGIN: userName or email and password.
    void loginWithEmail(final ResponseCallBackHandler responseCallBackHandler, LoginDto loginDto);

    void getAccountInfo(final ResponseCallBackHandler responseCallBackHandler, long userId);

}
