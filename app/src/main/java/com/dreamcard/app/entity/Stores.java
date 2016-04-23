package com.dreamcard.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Moayed on 6/24/2014.
 */
public class Stores implements Parcelable{

    private String id,storeName,address1,address2,city,phone,mobile,website,facebook,representativeName,email
               ,logo,wideLogo,latitude,longitude;
    private int storeClass,position,rating;
    private String ourMessage;
    private String mission;
    private String vision;
    private String[] pictures;
    private double discountPrecentage;
    private String cashPoints = "0";

    public Stores() {
    }

    public void setCashPoints(String cashPoints) {
        this.cashPoints = cashPoints;
    }

    public String getCashPoints() {
        return cashPoints;
    }

    public void setPictures(String[] pics) {
        pictures = pics;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setDiscountPrecentage(Double precentage) {
        discountPrecentage = precentage;
    }

    public double getDiscountPrecentage() {
        return discountPrecentage;
    }

    public Stores(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWideLogo() {
        return wideLogo;
    }

    public void setWideLogo(String wideLogo) {
        this.wideLogo = wideLogo;
    }

    public int getStoreClass() {
        return storeClass;
    }

    public void setStoreClass(int storeClass) {
        this.storeClass = storeClass;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOurMessage() {
        return ourMessage;
    }

    public void setOurMessage(String ourMessage) {
        this.ourMessage = ourMessage;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(storeName);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeString(city);
        parcel.writeString(phone);
        parcel.writeString(mobile);
        parcel.writeString(website);
        parcel.writeString(facebook);
        parcel.writeString(representativeName);
        parcel.writeString(email);
        parcel.writeString(logo);
        parcel.writeString(wideLogo);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeInt(storeClass);
        parcel.writeInt(position);
        parcel.writeInt(rating);
        parcel.writeString(ourMessage);
        parcel.writeString(mission);
        parcel.writeString(vision);
        parcel.writeDouble(discountPrecentage);
        parcel.writeString(cashPoints);
    }


    private void readFromParcel(Parcel in) {
        id=in.readString();
        storeName=in.readString();
        address1=in.readString();
        address2=in.readString();
        city=in.readString();
        phone=in.readString();
        mobile=in.readString();
        website=in.readString();
        facebook=in.readString();
        representativeName=in.readString();
        email=in.readString();
        logo=in.readString();
        wideLogo=in.readString();
        latitude=in.readString();
        longitude=in.readString();
        storeClass=in.readInt();
        position=in.readInt();
        rating=in.readInt();
        ourMessage = in.readString();
        mission = in.readString();
        vision = in.readString();
        discountPrecentage = in.readDouble();
        cashPoints = in.readString();
    }
    public static final Creator<Stores> CREATOR = new Creator<Stores>() {
        public Stores createFromParcel(Parcel in) {
            return new Stores(in);
        }

        public Stores[] newArray(int size) {
            return new Stores[size];
        }
    };

}
