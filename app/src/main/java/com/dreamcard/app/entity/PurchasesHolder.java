package com.dreamcard.app.entity;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Moayed on 6/24/2015.
 */
public class PurchasesHolder {

    private TextView txtStoreName;
    private TextView txtCity;
    private TextView txtAmount;
    private ImageView pdfImage;

    public TextView getTxtStoreName() {
        return txtStoreName;
    }

    public void setTxtStoreName(TextView txtStoreName) {
        this.txtStoreName = txtStoreName;
    }

    public TextView getTxtCity() {
        return txtCity;
    }

    public void setTxtCity(TextView txtCity) {
        this.txtCity = txtCity;
    }

    public TextView getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(TextView txtAmount) {
        this.txtAmount = txtAmount;
    }

    public void setPdfImg(ImageView pdfImage) {
        this.pdfImage = pdfImage;
    }

    public ImageView getPdfImg() {
        return pdfImage;
    }
}
