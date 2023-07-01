package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import com.brazvip.fivetv.beans.Group;
import com.zhy.autolayout.utils.AutoUtils;


public class MenuAdapter extends BaseAdapter {
    public Map<Integer, Group> groupedMenus;
    public final Integer[] menuGroupKeys;

    public MenuAdapter(Context context, Map<Integer, Group> map, ListView listView) {
        this.groupedMenus = map;
        Integer[] numArr = (Integer[]) map.keySet().toArray(new Integer[0]);
        this.menuGroupKeys = numArr;
        Arrays.sort(numArr);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.groupedMenus.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.groupedMenus.get(this.menuGroupKeys[i]);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View view, ViewGroup viewGroup) {
        int resId;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.group_item, null);
            AutoUtils.auto(view, 3, 3);
        }
        ImageView imageView = view.findViewById(R.id.group_type_icon);
        imageView.setVisibility(View.VISIBLE);
        int key = menuGroupKeys[position];
        Group group = groupedMenus.get(key);
        if (group != null) {
            ((TextView) view.findViewById(R.id.group_name)).setText(group.name);
            if (Objects.requireNonNull(group).restrictedAccess) {
                resId = MainActivity.restrictedGroupsUnlocked ? R.mipmap.group_type_lock_0 : R.mipmap.group_type_lock_1;
            } else if (group.type == -5) {
                resId = R.mipmap.group_type_favorite;
            } else if (group.type == -4 || group.type == 104) {
                resId = R.mipmap.group_type_playback;
            } else if (group.type != -3) {
                imageView.setVisibility(View.GONE);
                view.setTag(key);
                return view;
            } else {
                resId = R.mipmap.group_type_all;
            }
            imageView.setImageResource(resId);
        }
        view.setTag(key);

        return view;
    }
}
