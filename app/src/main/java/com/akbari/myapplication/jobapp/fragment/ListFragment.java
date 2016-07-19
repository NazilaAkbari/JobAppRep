package com.akbari.myapplication.jobapp.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.adapter.CustomRecyclerAdapter;
import com.akbari.myapplication.jobapp.dao.JobDao;
import com.akbari.myapplication.jobapp.interfaces.OnAddItemListener;
import com.akbari.myapplication.jobapp.model.Job;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements OnAddItemListener {

    private RecyclerView mRecyclerView;
    private CustomRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button removeBtn;
    private Button editBtn;


    private List<Job> jobs = new ArrayList<>();

    private Button addButton;
    private Button removeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page, container, false);
        removeBtn = (Button) view.findViewById(R.id.remove);
        editBtn = (Button) view.findViewById(R.id.edit);
        JobDao jobDao = new JobDao();
        jobs.addAll(jobDao.getAllJobs(this.getActivity()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomRecyclerAdapter(view, jobs);
        mRecyclerView.setAdapter(mAdapter);
        addButton = (Button) view.findViewById(R.id.btnAddJob);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        removeButton = (Button) view.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDao jobDao = new JobDao();
                List<Job> adapterJobs = new ArrayList<>();
                adapterJobs.addAll(CustomRecyclerAdapter.jobs);
                for (int i = 0; i < adapterJobs.size(); i++) {
                    if (adapterJobs.get(i).isChecked()) {
                        jobDao.delete(getActivity().getBaseContext(), adapterJobs.get(i));
                        CustomRecyclerAdapter.jobs.remove(adapterJobs.get(i));
                        mAdapter.deleteItem(CustomRecyclerAdapter.jobs.indexOf(adapterJobs.get(i)) + 1, adapterJobs.size());
                    }
                }
                CustomRecyclerAdapter.checkNum = 0;
                removeBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void OnAddItem(String title, String payDay) {
        Job job = new Job();
        job.setJobTitle(title);
        job.setPayDay(Integer.valueOf(payDay));
        job.setIsChecked(false);
        jobs.add(job);
        JobDao jobDao = new JobDao();
        jobDao.addJob(getActivity().getBaseContext(), job);
        mAdapter.addItem(jobs.size() - 1);
    }

    private void showDialog() {
        AddDialogFragment dialogFragment = new AddDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "add");
    }
}
