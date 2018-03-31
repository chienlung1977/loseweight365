package com.oli365.nc.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oli365.nc.model.UserDataModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alvinlin on 2016/11/10.
 */

public class UserDAO {

    //登出到期的小時數
    static final int EXPIRE_HOURS = 24 * 7;
    static final String TAG=UserDAO.class.getName();

    SharedPreferences sp;
    Context context ;
    UserDataModel sm ;
    //UserModel um;

    public UserDAO(Context context){
        this.context=context;
        sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        SettingDAO sd =new SettingDAO(context);
        sm = sd.getSetting();
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




    public String getMetabolism(){
        return sp.getString("sys_metabolism","0");
    }

    //性別
    public String getSex(){

        //return sp.getString("sys_sex","M");
        return sm.Sex;
    }

    //活動係數
    public String getCoefficient(){
        //return sp.getString("sys_base_day_life","0");

        return sm.Coefficient;
    }


    //目標體重
    public String getTargetWeight(){
        return sp.getString("sys_target_weight","0");
    }




    //取目標開始日期
    public String getTargetStartDate(){

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
        String targetStartDate= sp.getString("sys_target_start_date","");

        try{
            if(!targetStartDate.equals("")){
                Date d = sdf.parse(sp.getString("sys_target_start_date",""));
                targetStartDate =sdf.format(d);
            }

        }
        catch(Exception ex){
            Log.e(TAG,ex.getMessage());
        }

        return targetStartDate;
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




    //最後記錄體重
    public String getWeight(){
        return sp.getString("sys_base_weight","0");
    }

    //最後記錄體脂肪
    public String getBodyFat(){

        return sp.getString("sys_body_fat","0");
    }

    //初始體重
    public String getInitWegiht(){
        return sm.InitWeight;
    }

    //初始體脂肪
    public String getInitFatRate(){

        return sm.InitFatrate;
    }

    //計算脂肪重量
    private void setInitFatWeight(){
        Double initfatweight = Double.valueOf(getInitWegiht()) * (Double.valueOf(getInitFatRate()) * 0.01);
        DecimalFormat df = new DecimalFormat("#0.00");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_init_fat_weight",String.valueOf(Double.parseDouble(df.format(initfatweight))));
        editor.apply();
    }


    //初始脂肪重量
    public String getInitFatWeight(){
        setInitFatWeight(); //重新計算重量
        return sp.getString("sys_init_fat_weight","0");
    }


    //目前脂肪重量
    public String getFatWeight(){

        setFatWeight();
        return sp.getString("sys_fat_weight","0");
    }

    //重新計算目前脂肪重量
    public void setFatWeight(){

        Double fatweight = Double.valueOf(getWeight()) * (Double.valueOf(getBodyFat()) * 0.01);
        DecimalFormat df = new DecimalFormat("#0.00");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_fat_weight",String.valueOf(Double.parseDouble(df.format(fatweight))));
        editor.apply();
    }

    //已減去的肪脂重量
    private void setConsumFatWeight(){

        Double consumfatweight = Double.valueOf(getInitFatWeight() ) - Double.valueOf(getFatWeight());
        DecimalFormat df = new DecimalFormat("#0.00");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_consum_fat_weight",df.format(consumfatweight));
        editor.apply();
    }

    public String getConsumFatWeight(){
        setConsumFatWeight();
        return sp.getString("sys_consum_fat_weight","0");
    }

    //已減去的脂肪比例
    private void setConsumFatRate(){

        Double consumfatrate = Double.valueOf(getConsumFatWeight())/Double.valueOf(getInitFatWeight()) * 100 ;
        DecimalFormat df = new DecimalFormat("#0.00");

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_consum_fat_rate",df.format(consumfatrate));
        editor.apply();
    }

    public String getConsumFatRate(){
        setConsumFatRate();
        return sp.getString("sys_consum_fat_rate","0");
    }


    //已減去的體重
    public void setConsumWeight(){

        Double consumWeight =  Double.valueOf(getInitWegiht())- Double.valueOf(getWeight());
        DecimalFormat df = new DecimalFormat("#0.0");
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

    //取得每日卡路里
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

    //計算距離目標還有多少天數
    public void setTargetDays(){
        Double targetdays = Math.ceil(Double.valueOf(getTargetCalory()) / Double.valueOf(getDailyConsumCalory())) ;
        //Utility.putKeyValue(this.context,"sys_target_days",String.valueOf(targetdays));
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sys_target_days",String.valueOf(targetdays.intValue()));
        editor.apply();
    }

    //取得距離目標還有多少天數
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


    //region 使用者資料

    //註冊和登入後要初始化所有app會使用到的參數

    public static void initLoginData(Context context,String email,String uid,String nickname,String updatedate){
        setUserIslogin(context,true);
        setUserEmail(context,email);
        setUserUid(context,uid);
        setUserNickname(context,nickname);
        setUpdateDate(context,updatedate);
        //setUserLastLogin(context,Utility.getToday());
        //setUserLevel(context,level);
        //setUserPassword(context,udm.Password);
    }



    //識別碼
    public static void setUserUid(Context context,String userUid){
        Utility.putKeyValue(context,"USER_UID",userUid);
    }

    public static String getUserUid(Context context){
        return Utility.getKeyValue(context, "USER_UID");
    }

    //email
    public static void setUserEmail(Context context,String email){
        Utility.putKeyValue(context,"USER_EMAIL",email);
    }

    public static String getUserEmail(Context context){
        return Utility.getKeyValue(context, "USER_EMAIL");
    }

    //䁥稱
    public static void setUserNickname(Context context,String nickname){
        Utility.putKeyValue(context,"USER_NICKNAME",nickname);
    }

    public static String getUserNickname(Context context){
        return Utility.getKeyValue(context,"USER_NICKNAME");
    }

    //設定最後更新日期（同步用）
    public static void setUpdateDate(Context context,String updatedate){
        Utility.putKeyValue(context,"USER_UPDATE_DATE",updatedate);
    }

    //取得最後更新日期
    public static String getUpdateDate(Context context){
        return Utility.getKeyValue(context,"USER_UPDATE_DATE");
    }
    /*
        //最後登入日期
        public static void setUserLastLogin(Context context,String logintime){
            Utility.putKeyValue(context,"USER_LASTLOGIN",logintime);
        }

        public static String getUserLastLogin(Context context){
            return Utility.getKeyValue(context,"USER_LASTLOGIN");
        }

        //體脂計的種類
        public static void setUserLevel(Context context,String level){
            Utility.putKeyValue(context,"USER_LEVEL",level);
        }

        public static String getUserLevel(Context context){
            return Utility.getKeyValue(context,"USER_LEVEL");
        }

    */
    //登入狀態
    public static void setUserIslogin(Context context, boolean islogin){
        Utility.putKeyValue(context,"USER_ISLOGIN", String.valueOf(islogin));
    }

    public static boolean getUserIslogin(Context context){
        if(Utility.getKeyValue(context,"USER_ISLOGIN").equals("")){
            return false;
        }
        return Boolean.valueOf(Utility.getKeyValue(context,"USER_ISLOGIN"));
    }

    // 是否記錄
    public static boolean getUserIsRemember(Context context){
        if(Utility.getKeyValue(context,"USER_ISREMEMBER").equals("")){
            return false;
        }
        return Boolean.valueOf(Utility.getKeyValue(context,"USER_ISREMEMBER"));
    }

    public static void setUserIsRemember(Context context,boolean isremember){
        Utility.putKeyValue(context,"USER_ISREMEMBER", String.valueOf(isremember));
    }

    /*
    public static String getUserPassword(Context context){
        return Utility.getKeyValue(context,"USER_PASSWORD");
    }

    public static void setUserPassword(Context context,String password){
        Utility.putKeyValue(context,"USER_PASSWORD",password);
    }
    */

    //endregion


    //region 登入

    //登入是否已到期
    public static boolean isLoginExpire(Context context,String loginDate){

        long diff =0;

        try {

            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            Date dateStart = format.parse(loginDate);
            Date dateNow = new Date();
            diff = dateStart.getTime() - dateNow.getTime();

        } catch (Exception ex) {
           LogDAO.LogError(context,TAG,ex);
        }

        boolean result = (diff <0);

        return result;
    }

    //處理登入所要記錄的行為
    public static boolean processLogin(Context context,boolean isLogin,String uid){

        try{


            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            //System.out.println(dateFormat.format(cal.getTime()));


            if(isLogin==true ){
                Utility.putKeyValue(context,"IS_LOGIN","YES");
                //到期時間
                cal.add(Calendar.HOUR_OF_DAY, EXPIRE_HOURS);
            }
            else{
                Utility.putKeyValue(context,"IS_LOGIN","NO");
            }

            String dateTime  = dateFormat.format(cal.getTime());
            Utility.putKeyValue(context,"LOGIN_DATE",dateTime);
            Utility.putKeyValue(context,"USER_UID",uid);

        }
        catch (Exception ex){
            LogDAO.LogError(context,TAG,ex);
            return false;
        }

        return true;

    }


    //是否已有登入記錄，以及已登入
    public static boolean isSysLogin(Context context ){

        try{

            if(UserDAO.getUserIslogin(context)==false){
                return false;
            }

            /*
            String loginDate =UserDAO.getUserLastLogin(context);

            if(loginDate.equals("")){
                return false;
            }

            //檢查已登入但是否已到期

            if(isLoginExpire(context,loginDate)==true ){
                return false;
            }

            //都通過則回傳true，並將到期時間往後推10個小時，就不用一直登入
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            cal.add(Calendar.HOUR_OF_DAY, EXPIRE_HOURS);
            String dateTime  = dateFormat.format(cal.getTime());
            UserDAO.setUserLastLogin(context,dateTime);
            */

        }catch (Exception ex ){
            LogDAO.LogError(context,TAG,ex);
            return false;
        }

        return true;
    }

    public static void Logout(Context context){

        NetworkDAO nd=new NetworkDAO(context);
        UserDataDAO udd =new UserDataDAO(context);
        UserDataModel udm = new UserDataModel();
        udm.UserUid = UserDAO.getUserUid(context);
        udm.Email = UserDAO.getUserEmail(context);

        final Context c =context;

        if(udm!=null){
            try{

               // Log.d(TAG,"logout object =" + udm.toString());

                nd.uploadJson("user/logout", udm, new NetworkDAO.downloadJosn() {
                    @Override
                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                            UserDAO.setUserIslogin(c,false);
                        }
                    }
                });
            }
            catch(Exception ns){
                LogDAO.LogError(context,TAG,ns);
                Log.e(TAG,ns.toString());
            }


        }


        /*
        Log.i(TAG,"Logout start !");

        Utility.LogoutTask lt = new Utility.LogoutTask(context);
        lt.execute();

        Log.i(TAG,"Logout end !");
*/
        /*
        InputStream inputStream = null;


        try{

            //送到伺服器的登出記錄
            String hostUrl =  getHostUrl(context,"/users/logout");
            String userid = getUserUid(context);
            JsonObject json = new JsonObject();
            json.addProperty("uid", userid);

            URL url =new URL(hostUrl);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            //dto.setCreator(java.net.URLEncoder.encode(dto.getCreator(), "utf-8"));
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if(statusCode==200){
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = getStringFromInputStream(inputStream);
                Log.i(TAG,response);
            }



        }
        catch(Exception ex){
            Log.e(TAG,ex.toString());
        }




        //清空記錄
        putKeyValue(context,"IS_LOGIN","");
        putKeyValue(context,"LOGIN_DATE","");
        putKeyValue(context,"USER_UID","");

        */

    }



    //endregion



}
