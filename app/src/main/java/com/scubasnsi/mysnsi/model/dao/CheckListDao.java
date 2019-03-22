package com.scubasnsi.mysnsi.model.dao;

import com.scubasnsi.mysnsi.model.data_models.CheckList;

import java.util.List;

/**
 * Author -
 * Date -  20-04-2017.
 */

public interface CheckListDao {

    void saveOrUpdate(CheckList checkList, long userId);

    List<CheckList> getList(long userId);

    void delete(long id,long userId);


}
