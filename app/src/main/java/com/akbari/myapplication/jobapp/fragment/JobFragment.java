package com.akbari.myapplication.jobapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.activity.JobActivity;
import com.akbari.myapplication.jobapp.activity.MainActivity;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.dao.TimeDao;
import com.akbari.myapplication.jobapp.dialogFragment.AddJobDialogFragment;
import com.akbari.myapplication.jobapp.dialogFragment.EditJobDialogFragment;
import com.akbari.myapplication.jobapp.interfaces.JobClickListener;
import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.model.JobTime;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobFragment extends Fragment implements JobClickListener {

    private DrawerLayout drawerLayout;
    private String jobId;
    private Job job;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        jobId = getArguments().getString("jobId");
        JobDao jobDao = new JobDao();
        job = jobDao.findJobById(getContext(), jobId);
        setHasOptionsMenu(true);
        setTodayDate(view);
        setMonthTime(view);
        setWeekTime(view);
        setAddButtonActionListener(view);
        setChartData(view);
        setOnClickDrawer(view);
        return view;
    }

    private void setTodayDate(View view) {
        TextView todayText = (TextView) view.findViewById(R.id.todayDate);
        todayText.setText(DateUtil.getCurrentStringPersianDate());
    }

    private void setMonthTime(View view) {
        TextView monthTime = (TextView) view.findViewById(R.id.monthTime);
        TimeDao timeDao = new TimeDao();
        Integer time = timeDao.getThisMonthHour(this.getActivity(), job);
        String timeS = time / 60 + " ساعت ";
        if (time % 60 > 0)
            timeS += "و " + time % 60 + " دقیقه";
        monthTime.setText(timeS);
    }

    private void setWeekTime(View view) {
        TextView weekTime = (TextView) view.findViewById(R.id.weekTime);
        TimeDao timeDao = new TimeDao();
        Integer time = timeDao.getThisWeekHour(this.getActivity(), job);
        String timeS = time / 60 + " ساعت ";
        if (time % 60 > 0)
            timeS += "و " + time % 60 + " دقیقه";
        weekTime.setText(timeS);
    }

    private void setAddButtonActionListener(View view) {
        Button addTimeButton = (Button) view.findViewById(R.id.addTimeBtn);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("jobId", jobId);
                AddTimeFragment addTimeFragment = new AddTimeFragment();
                FragmentManager fm = getFragmentManager();
                addTimeFragment.setArguments(bundle);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.job_holder, addTimeFragment);
                transaction.commit();
            }
        });
    }

    private void setChartData(View view) {
        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        TimeDao timeDao = new TimeDao();
        Map<String, Integer> chartData = timeDao.
                getMonthDailyHourData(getContext(), getJobTime());
        List<String> chartKeys = new ArrayList<>(chartData.keySet());
        Map<Float, String> labelMap = new HashMap<>();
        for (int i = 0; i < chartKeys.size(); i++) {
            labelMap.put((float) i, chartKeys.get(i));
            entries.add(new BarEntry(i, chartData.get(chartKeys.get(i)) / 60));
        }
        BarDataSet set = new BarDataSet(entries, "کارکرد روزانه");
        BarData data = new BarData(set);
        set.setColors(new int[]{
                        R.color.pink, R.color.yellow,
                        R.color.blue, R.color.orange,
                        R.color.purple, R.color.green},
                getContext());
        data.setBarWidth(0.5f);
        chart.setData(data);
        YAxis yAxis = chart.getAxisRight();
        yAxis.setEnabled(false);
        yAxis.setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(labelMap));
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(5);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);
        chart.invalidate();
        chart.setVisibleXRangeMaximum(5f);
    }

    private JobTime getJobTime() {
        JobTime jobTime = new JobTime();
        jobTime.setJobName(job.getJobName());
        jobTime.setPayDay(job.getPayDay());
        jobTime.setDateTo(DateUtil.getCurrentPersianDate());
        return jobTime;
    }

    private void setOnClickDrawer(View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nvView);
        final JobFragment jobFragmentInstance = this;
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addNewJob:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                showDialog();
                                item.setChecked(false);
                                break;
                            case R.id.showJobList:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                openListFragment();
                                item.setChecked(false);
                                break;
                            case R.id.editJobDrawer:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                item.setChecked(false);
                                editJob(jobFragmentInstance);
                                item.setChecked(false);
                                break;
                            case R.id.monthDetailHour:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                openJobDetailHourFragment();
                                item.setChecked(false);
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    private void showDialog() {
        AddJobDialogFragment dialogFragment = new AddJobDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "add");
    }

    private void openListFragment() {
        JobsFragment jobsFragment = new JobsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.replace(R.id.job_holder, jobsFragment);
        tr.commit();
    }

    private void openJobDetailHourFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("jobId", job.getId());
        TimesFragment timesFragment =
                new TimesFragment();
        timesFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.replace(R.id.job_holder, timesFragment);
        tr.commit();
    }

    private void editJob(JobFragment jobFragmentInstance) {
        EditJobDialogFragment editJobDialogFragment =
                new EditJobDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("jobId", jobId);
        editJobDialogFragment.setArguments(bundle);
        editJobDialogFragment.setTargetFragment(jobFragmentInstance, 0);
        editJobDialogFragment.show(getFragmentManager(), "Edit");
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_open_drawer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_drawer:
                if (drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawer(GravityCompat.END);
                else
                    drawerLayout.openDrawer(GravityCompat.END);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OnAddItem(String title, String payDay) {
        Job job = new Job();
        job.setJobName(title);
        job.setPayDay(Integer.valueOf(payDay));
        JobDao jobDao = new JobDao();
        jobDao.addJob(getContext(), job);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnSelectRemoveButton(String title, String payDay, int position) {

    }

    @Override
    public void OnRemoveItem(String title, String payDay, int position) {

    }

    @Override
    public void OnSelectEditButton(String title, String payDay) {

    }

    @Override
    public void OnEditItem(Job job, String oldName) {
        JobDao jobDao = new JobDao();
        jobDao.editJob(getContext(), job);
        jobDao.editJobNameInTimeDb(getContext(), job, oldName);
        ((JobActivity) getActivity()).getSupportActionBar().setTitle(job.getJobName());
    }

    private class XAxisValueFormatter implements IAxisValueFormatter {

        private Map<Float, String> labelMap = new HashMap<>();


        XAxisValueFormatter(Map<Float, String> labelMap) {
            this.labelMap.putAll(labelMap);
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (labelMap.get(value) != null)
                return labelMap.get(value);
            return "";
        }
    }

}
