package com.dreamcard.app.entity;

/**
 * Created by Moayed on 7/30/2014.
 */
public class MessageInfo {

    private boolean isValid;
    private String value;
    private boolean isSuccess;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
