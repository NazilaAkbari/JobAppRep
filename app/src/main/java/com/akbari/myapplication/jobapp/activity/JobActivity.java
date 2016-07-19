package com.akbari.myapplication.jobapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.TimeDao;
import com.akbari.myapplication.jobapp.fragment.JobFragment;
import com.akbari.myapplication.jobapp.model.Time;

import java.text.ParseException;

public class JobActivity extends AppCompatActivity {
    private String jobName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        jobName = intent.getStringExtra("selectedJob");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(jobName);
        String payDay = intent.getStringExtra("payDay");
        Bundle bundle = new Bundle();
        bundle.putString("selectedJob", jobName);
        bundle.putString("payDay", payDay);
        JobFragment jobFragment = new JobFragment();
        jobFragment.setArguments(bundle);
        ft.replace(R.id.job_holder, jobFragment);
        ft.commit();
    }

    public void addTime(View view) throws ParseException {
        EditText enterTime = (EditText) findViewById(R.id.timeStart);
        EditText exitTime = (EditText) findViewById(R.id.timeExit);
        EditText date = (EditText) findViewById(R.id.date);
        Time time = new Time();
        time.setJobName(jobName);
        time.setEnterTime(enterTime.getText().toString());
        time.setExitTime(exitTime.getText().toString());
        time.setDate(date.getText().toString());
        TimeDao timeDao = new TimeDao();
        timeDao.AddTime(this, time);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        enterTime.setText("");
        exitTime.setText("");
        date.setText("");
        this.finish();
    }

}
