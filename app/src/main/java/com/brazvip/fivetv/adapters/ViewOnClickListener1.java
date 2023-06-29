package com.brazvip.fivetv.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public final class ViewOnClickListener1 implements View.OnClickListener {

    public RecyclerView.Adapter adapter;
    public int index;
    public int type;

    public ViewOnClickListener1(RecyclerView.Adapter viewAdapter, int index, int type) {
        this.adapter = viewAdapter;
        this.index = index;
        this.type = type;
    }

    @Override
    public void onClick(View view) {
        switch (this.type) {
            case 0:
                DashboardGroupL1Adapter.m1743c((DashboardGroupL1Adapter) this.adapter, this.index, view);
                break;
            case 1:
                DashboardGroupL2Adapter.m1740d((DashboardGroupL2Adapter) this.adapter, this.index, view);
                return;
            default:
                //AvatarAdapter.m1695a((AvatarAdapter) this.adapter, this.index, view);
                break;
        }
    }
}
