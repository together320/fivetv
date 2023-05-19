package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.layouts.MenuLayout;

import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuChannelListAdapter extends BaseAdapter {

    /* renamed from: a */
    public static int f13521a;

    /* renamed from: b */
    public int f13522b;

    /* renamed from: c */
    public List<ChannelBean> f13523c;

    /* renamed from: d */
    public List<Integer> f13524d;

    /* renamed from: e */
    public Context f13525e;

    /* renamed from: f */
    public ListView f13526f;

    /* renamed from: g */
    public View.OnTouchListener f13527g = new View.OnTouchListener() { //View$OnTouchListenerC3475d
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //String log = "v: " + view.getId();
            MenuLayout.f13821j = true;
            return false;
        }
    };

    public MenuChannelListAdapter(int i, List<ChannelBean> list, Context context, ListView listView) {
        this.f13522b = i;
        this.f13523c = list;
        if (i == -4) {
            //String log = "loadChannelData,A_Z sorted, size:" + f13523c.size();
            Collections.sort(this.f13523c, new Comparator<ChannelBean>() { //C3474c, e.b.a.a.c
                @Override
                public int compare(ChannelBean o1, ChannelBean o2) {
                    return o1.getName().getInit().compareTo(o2.getName().getInit()) >= 0 ? 1 : -1;
                }
            });
        }
        this.f13526f = listView;
        this.f13524d = new ArrayList<>();
        this.f13525e = context;
    }

    /* renamed from: a */
    public boolean m2502a(int i) {
        return f13524d.contains(Integer.valueOf(i));
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f13523c.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return f13523c.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.channel_item, null);
            AutoUtils.auto(convertView, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
        }
        ChannelBean channel = f13523c.get(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.channel_name);
        String name = channel.getName().getInit();
        if (this.f13522b != -4 && channel.getSid() > 0) {
            name = channel.getSid() + "." + name;
        }
        TextView tvProgram = (TextView) convertView.findViewById(R.id.program_item);
        //if (BSChannel.f13643f.contains("" + channel.getChid())) {
        //    tvName.setText("★ " + name);
        //} else {
            tvName.setText(name);
        //}
        //String programId = BSEPG.getNameById(channel.getEpgSameAs() > 0 ? channel.getEpgSameAs() : channel.getChid());
        int epg = channel.getEpgSameAs();
        if (epg < 1) epg = channel.getChid();
//        String programId = BSEPG.getNameById(epg);
//        if (programId.equals("")) {
//            tvProgram.setVisibility(View.GONE);
//        } else {
//            tvProgram.setVisibility(View.VISIBLE);
//            tvProgram.setText(programId);
//        }
//        convertView.setTag(channel);
//        if (RestApiUtils.f13760y) {
//            convertView.setOnTouchListener(f13527g);
//        }
        ImageView ivStarted = (ImageView) convertView.findViewById(R.id.channel_started);
        if (channel.getChid() == f13521a) {
            ivStarted.setVisibility(View.VISIBLE);
        } else {
            ivStarted.setVisibility(View.GONE);
        }
        return convertView;
    }

    /* renamed from: a */
    public void m2501a(ListView listView, int position) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (position < firstVisiblePosition || position > lastVisiblePosition) {
            return;
        }
        getView(position, listView.getChildAt(position - firstVisiblePosition), listView);
    }
}
