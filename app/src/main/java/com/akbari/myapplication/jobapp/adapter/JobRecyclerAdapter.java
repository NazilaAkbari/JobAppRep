package com.akbari.myapplication.jobapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akbari.myapplication.jobapp.activity.MainActivity;
import com.akbari.myapplication.jobapp.dialogFragment.JobListItemLongClickDialogFragment;
import com.akbari.myapplication.jobapp.interfaces.ItemClickListener;
import com.akbari.myapplication.jobapp.interfaces.ItemLongClickListener;
import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.viewHolder.JobRecyclerViewHolder;

import java.util.Collections;
import java.util.List;

import com.akbari.myapplication.jobapp.R;

/**
 * @author n.akbari
 * @version 1.0
 * @since 06/12/2016
 */
public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerViewHolder> {

    private static List<Job> jobs = Collections.emptyList();
    private Fragment fragment;

    public JobRecyclerAdapter(List<Job> jobs, Fragment fragment) {
        JobRecyclerAdapter.jobs = jobs;
        this.fragment = fragment;
        notifyDataSetChanged();
    }


    @Override
    public JobRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new JobRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobRecyclerViewHolder viewHolder, final int position) {
        if (jobs.size() != 0) {
            viewHolder.getJobTitle().setText(jobs.get(position).getJobName());
            viewHolder.getPayDay().setText(jobs.get(position).getPayDay().toString());
            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("payDay", jobs.get(position).getPayDay().toString());
                    intent.putExtra("selectedJob", jobs.get(position).getJobName());
                    view.getContext().startActivity(intent);
                }
            });
            viewHolder.setLongClickListener(new ItemLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position, boolean isLongClick) {
                    String payDay = jobs.get(position).getPayDay().toString();
                    String jobName = jobs.get(position).getJobName();
                    showOnLongClickDialog(payDay, jobName, position);
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void addItem(int position) {
        notifyItemInserted(position);
    }

    public void deleteItem(int position) {
        jobs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void updateList(List<Job> data) {
        jobs = data;
        notifyDataSetChanged();
    }

    private void showOnLongClickDialog(String payDay, String jobName, int position) {
        JobListItemLongClickDialogFragment longClickDialogFragment =
                new JobListItemLongClickDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedJob", jobName);
        bundle.putString("payDay", payDay);
        bundle.putInt("position", position);
        longClickDialogFragment.setTargetFragment(fragment, 0);
        longClickDialogFragment.setArguments(bundle);
        longClickDialogFragment.show(fragment.getFragmentManager(), "LongClickDialog");
    }

}
