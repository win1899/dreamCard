package com.dreamcard.app.entity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Moayed on 7/15/2014.
 */
public class RecordHolder {

    TextView txtTitle;
    ImageView imageItem;
    String id;
    Button button;
    private ImageView imgSelected;
    private RelativeLayout pnl;
    private RelativeLayout pnl2;
    private TextView txtUser;

    @Override
    public String toString() {
        return "RecordHolder{" +
                "txtTitle=" + txtTitle.getText() +
                ", imageItem=" + imageItem +
                ", id=" + id +
                '}';
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(TextView txtTitle) {
        this.txtTitle = txtTitle;
    }

    public ImageView getImageItem() {
        return imageItem;
    }

    public void setImageItem(ImageView imageItem) {
        this.imageItem = imageItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ImageView getImgSelected() {
        return imgSelected;
    }

    public void setImgSelected(ImageView imgSelected) {
        this.imgSelected = imgSelected;
    }

    public RelativeLayout getPnl() {
        return pnl;
    }

    public void setPnl(RelativeLayout pnl) {
        this.pnl = pnl;
    }

    public RelativeLayout getPnl2() {
        return pnl2;
    }

    public void setPnl2(RelativeLayout pnl2) {
        this.pnl2 = pnl2;
    }

    public TextView getTxtUser() {
        return txtUser;
    }

    public void setTxtUser(TextView txtUser) {
        this.txtUser = txtUser;
    }
}
