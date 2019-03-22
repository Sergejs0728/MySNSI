package com.scubasnsi.mysnsi.model.dao;

import com.scubasnsi.mysnsi.model.data_models.Logbook;

import java.util.List;

/**
 * Author -
 * Date -  20-04-2017.
 */

public interface LogBookDao {

    List<Logbook> getList(long userId);

    List<Logbook> getList(long userId, boolean sync);

    int  unSyncCount(long userId, boolean sync);

    void delete(long userId,long logbookId);

    void deleteSynced(long userId,boolean isSynced);

    void clearTable();

    void save(Logbook logbook);

    void save(List<Logbook> logbookList);

    void update(long userId,long logbookId,boolean sync);


}
