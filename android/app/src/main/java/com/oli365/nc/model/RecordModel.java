package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/3/31.
 * 使用者新的記錄
 */
public class RecordModel {

    public long Id;

    public String RecordUid;
    public String CreateDate;
    public String UserUid;
    public double Weight;
    public double FatRate;
    public String  FatWeight;
    public double BoneWeight;
    public double BodyAge;
    public double InsideFat;
    public double MuscleWeight;
    public double BodyWater;
    public double Metabolism;
    public String Photo;
    public String UploadStatus;
    public String Status;
    public String Memo;
    public String CalculateMetabolism;  //計算的基礎代謝率
    public String CalculateExpenditure;  //計算的最低熱量




 /*
    public RecordModel(){}

    public RecordModel(long id, String createTime, double weight, double fatRate, double boneweight, double bodyage, double insidefat, double muscleweight, double musclerate, double metabolism, String photo, String upload, String status, String email, String uid, String memo) {
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
        this.upload = upload;
        this.status = status;
        this.email = email;
        this.uid = uid;
        this.memo = memo;
    }


    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    //region 計算欄位

    //計算脂肪重量
    public String getFatWeight(){
        Double fatweight = Double.valueOf( getWeight()) *  Double.valueOf( getFatRate()) * 0.01;
        DecimalFormat df = new DecimalFormat("#0.#");
        return String.valueOf(df.format(fatweight));
    }


    //end region
*/


}
