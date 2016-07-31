package com.oli365.nc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LoseweightPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loseweight_plan);




        /*
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
        */


        initData();
    }



    private void initData(){

        UserUtility uu =new UserUtility(this);
        String sex = uu.getSex();
        String metabolism= uu.getMetabolism();
        String coefficient =uu.getCoefficient();
        String bodyfat = uu.getBodyFat();
        String targetweight =uu.getTargetWeight();
        String baseweight = uu.getWeight();
        double sexrate =0;

        if(sex.equals("")){
            Utility.showMessage(this,"請先設定基本資料");
            return ;
        }

        if(metabolism.equals("")){
            Utility.showMessage(this,"請先新增一筆減重日誌記錄");
            return ;
        }

        /*
        if(sex.equals("M")){
            sexrate = 0.8;
        }
        else if(sex.equals("F")){
            sexrate=0.9;
        }

        Double calory = Double.valueOf(metabolism) / Double.valueOf(sexrate);
        Double totalCalory = calory * Double.valueOf(coefficient);

        //要減去的體重
        Double consumweight = Double.valueOf(baseweight)- Double.valueOf(targetweight);


        //每日可減卡路里
        Double dailyconsum =totalCalory-Double.valueOf(metabolism);
*/
        String msg="";
        //目前脂肪量
        //Double fatweight = Double.valueOf(baseweight) * (Double.valueOf(bodyfat) * 0.01);
        TextView fat_weight =(TextView)findViewById(R.id.fat_weight);
        //DecimalFormat df = new DecimalFormat("##.00");
        msg ="目前脂肪重量：" + uu.getFatWeight()  + "kg";
        fat_weight.setText(msg);

        TextView consum_weight =(TextView)findViewById(R.id.consum_weight);
        msg ="目標減去體重：" + uu.getConsumWeight()  + "kg";
        consum_weight.setText(msg);

        TextView daily_calory =(TextView)findViewById(R.id.daily_calory);
        msg ="每日總消耗卡路里：" + uu.getDailyCalory() + "卡";
        daily_calory.setText(msg);

        TextView daily_consum_calory = (TextView)findViewById(R.id.daily_consum_calory);
        msg ="每日可減去卡路里：" + uu.getDailyConsumCalory() + "卡";
        daily_consum_calory.setText(msg);

        //======要減去的總卡路里=====
        //目前體重-目標體重=要減去體重
        //要減去體重/目前體重=比率
        //目前體重*體脂率=脂肪量
        //總脂肪量*比率=實際要減去脂肪*7700(要減去的總卡路里)
      //  Double rate = consumweight / Double.valueOf(baseweight);
       // Double consumcalory = Double.valueOf(fatweight) * rate * 7700;
        //目標達成花費天數
      //  Double targetdays = Math.ceil(consumcalory / dailyconsum) ;

        TextView final_target_calory =(TextView)findViewById(R.id.final_target_calory);
        msg ="要減去總卡路里：" + uu.getTargetCalory() + "卡";
        final_target_calory.setText(msg);

        TextView forecast_days =(TextView)findViewById(R.id.forecast_days);
        msg ="目標預計天數：" +  uu.getTargetDays() + "天";
        forecast_days.setText(msg);


        TextView target_date = (TextView)findViewById(R.id.target_date);
        msg ="目前目標日期：" + uu.getTargetDate();
        target_date.setText(msg);

        TextView suggestion_date =(TextView)findViewById(R.id.suggestion_date);
        msg ="建議目標日期：" + uu.getSuggestionDate();
        suggestion_date.setText(msg);

    }

}
