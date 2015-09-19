package com.dreamcard.app.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Moayed on 7/15/2014.
 */
public class ItemButton extends GridItem {
    protected int btnImage;
    private Drawable imageDrawable;
    private boolean hasOffer;


    public ItemButton(String url, String title, String id, int btnImage) {
        super(url, title, id);
        this.btnImage = btnImage;
    }
    public ItemButton(String url, String title, String id, int btnImage,boolean isSelected) {
        super(url, title, id);
        this.btnImage = btnImage;
        this.hasOffer=isSelected;
    }
    public ItemButton(String url, String title, String id, int btnImage, Drawable imageDrawable,boolean hasOffer) {
        super(url, title, id);
        this.btnImage = btnImage;
        this.imageDrawable=imageDrawable;
        this.hasOffer=hasOffer;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public int getBtnImage() {
        return btnImage;
    }

    public void setBtnImage(int btnImage) {
        this.btnImage = btnImage;
    }

    public boolean isHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(boolean hasOffer) {
        this.hasOffer = hasOffer;
    }
}
