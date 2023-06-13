package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.dialogs.EpisodeDialog;
import com.brazvip.fivetv.Config;

import java.util.List;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.i 3480 */
/* loaded from: classes.dex */
public class EpisodeAdapter extends EpisodeGridRecyclerViewAdapter<EpisodeAdapter.ViewHolder> {

    /* renamed from: h */
    public static final String TAG = "EpisodeAdapter";

    /* renamed from: i */
    public static final int f13547i = -1;

    /* renamed from: j */
    public static final int f13548j = -1879048193;

    /* renamed from: k */
    public List<ChannelBean.SourcesBean> f13549k;

    /* renamed from: l */
    public Context f13550l;

    /* renamed from: m */
    public String f13551m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a.i$a */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /* renamed from: I */
        public TextView f13552I;

        public ViewHolder(View view) {
            super(view);
            this.f13552I = (TextView) view.findViewById(R.id.episode_name);
            AutoUtils.auto(view, 3, 3);
        }
    }

    public EpisodeAdapter(List<ChannelBean.SourcesBean> list, String channelName, Context context) {
        super(context);
        this.f13550l = context;
        this.f13549k = list;
        this.f13551m = channelName;
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override
    /* renamed from: a */
    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean z = false;
        boolean z2 = position == getPosition();
        if (z2 && m2487g() >= 0) {
            z = true;
        }
        holder.itemView.setSelected(z2);
        ChannelBean.SourcesBean sourcesBean = this.f13549k.get(position);
        String subTitle = sourcesBean.getSubTitle();
        if (getItemCount() == 1) {
            subTitle = getContext().getString(R.string.Play);
        }
        String address = sourcesBean.getAddress();
        holder.f13552I.setText(subTitle);
        if (z) {
            holder.f13552I.setTextColor(f13547i);
            holder.itemView.setBackgroundResource(R.mipmap.episode_focus);
        } else {
            holder.f13552I.setTextColor(f13548j);
            holder.itemView.setBackgroundResource(R.mipmap.episode_unfocus);
        }
        String finalSubTitle = subTitle;
        holder.itemView.setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3479h
            @Override
            public void onClick(View v) {
                EpisodeAdapter adapter = EpisodeAdapter.this;
                adapter.notifyItemChanged(adapter.getPosition());
                adapter.setLayoutPosition(adapter.getRecycleView().getChildLayoutPosition(v));
                adapter.setPosition(adapter.getRecycleView().getChildLayoutPosition(v));
                adapter.notifyItemChanged(adapter.getPosition());
                if (address == null || address.equals("")) {
                    return;
                }
                adapter.m2497a(adapter.f13551m + " " + finalSubTitle, address);
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override 
    /* renamed from: b */
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(this.f13550l).inflate(R.layout.vod_episode_item, viewGroup, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public void m2497a(String channelName, String url) {
        Message msg = new Message();
        msg.what = 80;
        Bundle params = new Bundle();
        params.putString("url", url);
        params.putString("name", channelName);
        params.putString("type", (url.indexOf("tvcar://") >= 0 ? Config.BS_MODE.BSVOD : Config.BS_MODE.STATIC).name());
        msg.setData(params);
        MainActivity.mMsgHandler.sendMessage(msg);
        EpisodeDialog.Helper.hide();
    }

    @Override
    /* renamed from: a */
    public int getItemCount() {
        return this.f13549k.size();
    }
}
