package com.oli365.nc.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alvinlin on 2016/12/27.
 */

public class ParameterDAO {

    private static final String TAG=UserUtility.class.getName();
    SharedPreferences sp;
    Context context ;

    public ParameterDAO(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    //檢查今天是否已經和主機同步
    public boolean getIsUpload(){
        String result =  sp.getString("UPLOAD_" + Utility.getShortDateString(),"N");
        if(result.equals("N")){
            return false;
        }
        else{
            return true;
        }
    }

    public void setIsUpload(boolean isUpload){
        if(isUpload==true){
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("UPLOAD_" + Utility.getShortDateString(),"Y");
            editor.apply();
        }
        else if (isUpload==false){
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("UPLOAD_" + Utility.getShortDateString(),"N");
            editor.apply();
        }
    }

/*
    public String getDbLastUpdateTime(){
        return  sp.getString(Utility.getToday(),"2000-1-1");
    }

    public void setDbLastUpdateTime(String lasttime){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("DB_LAST_UPDATE_TIME",lasttime);
        editor.apply();
    }
*/

}
