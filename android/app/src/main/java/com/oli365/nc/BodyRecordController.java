package com.oli365.nc;

import android.provider.BaseColumns;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class BodyRecordController {


    public static final String TEXT_TYPE=" TEXT";
    public static final String COMMA_SEP=",";
    public static final String SQL_CREATE_ENTRIES="CREATE TABLE " + BodyRecordModel.TABLE_NAME +
            " (" + BodyRecordModel._ID + " INTEGER PRIMARY KEY," +
            BodyRecordModel.CUSTOMER_ID + TEXT_TYPE + COMMA_SEP +
            BodyRecordModel.CREATE_TIME + TEXT_TYPE + COMMA_SEP +
            BodyRecordModel.WEIGHT + TEXT_TYPE + COMMA_SEP +
            BodyRecordModel.WEIGHT_DIFF + TEXT_TYPE +COMMA_SEP +
            BodyRecordModel.FAT_RATE + TEXT_TYPE +COMMA_SEP +
            BodyRecordModel.FAT_RATE_DIFF + TEXT_TYPE  +
            ")";
    public static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "  + BodyRecordModel.TABLE_NAME;


    public static abstract class BodyRecordModel implements BaseColumns{

        public static final String TABLE_NAME="BODY_RECORD";
        public static final String CUSTOMER_ID="CUSTOMER_ID";
        public static final String CREATE_TIME="CREATE_TIME";
        public static final String WEIGHT="WEIGHT";
        public static final String WEIGHT_DIFF="WEIGHT_DIFF";
        public static final String FAT_RATE="FAT_RATE";
        public static final String FAT_RATE_DIFF="FAT_RATE_DIFF";

    }

}
