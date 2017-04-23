package com.akbari.myapplication.jobapp.interfaces;

import com.akbari.myapplication.jobapp.model.Job;

/**
 * @author n.akbari
 * @since 06/19/2016
 * @version 1.0
 */
public interface JobClickListener {

    void OnAddItem(String title, String payDay);

    void OnSelectRemoveButton(String id, int position);

    void OnRemoveItem(String id, int position);

    void OnSelectEditButton(String jobId);

    void OnEditItem(Job job, String oldName);
}
