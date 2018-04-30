package com.oli365.nc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oli365.nc.controller.LogDAO;
import com.oli365.nc.controller.SettingDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class ActivitySettings extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = ActivitySettings.class.getName();
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //初始化
        //sp =PreferenceManager.getDefaultSharedPreferences(this);

        //初始資料
        try{

            sp= getPreferenceScreen().getSharedPreferences();
            initData();

        }
        catch (Exception ex){
            LogDAO.LogError(this,TAG,ex);
        }


    }


    //載入初始資料
    private void initData(){


        ListPreference mode =(ListPreference)findPreference("sys_settings_system_level");
        String s = sp.getString("sys_settings_system_level", getResources().getString(R.string.sys_settings_system_level_summary));
        if(s.equals("N")){
            String[] a = getResources().getStringArray(R.array.sys_opts_system_level_item);
            mode.setSummary(a[0]);
        }else if(s.equals("H")){
            String[] a = getResources().getStringArray(R.array.sys_opts_system_level_item);
            mode.setSummary(a[1]);
        }

    }



    //生日預設往前推20年


    private String getTagetDate(){

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-20);
        Date d =c.getTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        String targetdate = sdf.format(d);
        return targetdate;
    }






    @Override
    protected void onPause() {

        Log.d(TAG,"Activity onPause");
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        //將首次設定的狀態改為true
        SettingDAO sd =new SettingDAO(this);
        if(sd.getFirstSetting()==false){
            sd.setFirstSetting();
        }

        Preference pref = findPreference(key);
        if(pref instanceof EditTextPreference){
            EditTextPreference etp= (EditTextPreference) pref;
            if(key.equals("password")){
                String star ="";
                for(int i=0;i<etp.getText().length();i++){
                    star += "*";
                }
                pref.setSummary(star);
            }
            else{
                pref.setSummary(etp.getText());
            }

        }
        else if(pref instanceof ListPreference){


            //量測器等級N 正常 H 七大指數
            if(key.equals("sys_settings_system_level")){
                ListPreference mode =(ListPreference)findPreference("sys_settings_system_level");

                SharedPreferences.Editor editor =sp.edit();
                editor.putString("sys_settings_system_level",mode.getValue());
                editor.commit();
                Log.v(TAG,"level=" + mode.getValue());

                String s = sp.getString("sys_settings_system_level", getResources().getString(R.string.sys_settings_system_level_summary));
                if(s.equals("N")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_system_level_item);
                    mode.setSummary(a[0]);
                }else if(s.equals("H")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_system_level_item);
                    mode.setSummary(a[1]);
                }




            }
        }


    }



}
