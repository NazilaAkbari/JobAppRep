package com.akbari.myapplication.jobapp.interfaces;

import com.akbari.myapplication.jobapp.model.Job;

/**
 * @author n.akbari
 * @since 06/19/2016
 * @version 1.0
 */
public interface OnJobListListener {

    void OnAddItem(String title, String payDay);

    void OnRemoveItem(String title, String payDay, int position);

    void OnSelectEditButton(String title, String payDay);

    void OnEditItem(Job job, String oldName);
}
