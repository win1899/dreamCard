package com.dreamcard.app.components;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Moayed on 3/7/2015.
 */
public class CustomImageViewer extends android.widget.ImageView {

    private int mDrawableWidth;
    private int mDrawableHeight;
    private boolean mAdjustViewBoundsL;
    private int mMaxWidthL = Integer.MAX_VALUE;
    private int mMaxHeightL = Integer.MAX_VALUE;
    private View relatedView;

    public CustomImageViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // hack for acces some private field of parent :-(
        Field f;
        try {
            f = android.widget.ImageView.class.getDeclaredField("mAdjustViewBounds");
            f.setAccessible(true);
            setAdjustViewBounds((Boolean) f.get(this));

            f = android.widget.ImageView.class.getDeclaredField("mMaxWidth");
            f.setAccessible(true);
            setMaxWidth((Integer) f.get(this));

            f = android.widget.ImageView.class.getDeclaredField("mMaxHeight");
            f.setAccessible(true);
            setMaxHeight((Integer) f.get(this));
        } catch (Exception e) {
        }
    }

    public CustomImageViewer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageViewer(Context context) {
        super(context);
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
        mAdjustViewBoundsL = adjustViewBounds;
    }

    public void setMaxWidth(int maxWidth) {
        super.setMaxWidth(maxWidth);
        mMaxWidthL = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        super.setMaxHeight(maxHeight);
        mMaxHeightL = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() == null) {
            setMeasuredDimension(0, 0);
            return;
        }

        mDrawableWidth = getDrawable().getIntrinsicWidth();
        mDrawableHeight = getDrawable().getIntrinsicHeight();

        int w = 0;
        int h = 0;

        // Desired aspect ratio of the view's contents (not including padding)
        float desiredAspect = 0.0f;

        // We are allowed to change the view's width
        boolean resizeWidth = false;

        // We are allowed to change the view's height
        boolean resizeHeight = false;

        if (mDrawableWidth > 0) {
            w = mDrawableWidth;
            h = mDrawableHeight;
            if (w <= 0) w = 1;
            if (h <= 0) h = 1;

            // We are supposed to adjust view bounds to match the aspect
            // ratio of our drawable. See if that is possible.
            if (mAdjustViewBoundsL) {

                int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
                int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

                resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
                resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

                desiredAspect = (float) w / (float) h;
            }
        }

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        if (resizeWidth || resizeHeight) {
			/* If we get here, it means we want to resize to match the
			    drawables aspect ratio, and we have the freedom to change at
			    least one dimension.
			*/

            // Get the max possible width given our constraints
            widthSize = resolveAdjustedSize(w + pleft + pright,
                    mMaxWidthL, widthMeasureSpec);

            // Get the max possible height given our constraints
            heightSize = resolveAdjustedSize(h + ptop + pbottom,
                    mMaxHeightL, heightMeasureSpec);

            if (desiredAspect != 0.0f) {
                // See what our actual aspect ratio is
                float actualAspect = (float) (widthSize - pleft - pright) /
                        (heightSize - ptop - pbottom);

                if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {

                    // Try adjusting width to be proportional to height
                    if (resizeWidth) {
                        int newWidth = (int) (desiredAspect * (heightSize - ptop - pbottom)) + pleft + pright;
                        if (/*newWidth <= widthSize &&*/newWidth > 0) {
                            widthSize = Math.min(newWidth, mMaxWidthL);
                            heightSize = (int) ((widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
                        }
                    }

                    // Try adjusting height to be proportional to width
                    if (resizeHeight) {
                        int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
                        if (/*newHeight <= heightSize && */newHeight > 0) {
                            heightSize = Math.min(newHeight, mMaxHeightL);
                            widthSize = (int) (desiredAspect * (heightSize - ptop - pbottom)) + pleft + pright;
                        }
                    }
                }
            }
        } else {
			/* We are either don't want to preserve the drawables aspect ratio,
			   or we are not allowed to change view dimensions. Just measure in
			   the normal way.
			*/
            w += pleft + pright;
            h += ptop + pbottom;

            w = Math.max(w, getSuggestedMinimumWidth());
            h = Math.max(h, getSuggestedMinimumHeight());

            widthSize = resolveSize(w, widthMeasureSpec);
            heightSize = resolveSize(h, heightMeasureSpec);
        }

        //Log.d(Constants.LOGTAG, mDrawableWidth + ":" +  mDrawableHeight + " to " + widthSize + ":" + heightSize);

        setMeasuredDimension(widthSize, heightSize);

        if (relatedView != null) {
            //Log.i(Constants.LOGTAG, getTag() +  " onMeasure:" +  widthSize + ", " + heightSize + " update size of related view!");
            relatedView.getLayoutParams().width = widthSize;
            relatedView.getLayoutParams().height = heightSize;
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Log.d(Constants.LOGTAG, getTag() +  " onLayout:" +  left + ", " + top + ", " + right + ", " + bottom);
    }

    /**
     * Experimental. This view will be set to the same size as this image.
     */
    public void setRelatedView(View view) {
        relatedView = view;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Log.d(Constants.LOGTAG, getTag() + " onSizeChanged:" +  w + ", " + h + ", " + oldw + ", " + oldh);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
			/* Parent says we can be as big as we want. Just don't be larger
			than max size imposed on ourselves.
			*/
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }
}
