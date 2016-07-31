package com.oli365.nc;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class Calory {

    public Calory() {

    }

    public Calory(Long id, String createdate, Integer breakfast, Integer lunch, Integer dinner, Integer dessert, Integer sport, String memo) {
        this.id = id;
        this.createdate = createdate;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.dessert = dessert;
        this.sport = sport;
        this.memo = memo;
    }

    Long id;
    String createdate;
    Integer breakfast ;
    Integer lunch;
    Integer dinner;
    Integer dessert;
    Integer sport;
    String memo;

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

    public Integer getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Integer breakfast) {
        this.breakfast = breakfast;
    }

    public Integer getLunch() {
        return lunch;
    }

    public void setLunch(Integer lunch) {
        this.lunch = lunch;
    }

    public Integer getDinner() {
        return dinner;
    }

    public void setDinner(Integer dinner) {
        this.dinner = dinner;
    }

    public Integer getDessert() {
        return dessert;
    }

    public void setDessert(Integer dessert) {
        this.dessert = dessert;
    }

    public Integer getSport() {
        return sport;
    }

    public void setSport(Integer sport) {
        this.sport = sport;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
