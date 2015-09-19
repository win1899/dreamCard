package com.dreamcard.app.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Moayed on 8/9/2014.
 */
public class BusinessComment implements KvmSerializable{

    public Integer BusinessID;
    public String Comment;
    public Integer Consumer;

    public BusinessComment() {
    }

    public BusinessComment(Integer businessID, String comment, Integer consumer) {
        BusinessID = businessID;
        Comment = comment;
        Consumer = consumer;
    }

    public Object getProperty(int arg0) {

        switch(arg0)
        {
            case 0:
                return BusinessID;
            case 1:
                return Comment;
            case 2:
                return Consumer;
        }

        return null;
    }
    public int getPropertyCount() {
        return 4;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index)
        {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BusinessID";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Comment";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Consumer";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Id";
                break;
            default:break;
        }
    }
    public void setProperty(int index, Object value) {
        switch(index)
        {
            case 0:
                BusinessID = (Integer) value;
                break;
            case 1:
                Comment = value.toString();
                break;
            case 2:
                Consumer = (Integer) value;
                break;
            default:
                break;
        }
    }

    public Integer getBusinessID() {
        return BusinessID;
    }

    public void setBusinessID(Integer businessID) {
        BusinessID = businessID;
    }

    public Integer getConsumer() {
        return Consumer;
    }

    public void setConsumer(Integer consumer) {
        Consumer = consumer;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
