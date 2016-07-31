package com.oli365.nc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=7;
    public static final String DATABASE_NAME="nc.db";
    public static SQLiteDatabase database;



    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    public static SQLiteDatabase getDatabase(Context context){
        if (database == null || !database.isOpen()) {
            database = new DbHelper(context).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecordDAO.CREATE_TABLE);
       // db.execSQL(CustomerController.SQL_CREATE_ENTRIES);
        //db.execSQL(BodyRecordController.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion==6){
            db.execSQL(PictureDAO.CREATE_TABLE);
        }
        //db.execSQL(RecordDAO.DROP_TABLE);
        // 呼叫onCreate建立新版的表格
        //onCreate(db);
       // db.execSQL(CustomerController.SQL_DELETE_ENTRIES);
       // db.execSQL(BodyRecordController.SQL_DELETE_ENTRIES);
        //onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
    }
}
