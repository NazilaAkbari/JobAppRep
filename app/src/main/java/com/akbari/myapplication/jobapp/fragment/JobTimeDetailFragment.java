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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_month_hour, container, false);
        Button viewDetail = (Button) view.findViewById(R.id.view_detail);
        monthSpinner = (Spinner) view.findViewById(R.id.month_choose);
        year = (EditText) view.findViewById(R.id.year);
        TimeDao timeDao = new TimeDao();


        viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month = (int) monthSpinner.getSelectedItemId();
                int yearEnter = Integer.valueOf(year.getText().toString());
                JobDao jobDao = new JobDao();
                Job job = jobDao.findJobById(getContext(), getArguments().getString("jobId"));
                JobTime jobTime = new JobTime();
                jobTime.setPayDay(job.getPayDay());
                jobTime.setJobName(job.getJobName());
                PersianCalendar calendar = new PersianCalendar();
                calendar.set(Calendar.DAY_OF_MONTH, jobTime.getPayDay());
                calendar.set(Calendar.MONTH, calendar.get(month) + 1);
                calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                System.out.println(DateUtil.computeDateString(calendar)+"!!!!");
                jobTime.setDateTo(DateUtil.computeDateString(calendar));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                System.out.println(DateUtil.computeDateString(calendar)+"!!!!");
                System.out.println(Calendar.YEAR+"!!!!");
                jobTime.setDateFrom(DateUtil.computeDateString(calendar));
            }
        });

       /* times.addAll(timeDao.getMonthTimes(getContext(), job));
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.detailRecycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TimeDetailRecyclerAdapter(times, this, job);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(getContext()));*/
        return view;
    }

    @Override
    public void OnRemoveItem() {

    }

    @Override
    public void OnEditItem() {

    }
}
