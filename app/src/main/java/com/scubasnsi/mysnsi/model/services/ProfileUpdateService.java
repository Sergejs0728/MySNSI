package com.scubasnsi.mysnsi.model.services;

import com.scubasnsi.mysnsi.model.data_models.Logbook;

import java.io.File;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Created by macrew on 2/27/2018.
 */

public interface ProfileUpdateService {

    void uloadPassportImage(final ResponseCallBackHandler responseCallBackHandler, Logbook userid, File file);
}
