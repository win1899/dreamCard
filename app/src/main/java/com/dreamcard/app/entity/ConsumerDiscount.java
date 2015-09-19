package com.dreamcard.app.entity;

/**
 * Created by Moayed on 9/6/2014.
 */
public class ConsumerDiscount {

    private String amount,date;
    private String businessName;
    private double amountBeforeDiscount;
    private double amountAfterDiscount;
    private String logo;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getAmountBeforeDiscount() {
        return amountBeforeDiscount;
    }

    public void setAmountBeforeDiscount(double amountBeforeDiscount) {
        this.amountBeforeDiscount = amountBeforeDiscount;
    }

    public double getAmountAfterDiscount() {
        return amountAfterDiscount;
    }

    public void setAmountAfterDiscount(double amountAfterDiscount) {
        this.amountAfterDiscount = amountAfterDiscount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
