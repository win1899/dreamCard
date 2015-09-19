package com.dreamcard.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Moayed on 6/21/2014.
 */
public class SystemOperation {
    public static boolean isOnline(Context activity){
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            if(cm.getActiveNetworkInfo()!=null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Log.d(activity.getClass().getSimpleName(), "Internet is available ");
                    return true;
                }
            }
        }
        return false;
    }
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
