package com.oli365.nc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.oli365.nc.controller.DbHelper;
import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.controller.SettingDAO;
import com.oli365.nc.controller.Utility;

import java.io.InputStream;


/**
 * Created by alvinlin on 2015/10/28.
 */
public class ActivityRecordMain extends AppCompatActivity
        {

    private static final String TAG=ActivityRecordMain.class.getName();
    private ListView lv;
    private AdapterRecordCursor itemAdapter;
    private long  _id ;
    private final int PICK_IMAGE = 101;
    private final int ADD_RECORD=1;
    //private final int TAKE_PICTURE=102;

    private CallbackManager callbackManager;
    private LoginManager loginManager;



    private String LEVEL;

    private enum TIME_PERIOD{
        ALL,TODAY,MORNING,AFTERNOON,NIEGHT,DAWN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_main);
        SettingDAO sd =new SettingDAO(this);
        LEVEL = sd.getLevel();
        if(LEVEL=="N"){
            bindNData(TIME_PERIOD.ALL);
        }
        else if(LEVEL=="H"){
            bindHData(TIME_PERIOD.ALL);
        }

    }



    //region 一般指數

            //依照時間區間載入資料（一般指數）
            private void bindNData(TIME_PERIOD period){

                String queryString = "" ;

                switch (period){
                    case TODAY:
                        queryString="SELECT * FROM RECORD  WHERE date('now') = date(CREATE_TIME) AND (STATUS='1' OR STATUS='0') ";
                        break;
                    case MORNING:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=6 AND cast(strftime('%H',CREATE_TIME) as integer)<12 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case AFTERNOON:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=12 AND cast(strftime('%H',CREATE_TIME) as integer)<18 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case NIEGHT:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=18 AND cast(strftime('%H',CREATE_TIME) as integer) <=23 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case DAWN:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=0 AND cast(strftime('%H',CREATE_TIME) as integer)<6 AND (STATUS='1' OR STATUS='0')";
                        break;
                    default:
                        queryString="SELECT * FROM RECORD WHERE (STATUS='1' OR STATUS='0')";
                        break;
                }

                queryString += "  ORDER BY CREATE_TIME DESC";
                SQLiteDatabase db = DbHelper.getDatabase(this);
                Cursor todoCursor =db.rawQuery(queryString, null);


                lv  =(ListView)findViewById(R.id.recordList);
                itemAdapter =new AdapterRecordCursor(this,todoCursor);
                lv.setAdapter(itemAdapter);




            }

    //endregion



    //region 七大指數程式


            //依照時間區間載入資料（七大指數）
            private void bindHData(TIME_PERIOD period){

                String queryString = "" ;

                switch (period){
                    case TODAY:
                        queryString="SELECT * FROM RECORD  WHERE date('now') = date(CREATE_TIME) AND (STATUS='1' OR STATUS='0') ";
                        break;
                    case MORNING:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=6 AND cast(strftime('%H',CREATE_TIME) as integer)<12 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case AFTERNOON:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=12 AND cast(strftime('%H',CREATE_TIME) as integer)<18 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case NIEGHT:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=18 AND cast(strftime('%H',CREATE_TIME) as integer) <=23 AND (STATUS='1' OR STATUS='0')";
                        break;
                    case DAWN:
                        queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=0 AND cast(strftime('%H',CREATE_TIME) as integer)<6 AND (STATUS='1' OR STATUS='0')";
                        break;
                    default:
                        queryString="SELECT * FROM RECORD WHERE (STATUS='1' OR STATUS='0')";
                        break;
                }

                queryString += "  ORDER BY CREATE_TIME DESC";
                SQLiteDatabase db = DbHelper.getDatabase(this);
                Cursor todoCursor =db.rawQuery(queryString, null);


                lv  =(ListView)findViewById(R.id.recordList);
                itemAdapter =new AdapterRecordCursor(this,todoCursor);
                lv.setAdapter(itemAdapter);


        /*
        lv.setLongClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String itemValue = (String) lv.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"itemid=" + lv.getSelectedItemId() + ",itemvalue=" + itemValue,
                        Toast.LENGTH_LONG).show();

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"long click itemid=" + lv.getSelectedItemId() ,
                        Toast.LENGTH_LONG).show();
                return true ;
            }
        });


*/

            }


    //endregion







    //region facebook

            private void sharePhotoToFacebook(){

                Log.d(TAG,"sharePhotoToFacebook function start");
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .setCaption("test share pic")
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ShareApi.share(content, null);

            }

            //endregion


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



              if(LEVEL=="N"){

              }
              else if(LEVEL=="H"){

                  switch (item.getItemId()){
                      case R.id.btnAdd:
                          Intent intent =new Intent(this,ActivityRecordAdd.class);
                          //startActivity(intent);
                          startActivityForResult(intent, ADD_RECORD);
                          return true;
                      case R.id.btnView_Today:
                          bindHData(TIME_PERIOD.TODAY);
                          return true;
                      case R.id.btnView_1:
                          bindHData(TIME_PERIOD.MORNING);
                          return true;
                      case R.id.btnView_2:
                          bindHData(TIME_PERIOD.AFTERNOON);
                          return true;
                      case R.id.btnView_3:
                          bindHData(TIME_PERIOD.NIEGHT);
                          return true;
                      case R.id.btnView_4:
                          bindHData(TIME_PERIOD.DAWN);
                          return true;
                      case R.id.btnView_All:
                          bindHData(TIME_PERIOD.ALL);
                          return true;
                      default:
                          return super.onOptionsItemSelected(item);
                  }
                  //return super.onOptionsItemSelected(item);
              }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if(LEVEL=="N"){

            }
            else if(LEVEL =="H"){
                if(requestCode == ADD_RECORD &&  resultCode==RESULT_OK){
                    bindHData(TIME_PERIOD.ALL);
                }

                if (requestCode == PICK_IMAGE && data != null) {

                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap
                }
                //回呼facebook
                //Log.d(TAG,"CALL BACK FACEBOOK");
                //callbackManager.onActivityResult(requestCode, resultCode, data);
                //String result = "requestCode=" + String.valueOf(requestCode) + "resultCode" + String.valueOf(resultCode);
                //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }



        }
        catch(Exception ex){

        }
        /*
        switch(resultCode){
            case RESULT_OK:
                //Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
                bindData();
                break;
            case RESULT_CANCELED:
                //Toast.makeText(this,"cancel",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }


        bindData();
        */
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_record, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }




    private AlertDialog AskOption()
    {

        if(LEVEL =="N"){


        }

        else if(LEVEL=="H"){
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                    //set message, title, and icon
                    //.setTitle("Delete")
                    .setMessage("確定要刪除？")
                    // .setIcon(R.drawable.delete)

                    .setPositiveButton("刪除", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //       android.os.Debug.waitForDebugger();
                            //上傳刪除記錄，接著才刪除本地端內容
                            RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                            rd.delete(_id);
                            Utility.showMessage(ActivityRecordMain.this,"刪除成功");
                            bindHData(TIME_PERIOD.ALL);
                        /*
                        NetworkDAO nd =new NetworkDAO(ActivityRecordMain.this);
                        RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                        Log.d(TAG,"get id =" + _id);
                        RecordModel rm = rd.get(_id);

                        Log.d(TAG,"rm != null:" + String.valueOf(rm!=null));

                        if(rm!=null){
                            //開始上傳刪除記錄
                            rm.UploadStatus= "D";
                            rm.Status="0";

                            try{
                                nd.uploadJson("user/record/update", rm, new NetworkDAO.downloadJosn() {
                                    @Override
                                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {

                                       // android.os.Debug.waitForDebugger();
                                        Log.d(TAG,"Delete Record return result=" + result);

                                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                                            //真正刪除本地資料
                                            RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                                            rd.delete(_id);
                                            Utility.showMessage(ActivityRecordMain.this,"刪除成功");
                                            bindData();
                                        }
                                        else{
                                            Utility.showMessage(ActivityRecordMain.this,"上傳刪除失敗："+ result);
                                        }
                                    }
                                });
                            }
                            catch (Exception ex){
                                Log.d(TAG,ex.toString());
                                LogDAO.LogError(ActivityRecordMain.this,TAG,ex);
                            }

                        }

*/
                            dialog.dismiss();

                        /*
                        if (dao.deleteR(_id)) {
                            msg ="刪除成功";

                            Gson gson = new Gson();
                            String jstr = gson.toJson(rm);
                            try{
                                NetworkDAO network =new NetworkDAO(ActivityRecordMain.this);
                                //network.uploadJson("/users/record/json",jstr);
                            }
                           catch (Exception ex){
                               LogDAO.LogError(ActivityRecordMain.this,TAG,ex);
                           }
                            //startUploadService();
                        }
                        else{
                            msg ="刪除失敗";
                        }
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bindData();
                        */
                        }

                    })



                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(),"choice cancel",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    })
                    .create();
            return myQuittingDialogBox;
        }


        return null;

    }

/*
    //啓動上傳服務
    private void startUploadService(){
        Intent mServiceIntent =new Intent(ActivityRecordMain.this,UploadService.class);
        Log.i(TAG, UserDAO.getUserUid(this));
        ActivityRecordMain.this.startService(mServiceIntent);
    }
*/



}
