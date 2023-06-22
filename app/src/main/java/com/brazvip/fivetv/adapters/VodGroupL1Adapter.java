package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.brazvip.fivetv.beans.vod.VodGroupL2;


public class VodGroupL1Adapter extends HRecyclerViewAdapter2<VodGroupL1Adapter.ViewHolder> {

    private static final String TAG = "VodGroupL1Adapter";
    private Context context;
    private List<String> groupNames;
    private Handler handler;
    private int lastSelectedItem;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupL1TextView;

        public ViewHolder(VodGroupL1Adapter vodGroupL1Adapter, View view) {
            super(view);
            this.groupL1TextView = (TextView) view.findViewById(R.id.group_l1_name);
        }
    }

    public VodGroupL1Adapter(Map<String, List<VodGroupL2>> map, Context context, Handler handler, NavigationListener navigationListener) {
        super(context, 1, navigationListener);
        this.lastSelectedItem = 0;
        this.context = context;
        this.handler = handler;
        ArrayList arrayList = new ArrayList(map.keySet());
        this.groupNames = arrayList;
        if (arrayList.indexOf("All") >= 0) {
            List<String> list = this.groupNames;
            Collections.swap(list, list.indexOf("All"), 0);
        }
        ArrayList arrayList2 = new ArrayList(Arrays.asList("ADULTOS", "XXX", "+18"));
        Iterator<String> it = MainActivity.adultList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (this.groupNames.indexOf(next) < 0) {
                arrayList2.add(next);
            }
        }
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            String str = (String) it2.next();
            if (this.groupNames.indexOf(str) >= 0) {
                List<String> list2 = this.groupNames;
                Collections.swap(list2, list2.indexOf(str), this.groupNames.size() - 1);
            }
        }
        this.groupNames.size();
    }

    public void onBindViewClickListener(int i, ViewHolder viewHolder, View view) {
        viewHolder.getAbsoluteAdapterPosition();
        notifyItemChanged(this.mSelectedItem);
        this.nextSelectItem = recyclerView.getChildLayoutPosition(view);
        int pos = recyclerView.getChildLayoutPosition(view);
        this.mSelectedItem = pos;
        notifyItemChanged(pos);
    }

    public void onBindViewFocusChange(int i, ViewHolder viewHolder, View view, boolean z) {
        viewHolder.getAbsoluteAdapterPosition();
        viewHolder.itemView.setSelected(z);
    }

    @Override
    public int getItemCount() {
        return this.groupNames.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.vod_group_l1_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(VodGroupL1Adapter.ViewHolder holder, int position) {
        boolean z = position == this.mSelectedItem;
        holder.itemView.setSelected(z);
        holder.groupL1TextView.setText(this.groupNames.get(position));
        if (z && this.lastSelectedItem != this.mSelectedItem) {
            this.handler.removeMessages(2);
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("selectedGroupL1", this.groupNames.get(position));
            message.setData(bundle);
            this.handler.sendMessage(message);
            this.lastSelectedItem = this.mSelectedItem;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                VodGroupL1Adapter.this.onBindViewClickListener(position, holder, view);
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public final void onFocusChange(View view, boolean z2) {
                VodGroupL1Adapter.this.onBindViewFocusChange(position, holder, view, z2);
            }
        });
    }

    @Override
    public boolean onLastItemScrolled() {
        return true;
    }
}