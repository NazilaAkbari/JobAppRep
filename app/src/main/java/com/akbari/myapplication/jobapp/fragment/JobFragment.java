package com.akbari.myapplication.jobapp.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.TimeDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class JobFragment extends Fragment {
    private EditText date;
    private DatePickerDialog datePicker;
    private EditText enterTime;
    private TimePickerDialog enterTimePicker;
    private EditText exitTime;
    private TimePickerDialog exitTimePicker;
    private TextView monthTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        date = (EditText) view.findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        setDateField();
        enterTime = (EditText) view.findViewById(R.id.timeStart);
        enterTime.setInputType(InputType.TYPE_NULL);
        setEnterTimeField();
        exitTime = (EditText) view.findViewById(R.id.timeExit);
        setExitTimeField();
        monthTime = (TextView) view.findViewById(R.id.monthTime);
        TimeDao timeDao = new TimeDao();
        String time = timeDao.getMonthTime(this.getActivity(),
                getArguments().getString("payDay"),
                getArguments().getString("selectedJob")
        ).toString();
        monthTime.setText(time);
        return view;
    }

    private void setDateField() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == date)
                    datePicker.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                date.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setEnterTimeField() {
        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == enterTime)
                    enterTimePicker.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        enterTimePicker = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                enterTime.setText(simpleDateFormat.format(newTime.getTime()));
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

    }

    private void setExitTimeField() {
        exitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == exitTime)
                    exitTimePicker.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        exitTimePicker = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                exitTime.setText(simpleDateFormat.format(newTime.getTime()));
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
    }

}
