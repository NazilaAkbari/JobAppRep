package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.akbari.myapplication.jobapp.activity.MainActivity;
import com.akbari.myapplication.jobapp.interfaces.OnJobDetailHourListListener;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 12/10/2016
 */

public class TimeDetailItemLongClickDialogFragment extends DialogFragment {

    private String payDay;
    private String jobName;
    private int position;
    private OnJobDetailHourListListener callBack;
    private static final CharSequence[] items = {"حذف", "ویرایش"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payDay = getArguments().getString("payDay");
        jobName = getArguments().getString("selectedJob");
        position = getArguments().getInt("position");
        try {
            callBack = (OnJobDetailHourListListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        getActivity().getLayoutInflater();
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("payDay", payDay);
                intent.putExtra("selectedJob", jobName);
                switch (which) {
                    case 0:
                        callBack.OnSelectRemoveButton();
                        break;
                    case 1:
                        callBack.OnSelectEditButton();
                        break;

                }
            }
        });
        return builder.create();

    }
}
