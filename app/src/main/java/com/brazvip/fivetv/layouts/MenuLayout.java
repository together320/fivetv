package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;

import java.util.ArrayList;
import java.util.List;

public class MenuLayout extends RelativeLayout {
    public ListView mGroupListView;
    public ListView mChannelListView;
    public ExpandableListView mEpgListView;

    public MenuLayout(Context context) {
        super(context);
        init(context);
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.menu_layout, this, true);

        initComponents();
    }

    private void initComponents() {
        mGroupListView = (ListView) findViewById(R.id.group_listview);
        mChannelListView = (ListView) findViewById(R.id.channel_listview);
        mEpgListView = (ExpandableListView) findViewById(R.id.epg_listview);
    }

    public void loadGroup() {
        mGroupListView.setVisibility(VISIBLE);
        mChannelListView.setVisibility(GONE);
        mEpgListView.setVisibility(GONE);

        loadEpgById(81);
    }

    public void loadChannel() {
        mGroupListView.setVisibility(VISIBLE);
        mChannelListView.setVisibility(VISIBLE);
        mEpgListView.setVisibility(GONE);
    }

    public void loadEpg() {
        mGroupListView.setVisibility(VISIBLE);
        mChannelListView.setVisibility(VISIBLE);
        mEpgListView.setVisibility(VISIBLE);
    }

    public List<ChannelBean> loadChannelByGroup(int groupId) {
        List<ChannelBean> channels = new ArrayList<>();
        for (ChannelBean channel : ChannelInstance.mChannels) {
            if (groupId == Constant.GROUP_FAVORITE) {

            }
            else if (groupId == Constant.GROUP_ALL) {
                if (channel.getLevel() == 18)
                    continue;
                channels.add(channel);
            }
            else {
                List<ChannelBean.TagsBean> tags = channel.getTags();
                for (ChannelBean.TagsBean tag : tags) {
                    if (tag.getId() == groupId) {
                        channels.add(channel);
                        break;
                    }
                }
            }
        }

        return channels;
    }

    public List<EpgBeans> loadEpgById(int id) {
        List<EpgBeans> epgs = new ArrayList<>();
        for (EpgBeans epg : EPGInstance.mEpgs) {
            if (epg.getId() == id)
                epgs.add(epg);
        }

        return epgs;
    }
}