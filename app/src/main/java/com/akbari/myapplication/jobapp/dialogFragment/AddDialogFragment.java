package com.akbari.myapplication.jobapp.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.interfaces.OnAddItemListener;

/**
 * Created by n.akbari on 06/19/2016.
 */
public class AddDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText title;
    private EditText payDay;
    private OnAddItemListener callBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callBack = (OnAddItemListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add, null))
                .setPositiveButton(R.string.addJob, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        title = (EditText) getDialog().findViewById(R.id.jobTitleTxt);
                        payDay = (EditText) getDialog().findViewById(R.id.payDayTxt);
                        assert payDay != null && title != null;
                        int payDayInt = Integer.valueOf(payDay.getText().toString());
                        System.out.println(payDay.getText().toString()+"@@@@@@@");
                        if (payDayInt > 0 && payDayInt <= 31 &&
                                !title.getText().toString().equals("")) {
                            callBack.OnAddItem(title.getText().toString(),
                                    payDay.getText().toString());
                            title.setText("");
                            payDay.setText("");
                        }
                    }
                })
                .setNegativeButton(R.string.cancelAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }

}
