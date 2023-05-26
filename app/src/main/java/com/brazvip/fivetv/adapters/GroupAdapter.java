package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
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

import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.r 3490 */
/* loaded from: classes.dex */
public class GroupAdapter extends BaseAdapter {

    public Map<Integer, Group> mGroupData;

    public final Integer[] mIndices;

    public ListView mListView;

    public Context mContext;

    public GroupAdapter(Context context, Map<Integer, Group> mapData, ListView listView) {
        mContext = context;
        mGroupData = mapData;
        Set<Integer> keySet = mapData.keySet();
        mIndices = (Integer[]) keySet.toArray(new Integer[0]); //new Integer[keySet.size()]
        Arrays.sort(mIndices);
        mListView = listView;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mGroupData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mGroupData.get(mIndices[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int icon = 0;
        if (view == null) {
            view = View.inflate(parent.getContext(), R.layout.group_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.group_type_icon);
        imageView.setVisibility(View.VISIBLE);
        int intValue = this.mIndices[position].intValue();
        ((TextView) view.findViewById(R.id.group_name)).setText(this.mGroupData.get(Integer.valueOf(intValue)).name);
        if (this.mGroupData.get(Integer.valueOf(intValue)).restrictedAccess) {
            icon = MainActivity.isRestrictedAccess ? R.mipmap.group_type_lock_0 : R.mipmap.group_type_lock_1;
        } else if (this.mGroupData.get(Integer.valueOf(intValue)).type == -5) {
            icon = R.mipmap.group_type_favorite;
        } else if (this.mGroupData.get(Integer.valueOf(intValue)).type == -4 || this.mGroupData.get(Integer.valueOf(intValue)).type == 104) {
            icon = R.mipmap.group_type_playback;
        } else if (this.mGroupData.get(Integer.valueOf(intValue)).type != -3) {
            imageView.setVisibility(View.GONE);
            view.setTag(Integer.valueOf(intValue));
            return view;
        } else {
            icon = R.mipmap.group_type_all;
        }
        imageView.setImageResource(icon);
        view.setTag(Integer.valueOf(intValue));
        return view;
    }
}
