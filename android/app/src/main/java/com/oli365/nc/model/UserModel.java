package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/11/10.
 * 現在使用者狀態
 */

public class UserModel {


    String weight;      //最後體重
    String bodyFat;     //最後體脂

    public UserModel(String weight, String bodyFat) {
        this.weight = weight;
        this.bodyFat = bodyFat;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }




}
