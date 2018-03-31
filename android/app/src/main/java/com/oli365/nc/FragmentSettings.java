package com.oli365.nc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oli365.nc.controller.SettingDAO;

/**
 * Created by USER on 2015/7/31.
 */
public class FragmentSettings extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    SettingDAO sd;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        /*
        //載入先前儲存的記錄
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

        ListPreference mode =(ListPreference)findPreference("mode");

        String s =sp.getString("mode", getResources().getString(R.string.mode_summary));
        String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
        if(s.equals("1")){
            s= a[0];
        }else if(s.equals("2"))
        {
            s =a[1];
        }

        mode.setSummary(s);
        EditTextPreference username = (EditTextPreference)findPreference("username");
        username.setSummary(sp.getString("username",getResources().getString(R.string.username_summary)));
        //密碼要改為相同長度的star
        EditTextPreference password = (EditTextPreference)findPreference("password");
        String star ="";
        String mPassword = sp.getString("password","");
        for(int i=0;i<mPassword.length();i++){
            star += "*";
        }
        if(star.length()>0){
            password.setSummary(star);
        }
        else{
            password.setSummary(getResources().getString(R.string.password_summary));
        }

        //password.setEnabled(false);
        EditTextPreference height = (EditTextPreference)findPreference("height");
        //height.setSummary(sp.getString("height",getResources().getString(R.string.height_summary)));
        EditTextPreference weight = (EditTextPreference)findPreference("weight");
        //weight.setSummary(sp.getString("weight",getResources().getString(R.string.weigth_summary)));
        EditTextPreference birthday = (EditTextPreference)findPreference("birthday");
        birthday.setSummary(sp.getString("birthday",getResources().getString(R.string.birthday_summary)));

        EditTextPreference email = (EditTextPreference)findPreference("email");
        email.setSummary(sp.getString("email",getResources().getString(R.string.email_summary)));

*/
    }


    //檢查是否已有資料
    private void initData(){
        sd =new SettingDAO(getActivity());
        //已經設定過資料
        if(sd.getFirstSetting()==true){




        }

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //如果是第一次設定，則下次登入不再直接跳出設定畫面
        if(sd.getFirstSetting()==false){
            sd.setFirstSetting();
        }


      /*
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
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



        //顯示改變值後的記錄
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        Preference pref = findPreference(key);
        if(pref instanceof  EditTextPreference){
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

            if(key.equals("mode")){
                ListPreference mode =(ListPreference)findPreference("mode");
                String s = sp.getString("mode", getResources().getString(R.string.mode_summary));
                if(s.equals("1")){
                   String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    mode.setSummary(a[0]);
                }else if(s.equals("2")){
                    String[] a = getResources().getStringArray(R.array.sys_opts_sex_item);
                    mode.setSummary(a[1]);
                }


            }
        }

 */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       return super.onCreateView(inflater, container, savedInstanceState);
       // v =inflater.inflate(R.layout.fragment_main, container, false);



      //  return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
