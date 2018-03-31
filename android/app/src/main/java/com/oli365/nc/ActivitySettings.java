package com.oli365.nc;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

import com.oli365.nc.controller.LogDAO;
import com.oli365.nc.controller.SettingDAO;
import com.oli365.nc.model.UserDataModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class ActivitySettings extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,DatePickerDialog.OnDateSetListener {

    private String _period ;

    private static final String TAG = ActivitySettings.class.getName();

    private SharedPreferences sp;

    private SettingDAO sd;
    private UserDataModel udm;
    //private String mybirthday;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //初始化
        //sp =PreferenceManager.getDefaultSharedPreferences(this);

        //初始資料
        try{
            initData();
        }
        catch (Exception ex){
            LogDAO.LogError(this,TAG,ex);
        }


    }


    private void initData(){

  /*
        sd=new SettingDAO(this);
        udm =sd.getSetting();
        DecimalFormat df=new DecimalFormat("#0.##");
        //性別
        ListPreference sex =(ListPreference) findPreference("sys_settings_sex");

        if(udm.Sex!=null && !"".equals(udm.Sex)){
            String s =udm.Sex;
            if(s.equals("M")){
                String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                sex.setSummary(a[0]);
            }else if(s.equals("F")){
                String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                sex.setSummary(a[1]);
            }
        }


        //身高
        EditTextPreference height =(EditTextPreference)findPreference("sys_settings_height");
        if(udm.Height!=null && !"".equals(udm.Height) && !"0".equals(udm.Height)){
            height.setSummary(df.format(Double.valueOf(udm.Height)));
        }


        //生日
        Preference birthday =(Preference)findPreference("sys_settings_birthday");
        if(udm.Birthday!=null && !"".equals(udm.Birthday) && !"1900-01-01".equals(udm.Birthday)){
            birthday.setSummary(udm.Birthday);
        }

        //選取生日
        birthday.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String  mybirthday=sd.getBirthday();
                if(mybirthday.equals("")){
                    mybirthday=getTagetDate();
                }
                //Log.d(TAG,"birthday click mybirthday=" + mybirthday + ",sd.birthday=" + sd.getBirthday());
                showDateDialog(0,mybirthday);
                return false;
            }
        });


        //目標體重
        EditTextPreference targetweight =(EditTextPreference)findPreference("sys_settings_target_weight");
        if(udm.TargetWeight!=null && !"".equals(udm.TargetWeight)){
            targetweight.setSummary(udm.TargetWeight);
        }

        //目標體脂
        EditTextPreference targetfatrate =(EditTextPreference)findPreference("sys_settings_target_fatrate");
        if(udm.TargetFatrate!=null && !"".equals(udm.TargetFatrate)){
            targetfatrate.setSummary(df.format(Double.valueOf(udm.TargetFatrate)) + "%");
        }


        Preference targetfatweight =(Preference)findPreference("sys_settings_target_fatweight");
        if(udm.TargetFatweight!=null && !"".equals(udm.TargetFatweight)){
            targetfatweight.setSummary(df.format(Double.valueOf(udm.TargetFatweight)));
        }

        Preference email =(Preference)findPreference("sys_settings_user_email");

        String semail = UserDAO.getUserEmail(this);
        if(semail!=null && !semail.equals("")){
            email.setSummary(semail);
        }

        Preference nickname =(Preference)findPreference("sys_settings_user_nickname");
        String snickname =UserDAO.getUserNickname(this);
        if(snickname!=null && !snickname.equals("")){
            nickname.setSummary(snickname);
        }

        //年紀
        Preference age =(Preference)findPreference("sys_settings_age");
        if(udm.Age!=null && !"".equals(udm.Age) && !"1900-01-01".equals(udm.Birthday)){
            age.setSummary(udm.Age);
        }


        Preference initweight =(Preference)findPreference("sys_setting_init_weight");
        if(udm.InitFatweight!=null && !"".equals(udm.InitFatweight)){
            initweight.setSummary(udm.InitWeight);
        }

        Preference initfatrate =(Preference)findPreference("sys_setting_init_fatrate");
        if(udm.InitFatrate!=null && !"".equals(udm.InitFatrate)){
            initfatrate.setSummary(df.format(Double.valueOf(udm.InitFatrate)));
        }

        Preference initfatweight =(Preference)findPreference("sys_setting_init_fatweight");
        if(udm.InitFatweight!=null && !"".equals(udm.InitFatweight)){
            initfatweight.setSummary(df.format(Double.valueOf(udm.InitFatweight)));
        }

        //體重
        Preference weight =(Preference)findPreference("sys_base_weight");
        if(udm.Weight!=null && !"".equals(udm.Weight) && !"0.0".equals(udm.Weight)){
            weight.setSummary(udm.Weight);
        }

        //體脂
        Preference fatrate = (Preference)findPreference("sys_base_fatrate");
        if(udm.BodyFatRate!=null && !"".equals(udm.BodyFatRate) && !"0.0".equals(udm.BodyFatRate)){
            fatrate.setSummary(udm.BodyFatRate);
        }

        //脂肪重量
        Preference fatweight =(Preference)findPreference("sys_base_fatweight");
        Log.d(TAG,"udm.BodyFatWeight=" + udm.BodyFatWeight);
        if(udm.BodyFatWeight!=null && !"".equals(udm.BodyFatWeight) && !"0.0".equals(udm.BodyFatWeight)){
            fatweight.setSummary(udm.BodyFatWeight);
        }


        Preference metabolism=(Preference)findPreference("sys_setting_metabolism");
        if(udm.Metabolism!=null && !"".equals(udm.Metabolism)){
            metabolism.setSummary(udm.Metabolism);
        }


        //最低熱量
        Preference expenditure=(Preference)findPreference("sys_setting_expenditure");
        if(udm.Expenditure!=null && !"".equals(udm.Expenditure) && udm.Expenditure!=0){
            expenditure.setSummary(String.valueOf(udm.Expenditure));
        }


        //活動係數
        ListPreference coefficient = (ListPreference) findPreference("sys_settings_coefficient");
        if(udm.Coefficient!=null && !"".equals(udm.Coefficient)){
            String[] a = getResources().getStringArray(R.array.sys_opts_day_life_item);
            switch (udm.Coefficient){
                case "1":
                    coefficient.setSummary(a[0]);
                    break;
                case "1.2":
                    coefficient.setSummary(a[1]);
                    break;
                case "1.375":
                    coefficient.setSummary(a[2]);
                    break;
                case "1.55":
                    coefficient.setSummary(a[3]);
                    break;
                case "1.725":
                    coefficient.setSummary(a[4]);
                    break;
                case "1.9":
                    coefficient.setSummary(a[5]);
                    break;

            }
        }


*/


        /*
        //性別
        Preference sex = findPreference("sys_setting_sex");
        if(sex instanceof ListPreference && ((ListPreference)sex).getEntry() !="" ){
            sex.setSummary(((ListPreference)sex).getEntry());
        }
        //初始體重
        Preference initweight = findPreference("sys_setting_init_weight");
        if(initweight instanceof EditTextPreference && ((EditTextPreference) initweight).getText()!=""){
            initweight.setSummary(((EditTextPreference) initweight).getText());
        }

        //初始體脂肪
        EditTextPreference sys_init_fatrate = (EditTextPreference)findPreference("sys_setting_init_fatrate");

        if(sys_init_fatrate.getText()!=null &&  sys_init_fatrate.getText()!=""){
            sys_init_fatrate.setSummary(((EditTextPreference) sys_init_fatrate).getText() + "%");
        }

        //目標體重
        Preference targetWeight =findPreference("sys_setting_target_weight");
        if(targetWeight instanceof EditTextPreference && ((EditTextPreference) targetWeight).getText()!=""){
            targetWeight.setSummary(((EditTextPreference) targetWeight).getText());
        }

        //目標開始日期
        Preference sys_target_start_date = findPreference("sys_setting_target_start_date");
        targetStartDate = sp.getString("sys_setting_target_start_date","");
        //空值就設今天日期
        if(targetStartDate.equals("")){
            final Calendar c = Calendar.getInstance();
            Date d =c.getTime();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
            targetStartDate= sdf.format(d);
        }
        else{
            sys_target_start_date.setSummary(targetStartDate);
        }
        sys_target_start_date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showDateDialog(0,shortTargetDate);
                return false;
            }
        });

        //目標結束日期
        Preference sys_target_end_date =findPreference("sys_setting_target_end_date");
        targetDate = sp.getString("sys_setting_target_end_date","");
        //空值就設定6個月
        int targetMonths =6;

        if(targetDate==""){
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH,targetMonths);
            Date d =c.getTime();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
            targetDate= sdf.format(d);
        }
        else{
            sys_target_end_date.setSummary(targetDate);
        }
        sys_target_end_date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                showDateDialog(1,targetDate);
                return false;
            }
        });

*/



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
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Log.d(TAG,"ActivitySettings onDateSet ,year =" + year + ",month=" + month + ",day=" + day);
        //生日
        if(_period=="0"){
            String targetDate = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day);
            Log.d(TAG,"targetdate=" + targetDate);
            sd.setBirthday(targetDate);
            Preference birthday =(Preference)findPreference("sys_settings_birthday");
            birthday.setSummary(targetDate);
            calculateAge();
            //Preference sys_target_start_date =findPreference("sys_setting_target_start_date");
            //sys_target_start_date.setSummary(targetStartDate);
            //editor.putString("sys_setting_target_start_date", targetStartDate);
            //Log.i(TAG, "update sys_target_start_date=" + targetStartDate);
        }
        else if(_period=="1"){
            /*
            String longDate =String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day);
            Preference sys_target_weight_date =findPreference("sys_setting_target_end_date");
            sys_target_weight_date.setSummary(longDate);
            editor.putString("sys_setting_target_end_date", longDate);
            */
           // Log.i(TAG, "update longDate=" + longDate);
        }

        //editor.commit();
        //Log.w("DatePicker","Date = " + year);
        //Toast.makeText(this, year, Toast.LENGTH_SHORT).show();
    }


    private void showDateDialog(int id,String dateString){

        Log.d(TAG,"showDateDialog dateString=" + dateString);

        _period= String.valueOf(id) ;
        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle b =new Bundle();
        b.putString("target_date",dateString);
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "datePicker");


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
    protected void onStop() {

        //結束前上傳資料

        //更新所有計算值的內容
        /*
        try{
        sd.recalculateSettings();

        //同步本機DB和setting
        udm =sd.getSetting();

        NetworkDAO nd =new NetworkDAO(this);

            nd.uploadJson("user/setting", udm, new NetworkDAO.downloadJosn() {
                @Override
                public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                    if(status== NetworkDAO.RETURN_CODE.SUCCESS){

                    }
                }
            });
        }
        catch (Exception ex){

            LogDAO.LogError(this,TAG,ex);
        }



*/

        super.onStop();
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

        //將首次設定的狀態改為true
        SettingDAO sd =new SettingDAO(this);
        if(sd.getFirstSetting()==false){
            sd.setFirstSetting();
        }

       // sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp=sharedPreferences;

        Preference pref = findPreference(key);
        if(pref instanceof EditTextPreference){
            EditTextPreference etp= (EditTextPreference) pref;
            /*
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
            */
            Log.d(TAG,"key value=" + key + ",key ");

            if(key.equals("sys_settings_target_fatrate")){
                pref.setSummary(etp.getText()+"%");
                calculateTargetFatweight();
            }
            else if(key.equals("sys_settings_target_weight")){
                Log.d(TAG,"start calculateTargetFatweight");
                calculateTargetFatweight();
                pref.setSummary(etp.getText());
            }
            else{
                pref.setSummary(etp.getText());
            }

        }
        else if(pref instanceof ListPreference){

            if(key.equals("sys_settings_sex")){
                ListPreference sex =(ListPreference)findPreference("sys_settings_sex");
                String s = sp.getString("sys_settings_sex", "");
                if(s.equals("M")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    sex.setSummary(a[0]);
                }else if(s.equals("F")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    sex.setSummary(a[1]);
                }
            }

            if(key.equals("sys_settings_coefficient")){
                ListPreference coefficient =(ListPreference)findPreference("sys_settings_coefficient");
                String s = sp.getString("sys_settings_coefficient", "");

                String[] a = getResources().getStringArray(R.array.sys_opts_day_life_item);
                switch (s){
                    case "1":
                        coefficient.setSummary(a[0]);
                        break;
                    case "1.2":
                        coefficient.setSummary(a[1]);
                        break;
                    case "1.375":
                        coefficient.setSummary(a[2]);
                        break;
                    case "1.55":
                        coefficient.setSummary(a[3]);
                        break;
                    case "1.725":
                        coefficient.setSummary(a[4]);
                        break;
                    case "1.9":
                        coefficient.setSummary(a[5]);
                        break;
                    default:
                        coefficient.setSummary(s);
                        break;

                }
            }

        }

    }

    //region 計算資料

    //計算年紀
    private void  calculateAge(){

        try{
            SettingDAO sd =new SettingDAO(this);
            String  birthday =sd.getBirthday();
            Log.d(TAG,"birthday =" + udm.Birthday + ",sd birthday=" + birthday);
            if(!birthday.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date =sdf.parse(birthday);
                Calendar dob = Calendar.getInstance();
                dob.setTime(date);
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                Log.d(TAG,"age =" + age);
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    age--;
                }

                sd.setAge(String.valueOf(age));
                Preference myage =(Preference)findPreference("sys_settings_age");
                myage.setSummary(String.valueOf(age));

            }
        }
        catch(Exception ex){
            Log.d(TAG,ex.toString());
        }

    }




    //計算脂肪重量
    private void calculateTargetFatweight(){

        //要抓最新資料
        udm = sd.getSetting();
        Log.d(TAG,"calculateTargetFatweight TargetWeight=" + udm.TargetWeight + ",TargetFatrate=" +udm.TargetFatrate);

        if(!udm.TargetWeight.equals("") && !udm.TargetFatrate.equals("")){

            try{

                Double targetfatweight = Double.valueOf(udm.TargetWeight) *  Double.valueOf( udm.TargetFatrate) * 0.01;

                Log.d(TAG,"targetfatweight=" + targetfatweight);

                DecimalFormat df = new DecimalFormat("#0.#");
                String result = String.valueOf(df.format(targetfatweight));
                sd.setTargetFatweight(result);
                Preference mytargetfatweight =(Preference)findPreference("sys_settings_target_fatweight");
                mytargetfatweight.setSummary(String.valueOf(result));

            }
            catch (Exception ex){
                Log.d(TAG,ex.toString());
            }

        }
    }


    //endregion


    //region 異步執行緒


    //endregion


}
