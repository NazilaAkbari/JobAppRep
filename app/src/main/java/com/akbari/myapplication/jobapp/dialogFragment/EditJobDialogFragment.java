package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.interfaces.OnListListener;
import com.akbari.myapplication.jobapp.model.Job;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 12/07/2016
 */

public class EditJobDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText title;
    private EditText payDay;
    private String oldName;
    private OnListListener callBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callBack = (OnListListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);
        builder.setView(view);
        title = (EditText) view.findViewById(R.id.jobTitleEdit);
        payDay = (EditText) view.findViewById(R.id.payDayEdit);
        title.setText(getArguments().getString("title"));
        payDay.setText(getArguments().getString("payDay"));
        oldName=getArguments().getString("title");
        builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                assert payDay != null && title != null;
                int payDayInt = Integer.valueOf(payDay.getText().toString());
                if (payDayInt > 0 && payDayInt <= 30 &&
                        !title.getText().toString().equals("")) {
                    Job job = new Job();
                    job.setId(getArguments().getString("id"));
                    job.setJobName(title.getText().toString());
                    job.setPayDay(Integer.valueOf(payDay.getText().toString()));
                    callBack.OnEdit(job,oldName);
                    title.setText("");
                    payDay.setText("");
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditJobDialogFragment.this.getDialog().cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;

    }
}
