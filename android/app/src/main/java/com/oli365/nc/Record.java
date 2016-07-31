package com.oli365.nc;

/**
 * Created by alvinlin on 2016/3/31.
 */
public class Record {

    private long id;
    private String createTime;
    private double weight;
    private double fatRate;
    private double boneweight;
    private double bodyage;
    private double insidefat;
    private double muscleweight;
    private double musclerate;
    private double metabolism;
    private String photo;

    public Record(){}

    public Record(long id, String createTime, double weight, double fatRate, double boneweight, double bodyage, double insidefat, double muscleweight, double musclerate, double metabolism,String photo) {
        this.id = id;
        this.createTime = createTime;
        this.weight = weight;
        this.fatRate = fatRate;
        this.boneweight = boneweight;
        this.bodyage = bodyage;
        this.insidefat = insidefat;
        this.muscleweight = muscleweight;
        this.musclerate = musclerate;
        this.metabolism = metabolism;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public double getBoneweight() {
        return boneweight;
    }

    public void setBoneweight(double boneweight) {
        this.boneweight = boneweight;
    }

    public double getBodyage() {
        return bodyage;
    }

    public void setBodyage(double bodyage) {
        this.bodyage = bodyage;
    }

    public double getInsidefat() {
        return insidefat;
    }

    public void setInsidefat(double insidefat) {
        this.insidefat = insidefat;
    }

    public double getMuscleweight() {
        return muscleweight;
    }

    public void setMuscleweight(double muscleweight) {
        this.muscleweight = muscleweight;
    }

    public double getMusclerate() {
        return musclerate;
    }

    public void setMusclerate(double musclerate) {
        this.musclerate = musclerate;
    }

    public double getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(double metabolism) {
        this.metabolism = metabolism;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
