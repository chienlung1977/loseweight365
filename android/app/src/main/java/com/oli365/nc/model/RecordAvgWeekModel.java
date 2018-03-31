package com.oli365.nc.model;

/**
 * Created by alvinlin on 2016/12/7.
 */

public class RecordAvgWeekModel {

    private String ym;
    private String weekofmonth;
    private double avgweight;
    private double avgfatrate;
    private double avgfatweight;

    public RecordAvgWeekModel() {
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getWeekofmonth() {
        return weekofmonth;
    }

    public void setWeekofmonth(String weekofmonth) {
        this.weekofmonth = weekofmonth;
    }

    public double getAvgweight() {
        return avgweight;
    }

    public void setAvgweight(double avgweight) {
        this.avgweight = avgweight;
    }

    public double getAvgfatrate() {
        return avgfatrate;
    }

    public void setAvgfatrate(double avgfatrate) {
        this.avgfatrate = avgfatrate;
    }

    public double getAvgfatweight() {
        return avgfatweight;
    }

    public void setAvgfatweight(double avgfatweight) {
        this.avgfatweight = avgfatweight;
    }
}
