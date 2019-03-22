package com.scubasnsi.mysnsi.model.services;


import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.SignupDto;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Author - J.K.Joshi
 * Date -  17-10-2016.
 */

public interface SignupService {

    // LOGIN: userName or email and password.
    void signupUser(final ResponseCallBackHandler responseCallBackHandler, SignupDto signupDto);



}
