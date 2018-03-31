package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class SportModel {


    public SportModel(String id, String caloryid, String createdate, String calory, String memo) {
        this.id = id;
        this.caloryid = caloryid;
        this.createdate = createdate;
        this.calory = calory;
        this.memo = memo;
    }

    String id;
    String caloryid;
    String createdate;
    String calory;
    String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaloryid() {
        return caloryid;
    }

    public void setCaloryid(String caloryid) {
        this.caloryid = caloryid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCalory() {
        return calory;
    }

    public void setCalory(String calory) {
        this.calory = calory;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
