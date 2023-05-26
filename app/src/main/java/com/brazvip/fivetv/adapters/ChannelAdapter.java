package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.layouts.MenuLayout;

import com.brazvip.fivetv.utils.RestApiUtils;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChannelAdapter extends BaseAdapter {

    public int mID;

    public List<ChannelBean> mList;

    public List<Integer> mArrayList;

    public Context mContext;

    public ListView mListView;

    public static int mChild;

    public View.OnTouchListener mChannelListTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //String log = "v: " + view.getId();
            MenuLayout.mTouchFlag = true;
            return false;
        }
    };

    public ChannelAdapter(int i, List<ChannelBean> list, Context context, ListView listView) {
        this.mID = i;
        this.mList = list;
        if (i == Constant.GROUP_ALL) {
            Collections.sort(this.mList, new Comparator<ChannelBean>() {
                @Override
                public int compare(ChannelBean o1, ChannelBean o2) {
                    return o1.getName().getInit().compareTo(o2.getName().getInit()) >= 0 ? 1 : -1;
                }
            });
        }
        this.mListView = listView;
        this.mArrayList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.channel_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        TextView textView = (TextView) view.findViewById(R.id.channel_name);
        String init = mList.get(i).getName().getInit();
        if (mID != -3 && mList.get(i).getSid() > 0) {
            init = mList.get(i).getSid() + "." + init;
        }
        textView.setText(init);
        TextView textView2 = (TextView) view.findViewById(R.id.program_item);
        String strEpgName = EPGInstance.getNameById(mList.get(i).getEpgSameAs() > 0 ? mList.get(i).getEpgSameAs() : mList.get(i).getChid());
        if (strEpgName.equals("")) {
            textView2.setVisibility(View.GONE);
        } else {
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(strEpgName);
        }
        view.setTag(mList.get(i));
        if (Config.f8924v) {
            view.setOnTouchListener(mChannelListTouchListener);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.channel_started);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.channel_type_icon);
        if (mList.get(i).getChid() == mChild) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(mList.get(i).getChid());
        if (ChannelInstance.favoriteLiveChannels.contains(sb.toString())) {
            imageView2.setVisibility(View.VISIBLE);
        } else {
            imageView2.setVisibility(View.GONE);
        }
        return view;
    }
}
