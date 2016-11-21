package com.akbari.myapplication.jobapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.interfaces.ItemClickListener;
import com.akbari.myapplication.jobapp.interfaces.ItemLongClickListener;

/**
 * @author n.akbari
 * @version 1.0
 * @since 06/12/2016
 */
public class RecyclerViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    public TextView jobTitle;
    public TextView payDay;
    private ItemClickListener clickListener;
    private ItemLongClickListener longClickListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
        payDay = (TextView) itemView.findViewById(R.id.payDay);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(ItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getLayoutPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        longClickListener.onLongClick(v, getLayoutPosition(), true);
        return true;
    }
}
