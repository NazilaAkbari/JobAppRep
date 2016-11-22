package com.akbari.myapplication.jobapp.interfaces;

/**
 * Created by n.akbari on 06/19/2016.
 */
public interface OnListListener {

    void OnAddItem(String title, String payDay);

    void OnRemoveItem(String title, String payDay,int position);
}
