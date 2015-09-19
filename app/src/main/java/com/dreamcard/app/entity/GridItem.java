package com.dreamcard.app.entity;

/**
 * Created by Moayed on 7/15/2014.
 */
public class GridItem {
    protected String title;
    protected String id;
    private String url;

    public GridItem(String url, String title) {
        super();
        this.title = title;
        this.url=url;
    }

    public GridItem(String url, String title, String id) {
        this(url,title);
        this.id = id;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
