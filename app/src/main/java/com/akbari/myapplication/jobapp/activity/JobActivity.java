package com.akbari.myapplication.jobapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.fragment.AddTimeFragment;
import com.akbari.myapplication.jobapp.fragment.JobFragment;

public class JobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        String jobName = intent.getStringExtra("selectedJob");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(jobName);
        String payDay = intent.getStringExtra("payDay");
        String isAddTime = intent.getStringExtra("addTime");
        Bundle bundle = new Bundle();
        bundle.putString("selectedJob", jobName);
        bundle.putString("payDay", payDay);
        if (isAddTime!=null && isAddTime.equals("true")){
            AddTimeFragment timeFragment=new AddTimeFragment();
            timeFragment.setArguments(bundle);
            ft.replace(R.id.job_holder, timeFragment);
            ft.commit();
        }
        else {
            JobFragment jobFragment = new JobFragment();
            jobFragment.setArguments(bundle);
            ft.replace(R.id.job_holder, jobFragment);
            ft.commit();
        }
    }

}
