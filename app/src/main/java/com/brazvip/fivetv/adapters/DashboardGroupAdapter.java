package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.R;
import com.brazvip.fivetv.layouts.DashboardLayout;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.beans.GroupL1;

import java.util.HashSet;
import java.util.List;

public class DashboardGroupAdapter extends HRecyclerViewAdapter<DashboardGroupAdapter.DashGroupViewHolder> {

    public static final String TAG = "DashboardGroupAdapter";

    public Context mContext;

    public List<GroupL1> mGroupL1List;

    public SparseArray<GroupL1> mGroupL1Array;

    public final Integer[] mGroupItemList;

    public int mSelItem;

    public Handler mHandler;

    public class DashGroupViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public DashGroupViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.group_name);
            AutoUtils.auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_HEIGHT);
        }
    }

    public DashboardGroupAdapter(SparseArray<GroupL1> sparseArray, Context context, Handler handler) {
        super(context, 1);

        this.mContext = context;
        this.mHandler = handler;
        this.mGroupL1Array = sparseArray;

        this.mSelItem = 0;

        HashSet hashSet = new HashSet();
        for (int i = 0; i < sparseArray.size(); i++) {
            hashSet.add(Integer.valueOf(sparseArray.keyAt(i)));
        }
        this.mGroupItemList = (Integer[]) hashSet.toArray(new Integer[hashSet.size()]);
        //Arrays.sort(this.mGroupItemList);
    }

    @Override
    public DashGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DashGroupViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.dash_group_item, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return this.mGroupL1Array.size();
    }

    @Override
    /* renamed from: a */
    public void onBindViewHolder(DashGroupViewHolder holder, int position) {
        boolean isSelected = position == getSelectedItem();
        if (isSelected) {
            getNextSelectItem();
        }
        holder.itemView.setSelected(isSelected);

        int groupId = this.mGroupItemList[position].intValue();
        holder.tvName.setText(this.mGroupL1Array.get(groupId).name);
        holder.tvName.setTag(Integer.valueOf(groupId));
        if (isSelected) {
            holder.tvName.setTextColor(0xffffffff);
            if (this.mSelItem != getSelectedItem()) {
                this.mHandler.removeMessages(DashboardLayout.MSG_DASH_CHANNEL_TOP_SHOW);
                this.mHandler.sendMessage(Message.obtain(this.mHandler, DashboardLayout.MSG_DASH_CHANNEL_TOP_SHOW, groupId, 0));
                this.mSelItem = getSelectedItem();
            }
        } else {
            holder.tvName.setTextColor(0x8fffffff);
        }
        holder.itemView.setOnClickListener(view -> {
            notifyItemChanged(getSelectedItem());
            setNextSelectItem(getOwnerView().getChildLayoutPosition(view));
            setSelectedItem(getOwnerView().getChildLayoutPosition(view));
            notifyItemChanged(getSelectedItem());
        });
    }
}
