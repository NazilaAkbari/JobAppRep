package com.akbari.myapplication.jobapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.akbari.myapplication.jobapp.dao.TimeDao;
import com.akbari.myapplication.jobapp.model.JobTime;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobFragment extends Fragment {

    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        setHasOptionsMenu(true);
        setTodayDate(view);
        setMonthTime(view);
        setWeekTime(view);
        setAddButtonActionListener(view);
        setChartData(view);
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
                getArguments().getString("payDay"),
                getArguments().getString("selectedJob")
        );
        String timeS = time / 60 + " ساعت ";
        if (time % 60 > 0)
            timeS += "و " + time % 60 + " دقیقه";
        monthTime.setText(timeS);
    }

    private void setWeekTime(View view) {
        TextView weekTime = (TextView) view.findViewById(R.id.weekTime);
        TimeDao timeDao = new TimeDao();
        Integer time = timeDao.getThisWeekHour(this.getActivity(),
                getArguments().getString("payDay"),
                getArguments().getString("selectedJob")
        );
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
                intent.putExtra("selectedJob", getArguments().getString("selectedJob"));
                intent.putExtra("payDay", getArguments().getString("payDay"));
                intent.putExtra("addTime", "true");
                startActivity(intent);
            }
        });
    }

    private void setChartData(View view) {
        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        TimeDao timeDao = new TimeDao();
        Map<Integer, Integer> chartData = timeDao.getChartData(getContext(), getJobTime());
        for (Map.Entry<Integer, Integer> entry : chartData.entrySet()
                ) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue()/60));
        }
        BarDataSet set = new BarDataSet(entries, "کارکرد هر ماه");
        BarData data = new BarData(set);
        set.setColors(new int[]{R.color.pink, R.color.colorAccent, R.color.yellow},
                getContext());
        data.setBarWidth(0.1f);
        chart.setHorizontalScrollBarEnabled(true);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate();
    }

    private JobTime getJobTime() {
        JobTime jobTime = new JobTime();
        jobTime.setJobName(getArguments().getString("selectedJob"));
        jobTime.setPayDay(Integer.valueOf(getArguments().getString("payDay")));
        jobTime.setDateTo(DateUtil.getCurrentPersianDate());
        return jobTime;
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
                if (drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawer(GravityCompat.END);
                else
                    drawerLayout.openDrawer(GravityCompat.END);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
