package com.oli365.nc.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.PhraseModel;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=22;
    public static final String DATABASE_NAME="nc.db";
    public static SQLiteDatabase database;
    Context context ;



    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }


    public static SQLiteDatabase getDatabase(Context context){

        if(database!=null && database.isDbLockedByCurrentThread()){
            database.close();
        }

        if (database == null || !database.isOpen()) {
            database = new DbHelper(context).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //使用者表格
        db.execSQL(UserDataDAO.CREATE_TABLE);

        db.execSQL(RecordDAO.CREATE_TABLE);

    //    db.execSQL(CaloryDAO.CREATE_TABLE);

        db.execSQL(PhraseDAO.CREATE_TABLE);



        //初始資料
       // PhraseDAO pd =new PhraseDAO(context);
       // pd.insertInitData();
       // pd.close();

       // db.execSQL(FoodDAO.CREATE_TABLE);
       // db.execSQL(FoodTypeDAO.CREATE_TABLE);

       // db.execSQL(FoodPersonalDAO.CREATE_TABLE);
       // db.execSQL(CustomerController.SQL_CREATE_ENTRIES);
        //db.execSQL(BodyRecordController.SQL_CREATE_ENTRIES);
    }


    private void initPhrase(){
        //新增初始資料
        PhraseModel pm =new PhraseModel();
        PhraseDAO pd =new PhraseDAO(context);

        pm.PhraseUid= Utility.getUUID();
        pm.CreateDate= Utility.getToday();
        pm.UserUid=UserDAO.getUserUid(context);
        pm.PhraseType= Config.PHRASE_TYPE.PRIVATE;
        pm.Memo ="起床";
        pm.UploadStatus= Config.UPLOAD_STATUS.NONE;
        pm.Status="1";
        pm.Source= Config.SOURCE_TYPE.CLIENT;


        pd.insert(pm);

        pm.PhraseUid=Utility.getUUID();
        pm.Memo="吃飯前";
        pd.insert(pm);
        pm.PhraseUid=Utility.getUUID();
        pm.Memo="吃飯後";
        pd.insert(pm);
        pm.PhraseUid=Utility.getUUID();
        pm.Memo="運動前";
        pd.insert(pm);
        pm.PhraseUid=Utility.getUUID();
        pm.Memo="運動後";
        pd.insert(pm);

        pm.PhraseUid=Utility.getUUID();
        pm.Memo="睡前";
        pd.insert(pm);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(UserDataDAO.DROP_TABLE);
        db.execSQL(UserDataDAO.CREATE_TABLE);

        db.execSQL(RecordDAO.DROP_TABLE);
        db.execSQL(RecordDAO.CREATE_TABLE);

        db.execSQL(PhraseDAO.DROP_TABLE);
        db.execSQL(PhraseDAO.CREATE_TABLE);
        /*
        if(oldVersion==16){
            db.execSQL(UserDataDAO.CREATE_TABLE);
            db.execSQL(FoodTypeDAO.UPGRADE_TABLE17);
            db.execSQL(FoodDAO.DROP_FOOD_TABLE);
            db.execSQL(FoodDAO.CREATE_TABLE);
        }

        if(oldVersion==17){
            db.execSQL(FoodPersonalDAO.CREATE_TABLE);
            db.execSQL(RecordDAO.DROP_TABLE);
            db.execSQL(RecordDAO.CREATE_TABLE);
        }

        if(oldVersion==18){
            db.execSQL(PhraseDAO.DROP_TABLE);
            db.execSQL(PhraseDAO.CREATE_TABLE);
           //initPhrase();
        }
*/
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
