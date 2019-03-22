package com.scubasnsi.mysnsi.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler;
import com.scubasnsi.mysnsi.model.DataBase.impl.DatabaseContractor;
import com.scubasnsi.mysnsi.model.dao.LogBookDao;
import com.scubasnsi.mysnsi.model.data_models.Logbook;

import java.util.ArrayList;
import java.util.List;

import static com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler.DESC;


/**
 * Author -
 * Date -  20-04-2017.
 */

public class LogbookDaoImpl implements LogBookDao {

    private DatabaseHandler mDatabaseHandler;
    private final String LIMIT = "5";

    public LogbookDaoImpl() {
        mDatabaseHandler = MyApplication.getApplicationInstance().getDatabaseHandler();
    }


    @Override
    public List<Logbook> getList(long userId) {
        List<Logbook> logbookList = new ArrayList<>();
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + "=?";
        String[] selectionArg = {String.valueOf(userId)};
        String sortOrder = DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DATE + DESC;
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.LogBook.TABLE_NAME, null, selection, selectionArg, sortOrder);
        logbookList.addAll(getList(cursor));
        return logbookList;
    }

    @Override
    public List<Logbook> getList(long userId, boolean sync) {
        List<Logbook> logbookList = new ArrayList<>();
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.COLUMN_NAME_SYNC + " = ?";
        String[] selectionArg = {String.valueOf(userId), String.valueOf(mDatabaseHandler.convertBooleanToInt(sync))};
        String sortOrder = DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DATE + DESC;

        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.LogBook.TABLE_NAME, null, selection, selectionArg, sortOrder);
        logbookList.addAll(getList(cursor));
        return logbookList;
    }

    @Override
    public int unSyncCount(long userId, boolean sync) {
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.COLUMN_NAME_SYNC + " = ?";
        String[] selectionArg = {String.valueOf(userId), String.valueOf(mDatabaseHandler.convertBooleanToInt(sync))};
        String sortOrder = DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DATE + DESC;

        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.LogBook.TABLE_NAME, null, selection, selectionArg, sortOrder, LIMIT);
        return cursor.getCount();
    }


    private List<Logbook> getList(Cursor cursor) {
        List<Logbook> logbookList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Logbook logbook = new Logbook();
                logbook.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_USER_ID)));
                logbook.setLogBackId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_LOGBOOK_ID)));


                //Dive Type
                logbook.setMaxDepth(cursor.getFloat(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_MAX_DEPTH)));
                logbook.setDiveFor(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_FOR)));
                logbook.setDiveType(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TYPE)));

                // Date
                logbook.setDiveDate(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DATE)));
                logbook.setDiveStartTime(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TIME_IN)));
                logbook.setDiveEndTime(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TIME_OUT)));
                logbook.setDiveTotalTime(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DURATION)));

                // Location
                logbook.setLocationName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_NAME)));
                logbook.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_LATITUDE)));
                logbook.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_LONGITUDE)));

                //Dive site
                logbook.setDiveSiteName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_SITE_NAME)));
                logbook.setBuddyName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_BUDDY_NAME)));
                logbook.setInstructorFlag(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_INSTRUCTOR_FLAG)));
                logbook.setInstructorName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_INSTRUCTOR_NAME)));
                logbook.setDiveCenterName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_CENTER_NAME)));

                //Dive info
                logbook.setTankPressureStart(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_IN)));
                logbook.setTankPressureEnd(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_OUT)));
                logbook.setTankPressureUsed(cursor.getFloat(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_USED)));
                logbook.setEquipmentWeight(cursor.getInt(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_WEIGHT)));
                logbook.setEquipmentSuit(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_SUIT)));

                //Weather
                logbook.setGasType(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_GAS_TYPE)));
                logbook.setAirTemperature(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_AIR_TEMP)));
                logbook.setWaterTemperature(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_WATER_TEMP)));
                logbook.setWeather(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_WEATHER)));
                logbook.setVisibility(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_VISIBILITY)));

                //Logo
                logbook.setDiveCenterLogo(cursor.getString(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_LOGO)));
                logbook.setSync(mDatabaseHandler.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(DatabaseContractor.COLUMN_NAME_SYNC))));
                logbook.setEdit(mDatabaseHandler.convertIntToBoolean(cursor.getInt(cursor.getColumnIndex(DatabaseContractor.LogBook.COLUMN_NAME_IS_EDITED))));
                logbookList.add(logbook);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return logbookList;
    }


    @Override
    public void delete(long userId, long logbookId) {
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.LogBook.COLUMN_NAME_LOGBOOK_ID
                + " = ? ";
        String[] selectionArg = {String.valueOf(userId), String.valueOf(logbookId)};
        mDatabaseHandler.delete(DatabaseContractor.LogBook.TABLE_NAME, selection, selectionArg);
    }

    @Override
    public void deleteSynced(long userId, boolean isSynced) {
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.COLUMN_NAME_SYNC
                + " = ? ";
        String[] selectionArg = {String.valueOf(userId), String.valueOf(mDatabaseHandler.convertBooleanToInt(isSynced))};
        mDatabaseHandler.delete(DatabaseContractor.LogBook.TABLE_NAME, selection, selectionArg);
    }

    @Override
    public void clearTable() {
        mDatabaseHandler.delete(DatabaseContractor.LogBook.TABLE_NAME, null, null);
    }


    @Override
    public void save(Logbook logbook) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_USER_ID, logbook.getUserId());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_LOGBOOK_ID, logbook.getLogBackId());
        //Dive Type
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_MAX_DEPTH, logbook.getMaxDepth());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_FOR, logbook.getDiveFor());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TYPE, logbook.getDiveType());
        // Date
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DATE, logbook.getDiveDate());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TIME_IN, logbook.getDiveStartTime());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_TIME_OUT, logbook.getDiveEndTime());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_DURATION, logbook.getDiveTotalTime());
        // Location
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_NAME, logbook.getLocationName());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_LATITUDE, logbook.getLatitude());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_LOCATION_LONGITUDE, logbook.getLongitude());
        //Dive site
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_SITE_NAME, logbook.getDiveSiteName());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_BUDDY_NAME, logbook.getBuddyName());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_INSTRUCTOR_FLAG, logbook.getInstructorFlag());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_INSTRUCTOR_NAME, logbook.getInstructorName());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_DIVE_CENTER_NAME, logbook.getDiveCenterName());
        //Dive info
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_IN, logbook.getTankPressureStart());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_OUT, logbook.getTankPressureEnd());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_TANK_PRESSURE_USED, logbook.getTankPressureUsed());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_WEIGHT, logbook.getEquipmentWeight());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_SUIT, logbook.getEquipmentSuit());
        //Weather
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_GAS_TYPE, logbook.getGasType());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_AIR_TEMP, logbook.getAirTemperature());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_WATER_TEMP, logbook.getWaterTemperature());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_WEATHER, logbook.getWeather());
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_VISIBILITY, logbook.getVisibility());
        //Logo
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_LOGO, logbook.getDiveCenterLogo());
        contentValues.put(DatabaseContractor.COLUMN_NAME_SYNC, mDatabaseHandler.convertBooleanToInt(logbook.isSync()));
        contentValues.put(DatabaseContractor.LogBook.COLUMN_NAME_IS_EDITED, mDatabaseHandler.convertBooleanToInt(logbook.isEdit()));

        mDatabaseHandler.insert(DatabaseContractor.LogBook.TABLE_NAME, contentValues);
    }

    @Override
    public void save(List<Logbook> logbookList) {
        boolean onlyOneTime = true;
        for (Logbook logbook : logbookList) {
            if (onlyOneTime) {
                deleteSynced(logbook.getUserId(), true);  // delete all synced data before add new
                onlyOneTime = false;
            }
            if (!isLogbookExist(logbook))
                save(logbook);
        }
    }

    @Override
    public void update(long userId, long logbookId, boolean sync) {
        ContentValues contentValues = new ContentValues();
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.LogBook.COLUMN_NAME_LOGBOOK_ID
                + " = ? ";
        String[] selectionArg = {String.valueOf(userId), String.valueOf(logbookId)};
        contentValues.put(DatabaseContractor.COLUMN_NAME_SYNC, mDatabaseHandler.convertBooleanToInt(sync));
        mDatabaseHandler.update(DatabaseContractor.LogBook.TABLE_NAME, contentValues, selection, selectionArg);
    }


    private boolean isLogbookExist(Logbook logbook) {
        String selection = DatabaseContractor.LogBook.COLUMN_NAME_USER_ID + " = ? AND " + DatabaseContractor.LogBook.COLUMN_NAME_LOGBOOK_ID + " = ?";
        String[] selectionArg = {String.valueOf(logbook.getUserId()), String.valueOf(logbook.getLogBackId())};
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.LogBook.TABLE_NAME, null, selection, selectionArg, null);
        return cursor.getCount() != 0;
    }

}
