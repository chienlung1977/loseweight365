package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.FoodTypeModel;

import java.util.ArrayList;

/**
 * Created by alvinlin on 2016/12/26.
 */

public class FoodTypeDAO {

    public static final String TAG=FoodTypeDAO.class.getName();
    Context context ;

    // 資料庫物件
    private SQLiteDatabase db;


    // 表格名稱
    public static final String TABLE_NAME = "FOOD_TYPE";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String UID_COLUMN = "FOOD_TYPE_UID";
    public static final String DATETIME_COLUMN = "CREATE_DATE";
    public static final String FOOD_TYPE_ID_COLUMN = "FOOD_TYPE_ID";
    public static final String FOOD_TYPE_NAME_COLUMN = "FOOD_TYPE_NAME";
    public static final String MEMO_COLUMN = "MEMO";
    public static final String STATUS_COLUMN = "STATUS";
    //public static final String UPLOAD_COLUMN ="IS_UPLOAD";
    public static final String USER_UID_COLUMN ="USER_UID";
    public static final String SOURCE_COLUMN ="SOURCE";
    public static final String UPLOAD_STATUS_COLUMN ="UPLOAD_STATUS";



    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UID_COLUMN + " TEXT NOT NULL UNIQUE , " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    FOOD_TYPE_ID_COLUMN + " TEXT NOT NULL UNIQUE, " +
                    FOOD_TYPE_NAME_COLUMN + " TEXT NOT NULL, " +
                    STATUS_COLUMN + " TEXT NOT NULL DEFAULT '1' , " +
                    MEMO_COLUMN + " TEXT NOT NULL DEFAULT ''," +
                    USER_UID_COLUMN + " TEXT , " +
                    SOURCE_COLUMN + " TEXT , " +
                    UPLOAD_STATUS_COLUMN + ")";

    public static final String DROP_TABLE ="DROP TABLE " + TABLE_NAME;

    public static final String UPGRADE_TABLE17  =  " ALTER TABLE " + TABLE_NAME +
            " ADD COLUMN " + USER_UID_COLUMN + " TEXT " +
            ", ADD COLUMN " + SOURCE_COLUMN + " TEXT " +
            ", ADD COLUMN " + UPLOAD_STATUS_COLUMN + " TEXT";

    // 建構子，一般的應用都不需要修改
    public FoodTypeDAO(Context context) {
        this.context = context;
        db = DbHelper.getDatabase(context);
    }


    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public FoodTypeModel insert(FoodTypeModel item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料


        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UID_COLUMN,item.TypeUid);
        cv.put(DATETIME_COLUMN, item.CreateDate);
        cv.put(FOOD_TYPE_ID_COLUMN, item.TypeId);
        cv.put(FOOD_TYPE_NAME_COLUMN, item.TypeName);
        cv.put(STATUS_COLUMN,item.Status);
        cv.put(MEMO_COLUMN,item.Memo);
        cv.put(USER_UID_COLUMN,item.UserUid);
        cv.put(SOURCE_COLUMN,item.Source);
        cv.put(UPLOAD_STATUS_COLUMN,item.UploadStatus.toString());


        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        item.Id = db.insert(TABLE_NAME, null, cv);


        return item;
    }

    //public boolean updateUpload()

    public boolean delete(long id ){

        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }

    public void deleteAll(){db.delete(TABLE_NAME,"_id>0",null);}

    // 讀取所有記事資料
    public ArrayList<FoodTypeModel> getAll() {
        ArrayList<FoodTypeModel> result = new ArrayList<FoodTypeModel>();

        String whereClause = " STATUS = ?  ";
        String[] whereArgs = new String[] {
                "1"
        };
        Cursor cursor = db.query(
                TABLE_NAME, null, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getItem(cursor));
        }

        cursor.close();
        return result;
    }


    public Cursor getList(String userUid){

        String whereClause = " STATUS = ? AND  USER_UID= ? ";
        String[] whereArgs = new String[] {
                "1",userUid
        };

//        String[] columns =new String[]{
//                "_id","FOOD_TYPE_UID","FOOD_TYPE_NAME"
//        };
        Cursor cursor = db.query(
                TABLE_NAME, null, whereClause, whereArgs, null, null, null, null);

        return cursor;
    }

    public FoodTypeModel getItem(String typeUid){
        FoodTypeModel item = null;
        // 使用編號為查詢條件
        String where = UID_COLUMN + "=?";
        String[] whereArgs = new String[] {
                typeUid
        };
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, whereArgs, null, null, null, null);

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

    // 取得指定編號的資料物件
    public FoodTypeModel get(long id) {
        // 準備回傳結果用的物件
        FoodTypeModel item = null;
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
    public FoodTypeModel getItem(Cursor cursor) {
        // 準備回傳結果用的物件
        FoodTypeModel result = new FoodTypeModel();

        result.Id=cursor.getLong(cursor.getColumnIndex(KEY_ID));
        result.TypeUid=cursor.getString(cursor.getColumnIndex(UID_COLUMN));
        result.CreateDate=cursor.getString(cursor.getColumnIndex(DATETIME_COLUMN));
        result.TypeId=cursor.getString(cursor.getColumnIndex(FOOD_TYPE_ID_COLUMN));
        result.TypeName=cursor.getString(cursor.getColumnIndex(FOOD_TYPE_NAME_COLUMN));
        result.Memo=cursor.getString(cursor.getColumnIndex(MEMO_COLUMN));
        result.Status=cursor.getString(cursor.getColumnIndex(STATUS_COLUMN));

        result.UserUid=cursor.getString(cursor.getColumnIndex(USER_UID_COLUMN));
        result.Source=cursor.getString(cursor.getColumnIndex(SOURCE_COLUMN));
        result.UploadStatus= Config.UPLOAD_STATUS.valueOf(cursor.getString(cursor.getColumnIndex(UPLOAD_STATUS_COLUMN)));
        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE (STATUS='1' OR STATUS='0') ", null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }



}
