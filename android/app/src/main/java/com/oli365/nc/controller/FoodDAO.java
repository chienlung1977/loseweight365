package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.FoodModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/11/28.
 */

public class FoodDAO {


    private static final String TAG=FoodDAO.class.getName();
    // 表格名稱
    public static final String TABLE_NAME = "FOOD";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱

    public static final String FOOD_UID_COLUMN ="FOOD_UID";
    public static final String FOOD_ID_COLUMN ="FOOD_ID";
    public static final String FOOD_NAME_COLUMN = "FOOD_NAME";
    public static final String DATETIME_COLUMN = "CREATE_DATE";
    public static final String CALORY_COLUMN = "CALORY";
    public static final String MEMO_COLUMN = "MEMO";
    public static final String STATUS_COLUMN="STATUS";
    public static final String PHOTO_COLUMN ="PHOTO";
    public static final String PER_UNIT_COLUMN ="PER_UNIT";
    public static final String USER_UID_COLUMN ="USER_UID";
    public static final String SOURCE_COLUMN ="SOURCE";
    public static final String UPLOAD_STATUS_COLUMN ="UPLOAD_STATUS";
    public static final String MAIN_FOOD_TYPE_COLUMN="MAIN_FOOD_TYPE";
    public static final String FOOD_TYPE_UID_COLUMN = "FOOD_TYPE_UID";
    public static final String IS_SHARE_COLUMN ="IS_SHARE";



    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FOOD_UID_COLUMN + " TEXT NOT NULL, " +
                    FOOD_ID_COLUMN + " TEXT NOT NULL, " +
                    FOOD_NAME_COLUMN + " TEXT NOT NULL, " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    CALORY_COLUMN + " TEXT NOT NULL, " +
                    MEMO_COLUMN + " TEXT NOT NULL, " +
                    STATUS_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_COLUMN + " TEXT NOT NULL, " +
                    PER_UNIT_COLUMN + " TEXT NOT NULL , " +
                    USER_UID_COLUMN + " TEXT NOT NULL, " +
                    SOURCE_COLUMN + " TEXT NOT NULL  ," +
                    UPLOAD_STATUS_COLUMN + " TEXT NOT NULL  ," +
                    MAIN_FOOD_TYPE_COLUMN + " TEXT NOT NULL  ," +
                    FOOD_TYPE_UID_COLUMN + " TEXT," +
                    IS_SHARE_COLUMN + " TEXT )";

    public static final String DROP_FOOD_TABLE ="DROP TABLE " + "FOOD";

    public static final String UPGRADE_TABLE15=" ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + MAIN_FOOD_TYPE_COLUMN + " TEXT ";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public FoodDAO(Context context) {
        db = DbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public FoodModel insert(FoodModel item) {

        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(FOOD_UID_COLUMN, item.FoodUid);
        cv.put(FOOD_ID_COLUMN, item.FoodId);
        cv.put(FOOD_NAME_COLUMN, item.FoodName);
        cv.put(DATETIME_COLUMN, item.CreateDate);
        cv.put(CALORY_COLUMN, item.Calory);
        cv.put(MEMO_COLUMN, item.Memo);
        cv.put(STATUS_COLUMN, item.Status);
        cv.put(PHOTO_COLUMN, item.Photo);
        cv.put(PER_UNIT_COLUMN, item.Perunit);
        cv.put(USER_UID_COLUMN, item.UserUid);
        cv.put(SOURCE_COLUMN, item.Source);
        cv.put(UPLOAD_STATUS_COLUMN, item.UploadStatus.toString());
        cv.put(MAIN_FOOD_TYPE_COLUMN,item.MainFoodType.toString());
        cv.put(FOOD_TYPE_UID_COLUMN, item.FoodTypeUid);
        cv.put(IS_SHARE_COLUMN,item.IsShare);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        item.Id  = db.insert(TABLE_NAME, null, cv);


        return item;
    }

    public boolean delete(long id ){
        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }


    //是否已有相同的名稱
    public boolean hasFood(String foodname){

        boolean resultboolean =false;
        String where = FOOD_NAME_COLUMN + "='" + foodname.trim() + "'";
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        if(result.getCount()>0){
            resultboolean=true ;
        }

        result.close();
        return resultboolean;
    }

    public Cursor getCursorAll(){
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);
        return cursor;
    }

    //取得該日期下的主類別資料
    public List<FoodModel> getList(String date, FoodModel.MAIN_FOOD_TYPE mainFoodType){

        List<FoodModel> result = new ArrayList<FoodModel>();

        String whereClause = " STATUS = ? AND date(CREATE_DATE)=date(?) AND MAIN_FOOD_TYPE=? ";
        String[] whereArgs = new String[] {
                "1",date,mainFoodType.toString()
        };

        Cursor cursor = db.query(
                TABLE_NAME, null, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getFood(cursor));
        }

        cursor.close();
        return result;
    }

    // 讀取所有記事資料
    public List<FoodModel> getAll() {
        List<FoodModel> result = new ArrayList<FoodModel>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getFood(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public FoodModel get(long id) {
        // 準備回傳結果用的物件
        FoodModel item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getFood(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public FoodModel getFood(Cursor cursor) {
        // 準備回傳結果用的物件
        FoodModel result = new FoodModel();

        result.Id=cursor.getLong(cursor.getColumnIndex(KEY_ID));
        result.FoodUid=cursor.getString(cursor.getColumnIndex(FOOD_UID_COLUMN));
        result.FoodId=cursor.getString(cursor.getColumnIndex(FOOD_ID_COLUMN));
        result.CreateDate=cursor.getString(cursor.getColumnIndex(DATETIME_COLUMN));
        result.UserUid=cursor.getString(cursor.getColumnIndex(USER_UID_COLUMN));
        result.FoodTypeUid=cursor.getString(cursor.getColumnIndex(FOOD_TYPE_UID_COLUMN));
        result.Photo=cursor.getString(cursor.getColumnIndex(PHOTO_COLUMN));
        result.Calory=cursor.getString(cursor.getColumnIndex(CALORY_COLUMN));
        result.Perunit=cursor.getString(cursor.getColumnIndex(PER_UNIT_COLUMN));
        result.Memo=cursor.getString(cursor.getColumnIndex(MEMO_COLUMN));

        result.Status=cursor.getString(cursor.getColumnIndex(STATUS_COLUMN));
        result.Source=cursor.getString(cursor.getColumnIndex(SOURCE_COLUMN));
        result.UploadStatus= Config.UPLOAD_STATUS.valueOf(cursor.getString(cursor.getColumnIndex(UPLOAD_STATUS_COLUMN)));
        result.MainFoodType= FoodModel.MAIN_FOOD_TYPE.valueOf( cursor.getString(cursor.getColumnIndex(MAIN_FOOD_TYPE_COLUMN)));
        result.IsShare = cursor.getString(cursor.getColumnIndex(IS_SHARE_COLUMN));


        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE STATUS='1'", null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
}
