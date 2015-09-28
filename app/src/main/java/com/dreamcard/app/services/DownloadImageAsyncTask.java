package com.dreamcard.app.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.dreamcard.app.components.CustomImageViewer;
import com.dreamcard.app.view.activity.TestActivity;

import java.io.InputStream;

/**
 * Created by Moayed on 3/7/2015.
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private TestActivity.ImageListener listener;

    public DownloadImageAsyncTask(ImageView bmImage, TestActivity.ImageListener listener) {
        this.bmImage = bmImage;
        this.listener=listener;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        this.listener.listener();
    }
}
