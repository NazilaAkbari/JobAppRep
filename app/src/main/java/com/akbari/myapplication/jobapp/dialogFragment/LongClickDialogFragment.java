package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.akbari.myapplication.jobapp.activity.MainActivity;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 11/19/2016
 */

public class LongClickDialogFragment extends android.support.v4.app.DialogFragment {

    private String payDay;
    private String jobName;
    private static final CharSequence[] items = {"نمایش", "حذف", "ویرایش"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payDay = getArguments().getString("payDay");
        jobName = getArguments().getString("selectedJob");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("payDay", payDay);
                        intent.putExtra("selectedJob", jobName);
                        getContext().startActivity(intent);
                        break;
                }
            }
        });
        return builder.create();

    }
}
