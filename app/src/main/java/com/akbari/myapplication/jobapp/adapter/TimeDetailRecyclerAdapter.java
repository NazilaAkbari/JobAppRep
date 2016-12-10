package com.akbari.myapplication.jobapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.dialogFragment.TimeDetailItemLongClickDialogFragment;
import com.akbari.myapplication.jobapp.interfaces.ItemLongClickListener;
import com.akbari.myapplication.jobapp.model.Time;
import com.akbari.myapplication.jobapp.viewHolder.TimeRecyclerViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * @author Akbari
 * @version 1.0
 * @since 12/10/2016
 */

public class TimeDetailRecyclerAdapter extends RecyclerView.Adapter<TimeRecyclerViewHolder> {

    private static List<Time> times = Collections.emptyList();
    private Fragment fragment;
    private String jobTitle;
    private String payDay;

    public TimeDetailRecyclerAdapter(List<Time> times, Fragment fragment
            , String payDay, String jobTitle) {
        TimeDetailRecyclerAdapter.times = times;
        this.fragment = fragment;
        this.payDay = payDay;
        this.jobTitle = jobTitle;
        notifyDataSetChanged();
    }

    @Override
    public TimeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.job_detail_list_item, parent, false);
        return new TimeRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimeRecyclerViewHolder holder, int position) {
        if (times.size() != 0) {
            holder.getEnterTime().setText(times.get(position).getEnterTime());
            holder.getExitTime().setText(times.get(position).getExitTime());
            holder.getDate().setText(times.get(position).getDate());
            holder.setLongClickListener(new ItemLongClickListener() {
                @Override
                public boolean onLongClick(View view, int position, boolean isLongClick) {
                    showOnLongClickDialog(payDay, jobTitle, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    private void showOnLongClickDialog(String payDay, String jobName, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedJob", jobName);
        bundle.putString("payDay", payDay);
        bundle.putInt("position", position);
        TimeDetailItemLongClickDialogFragment dialogFragment =
                new TimeDetailItemLongClickDialogFragment();
        dialogFragment.setTargetFragment(fragment, 0);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragment.getFragmentManager(), "LongClickDialog");
    }
}