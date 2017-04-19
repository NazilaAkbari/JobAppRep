package com.akbari.myapplication.jobapp.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.activity.JobActivity;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.dao.TimeDao;
import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.model.Time;
import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 11/19/2016
 */

public class AddTimeFragment extends Fragment {

    private EditText date, enterTime, exitTime;
    private TimePickerDialog enterTimePicker, exitTimePicker;
    private TextInputLayout timeStartLayout, timeEndLayout, dateLayout;
    private Button addButton;
    private Job job;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_time, container, false);
        setHasOptionsMenu(true);
        setDateField(view);
        setEnterTimeField(view);
        setExitTimeField(view);
        setAddButtonListener(view);
        JobDao jobDao = new JobDao();
        job = jobDao.findJobById(getContext(), getArguments().getString("jobId"));
        return view;
    }


    private void setDateField(View view) {
        dateLayout = (TextInputLayout) view.findViewById(R.id.input_layout_date);
        date = (EditText) view.findViewById(R.id.dateOfTime);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == date)
                    new DatePicker.Builder()
                            .id(R.id.dateOfTime)
                            .date(new GregorianCalendar())
                            .build(new DateSetListener() {
                                @Override
                                public void onDateSet(int id, @Nullable Calendar calendar,
                                                      int day, int month, int year) {
                                    Calendar newDate = Calendar.getInstance();
                                    newDate.set(year, month - 1, day);
                                    SimpleDateFormat simpleDateFormat =
                                            new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                    date.setText(simpleDateFormat.format(newDate.getTime()));
                                }
                            })
                            .show(getFragmentManager(), "");
            }
        });
        date.addTextChangedListener(new MyTextWatcher(date));
    }

    private void setEnterTimeField(View view) {
        timeStartLayout = (TextInputLayout) view.findViewById(R.id.input_layout_timeStart);
        enterTime = (EditText) view.findViewById(R.id.timeStart);
        enterTime.setInputType(InputType.TYPE_NULL);
        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == enterTime)
                    enterTimePicker.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        enterTimePicker = new TimePickerDialog(this.getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                        enterTime.setText(simpleDateFormat.format(newTime.getTime()));
                    }
                }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        enterTime.addTextChangedListener(new MyTextWatcher(enterTime));

    }

    private void setExitTimeField(View view) {
        timeEndLayout = (TextInputLayout) view.findViewById(R.id.input_layout_timeExit);
        exitTime = (EditText) view.findViewById(R.id.timeExit);
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
        exitTime.addTextChangedListener(new MyTextWatcher(exitTime));
    }

    private void setAddButtonListener(View view) {
        addButton = (Button) view.findViewById(R.id.btnAddTime);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterTimeText = enterTime.getText().toString().trim();
                String exitTimeText = exitTime.getText().toString().trim();
                String dateText = date.getText().toString().trim();
                if (enterTimeText.trim().isEmpty()) {
                    timeStartLayout.setError(getString(R.string.time_enter_error_empty));
                    requestFocus(enterTime);
                    return;
                }
                if (exitTimeText.trim().isEmpty()) {
                    timeEndLayout.setError(getString(R.string.time_exit_error_empty));
                    requestFocus(exitTime);
                    return;
                }
                if (dateText.trim().isEmpty()) {
                    dateLayout.setError(getString(R.string.date_error_empty));
                    requestFocus(date);
                    return;
                }
                if (!validateEnterTime() || !validateExitTime() || !validateDate())
                    return;
                Time time = new Time();
                time.setJobName(job.getJobName());
                time.setEnterTime(enterTime.getText().toString());
                time.setExitTime(exitTime.getText().toString());
                time.setDate(date.getText().toString());
                TimeDao timeDao = new TimeDao();
                try {
                    timeDao.AddTime(getContext(), time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getContext(), JobActivity.class);
                intent.putExtra("jobId", job.getId());
                startActivity(intent);
                enterTime.setText("");
                exitTime.setText("");
                date.setText("");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_open_drawer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.open_drawer:
                Intent intent = new Intent(getActivity(), JobActivity.class);
                intent.putExtra("jobId", job.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateEnterTime() {
        String enterTimeText = enterTime.getText().toString().trim();
        String timeRegex = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        if (!enterTimeText.matches(timeRegex)) {
            timeStartLayout.setError(getString(R.string.time_enter_error));
            requestFocus(enterTime);
            return false;
        } else {
            timeStartLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateExitTime() {
        String exitTimeText = exitTime.getText().toString().trim();
        String enterTimeText = enterTime.getText().toString().trim();
        String timeRegex = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            if (!exitTimeText.matches(timeRegex)) {
                timeEndLayout.setError(getString(R.string.time_exit_error));
                requestFocus(exitTime);
                return false;
            } else if (sdf.parse(enterTimeText).after(sdf.parse(exitTimeText))) {
                timeStartLayout.setError(getString(R.string.enter_before_exit_error));
                requestFocus(enterTime);
                return false;
            } else {
                timeEndLayout.setErrorEnabled(false);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateDate() {
        String dateText = date.getText().toString().trim();
        String timeRegex = "^((13|14)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])$";
        if (!dateText.matches(timeRegex)) {
            dateLayout.setError(getString(R.string.date_error));
            requestFocus(date);
            return false;
        } else {
            dateLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().
                    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.timeStart:
                    validateEnterTime();
                    break;
                case R.id.timeExit:
                    validateExitTime();
                    break;
                case R.id.date:
                    validateDate();
                    break;
            }
        }
    }
}
