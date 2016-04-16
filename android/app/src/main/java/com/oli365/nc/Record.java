package com.oli365.nc;

/**
 * Created by alvinlin on 2016/3/31.
 */
public class Record {

    private long id;
    private String createTime;
    private double weight;
    private double fatRate;

    public Record(){}

    public Record(long id,String createTime,double weight,double fatRate){
        this.id =id;
        this.createTime=createTime;
        this.weight=weight;
        this.fatRate=fatRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getFatRate() {
        return fatRate;
    }

    public void setFatRate(double fatRate) {
        this.fatRate = fatRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
