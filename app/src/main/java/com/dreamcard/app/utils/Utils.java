package com.dreamcard.app.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Locale;

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

    public static void updateStoreBadge (Context context, int value) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        int oldStoreBadge = prefs.getInt(Params.STORE_BADGE_COUNT, 0);
        int newStoreBadge;
        if (value == 0) {
            newStoreBadge = 0;
        }
        else
            newStoreBadge= oldStoreBadge + 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Params.STORE_BADGE_COUNT,newStoreBadge);
        editor.commit();

        updateMainBadge(context);
    }

    public static void updateNotificationBadge(Context context, int value) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        int oldVal = prefs.getInt(Params.NOTIFICATION_BADGE_COUNT, 0);
        int newVal;
        if (value == 0) {
            newVal = 0;
        }
        else {
            newVal = oldVal + 1;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Params.NOTIFICATION_BADGE_COUNT, newVal);
        editor.commit();
        updateMainBadge(context);

    }

    public static void updateOffersBadge (Context context, int value) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        int oldOffersBagde = prefs.getInt(Params.OFFERS_BADGE_COUNT, 0);
        int newOffersBadge;
        if (value == 0)
            newOffersBadge = 0;
        else
            newOffersBadge = oldOffersBagde + 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Params.OFFERS_BADGE_COUNT, newOffersBadge);
        editor.commit();
        updateMainBadge(context);

    }

    public static int getStoreBadge (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        return prefs.getInt(Params.STORE_BADGE_COUNT,0);
    }

    public static int getOffersBadge (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        return prefs.getInt(Params.OFFERS_BADGE_COUNT,0);
    }

    public static int getNotificationBadge(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        return prefs.getInt(Params.NOTIFICATION_BADGE_COUNT, 0);
    }

    private static void updateMainBadge(Context context) {

        String manufactureStr = Build.MANUFACTURER;

        if (manufactureStr != null) {

            boolean bool2 = manufactureStr.toLowerCase(Locale.US).contains("htc");
            boolean bool3 = manufactureStr.toLowerCase(Locale.US).contains("sony");
            boolean bool4 = manufactureStr.toLowerCase(Locale.US).contains("samsung");

            // Sony Ericssion
            if (bool3) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", "com.dreamcard.app.view.activity.SplashActivity");
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
                    int storeBadge = Utils.getStoreBadge(context);
                    int offersBadge = Utils.getOffersBadge(context);
                    int notificationBage = Utils.getNotificationBadge(context);
                    int badgeCount = storeBadge + offersBadge + notificationBage;
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", badgeCount);
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", "com.dreamcard.app");

                    context.sendBroadcast(intent);
                } catch (Exception localException) {
                    Log.e("CHECK", "Sony : " + localException.getLocalizedMessage());
                }
            }

            // HTC
            if (bool2) {
                try {
                    Intent localIntent1 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
                    localIntent1.putExtra("packagename", "com.dreamcard.app");
                    int s = Utils.getStoreBadge(context);
                    int o = Utils.getOffersBadge(context);
                    int badgeCount = s + o;
                    localIntent1.putExtra("count", badgeCount);
                    context.sendBroadcast(localIntent1);

                    Intent localIntent2 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
                    ComponentName localComponentName = new ComponentName(context, "com.dreamcard.app.view.activity.SplashActivity");
                    localIntent2.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
                    localIntent2.putExtra("com.htc.launcher.extra.COUNT", 10);
                    context.sendBroadcast(localIntent2);
                } catch (Exception localException) {
                    Log.e("CHECK", "HTC : " + localException.getLocalizedMessage());
                }
            }
            if (bool4) {
                // Samsung
                try {
                    ContentResolver localContentResolver = context.getContentResolver();
                    Uri localUri = Uri.parse("content://com.sec.badge/apps");
                    ContentValues localContentValues = new ContentValues();
                    localContentValues.put("package", "com.dreamcard.app");
                    localContentValues.put("class", "com.dreamcard.app.view.activity.SplashActivity");
                    int s = Utils.getStoreBadge(context);
                    int o = Utils.getOffersBadge(context);
                    int badgeCount = s + o;
                    localContentValues.put("badgecount", badgeCount);
                    String str = "package=? AND class=?";
                    String[] arrayOfString = new String[2];
                    arrayOfString[0] = "com.dreamcard.appe";
                    arrayOfString[1] = "com.dreamcard.app.view.activity.SplashActivity";

                    int update = localContentResolver.update(localUri, localContentValues, str, arrayOfString);

                    if (update == 0) {
                        localContentResolver.insert(localUri, localContentValues);
                    }

                } catch (IllegalArgumentException localIllegalArgumentException) {
                    Log.e("CHECK", "Samsung1F : " + localIllegalArgumentException.getLocalizedMessage());
                } catch (Exception localException) {
                    Log.e("CHECK", "Samsung : " + localException.getLocalizedMessage());
                }
            }
        }
    }

    public static void loadImage(Context context, String url, ImageView imageView, int maxWidthDp, int maxHeightDp, boolean convert) {
        int maxWidth = maxWidthDp;
        int maxHeight = maxHeightDp;
        if (convert) {
            maxWidth = dpToPx(maxWidthDp, context.getResources().getDisplayMetrics());
            maxHeight = dpToPx(maxHeightDp, context.getResources().getDisplayMetrics());
        }

        try {
            ImageLoader loader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
            loader.get(url, ImageLoader.getImageListener(imageView, R.drawable.error_loading_photo, R.drawable.error_loading_photo),
                    maxWidth, maxHeight, ImageView.ScaleType.CENTER_INSIDE);
        } catch (OutOfMemoryError e) {

        } catch (Exception e) {

        }
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        try {
            ImageLoader loader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
            loader.get(url, ImageLoader.getImageListener(imageView, R.drawable.error_loading_photo, R.drawable.error_loading_photo));
        } catch (OutOfMemoryError e) {

        } catch (Exception e) {

        }
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
