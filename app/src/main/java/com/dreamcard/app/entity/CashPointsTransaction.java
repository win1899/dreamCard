package com.dreamcard.app.entity;

import java.io.Serializable;

/**
 * Created by WIN on 6/11/2016.
 */
public class CashPointsTransaction implements Serializable{

    private String id;
    private int businessId;
    private String consumerId;
    private double pointsValue;
    private String status;
    private String date;
    private String offerUsageId;
    private String storeName;
    private String storeLogo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public double getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(double pointsValue) {
        this.pointsValue = pointsValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOfferUsageId() {
        return offerUsageId;
    }

    public void setOfferUsageId(String offerUsageId) {
        this.offerUsageId = offerUsageId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

}
