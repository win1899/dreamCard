package com.dreamcard.app.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.dreamcard.app.R;
import com.dreamcard.app.view.interfaces.IDatePickerListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moayed on 6/29/2014.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private IDatePickerListener listener=null;
    private int processId=0;
    private Date time;
    private boolean cancelled = false;

    public DatePickerFragment(IDatePickerListener listener) {
        this.listener=listener;
    }

    public DatePickerFragment(IDatePickerListener listener,int processId, Date time) {
        this.listener=listener;
        this.processId=processId;
        this.time = time;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        if (time == null) {
            Calendar now = Calendar.getInstance();
            c.setTime(now.getTime());
        }
        else {
            c.setTime(time);
        }

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
        d.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelled = true;
                dismiss();
            }
        });
        // Create a new instance of DatePickerDialog and return it
        return d;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date=day+"/"+(month+1)+"/"+year;
        if(this.listener!=null && !cancelled)
            this.listener.setDate(date,this.processId);
    }

    private void updateDate(int day,int month,int year){

    }

}
