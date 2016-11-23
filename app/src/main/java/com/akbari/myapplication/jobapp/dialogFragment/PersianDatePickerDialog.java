package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 11/23/2016
 */

public class PersianDatePickerDialog extends DatePickerDialog {

    public PersianDatePickerDialog(Context context, OnDateSetListener callBack,
                                   int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }
}
