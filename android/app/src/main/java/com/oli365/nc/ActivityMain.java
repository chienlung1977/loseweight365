package com.oli365.nc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.oli365.nc.controller.DebugDAO;
import com.oli365.nc.controller.LogDAO;
import com.oli365.nc.controller.SettingDAO;
import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.controller.UserUtility;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.host.SyncHostDataService;
import com.oli365.nc.host.SyncHostDataTask;



public class ActivityMain extends AppCompatActivity {

  //  private ShareActionProvider mShareActionProvider;
    private static final String TAG =ActivityMain.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //檢查登入狀態
        try{

            //檢查是否要啟用服務
            checkService();
            //初始畫面資料
            initData();
            //checkLogin();
        }
        catch (Exception ex){
            LogDAO.LogError(this,TAG,ex);
        }


        if(DebugDAO.IsDebugMode()==false){
            initCopyDb();
        }


    }


        private void initCopyDb(){

            Button btn_copy_db =(Button)findViewById(R.id.btn_copy_db);
            btn_copy_db.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //複製db
                    try{
                        if(DebugDAO.CopyDbFile2Sd(view.getContext()))
                        {
                            Utility.showMessage(view.getContext(),"複製成功");
                        }
                       else{
                            Utility.showMessage(view.getContext(),"複製失敗");
                        }
                    }
                    catch (Exception ex){
                        Utility.showMessage(view.getContext(),"複製失敗：" + ex.getMessage());
                    }

                }
            });

            if(DebugDAO.IsDebugMode()==false){
                btn_copy_db.setVisibility(View.GONE);
            }

        }




    //region 服務啟用

    private void checkService(){

        SettingDAO sd =new SettingDAO(this);
        Log.d(TAG,"host_connection=" + sd.getHostConnection());

        if(sd.getHostConnection()==true ){
            //todo 此處啟用同步服務
            //呼叫同步工作
           // SyncHostDataTask task =new SyncHostDataTask(this,uid);
           // task.execute();
        }


    }


    //endregion

    //檢查登入
    private void checkLogin(){

        //檢查是否已和主機連線
        Log.d(TAG,"UserDAO.isSysLogin(this)=" + UserDAO.isSysLogin(this));

        if(UserDAO.isSysLogin(this)==true){

            String uid  = UserDAO.getUserUid(this);
            String updatedate = UserDAO.getUpdateDate(this);
            //檢查是否要更新使用者的資料
            UserUtility uu =new UserUtility(this);
            if(uu.isUpdateLocalUserData(uid,updatedate)==true){
                //呼叫同步工作
                SyncHostDataTask task =new SyncHostDataTask(this,uid);
                task.execute();
            }



            //startUploadService();

        }
        else{
            showLogin();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //檢查是否尚未登入，若是則顯示重新登入
        /*
        if(UserDAO.isSysLogin(this)==false){
            showLogin();
        }
        */
    }



    //顯示登入畫面
    private void showLogin(){
        Intent intent =new Intent(this,ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initData(){

        // 檢查是否已有設定資料或無則跳出設定頁面
        /*
        SettingDAO sd =new SettingDAO(this);
        if(sd.getFirstSetting()==false){
            openSettings();
        }


        TextView txt_main_remaining_weight =(TextView) findViewById(R.id.txt_main_remaining_weight);

        //SettingModel sm = sd.getSetting();
        RecordDAO rd =new RecordDAO(this);
        RecordModel rm = rd.getLatestRecord();
        //如果還沒有任何記錄就先隱藏
        if(rm==null){
            txt_main_remaining_weight.setVisibility(View.GONE);
            return ;
        }
*/
/*
        Double targetWeight =Double.valueOf(rm.Weight)-Double.valueOf(sm.TargetWeight);
        DecimalFormat df = new DecimalFormat("##.0");
        String msg ="尚餘:" + String.valueOf(df.format(targetWeight)) + "KG";
        txt_main_remaining_weight.setText(msg);
        */
        /*
        String msg ="距離目標" + u.getTargetWeight() + "公斤\n已減" + u.getConsumWeight() + "公斤"
                + "\n距離目標尚餘" + String.valueOf(df.format(loseweight)) + "公斤";
        tv_using_kg.setText(msg);

        TextView tv_target_cal = (TextView)findViewById(R.id.tv_target_cal);
        msg ="目標卡路里尚餘" + u.getTargetCalory() + "卡";
        tv_target_cal.setText(msg);

        TextView tv_daily_calory =(TextView)findViewById(R.id.tv_daily_calory);
        msg ="今日可用卡路里" + u.getDailyCalory() + "卡";
        tv_daily_calory.setText(msg);
        */
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
       // mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //Intent intent;

        switch (item.getItemId()){
            case R.id.action_mymenu_settings:
                openSettings();
                return true;
            //卡路里記錄

            case R.id.action_mymenu_calory:
               // openCalory();
                return true;

            case R.id.action_mymenu_weight:
                openBodyRecord();
                return true;
                /*
            case R.id.action_mymenu_status:

                return true;
*/
                /*
            case R.id.action_mymenu_logout:
                UserDAO.Logout(this);
                showLogin();
                return true;
*/
            case R.id.action_mymenu_plan:
                //use fragment show
                //LoseweightPlanFragment lf =(LoseweightPlanFragment) getFragmentManager().findFragmentById(R.id.)
                startActivity(new Intent(this,ActivityPlan.class));
                return true;
/*
            case R.id.contact_us:

                ActionDAO dao =new ActionDAO(this);
                dao.openEmail("問題詢問","請留下您要詢問的內容：");

                dao.Call(getString(R.string.sys_contact));


                return true;
                */
            //主機同步
/*
            case R.id.action_mymenu_sync:

                SyncHostDataTask task =new SyncHostDataTask(this,UserDAO.getUserUid(this));
                task.execute();
                return true;
*/
            //加入好友
            /*
            case R.id.action_friend_join:


                return true;
*/
            //使用前後
      /*
            case R.id.action_mymenu_picture:
                startActivity(new Intent(this,BodyPicture.class));
               return true;

            case R.id.action_mymenu_test:

                FragmentTransaction ft =getFragmentManager().beginTransaction();
                FragmentSettings sf =new FragmentSettings();
                ft.add(R.id.test_fragment,sf);
                ft.commit();
                return true;
                */
        }

        return super.onOptionsItemSelected(item);
    }




    public void openBodyRecord(){

        Intent i =new Intent(this,ActivityRecordMain.class);
        startActivity(i);
    }

    public void openCustomer(){

       // inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void openSettings(){


        Intent i =new Intent(this,ActivitySettings.class);
        startActivity(i);
   /*
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        FragmentSettings mPrefsFragment = new FragmentSettings();
       // mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.replace(R.id.content_main, mPrefsFragment);
        mFragmentTransaction.commit();

        */
    }

    public void openBase(){
/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentSettings fragment1 = new FragmentSettings();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    public void openCalory(){

        Intent intent =new Intent(this,ActivityCaloryMain.class);
        startActivity(intent);
    }


    /*
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
*/

    @Override
    protected void onStart() {
        super.onStart();
        /*
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if (msg!=null)
            Log.d(TAG, "FCM msg:"+msg);
            */
        //檢查是否尚未登入，若是則顯示重新登入
        /*
        if(UserDAO.isSysLogin(this)==false){
            showLogin();
        }
        */
    }

    //region 固定函式



    //啓動同步服務
    private void startUploadService(){

        Intent mServiceIntent =new Intent(ActivityMain.this,SyncHostDataService.class);
        mServiceIntent.putExtra("UID",UserDAO.getUserUid(this));
        //mServiceIntent.putExtra("EMAIL",UserDAO.getUserEmail(this));

        ActivityMain.this.startService(mServiceIntent);
    }


    //endregion



}
