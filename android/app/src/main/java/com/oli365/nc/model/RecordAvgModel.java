package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/12/7.
 * 平均值的資料
 */

public class RecordAvgModel {

    private String createTime;
    private double weight;
    private double fatRate;
    private double fatweight;

    public RecordAvgModel() {
    }

    public RecordAvgModel(String createTime, double weight, double fatRate, double fatweight) {
        this.createTime = createTime;
        this.weight = weight;
        this.fatRate = fatRate;
        this.fatweight = fatweight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getFatRate() {
        return fatRate;
    }

    public void setFatRate(double fatRate) {
        this.fatRate = fatRate;
    }

    public double getFatweight() {
        return fatweight;
    }

    public void setFatweight(double fatweight) {
        this.fatweight = fatweight;
    }
}
