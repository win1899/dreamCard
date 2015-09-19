package com.dreamcard.app.entity;

/**
 * Created by Moayed on 6/16/2015.
 */
public class SearchCriteria {

    private String cityId;
    private String cityName;
    private String discRate;
    private String categories;
    private Integer rate;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDiscRate() {
        return discRate;
    }

    public void setDiscRate(String discRate) {
        this.discRate = discRate;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
