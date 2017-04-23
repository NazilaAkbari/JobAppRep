package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.interfaces.OnJobListListener;

/**
 * @author Akbari
 */

public class DeleteJobAlertDialogFragment extends DialogFragment {

    private OnJobListListener callBack;
    private String payDay;
    private String jobName;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payDay = getArguments().getString("payDay");
        jobName = getArguments().getString("selectedJob");
        position = getArguments().getInt("position");
        try {
            callBack = (OnJobListListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_alert, null))
                .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        callBack.OnRemoveItem(jobName, payDay, position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteJobAlertDialogFragment.this.getDialog().cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;

    }
}
