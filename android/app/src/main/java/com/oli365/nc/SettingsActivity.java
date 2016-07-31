package com.oli365.nc;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by alvinlin on 2015/10/27.
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,DatePickerDialog.OnDateSetListener {

    private String _period ;

    private static final String TAG = "SettingsActivity.tag";

    private String shortTargetDate;
    private String targetDate;
    SharedPreferences sp ;

    private void initData(){

        sp= PreferenceManager.getDefaultSharedPreferences(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //初始化預設資料
        initData();

        /*
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        Preference username = findPreference("username");
        if(username instanceof EditTextPreference && ((EditTextPreference) username).getText()!=""){
            username.setSummary(((EditTextPreference) username).getText());
        }
        Preference email =findPreference("email");
        if(email instanceof EditTextPreference && ((EditTextPreference) email).getText()!=""){
            email.setSummary(((EditTextPreference) email).getText());
        }
        Preference password =findPreference("password");
        if(password instanceof EditTextPreference && ((EditTextPreference) password).getText()!=""){
            String star ="";

            for(int i=0;i< ((EditTextPreference) password).getText().length();i++){
                star += "*";
            }
            password.setSummary(star);
        }

        Preference birthday =findPreference("birthday");
        if(birthday instanceof EditTextPreference && ((EditTextPreference) birthday).getText()!=""){
            birthday.setSummary(((EditTextPreference) birthday).getText());
        }






        Preference age = findPreference("sys_age");
        if(age instanceof EditTextPreference && ((EditTextPreference) age).getText()!=""){

            age.setSummary(((EditTextPreference)age).getText());
        }

        Preference height = findPreference("sys_height");
        if(height instanceof  EditTextPreference && ((EditTextPreference) height).getText()!=""){
            height.setSummary(((EditTextPreference) height).getText());
        }
*/


        Preference sex = findPreference("sys_sex");
        if(sex instanceof ListPreference && ((ListPreference)sex).getEntry() !="" ){
            sex.setSummary(((ListPreference)sex).getEntry());
        }

        Preference weight =findPreference("sys_base_weight");
        if(weight instanceof EditTextPreference && ((EditTextPreference) weight).getText()!=""){
            weight.setSummary(((EditTextPreference) weight).getText());
        }

        Preference targetWeight =findPreference("sys_target_weight");
        if(targetWeight instanceof EditTextPreference && ((EditTextPreference) targetWeight).getText()!=""){
            targetWeight.setSummary(((EditTextPreference) targetWeight).getText());
        }

        Preference initweight = findPreference("sys_init_weight");
        if(initweight instanceof EditTextPreference && ((EditTextPreference) initweight).getText()!=""){
            initweight.setSummary(((EditTextPreference) initweight).getText());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        //活動係數
        bindDailyLife("sys_base_day_life");



      /*
        Preference sys_short_target =findPreference("sys_short_target");
        if(sys_short_target instanceof EditTextPreference && ((EditTextPreference) sys_short_target).getText()!=""){
            sys_short_target.setSummary(((EditTextPreference) sys_short_target).getText());
        }

        //SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

        Preference sys_short_target_date =findPreference("sys_short_target_date");
        shortTargetDate =sp.getString("sys_short_target_date","");
        //如果是空值就設定短期3個月
        if(shortTargetDate==""){

            final Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH,3);
            Date d =c.getTime();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
            shortTargetDate= sdf.format(d);

        }
        Log.i(TAG,"shortTargetDate=" + shortTargetDate);
        if(sys_short_target_date instanceof Preference && shortTargetDate!=""){
            sys_short_target_date.setSummary( shortTargetDate);
        }
        sys_short_target_date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showDateDialog(0,shortTargetDate);
                return false;
            }
        });
*/
        /*
        if(sys_short_target_date instanceof EditTextPreference && ((EditTextPreference) sys_short_target_date).getText()!=""){
            sys_short_target_date.setSummary(((EditTextPreference) sys_short_target_date).getText());
        }
*/
        Preference sys_target_weight_date =findPreference("sys_target_weight_date");
        targetDate = sp.getString("sys_target_weight_date","");
        //空值就設定長期1年
        if(targetDate==""){

            final Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR,1);
            Date d =c.getTime();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
            shortTargetDate= sdf.format(d);

        }


        Log.i(TAG,"targetDate=" + targetDate);
        if(sys_target_weight_date instanceof Preference && targetDate!=""){
            sys_target_weight_date.setSummary(targetDate);
        }
        sys_target_weight_date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showDateDialog(1,targetDate);
                return false;
            }
        });
        /*
        if(sys_target_weight_date instanceof EditTextPreference && ((EditTextPreference) sys_target_weight_date).getText()!=""){
            sys_target_weight_date.setSummary(((EditTextPreference) sys_target_weight_date).getText());
        }
        */
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        //SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sp.edit();
        //do some stuff for example write on log and update TextField on activity
        //短期
        if(_period=="0"){
            String shortDate = String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day);
            Preference sys_short_target_date =findPreference("sys_short_target_date");
            sys_short_target_date.setSummary(shortDate);
            editor.putString("sys_short_target_date", shortDate);
            Log.i(TAG, "update targetDate=" + shortDate);
        }
        else if(_period=="1"){
            String longDate =String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day);
            Preference sys_target_weight_date =findPreference("sys_target_weight_date");
            sys_target_weight_date.setSummary(longDate);
            editor.putString("sys_target_weight_date", longDate);
            Log.i(TAG, "update longDate=" + longDate);
        }

        editor.commit();
        //Log.w("DatePicker","Date = " + year);
        //Toast.makeText(this, year, Toast.LENGTH_SHORT).show();
    }


    private void showDateDialog(int id,String dateString){
        // Use the current date as the default date in the picker

        _period= String.valueOf(id) ;
        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle b =new Bundle();
        b.putString("target_date",dateString);
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "datePicker");
        /*
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //短期
        if(id==0){

            new DatePickerDialog(this,

                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int y, int m,
                                              int d) {

                            Preference sys_short_target_date =findPreference("sys_short_target_date");
                            sys_short_target_date.setSummary(setDateFormat(y,m,d));
                            //dateText.setText("你設定的日期為"+setDateFormat(year,month,day));
                        }
                        }
                    , year, month, day);



        }

        //長期
        else if(id==1){
            new DatePickerDialog(this,

                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int y, int m,
                                              int d) {

                            Preference sys_target_weight_date =findPreference("sys_target_weight_date");
                            sys_target_weight_date.setSummary(setDateFormat(y,m,d));

                        }
                    }
                    , year, month, day);
        }

        */


    }
/*
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

        Log.i("dasd","year "+i+" month "+i2+" day "+i3);


    }
*/

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }


    @Override
    protected void onPause() {
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

    //每日活動係數
    private void bindDailyLife(String key){

        Preference pref = findPreference(key);
        if(pref instanceof ListPreference && key.equals("sys_base_day_life")){

            ListPreference sys_base_day_life=(ListPreference)findPreference("sys_base_day_life");
            String s = sp.getString("sys_base_day_life","請選擇活動係數");
            String[] a = getResources().getStringArray(R.array.sys_opts_day_life_item);
            switch (s){
                case "1":
                    sys_base_day_life.setSummary(a[0]);
                    break;
                case "1.2":
                    sys_base_day_life.setSummary(a[1]);
                    break;
                case "1.375":
                    sys_base_day_life.setSummary(a[2]);
                    break;
                case "1.55":
                    sys_base_day_life.setSummary(a[3]);
                    break;
                case "1.725":
                    sys_base_day_life.setSummary(a[4]);
                    break;
                case "1.9":
                    sys_base_day_life.setSummary(a[5]);
                    break;
                default:
                    sys_base_day_life.setSummary(s);
                    break;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //是否有設定
        /*
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        String value ="";


        if (key.equals("mode")) {
            //Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            // connectionPref.setSummary(sharedPreferences.getString(key, ""));
            //editor.putString("mode",,)
            // editor.putString()
        }
        else if (key.equals("username")){
            value =sharedPreferences.getString("username","");
            editor.putString(key,value);
        }

        editor.commit();

        */

        //顯示改變值後的記錄
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        //第一次設定後將狀態改為參數已設定
        boolean isSetting = sp.getBoolean("isPreferenceSetting",false);
        if(isSetting==false){
            SharedPreferences.Editor editor =sp.edit();
            editor.putBoolean("isPreferenceSetting",true);
            editor.commit();
        }

        Log.i(TAG,"onSharedPreferenceChanged , key=" + key);
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

            if(key.equals("sys_sex")){
                ListPreference mode =(ListPreference)findPreference("sys_sex");
                String s = sp.getString("sys_sex", "請選擇性別");
                if(s.equals("M")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    mode.setSummary(a[0]);
                }else if(s.equals("F")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    mode.setSummary(a[1]);
                }
            }


        }


        //活動係數
        bindDailyLife(key);

    }
}
