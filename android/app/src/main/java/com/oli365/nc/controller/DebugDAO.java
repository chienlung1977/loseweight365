package com.oli365.nc.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by alvinlin on 2016/12/1.
 */

public class DebugDAO {

    public enum SHOW_MODE{
        DEBUG,NORMAL
    }

    private final static SHOW_MODE MODE=SHOW_MODE.NORMAL;
    private final static String  TAG=DebugDAO.class.getName();

    //是否為偵錯模式
    public static boolean IsDebugMode(){

        if(MODE==SHOW_MODE.DEBUG){
            return true;
        }
        else if(MODE==SHOW_MODE.NORMAL){
            return false;
        }

        return false;
    }




    public static boolean CopyDbFile2Sd(Context context ){
        try {

            File sd =  Environment.getExternalStorageDirectory();
            //File data = Environment.getDataDirectory();

            Log.d(TAG,"Environment.getExternalStorageState()=" + Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
            //Log.d(TAG,"sd card path=" + sd.getPath() + ",AbsolutePath" + sd.getAbsolutePath());
            //Log.d(TAG,"data path =" + data.getPath() + ",AbsolutePath=" + data.getAbsolutePath());
            //Log.d(TAG,"image path =" + Utility.getImagePath(context));

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/" + DbHelper.DATABASE_NAME;
                Log.d(TAG,"source path =" + currentDBPath);
                String backupDBPath = Utility.getImagePath(context) +   Utility.getDateString() + DbHelper.DATABASE_NAME;
                Log.d(TAG,"backup path =" + backupDBPath);
                File currentDB = new File(currentDBPath);
                File backupDB = new File( backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            return false;
        }

        return true;
    }


}
