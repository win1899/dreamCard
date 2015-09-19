package com.dreamcard.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Moayed on 8/15/2014.
 */
public class LocationInfo implements Parcelable {

    private String name;
    private double latitude,longitude;

    public LocationInfo() {
    }

    public LocationInfo(Parcel in) {
        readFromParcel(in);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);

    }
    private void readFromParcel(Parcel in) {
        name=in.readString();
        latitude=in.readDouble();
        longitude=in.readDouble();
    }


    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
}
