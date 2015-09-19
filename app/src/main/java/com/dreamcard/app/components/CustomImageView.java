package com.dreamcard.app.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Moayed on 8/2/2014.
 */
public class CustomImageView extends ImageView {
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();

        if(d!=null){
            // ceil not round - avoid thin vertical gaps along the left/right edges
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
//            if(width>300) {
//                height -= height / 2;
//            }
//            if(height>width){
//                height=width/2;
//            }
//            setMeasuredDimension(width, height);

            float imageSideRatio = (float)d.getIntrinsicWidth() / (float)d.getIntrinsicHeight();
            float viewSideRatio = (float)MeasureSpec.getSize(widthMeasureSpec) / (float)MeasureSpec.getSize(heightMeasureSpec);
            if (imageSideRatio >= viewSideRatio) {
                // Image is wider than the display (ratio)
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = (int)(width / imageSideRatio);
                setMeasuredDimension(width, height);
            } else {
                // Image is taller than the display (ratio)
                int height = MeasureSpec.getSize(heightMeasureSpec);
//                int width = (int)(height * imageSideRatio);
                int width = MeasureSpec.getSize(widthMeasureSpec);
                setMeasuredDimension(width, height);
            }
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
//        try {
//            Drawable drawable = getDrawable();
//
//            if (drawable == null) {
//                setMeasuredDimension(0, 0);
//            } else {
//                float imageSideRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
//                float viewSideRatio = (float) MeasureSpec.getSize(widthMeasureSpec) / (float) MeasureSpec.getSize(heightMeasureSpec);
//                if (imageSideRatio >= viewSideRatio) {
//                    // Image is wider than the display (ratio)
//                    int width = MeasureSpec.getSize(widthMeasureSpec);
//                    int height = (int) (width / imageSideRatio);
//                    setMeasuredDimension(width, height);
//                } else {
//                    // Image is taller than the display (ratio)
//                    int height = MeasureSpec.getSize(heightMeasureSpec);
//                    int width = (int) (height * imageSideRatio);
//                    setMeasuredDimension(width, height);
//                }
//            }
//        } catch (Exception e) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
    }
}
