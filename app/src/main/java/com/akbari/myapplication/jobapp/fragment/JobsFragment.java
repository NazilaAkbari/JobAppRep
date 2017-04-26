package com.akbari.myapplication.jobapp.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.activity.JobActivity;
import com.akbari.myapplication.jobapp.adapter.JobRecyclerAdapter;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.dialogFragment.AddJobDialogFragment;
import com.akbari.myapplication.jobapp.dialogFragment.DeleteJobAlertDialogFragment;
import com.akbari.myapplication.jobapp.dialogFragment.EditJobDialogFragment;
import com.akbari.myapplication.jobapp.interfaces.JobClickListener;
import com.akbari.myapplication.jobapp.model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobsFragment extends Fragment implements JobClickListener {

    private JobRecyclerAdapter mAdapter;

    private List<Job> jobs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        JobDao jobDao = new JobDao();
        jobs.addAll(jobDao.getAllJobs(this.getActivity()));
        if (jobs.size() == 0) {
            showDialog();
        }
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new JobRecyclerAdapter(jobs, this);
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.btnAddJob);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        return view;
    }

    @Override
    public void OnAddItem(String title, String payDay) {
        Job job = new Job();
        job.setJobName(title);
        job.setPayDay(Integer.valueOf(payDay));
        jobs.add(job);
        JobDao jobDao = new JobDao();
        jobDao.addJob(getContext(), job);
        mAdapter.addItem(jobs.size() - 1);
    }

    @Override
    public void OnSelectRemoveButton(String id, int position) {
        DeleteJobAlertDialogFragment fragment = new DeleteJobAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("jobId", id);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), "Alert");
    }

    @Override
    public void OnRemoveItem(String id, int position) {
        JobDao jobDao = new JobDao();
        jobDao.delete(getContext(), jobs.get(position));
        mAdapter.deleteItem(position);
    }

    @Override
    public void OnSelectEditButton(String jobId) {
        EditJobDialogFragment fragment = new EditJobDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("jobId", jobId);
        fragment.setArguments(bundle);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), "Edit");
    }

    @Override
    public void OnEditItem(Job job, String oldName) {
        JobDao jobDao = new JobDao();
        jobDao.editJob(getContext(), job);
        mAdapter.updateList(jobDao.getAllJobs(getContext()));
    }

    private void showDialog() {
        AddJobDialogFragment dialogFragment = new AddJobDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "add");
    }
}
