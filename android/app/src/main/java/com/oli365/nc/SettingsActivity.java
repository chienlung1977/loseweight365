package com.oli365.nc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by alvinlin on 2015/10/27.
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //初始化預設資料
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
*/
        Preference sex = findPreference("sys_sex");
        if(sex instanceof ListPreference && ((ListPreference)sex).getEntry() !="" ){
            sex.setSummary(((ListPreference)sex).getEntry());
        }

        Preference age = findPreference("sys_age");
        if(age instanceof EditTextPreference && ((EditTextPreference) age).getText()!=""){

            age.setSummary(((EditTextPreference)age).getText());
        }

        Preference height = findPreference("sys_height");
        if(height instanceof  EditTextPreference && ((EditTextPreference) height).getText()!=""){
            height.setSummary(((EditTextPreference) height).getText());
        }

        Preference weight =findPreference("sys_base_weight");
        if(weight instanceof EditTextPreference && ((EditTextPreference) weight).getText()!=""){
            weight.setSummary(((EditTextPreference) weight).getText());
        }

        Preference targetWeight =findPreference("sys_target_weight");
        if(targetWeight instanceof EditTextPreference && ((EditTextPreference) targetWeight).getText()!=""){
            targetWeight.setSummary(((EditTextPreference) targetWeight).getText());
        }
    }

    @Override
    protected void onPause() {
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


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
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
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

    }
}
