package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
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
import com.brazvip.fivetv.adapters.MenuAdapter;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.dialogs.PasswordDialog;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.utils.Utils;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.Progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MenuLayout extends RelativeLayout implements View.OnKeyListener {
    public ListView groupListView;
    public ListView channelListView;
    public ExpandableListView epgListView;

    public GroupAdapter groupListAdapter;

    public ChannelAdapter channelAdapter;

    public EpgAdapter epgAdapter;

    public static Handler handler = null;

    public MenuAdapter menuAdapter;
    public Config.MenuType menuType;

    public HashMap<Integer, Group> groupChannelMap;

    public View selectedGroupView;
    public View selectedChannelView = null;
    public View selectedEpgView = null;

    public boolean f13823B = true;

    public boolean f9007pa = true;

    public boolean f9009ra = true;
    public boolean f9010sa = true;
    public boolean inited = false;

    public static int currentCHID = 0;
    public static int f8992aa = 0;
    public static int f8993ba = 0;
    public static boolean fromTouch = false;

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

        handler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int code = message.what;
                if (code == 1) {
                    loadGroupData();
                } else if (code == 2) {
                    loadChannelData(message.arg1);
                } else if (code == 3) {
                    loadEPGData(message.arg1);
                }
                super.handleMessage(message);
            }
        };

        initComponents();
    }

    private void initComponents() {
        groupListView = (ListView) findViewById(R.id.group_listview);
        channelListView = (ListView) findViewById(R.id.channel_listview);
        epgListView = (ExpandableListView) findViewById(R.id.epg_listview);
    }

    public void loadMenuLayout() {
        Config.isPlayStarted = false;
        groupListView.requestFocusFromTouch();

        if (inited)
            return;
        inited = true;

        groupListView.setVisibility(VISIBLE);
        channelListView.setVisibility(GONE);
        epgListView.setVisibility(GONE);

        loadGroupData();
    }

    public void loadGroupData() {
        if (this.inited) {
            HashMap<Integer, Group> hashMap = ChannelInstance.groupChannelMap;
            this.groupChannelMap = hashMap;
            if (hashMap == null || hashMap.size() == 0) {
                return;
            }
            try {
                this.groupChannelMap.size();
                this.menuAdapter = new MenuAdapter(getContext(), this.groupChannelMap, this.groupListView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.groupListView.setAdapter((ListAdapter) this.menuAdapter);
            if (this.groupListView.getVisibility() == View.GONE) {
                this.groupListView.setVisibility(View.VISIBLE);
            }
            this.groupListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public boolean f8976a = true;

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    if (view != null) {
                        selectedGroupView = view;
                        int intValue = (Integer) view.getTag();
                        if (ChannelInstance.groupChannelMap != null) {
                            MainActivity.groupType = ChannelInstance.groupChannelMap.get(intValue).type;
                        }
                        f9009ra = true;
                        handler.removeMessages(2);
                        handler.sendMessage(Message.obtain(handler, 2, intValue, 0));
                        if (this.f8976a) {
                            this.f8976a = false;
                        } else {
                            view.setBackgroundResource(R.drawable.group_focus_bg);
                        }
                    }
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: org.sopcast.android.fragment.3
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long j) {
                    groupListView.requestFocusFromTouch();
                    groupListView.setSelection(i);
                    menuType = Config.MenuType.f8632a;
                    selectedGroupView = view;
                    if (view.getTag() != null) {
                        final int key = (Integer) view.getTag();
                        Group group;
                        if (groupChannelMap != null && (group = groupChannelMap.get(key)) != null && group.restrictedAccess) {
                            if (MainActivity.restrictedGroupsUnlocked) {
                                f8993ba = 0;
                                MainActivity.restrictedGroupsUnlocked = false;
                                menuAdapter.notifyDataSetChanged();
                                handler.removeMessages(2);
                                handler.sendMessage(Message.obtain(handler, 2, key, 0));
                            } else {
                                PasswordDialog.Builder builder = new PasswordDialog.Builder(getContext());
                                builder.positiveClickListener = new DialogInterface.OnClickListener() {
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface, int i2) {
                                        MainActivity.restrictedGroupsUnlocked = true;
                                        menuAdapter.notifyDataSetChanged();
                                        handler.removeMessages(2);
                                        handler.sendMessage(Message.obtain(handler, 2, key, 0));
                                        groupListView.requestFocusFromTouch();
                                        groupListView.setSelection(i);
                                        view.setBackgroundResource(R.drawable.group_focus_bg);
                                    }
                                };
                                builder.build().show();
                            }
                        }
                        handler.removeMessages(2);
                        handler.sendMessage(Message.obtain(handler, 2, key, 0));
                        menuAdapter.notifyDataSetChanged();
                    }
                    view.setBackgroundResource(R.drawable.group_focus_bg);
                }
            });
            this.groupListView.setVerticalScrollBarEnabled(false);
            Integer[] numArr = (Integer[]) this.groupChannelMap.keySet().toArray(new Integer[0]);
            if (numArr == null || numArr.length <= 0) {
                return;
            }
            Arrays.sort(numArr, new Comparator<Integer>() {
                @Override // java.util.Comparator
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
            int intValue = num.intValue();
            handler.removeMessages(2);
            Handler handler2 = handler;
            handler2.sendMessage(Message.obtain(handler2, 2, intValue, 0));
        }
    }

    public void loadChannelData(int index) {
        if (this.channelListView == null) {
            return;
        }
        List<ChannelBean> channelList = new ArrayList<>();
        Group group = this.groupChannelMap.get(index);
        if (this.groupChannelMap != null && group != null) {
            channelList = group.channnels;
        }
        if (channelList == null || channelList.size() == 0) {
            this.channelListView.hasFocus();
            this.epgListView.hasFocus();
            if (this.channelListView.hasFocus() || this.epgListView.hasFocus()) {
                this.menuType = Config.MenuType.f8632a;
            }
            this.channelListView.setVisibility(View.GONE);
            this.epgListView.setVisibility(View.GONE);
        } else if (!MainActivity.restrictedGroupsUnlocked && group.restrictedAccess) {
            if (this.channelListView.hasFocus() || this.epgListView.hasFocus()) {
                this.menuType = Config.MenuType.f8632a;
            }
            this.channelListView.setVisibility(View.GONE);
            this.epgListView.setVisibility(View.GONE);
            if (f8993ba < Config.restrictedGroupMinPress) {
                f8993ba++;
                MainActivity.prepareToast(R.string.Click_Restricted_Group);
            }
        } else {
            try {
                this.channelAdapter = new ChannelAdapter(index, channelList, getContext(), this.channelListView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (channelAdapter == null) {
                return;
            }
            this.channelListView.setAdapter(channelAdapter);
            if (this.channelListView.getVisibility() == View.GONE) {
                this.channelListView.setVisibility(View.VISIBLE);
            }
            this.channelListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                    if (view != null) {
                        selectedChannelView = view;
                        handler.removeMessages(3);
                        f9010sa = true;
                        ChannelBean channelBean = (ChannelBean) view.getTag();
                        if (channelBean != null) {
                            int chid = channelBean.getChid();
                            if (channelBean.getEpgSameAs() > 0 && ChannelInstance.liveChannels.get(channelBean.getEpgSameAs()) != null) {
                                chid = channelBean.getEpgSameAs();
                            }
                            Message message = new Message();
                            message.what = 3;
                            message.arg1 = chid;
                            handler.sendMessage(message);
                            if (!f9009ra) {
                                view.setBackgroundResource(R.drawable.channel_focus_bg);
                                return;
                            }
                            f9009ra = false;
                            view.clearFocus();
                        }
                    }
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.channelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i2, long j) {
                    channelListView.requestFocusFromTouch();
                    channelListView.setSelection(index);
                    ChannelBean channelBean = (ChannelBean) view.getTag();
                    if (channelBean == null) {
                        return true;
                    }
                    if (channelBean.getSources() == null || channelBean.getSources().size() == 0 || channelBean.getSources().get(0).getAddress() == null) {
                        return false;
                    }
                    channelAdapter.notifyDataSetChanged();
                    if (epgAdapter != null) {
                        EpgAdapter.f8722a = "";
                        epgAdapter.notifyDataSetChanged();
                    }
                    int chid = channelBean.getChid();
                    Set<String> set = ChannelInstance.favoriteLiveChannels;
                    if (!set.contains("" + chid)) {
                        Context context = getContext();
                        Toast.makeText(context, channelBean.getName().getInit() + " " + getContext().getString(R.string.favorited), Toast.LENGTH_SHORT).show();
                        Set<String> set2 = ChannelInstance.favoriteLiveChannels;
                        set2.add("" + chid);
                        Utils.saveSPStringSet(Config.SP_FAV_LIVE_CHANNEL, ChannelInstance.favoriteLiveChannels);
                        ChannelInstance.channelGrouping();
                        groupChannelMap = ChannelInstance.groupChannelMap;
                        HashMap<Integer, Group> hashMap = groupChannelMap;
                        if (hashMap != null && hashMap.size() > 0) {
                            menuAdapter.groupedMenus = groupChannelMap;
                            menuAdapter.notifyDataSetChanged();
                            return true;
                        }
                        return true;
                    }
                    Context context2 = getContext();
                    Toast.makeText(context2, channelBean.getName().getInit() + " " + getContext().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
                    Set<String> set3 = ChannelInstance.favoriteLiveChannels;
                    set3.remove("" + chid);
                    Utils.saveSPStringSet(Config.SP_FAV_LIVE_CHANNEL, ChannelInstance.favoriteLiveChannels);
                    ChannelInstance.channelGrouping();
                    if (i2 != -5) {
                        channelAdapter.notifyDataSetChanged();
                        return true;
                    }

                    List<ChannelBean> list = ChannelInstance.groupChannelMap.get(-5).channnels;
                    if (list == null || list.size() == 0) {
                        MainActivity.handler.sendEmptyMessage(Constant.EVENT_FOCUS_LIVE_BUTTON);
                        return true;
                    }
                    channelAdapter.channels = list;
                    channelAdapter.notifyDataSetChanged();
                    return true;
                }
            });
            this.channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: org.sopcast.android.fragment.7
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                    channelListView.requestFocusFromTouch();
                    channelListView.setSelection(i2);
                    ChannelBean channelBean = (ChannelBean) view.getTag();
                    if (fromTouch && channelBean != null && currentCHID != channelBean.getChid()) {
                        currentCHID = channelBean.getChid();
                        int epgSameAs = channelBean.getEpgSameAs() > 0 ? channelBean.getEpgSameAs() : channelBean.getChid();
                        Message message = new Message();
                        message.what = 3;
                        message.arg1 = epgSameAs;
                        handler.sendMessage(message);
                    } else if (channelBean != null) {
                        if (channelBean.getSources() == null || channelBean.getSources().size() == 0 || channelBean.getSources().get(0).getAddress() == null) {
                            return;
                        }
                        ChannelAdapter.f8718a = channelBean.getChid();
                        if (epgAdapter != null) {
                            EpgAdapter.f8722a = "";
                            epgAdapter.notifyDataSetChanged();
                        }
                        String address = channelBean.getSources().get(0).getAddress();
                        if (address == null || address.equals("")) {
                            return;
                        }
                        playLive(channelBean);
                        menuType = Config.MenuType.LIVE;
                        selectedChannelView = view;
                    }
                    fromTouch = false;
                    channelAdapter.notifyDataSetChanged();
                    view.setBackgroundResource(R.drawable.channel_focus_bg);
                }
            });
            int chid = (channelList.get(0)).getChid();
            if ((channelList.get(0)).getEpgSameAs() > 0 && ChannelInstance.liveChannels.get((channelList.get(0)).getEpgSameAs()) != null) {
                chid = (channelList.get(0)).getEpgSameAs();
            }
            Message message = new Message();
            message.what = 3;
            message.arg1 = chid;
            handler.sendMessage(message);
        }
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
            channels = groupChannelMap.get(groupId).channnels;
        }

        return channels;
    }

    public void loadEPGData(int index) {
        if (epgListView == null || EPGInstance.liveEpgsMap == null ||
                EPGInstance.liveEpgsMap.get(index) == null ||
                EPGInstance.liveEpgsMap.get(index).size() == 0) {
            ExpandableListView expandableListView = epgListView;
            if (expandableListView != null) {
                expandableListView.setVisibility(View.GONE);
                return;
            }
            return;
        }
        try {
            epgAdapter = new EpgAdapter(EPGInstance.liveEpgsMap.get(index), epgListView, f9007pa, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (epgAdapter != null) {
            epgListView.setAdapter(epgAdapter);
            if (epgListView.getVisibility() == View.GONE) {
                epgListView.setVisibility(View.VISIBLE);
            }
            epgListView.setGroupIndicator(null);
            epgListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: org.sopcast.android.fragment.LiveFragment.1.1
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j) {
                    if (view != null) {
                        selectedEpgView = view;
                        if (f9010sa) {
                            f9010sa = false;
                        } else {
                            view.setBackgroundResource(R.drawable.epg_focus_bg);
                        }
                    }
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            epgListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: org.sopcast.android.fragment.LiveFragment.1.2
                @Override // android.widget.ExpandableListView.OnGroupClickListener
                public boolean onGroupClick(ExpandableListView expandableListView2, View view, int i3, long j) {
                    int i4 = index;
                    int i5 = epgAdapter.f8727f;
                    if (i4 == i5) {
                        if (expandableListView2.isGroupExpanded(i5)) {
                            expandableListView2.collapseGroup(epgAdapter.f8727f);
                            epgAdapter.f8728g = false;
                        } else {
                            expandableListView2.expandGroup(epgAdapter.f8727f);
                            epgAdapter.f8728g = true;
                        }
                    } else if (expandableListView2.isGroupExpanded(i3)) {
                        expandableListView2.collapseGroup(i3);
                    } else {
                        expandableListView2.expandGroup(i3);
                    }
                    return true;
                }
            });
            epgListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView listView, View view, int group, int child, long j) {
                    String playbackUrl;
                    if (epgListView != null && view != null) {
                        epgListView.requestFocusFromTouch();
                        epgListView.setSelectedChild(group, child, true);
                        EpgBeans.EpgBean epgBean = (EpgBeans.EpgBean) view.getTag();
                        if (epgBean != null && (playbackUrl = epgBean.getPlaybackUrl()) != null && !playbackUrl.equals("")) {
                            Message msg = new Message();
                            msg.what = Constant.MSG_PLAYER_PLAY_VIDEO;
                            Bundle bundle = new Bundle();
                            bundle.putString("chid", (String) view.getTag(R.id.chid));
                            bundle.putString("subId", epgBean.getId());
                            bundle.putString(Progress.URL, playbackUrl);
                            bundle.putString(SerializableCookie.NAME, ChannelInstance.liveChannels.get(index).getName().getInit());
                            bundle.putString("subTitle", epgBean.getName());
                            bundle.putString("type", Config.VIDEO_TYPE.PLAYBACK.name());
                            bundle.putBoolean("restricted", false);
                            bundle.putString("menuType", Config.MenuType.LIVE.name());
                            msg.setData(bundle);
                            MainActivity.handler.sendMessage(msg);
                            if (channelAdapter != null) {
                                ChannelAdapter.f8718a = 0;
                                channelAdapter.notifyDataSetChanged();
                            }
                            EpgAdapter.f8722a = epgBean.getId();
                            epgAdapter.notifyDataSetChanged();
                            menuType = Config.MenuType.f8634c;
                            selectedEpgView = view;
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
    }

    public void playLive(final ChannelBean channelBean) {
        boolean z;
        String init;
        channelBean.getLevel();
        if (!MainActivity.restrictedGroupsUnlocked && channelBean.getLevel() >= 18) {
            PasswordDialog.Builder builder = new PasswordDialog.Builder(getContext());
            builder.positiveClickListener = new DialogInterface.OnClickListener() {
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.restrictedGroupsUnlocked = true;
                    if (menuAdapter != null) {
                        menuAdapter.notifyDataSetChanged();
                    }
                    playLive(channelBean);
                }
            };
            builder.build().show();
            return;
        }
        Message message = new Message();
        channelBean.toString();
        Iterator<ChannelBean.TagsBean> it = channelBean.getTags().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().isRestrictedAccess()) {
                z = true;
                break;
            }
        }
        message.what = 80;
        Bundle bundle = new Bundle();
        bundle.putString("chid", String.valueOf(channelBean.getChid()));
        bundle.putString(Progress.URL, channelBean.getSources().get(0).getAddress());
        if (channelBean.getSid() > 0) {
            init = channelBean.getSid() + "." + channelBean.getName().getInit();
        } else {
            init = channelBean.getName().getInit();
        }
        bundle.putString(SerializableCookie.NAME, init);
        bundle.putString("subId", "");
        bundle.putString("subTitle", "");
        bundle.putBoolean("restricted", z);
        bundle.putString("type", Config.VIDEO_TYPE.BSLIVE.name());
        bundle.putString("menuType", Config.MenuType.LIVE.name());
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        int id = view.getId();
        if (keyEvent.getRepeatCount() == KeyEvent.ACTION_DOWN && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (id == R.id.group_listview) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (channelListView == null || channelListView.getVisibility() != View.VISIBLE) {
                        if ((channelListView != null || channelListView.getVisibility() == View.GONE) &&
                                groupListView.getSelectedView() != null &&
                                groupListView.getSelectedView().getTag() != null &&
                                (Integer) groupListView.getSelectedView().getTag() == -5 &&
                                f8992aa < Config.backToExitMinPress) {
                            f8992aa++;
                            MainActivity.prepareToast(R.string.Fav_channel);
                        }
                    } else {
                        channelListView.requestFocus();
                        menuType = Config.MenuType.LIVE;
                        if (selectedChannelView != null) {
                            selectedChannelView.setBackgroundResource(R.drawable.channel_focus_bg);
                        }
                        ListView listView3 = this.groupListView;
                        if (listView3 != null) {
                            listView3.clearFocus();
                        }
                        if (groupListView.getSelectedView() != null) {
                            groupListView.getSelectedView().setBackgroundResource(R.drawable.channel_button_selected_bg);
                        }
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (groupListView != null) {
                        groupListView.clearFocus();
                        if (groupListView.getSelectedView() != null) {
                            groupListView.getSelectedView().setBackgroundResource(0);
                        }
                    }
//                    SopCast sopCast = (SopCast) getActivity();
//                    sopCast.rbLive.requestFocus();
//                    sopCast.rbLive.setChecked(true);
                    MainActivity.SendMessage(Constant.EVENT_FOCUS_LIVE_BUTTON);
                    menuType = null;
                    return true;
                }
            } else if (id == R.id.channel_listview) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (epgListView != null && epgListView.getVisibility() != View.GONE) {
                        if (channelListView != null) {
                            channelListView.clearFocus();
                        }
                        if (channelListView.getSelectedView() != null) {
                            channelListView.getSelectedView().setBackgroundResource(R.drawable.channel_item_selected_bg);
                        }
                        epgListView.requestFocus();
                        menuType = Config.MenuType.f8634c;
                        if (selectedEpgView != null) {
                            selectedEpgView.setBackgroundResource(R.drawable.epg_focus_bg);
                        }
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (channelListView != null) {
                        channelListView.clearFocus();
                    }
                    if (selectedChannelView != null) {
                        selectedChannelView.setBackgroundResource(0);
                    }
                    if (groupListView != null) {
                        groupListView.requestFocus();
                        if (groupListView.getSelectedView() != null) {
                            groupListView.getSelectedView().setBackgroundResource(R.drawable.group_focus_bg);
                        }
                        menuType = Config.MenuType.f8632a;
                    }
                    return true;
                }
            } else if (id == R.id.epg_listview) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    ExpandableListView expandableListView2 = this.epgListView;
                    if (epgListView != null) {
                        epgListView.clearFocus();
                    }
                    if (selectedEpgView != null) {
                        selectedEpgView.setBackgroundResource(0);
                    }
                    if (channelListView != null) {
                        channelListView.requestFocus();
                        if (channelListView.getSelectedView() != null) {
                            channelListView.getSelectedView().setBackgroundResource(R.drawable.channel_focus_bg);
                        }
                        menuType = Config.MenuType.LIVE;
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, keyEvent);
    }
}