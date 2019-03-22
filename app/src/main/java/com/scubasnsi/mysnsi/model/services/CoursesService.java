package com.scubasnsi.mysnsi.model.services;


import com.scubasnsi.mysnsi.model.dto.CoursesDto;
import com.scubasnsi.mysnsi.model.dto.SignupDto;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Author - J.K.Joshi
 * Date -  17-10-2016.
 */

public interface CoursesService {

    // LOGIN: userName or email and password.
    void getCources(final ResponseCallBackHandler responseCallBackHandler, CoursesDto coursesDto);



}
