package com.dreamcard.app.entity;

/**
 * Created by Moayed on 6/21/2014.
 */
public class ErrorMessageInfo {
    private String status,message;
    private int processId;

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
