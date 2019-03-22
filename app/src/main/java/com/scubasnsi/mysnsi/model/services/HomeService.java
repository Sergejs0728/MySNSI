package com.scubasnsi.mysnsi.model.services;


import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.dto.C_CardsListDto;
import com.scubasnsi.mysnsi.model.dto.C_CardsPDFDto;
import com.scubasnsi.mysnsi.model.dto.LogCountDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDeleteDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDto;

import java.io.File;

import shiva.joshi.common.callbacks.ResponseCallBackHandler;

/**
 * Author - J.K.Joshi
 * Date -  17-10-2016.
 */

public interface HomeService {

    // LOGIN: userName or email and password.
    void getC_Cards(final ResponseCallBackHandler responseCallBackHandler, C_CardsListDto c_cardsListDto);

    void getLogCounts(final ResponseCallBackHandler responseCallBackHandler, LogCountDto logCountDto);

    void addDive(final ResponseCallBackHandler responseCallBackHandler, Logbook logbook, File file);

    void addDiveSync(final ResponseCallBackHandler responseCallBackHandler, Logbook logbook, File file);

    void getLogbackDives(final ResponseCallBackHandler responseCallBackHandler, LogbookDto logbookDto);

    void deleteLogs(final ResponseCallBackHandler responseCallBackHandler, LogbookDeleteDto logbookDeleteDto);

    void getPDFUrl(final ResponseCallBackHandler responseCallBackHandler, C_CardsPDFDto c_cardsPDFDto);

    void downloadFile(final ResponseCallBackHandler responseCallBackHandler, String url);


}
