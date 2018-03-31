package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oli365.nc.model.PictureModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/7/12.
 */
public class PictureDAO {

    // 表格名稱
    public static final String TABLE_NAME = "PICTURE";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String DATETIME_COLUMN = "CREATE_TIME";
    public static final String PICTURE_COLUMN = "PICTURE_PATH";
    public static final String SHOW_TIME_COLUMN = "SHOW_TIME";
    public static final String STATUS_COLUMN = "STATUS";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    PICTURE_COLUMN + " TEXT NOT NULL, " +
                    SHOW_TIME_COLUMN + " TEXT NOT NULL, " +
                    STATUS_COLUMN + " TEXT)";

    public static final String DROP_TABLE ="DROP TABLE " + TABLE_NAME;

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public PictureDAO(Context context) {
        db = DbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public PictureModel insert(PictureModel item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getCreatedate());
        cv.put(PICTURE_COLUMN, item.getPicturepath());
        cv.put(SHOW_TIME_COLUMN, item.getShowtime());

        cv.put(STATUS_COLUMN, item.getStatus());



        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    public boolean delete(long id ){
        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }

    public boolean update(PictureModel item){

        ContentValues cv = new ContentValues();


        return  db.update(TABLE_NAME,cv,"_id=" + item.getId(),null)>0;
    }


    // 讀取所有記事資料
    public List<PictureModel> getAll() {
        List<PictureModel> result = new ArrayList<PictureModel>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getItem(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public PictureModel get(long id) {
        // 準備回傳結果用的物件
        PictureModel item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getItem(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public PictureModel getItem(Cursor cursor) {
        // 準備回傳結果用的物件
        PictureModel result = new PictureModel();

        result.setId(cursor.getLong(0));
        result.setCreatedate(cursor.getString(1));
        result.setPicturepath(cursor.getString(2));
        result.setShowtime(cursor.getString(3));
        result.setStatus(cursor.getString(4));

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }


}
