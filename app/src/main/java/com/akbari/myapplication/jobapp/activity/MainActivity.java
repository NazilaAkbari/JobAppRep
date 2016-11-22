package com.akbari.myapplication.jobapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.fragment.ListFragment;

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
        JobDao jobDao = new JobDao();
        if (jobDao.getAllJobs(this).size()==0){

        }
        if (jobName == null || jobName.equals("") ) {
            ft.replace(R.id.place_holder, new ListFragment());
            ft.commit();
        } else {
            Intent jobIntent = new Intent(this, JobActivity.class);
            jobIntent.putExtra("selectedJob", jobName);
            jobIntent.putExtra("payDay", payDay);
            startActivity(jobIntent);
            this.finish();
        }
    }

}
