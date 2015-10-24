package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.view.activity.ImageViewerActivity;


/**
 * Created by WIN on 10/16/2015.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Offers mOffer;


    public ImagePagerAdapter(Context context, Offers offer) {
        mContext = context;
        mOffer = offer;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mOffer.getPicturesList() == null) {
            return 1;
        }
        return mOffer.getPicturesList().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.activity_image_viewer, container, false);
        AQuery aq = new AQuery(mContext);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_offer_image);
        final Intent intent = new Intent(mContext, ImageViewerActivity.class);

        if (mOffer.getPicturesList() == null || mOffer.getPicturesList().length <= 0) {
            aq.id(imageView).image(mOffer.getOfferMainPhoto(), true, true
                    , imageView.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
            intent.putExtra("imageURL", mOffer.getOfferMainPhoto());

        }
        else {
            aq.id(imageView).image(mOffer.getPicturesList()[position], true, true
                    , imageView.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);

            intent.putExtra("imageURL", mOffer.getPicturesList()[position]);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
