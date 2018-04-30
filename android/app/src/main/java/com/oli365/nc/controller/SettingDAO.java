package com.oli365.nc.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oli365.nc.model.RecordModel;
import com.oli365.nc.model.UserDataModel;

/**
 * Created by alvinlin on 2016/11/10.
 */

public class SettingDAO {

    private static final String TAG=SettingDAO.class.getName();
    SharedPreferences sp;
    Context context ;



    public SettingDAO(Context context ){
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(this.context);
    }


    //取得體脂器類型 N 一般 H 七大指數
    public String getLevel(){

        return  sp.getString("sys_settings_system_level","N");

    }




    //region "封存"




    //設定app設定中的初始資料
    public boolean setSetting(){

        try{
            String uid = UserDAO.getUserUid(context);
            UserDataDAO udd =new UserDataDAO(context);
            UserDataModel udm = udd.getUser(uid);
            //將DB的資料複製到Setting中
            setSex(udm.Sex);
            setHeight(udm.Height);
            setBirthday(udm.Birthday);
            setAge(udm.Age);
            setCoefficient(udm.Coefficient);  //活動係數
            setTargetWeight(udm.TargetWeight);
            setTargetFatrate(udm.TargetFatrate);
            setTargetFatweight(udm.TargetFatweight);
            setInitWeight(udm.InitWeight);
            setInitFatrate(udm.InitFatrate);
            setInitFatweight(udm.InitFatweight);
            setWeight(udm.Weight);
            setFatrate(udm.BodyFatRate);

            Log.d(TAG,"udm.BodyFatWeight=" + udm.BodyFatWeight);
            setFatweight(udm.BodyFatWeight);
            setMetabolism(udm.Metabolism);  //基礎代率
            setExpenditure(udm.Expenditure.toString());
            //有初始化設定就不需要再跳第一次設定
            setFirstSetting();
            return true;
        }
        catch (Exception ex){
            LogDAO.LogError(context,TAG,ex);
            return false;
        }


    }


    //取得使用者的設定資料
    public UserDataModel getSetting(){

        UserDataDAO udd =new UserDataDAO(context);
        UserDataModel udm = udd.getUser(UserDAO.getUserUid(context));

        if(udm==null){
            udm=new UserDataModel();
        }

        udm.Sex=getSex();
        udm.Height=getHeight();
        udm.Birthday=getBirthday();
        udm.Age =getAge();
        udm.Coefficient=getCoefficient();
        udm.TargetWeight=getTargetWeight();
        udm.TargetFatrate=getTargetFatrate();
        udm.TargetFatweight=getTargetFatweight();
        udm.InitWeight=getInitWeight();
        udm.InitFatrate=getInitFatrate();
        udm.InitFatweight=getInitFatweight();
        udm.Weight=getWeight();
        udm.BodyFatRate=getFatrate();
        udm.BodyFatWeight=getFatweight();
        udm.Metabolism=getMetabolism();
        udm.Expenditure=getExpenditure();
        udd.updateUser(udm);



        return udm;
    }


    //region 基本參數


    //檢查是否已有設定值
    public  boolean getFirstSetting(){

        return  sp.getBoolean("isPreferenceSetting",false);

    }

    //設定第一次設定的參數值
    public  void setFirstSetting(){

        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("isPreferenceSetting",true);
        editor.commit();

    }


    public String getSex(){
        return sp.getString("sys_settings_sex","");
    }

    public void setSex(String sex){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_sex",sex);
        editor.commit();

    }



    public String getHeight(){
        return sp.getString("sys_settings_height","");
    }

    public void setHeight(String height){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_height",height);
        editor.commit();

    }

    public String getBirthday(){
        return sp.getString("sys_settings_birthday","");
    }

    public void setBirthday(String birthday){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_birthday",birthday);
        editor.commit();

    }

    public String getAge(){
        return sp.getString("sys_settings_age","");
    }

    public void setAge(String age){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_age",age);
        editor.commit();

    }

    //活動係數
    public String getCoefficient(){
        return sp.getString("sys_settings_coefficient","");
    }

    public void setCoefficient(String coefficient){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_coefficient",coefficient);
        editor.commit();

    }

    public String getTargetWeight(){
        return sp.getString("sys_settings_target_weight","");
    }

    public void setTargetWeight(String targetWeight){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_target_weight",targetWeight);
        editor.commit();

    }

    public String getTargetFatrate(){
        return sp.getString("sys_settings_target_fatrate","");
    }

    public void setTargetFatrate(String targetFatrate){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_target_fatrate",targetFatrate);
        editor.commit();

    }

    public String getTargetFatweight(){
        return sp.getString("sys_settings_target_fatweight","");
    }

    public void setTargetFatweight(String targetFatweight){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_target_fatweight",targetFatweight);
        editor.commit();

    }

    public String getInitWeight(){
        return sp.getString("sys_settings_init_weight","");
    }

    public void setInitWeight(String initWeight){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_init_weight",initWeight);
        editor.commit();

    }

    public String getInitFatrate(){
        return sp.getString("sys_settings_init_fatrate","");
    }

    public void setInitFatrate(String initFatrate){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_init_fatrate",initFatrate);
        editor.commit();

    }

    public String getInitFatweight(){
        return sp.getString("sys_settings_init_fatweight","");
    }

    public void setInitFatweight(String initFatweight){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_init_fatweight",initFatweight);
        editor.commit();

    }

    public String getWeight(){
        return sp.getString("sys_settings_base_weight","");
    }

    public void setWeight(String weight){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_base_weight",weight);
        editor.commit();

    }

    public String getFatrate(){
        return sp.getString("sys_settings_base_fatrate","");
    }

    public void setFatrate(String fatrate){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_base_fatrate",fatrate);
        editor.commit();

    }

    public String getFatweight(){
        return sp.getString("sys_settings_base_fatweight","");
    }

    public void setFatweight(String fatweight){

        Log.d(TAG,"fatweight=" + fatweight + ",getWeight()=" + getWeight() + ",getFatrate()=" + getFatrate());

        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_base_fatweight",fatweight);
        editor.commit();

    }

    public String getMetabolism(){
        return sp.getString("sys_settings_metabolism","");
    }

    public void setMetabolism(String metabolism){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("sys_settings_metabolism",metabolism);
        editor.commit();

    }

    public Integer getExpenditure(){
        if("".equals(sp.getString("sys_settings_expenditure","0"))){
            return 0;
        }
        return Integer.valueOf(sp.getString("sys_settings_expenditure","0")) ;
    }

    public void setExpenditure(String expenditure){
        SharedPreferences.Editor editor =sp.edit();
        if("".equals(expenditure)){
            expenditure="0";
        }
        editor.putString("sys_settings_expenditure",expenditure);
        editor.commit();

    }

    public boolean getHostConnection(){

        return sp.getBoolean("host_connection",false) ;
    }

    public void setHostConnection(boolean hostConnection){
        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("host_connection",hostConnection);
        editor.commit();
    }

    //endregion




    //region 常用函式

    //重新計算所有計算值
    public void recalculateSettings(){

        RecordDAO rd =new RecordDAO(context);
        //第一筆記錄
        RecordModel fstrm = rd.getFirtRecord();

        if(fstrm!=null ){



            //初始體重
            setInitWeight(String.valueOf( fstrm.Weight));
            //初始體脂肪
            setInitFatrate(String.valueOf(fstrm.FatRate));
            //初始脂肪重量
            setInitFatweight(String.valueOf(fstrm.FatWeight));

        }

        //最新的一筆記錄
        RecordModel lstrm = rd.getLatestRecord();

        if(lstrm!=null){
            //基礎代謝率
            setMetabolism(rd.getMetabolism(String.valueOf(lstrm.Weight)));

            //每日最低熱量
            setExpenditure(rd.getExpenditure(String.valueOf(lstrm.Weight)));

            //現在體重
            setWeight(String.valueOf(lstrm.Weight));

            //現在體脂肪
            setFatrate(String.valueOf(lstrm.FatRate));

            //現在脂肪重量
            setFatweight(String.valueOf(lstrm.FatWeight));

            //目標脂肪重量

            if(!getTargetWeight().equals("") && !getTargetFatrate().equals("")){
                Double targetfatweight =Double.parseDouble(getTargetWeight()) * Double.parseDouble(getTargetFatrate()) * 0.01;
                setTargetFatweight(String.valueOf(targetfatweight));
            }
        }


    }

    //endregion
    //endregion




}
