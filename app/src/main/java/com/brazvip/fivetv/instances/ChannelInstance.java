package com.brazvip.fivetv.instances;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.BSCF;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ChannelInstance {

    private static String mCacheKey = "ChannelInstance";

    public static volatile List<ChannelBean> channelList;
    public static volatile HashMap<String, ChannelBean> channelListByID;
    public static volatile HashMap<Integer, ChannelBean> channelListBySID;
    public static volatile Set<String> favoriteLiveChannels;
    public static volatile HashMap<Integer, Group> groupChannelMap;
    public static volatile HashMap<Integer, ChannelBean> liveChannels;

    public static void getAllChannels() {

        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.CHANNEL);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("ChannelInstance", "URL: " + url);
                if (Constant.OFFLINE_TEST == true) {
                    String strInfo = PrefUtils.getPrefString(Constant.PREFS_CHANNEL_INFO, "");
                    if (strInfo.length() > 0) {
                        parseChannelData(strInfo, false);
                        return;
                    }
                }

                new GetRequest(url)
                        .removeHeader(HttpHeaders.HEAD_KEY_USER_AGENT)
                        .headers(HttpHeaders.HEAD_KEY_USER_AGENT, BSCF.userAgent)
                        .tag(this)
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(new StringCallback() {
                //((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(new StringCallback() {
                        @Override
                        public void onCacheSuccess(Response<String> response) {
                            Log.d("ChannelInstance", "onCacheSuccess");
                            parseChannelData(response.body(), true);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.e("ChannelInstance", "onError");
                            onFail();
                        }

                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.d("ChannelInstance", "onSuccess - response successful!");
                                parseChannelData(response.body(), true);
                            } else {
                                Log.e("ChannelInstance", "onSuccess - response failed!");
                                onFail();
                            }
                        }
                    }
                );
            }
        }
        ).start();
    }

    private static void parseChannelData(String str, boolean savePref) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (savePref == true) {
                        PrefUtils.setPrefString(Constant.PREFS_CHANNEL_INFO, str);
                    }

                    channelList = JSON.parseArray(str, ChannelBean.class);
                    channelGrouping();

                    Message msg = new Message();
                    msg.what = Constant.MSG_CHANNEL_LOADED;
                    MainActivity.SendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void channelGrouping() {
        favoriteLiveChannels = new HashSet<>(PrefUtils.getPrefStringSet(Config.SP_FAV_LIVE_CHANNEL, new HashSet<>()));
        liveChannels = new LinkedHashMap<>();
        groupChannelMap = new LinkedHashMap<>();
        channelListBySID = new LinkedHashMap<>();
        channelListByID = new LinkedHashMap<>();
        if (channelList == null || channelList.isEmpty()) {
            return;
        }
        Group group = new Group();
        group.name = SopApplication.getSopContext().getString(R.string.Favorites_live);
        group.type = Constant.GROUP_FAVORITE;
        group.channnels = new ArrayList<>();
        groupChannelMap.put(Constant.GROUP_FAVORITE, group);
        if (Config.isPlayback) {
            Group group2 = new Group();
            group2.name = SopApplication.getSopContext().getString(R.string.Playback);
            group2.type = Constant.GROUP_PLAYBACK;
            group2.channnels = new ArrayList<>();
            groupChannelMap.put(Constant.GROUP_PLAYBACK, group2);
        }
        Group group3 = new Group();
        group3.name = SopApplication.getSopContext().getString(R.string.All_A_Z);
        group3.type = Constant.GROUP_ALL;
        group3.channnels = new ArrayList<>();
        groupChannelMap.put(Constant.GROUP_ALL, group3);
        for (ChannelBean channelBean : channelList) {
            List<ChannelBean.TagsBean> tags = channelBean.getTags();
            liveChannels.put(channelBean.getChid(), channelBean);
            channelListByID.put(channelBean.getId(), channelBean);
            if (channelBean.getSid() > 0) {
                channelListBySID.put(channelBean.getSid(), channelBean);
            }
            for (ChannelBean.TagsBean tagsBean : tags) {
                if (groupChannelMap.get(tagsBean.getId()) == null) {
                    Group group4 = new Group();
                    group4.name = tagsBean.getName().getInit();
                    boolean isRestrictedAccess = tagsBean.isRestrictedAccess();
                    group4.restrictedAccess = isRestrictedAccess;
                    if (isRestrictedAccess || channelBean.getLevel() != 18) {
                        if (!Config.f8896P || tagsBean.getType() != 104) {
                            group4.type = tagsBean.getType();
                            List<ChannelBean> arrayList = new ArrayList<>();
                            group4.channnels = arrayList;
                            arrayList.add(channelBean);
                            groupChannelMap.put(tagsBean.getId(), group4);
                            Set<String> set = favoriteLiveChannels;
                            StringBuilder sb = new StringBuilder();
                            sb.append(channelBean.getChid());
                            if (set.contains(sb.toString())) {
                                groupChannelMap.get(Constant.GROUP_FAVORITE).channnels.add(channelBean);
                            }
                        }
                    }
                } else if (groupChannelMap.get(tagsBean.getId()).restrictedAccess || channelBean.getLevel() != 18) {
                    if (!Config.f8896P || tagsBean.getType() != 104) {
                        groupChannelMap.get(tagsBean.getId()).channnels.add(channelBean);
                        Set<String> set2 = favoriteLiveChannels;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(channelBean.getChid());
                        if (set2.contains(sb2.toString()) && !groupChannelMap.get(-5).channnels.contains(channelBean)) {
                            groupChannelMap.get(Constant.GROUP_FAVORITE).channnels.add(channelBean);
                        }
                    }
                }
            }
            if (channelBean.getLevel() < 18) {
                groupChannelMap.get(Constant.GROUP_ALL).channnels.add(channelBean);
                if (channelBean.isHasPlayBack() && Config.FLAG_PLAYBACK_ENABLE) {
                    groupChannelMap.get(Constant.GROUP_PLAYBACK).channnels.add(channelBean);
                }
            }
        }
        if (!Config.FLAG_FAVORITE_INIT &&
                groupChannelMap.get(Constant.GROUP_FAVORITE) != null &&
                groupChannelMap.get(Constant.GROUP_FAVORITE).channnels != null &&
                groupChannelMap.get(Constant.GROUP_FAVORITE).channnels.size() == 0) {
            groupChannelMap.remove(Constant.GROUP_FAVORITE);
        }
    }

    private static void onFail() {
        PrefUtils.ToastShort("Failed to get channel list!");
    }
}
