package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.layouts.DashboardLayout;

import java.util.List;


public class DashboardGroupL1Adapter extends CustomItemAdapter<DashboardGroupL1Adapter.ViewHolder> {
    private final List<String> l1Titles;
    private final Context mContext;
    private final NavigationListener navigationListener;

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;

        public ViewHolder(View view) {
            super(view);
            this.groupName = (TextView) view.findViewById(R.id.l1_name);
        }
    }

    public DashboardGroupL1Adapter(List<String> list, Context context, NavigationListener navigationListener) {
        this.mContext = context;
        this.l1Titles = list;
        this.navigationListener = navigationListener;
        this.onKeyListener = new View.OnKeyListener() { //new View$OnKeyListenerC2425a(this, context, 0);
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return lambda$new$0(mContext, view, i, keyEvent);
            }
        };
    }

    public static void m1743c(final DashboardGroupL1Adapter dashboardGroupL1Adapter, final int n, final View view) {
        dashboardGroupL1Adapter.lambda$onBindViewHolder$1(n, view);
    }

    public boolean lambda$new$0(Context context, View view, int i, KeyEvent keyEvent) {
        RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        if (keyEvent.getAction() != 0) {
            if (keyEvent.getAction() == 1 && HRecyclerViewAdapter2.isReturnKeycode(keyEvent) && (keyEvent.getFlags() & 128) != 128) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.recyclerView.findViewHolderForAdapterPosition(this.mSelectedItem);
                if (findViewHolderForAdapterPosition != null) {
                    findViewHolderForAdapterPosition.itemView.performClick();
                }
                return true;
            }
            return false;
        } else if (i == 4) {
            //Utils.showQuitDialog(context);
            return true;
        } else {
            switch (i) {
                case 19:
                    return this.navigationListener.navigateAbove();
                case 20:
                    return this.navigationListener.navigateBelow();
                case 21:
                    if (tryMoveSelection(layoutManager, -1)) {
                        return true;
                    }
                    return this.navigationListener.navigateLeft();
                case 22:
                    if (tryMoveSelection(layoutManager, 1)) {
                        return true;
                    }
                    return this.navigationListener.navigateRight();
                default:
                    return false;
            }
        }
    }

    public void lambda$onBindViewHolder$1(int i, View view) {
        onItemSelected(i);
    }

    private void loadItemAt(int i) {
        Message obtainMessage = DashboardLayout.dashboardHandler.obtainMessage(20);
        Bundle bundle = new Bundle();
        bundle.putString("title", this.l1Titles.get(i));
        obtainMessage.setData(bundle);
        DashboardLayout.dashboardHandler.sendMessageDelayed(obtainMessage, 20L);
    }

    @Override
    public int getItemCount() {
        return this.l1Titles.size();
    }

    @Override
    public void onBindViewHolder(DashboardGroupL1Adapter.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (position == 0 && this.mSelectedItem == 0) {
            loadItemAt(0);
        }
        viewHolder.groupName.setText(this.l1Titles.get(position));
        viewHolder.itemView.setOnClickListener(new ViewOnClickListener(this, position));
    }

        public static class ViewOnClickListener implements View.OnClickListener {

            public RecyclerView.Adapter adapter;
            public int position;

            public ViewOnClickListener(RecyclerView.Adapter viewAdapter, int position) {
                this.adapter = viewAdapter;
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                DashboardGroupL1Adapter.m1743c((DashboardGroupL1Adapter) this.adapter, this.position, view);
            }
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.dash_group_l1_item, viewGroup, false));
    }

    @Override
    public void onItemSelected(int i) {
        super.onItemSelected(i);
        loadItemAt(i);
    }
}
