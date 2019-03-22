package com.scubasnsi.mysnsi.model.dao;

import com.scubasnsi.mysnsi.model.data_models.C_Cards;

import java.util.List;

/**
 * Author -
 * Date -  20-04-2017.
 */

public interface CardListDao {


    void saveList(List<C_Cards> checkList);

    List<C_Cards> getList();

    void delete(long id);

    void clearTable();


    String getPDFFileName(long cardId);

     void saveOrUpdatePDF(long cardId, String fileName);

    void clearPDFTable();


}
