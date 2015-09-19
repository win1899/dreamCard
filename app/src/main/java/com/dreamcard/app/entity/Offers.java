package com.dreamcard.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Moayed on 6/24/2014.
 */
public class Offers implements Parcelable {

    private String id,type,saleOldPrice,saleNewPrice,businessId,validFrom,validationPeriod,title,description
            ,status,city,mobile,phone,fromDate,toDate,currency,numOfLikes,discount,businessName
            ,businessLogo,offerMainPhoto,businessClass;
    private double latitude;
    private double longitude;
    private int position,rating;
    private int offerRating;
    private int ratingCount;
    private String date;
    private String amount;
    private double amountBeforeDicount;
    private double amountAfterDiscount;
    private String categoryIcon;
    private String[] picturesList;

    public Offers() {
    }

    public Offers(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSaleOldPrice() {
        return saleOldPrice;
    }

    public void setSaleOldPrice(String saleOldPrice) {
        this.saleOldPrice = saleOldPrice;
    }

    public String getSaleNewPrice() {
        return saleNewPrice;
    }

    public void setSaleNewPrice(String saleNewPrice) {
        this.saleNewPrice = saleNewPrice;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidationPeriod() {
        return validationPeriod;
    }

    public void setValidationPeriod(String validationPeriod) {
        this.validationPeriod = validationPeriod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(String numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(String businessLogo) {
        this.businessLogo = businessLogo;
    }

    public String getOfferMainPhoto() {
        return offerMainPhoto;
    }

    public void setOfferMainPhoto(String offerMainPhoto) {
        this.offerMainPhoto = offerMainPhoto;
    }

    public String[] getPicturesList() {
        return picturesList;
    }

    public void setPicturesList(String[] picturesList) {
        this.picturesList = picturesList;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getOfferRating() {
        return offerRating;
    }

    public void setOfferRating(int offerRating) {
        this.offerRating = offerRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getAmountBeforeDicount() {
        return amountBeforeDicount;
    }

    public void setAmountBeforeDicount(double amountBeforeDicount) {
        this.amountBeforeDicount = amountBeforeDicount;
    }

    public double getAmountAfterDiscount() {
        return amountAfterDiscount;
    }

    public void setAmountAfterDiscount(double amountAfterDiscount) {
        this.amountAfterDiscount = amountAfterDiscount;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(saleOldPrice);
        parcel.writeString(saleNewPrice);
        parcel.writeString(businessId);
        parcel.writeString(validFrom);
        parcel.writeString(validationPeriod);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeString(city);
        parcel.writeString(mobile);
        parcel.writeString(phone);
        parcel.writeString(fromDate);
        parcel.writeString(toDate);
        parcel.writeString(currency);
        parcel.writeString(numOfLikes);
        parcel.writeString(discount);
        parcel.writeString(businessName);
        parcel.writeString(businessLogo);
        parcel.writeString(offerMainPhoto);
        parcel.writeString(businessClass);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(position);
        parcel.writeInt(rating);
        parcel.writeInt(offerRating);
        parcel.writeInt(ratingCount);
        parcel.writeString(date);
        parcel.writeString(amount);
        parcel.writeDouble(amountBeforeDicount);
        parcel.writeDouble(amountAfterDiscount);
        parcel.writeString(categoryIcon);
        parcel.writeStringArray(picturesList);

    }


    private void readFromParcel(Parcel in) {
        id=in.readString();
        type=in.readString();
        saleOldPrice=in.readString();
        saleNewPrice=in.readString();
        businessId=in.readString();
        validFrom=in.readString();
        validationPeriod=in.readString();
        title=in.readString();
        description=in.readString();
        status=in.readString();
        city=in.readString();
        mobile=in.readString();
        phone=in.readString();
        fromDate=in.readString();
        toDate=in.readString();
        currency=in.readString();
        numOfLikes=in.readString();
        discount=in.readString();
        businessName=in.readString();
        businessLogo=in.readString();
        offerMainPhoto=in.readString();
        businessClass=in.readString();
        latitude=in.readDouble();
        longitude=in.readDouble();
        position=in.readInt();
        rating=in.readInt();
        offerRating=in.readInt();
        ratingCount=in.readInt();
        date=in.readString();
        amount=in.readString();
        amountBeforeDicount=in.readDouble();
        amountAfterDiscount=in.readDouble();
        categoryIcon=in.readString();
//        in.readStringArray(picturesList);
    }


    public static final Creator<Offers> CREATOR = new Creator<Offers>() {
        public Offers createFromParcel(Parcel in) {
            return new Offers(in);
        }

        public Offers[] newArray(int size) {
            return new Offers[size];
        }
    };


}
