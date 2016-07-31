package com.oli365.nc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alvinlin on 2016/7/8.
 */
public class UserUtility {

    SharedPreferences sp;
    Context context ;

    public UserUtility(Context context){

        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(this.context);

    }

    //更新體重資訊
    public void setWeight(String weight){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_base_weight",weight);
        editor.apply();
    }

    //更新新陳代謝率
    public void setMetabolism(String metabolism){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_metabolism",metabolism);
        editor.apply();
    }

    //更新體脂肪
    public void setBodyFat(String bodyfat){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_body_fat",bodyfat);
        editor.apply();
    }

    //目前現有體重
    public String getWeight(){

            return sp.getString("sys_base_weight","0");
    }

    public String getMetabolism(){
        return sp.getString("sys_metabolism","0");
    }

    public String getSex(){
        return sp.getString("sys_sex","");
    }

    //活動係數
    public String getCoefficient(){
        return sp.getString("sys_base_day_life","");
    }

    //體脂肪
    public String getBodyFat(){
        return sp.getString("sys_body_fat","0");
    }

    //目標體重
    public String getTargetWeight(){
        return sp.getString("sys_target_weight","0");
    }

    //初始體重
    public String getInitWegiht(){
        return sp.getString("sys_init_weight","0");
    }

    public void setInitWeight(String initweight){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_init_weight",initweight);
        editor.apply();

    }

    //取目標日期
    public String getTargetDate(){

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
        String targetDate= sp.getString("sys_target_weight_date","");

        try{
            if(!targetDate.equals("")){
                Date d = sdf.parse(sp.getString("sys_target_weight_date",""));
                targetDate =sdf.format(d);
            }

        }
        catch(Exception ex){
            Log.e("UserUtility Tag",ex.getMessage());
        }

        return targetDate;
    }


    //目前脂肪重量

    public void setFatWeight(){

        Double fatweight = Double.valueOf(getWeight()) * (Double.valueOf(getBodyFat()) * 0.01);
        DecimalFormat df = new DecimalFormat("##.00");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_fat_weight",String.valueOf(Double.parseDouble(df.format(fatweight))));
        editor.apply();

    }

    public String getFatWeight(){
        //if(sp.getString("sys_fat_weight","0")=="0"){
         //   setFatWeight();
       // }
        setFatWeight();
        return sp.getString("sys_fat_weight","0");
    }

    //已減去的體重
    public void setConsumWeight(){

        Double consumWeight =  Double.valueOf(getInitWegiht())- Double.valueOf(getWeight());
        DecimalFormat df = new DecimalFormat("##.0");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_consum_weight",String.valueOf(Double.parseDouble(df.format(consumWeight))));
        editor.apply();
    }

    public String getConsumWeight(){

        //if(sp.getString("sys_consum_weight","0")=="0"){
         //   setConsumWeight();
        //}
        setConsumWeight();
        return sp.getString("sys_consum_weight","0");
    }

    //每日總消耗卡路里(新陳代謝/性別變數*活動係數)

    public void setDailyCalory(){

        double sexrate =0;
        if(getSex().equals("M")){
            sexrate = 0.8;
        }
        else if(getSex().equals("F")){
            sexrate=0.9;
        }
        Double calory = Double.valueOf(getMetabolism()) / Double.valueOf(sexrate);
        Double dailyCalory = calory * Double.valueOf(getCoefficient());

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_daily_calory",String.valueOf(dailyCalory.intValue()));
        editor.apply();

    }

    public String getDailyCalory(){
       // if(sp.getString("sys_daily_calory","0")=="0"){
       //     setDailyCalory();
       // }
        setDailyCalory();
        return sp.getString("sys_daily_calory","0");
    }

    //每日可減去卡路里(每日總消耗卡路里減去新陳代謝率)

    public void setDailyConsumCalory(){
        Double dailyconsumcalory =Double.valueOf(getDailyCalory())-Double.valueOf(getMetabolism());
        //Utility.putKeyValue(this.context,"sys_daily_consum_calory",String.valueOf(dailyconsumcalory));
        //DecimalFormat df = new DecimalFormat("##");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_daily_consum_calory",String.valueOf(dailyconsumcalory.intValue()));
        editor.apply();

    }

    public String getDailyConsumCalory(){
       // if(sp.getString("sys_daily_consum_calory","0")=="0"){
       //     setDailyConsumCalory();
        //}
        setDailyConsumCalory();
        return sp.getString("sys_daily_consum_calory","0");
    }


    //======要減去的總卡路里=====
    //目前體重-目標體重=要減去體重
    //要減去體重/目前體重=比率
    //目前體重*體脂率=脂肪量
    //總脂肪量*比率=實際要減去脂肪*7700(要減去的總卡路里)

    public void setTargetCalory(){
        Double targetconsumweight = Double.valueOf(getWeight())- Double.valueOf(getTargetWeight());
        Double rate = Double.valueOf(targetconsumweight) / Double.valueOf(getWeight());
        Double targetconsumcalory = Double.valueOf(getFatWeight()) * rate * 7700;
        //Utility.putKeyValue(this.context,"sys_target_consum_calory",String.valueOf(targetconsumcalory));

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_target_consum_calory",String.valueOf(targetconsumcalory.intValue()));
        editor.apply();

    }

    public String getTargetCalory(){
       // if(sp.getString("sys_target_consum_calory","0")=="0"){
        //    setTargetCalory();
       // }
        setTargetCalory();
        return sp.getString("sys_target_consum_calory","0");
    }

    //依每日減少卡路里，目標達成預計花費天數

    public void setTargetDays(){
        Double targetdays = Math.ceil(Double.valueOf(getTargetCalory()) / Double.valueOf(getDailyConsumCalory())) ;
        //Utility.putKeyValue(this.context,"sys_target_days",String.valueOf(targetdays));
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_target_days",String.valueOf(targetdays.intValue()));
        editor.apply();
    }

    public String getTargetDays(){
       // if(sp.getString("sys_target_days","0")=="0"){
       //     setTargetDays();
       // }
        setTargetDays();
        return sp.getString("sys_target_days","0");
    }

    //計算的建議日期

    public void setSuggestionDate(){
        String suggestiondate =  Utility.getDate(Double.valueOf(getTargetDays()).intValue());
        //Utility.putKeyValue(this.context,"sys_suggestion_date",String.valueOf(suggestiondate));
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_suggestion_date",String.valueOf(suggestiondate));
        editor.apply();
    }

    public String getSuggestionDate(){
       // if(sp.getString("sys_suggestion_date","")==""){
        //    setSuggestionDate();
       // }
        setSuggestionDate();
        return sp.getString("sys_suggestion_date","");

    }


}
