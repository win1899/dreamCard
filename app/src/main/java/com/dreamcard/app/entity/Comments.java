package com.dreamcard.app.entity;

/**
 * Created by Moayed on 6/25/2014.
 */
public class Comments {

    private String id,comment,date;
    private String Consumer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConsumer() {
        return Consumer;
    }

    public void setConsumer(String consumer) {
        Consumer = consumer;
    }
}
