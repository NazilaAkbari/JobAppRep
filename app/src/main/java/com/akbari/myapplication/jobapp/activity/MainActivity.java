package com.akbari.myapplication.jobapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.fragment.JobsFragment;
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
        getSupportActionBar().setTitle("");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String jobId = getIntent().getStringExtra("jobId");
        JobDao jobDao = new JobDao();
        List<Job> jobs = jobDao.getAllJobs(this);
        if (jobs.size() == 1 || jobId != null) {
            Intent jobIntent = new Intent(this, JobActivity.class);
            jobIntent.putExtra("jobId", jobs.get(0).getId());
            startActivity(jobIntent);
            this.finish();
        } else {
            ft.replace(R.id.place_holder, new JobsFragment());
            ft.commit();
        }
    }

}
