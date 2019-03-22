package com.scubasnsi.mysnsi.model.DataBase;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by joshi on 8/29/2016.
 */

public interface DatabaseHandler {

    String ASC = " ASC";
    String DESC = " DESC";

    // fetch cursor from database query
    Cursor get(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    //Limit
    Cursor get(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder, String limit);

    //insert
    long insert(String tableName, ContentValues values);

    // Update
    long update(String tableName, ContentValues values, String selection, String[] selectionArg);

    //Delete
    int delete(String tableName, String selection, String[] selectionArgs);

    // Row Count
    int rowCount(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    // boolean to int and int to boolean
    int convertBooleanToInt(boolean sync);

    boolean convertIntToBoolean(int sync);
}