package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oli365.nc.model.CaloryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class CaloryDAO {


    private static final String TAG=CaloryDAO.class.getName();

    // 表格名稱
    public static final String TABLE_NAME = "CALORY";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String DATETIME_COLUMN = "CREATE_TIME";
    public static final String BREAKFAST_COLUMN = "BREAKFAST";
    public static final String LUNCH_COLUMN = "LUNCH";
    public static final String DINNER_COLUMN = "DINNER";
    public static final String DESSERT_COLUMN = "DESSERT";
    public static final String SPORT_COLUMN = "SPORT";
    public static final String MEMO_COLUMN = "MEMO";
    // public static final String LASTMODIFY_COLUMN = "lastmodify";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    BREAKFAST_COLUMN + " INTEGER , " +
                    LUNCH_COLUMN + " INTEGER ," +
                    DINNER_COLUMN + " INTEGER ," +
                    DESSERT_COLUMN + " INTEGER," +
                    SPORT_COLUMN + " INTEGER," +
                    MEMO_COLUMN + " TEXT )";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public CaloryDAO(Context context) {
        db = DbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public CaloryModel insert(CaloryModel item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getCreatedate().replace("/","-"));
        cv.put(BREAKFAST_COLUMN, item.getBreakfast());
        cv.put(LUNCH_COLUMN, item.getLunch());
        cv.put(DINNER_COLUMN, item.getDinner());
        cv.put(DESSERT_COLUMN, item.getDessert());
        cv.put(SPORT_COLUMN, item.getSport());
        cv.put(MEMO_COLUMN, item.getMemo());

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

    //偵錯模式刪除所有資料
    public void deleteAll(){
        db.delete(TABLE_NAME,null,null);
    }


    public boolean delete(long id ){
        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }

    //刪除特定日期的資料
    public void deleteFromDate(String date){

        String where =" date(CREATE_TIME)='" + date.replace("/","-") + "'";
        db.delete(TABLE_NAME,where,null);
    }

    public boolean update(CaloryModel item){

        ContentValues cv = new ContentValues();
        if(item.getId()==0){
            //直接新增
            insert(item);
            return true;
        }

        //更新資料
        cv.put(BREAKFAST_COLUMN, item.getBreakfast());
        cv.put(LUNCH_COLUMN, item.getLunch());
        cv.put(DINNER_COLUMN, item.getDinner());
        cv.put(DESSERT_COLUMN, item.getDessert());
        cv.put(SPORT_COLUMN, item.getSport());
        cv.put(MEMO_COLUMN, item.getMemo());

        return  db.update(TABLE_NAME,cv,"_id=" + item.getId(),null)>0;
    }


    // 讀取所有記事資料
    public List<CaloryModel> getAll() {
        List<CaloryModel> result = new ArrayList<CaloryModel>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getCalory(cursor));
        }

        cursor.close();
        return result;
    }

    //取得今天往前推30天的記錄
    public List<CaloryModel> getLatest30(){

        List<CaloryModel> result = new ArrayList<CaloryModel>();

        //Log.i(TAG,)
        String sql ="SELECT *,date(CREATE_TIME) AS A FROM " + TABLE_NAME + " WHERE CREATE_TIME>date('now','-30 day') AND NOT A IS NULL ORDER BY CREATE_TIME DESC";
        Cursor cursor = db.rawQuery(sql,null);

        while (cursor.moveToNext()) {
            result.add(getCalory(cursor));
        }

        cursor.close();
        return result;

    }

    public CaloryModel getByDate(String date){

        CaloryModel item =null;

        //日期是字串格式所以符號要一致為-
        String where = "  date(" + DATETIME_COLUMN + ")='" + date.replace("/","-") + "'";

        // 執行查詢
        Log.d(TAG,"where = " + where);

        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, " CREATE_TIME DESC", "1");

       // Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + where , null);


        // 如果有查詢結果
        if (result!=null && result.getCount()>0) {
            // 讀取包裝一筆資料的物件
            result.moveToFirst();
            item = getCalory(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 取得指定編號的資料物件
    public CaloryModel get(long id) {
        // 準備回傳結果用的物件
        CaloryModel item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getCalory(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public CaloryModel getCalory(Cursor cursor) {
        // 準備回傳結果用的物件
        CaloryModel result = new CaloryModel();

        result.setId(cursor.getLong(0));
        result.setCreatedate(cursor.getString(1));
        result.setBreakfast(cursor.getInt(2));
        result.setLunch(cursor.getInt(3));
        result.setDinner(cursor.getInt(4));
        result.setDessert(cursor.getInt(5));
        result.setSport(cursor.getInt(6));
        result.setMemo(cursor.getString(7));
        //result.setLastModify(cursor.getLong(8));

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
