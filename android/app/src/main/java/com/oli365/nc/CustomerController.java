package com.oli365.nc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;




/**
 * Created by alvinlin on 2015/10/27.
 */
public final class CustomerController {

    public CustomerController() {
    }

    public static final String TEXT_TYPE=" TEXT";
    public static final String COMMA_SEP=",";
    public static final String SQL_CREATE_ENTRIES="CREATE TABLE " + CustomerModel.TABLE_NAME +
            " (" + CustomerModel._ID + " INTEGER PRIMARY KEY," +
            CustomerModel.CUSTOMER_ID + TEXT_TYPE + COMMA_SEP +
            CustomerModel.CUSTOMER_NAME + TEXT_TYPE + COMMA_SEP +
            CustomerModel.SEX + TEXT_TYPE + COMMA_SEP +
            CustomerModel.WEIGHT + TEXT_TYPE +COMMA_SEP +
            CustomerModel.HEIGHT + TEXT_TYPE +COMMA_SEP +
            CustomerModel.BIRTHDAY + TEXT_TYPE + COMMA_SEP +
            CustomerModel.SELLPHONE + TEXT_TYPE +COMMA_SEP +
            CustomerModel.HOME_PHONE + TEXT_TYPE  +
            ")";
    public static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "  + CustomerModel.TABLE_NAME;



    public static abstract class CustomerModel implements BaseColumns{

        public static final String TABLE_NAME="CUSTOMER";
        public static final String CUSTOMER_ID="CUSTOMER_ID";
        public static final String CUSTOMER_NAME="CUSTOMER_NAME";
        public static final String SEX="SEX";
        public static final String WEIGHT="WEIGHT";
        public static final String HEIGHT ="HEIGHT";
        public static final String BIRTHDAY="BIRTHDAY";
        public static final String SELLPHONE="SELLPHONE";
        public static final String HOME_PHONE="HOME_PHONE";


    }
}
