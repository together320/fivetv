package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import java.util.List;

import com.brazvip.fivetv.layouts.DashboardLayout;


public class DashboardGroupL2Adapter extends CustomItemAdapter<DashboardGroupL2Adapter.ViewHolder> {
    private final List<String> l2Titles;
    private final Context mContext;
    private final NavigationListener navigationListener;

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.l2_dash_title);
        }
    }

    public DashboardGroupL2Adapter(List<String> list, Context context, NavigationListener navigationListener) {
        this.mContext = context;
        this.l2Titles = list;
        this.navigationListener = navigationListener;
        this.onKeyListener = new ViewOnKeyListener(this, context);
    }

        public final class ViewOnKeyListener implements View.OnKeyListener {
            public final Context context;

            public final CustomItemAdapter apapter;
    
            public ViewOnKeyListener(CustomItemAdapter customItemAdapter, Context context) {
                this.apapter = customItemAdapter;
                this.context = context;
            }
    
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return onAdapterKey(this.context, view, i, keyEvent);
            }
        }


    public static void selectItem(final DashboardGroupL2Adapter dashboardGroupL2Adapter, int index, View view) {
        dashboardGroupL2Adapter.onItemSelected(index);;
    }

    public boolean onAdapterKey(Context context, View view, int i, KeyEvent keyEvent) {
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
        } else if (i == KeyEvent.KEYCODE_BACK) {
            MainActivity.SendMessage(Constant.MSG_SHOW_QUIT_DIALOG);
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

    private void loadItemAt(int i) {
        Message obtainMessage = DashboardLayout.dashboardHandler.obtainMessage(DashboardLayout.L2_GROUP_SELECTED);
        Bundle bundle = new Bundle();
        bundle.putString("title", this.l2Titles.get(i));
        obtainMessage.setData(bundle);
        DashboardLayout.dashboardHandler.sendMessage(obtainMessage);
    }

    @Override
    public int getItemCount() {
        return this.l2Titles.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardGroupL2Adapter.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (position == 0 && this.mSelectedItem == 0) {
            loadItemAt(0);
        }
        viewHolder.title.setText(this.l2Titles.get(position));
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
                DashboardGroupL2Adapter.selectItem((DashboardGroupL2Adapter) this.adapter, this.position, view);
            }
        }

    @NonNull
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.dash_group_l2_item, viewGroup, false));
    }

    @Override // com.brazvip.fivetv.adapter.CustomItemAdapter
    public void onItemSelected(int i) {
        super.onItemSelected(i);
        loadItemAt(i);
    }
}
