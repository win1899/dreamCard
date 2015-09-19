package com.dreamcard.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Moayed on 6/24/2014.
 */
public class Categories implements Parcelable {
    private String id,title,logo;
    private int position;
    private boolean selected;
    private String parentId;

    public Categories() {
    }

    public Categories(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(logo);
        parcel.writeInt(position);
        parcel.writeByte((byte) (selected ? 1 : 0));
        parcel.writeString(parentId);
    }

    private void readFromParcel(Parcel in) {
        id=in.readString();
        title=in.readString();
        logo=in.readString();
        position=in.readInt();
        selected=in.readByte() != 0;
        parentId=in.readString();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
