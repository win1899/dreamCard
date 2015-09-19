package com.dreamcard.app.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.dreamcard.app.R;
import com.dreamcard.app.view.interfaces.IDatePickerListener;

import java.util.Calendar;

/**
 * Created by Moayed on 6/29/2014.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private IDatePickerListener listener=null;
    private int processId=0;
    public DatePickerFragment(IDatePickerListener listener) {
        this.listener=listener;
    }
    public DatePickerFragment(IDatePickerListener listener,int processId) {
        this.listener=listener;
        this.processId=processId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog d=new DatePickerDialog(getActivity(), this, year, month, day){
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
//                super.onDateChanged(view, year, month, day);
                String date=day+"/"+(month+1)+"/"+year;
                setTitle(date);
            }
        };
        d.setButton(DatePickerDialog.BUTTON_POSITIVE,getResources().getString(R.string.ok),d);
        d.setButton(DatePickerDialog.BUTTON_NEGATIVE,getResources().getString(R.string.cancel),d);
        // Create a new instance of DatePickerDialog and return it
        return d;

    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date=day+"/"+(month+1)+"/"+year;
        if(this.listener!=null)
            this.listener.setDate(date,this.processId);
    }
    private void updateDate(int day,int month,int year){

    }

}
