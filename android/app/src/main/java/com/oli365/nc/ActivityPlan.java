package com.oli365.nc;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.controller.SettingDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.UserDataModel;

public class ActivityPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        //initToolbar();
        showData();
    }
    /*
    private void initToolbar(){
        //先將工具列隱藏不顯示
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

    }
*/

    private void showAll(){
        //載入預設的fragment 全部

        //fragment_plan_main

        FragmentManager fragmentMgr = getFragmentManager();
        FragmentPlanAverageAll fg =new FragmentPlanAverageAll();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
       /*
        Bundle b =new Bundle();
        //傳到calorynew頁面，才知道要抓哪一天的資料
        b.putString("date",date);
        fgClaoryNew.setArguments(b);
*/

      //  fragmentTrans.addToBackStack(null);
        fragmentTrans.replace(R.id.fragment_plan_main, fg);

        fragmentTrans.commit();
    }

    private void showLastseven(){
        FragmentManager fragmentMgr = getFragmentManager();
        FragmentPlanLastSevenDays fg =new FragmentPlanLastSevenDays();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.replace(R.id.fragment_plan_main, fg);
        fragmentTrans.commit();
    }

    private void showAverageweek(){
        FragmentManager fragmentMgr = getFragmentManager();
        FragmentPlanAverageWeek fg =new FragmentPlanAverageWeek();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.replace(R.id.fragment_plan_main, fg);
        fragmentTrans.commit();
    }

    private void showAveragemonth(){
        FragmentManager fragmentMgr = getFragmentManager();
        FragmentPlanAverageMonth fg =new FragmentPlanAverageMonth();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.replace(R.id.fragment_plan_main, fg);
        fragmentTrans.commit();
    }

    //顯示資料
    private void showData(){

       //必須先有第一次設定以及一筆記錄
        SettingDAO sd =new SettingDAO(this);
        RecordDAO rd =new RecordDAO(this);


        if(sd.getFirstSetting()==false){
            Utility.showMessage(this,"請先設定基本資料");
            return ;
        }
        if(rd.getCount()==0){
            Utility.showMessage(this,"請先新增一筆減重日誌記錄");
            return ;
        }

        UserDataModel sm = sd.getSetting();
        if(sm==null){
            Utility.showMessage(this,"取得初始資料失敗");
            return ;
        }

        //通過檢核
        showAll();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_plan, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.menu_plan_all:
                //全部分析
                showAll();
                return true;
            case R.id.menu_plan_seventimes:
                //最後七次/天的分析
                showLastseven();

                return true;
            case R.id.menu_plan_week:
                //週平均分析
                showAverageweek();
                return true;
            case R.id.menu_plan_month:
                //月平均分析
                showAveragemonth();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }


    }
