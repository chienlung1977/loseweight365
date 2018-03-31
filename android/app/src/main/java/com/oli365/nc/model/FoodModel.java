package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/11/28.
 */

public class FoodModel {

    public enum MAIN_FOOD_TYPE{
        BREAKFAST,LUNCH,DINNER,DESSERT,SPORT
    }


    public long Id;
    public  String FoodUid;
    public String FoodId;
    public  String CreateDate;
    public String UserUid;
    public String FoodTypeUid;
    public String FoodName;
    public String Photo;
    public String Calory;
    public String Perunit;
    public String Memo;
    public String Status;
    public String Source;
    public Config.UPLOAD_STATUS UploadStatus;
    public MAIN_FOOD_TYPE MainFoodType;
    public String IsShare;  //是否分享


}
