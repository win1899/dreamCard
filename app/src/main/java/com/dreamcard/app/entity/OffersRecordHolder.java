package com.dreamcard.app.entity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamcard.app.components.CustomImageView;
import com.dreamcard.app.components.CustomImageViewer;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Moayed on 7/31/2014.
 */
public class OffersRecordHolder {

    private TextView txtPrice;
    private ImageView imgOffer;
    private String id;
    private Button button;
    private ImageView imgStoreLogo;
    private RelativeLayout pnl;
    private TextView txtDiscount;
    private TextView txtNumOfLikes;
    private TextView txtLikeBtn;
    private ImageView imgClass;
    private TextView txtName;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private View view;
    private LinearLayout ratingBar;
    private ImageView imgCategory;
    private ImageView imgCategoryBg;
    private SimpleDraweeView mainImage;
    private SimpleDraweeView storeImage;
    private TextView txtOldPrice;
    private TextView txtDesc;

    public TextView getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(TextView txtPrice) {
        this.txtPrice = txtPrice;
    }

    public ImageView getImgOffer() {
        return imgOffer;
    }

    public void setImgOffer(ImageView imgOffer) {
        this.imgOffer = imgOffer;
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

    public ImageView getImgStoreLogo() {
        return imgStoreLogo;
    }

    public void setImgStoreLogo(ImageView imgStoreLogo) {
        this.imgStoreLogo = imgStoreLogo;
    }

    public RelativeLayout getPnl() {
        return pnl;
    }

    public void setPnl(RelativeLayout pnl) {
        this.pnl = pnl;
    }

    public TextView getTxtDiscount() {
        return txtDiscount;
    }

    public void setTxtDiscount(TextView txtDiscount) {
        this.txtDiscount = txtDiscount;
    }

    public TextView getTxtNumOfLikes() {
        return txtNumOfLikes;
    }

    public void setTxtNumOfLikes(TextView txtNumOfLikes) {
        this.txtNumOfLikes = txtNumOfLikes;
    }

    public TextView getTxtLikeBtn() {
        return txtLikeBtn;
    }

    public void setTxtLikeBtn(TextView txtLikeBtn) {
        this.txtLikeBtn = txtLikeBtn;
    }

    public ImageView getImgClass() {
        return imgClass;
    }

    public void setImgClass(ImageView imgClass) {
        this.imgClass = imgClass;
    }

    public TextView getTxtName() {
        return txtName;
    }

    public void setTxtName(TextView txtName) {
        this.txtName = txtName;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public LinearLayout getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(LinearLayout ratingBar) {
        this.ratingBar = ratingBar;
    }

    public ImageView getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(ImageView imgCategory) {
        this.imgCategory = imgCategory;
    }

    public ImageView getImgCategoryBg() {
        return imgCategoryBg;
    }

    public void setImgCategoryBg(ImageView imgCategoryBg) {
        this.imgCategoryBg = imgCategoryBg;
    }

    public SimpleDraweeView getMainImage() {
        return mainImage;
    }

    public void setMainImage(SimpleDraweeView mainImage) {
        this.mainImage = mainImage;
    }

    public SimpleDraweeView getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(SimpleDraweeView storeImage) {
        this.storeImage = storeImage;
    }

    public TextView getTxtOldPrice() {
        return txtOldPrice;
    }

    public void setTxtOldPrice(TextView txtOldPrice) {
        this.txtOldPrice = txtOldPrice;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public void setTxtDesc(TextView txtDesc) {
        this.txtDesc = txtDesc;
    }
}
