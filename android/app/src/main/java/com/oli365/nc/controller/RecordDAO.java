package com.oli365.nc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.RecordAvgModel;
import com.oli365.nc.model.RecordAvgMonthModel;
import com.oli365.nc.model.RecordAvgWeekModel;
import com.oli365.nc.model.RecordModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by alvinlin on 2016/3/31.
 */
public class RecordDAO {

    private static final String TAG=RecordDAO.class.getName();

    // 表格名稱
    public static final String TABLE_NAME = "RECORD";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String RECORD_UID_COLUMN = "RECORD_UID";
    public static final String DATETIME_COLUMN = "CREATE_TIME";
    public static final String USER_UID_COLUMN = "USER_UID";
    public static final String WEIGHT_COLUMN = "WEIGHT";
    public static final String FAT_RATE_COLUMN = "FAT_RATE";
    public static final String BONE_WEIGHT_COLUMN = "BONE_WEIGHT";
    public static final String BODY_AGE_COLUMN = "BODY_AGE";
    public static final String INSIDE_FAT_COLUMN = "INSIDE_FAT";
    public static final String MUSCLE_WEIGHT_COLUMN = "MUSCLE_WEIGHT";
    public static final String BODY_WATER_COLUMN = "BODY_WATER";
    public static final String METABOLISM_COLUMN = "METABOLISM";
    public static final String PHOTO_COLUMN ="PHOTO";
    public static final String UPLOAD_STATUS_COLUMN ="UPLOAD_STATUS";
    public static final String STATUS_COLUMN ="STATUS";
    public static final String MEMO_COLUMN ="MEMO";
    public static final String CALCULATE_METABOLISM_COLUMN ="CALCULATE_METABOLISM";
    public static final String CALCULATE_EXPENDITURE_COLUMN ="CALCULATE_EXPENDITURE";
    public static final String FAT_WEIGHT_COLUMN ="FAT_WEIGHT";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECORD_UID_COLUMN + " TEXT NOT NULL, " +
                    DATETIME_COLUMN + " TEXT NOT NULL, " +
                    USER_UID_COLUMN + " TEXT NOT NULL, " +
                    WEIGHT_COLUMN + " TEXT NOT NULL, " +
                    FAT_RATE_COLUMN + " TEXT NOT NULL, " +
                    BONE_WEIGHT_COLUMN + " TEXT , " +
                    BODY_AGE_COLUMN + " TEXT , " +
                    INSIDE_FAT_COLUMN + " TEXT , " +
                    MUSCLE_WEIGHT_COLUMN + " TEXT , " +
                    BODY_WATER_COLUMN + " TEXT , " +
                    METABOLISM_COLUMN + " TEXT ," +
                    PHOTO_COLUMN + " TEXT ," +
                    UPLOAD_STATUS_COLUMN + " TEXT  , " +
                    STATUS_COLUMN + " TEXT   , " +
                    MEMO_COLUMN + " TEXT ," +
                    CALCULATE_METABOLISM_COLUMN + " TEXT," +
                    CALCULATE_EXPENDITURE_COLUMN + " TEXT ," +
                    FAT_WEIGHT_COLUMN +  " TEXT )";

    public static final String DROP_TABLE ="DROP TABLE " + TABLE_NAME;


    // 資料庫物件
    private SQLiteDatabase db;
    private Context context;

    // 建構子，一般的應用都不需要修改
    public RecordDAO(Context context) {
        this.context=context;
        if(db==null){
            db = DbHelper.getDatabase(context);
        }

    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public RecordModel insert(RecordModel item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(RECORD_UID_COLUMN, item.RecordUid);
        cv.put(DATETIME_COLUMN, item.CreateDate);
        cv.put(USER_UID_COLUMN, item.UserUid);

        cv.put(WEIGHT_COLUMN, item.Weight);
        cv.put(FAT_RATE_COLUMN, item.FatRate);

        cv.put(BONE_WEIGHT_COLUMN, item.BoneWeight);
        cv.put(BODY_AGE_COLUMN, item.BodyAge);
        cv.put(INSIDE_FAT_COLUMN, item.InsideFat);
        cv.put(MUSCLE_WEIGHT_COLUMN, item.MuscleWeight);
        cv.put(BODY_WATER_COLUMN, item.BodyWater);
        cv.put(METABOLISM_COLUMN, item.Metabolism);
        cv.put(PHOTO_COLUMN,item.Photo);
        cv.put(UPLOAD_STATUS_COLUMN,item.UploadStatus.toString());
        cv.put(STATUS_COLUMN,item.Status);
        cv.put(MEMO_COLUMN,item.Memo);
        cv.put(CALCULATE_METABOLISM_COLUMN,item.CalculateMetabolism);
        cv.put(CALCULATE_EXPENDITURE_COLUMN,item.CalculateExpenditure);
        cv.put(FAT_WEIGHT_COLUMN,item.FatWeight);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        item.Id = db.insert(TABLE_NAME, null, cv);

        return item;
    }

    //public boolean updateUpload()



    //真正刪除記錄
    public boolean delete(long id ){
         return db.delete(TABLE_NAME,KEY_ID + "='" + String.valueOf(id) + "'",null)>0;
    }


    //刪除所有記錄
    public void deleteAll(){
        db.delete(TABLE_NAME,null,null);
    }




    // 讀取所有記事資料
    public List<RecordModel> getAll() {
        List<RecordModel> result = new ArrayList<RecordModel>();

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

    //取得這一週第一筆記錄
    public RecordModel getThisWeekFirstRecord(){

        //todo 補上本週第一天的資料查詢
        Calendar c=Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.DAY_OF_WEEK,(-1)*c.get(Calendar.DAY_OF_WEEK)+1);
        String first =df.format(c.getTime());
        Log.d(TAG,"本周第一天："+ first);
        //System.out.println("本周第一天："+df.format(c.getTime()));
        c.add(Calendar.DAY_OF_WEEK,7-c.get(Calendar.DAY_OF_WEEK));
        //System.out.println("本周最后一天："+df.format(c.getTime()));
        String last=df.format(c.getTime());
        Log.d(TAG,"本周最后一天："+last);

        RecordModel item = null;
        int queryId = 0;
        String sql ="SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + DATETIME_COLUMN + " > '" + first + " 00:00' AND " +  DATETIME_COLUMN +" < '" + last + " 23:59'  ORDER BY " + DATETIME_COLUMN + " ASC limit 1";
        Log.d(TAG,sql);
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor!=null && cursor.moveToNext()) {
            queryId = cursor.getInt(0);
            item = get(queryId);
        }

        return item;


    }

    //取得這一個月的第一筆記錄
    public RecordModel getThisMonthFirstRecord(){

        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

        String first=df.format(c.getTime());
        Log.d(TAG,"本月第一天："+ first );

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = df.format(ca.getTime());
        Log.d(TAG,"本月最后一天："+df.format(ca.getTime()));

        RecordModel item = null;
        int queryId = 0;
        String sql ="SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + DATETIME_COLUMN + " > '" + first + " 00:00' AND " +  DATETIME_COLUMN +" < '" + last + " 23:59'  ORDER BY " + DATETIME_COLUMN + " ASC limit 1";
        Log.d(TAG,sql);
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor!=null && cursor.moveToNext()) {
            queryId = cursor.getInt(0);
            item = get(queryId);
        }

        return item;



    }


    //取得第一筆記錄
    public RecordModel getFirtRecord(){

        RecordModel item = null;
        int queryId = 0;
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_NAME + "  ORDER BY " + DATETIME_COLUMN + " ASC limit 1", null);

        if (cursor!=null && cursor.moveToNext()) {
            queryId = cursor.getInt(0);
            item = get(queryId);
        }

        return item;
    }


    //取得最新的一筆記錄
    public RecordModel getLatestRecord(){
        RecordModel item = null;
        int queryId = 0;
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_NAME + "  ORDER BY " + DATETIME_COLUMN + " DESC limit 1", null);

        if (cursor!=null && cursor.moveToNext()) {
            queryId = cursor.getInt(0);
            item = get(queryId);
        }

        return item;
    }



    // 取得指定編號的資料物件
    public RecordModel get(long id) {
        // 準備回傳結果用的物件
        RecordModel item = null;
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
    public RecordModel getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        RecordModel result = new RecordModel();

        result.Id=cursor.getLong(cursor.getColumnIndex(KEY_ID));
        result.RecordUid=cursor.getString(cursor.getColumnIndex(RECORD_UID_COLUMN));
        result.CreateDate=cursor.getString(cursor.getColumnIndex(DATETIME_COLUMN));
        result.UserUid=cursor.getString(cursor.getColumnIndex(USER_UID_COLUMN));
        result.Weight=cursor.getDouble(cursor.getColumnIndex(WEIGHT_COLUMN));
        result.FatRate=cursor.getDouble(cursor.getColumnIndex(FAT_RATE_COLUMN));

        double fatweight = Double.valueOf(result.Weight) *  Double.valueOf(result.FatRate) * 0.01;
        DecimalFormat df =new DecimalFormat("0.0");
        result.FatWeight = df.format(fatweight);
        result.BoneWeight=cursor.getDouble(cursor.getColumnIndex(BONE_WEIGHT_COLUMN));
        result.BodyAge=cursor.getDouble(cursor.getColumnIndex(BODY_AGE_COLUMN));
        result.InsideFat=cursor.getDouble(cursor.getColumnIndex(INSIDE_FAT_COLUMN));
        result.MuscleWeight=cursor.getDouble(cursor.getColumnIndex(MUSCLE_WEIGHT_COLUMN));
        result.BodyWater=cursor.getDouble(cursor.getColumnIndex(BODY_WATER_COLUMN));
        result.Metabolism=cursor.getDouble(cursor.getColumnIndex(METABOLISM_COLUMN));
        result.Photo=cursor.getString(cursor.getColumnIndex(PHOTO_COLUMN));
        result.UploadStatus= cursor.getString(cursor.getColumnIndex(UPLOAD_STATUS_COLUMN));
        result.Status=cursor.getString(cursor.getColumnIndex(STATUS_COLUMN));
        result.Memo=cursor.getString(cursor.getColumnIndex(MEMO_COLUMN));
        result.CalculateMetabolism=cursor.getString(cursor.getColumnIndex(CALCULATE_METABOLISM_COLUMN));
        result.CalculateExpenditure=cursor.getString(cursor.getColumnIndex(CALCULATE_EXPENDITURE_COLUMN));


        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE (STATUS='1') ", null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }


    //更新為已上傳
    public boolean setUpload(String recordUid){

        ContentValues cv = new ContentValues();
        cv.put(UPLOAD_STATUS_COLUMN,Config.UPLOAD_STATUS.NONE.toString());
        String whereClause = RECORD_UID_COLUMN + "=? ";
        String[] whereArgs = new String[] {
                recordUid
        };

        return db.update(TABLE_NAME,cv,whereClause,whereArgs)>0;
    }

    //取得待上傳的資料，每次只處理5筆避免時間過久
    /*
    public List<RecordModel> getUploadList(Context context) {
        List<RecordModel> result = new ArrayList<RecordModel>();

        String whereClause = " UPLOAD = ? ";
        String[] whereArgs = new String[] {
                "0"
        };
        Cursor cursor = db.query(
                TABLE_NAME, null, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {

            RecordModel r =getRecord(cursor);
            //給server辨識用
            r.setEmail(UserDAO.getUserEmail(context));
            r.setUid(UserDAO.getUserUid(context));
            result.add(r);
        }

        cursor.close();
        return result;
    }

*/
    //region "試算分析"




    //取得最後七天的平均值
    public List<RecordAvgModel> getLastSevenDaysAvg(){

        RecordAvgModel item ;
        List<RecordAvgModel> result = new ArrayList<RecordAvgModel>();
        String sql ="select date(CREATE_TIME) AS CREATE_TIME,avg(WEIGHT) AS WEIGHT,avg(FAT_RATE) AS FAT_RATE,(avg(WEIGHT)*(avg(FAT_RATE)*0.01)) AS FAT_WEIGHT FROM RECORD" +
                "  GROUP BY date(CREATE_TIME) HAVING date(CREATE_TIME)<>'' AND (STATUS='1' OR STATUS='0') ORDER BY date(CREATE_TIME) DESC LIMIT 7";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            item=new RecordAvgModel();
            item.setCreateTime(cursor.getString(cursor.getColumnIndex("CREATE_TIME")));
            item.setWeight(cursor.getDouble(cursor.getColumnIndex("WEIGHT")));
            item.setFatRate(cursor.getDouble(cursor.getColumnIndex("FAT_RATE")));
            item.setFatweight(cursor.getDouble(cursor.getColumnIndex("FAT_WEIGHT")));
             result.add(item);
        }


        return result;
    }


    //取得最後七天的總平均值
    public RecordAvgModel getLastSevenDaysTotalAvg(){

        RecordAvgModel item =null;
        RecordAvgModel result  =null;
        List<RecordAvgModel> l =getLastSevenDaysAvg();
        double sumweight=0;
        double sumfatrate=0;
        double sumfatweight=0;

        if(l.size()>0){

            for(int i =0;i<l.size();i++){
                item = l.get(i);
                sumweight+= item.getWeight();
                sumfatrate+= item.getFatRate();
                sumfatweight+=item.getFatweight();
            }

            result =new RecordAvgModel();
            result.setCreateTime("最近七天總平均");
            DecimalFormat df=new DecimalFormat("#.##");

            result.setWeight(Double.valueOf( df.format(sumweight/l.size())));
            result.setFatRate(Double.valueOf( df.format(sumfatrate/l.size())));
            result.setFatweight(Double.valueOf( df.format(sumfatweight/l.size())));

        }

        return result;
    }

    //取得最後52週每週平均值
    public List<RecordAvgWeekModel> getWeekAvg(){
        RecordAvgWeekModel item ;
        List<RecordAvgWeekModel> result = new ArrayList<RecordAvgWeekModel>();
        String sql ="SELECT STRFTIME('%Y-%m',CREATE_TIME) AS YM,(STRFTIME('%w',CREATE_TIME)%4)+1 AS WEEK_OF_MONTH,AVG(WEIGHT) AS AVG_WEIGHT,AVG(FAT_RATE) AS AVG_FAT_RATE,(AVG(WEIGHT)*(AVG(FAT_RATE)*0.01)) AS AVG_FAT_WEIGHT" +
                "  FROM  RECORD GROUP BY STRFTIME('%Y-%m',CREATE_TIME),STRFTIME('%w',CREATE_TIME)%4"+
                " HAVING (STATUS='1' OR STATUS='0') ORDER BY STRFTIME('%Y-%m',CREATE_TIME) ,(STRFTIME('%w',CREATE_TIME) %4) LIMIT 52";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            item=new RecordAvgWeekModel();
            item.setYm(cursor.getString(cursor.getColumnIndex("YM")));
            item.setWeekofmonth(cursor.getString(cursor.getColumnIndex("WEEK_OF_MONTH")));
            item.setAvgweight(cursor.getDouble(cursor.getColumnIndex("AVG_WEIGHT")));
            item.setAvgfatrate(cursor.getDouble(cursor.getColumnIndex("AVG_FAT_RATE")));
            item.setAvgfatweight(cursor.getDouble(cursor.getColumnIndex("AVG_FAT_WEIGHT")));
            result.add(item);
        }


        return result;
    }

    //取得最後12個月的月平均值
    public List<RecordAvgMonthModel> getMonthAvg(){

        RecordAvgMonthModel item ;
        List<RecordAvgMonthModel> result = new ArrayList<RecordAvgMonthModel>();
        String sql ="SELECT STRFTIME('%Y-%m',CREATE_TIME) AS YM,AVG(WEIGHT) AS AVG_WEIGHT,AVG(FAT_RATE) AS AVG_FAT_RATE,(AVG(WEIGHT)*(AVG(FAT_RATE)*0.01)) AS AVG_FAT_WEIGHT " +
                "  FROM  RECORD GROUP BY STRFTIME('%Y-%m',CREATE_TIME)"+
                "  HAVING (STATUS='1' OR STATUS='0') ORDER BY STRFTIME('%Y-%m',CREATE_TIME)  LIMIT 12";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            item=new RecordAvgMonthModel();
            item.setYm(cursor.getString(cursor.getColumnIndex("YM")));
            item.setAvgweight(cursor.getDouble(cursor.getColumnIndex("AVG_WEIGHT")));
            item.setAvgfatrate(cursor.getDouble(cursor.getColumnIndex("AVG_FAT_RATE")));
            item.setAvgfatweight(cursor.getDouble(cursor.getColumnIndex("AVG_FAT_WEIGHT")));
            result.add(item);
        }
        return result;
    }

    //end region


    //region 自訂函式

    //取得計算後的基礎代謝率
    public String getMetabolism(String weight){

        SettingDAO sd =new SettingDAO(context);
        String  result ="";

        if(!weight.equals("") && !sd.getSex().equals("") && !sd.getAge().equals("") && !sd.getHeight().equals("")){
            try{
                String sx = sd.getSex();
                double wt = Double.valueOf(weight);
                double ag = Double.valueOf(sd.getAge());
                double ht = Double.valueOf(sd.getHeight());
                //要乘於活動率，才是基礎代謝率
                double co = Double.valueOf(sd.getCoefficient());

                //DecimalFormat df = new DecimalFormat("#0.0");
                DecimalFormat df = new DecimalFormat("0");


                double subtotal=0;
                if(sx.equals("M")){
                    subtotal= (13.7 * wt)+(5.0 * ht)-(6.8 * ag) + 66;
                    result=df.format(subtotal * co);
                }
                else if(sx.equals("F")){
                    subtotal= (9.6 * wt)+(1.8 * ht)-(4.7 * ag)+655;
                    result= df.format(subtotal * co);
                }


            }
            catch (Exception ex){
                Log.d(TAG,ex.toString());
                LogDAO.LogError(context,TAG,ex);

            }
        }


        return result;


    }

    //取得最低熱量
    public String getExpenditure(String weight){

        SettingDAO sd =new SettingDAO(context);
        String  result ="";

        if(!weight.equals("") && !sd.getSex().equals("") && !sd.getAge().equals("") && !sd.getHeight().equals("")){
            try{
                String sx = sd.getSex();
                double wt = Double.valueOf(weight);
                double ag = Double.valueOf(sd.getAge());
                double ht = Double.valueOf(sd.getHeight());
                //DecimalFormat df = new DecimalFormat("#0.0");
                DecimalFormat df = new DecimalFormat("0");


                double subtotal=0;
                if(sx.equals("M")){
                    subtotal= (10 * wt) +(6.25 * ht) - (5 * ag) + 5;
                    result=df.format(subtotal);
                }
                else if(sx.equals("F")){
                    subtotal= (10 * wt) +(6.25 * ht) - (5 * ag) - 161;
                    result= df.format(subtotal);
                }


            }
            catch (Exception ex){
                Log.d(TAG,ex.toString());
                LogDAO.LogError(context,TAG,ex);

            }
        }


            return result;

    }


    //取得脂肪重量
    public String getFatWeight(double weight,double fatrate ){

        DecimalFormat df = new DecimalFormat("0.##");
        double subtotal = weight * (fatrate * 0.01);
        String  result = df.format(subtotal);
        return result;

    }

    //endregion
}
