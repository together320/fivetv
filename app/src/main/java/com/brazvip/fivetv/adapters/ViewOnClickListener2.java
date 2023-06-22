package com.brazvip.fivetv.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public final class ViewOnClickListener2 implements View.OnClickListener {
    public int index;

    public RecyclerView.Adapter adapter;

    public RecyclerView.ViewHolder viewHolder;

    public ViewOnClickListener2(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int i) {
        this.index = i;
        this.adapter = adapter;
        this.viewHolder = viewHolder;
    }

    @Override
    public void onClick(View view) {
        switch (this.index) {
//            case 0:
//                ((EditProfilesListAdapter) this.adapter).lambda$onBindViewHolder$1((EditProfilesListAdapter.ProfileHolder) this.viewHolder, view);
//                return;
//            case 1:
//                ((VerticalProfileAdapter) this.adapter).lambda$onBindViewHolder$0((VerticalProfileAdapter.ViewHolder) this.viewHolder, view);
//                return;
//            case 2:
//                ((HorizontalProfileAdapter) this.adapter).lambda$onBindViewHolder$1((HorizontalProfileAdapter.ViewHolder) this.viewHolder, view);
//                return;
            default:
                SeasonAdapter.m1732c((SeasonAdapter) this.adapter, (SeasonAdapter.ViewHolder) this.viewHolder, view);
                return;
        }
    }
}
