package com.oli365.nc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.text.DecimalFormat;



public class MainActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private static final String TAG =MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //String login = Utility.getKeyValue(this,"IS_LOGIN");
       // Toast.makeText(this,"MAIN isLogin=" + Utility.getKeyValue(this,"IS_LOGIN") , Toast.LENGTH_SHORT).show();

        //測試訂閱功能
        Button btnSubscription =(Button)findViewById(R.id.btnSubscription);
        btnSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FirebaseMessaging.getInstance().subscribeToTopic("news");
            }
        });


        checkLogin();

    }

    private void checkLogin(){
        //檢查是否已和主機連線
        if(Utility.isSysLogin(this)==true){
            initData();
        }
        else{
            //initData();
            showLogin();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkLogin();
    }



    private void showLogin(){
        //Toast.makeText(this,"login=" + login , Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

    private void initData(){

        // 檢查是否已有設定資料或無則跳出設定頁面
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isSetting = sp.getBoolean("isPreferenceSetting",false);
        if(isSetting==false){
            openSettings();
        }


        UserUtility u =new UserUtility(this);
        TextView tv_using_kg =(TextView) findViewById(R.id.tv_using_kg);

        Double loseweight=Double.valueOf(u.getWeight())-Double.valueOf(u.getTargetWeight());
        DecimalFormat df = new DecimalFormat("##.0");
        String msg ="距離目標" + u.getTargetWeight() + "公斤\n已減" + u.getConsumWeight() + "公斤"
                + "\n距離目標尚餘" + String.valueOf(df.format(loseweight)) + "公斤";
        tv_using_kg.setText(msg);

        TextView tv_target_cal = (TextView)findViewById(R.id.tv_target_cal);
        msg ="目標卡路里尚餘" + u.getTargetCalory() + "卡";
        tv_target_cal.setText(msg);

        TextView tv_daily_calory =(TextView)findViewById(R.id.tv_daily_calory);
        msg ="今日可用卡路里" + u.getDailyCalory() + "卡";
        tv_daily_calory.setText(msg);
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
            case R.id.action_mymenu_calory:
                openCalory();
                return true;
            case R.id.action_mymenu_weight:
                openBodyRecord();
                return true;
            case R.id.action_mymenu_logout:
                Utility.Logout(this);
                showLogin();
                return true;

            case R.id.action_mymenu_plan:
                //use fragment show
                //LoseweightPlanFragment lf =(LoseweightPlanFragment) getFragmentManager().findFragmentById(R.id.)
                startActivity(new Intent(this,LoseweightPlan.class));
                return true;

            case R.id.contact_us:
                String permission = "android.permission.CALL_PHONE";
                int res =this.checkCallingOrSelfPermission(permission);
                if(res == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0980357972"));
                    startActivity(intent);
                }
                else{
                    Utility.showMessage(this,"沒有權限撥打電話");
                }

                return true;

            case R.id.action_mymenu_picture:
                startActivity(new Intent(this,BodyPicture.class));
               return true;
/*
            case R.id.action_mymenu_test:

                FragmentTransaction ft =getFragmentManager().beginTransaction();
                SettingsFragment sf =new SettingsFragment();
                ft.add(R.id.test_fragment,sf);
                ft.commit();
                return true;
                */
        }

        return super.onOptionsItemSelected(item);
    }


    public void openAboutUs(){

        Intent i =new Intent(this,AboutUs.class);
        startActivity(i);

    }


    public void openBodyRecord(){

        Intent i =new Intent(this,BodyRecordMain.class);
        startActivity(i);
    }

    public void openCustomer(){

       // inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void openSettings(){

        Intent i =new Intent(this,SettingsActivity.class);
        startActivity(i);

/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment1 = new SettingsFragment();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        //fragmentTransaction.replace(android.R.id.,fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    public void openBase(){
/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment1 = new SettingsFragment();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    public void openCalory(){

        Intent intent =new Intent(this,CaloryMain.class);
        startActivity(intent);
    }


    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if (msg!=null)
            Log.d(TAG, "FCM msg:"+msg);
    }

}
