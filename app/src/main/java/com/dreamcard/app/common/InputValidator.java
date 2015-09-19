package com.dreamcard.app.common;

import android.widget.EditText;

/**
 * Created by Moayed on 6/8/2014.
 */
public class InputValidator {
    public static boolean isNotEmptyNumber(EditText v, String error) {
        if (v.getText() == null || v.getText().toString().length() <= 0) {
            if (error != null)
                v.setError(error);
            return false;
        }else if(Integer.parseInt(v.getText().toString())==0){
            if (error != null)
                v.setError(error);
            return false;
        }
        return true;
    }
    public static boolean isNotEmpty(EditText v, String error) {
        if (v.getText() == null || v.getText().toString().length() <= 0) {
            if (error != null)
                v.setError(error);
            return false;
        }
        return true;
    }
    public static boolean isPassNotEqualsEmpty(EditText pass,EditText repeatPass, String error) {
        if (!pass.getText().toString().equalsIgnoreCase(repeatPass.getText().toString())) {
            if (error != null)
                repeatPass.setError(error);
            return false;
        }
        return true;
    }
}
