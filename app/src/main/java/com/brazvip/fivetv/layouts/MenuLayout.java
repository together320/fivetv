package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.adapters.GroupAdapter;
import com.brazvip.fivetv.adapters.ChannelAdapter;
import com.brazvip.fivetv.adapters.EpgAdapter;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.dialogs.PasswordChangeDialog;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MenuLayout extends RelativeLayout {
    public ListView mGroupListView;
    public ListView mChannelListView;
    public ExpandableListView mEpgListView;

    public GroupAdapter mGroupListAdapter;

    public ChannelAdapter mChannelListAdapter;

    public EpgAdapter mEpgAdapter;

    public static Handler mMsgHandler = null;

    public HashMap<Integer, Group> mGroupMap;

    public View mSelectedGroupView;
    public View mSelectedChannelView = null;

    public View mSelectedEPGView = null;

    public Config.CHANNEL_TYPE mChannelType;
    
    public static int mSelectedChannelChild = 0;

    public static boolean mTouchFlag = false;

    public boolean isItemSelected = true;

    public boolean f13823B = true;

    public boolean f13824C = false;

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

        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int code = message.what;
                if (code == 1) {
                    loadGroupData();
                } else if (code == 2) {
                    loadChannelData(message.arg1);
                } else if (code == 3) {
                    loadEpgListChannel(message.arg1);
                }
                super.handleMessage(message);
            }
        };

        initComponents();
        loadGroup();
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

        loadGroupData();
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

    public void loadGroupData() {
        try {
            mGroupMap = ChannelInstance.groupChannelMap;
            if (mGroupMap == null || mGroupMap.size() == 0) {
                return;
            }

            mGroupListAdapter = new GroupAdapter(getContext(), mGroupMap, mGroupListView);
            mGroupListView.setAdapter(mGroupListAdapter);
            if (this.mGroupListView.getVisibility() == View.GONE) {
                this.mGroupListView.setVisibility(View.VISIBLE);
            }

            mGroupListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public boolean isSelected = true;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (view != null) {
                        mSelectedGroupView = view;
                        isItemSelected = true;

                        int arg = ((Integer) view.getTag()).intValue();
                        MenuLayout.mMsgHandler.removeMessages(2);
                        MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, arg, 0));

                        if (isSelected) {
                            isSelected = false;
                        } else {
                            view.setBackgroundResource(R.drawable.group_focus_bg);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mGroupListView.requestFocusFromTouch();
                    mGroupListView.setSelection(position);
                    mChannelType = Config.CHANNEL_TYPE.GROUP;
                    mSelectedGroupView = view;

                    Integer tag = (Integer) view.getTag();
                    if (tag != null) {
                        int index = tag.intValue();
                        if (mGroupMap != null) {
                            Group group = mGroupMap.get(tag);
                            if ((group != null) && group.restrictedAccess) {
                                if (MainActivity.isRestrictedAccess) {
                                    MainActivity.isRestrictedAccess = false;
                                    mGroupListAdapter.notifyDataSetChanged();
                                    MenuLayout.mMsgHandler.removeMessages(2);
                                    MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, index, 0));
                                } else {
                                    PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(getContext());
                                    helper.setClickListener(new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3544g(this, index, position, view)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.isRestrictedAccess = true;
                                            mGroupListAdapter.notifyDataSetChanged();
                                            MenuLayout.mMsgHandler.removeMessages(2);
                                            MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, index, 0));
                                            mGroupListView.requestFocusFromTouch();
                                            mGroupListView.setSelection(position);
                                            view.setBackgroundResource(R.drawable.group_focus_bg);
                                        }
                                    });
                                    helper.create().show();
                                }
                            }
                        }
                        MenuLayout.mMsgHandler.removeMessages(2);
                        MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, index, 0));
                        mGroupListAdapter.notifyDataSetChanged();
                    }
                    view.setBackgroundResource(R.drawable.group_focus_bg);
                }
            });
            mGroupListView.setVerticalScrollBarEnabled(false);

            Integer[] numArr = (Integer[]) this.mGroupMap.keySet().toArray(new Integer[0]);
            if (numArr == null || numArr.length <= 0) {
                return;
            }
            Arrays.sort(numArr, new Comparator<Integer>() {
                @Override
                public int compare(Integer num, Integer num2) {
                    if (num == null && num2 == null) {
                        return 0;
                    }
                    if (num == null) {
                        return -1;
                    }
                    if (num2 == null) {
                        return 1;
                    }
                    return num.compareTo(num2);
                }
            });
            Integer num = numArr[0];
            if (num == null) {
                return;
            }
            int arg = num.intValue();
            MenuLayout.mMsgHandler.removeMessages(2);
            MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, arg, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChannelData(int key) {
        if (mChannelListView == null)
            return;
        Group group = mGroupMap.get(key);
        List<ChannelBean> channels = loadChannelByGroup(key);
        if (channels != null && channels.size() > 0) {
            if (!MainActivity.isRestrictedAccess && group.restrictedAccess) {
                //String log = "restrictedAccess: " + mShowGroups.get(key).name;
                if (!MainActivity.f16805n || mChannelListView.hasFocus() || mEpgListView.hasFocus()) {
                    mGroupListView.requestFocus();
                    mChannelType = Config.CHANNEL_TYPE.GROUP;
                }
                mChannelListView.setVisibility(View.GONE);
                mEpgListView.setVisibility(View.GONE);
                return;
            }
            try {
                mChannelListAdapter = new ChannelAdapter(key, channels, getContext(), mChannelListView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mChannelListAdapter == null) {
                return;
            }
            mChannelListView.setAdapter((ListAdapter) mChannelListAdapter);
            if (mChannelListView.getVisibility() == View.GONE) {
                mChannelListView.setVisibility(View.VISIBLE);
            }
            mChannelListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //C3546i
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (view != null) {
                        mSelectedChannelView = view;
                        MenuLayout.mMsgHandler.removeMessages(3);
                        f13823B = true;
                        ChannelBean channel = (ChannelBean) view.getTag();
                        if (channel != null) {
                            //int epg = channel.getEpgSameAs() > 0 ? channel.getEpgSameAs() : channel.getChid();
                            int epg = channel.getEpgSameAs();
                            if (epg < 1) epg = channel.getChid();
                            Message msg = new Message();
                            msg.what = 3;
                            msg.arg1 = epg;
                            MenuLayout.mMsgHandler.sendMessage(msg);
                            if (isItemSelected) {
                                isItemSelected = false;
                                view.clearFocus();
                            } else {
                                view.setBackgroundResource(R.drawable.channel_focus_bg);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mChannelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mChannelListView.requestFocusFromTouch();
                    mChannelListView.setSelection(position);

                    ChannelBean channel = (ChannelBean) view.getTag();
                    if (channel != null) {
//                        List<ChannelBean.SourcesBean> sources = channel.getSources();
//                        if (sources == null || sources.size() == 0)
//                            return false;
//                        String source = sources.get(0).getAddress();
//                        if ((source == null) || source.isEmpty())
//                            return false;
                        mChannelListAdapter.notifyDataSetChanged();
                        if (mEpgAdapter != null) {
                            EpgAdapter.selectedChildId = "";
                            mEpgAdapter.notifyDataSetChanged();
                        }
                        int child = channel.getChid();
                        if (ChannelInstance.favoriteLiveChannels.contains("" + child)) {
                            Toast.makeText(getContext(), channel.getName().getInit() + " " +
                                    getContext().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
                            ChannelInstance.favoriteLiveChannels.remove("" + child);
                            PrefUtils.setPrefStringSet(Config.SP_FAV_LIVE_CHANNEL, ChannelInstance.favoriteLiveChannels);
                            ChannelInstance.channelGrouping();
                            if (key == Constant.GROUP_FAVORITE) {
                                List<ChannelBean> list2 = ChannelInstance.groupChannelMap.get(Constant.GROUP_FAVORITE).channnels;
                                if (list2 != null && list2.size() > 0) {
                                    mChannelListAdapter.mList = list2;
                                    mChannelListAdapter.notifyDataSetChanged();
                                    return true;
                                }
                                MainActivity.mMsgHandler.sendEmptyMessage(111);
                            } else
                                mChannelListAdapter.notifyDataSetChanged();
                            return true;
                        }
                        Toast.makeText(getContext(), channel.getName().getInit() + " " +
                                getContext().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                        ChannelInstance.favoriteLiveChannels.add("" + child);
                        PrefUtils.setPrefStringSet(Config.SP_FAV_LIVE_CHANNEL, ChannelInstance.favoriteLiveChannels);
                        ChannelInstance.channelGrouping();
                        if ((mGroupMap != null) && (mGroupMap.size() > 0)) {
                            mGroupListAdapter.mGroupData = mGroupMap;
                            mGroupListAdapter.notifyDataSetChanged();
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
            });

            mChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //C3548k
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mChannelListView.requestFocusFromTouch();
                    mChannelListView.setSelection(position);
                    //String log = "################## onItemClick " + position + " from touch: " + MenuLayout.mTouchFlag;
                    ChannelBean channel = (ChannelBean) view.getTag();
                    if (MenuLayout.mTouchFlag && MenuLayout.mSelectedChannelChild != channel.getChid()) {
                        MenuLayout.mSelectedChannelChild = channel.getChid();
                        //int epg = channel.getEpgSameAs() > 0 ? channel.getEpgSameAs() : channel.getChid();
                        int epg = channel.getEpgSameAs();
                        if (epg < 1) epg = channel.getChid();
                        MenuLayout.sendLoadEpgEvent(epg);
                    } else if (channel != null) { //cond_1
                        List<ChannelBean.SourcesBean> sources = channel.getSources();
                        if (sources == null || sources.size() == 0 || sources.get(0) == null)
                            return; //cond_5, goto_1
                        String address = sources.get(0).getAddress();
                        if (address == null || address.isEmpty())
                            return;
                        ChannelAdapter.mChild = channel.getChid();
                        if (mEpgAdapter != null) {
                            EpgAdapter.selectedChildId = "";
                            mEpgAdapter.notifyDataSetChanged();
                        }
                        /*address = channel.getSources().get(0).getAddress();
                        if (address == null || address.equals("")) {
                            return;
                        }*/
                        startLiveChannel(channel);
                        mChannelType = Config.CHANNEL_TYPE.CHANNEL;
                        mSelectedChannelView = view;
                    }
                    //cond_6, goto_2
                    MenuLayout.mTouchFlag = false;
                    mChannelListAdapter.notifyDataSetChanged();
                    view.setBackgroundResource(R.drawable.channel_focus_bg);
                }
            });

            int epg = ((ChannelBean) channels.get(0)).getEpgSameAs() > 0 ?
                    ((ChannelBean) channels.get(0)).getEpgSameAs() :
                    ((ChannelBean) channels.get(0)).getChid();
            Message message = new Message();
            message.what = 3;
            message.arg1 = epg;
            mMsgHandler.sendMessage(message);
            return;
        }

        if (!MainActivity.f16805n || mChannelListView.hasFocus() || mEpgListView.hasFocus()) {
            mGroupListView.requestFocus();
            mChannelType = Config.CHANNEL_TYPE.GROUP;
        }
        mChannelListView.setVisibility(View.GONE);
        mEpgListView.setVisibility(View.GONE);
    }

    public List<ChannelBean> loadChannelByGroup(int groupId) {
        List<ChannelBean> channels = new ArrayList<>();

        if (groupId == Constant.GROUP_FAVORITE) {
            for (ChannelBean channel : ChannelInstance.channelList) {
                if (ChannelInstance.favoriteLiveChannels.contains("" + channel.getChid())) {
                    channels.add(channel);
                }
            }
        } else {
            channels = mGroupMap.get(groupId).channnels;
        }

        return channels;
    }

    public void loadEpgListChannel(int index) {
        if (mEpgListView == null ||
            EPGInstance.liveEpgsMap == null ||
            EPGInstance.liveEpgsMap.get(Integer.valueOf(index)) == null ||
            ((HashMap) EPGInstance.liveEpgsMap.get(Integer.valueOf(index))).size() == 0) {

            ExpandableListView expandableListView = mEpgListView;
            if (expandableListView != null) {
                expandableListView.setVisibility(View.GONE);
                return;
            }
            return;
        }
        try {
            mEpgAdapter = new EpgAdapter((HashMap) EPGInstance.liveEpgsMap.get(Integer.valueOf(index)), mEpgListView, true, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mEpgAdapter == null)
            return;

        mEpgListView.setAdapter(mEpgAdapter);
        if (mEpgListView.getVisibility() == View.GONE)
            mEpgListView.setVisibility(View.VISIBLE);
        mEpgListView.setGroupIndicator(null);
        mEpgListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //C3551n
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    mSelectedEPGView = view;
                    if (!f13823B) {
                        view.setBackgroundResource(R.drawable.epg_focus_bg);
                    } else {
                        f13823B = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mEpgListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { //C3540c
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == mEpgAdapter.epgSelectedGroupPosition) {
                    if (parent.isGroupExpanded(mEpgAdapter.epgSelectedGroupPosition)) {
                        parent.collapseGroup(mEpgAdapter.epgSelectedGroupPosition);
                        mEpgAdapter.isExpandGroup = false;
                    } else {
                        parent.expandGroup(mEpgAdapter.epgSelectedGroupPosition);
                        mEpgAdapter.isExpandGroup = true;
                    }
                } else if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    parent.expandGroup(groupPosition);
                }
                return true;
            }
        });
        mEpgListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { //C3541d
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mEpgListView != null && v != null) {
                    mEpgListView.requestFocusFromTouch();
                    mEpgListView.setSelectedChild(groupPosition, childPosition, true);
                    EpgBeans.EpgBean epg = (EpgBeans.EpgBean) v.getTag();
                    if (epg != null) {
                        String playbackUrl = epg.getPlaybackUrl();
                        if ((playbackUrl != null) && !playbackUrl.equals("")) {
                            Message msg = new Message();
                            msg.what = Constant.MSG_PLAYER_START_PLAYBACK;
                            Bundle bundle = new Bundle();
                            bundle.putString("url", playbackUrl);
                            bundle.putString("name", epg.getName());
                            bundle.putString("type", Config.BS_MODE.BSPALYBACK.name());
                            msg.setData(bundle);
                            MainActivity.mMsgHandler.sendMessage(msg);
                            if (mChannelListAdapter != null) {
                                ChannelAdapter.mChild = 0;
                                mChannelListAdapter.notifyDataSetChanged();
                            }
                            EpgAdapter.selectedChildId = epg.getId();
                            mEpgAdapter.notifyDataSetChanged();
                            //mChannelType = Config.CHANNEL_TYPE.EPG;
                            mSelectedEPGView = v;
                            return false;
                        }
                    }
                }
                return true;
            }
        });
    }

    public List<EpgBeans> loadEpgById(int id) {
        List<EpgBeans> epgs = new ArrayList<>();
        for (EpgBeans epg : EPGInstance.mEpgs) {
            if (epg.getId() == id)
                epgs.add(epg);
        }

        return epgs;
    }

    public static void sendLoadEpgEvent(int eventCode) {
        //String text = "sendLoadEpgEvent, " + eventCode;
        Message message = new Message();
        message.what = 3;
        message.arg1 = eventCode;
        mMsgHandler.sendMessage(message);
    }

    public void startLiveChannel(ChannelBean channel) {
//        if (!MainActivity.f16802k && channel.getLevel() >= 18) {
//            PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(getContext());
//            final ChannelBean finalChannel = channel;
//            helper.setClickListener(new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3550m (this, channel)
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    MainActivity.f16802k = true;
//                    if (mGroupListAdapter != null) {
//                        mGroupListAdapter.notifyDataSetChanged();
//                    }
//                    startLiveChannel(finalChannel);
//                }
//            });
//            helper.create().show();
//            return;
//        }
        Message msg = new Message();
        msg.what = Constant.MSG_PLAYER_START_PLAYBACK;
        Bundle params = new Bundle();
        params.putString("url", channel.getSources().get(0).getAddress());
        if (channel.getSid() > 0) {
            params.putString("name", channel.getSid() + "." + channel.getName().getInit());
        } else {
            params.putString("name", channel.getName().getInit());
        }
        params.putString("type", Config.BS_MODE.BSLIVE.name());
        msg.setData(params);
        MainActivity.mMsgHandler.sendMessage(msg);
    }
}