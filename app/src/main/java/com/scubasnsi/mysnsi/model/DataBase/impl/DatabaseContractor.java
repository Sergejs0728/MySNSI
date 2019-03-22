package com.scubasnsi.mysnsi.model.DataBase.impl;

import android.content.ContentValues;
import android.os.Parcel;
import android.provider.BaseColumns;

import java.util.Map;

import static android.os.Parcel.obtain;

/**
 * Created by joshi on 8/29/2016.
 * NOTE : Please use pure CamelCase notation for serializable variables.
 */



public class DatabaseContractor {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "my_snsi_1.0.db";


    // DATABASE FIELDS TYPES
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String LONG_TYPE = " LONG";
    public static final String INTEGER_KEY_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static final String UNIQUE_TYPE = " NOT NULL UNIQUE ";
    public static final String BOOLEAN_TYPE = " BOOLEAN";
    public static final String REAL_TYPE = " REAL";
    public static final String COMMA_SEP = ",";


    // Common Column
    public static final String COLUMN_NAME_SYNC = "Sync";
    public static final String COLUMN_NAME_GENDER = "Gender";
    public static String COLUMN_NAME_ID = "id";
    public static String COLUMN_NAME_NAME = "name";


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    /**
     * Check List entry
     */

    public static abstract class CheckListEntry implements BaseColumns {

        // Table Name
        public static final String TABLE_NAME = "CheckListEntry";
        public static final String IS_CHECKED = "is_checked";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        // Create Table
        public static String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_NAME_ID + INTEGER_KEY_TYPE + COMMA_SEP
                        + COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
                        + IS_CHECKED + INTEGER_TYPE
                        + " )";
    }

    /**
     * CARD
     *
     * @Expose
     * @SerializedName("Anagrafica_corsi_Id") private long mMasterCourseId;
     * @Expose
     * @SerializedName("professional_Numero_member") private long mProfessionalMemberNumber;
     */
    public static abstract class CardsEntry implements BaseColumns {

        // Table Name
        public static final String TABLE_NAME = "CardEntry";
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_PROFESSIONAL_ID = "professional_id";
        public static final String COLUMN_NAME_CERTIFICATION_ID = "certification_id";
        public static final String COLUMN_NAME_MASTER_ID = "master_course_id";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_COURSE_NICK_NAME = "course_nick_name";
        public static final String COLUMN_NAME_IMAGE_NAME = "image_name";

        // Create Table
        public static String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_NAME_CARD_ID + INTEGER_TYPE + UNIQUE_TYPE + COMMA_SEP
                        + COLUMN_NAME_PROFESSIONAL_ID + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_CERTIFICATION_ID + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_MASTER_ID + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_COURSE_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_COURSE_NICK_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_IMAGE_NAME + TEXT_TYPE
                        + " )";
    }
    public static abstract class CardsPDFEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "CardPDFEntry";
        public static final String COLUMN_NAME_PDF_NAME = "pdf_file_name";
        // Create Table
        public static String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + CardsEntry.COLUMN_NAME_CARD_ID + INTEGER_TYPE + UNIQUE_TYPE + COMMA_SEP
                        + COLUMN_NAME_PDF_NAME + TEXT_TYPE
                        + " )";
    }

    /*

     */


    public static abstract class LogBook implements BaseColumns {

        // Table Name
        public static final String TABLE_NAME = "LogBookEntry";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_LOGBOOK_ID = "logbook_id";
        public static final String COLUMN_NAME_IS_EDITED = "is_edit";

        public static final String COLUMN_NAME_MAX_DEPTH = "max_depth";
        public static final String COLUMN_NAME_DIVE_FOR = "dive_for";
        public static final String COLUMN_NAME_DIVE_TYPE = "dive_type";
        public static final String COLUMN_NAME_DIVE_DATE = "date";
        public static final String COLUMN_NAME_DIVE_TIME_IN = "dive_time_in";
        public static final String COLUMN_NAME_DIVE_TIME_OUT = "dive_time_out";
        public static final String COLUMN_NAME_DIVE_DURATION = "dive_time_total";
        public static final String COLUMN_NAME_LOCATION_NAME = "location_name";
        public static final String COLUMN_NAME_LOCATION_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LOCATION_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_DIVE_SITE_NAME = "dive_site";
        public static final String COLUMN_NAME_BUDDY_NAME = "buddy_name";
        public static final String COLUMN_NAME_INSTRUCTOR_FLAG = "instructor_flag";
        public static final String COLUMN_NAME_INSTRUCTOR_NAME = "instructor_info";
        public static final String COLUMN_NAME_DIVE_CENTER_NAME = "dive_center_name";
        public static final String COLUMN_NAME_TANK_PRESSURE_IN = "tank_pressure_in";
        public static final String COLUMN_NAME_TANK_PRESSURE_OUT = "tank_pressure_out";
        public static final String COLUMN_NAME_TANK_PRESSURE_USED = "tank_pressure_used";
        public static final String COLUMN_NAME_WEIGHT = "equipment_weight";
        public static final String COLUMN_NAME_SUIT = "equipment_suit";
        public static final String COLUMN_NAME_GAS_TYPE = "gasType";
        public static final String COLUMN_NAME_AIR_TEMP = "air_temperature";
        public static final String COLUMN_NAME_WATER_TEMP = "water_temperature";
        public static final String COLUMN_NAME_WEATHER = "wheather";
        public static final String COLUMN_NAME_VISIBILITY = "visibility";
        public static final String COLUMN_NAME_LOGO = "dive_center_logo";



        // Create Table
        public static String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_NAME_LOGBOOK_ID + INTEGER_TYPE + UNIQUE_TYPE + COMMA_SEP
                        + COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_MAX_DEPTH + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_FOR + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_TYPE + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_DATE + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_TIME_IN + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_TIME_OUT + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_DURATION + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_LOCATION_LATITUDE + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_LOCATION_LONGITUDE + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_SITE_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_BUDDY_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_INSTRUCTOR_FLAG + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_INSTRUCTOR_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_DIVE_CENTER_NAME + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_TANK_PRESSURE_IN + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_TANK_PRESSURE_OUT + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_TANK_PRESSURE_USED + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_SUIT + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_GAS_TYPE + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_AIR_TEMP + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_WATER_TEMP + REAL_TYPE + COMMA_SEP
                        + COLUMN_NAME_WEATHER + TEXT_TYPE + COMMA_SEP
                        + COLUMN_NAME_VISIBILITY + TEXT_TYPE + COMMA_SEP
                        +  COLUMN_NAME_SYNC+ INTEGER_TYPE + COMMA_SEP
                        +  COLUMN_NAME_IS_EDITED+ INTEGER_TYPE + COMMA_SEP
                        + COLUMN_NAME_LOGO + TEXT_TYPE
                        + " )";
    }

    public static ContentValues getContentValue(Map<String, String> map) {
        Parcel parcel = obtain();
        parcel.writeMap(map);
        parcel.setDataPosition(0);
        return ContentValues.CREATOR.createFromParcel(parcel);
    }

}