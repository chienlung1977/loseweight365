package com.oli365.nc;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alvinlin on 2016/4/17.
 */
public  class Utility {

    private static final String TAG= Utility.class.getName();
    //當作KEY用
    private static final String PRIMARY_KEY ="nc.oli365.com";

    //登出到期的小時數
    private static final int EXPIRE_HOURS = 10;

    //取得金鑰共20碼，主機只取前14碼
    public static String getKey(String baseKey){
        String key ;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        key = baseKey +  df.format(date) + getRandom(6);
        return key;
    }


    public static String getKey(){

        String key= getKey("lclove");
        return key;
    }


    //亂數值
    public static String getRandom(int length){

        String s = "abcdefghijklmnopqrstuvwxyz012356789";
        String result = "";
        Random r = new Random();
       // int i1 = r.nextInt(80 - 65) + 65;
        int rn ;
        for(int i =0;i<length;i++){
            rn  = r.nextInt(s.length()-1)+1;
            result +=  s.substring(rn,rn+1);
        }

        return result;

    }

    //取得主機位置
    public static String getHost(Context context){

        String host =context.getString(R.string.host);
        return host ;
    }

    //組成完整的查詢路徑
    public static String getHostUrl(Context context , String path ){

        String hostUrl =getHost(context) ;
        if(path.substring(0,1)!="/"){
            hostUrl +="/";
        }

        hostUrl += path + "?key=" + getKey();
        return hostUrl;
    }

    //取得日期
    public static String getShortDate(String mydate){

        String dateString="";
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date _date = sdf.parse(mydate);
            dateString = sdf.format(_date);

        }catch (Exception ex){
            Log.e("Utility", "getShortDate error :" + ex.getMessage());
        }

        return dateString;
    }

    //取得現在日期時間
    public static String getToday(){
        String dateString="";
        try{
            final Calendar c = Calendar.getInstance();
            Date d =c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date _date = sdf.parse(new Date().toString());
            dateString = sdf.format(d);

        }catch (Exception ex){
            Log.e(TAG, "getToday error : " + ex.getMessage());
        }

        return dateString;
    }

    //取得簡短日期
    public static String getDateString(){

        String dateString="";
        try{

            final Calendar c = Calendar.getInstance();
            Date d =c.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            //Date _date = sdf.parse(new Date().toString());
            dateString = sdf.format(d);

        }catch (Exception ex){
            Log.e(TAG, "getDateString error :" + ex.getMessage());
        }

        return dateString;
    }


    public static void putKeyValue(Context context,String key,String value) {

        SharedPreferences pref = context.getSharedPreferences(PRIMARY_KEY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor =pref.edit();
        editor.putString(key, value);
        //pref.edit().apply();
        editor.commit();
    }

    public static String getKeyValue(Context context,String key){

        SharedPreferences pref = context.getSharedPreferences(PRIMARY_KEY,context.MODE_PRIVATE);
        return pref.getString(key,"");
    }

    //登入是否已到期
    public static boolean isLoginExpire(String loginDate){

        long diff =0;

        try {

            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            Date dateStart = format.parse(loginDate);
            Date dateNow = new Date();
            diff = dateStart.getTime() - dateNow.getTime();

        } catch (ParseException e) {
           //e.printStackTrace();
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
                putKeyValue(context,"IS_LOGIN","YES");
                //到期時間
                cal.add(Calendar.HOUR_OF_DAY, EXPIRE_HOURS);
            }
            else{
                putKeyValue(context,"IS_LOGIN","NO");
            }

            String dateTime  = dateFormat.format(cal.getTime());
            putKeyValue(context,"LOGIN_DATE",dateTime);
            putKeyValue(context,"USER_UID",uid);

        }
        catch (Exception ex){
            return false;
        }

        return true;

    }


    //是否已有登入記錄，以及已登入
    public static boolean isSysLogin(Context context ){

        try{

            String islogin =getKeyValue(context,"IS_LOGIN");

            if(!islogin.equals("YES")){
                return false;
            }

            String loginDate =getKeyValue(context,"LOGIN_DATE");

            if(loginDate.equals("")){
                return false;
            }

            //檢查已登入但是否已到期

            if(isLoginExpire(loginDate)==true ){
                return false;
            }

            //都通過則回傳true，並將到期時間往後推10個小時，就不用一直登入
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            cal.add(Calendar.HOUR_OF_DAY, EXPIRE_HOURS);
            String dateTime  = dateFormat.format(cal.getTime());
            putKeyValue(context,"LOGIN_DATE",dateTime);


        }catch (Exception ex ){

            return false;
        }

        return true;
    }

    public static void Logout(Context context){

        Log.i(TAG,"Logout start !");

        LogoutTask lt = new LogoutTask(context);
        lt.execute();

        Log.i(TAG,"Logout end !");

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


    //加開執行緒執行登出
    private static class LogoutTask extends AsyncTask<Void, Void, Boolean> {

        Context context ;


        public LogoutTask(Context main_context){

            Log.i(TAG,"LogoutTask init !");
            this.context =main_context;
        }

        protected Boolean doInBackground(Void... params) {

            Log.i(TAG,"LogoutTask doInBackground !");
            InputStream inputStream = null;


            try{

                //送到伺服器的登出記錄
                String hostUrl =  getHostUrl(context,"users/logout");
                Log.i(TAG,"Post to url : " + hostUrl);
                String userid = getUserUid(context);
                String email = getUserEmail(context);
                Log.i(TAG,"json data userid : " + userid + ", email : " + email);
                JsonObject json = new JsonObject();
                json.addProperty("uid", userid);
                json.addProperty("email", email);

                URL url =new URL(hostUrl);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                //dto.setCreator(java.net.URLEncoder.encode(dto.getCreator(), "utf-8"));
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                Log.i(TAG,"LogoutTask doInBackground - POST!");
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(json.toString());
                wr.flush();
                wr.close();

                int statusCode = urlConnection.getResponseCode();
                Log.i(TAG,"LogoutTask doInBackground - statusCode = " + statusCode + " !");
                if(statusCode==200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = getStringFromInputStream(inputStream);
                    Log.i(TAG,response);
                }



            }
            catch(Exception ex){
                Log.e(TAG,"LogoutTask doInBackground - error :" + ex.toString());
            }


            //清空記錄
            putKeyValue(context,"IS_LOGIN","");
            putKeyValue(context,"LOGIN_DATE","");
            putKeyValue(context,"USER_UID","");


            return true;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Boolean result) {

            Log.i(TAG,"LogoutTask onPostExecute !");
            //showDialog("Downloaded " + result + " bytes");
        }
    }





    public static void showMessage(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }



    public static String getUserUid(Context context){
        return getKeyValue(context, "USER_UID");
    }

    public static String getUserEmail(Context context){
        return getKeyValue(context, "USER_EMAIL");
    }

    public static Boolean isEmpty(EditText editText){

        if(editText.getText().toString().trim().equals("")){
            return true;
        }

        return false;

    }



    //取得今天新增天數後的日期
    public static String getDate(int adddays){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,adddays);
        Date d =c.getTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
        String shortTargetDate= sdf.format(d);
        return shortTargetDate;
    }

    //格式化成簡短日期
    public static String getFormatShortDate(String dateString){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
        Date mydate =null;
        try{
            mydate = sdf.parse(dateString);

        }
        catch (ParseException ex){
           Log.e(TAG,"getFormatShortDate error:" + ex.getMessage());
        }
        return sdf.format(dateString);

    }





    public static Bitmap resizeBitmap(Context context,Uri uri){

        Bitmap ThumbImage =null;

        try{

            Bitmap bitmap = MediaStore.Images.Media.getBitmap( context.getContentResolver(), uri);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            InputStream in = context.getContentResolver().openInputStream(
                    uri);
            BitmapFactory.decodeStream(in, null, bitmapOptions);

            float widthRatio = (float) 800 / (float)bitmapOptions.outWidth;
            float heightRatio =(float) 600 / (float)bitmapOptions.outHeight;
            float height ;
            float weight ;
            if(widthRatio>heightRatio){
                height= bitmap.getHeight()*heightRatio;
                weight =bitmap.getWidth()*heightRatio;
            }
            else{
                height= bitmap.getHeight()*widthRatio;
                weight =bitmap.getWidth()*widthRatio;
            }

            int newheight = (int)height;
            int newweight =(int)weight;

            ThumbImage = ThumbnailUtils.extractThumbnail(bitmap, newweight, newheight);


        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return ThumbImage;
    }

    public static String SaveIamge(Bitmap finalBitmap) {

        String myPath ="";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/nc_images");
        myDir.mkdirs();
        String randomString = Utility.getDateString() + "_" + Utility.getRandom(8);
        //Random generator = new Random();
        //int n = 10000;
        //n = generator.nextInt(n);
        String fname = randomString +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            myPath =  file.getPath() ;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myPath;
    }



    public static  boolean isEmail(String email) {


        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(email);


        boolean b = m.matches();

        Log.i(TAG,"email:" + email + " is matches =" + b);

        return b;
    }

}

