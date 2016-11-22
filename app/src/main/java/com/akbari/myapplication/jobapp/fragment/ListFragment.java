package com.akbari.myapplication.jobapp.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.adapter.CustomRecyclerAdapter;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.dialogFragment.AddDialogFragment;
import com.akbari.myapplication.jobapp.interfaces.OnListListener;
import com.akbari.myapplication.jobapp.model.Job;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements OnListListener {

    private CustomRecyclerAdapter mAdapter;

    private List<Job> jobs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page, container, false);
        JobDao jobDao = new JobDao();
        jobs.addAll(jobDao.getAllJobs(this.getActivity()));
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomRecyclerAdapter(jobs, this);
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
        job.setIsChecked(false);
        jobs.add(job);
        JobDao jobDao = new JobDao();
        jobDao.addJob(getContext(), job);
        mAdapter.addItem(jobs.size() - 1);
    }

    @Override
    public void OnRemoveItem(String title, String payDay, int position) {
        JobDao jobDao = new JobDao();
        List<Job> adapterJobs = new ArrayList<>();
        adapterJobs.addAll(jobs);
        jobDao.delete(getContext(), jobs.get(position));
        mAdapter.jobs.remove(position);
        mAdapter.deleteItem(position,
                adapterJobs.size());
    }

    private void showDialog() {
        AddDialogFragment dialogFragment = new AddDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "add");
    }
}
