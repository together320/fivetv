package com.brazvip.fivetv.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.utils.RestApiUtils;

import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.g 3478 */
/* loaded from: classes.dex */
public class EpgAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "EpgAdapter";

    public static String selectedChildId = "";

    public HashMap<Long, List<EpgBeans.EpgBean>> epgList;

    public ExpandableListView expandableListView;

    public List<Long> epgListKeys;

    public HashMap<Long, ArrayList<Integer>> f8732k;

    public String[] formattedDates;
    public int epgSelectedGroupPosition;

    public boolean isExpandGroup;

    public int liveChannelId;

    public long dayWithZeroTime;

    public int f13541m;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MM-dd", Locale.getDefault());

    public View.OnTouchListener mTouchListener = new View.OnTouchListener() { //View$OnTouchListenerC3477f
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //String log = " selected:" + view.isSelected() + " event: " + event.getAction();
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setSelected(true);
            view.requestFocus();
            view.requestFocusFromTouch();
            return false;
        }
    };

    public EpgAdapter(HashMap<Long, List<EpgBeans.EpgBean>> hashMap, ExpandableListView expandableListView, boolean z, int channelId) {
        this.f13541m = 0;
        this.liveChannelId = channelId;
        this.expandableListView = expandableListView;
        this.isExpandGroup = z;
        this.epgList = hashMap;
        ArrayList<Long> needRemoveBlocks = new ArrayList<>(hashMap.keySet());
        long time = new Date().getTime() + Utils.DELTA_TIME;
        dayWithZeroTime = Utils.getDayWithZeroTime(time);
        if (Config.isEpgReverseOrder) {
            Collections.sort(needRemoveBlocks, Collections.reverseOrder());
        } else {
            Collections.sort(needRemoveBlocks);
        }

        ArrayList arrayList = new ArrayList(hashMap.keySet());
        HashMap<Long, ArrayList<Integer>> hashMap2 = new HashMap<>();
        if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(Integer.valueOf(liveChannelId)) == null ||
                !ChannelInstance.liveChannels.get(Integer.valueOf(liveChannelId)).isHasPlayBack()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                long longValue = ((Long) it.next()).longValue();
                if (longValue < this.dayWithZeroTime) {
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

    @Override
    public Object getChild(int position, int childPosition) {
        return epgList.get(epgListKeys.get(position)).get(childPosition);
    }

    @Override
    public long getChildId(int position, int childPosition) {
        return childPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    //public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    public View getChildView(int i, int j, boolean z, View view, ViewGroup viewGroup) {
        Context context;
        int i3;
        if (this.epgListKeys.get(i) != null && this.f8732k.get(this.epgListKeys.get(i)) != null) {
            Iterator<Integer> it = this.f8732k.get(this.epgListKeys.get(i)).iterator();
            while (it.hasNext()) {
                if (it.next().intValue() <= j) {
                    j++;
                }
            }
        }
        List<EpgBeans.EpgBean> list = this.epgList.get(this.epgListKeys.get(i));
        String playbackUrl = list.get(j).getPlaybackUrl();
        if (view == null) {
            if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(Integer.valueOf(this.liveChannelId)) == null ||
                    !ChannelInstance.liveChannels.get(Integer.valueOf(this.liveChannelId)).isHasPlayBack()) {
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
            if (ChannelInstance.liveChannels == null || ChannelInstance.liveChannels.get(Integer.valueOf(this.liveChannelId)) == null ||
                    ChannelInstance.liveChannels.get(Integer.valueOf(this.liveChannelId)).isHasPlayBack()) {
                textView.setText(list.get(j).getName());
                if (playbackUrl == null || playbackUrl.equals("")) {
                    imageView.setVisibility(View.GONE);
                    textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                    textView2.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.white));
                    textView2.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_sub_txt));
                    ((ImageView) view.findViewById(R.id.epg_item_icon)).setImageResource(list.get(j).getId().equals(selectedChildId) ? R.mipmap.live_play : R.mipmap.live);
                }
                String millisToHoursAndMinutes = Utils.millisToHoursAndMinutes(list.get(j).getTime());
                String millisToHoursAndMinutes2 = Utils.millisToHoursAndMinutes(list.get(j).getEndTime());
                textView2.setText(millisToHoursAndMinutes + "-" + millisToHoursAndMinutes2);
                view.setTag(list.get(j));
            } else {
                textView.setTextColor(SopApplication.getSopContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                textView.setText(Utils.millisToHoursAndMinutes(list.get(j).getTime()) + " " + list.get(j).getName());
            }
            view.setTag(R.id.chid, String.valueOf(this.liveChannelId));
        }
        return view;
    }

    @Override
    public int getChildrenCount(int i) {
        List<EpgBeans.EpgBean> list = epgList.get(epgListKeys.get(i));
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size() - (this.f8732k.get(this.epgListKeys.get(i)) != null ? this.f8732k.get(this.epgListKeys.get(i)).size() : 0);
    }

    @Override
    public Object getGroup(int i) {
        return epgListKeys.get(i);
    }

    @Override
    public int getGroupCount() {
        return Math.max(epgListKeys.size(), 0);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.epg_day_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        TextView textView = (TextView) view.findViewById(R.id.epg_day_text);
        if (this.epgListKeys.get(i) != null && this.epgListKeys.size() > 0) {
            textView.setText(this.formattedDates[i]);
            ((ImageView) view.findViewById(R.id.epg_group_arrow)).setImageResource(z ? R.mipmap.up : R.mipmap.down);
            if (dayWithZeroTime == this.epgListKeys.get(i).longValue()) {
                if (this.isExpandGroup) {
                    this.expandableListView.expandGroup(i);
                } else {
                    this.expandableListView.collapseGroup(i);
                }
                this.epgSelectedGroupPosition = i;
            }
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
