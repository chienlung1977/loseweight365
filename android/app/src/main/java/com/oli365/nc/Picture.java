package com.oli365.nc;

/**
 * Created by alvinlin on 2016/7/12.
 */
public class Picture {

    Long id;
    String createdate;
    String picturepath;
    String showtime ;
    String status ;

    public Picture(){

    }

    public Picture(long id,String createdate,String picturePath,String showtime,String status){
        this.id =id ;
        this.createdate =createdate;
        this.picturepath = picturePath;
        this.showtime = showtime;
        this.status =status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
