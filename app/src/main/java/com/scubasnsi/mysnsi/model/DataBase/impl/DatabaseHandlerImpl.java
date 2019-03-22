package com.scubasnsi.mysnsi.model.DataBase.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler;

import shiva.joshi.common.logger.AppLogger;


public class DatabaseHandlerImpl extends SQLiteOpenHelper implements DatabaseHandler {

    private final String TAG = DatabaseHandlerImpl.class.getName();

    public DatabaseHandlerImpl(Context context, String dbName) {
        super(context, dbName, null, DatabaseContractor.DATABASE_VERSION);
        AppLogger.Logger.info(TAG, "Database constructor." + dbName);
    }


    // Creating Tables in database
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DatabaseContractor.CheckListEntry.SQL_CREATE_TABLE);
            db.execSQL(DatabaseContractor.CardsEntry.SQL_CREATE_TABLE);
            db.execSQL(DatabaseContractor.CardsPDFEntry.SQL_CREATE_TABLE);
            db.execSQL(DatabaseContractor.LogBook.SQL_CREATE_TABLE);

            AppLogger.Logger.info(TAG, "Table  created.  " + DatabaseContractor.CardsEntry.SQL_CREATE_TABLE);
        } catch (SQLiteException ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    /*  This method is called when database is upgraded like modifying the table structure,
    adding constraints to database etc.,*/


    // Close db after completion of task
    public boolean closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
        return true;
    }

    /*A query returns a Cursor object. A Cursor represents the result of a query and
    basically points to one row of the query result.  */
    @Override
    public Cursor get(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                tableName,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

    }

    @Override
    public Cursor get(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder, String limit) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                tableName,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                limit
        );
    }

    @Override
    public long insert(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long index = -1;
        try {
            index = db.insert(tableName, null, values);
            Log.e("insert",">>>");
        } catch (SQLException ex) {
            AppLogger.Logger.error(TAG, "INSERT : " + ex.getMessage(), ex);
        }
        return index;
    }

    @Override
    public long update(String tableName, ContentValues values, String selection, String[] selectionArg) {
        SQLiteDatabase db = this.getWritableDatabase();
        long index = -1;
        try {
            index = db.update(tableName, values, selection, selectionArg);
        } catch (SQLException ex) {
            AppLogger.Logger.error(TAG, "UPDATE : " + ex.getMessage(), ex);
        }
        return index;
    }

    @Override
    public int delete(String tableName, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int index = -1;
        try {
            index = db.delete(tableName, selection, selectionArgs);
        } catch (SQLException ex) {
            AppLogger.Logger.error(TAG, "DELETE : " + ex.getMessage(), ex);
        }
        return index;
    }

    @Override
    public int rowCount(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return 0;
    }

    @Override
    public int convertBooleanToInt(boolean sync) {
        return !sync ? 0 : 1;
    }

    @Override
    public boolean convertIntToBoolean(int sync) {
        return sync != 0;
    }

}