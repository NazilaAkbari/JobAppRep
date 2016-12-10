package com.akbari.myapplication.jobapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akbari.myapplication.jobapp.R;
import com.akbari.myapplication.jobapp.interfaces.ItemLongClickListener;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 12/10/2016
 */

public class TimeRecyclerViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    private TextView enterTime;
    private TextView exitTime;
    private TextView date;
    private ItemLongClickListener longClickListener;

    public TimeRecyclerViewHolder(View itemView) {
        super(itemView);
        enterTime = (TextView) itemView.findViewById(R.id.enterTime);
        exitTime = (TextView) itemView.findViewById(R.id.exitTime);
        date = (TextView) itemView.findViewById(R.id.dateEnter);
        itemView.setOnLongClickListener(this);
    }

    public TextView getEnterTime() {
        return enterTime;
    }

    public TextView getExitTime() {
        return exitTime;
    }

    public TextView getDate() {
        return date;
    }

    public void setLongClickListener(ItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        longClickListener.onLongClick(view, getLayoutPosition(), true);
        return true;
    }
}
