package com.akbari.myapplication.jobapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.fragment.JobFragment;
import com.akbari.myapplication.jobapp.model.Job;

public class JobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String jobId = getIntent().getStringExtra("jobId");
        JobDao jobDao=new JobDao();
        Job job = jobDao.findJobById(this, jobId);
        getSupportActionBar().setTitle(job.getJobName());
        Bundle bundle = new Bundle();
        bundle.putString("jobId", jobId);
        JobFragment jobFragment = new JobFragment();
        jobFragment.setArguments(bundle);
        ft.replace(R.id.job_holder, jobFragment);
        ft.commit();
    }

}
