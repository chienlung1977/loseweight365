package com.oli365.nc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/3/31.
 */
public class RecordDAO {

    // 表格名稱
    public static final String TABLE_NAME = "RECORD";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String DATETIME_COLUMN = "CREATE_TIME";
    public static final String WEIGHT_COLUMN = "WEIGHT";
    public static final String FAT_RATE_COLUMN = "FAT_RATE";
    public static final String BONE_WEIGHT_COLUMN = "BONE_WEIGHT";
    public static final String BODY_AGE_COLUMN = "BODY_AGE";
    public static final String INSIDE_FAT_COLUMN = "INSIDE_FAT";
    public static final String MUSCLE_WEIGHT_COLUMN = "MUSCLE_WEIGHT";
    public static final String MUSCLE_RATE_COLUMN = "MUSCLE_RATE";
    public static final String METABOLISM_COLUMN = "METABOLISM";
    public static final String PHOTO_COLUMN ="PHOTO";
    public static final String UPLOAD_COLUMN ="UPLOAD";
    public static final String STATUS_COLUMN ="STATUS";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    WEIGHT_COLUMN + " REAL NOT NULL, " +
                    FAT_RATE_COLUMN + " REAL NOT NULL, " +
                    BONE_WEIGHT_COLUMN + " REAL , " +
                    BODY_AGE_COLUMN + " REAL , " +
                    INSIDE_FAT_COLUMN + " REAL , " +
                    MUSCLE_WEIGHT_COLUMN + " REAL , " +
                    MUSCLE_RATE_COLUMN + " REAL , " +
                    METABOLISM_COLUMN + " REAL ," +
                    PHOTO_COLUMN + " TEXT ," +
                    UPLOAD_COLUMN + " TEXT DEFAULT '0' , " +
                    STATUS_COLUMN + " TEXT DEFAULT '1' )";

    public static final String DROP_TABLE ="DROP TABLE " + TABLE_NAME;

    //版本7-->8
    public static final String UPGRADE_TABLE =
                    " ALTER TABLE " + TABLE_NAME + " ADD CLUMN " + UPLOAD_COLUMN + " TEXT DEFAULT '0'" +
                    ";ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + STATUS_COLUMN + " TEXT DEFAULT '1' ";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public RecordDAO(Context context) {
        db = DbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public Record insert(Record item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getCreateTime());
        cv.put(WEIGHT_COLUMN, item.getWeight());
        cv.put(FAT_RATE_COLUMN, item.getFatRate());

        cv.put(BONE_WEIGHT_COLUMN, item.getBoneweight());
        cv.put(BODY_AGE_COLUMN, item.getBodyage());
        cv.put(INSIDE_FAT_COLUMN, item.getInsidefat());
        cv.put(MUSCLE_WEIGHT_COLUMN, item.getMuscleweight());
        cv.put(MUSCLE_RATE_COLUMN, item.getMusclerate());
        cv.put(METABOLISM_COLUMN, item.getMetabolism());
        cv.put(PHOTO_COLUMN,item.getPhoto());


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

        //todo v1.07修改為update status =0 (刪除)，同步後才用服務刪除，或upload=1 and status=0才刪除
        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }

    public boolean update(Record record){

        ContentValues cv = new ContentValues();
        //加上更新的參數

       return  db.update(TABLE_NAME,cv,"_id=" + record.getId(),null)>0;
    }


    // 讀取所有記事資料
    public List<Record> getAll() {
        List<Record> result = new ArrayList<Record>();

        String whereClause = " STATUS = ? ";
        String[] whereArgs = new String[] {
                "1"
        };
        Cursor cursor = db.query(
                TABLE_NAME, null, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Record get(long id) {
        // 準備回傳結果用的物件
        Record item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public Record getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Record result = new Record();

        result.setId(cursor.getLong(0));
        result.setCreateTime(cursor.getString(1));
        result.setWeight(cursor.getDouble(2));
        result.setFatRate(cursor.getDouble(3));
        result.setBoneweight(cursor.getDouble(4));
        result.setBodyage(cursor.getDouble(5));
        result.setInsidefat(cursor.getDouble(6));
        result.setMuscleweight(cursor.getDouble(7));
        result.setMusclerate(cursor.getDouble(8));
        result.setMetabolism(cursor.getDouble(9));
        result.setPhoto(cursor.getString(10));
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
