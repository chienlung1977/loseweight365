package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.PhraseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/11/19.
 */

public class PhraseDAO {

    final static String TAG =PhraseDAO.class.getName();
    // 表格名稱
    public static final String TABLE_NAME = "PHRASE";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String PHRASE_UID_COLUMN="PHRASE_UID";
    public static final String USER_UID_COLUMN ="USER_UID";
    public static final String DATETIME_COLUMN = "CREATE_DATE";
    public static final String PHRASE_TYPE_COLUMN = "PHRASE_TYPE";
    public static final String MEMO_COLUMN = "MEMO";
    public static final String UPLOAD_STATUS_CLUMN ="UPLOAD_STATUS";
    public static final String STATUS_COLUMN ="STATUS";
    public static final String SOURCE_COLUMN ="SOURCE";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PHRASE_UID_COLUMN + " TEXT NOT NULL, " +
                    USER_UID_COLUMN + " TEXT NOT NULL, " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    PHRASE_TYPE_COLUMN + " TEXT NOT NULL, " +
                    MEMO_COLUMN + " TEXT NOT NULL, " +
                    UPLOAD_STATUS_CLUMN + " TEXT NOT NULL, " +
                    STATUS_COLUMN + " TEXT NOT NULL, " +
                    SOURCE_COLUMN + " TEXT NOT NULL )";

    public static final String DROP_TABLE ="DROP TABLE " + TABLE_NAME;

    Context context;


    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public PhraseDAO(Context context) {
        if(db==null){
            db = DbHelper.getDatabase(context);
        }
        this.context=context;
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    /*
    public  void insertInitData(){

        PhraseModel pm =new PhraseModel(0,Utility.getToday(), PhraseModel.PHRASE_TYPE.COMMON ,"運動前");
        insert(pm);
        pm =new PhraseModel(0,Utility.getToday(), PhraseModel.PHRASE_TYPE.COMMON ,"運動後");
        insert(pm);
        pm =new PhraseModel(0,Utility.getToday(), PhraseModel.PHRASE_TYPE.COMMON ,"吃飯後");
        insert(pm);
        pm =new PhraseModel(0,Utility.getToday(), PhraseModel.PHRASE_TYPE.COMMON ,"吃飯後");
        insert(pm);

    }


*/


    public boolean update(PhraseModel item){
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(PHRASE_UID_COLUMN,item.PhraseUid);
        cv.put(USER_UID_COLUMN,item.UserUid);
        cv.put(DATETIME_COLUMN,item.CreateDate);
        cv.put(PHRASE_TYPE_COLUMN,item.PhraseType.toString());
        cv.put(MEMO_COLUMN,item.Memo);
        cv.put(UPLOAD_STATUS_CLUMN, item.UploadStatus.toString());
        cv.put(STATUS_COLUMN, item.Status);
        cv.put(SOURCE_COLUMN, item.Source.toString());


        String whereClause = PHRASE_UID_COLUMN + "=?";
        String[] whereArgs = new String[] {
                item.PhraseUid
        };


        return  db.update(TABLE_NAME,cv, whereClause, whereArgs)>0;

    }

    // 新增參數指定的物件
    public PhraseModel insert(PhraseModel item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(PHRASE_UID_COLUMN,item.PhraseUid);
        cv.put(USER_UID_COLUMN,item.UserUid);
        cv.put(DATETIME_COLUMN,item.CreateDate);
        cv.put(PHRASE_TYPE_COLUMN,item.PhraseType.toString());
        cv.put(MEMO_COLUMN,item.Memo);
        cv.put(UPLOAD_STATUS_CLUMN, item.UploadStatus.toString());
        cv.put(STATUS_COLUMN, item.Status);
        cv.put(SOURCE_COLUMN, item.Source.toString());


        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        item.id = db.insert(TABLE_NAME, null, cv);


        // 回傳結果
        return item;
    }

    public boolean delete(String memo){

        String where = MEMO_COLUMN + "=?" ;
        String[] whereArgs = new String[] {
                memo
        };

        return db.delete(TABLE_NAME,where,whereArgs)>0;
    }

    public void deleteAll(){

        db.delete(TABLE_NAME,null,null);
    }


    public Cursor getCursorAll(){

        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);
        return cursor;
    }


    // 讀取所有記事資料
    public List<PhraseModel> getAll() {
        List<PhraseModel> result = new ArrayList<PhraseModel>();

        /*
        String whereClause = " STATUS <> ? ";
        String[] whereArgs = new String[] {
                "9"
        };
        */
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getPhrase(cursor));
        }

        cursor.close();
        return result;
    }

    //是否已有相同的名稱
    public boolean hasMemo(String memo){

        Log.d(TAG,"memo = " + memo);
        boolean resultboolean =false;
        String where = MEMO_COLUMN + "='" + memo.trim() + "'";
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        if(result.getCount()>0){
            resultboolean=true ;
        }

        result.close();
        return resultboolean;
    }

    // 取得指定編號的資料物件
    public PhraseModel get(long id) {
        // 準備回傳結果用的物件
        PhraseModel item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getPhrase(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    public PhraseModel get(String memo) {
        // 準備回傳結果用的物件
        PhraseModel item = null;
        // 使用編號為查詢條件
        String where = MEMO_COLUMN + "=?" ;
        String[] whereArgs = new String[] {
                memo
        };

        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, whereArgs, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getPhrase(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public PhraseModel getPhrase(Cursor cursor) {
        // 準備回傳結果用的物件
        PhraseModel result = new PhraseModel();

        result.id=cursor.getLong(cursor.getColumnIndex(KEY_ID));
        result.PhraseUid=cursor.getString(cursor.getColumnIndex(PHRASE_UID_COLUMN));
        result.UserUid=cursor.getString(cursor.getColumnIndex(USER_UID_COLUMN));
        result.CreateDate=cursor.getString(cursor.getColumnIndex(DATETIME_COLUMN));
        result.PhraseType= Config.PHRASE_TYPE.valueOf( cursor.getString(cursor.getColumnIndex(PHRASE_TYPE_COLUMN)));
        result.Memo=cursor.getString(cursor.getColumnIndex(MEMO_COLUMN));
        result.UploadStatus=Config.UPLOAD_STATUS.valueOf( cursor.getString(cursor.getColumnIndex(UPLOAD_STATUS_CLUMN)));
        result.Status=cursor.getString(cursor.getColumnIndex(STATUS_COLUMN));
        result.Source=Config.SOURCE_TYPE.valueOf( cursor.getString(cursor.getColumnIndex(SOURCE_COLUMN)));


        // 回傳結果
        return result;
    }


}
