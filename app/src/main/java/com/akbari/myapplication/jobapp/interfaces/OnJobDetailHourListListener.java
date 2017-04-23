package com.akbari.myapplication.jobapp.interfaces;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 12/10/2016
 */

public interface OnJobDetailHourListListener {

    void OnSelectRemoveButton(String timeId,int position);

    void OnRemoveItem(String timeId,int position);

    void OnSelectEditButton();


    void OnEditItem();
}
