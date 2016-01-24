package com.dreamcard.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by WIN on 12/19/2015.
 */
public class Utils {

    public static String getUserName(final Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String name = prefs.getString(Params.USER_INFO_NAME, "");
        String firstName = prefs.getString(Params.USER_INFO_FIRST_NAME, "");
        String lastName = prefs.getString(Params.USER_INFO_LAST_NAME, "");
        String fullName = prefs.getString(Params.USER_INFO_FULL_NAME, "");

        if (!"null".equalsIgnoreCase(name) && !"".equalsIgnoreCase(name)) {
            return name;
        }
        if (!"null".equalsIgnoreCase(fullName) && !"".equalsIgnoreCase(fullName)) {
            return fullName;
        }
        if (!"null".equalsIgnoreCase(firstName) && !"".equalsIgnoreCase(firstName)) {
            if (!"null".equalsIgnoreCase(lastName) && !"".equalsIgnoreCase(lastName)) {
                return firstName + " " + lastName;
            }
            return firstName;
        }
        return "";
    }

    public static void loadImage(Context context, String url, ImageView imageView, int maxWidthDp, int maxHeightDp, boolean convert) {
        int maxWidth = maxWidthDp;
        int maxHeight = maxHeightDp;
        if (convert) {
            maxWidth = dpToPx(maxWidthDp, context.getResources().getDisplayMetrics());
            maxHeight = dpToPx(maxHeightDp, context.getResources().getDisplayMetrics());
        }

        ImageLoader loader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        loader.get(url, ImageLoader.getImageListener(imageView, R.drawable.error_loading_photo, R.drawable.error_loading_photo),
                maxWidth, maxHeight, ImageView.ScaleType.CENTER_INSIDE);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        ImageLoader loader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        loader.get(url, ImageLoader.getImageListener(imageView, R.drawable.error_loading_photo, R.drawable.error_loading_photo));
    }

    public static int dpToPx(int dp, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static SoapObject fixLocale(SoapObject request) {
        SoapObject newRequest = new SoapObject(request.getNamespace(), request.getName());

        for (int i = 0; i < request.getPropertyCount(); i++) {
            PropertyInfo info = (PropertyInfo) request.getProperty(i);
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(info.getName());
            propertyInfo.setType(info.getType());
            propertyInfo.setValue(toEnUs(info.getValue().toString()));

            newRequest.addProperty(propertyInfo);
        }

        return newRequest;
    }

    public static Object toEnUs(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            String val = obj.toString();
            return val.replaceAll("١", "1").replaceAll("٢", "2").replaceAll("٣", "3").replaceAll("٤", "4").replaceAll("٥", "5").replaceAll("٦", "6").replaceAll("٧", "7").replaceAll("٨", "8").replaceAll("٩", "9").replaceAll("٠", "0");
        }
        catch (Exception e) {
            return null;
        }
    }
}
