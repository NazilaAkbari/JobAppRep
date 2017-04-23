package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.akbari.myapplication.jobapp.activity.MainActivity;
import com.akbari.myapplication.jobapp.interfaces.JobClickListener;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 11/19/2016
 */

public class JobsLongClickDialogFragment extends android.support.v4.app.DialogFragment {

    private String jobId;
    private int position;
    private JobClickListener callBack;
    private static final CharSequence[] items = {"نمایش", "حذف", "ویرایش"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobId = getArguments().getString("jobId");
        position = getArguments().getInt("position");
        try {
            callBack = (JobClickListener) getTargetFragment();
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
                switch (which) {
                    case 0:
                        getContext().startActivity(intent);
                        break;
                    case 1:
                        callBack.OnSelectRemoveButton(jobId, position);
                        break;
                    case 2:
                        callBack.OnSelectEditButton(jobId);
                        break;
                }
            }
        });
        return builder.create();

    }
}
