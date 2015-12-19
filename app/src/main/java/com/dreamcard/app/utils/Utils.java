package com.dreamcard.app.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.dreamcard.app.constants.Params;

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
}
