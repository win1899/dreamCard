package com.dreamcard.app.entity;

/**
 * Created by Moayed on 6/25/2014.
 */
public class ConsumerInfo {

    private String totalSaving,numOfLikes;
    private int offerCode;

    public String getTotalSaving() {
        return totalSaving;
    }

    public void setTotalSaving(String totalSaving) {
        this.totalSaving = totalSaving;
    }

    public String getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(String numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public int getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(int offerCode) {
        this.offerCode = offerCode;
    }
}
