package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.brazvip.fivetv.beans.vod.VodGroupL2;
import com.brazvip.fivetv.dialogs.PasswordDialog;
import com.zhy.autolayout.utils.AutoUtils;
import p156o4.C2320l;


public class VodGroupAdapter extends HRecyclerViewAdapter2<VodGroupAdapter.ViewHolder> {
    private Context context;
    public Handler handler;
    public int lastSelectedItem;
    public List<VodGroupL2> vodL2Groups;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView grouptypeIcon;

        public ViewHolder(View view) {
            super(view);
            this.grouptypeIcon = (ImageView) view.findViewById(R.id.group_type_icon);
            this.groupName = (TextView) view.findViewById(R.id.group_name);
            AutoUtils.auto(view, 3, 2);
        }
    }

    public VodGroupAdapter(List<VodGroupL2> list, Context context, Handler handler, NavigationListener navigationListener) {
        super(context, 2, navigationListener);
        this.lastSelectedItem = 0;
        list = list == null ? new ArrayList<>() : list;
        Collections.sort(list, new C2320l(5));
        this.vodL2Groups = list;
        this.context = context;
        this.handler = handler;
    }

    public static int compare(VodGroupL2 vodGroupL2, VodGroupL2 vodGroupL22) {
        return Integer.compare(Integer.parseInt(vodGroupL2._id), Integer.parseInt(vodGroupL22._id));
    }

    public void sendLoadChannelEvent(VodGroupL2 vodGroupL2) {
        this.handler.removeMessages(3);
        Message obtainMessage = this.handler.obtainMessage();
        obtainMessage.what = 3;
        Bundle bundle = new Bundle();
        bundle.putString("groupId", vodGroupL2.getId());
        bundle.putBoolean("restrictedAccess", vodGroupL2.isRestricted());
        obtainMessage.setData(bundle);
        this.handler.sendMessage(obtainMessage);
    }

    public void showUnlockDialog(final ViewHolder viewHolder, final VodGroupL2 vodGroupL2) {
        PasswordDialog.Builder builder = new PasswordDialog.Builder(this.context);
        builder.positiveClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.restrictedGroupsUnlocked = true;
                viewHolder.grouptypeIcon.setImageResource(R.mipmap.group_type_lock_0);
                VodGroupAdapter vodGroupAdapter = VodGroupAdapter.this;
                vodGroupAdapter.notifyItemChanged(vodGroupAdapter.mSelectedItem);
                sendLoadChannelEvent(vodGroupL2);
            }
        };
        builder.build().show();
    }

    @Override
    public int getItemCount() {
        return this.vodL2Groups.size();
    }

    @Override
    public void onBindViewHolder(VodGroupAdapter.ViewHolder viewHolder, int imageResource) {
        boolean selected = imageResource == super.mSelectedItem;
        viewHolder.itemView.setSelected(selected);
        VodGroupL2 vodGroupL2 = this.vodL2Groups.get(imageResource);
        viewHolder.grouptypeIcon.setVisibility(View.VISIBLE);
        if (vodGroupL2.isRestricted()) {
            if (MainActivity.restrictedGroupsUnlocked) {
                viewHolder.grouptypeIcon.setImageResource(R.mipmap.group_type_lock_0);
            }
            else {
                viewHolder.grouptypeIcon.setImageResource(R.mipmap.group_type_lock_1);
            }
        }
        else {
            if (vodGroupL2.getId().equals("-5")) {
                viewHolder.grouptypeIcon.setImageResource(R.mipmap.group_type_favorite);
            } else {
                viewHolder.grouptypeIcon.setVisibility(View.GONE);
            }
        }

        viewHolder.groupName.setText((CharSequence)vodGroupL2.name.replaceAll(".*/", ""));
        if (selected) {
            viewHolder.groupName.setTextColor(-1);
            if (this.lastSelectedItem != super.mSelectedItem) {
                this.sendLoadChannelEvent(vodGroupL2);
                this.lastSelectedItem = super.mSelectedItem;
            }
        }
        else {
            viewHolder.groupName.setTextColor(-1879048193);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                notifyItemChanged(mSelectedItem);
                nextSelectItem = recyclerView.getChildLayoutPosition(view);
                mSelectedItem = recyclerView.getChildLayoutPosition(view);
                notifyItemChanged(mSelectedItem);
                if (vodGroupL2.isRestricted()) {
                    if (MainActivity.restrictedGroupsUnlocked) {
                        MainActivity.restrictedGroupsUnlocked = false;
                        notifyItemChanged(mSelectedItem);
                        viewHolder.grouptypeIcon.setImageResource(R.mipmap.group_type_lock_1);
                        sendLoadChannelEvent(vodGroupL2);
                    }
                    else {
                        showUnlockDialog(viewHolder, vodGroupL2);
                    }
                }
            }
        });
        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                viewHolder.itemView.setSelected(b);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.vod_group_item, viewGroup, false));
    }

    @Override
    public boolean onLastItemScrolled() {
        return true;
    }
}