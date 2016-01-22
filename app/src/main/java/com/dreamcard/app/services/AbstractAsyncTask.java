package com.dreamcard.app.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by WIN on 9/25/2015.
 */
public abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask {
    private Context context;

    abstract Object doInBackgroundSafe(Object[] objects);
    abstract void onPostExecuteSafe(Object serviceResponse);

    @Override
    protected Object doInBackground(Object[] objects) {
        this.context= (Context) objects[0];
        return doInBackgroundSafe(objects);
    }

    @Override
    protected void onPostExecute(Object serviceResponse) {
        if (((Activity) context).isFinishing()) {
            Log.e(AbstractAsyncTask.class.getName(), "Activity is finishing, ignoring late callback");
            return;
        }
        onPostExecuteSafe(serviceResponse);
    }
}
