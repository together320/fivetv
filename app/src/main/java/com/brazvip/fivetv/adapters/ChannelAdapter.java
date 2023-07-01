package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.brazvip.fivetv.R;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.layouts.MenuLayout;
import com.zhy.autolayout.utils.AutoUtils;

/* loaded from: classes.dex */
public class ChannelAdapter extends BaseAdapter {
    public static int f8718a;
    public List<ChannelBean> channels;
    private int f8719b;
    public View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.getId();
            MenuLayout.fromTouch = true;
            return false;
        }
    };

    public ChannelAdapter(int i, List<ChannelBean> list, Context context, ListView listView) {
        this.f8719b = i;
        this.channels = list;
        if (i == -3) {
            this.channels.size();
            Collections.sort(this.channels, new Comparator<ChannelBean>() {
                @Override // java.util.Comparator
                public int compare(ChannelBean channelBean, ChannelBean channelBean2) {
                    return channelBean.getName().getInit().compareTo(channelBean2.getName().getInit()) >= 0 ? 1 : -1;
                }
            });
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.channels.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.channels.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.channel_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        TextView textView = (TextView) view.findViewById(R.id.channel_name);
        String init = this.channels.get(i).getName().getInit();
        if (this.f8719b != -3 && this.channels.get(i).getSid() > 0) {
            init = this.channels.get(i).getSid() + "." + init;
        }
        textView.setText(init);
        TextView textView2 = (TextView) view.findViewById(R.id.program_item);
        String m1335a = EPGInstance.getNameById(this.channels.get(i).getEpgSameAs() > 0 ? this.channels.get(i).getEpgSameAs() : this.channels.get(i).getChid());
        if (m1335a.equals("")) {
            textView2.setVisibility(View.GONE);
        } else {
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(m1335a);
        }
        view.setTag(this.channels.get(i));
        if (Config.f8924v) {
            view.setOnTouchListener(this.touchListener);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.channel_started);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.channel_type_icon);
        if (this.channels.get(i).getChid() == f8718a) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.channels.get(i).getChid());
        if (ChannelInstance.favoriteLiveChannels.contains(sb.toString())) {
            imageView2.setVisibility(View.VISIBLE);
        } else {
            imageView2.setVisibility(View.GONE);
        }
        return view;
    }
}