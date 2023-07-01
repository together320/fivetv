package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.brazvip.fivetv.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.utils.AutoUtils;

/* loaded from: classes.dex */
public class EpgAdapter extends BaseExpandableListAdapter {
    public static String f8722a = "";
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MM-dd", Locale.getDefault());
    public HashMap<Long, List<EpgBeans.EpgBean>> epgList;
    public List<Long> epgListKeys;
    public ExpandableListView expandableListView;
    public int f8727f;
    public boolean f8728g;
    public long f8731j;
    public HashMap<Long, ArrayList<Integer>> f8732k;
    public String[] formattedDates;
    public int liveChannelId;

    public EpgAdapter(HashMap<Long, List<EpgBeans.EpgBean>> hashMap, ExpandableListView expandableListView, boolean z, int i) {
        this.liveChannelId = i;
        this.expandableListView = expandableListView;
        this.f8728g = z;
        this.epgList = hashMap;
        ArrayList arrayList = new ArrayList(hashMap.keySet());
        long time = new Date().getTime() + Utils.DELTA_TIME;
        this.f8731j = Utils.getDayWithZeroTime(time);
        if (Config.f8907e) {
            Collections.sort(arrayList, Collections.reverseOrder());
        } else {
            Collections.sort(arrayList);
        }
        HashMap<Long, ArrayList<Integer>> hashMap2 = new HashMap<>();
        if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(i) == null ||
                !ChannelInstance.liveChannels.get(i).isHasPlayBack()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                long longValue = (Long) it.next();
                if (longValue < this.f8731j) {
                    it.remove();
                } else {
                    List<EpgBeans.EpgBean> list = hashMap.get(Long.valueOf(longValue));
                    ArrayList<Integer> arrayList2 = new ArrayList<>();
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        if (list.get(i2).getTime().longValue() < time) {
                            arrayList2.add(Integer.valueOf(i2));
                        }
                    }
                    hashMap2.put(Long.valueOf(longValue), arrayList2);
                }
            }
        }
        this.epgListKeys = arrayList;
        this.formattedDates = new String[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            this.formattedDates[i3] = this.dateFormat.format(this.epgListKeys.get(i3));
        }
        this.f8732k = hashMap2;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        return this.epgList.get(this.epgListKeys.get(i)).get(i2);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        Context context;
        int i3;
        if (this.epgListKeys.get(i) != null && this.f8732k.get(this.epgListKeys.get(i)) != null) {
            for (Integer integer : this.f8732k.get(this.epgListKeys.get(i))) {
                if (integer <= i2) {
                    i2++;
                }
            }
        }
        List<EpgBeans.EpgBean> list = this.epgList.get(this.epgListKeys.get(i));
        String playbackUrl = list.get(i2).getPlaybackUrl();
        if (view == null) {
            if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(this.liveChannelId) == null ||
                    !ChannelInstance.liveChannels.get(this.liveChannelId).isHasPlayBack()) {
                context = viewGroup.getContext();
                i3 = R.layout.epg_item_1line;
            } else {
                context = viewGroup.getContext();
                i3 = R.layout.epg_item;
            }
            view = View.inflate(context, i3, null);
            AutoUtils.auto(view, 3, 3);
        }
        TextView textView = (TextView) view.findViewById(R.id.epg_item_name);
        TextView textView2 = (TextView) view.findViewById(R.id.epg_item_time);
        ImageView imageView = (ImageView) view.findViewById(R.id.epg_item_icon);
        if (list.size() > 0) {
            if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(this.liveChannelId) == null ||
                    ChannelInstance.liveChannels.get(this.liveChannelId).isHasPlayBack()) {
                textView.setText(list.get(i2).getName());
                if (playbackUrl == null || playbackUrl.equals("")) {
                    imageView.setVisibility(View.GONE);
                    textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                    textView2.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.white));
                    textView2.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_sub_txt));
                    ((ImageView) view.findViewById(R.id.epg_item_icon)).setImageResource(list.get(i2).getId().equals(f8722a) ? R.mipmap.live_play : R.mipmap.live);
                }
                String millisToHoursAndMinutes = Utils.millisToHoursAndMinutes(list.get(i2).getTime());
                String millisToHoursAndMinutes2 = Utils.millisToHoursAndMinutes(list.get(i2).getEndTime());
                textView2.setText(millisToHoursAndMinutes + "-" + millisToHoursAndMinutes2);
                view.setTag(list.get(i2));
            } else {
                textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                textView.setText(Utils.millisToHoursAndMinutes(list.get(i2).getTime()) + " " + list.get(i2).getName());
            }
            view.setTag(R.id.chid, String.valueOf(this.liveChannelId));
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        List<EpgBeans.EpgBean> list = this.epgList.get(this.epgListKeys.get(i));
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return list.size() - (this.f8732k.get(this.epgListKeys.get(i)) != null ? this.f8732k.get(this.epgListKeys.get(i)).size() : 0);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return this.epgListKeys.get(i);
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return Math.max(this.epgListKeys.size(), 0);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.epg_day_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        TextView textView = (TextView) view.findViewById(R.id.epg_day_text);
        if (this.epgListKeys.get(i) != null && this.epgListKeys.size() > 0) {
            textView.setText(this.formattedDates[i]);
            ((ImageView) view.findViewById(R.id.epg_group_arrow)).setImageResource(z ? R.mipmap.up : R.mipmap.down);
            if (this.f8731j == this.epgListKeys.get(i).longValue()) {
                if (this.f8728g) {
                    this.expandableListView.expandGroup(i);
                } else {
                    this.expandableListView.collapseGroup(i);
                }
                this.f8727f = i;
            }
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}