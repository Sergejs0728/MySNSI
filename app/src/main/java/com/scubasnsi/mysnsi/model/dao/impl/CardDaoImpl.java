package com.scubasnsi.mysnsi.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.model.DataBase.DatabaseHandler;
import com.scubasnsi.mysnsi.model.DataBase.impl.DatabaseContractor;
import com.scubasnsi.mysnsi.model.dao.CardListDao;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;

import java.util.ArrayList;
import java.util.List;


/**
 * Author -
 * Date -  20-04-2017.
 */

public class CardDaoImpl implements CardListDao {

    private DatabaseHandler mDatabaseHandler;

    public CardDaoImpl() {
        mDatabaseHandler = MyApplication.getApplicationInstance().getDatabaseHandler();
    }

    @Override
    public void saveList(List<C_Cards> c_cards) {
        clearTable();
        for (C_Cards cCards : c_cards) {
            save(cCards);
        }
    }

    @Override
    public List<C_Cards> getList() {
        List<C_Cards> checkLists = null;
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.CardsEntry.TABLE_NAME, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            checkLists = new ArrayList<>();
            do {
                C_Cards c_cards = new C_Cards();
                c_cards.setCardId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID)));
                c_cards.setProfessionalQualificationId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_PROFESSIONAL_ID)));
                c_cards.setCertificationId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_CERTIFICATION_ID)));
                c_cards.setMasterCourseId(cursor.getLong(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_MASTER_ID)));
                c_cards.setCourseName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_COURSE_NAME)));
                c_cards.setCourseNickName(cursor.getString(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_COURSE_NICK_NAME)));
                c_cards.setCourseUrl(cursor.getString(cursor.getColumnIndex(DatabaseContractor.CardsEntry.COLUMN_NAME_IMAGE_NAME)));

                checkLists.add(c_cards);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return checkLists;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void clearTable() {

        mDatabaseHandler.delete(DatabaseContractor.CardsEntry.TABLE_NAME, null, null);
    }

    // Save Card
    private void save(C_Cards c_cards) {
        ContentValues contentValues = new ContentValues();

        Log.e("save",">>>");
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID, c_cards.getCardId());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_PROFESSIONAL_ID, c_cards.getProfessionalQualificationId());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_CERTIFICATION_ID, c_cards.getCertificationId());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_MASTER_ID, c_cards.getMasterCourseId());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_COURSE_NAME, c_cards.getCourseName());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_COURSE_NICK_NAME, c_cards.getCourseNickName());
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_IMAGE_NAME, c_cards.getCourseUrl());
        mDatabaseHandler.insert(DatabaseContractor.CardsEntry.TABLE_NAME, contentValues);
    }

    //PDF
    @Override
    public String getPDFFileName(long cardId) {
        String fileName = null;
        String[] projection = {DatabaseContractor.CardsPDFEntry.COLUMN_NAME_PDF_NAME};
        String selection = DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID + "=?";
        String[] selectionArg = {String.valueOf(cardId)};
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.CardsPDFEntry.TABLE_NAME, projection, selection, selectionArg, null);
        if (cursor.moveToFirst()) {
            do {
                fileName = cursor.getString(cursor.getColumnIndex(DatabaseContractor.CardsPDFEntry.COLUMN_NAME_PDF_NAME));
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return fileName;
    }

    @Override
    public void saveOrUpdatePDF(long cardId, String fileName) {
        String[] projection = {DatabaseContractor.CardsPDFEntry.COLUMN_NAME_PDF_NAME};
        String selection = DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID + "=?";
        String[] selectionArg = {String.valueOf(cardId)};
        Cursor cursor = mDatabaseHandler.get(DatabaseContractor.CardsPDFEntry.TABLE_NAME, projection, selection, selectionArg, null);
        if (cursor.getCount() > 0)
            updatePDF(cardId, fileName);
        else
            savePDF(cardId, fileName);
    }

    @Override
    public void clearPDFTable() {
        mDatabaseHandler.delete(DatabaseContractor.CardsPDFEntry.TABLE_NAME, null, null);
    }


    // Save PDF
    private void savePDF(Long cardId, String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID, cardId);
        contentValues.put(DatabaseContractor.CardsPDFEntry.COLUMN_NAME_PDF_NAME, fileName);
        mDatabaseHandler.insert(DatabaseContractor.CardsPDFEntry.TABLE_NAME, contentValues);
    }

    //Update PDF
    private void updatePDF(Long cardId, String fileName) {
        ContentValues contentValues = new ContentValues();
        String selection = DatabaseContractor.CardsEntry.COLUMN_NAME_CARD_ID + "=?";
        String[] selectionArg = {String.valueOf(cardId)};
        contentValues.put(DatabaseContractor.CardsPDFEntry.COLUMN_NAME_PDF_NAME, fileName);
        mDatabaseHandler.update(DatabaseContractor.CardsPDFEntry.TABLE_NAME, contentValues, selection, selectionArg);
    }

}
