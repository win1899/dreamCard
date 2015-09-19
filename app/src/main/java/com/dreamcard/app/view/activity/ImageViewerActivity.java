package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.utils.ImageViewLoader;

public class ImageViewerActivity extends Activity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        buildUI();
    }

    private void buildUI() {
        img = (ImageView) findViewById(R.id.img_offer_image);
        String imageURL = getIntent().getStringExtra("imageURL");
        AQuery aq = new AQuery(this);
        aq.id(R.id.img_offer_image).progress(R.drawable.loading_progress).image(imageURL, true, true, img.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
