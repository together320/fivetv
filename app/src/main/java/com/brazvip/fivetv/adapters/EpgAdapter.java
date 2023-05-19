package com.brazvip.fivetv.adapters;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.RestApiUtils;

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

    /* renamed from: a 13529 */
    public static final String TAG = "EpgAdapter";

    /* renamed from: b */
    public static String f13530b = "";

    /* renamed from: c */
    public HashMap<Long, List<EpgBeans.EpgBean>> f13531c;

    /* renamed from: d */
    public ExpandableListView f13532d;

    /* renamed from: e */
    public List<Long> f13533e;

    /* renamed from: f */
    public String[] f13534f;

    /* renamed from: g */
    public int f13535g;

    /* renamed from: h */
    public boolean f13536h;

    /* renamed from: i */
    public int f13537i;

    /* renamed from: l */
    public long f13540l;

    /* renamed from: m */
    public int f13541m;

    /* renamed from: j */
    public SimpleDateFormat f13538j = new SimpleDateFormat("EEE, MM-dd", Locale.getDefault());

    /* renamed from: k */
    public SimpleDateFormat f13539k = new SimpleDateFormat("EEE MM-dd-hh-mm", Locale.getDefault());

    /* renamed from: n 13542 */
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

    public EpgAdapter(HashMap<Long, List<EpgBeans.EpgBean>> hashMap, ExpandableListView listView, boolean z, int progress) {
        this.f13541m = 0;
        this.f13537i = progress;
        this.f13532d = listView;
        this.f13536h = z;
        this.f13531c = hashMap;
        ArrayList<Long> needRemoveBlocks = new ArrayList<>(hashMap.keySet());
        long time = new Date().getTime() + PrefUtils.f14035a;
        f13540l = PrefUtils.getDateOfTime(time);
        if (RestApiUtils.f13740e) {
            Collections.sort(needRemoveBlocks, Collections.reverseOrder());
        } else {
            Collections.sort(needRemoveBlocks);
        }
        if (RestApiUtils.f13742g && progress == 100) {
            Iterator<Long> it = needRemoveBlocks.iterator();
            while (it.hasNext()) {
                long pos = it.next().longValue();
                if (pos < f13540l) {
                    it.remove();
                } else {
                    List<EpgBeans.EpgBean> list = hashMap.get(Long.valueOf(pos));
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getEndTime().longValue() >= time) {
                                f13541m = i;
                                break;
                            }
                        }
                    }
                }
            }
        }
        f13533e = needRemoveBlocks;
        int count = needRemoveBlocks.size();
        f13534f = new String[count];
        for (int i = 0; i < count; i++) {
            f13534f[i] = f13538j.format(f13533e.get(i));
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int position, int childPosition) {
        return f13531c.get(f13533e.get(position)).get(childPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int position, int childPosition) {
        return childPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int targetPos = childPosition + f13541m;
        List<EpgBeans.EpgBean> list = this.f13531c.get(this.f13533e.get(groupPosition));
        if (convertView == null) {
            if (f13537i == 100) {
                convertView = View.inflate(parent.getContext(), R.layout.epg_item_1line, null);
            } else {
                convertView = View.inflate(parent.getContext(), R.layout.epg_item, null);
            }
            AutoUtils.auto(convertView, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.epg_item_name);
        TextView tvTime = (TextView) convertView.findViewById(R.id.epg_item_time);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.epg_item_icon);
        if (list != null && list.size() > 0) {
            if (f13537i == 100) {
                tvName.setTextColor(SopApplication.getAppContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                tvName.setText(PrefUtils.m2249a(list.get(targetPos).getTime()) + " " + list.get(targetPos).getName());
            } else {
                tvName.setText(list.get(targetPos).getName());
                String playbackUrl = list.get(targetPos).getPlaybackUrl();
                if (playbackUrl != null && !playbackUrl.equals("")) {
                    ivIcon.setVisibility(View.VISIBLE);
                    tvName.setTextColor(SopApplication.getAppContext().getResources().getColor(R.color.white));
                    tvTime.setTextColor(SopApplication.getAppContext().getResources().getColor(R.color.channel_epg_sub_txt));
                    ImageView imageView2 = (ImageView) convertView.findViewById(R.id.epg_item_icon);
                    if (list.get(targetPos).getId().equals(f13530b)) {
                        imageView2.setImageResource(R.mipmap.live_play);
                    } else {
                        imageView2.setImageResource(R.mipmap.live);
                    }
                } else {
                    ivIcon.setVisibility(View.GONE);
                    tvName.setTextColor(SopApplication.getAppContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                    tvTime.setTextColor(SopApplication.getAppContext().getResources().getColor(R.color.channel_epg_no_addr_txt));
                }
                tvTime.setText(PrefUtils.m2249a(list.get(targetPos).getTime()) + "-" + PrefUtils.m2249a(list.get(targetPos).getEndTime()));
                convertView.setTag(list.get(targetPos));
            }
        }
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        List<EpgBeans.EpgBean> list = f13531c.get(f13533e.get(groupPosition));
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return list.size() - f13541m;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return f13533e.get(i);
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return Math.max(f13533e.size(), 0);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.epg_day_item, null);
            AutoUtils.auto(convertView, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
        }
        TextView dayText = (TextView) convertView.findViewById(R.id.epg_day_text);
        if (this.f13533e.get(groupPosition) != null && this.f13533e.size() > 0) {
            dayText.setText(this.f13534f[groupPosition]);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.epg_group_arrow);
            if (isExpanded) {
                imageView.setImageResource(R.mipmap.down);
            } else {
                imageView.setImageResource(R.mipmap.up);
            }
            if (RestApiUtils.f13742g) {
                if (this.f13540l == this.f13533e.get(groupPosition).longValue() && this.f13537i == 100) {
                    if (this.f13536h) {
                        this.f13532d.expandGroup(groupPosition);
                    } else {
                        this.f13532d.collapseGroup(groupPosition);
                    }
                    this.f13535g = groupPosition;
                }
            } else if (this.f13540l == this.f13533e.get(groupPosition).longValue()) {
                if (this.f13536h) {
                    this.f13532d.expandGroup(groupPosition);
                } else {
                    this.f13532d.collapseGroup(groupPosition);
                }
                this.f13535g = groupPosition;
            }
        }
        return convertView;
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
