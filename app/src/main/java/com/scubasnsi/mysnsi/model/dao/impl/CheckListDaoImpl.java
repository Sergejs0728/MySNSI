package com.scubasnsi.mysnsi.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler;
import com.scubasnsi.mysnsi.model.DataBase.impl.DatabaseContractor;
import com.scubasnsi.mysnsi.model.dao.CheckListDao;
import com.scubasnsi.mysnsi.model.data_models.CheckList;

import java.util.ArrayList;
import java.util.List;


/**
 * Author -
 * Date -  20-04-2017.
 */

public class CheckListDaoImpl implements CheckListDao {

    private DatabaseHandler mDatabaseHandler;

    public CheckListDaoImpl() {
        mDatabaseHandler = MyApplication.getApplicationInstance().getDatabaseHandler();
    }

    @Override
    public void saveOrUpdate(CheckList checkList, long userId) {
        if (isExist(checkList,userId)) {
            update(checkList,userId);
        } else {
            save(checkList,userId);
        }
    }

    @Override
    public List<CheckList> getList(long userId) {
        List<CheckList> checkLists = null;
        String selection = DatabaseContractor.CheckListEntry.COLUMN_NAME_USER_ID+"=? ";

        String[] selectionArg = {String.valueOf(userId)};
        ;
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.CheckListEntry.TABLE_NAME, null, selection, selectionArg, null, null);

        if (cursor.moveToFirst()) {
            checkLists = new ArrayList<>();
            do {
                CheckList checkList = new CheckList();
                checkList.setListId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.COLUMN_NAME_ID)));
                checkList.setName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.COLUMN_NAME_NAME)));
                checkList.setChecked((cursor.getInt(cursor.getColumnIndex(DatabaseContractor.CheckListEntry.IS_CHECKED)) == 0) ? false : true);
                checkLists.add(checkList);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return checkLists;
    }

    @Override
    public void delete(long id,long userId) {
        String selection = DatabaseContractor.COLUMN_NAME_ID + "=? AND "+DatabaseContractor.CheckListEntry.COLUMN_NAME_USER_ID+"=? ";
        String[] selectionArg = {String.valueOf(id),String.valueOf(userId)};
        mDatabaseHandler.delete(DatabaseContractor.CheckListEntry.TABLE_NAME, selection, selectionArg);
    }

    private boolean isExist(CheckList checkList,long userId) {
        String selection = DatabaseContractor.COLUMN_NAME_ID + "=? AND "+DatabaseContractor.CheckListEntry.COLUMN_NAME_USER_ID+"=? ";

        String[] selectionArg = {String.valueOf(checkList.getListId()),String.valueOf(userId)};
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.CheckListEntry.TABLE_NAME, null, selection, selectionArg, null);
        if (cursor.getCount() <= 0)
            return false;
        return true;
    }

    private void save(CheckList checkList,long userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContractor.CheckListEntry.COLUMN_NAME_USER_ID, userId);
        contentValues.put(DatabaseContractor.COLUMN_NAME_NAME, checkList.getName());
        contentValues.put(DatabaseContractor.CheckListEntry.IS_CHECKED, checkList.getChecked());
        mDatabaseHandler.insert(DatabaseContractor.CheckListEntry.TABLE_NAME, contentValues);
    }

    private void update(CheckList checkList,long userId) {
        ContentValues contentValues = new ContentValues();
        String selection = DatabaseContractor.COLUMN_NAME_ID + "=? AND "+DatabaseContractor.CheckListEntry.COLUMN_NAME_USER_ID+"=? ";

        String[] selectionArg = {String.valueOf(checkList.getListId()),String.valueOf(userId)};
;
        contentValues.put(DatabaseContractor.COLUMN_NAME_NAME, checkList.getName());
        contentValues.put(DatabaseContractor.CheckListEntry.IS_CHECKED, checkList.getChecked());
        mDatabaseHandler.update(DatabaseContractor.CheckListEntry.TABLE_NAME, contentValues, selection, selectionArg);
    }
}
