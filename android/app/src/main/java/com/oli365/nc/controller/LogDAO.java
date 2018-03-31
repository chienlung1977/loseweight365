package com.oli365.nc.controller;

import android.content.Context;
import android.util.Log;

import com.oli365.nc.model.Config;
import com.oli365.nc.model.LogModel;

/**
 * Created by alvinlin on 2016/11/16.
 */

public class LogDAO {

    static final String TAG=LogDAO.class.getName();
    //記錄錯誤並上傳至伺服器
    public static void LogError(Context context,String classname , Exception ex){

        if(ex!=null){

            Log.d(classname,ex.toString());
            ex.printStackTrace();

            NetworkDAO nd =new NetworkDAO(context);

            LogModel lm =new LogModel();
            lm.CreateTime=Utility.getToday();
            lm.UserUid = UserDAO.getUserUid(context);
            lm.Soruce= classname;
            lm.Message = ex.getMessage();
            lm.FullMessage = ex.toString();
            lm.Memo= context.getPackageName();
            lm.Status= Config.PROCESS_STATUS.OPEN;
 /*
            //上傳到伺服器
            try{

                nd.uploadJson("logs/error", lm, new NetworkDAO.downloadJosn() {
                    @Override
                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                        //   Log.d(TAG,"RETURN CODE=" + status.toString() + ",Error=" + ex.toString() + ",msg=" + result);
                    }
                });

            }
            catch (Exception ns){
                Log.e(TAG,ns.toString());
                Utility.showMessage(context,classname + ":" + ex.toString(), Toast.LENGTH_LONG);
            }

             */
        }



        /*
        if(network.checkNetwork()){

            Log.d(TAG,"CHECK NETWORK = TRUE ");

            LogModel lm = new LogModel(Utility.getToday(),
                    UserDAO.getUserUid(context),
                    classname,
                    ex.getMessage(),
                    ex.toString(),
                    "",
                    LogModel.PROCESS_STATUS.OPEN);
            Gson gson = new Gson();
            String jstr = gson.toJson(lm);
            try{
                Log.d(TAG,"start upload json = " +jstr);
                //network.uploadJson("/logs/error",jstr);

            }
           catch (Exception eex){
               Log.d(TAG,eex.toString());
           }

        }
        else{
            Utility.showMessage(context,context.getString(R.string.error_message));
        }
*/
    }




}
