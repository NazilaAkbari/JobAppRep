package com.akbari.myapplication.jobapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.adapter.TimeDetailRecyclerAdapter;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.dao.TimeDao;
import com.akbari.myapplication.jobapp.decoration.CustomDividerItemDecoration;
import com.akbari.myapplication.jobapp.interfaces.OnJobDetailHourListListener;
import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.model.JobTime;
import com.akbari.myapplication.jobapp.model.Time;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

public class JobTimeDetailFragment extends Fragment implements OnJobDetailHourListListener {

    private List<Time> times = new ArrayList<>();
    private TimeDetailRecyclerAdapter mAdapter;
    private Spinner monthSpinner;
    private EditText year;
    private View view;
    private TextView sumHour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_month_hour, container, false);
        Button viewDetail = (Button) view.findViewById(R.id.view_detail);
        monthSpinner = (Spinner) view.findViewById(R.id.month_choose);
        year = (EditText) view.findViewById(R.id.year);
        sumHour = (TextView) view.findViewById(R.id.sum_hour);
        final JobTimeDetailFragment jobTimeDetailFragment = this;
        final TimeDao timeDao = new TimeDao();
        viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDao jobDao = new JobDao();
                Job job = jobDao.findJobById(getContext(),
                        getArguments().getString("jobId"));
                JobTime jobTime = getJobTime(job);
                StringBuilder sumText = new StringBuilder();
                sumText.append("  ");
                int sumMinute = timeDao.getHourInDateRange(getContext(), jobTime);
                int sum = sumMinute / 60;
                sumText.append(sum);
                sumText.append(" ساعت و ");
                int minute = sumMinute % 60;
                sumText.append(minute);
                sumText.append(" دقیقه ");
                sumHour.setText(sumText);
                times.addAll(timeDao.getTimesInDateRange(getContext(), jobTime));
                RecyclerView mRecyclerView = (RecyclerView) view.
                        findViewById(R.id.detailRecycler);
                mRecyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new TimeDetailRecyclerAdapter(times,
                        jobTimeDetailFragment, job);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.addItemDecoration(new
                        CustomDividerItemDecoration(getContext()));
                monthSpinner.setSelection(0);
                year.getText().clear();
            }
        });
        return view;
    }

    private JobTime getJobTime(Job job) {
        int month = (int) monthSpinner.getSelectedItemId();
        int yearEnter = Integer.valueOf(year.getText().toString());
        JobTime jobTime = new JobTime();
        jobTime.setPayDay(job.getPayDay());
        jobTime.setJobName(job.getJobName());
        PersianCalendar calendar = new PersianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, jobTime.getPayDay());
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, yearEnter);
        jobTime.setDateTo(DateUtil.computeDateString(calendar));
        calendar.add(Calendar.MONTH, -1);
        jobTime.setDateFrom(DateUtil.computeDateString(calendar));
        return jobTime;
    }

    @Override
    public void OnSelectRemoveButton() {

    }

    @Override
    public void OnRemoveItem() {

    }

    @Override
    public void OnSelectEditButton() {

    }

    @Override
    public void OnEditItem() {

    }
}
