package com.scubasnsi.mysnsi.model.services;


import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.ProfileDto;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Author - J.K.Joshi
 * Date -  17-10-2016.
 */

public interface ProfileService {


    void ProfileService(final ResponseCallBackHandler responseCallBackHandler, ProfileDto loginDto);

}
