package com.akbari.myapplication.jobapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.fragment.ListFragment;
import com.akbari.myapplication.jobapp.model.Job;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        String payDay = intent.getStringExtra("payDay");
        String jobName = intent.getStringExtra("selectedJob");
        String jobId = intent.getStringExtra("jobId");
        JobDao jobDao = new JobDao();
        List<Job> jobs = jobDao.getAllJobs(this);
        if (jobs.size() == 1) {
            Intent jobIntent = new Intent(this, JobActivity.class);
            jobIntent.putExtra("selectedJob", jobs.get(0).getJobName());
            jobIntent.putExtra("payDay", jobs.get(0).getPayDay().toString());
            jobIntent.putExtra("jobId", jobs.get(0).getId());
            startActivity(jobIntent);
            this.finish();
        }
        if (jobName == null || jobName.equals("")) {
            ft.replace(R.id.place_holder, new ListFragment());
            ft.commit();
        } else {
            Intent jobIntent = new Intent(this, JobActivity.class);
            jobIntent.putExtra("selectedJob", jobName);
            jobIntent.putExtra("payDay", payDay);
            jobIntent.putExtra("jobId", jobs.get(0).getId());
            startActivity(jobIntent);
            this.finish();
        }
    }

}
