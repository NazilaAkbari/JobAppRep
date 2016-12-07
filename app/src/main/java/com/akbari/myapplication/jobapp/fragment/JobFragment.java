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
import com.akbari.myapplication.jobapp.interfaces.OnListListener;
import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.model.JobTime;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobFragment extends Fragment implements OnListListener {

    private DrawerLayout drawerLayout;
    private String jobTitle;
    private String payDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        payDay = getArguments().getString("payDay");
        jobTitle = getArguments().getString("selectedJob");
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
        Integer time = timeDao.getThisMonthHour(this.getActivity(),
                payDay, jobTitle);
        String timeS = time / 60 + " ساعت ";
        if (time % 60 > 0)
            timeS += "و " + time % 60 + " دقیقه";
        monthTime.setText(timeS);
    }

    private void setWeekTime(View view) {
        TextView weekTime = (TextView) view.findViewById(R.id.weekTime);
        TimeDao timeDao = new TimeDao();
        Integer time = timeDao.getThisWeekHour(this.getActivity(),
                payDay, jobTitle);
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
                Intent intent = new Intent(getActivity(), JobActivity.class);
                intent.putExtra("selectedJob", jobTitle);
                intent.putExtra("payDay", payDay);
                intent.putExtra("addTime", "true");
                startActivity(intent);
            }
        });
    }

    private void setChartData(View view) {
        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        TimeDao timeDao = new TimeDao();
        Map<Integer, Integer> chartData = timeDao.
                getMonthDailyHourData(getContext(), getJobTime());
        for (Map.Entry<Integer, Integer> entry : chartData.entrySet()
                ) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue() / 60));
        }
        BarDataSet set = new BarDataSet(entries, "کارکرد روزانه");
        BarData data = new BarData(set);
        set.setColors(new int[]{R.color.pink, R.color.yellow,
                        R.color.lightBlue, R.color.lightGreen,
                        R.color.purple},
                getContext());
        data.setBarWidth(0.5f);
        chart.setHorizontalScrollBarEnabled(true);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate();
    }

    private JobTime getJobTime() {
        JobTime jobTime = new JobTime();
        jobTime.setJobName(jobTitle);
        jobTime.setPayDay(Integer.valueOf(payDay));
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
                                return true;
                            case R.id.annualReport:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                AnnualReportFragment fragment = new AnnualReportFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.job_holder, fragment);
                                transaction.commit();
                                return true;
                            case R.id.showJobList:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                ListFragment listFragment = new ListFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction tr = fragmentManager.beginTransaction();
                                tr.replace(R.id.job_holder, listFragment);
                                tr.commit();
                                return true;
                            case R.id.editJobDrawer:
                                item.setChecked(true);
                                drawerLayout.closeDrawers();
                                item.setChecked(false);
                                EditJobDialogFragment editJobDialogFragment =
                                        new EditJobDialogFragment();
                                JobDao jobDao = new JobDao();
                                Job job = jobDao
                                        .findJobIdByTitleAndPayDay(getContext(), jobTitle, payDay);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", job.getId());
                                bundle.putString("title", jobTitle);
                                bundle.putString("payDay", payDay);
                                editJobDialogFragment.setArguments(bundle);
                                editJobDialogFragment.setTargetFragment(jobFragmentInstance, 0);
                                editJobDialogFragment.show(getFragmentManager(), "Edit");
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
    public void OnRemoveItem(String title, String payDay, int position) {

    }

    @Override
    public void OnEditItem(String title, String payDay) {

    }

    @Override
    public void OnEdit(Job job, String oldName) {
        JobDao jobDao = new JobDao();
        jobDao.editJob(getContext(), job);
        jobDao.editJobNameInTimeDb(getContext(), job, oldName);
    }
}
