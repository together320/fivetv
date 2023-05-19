package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.adapters.EpgAdapter;
import com.brazvip.fivetv.adapters.MenuChannelListAdapter;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.adapters.MenuGroupListAdapter;
import com.brazvip.fivetv.utils.BsConf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MenuLayout extends RelativeLayout {
    public ListView mGroupListView;
    public ListView mChannelListView;
    public ExpandableListView mEpgListView;

    public MenuGroupListAdapter mGroupListAdapter;

    public MenuChannelListAdapter mChannelListAdapter;

    public EpgAdapter mEpgAdapter;

    public static Handler mMsgHandler = null;

    public HashMap<Integer, Group> mShowGroups;

    public View f13839w;
    public View f13840x = null;

    public View f13842z = null;

    public static final int f13815d = 1;

    /* renamed from: e */
    public static final int f13816e = 2;

    /* renamed from: f */
    public static final int f13817f = 3;

    /* renamed from: g */
    public static int f13818g = 0;

    /* renamed from: h */
    public static int f13819h = 0;

    /* renamed from: i */
    public static int f13820i = 0;

    /* renamed from: j */
    public static boolean f13821j = false;

    /* renamed from: A */
    public boolean f13822A = true;

    /* renamed from: B */
    public boolean f13823B = true;

    /* renamed from: C */
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
                    //loadEpgList4Channel(message.arg1);
                }
                super.handleMessage(message);
            }
        };

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
            mShowGroups = ChannelInstance.mGroups;

            mGroupListAdapter = new MenuGroupListAdapter(getContext(), mShowGroups, mGroupListView);
            mGroupListView.setAdapter(mGroupListAdapter);

            mGroupListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                /* renamed from: a 13797 */
                public boolean f13797a = true;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (view != null) {
                        //f13839w = view;
                        int arg = ((Integer) view.getTag()).intValue();
                        //f13822A = true;
                        MenuLayout.mMsgHandler.removeMessages(2);
                        MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, arg, 0));
                        if (!f13797a) {
                            view.setBackgroundResource(R.drawable.group_focus_bg);
                        } else {
                            f13797a = false;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //C3545h, e.b.a.d.h
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String log = "onGroupItemClick: " + position;
                    mGroupListView.requestFocusFromTouch();
                    mGroupListView.setSelection(position);
                    //f13838v = BsConf.CHANNEL_TYPE.GROUP;
                    //f13839w = view;
                    //String log = String.valueOf(view.isSelected()) + "| " + view.getTag();
                    Integer tag = (Integer) view.getTag();
                    if (tag != null) {
                        int index = tag.intValue();
                        if (mShowGroups != null) {
                            Group group = mShowGroups.get(tag);
                            if ((group != null) && group.restrictedAccess) {
//                                if (SopCast.f16802k) {
//                                    MenuLayout.f13819h = 0;
//                                    SopCast.f16802k = false;
//                                    mGroupListAdapter.notifyDataSetChanged();
//                                    MenuLayout.mMsgHandler.removeMessages(2);
//                                    MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, index, 0));
//                                } else {
//                                    PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(getContext());
//                                    helper.setClickListener(new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3544g(this, index, position, view)
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            SopCast.f16802k = true;
//                                            mGroupListAdapter.notifyDataSetChanged();
//                                            MenuLayout.mMsgHandler.removeMessages(2);
//                                            MenuLayout.mMsgHandler.sendMessage(Message.obtain(MenuLayout.mMsgHandler, 2, index, 0));
//                                            mGroupListView.requestFocusFromTouch();
//                                            mGroupListView.setSelection(position);
//                                            view.setBackgroundResource(R.drawable.group_focus_bg);
//                                        }
//                                    });
//                                    helper.create().show();
//                                }
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
            Set<Integer> ks = mShowGroups.keySet();
            Integer[] indices = (Integer[]) ks.toArray(new Integer[ks.size()]);
            Arrays.sort(indices);
            int arg = indices[0].intValue();
            mMsgHandler.removeMessages(2);
            mMsgHandler.sendMessage(Message.obtain(mMsgHandler, 2, arg, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChannelData(int key) {
        //String log = "loadChannelData, " + key;
        if (mChannelListView == null) {
            return;
        }

        Group group = mShowGroups.get(key);
        List<ChannelBean> channels = loadChannelByGroup(key);
        if (channels != null && channels.size() > 0) {
//            if (!SopCast.f16802k && group.restrictedAccess) {
//                //String log = "restrictedAccess: " + mShowGroups.get(key).name;
//                if (!SopCast.f16805n || mChannelListView.hasFocus() || mEpgListView.hasFocus()) {
//                    mGroupListView.requestFocus();
//                    f13838v = BsConf.CHANNEL_TYPE.GROUP;
//                }
//                mChannelListView.setVisibility(View.GONE);
//                mEpgListView.setVisibility(View.GONE);
//                if (f13819h < RestApiUtils.f13723G) {
//                    f13819h++;
//                    SopCast.showMessageFromResource(R.string.Click_Restricted_Group);
//                    return;
//                }
//                return;
//            }
            try {
                mChannelListAdapter = new MenuChannelListAdapter(key, channels, getContext(), mChannelListView);
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
                        //f13840x = view;
                        MenuLayout.mMsgHandler.removeMessages(3);
                        //f13823B = true;
                        ChannelBean channel = (ChannelBean) view.getTag();
                        if (channel != null) {
                            //int epg = channel.getEpgSameAs() > 0 ? channel.getEpgSameAs() : channel.getChid();
                            int epg = channel.getEpgSameAs();
                            if (epg < 1) epg = channel.getChid();
                            Message msg = new Message();
                            msg.what = 3;
                            msg.arg1 = epg;
                            MenuLayout.mMsgHandler.sendMessage(msg);
                            //if (f13822A) {
                                //f13822A = false;
                                view.clearFocus();
                            //} else
                                view.setBackgroundResource(R.drawable.channel_focus_bg);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mChannelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //C3547j(this, key) e.b.a.d.j
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mChannelListView.requestFocusFromTouch();
                    mChannelListView.setSelection(position);
                    //String log = "################## onItemLongClick" + key;
                    ChannelBean channel = (ChannelBean) view.getTag();
                    if (channel != null) {
                        List<ChannelBean.SourcesBean> sources = channel.getSources();
                        if (sources == null || sources.size() == 0)
                            return false;
                        String source = sources.get(0).getAddress();
                        if ((source == null) || source.isEmpty())
                            return false;
                        mChannelListAdapter.notifyDataSetChanged();
                        if (mEpgAdapter != null) {
                            EpgAdapter.f13530b = "";
                            mEpgAdapter.notifyDataSetChanged();
                        }
//                        int chid = channel.getChid();
//                        if (BSChannel.f13643f.contains("" + chid)) {
//                            Toast.makeText(getContext(), channel.getName().getInit() + " " +
//                                    getContext().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
//                            BSChannel.f13643f.remove("" + chid);
//                            PrefUtils.setPrefStringSet(BsConf.SP_FAV_LIVE_CHANNEL, BSChannel.f13643f);
//                            BSChannel.parseChannels();
//                            if (key == -5) {
//                                if (SopCast.mGroupType == 104) {
//                                    List<ChannelBean> list = BSChannel.f13641d.get(-5).channnels;
//                                    if ((list != null) && (list.size() > 0)) {
//                                        mChannelListAdapter.f13523c = list;
//                                        mChannelListAdapter.notifyDataSetChanged();
//                                    } else
//                                        SopCast.mMsgHandler.sendEmptyMessage(112);
//                                    return true;
//                                }
//                                List<ChannelBean> list2 = BSChannel.f13640c.get(-5).channnels;
//                                if (list2 != null && list2.size() > 0) {
//                                    mChannelListAdapter.f13523c = list2;
//                                    mChannelListAdapter.notifyDataSetChanged();
//                                    return true;
//                                }
//                                SopCast.mMsgHandler.sendEmptyMessage(111);
//                            } else
//                                mChannelListAdapter.notifyDataSetChanged();
//                            return true;
//                        }
//                        Toast.makeText(getContext(), channel.getName().getInit() + " " +
//                                getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
//                        BSChannel.f13643f.add("" + chid);
//                        PrefUtils.setPrefStringSet(BsConf.SP_FAV_LIVE_CHANNEL, BSChannel.f13643f);
//                        BSChannel.parseChannels();
//                        //mShowGroups = new HashMap<>();
//                        if (SopCast.mGroupType == 100) {
//                            mShowGroups = BSChannel.f13640c;
//                        } else {
//                            mShowGroups = BSChannel.f13641d;
//                        }
                        if ((mShowGroups != null) && (mShowGroups.size() > 0)) {
                            mGroupListAdapter.mGroupData = mShowGroups;
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
                    //String log = "################## onItemClick " + position + " from touch: " + MenuLayout.f13821j;
                    ChannelBean channel = (ChannelBean) view.getTag();
                    if (MenuLayout.f13821j && MenuLayout.f13820i != channel.getChid()) {
                        MenuLayout.f13820i = channel.getChid();
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
                        MenuChannelListAdapter.f13521a = channel.getChid();
                        if (mEpgAdapter != null) {
                            EpgAdapter.f13530b = "";
                            mEpgAdapter.notifyDataSetChanged();
                        }
                        /*address = channel.getSources().get(0).getAddress();
                        if (address == null || address.equals("")) {
                            return;
                        }*/
                        //startLiveChannel(channel);
                        //f13838v = BsConf.CHANNEL_TYPE.CHANNEL;
                        f13840x = view;
                    }
                    //cond_6, goto_2
                    MenuLayout.f13821j = false;
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
        //String log2 = "channelListView.hasFocus(), " + mChannelListView.hasFocus();
        //String str2 = "epgListView.hasFocus(), " + mEpgListView.hasFocus();
//        if (!SopCast.f16805n || mChannelListView.hasFocus() || mEpgListView.hasFocus()) {
//            mGroupListView.requestFocus();
//            f13838v = BsConf.CHANNEL_TYPE.GROUP;
//        }
        mChannelListView.setVisibility(View.GONE);
        mEpgListView.setVisibility(View.GONE);
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

    public void loadEpgList4Channel(int position) {
        if (mEpgListView != null) {// && BSEPG.mEpgCacheMap != null) {
            HashMap<Long, List<EpgBeans.EpgBean>> map = null; //BSEPG.mEpgCacheMap.get(Integer.valueOf(position));
            if (map != null && map.size() > 0) {
                //try {
                mEpgAdapter = new EpgAdapter(map, mEpgListView, true, 100);
                //} catch (ParseException e) {
                //  e.printStackTrace();
                //}
                if (mEpgAdapter == null) {
                    return;
                }
                mEpgListView.setAdapter(mEpgAdapter);
                if (mEpgListView.getVisibility() == View.GONE) {
                    mEpgListView.setVisibility(View.VISIBLE);
                }
                mEpgListView.setGroupIndicator(null);
                mEpgListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //C3551n
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (view != null) {
                            f13842z = view;
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
                        if (groupPosition == mEpgAdapter.f13535g) {
                            if (parent.isGroupExpanded(mEpgAdapter.f13535g)) {
                                parent.collapseGroup(mEpgAdapter.f13535g);
                                mEpgAdapter.f13536h = false;
                            } else {
                                parent.expandGroup(mEpgAdapter.f13535g);
                                mEpgAdapter.f13536h = true;
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
                                    msg.what = 80;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", playbackUrl);
                                    bundle.putString("name", epg.getName());
                                    bundle.putString("type", BsConf.BS_MODE.BSPALYBACK.name());
                                    msg.setData(bundle);
                                    //SopCast.mMsgHandler.sendMessage(msg);
                                    if (mChannelListAdapter != null) {
                                        MenuChannelListAdapter.f13521a = 0;
                                        mChannelListAdapter.notifyDataSetChanged();
                                    }
                                    EpgAdapter.f13530b = epg.getId();
                                    mEpgAdapter.notifyDataSetChanged();
                                    //f13838v = BsConf.CHANNEL_TYPE.EPG;
                                    f13842z = v;
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                });
                return;
            }
        }

        mEpgListView.setVisibility(View.GONE);
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
}