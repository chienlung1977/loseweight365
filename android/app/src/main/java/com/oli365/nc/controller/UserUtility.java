package com.oli365.nc.controller;

import android.content.Context;
import android.content.Intent;

import com.oli365.nc.host.SyncHostDataService;
import com.oli365.nc.model.UserDataModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alvinlin on 2016/7/8.
 * 使用者相關檢查函式
 */
public class UserUtility {

    Context context ;
    private static final String TAG=UserUtility.class.getName();

    public UserUtility(Context context){
        this.context = context;
    }

    //啓動服務初始化所有主機上的資料
    public void  initAppLocalData(String userUid,String email){

        Intent mServiceIntent =new Intent(context,SyncHostDataService.class);

        mServiceIntent.putExtra("UID",userUid);
        mServiceIntent.putExtra("EMAIL",email);

        context.startService(mServiceIntent);

    }

    //是否需要更新app上的使用者資料
    public boolean isUpdateLocalUserData(String userUid,String srvUpdateDate){

        boolean result =false;
        UserDataDAO udd =new UserDataDAO(context);
        UserDataModel udm =null;
        if(udd.getCount()==0){
            result=true;
            return result;
        }

        if(udd.getCount()>0){
            udm =udd.getUser(userUid);

            //比較線上更新日期和app的更新時間

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date date_01 = sdf.parse(udm.UpdateDate);
                Date date_02 = sdf.parse(srvUpdateDate);
                if(date_01.compareTo(date_02)==-1){
                    //本機更新日期小於主機更新日期，代表要更新資料
                    result=true;
                    return result;
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


        }

        return result;
    }











}
