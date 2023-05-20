package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChannelInstance {

    private static String mCacheKey = "ChannelInstance";

    public static volatile HashMap<Integer, Group> mGroups;
    public static volatile List<ChannelBean> mChannels;

    public static volatile Set<String> mFavoriteChannels;

    public static volatile HashMap<Integer, ChannelBean> f13642e;

    public static void Refresh() {
        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.CHANNEL);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                asyncParseChannels(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    asyncParseChannels(response.body());
                                } else {
                                    onFail();
                                }
                            }
                        }
                );
            }
        }
        ).start();
    }

    public static void parseJson(String text) {
        mGroups = new HashMap<>();
        mChannels = JSON.parseArray(text, ChannelBean.class);

        Group a = new Group();
        a.name = SopApplication.getAppContext().getString(R.string.Favorites);
        a.id = Constant.GROUP_FAVORITE;
        a.type = 0;
        mGroups.put(a.id, a);

        a = new Group();
        a.name = SopApplication.getAppContext().getString(R.string.All_A_Z);
        a.id = Constant.GROUP_ALL;
        a.type = 0;
        mGroups.put(a.id, a);

        for (ChannelBean channel : mChannels) {
            List<ChannelBean.TagsBean> tags = channel.getTags();

            for (ChannelBean.TagsBean tag : tags) {
                if (mGroups.get(tag.getId()) == null) {
                    a = new Group();
                    a.name = tag.getName().getInit();
                    a.id = tag.getId();
                    a.type = tag.getType();
                    a.restrictedAccess = tag.isRestrictedAccess();
                    mGroups.put(tag.getId(), a);
                }
            }
        }

        Message msg = new Message();
        msg.what = Constant.MSG_CHANNEL_LOADED;
        MainActivity.SendMessage(msg);
    }

    private static void asyncParseChannels(String result) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //ChannelInstance.parseJson(result);

                    ChannelInstance.mChannels = JSON.parseArray(result, ChannelBean.class);
                    ChannelInstance.parseChannels();

                    Message msg = new Message();
                    msg.what = Constant.MSG_CHANNEL_LOADED;
                    MainActivity.SendMessage(msg);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void parseChannels() {
        ChannelInstance.mFavoriteChannels = new HashSet<>(PrefUtils.getPrefStringSet("SP_FAV_LIVE_CHANNEL", new HashSet<>()));
        //String log = "############ channelGrouping " + ChannelInstance.mFavoriteChannels.toString();
        ChannelInstance.mGroups = new HashMap<>();
        ChannelInstance.f13642e = new HashMap<>();
        //r2 = Integer.valueOf(-4), r5 = Integer.valueOf(-5), r3 = 104

        if (ChannelInstance.mChannels.size() > 0) {
            Group a = new Group();
            a.name = SopApplication.getAppContext().getString(R.string.Favorites_live);
            a.type = -5;
            a.channnels = new ArrayList<>();
            ChannelInstance.mGroups.put(Integer.valueOf(Constant.GROUP_FAVORITE), a);

            Group b = new Group();
            b.name = SopApplication.getAppContext().getString(R.string.All_A_Z);
            b.type = -4;
            b.channnels = new ArrayList<>();
            ChannelInstance.mGroups.put(Integer.valueOf(Constant.GROUP_ALL), b);
        }

        //Laa
        Integer idx;
        for (ChannelBean r1 : ChannelInstance.mChannels) {
            List<ChannelBean.TagsBean> tags = r1.getTags(); //r4
            if (r1.getSid() > 0)
                ChannelInstance.f13642e.put(Integer.valueOf(r1.getSid()), r1);
            for (ChannelBean.TagsBean r6 : tags) { //tag = r6, Ld7:
                idx = Integer.valueOf(r6.getId()); //r9
                if ((ChannelInstance.mGroups.get(idx) == null) && (ChannelInstance.mGroups.get(idx) == null)) { //L106
                    Group r8 = new Group();
                    r8.name = r6.getName().getInit();
                    r8.restrictedAccess = r6.isRestrictedAccess();
                    if (!r8.restrictedAccess && (r1.getLevel() == 18))
                        continue; //goto Ld7
                    //L126
                    r8.type = r6.getType();
                    r8.channnels = new ArrayList<>();
                    r8.channnels.add(r1);
                    if (r8.type == 104) {
                        ChannelInstance.mGroups.put(idx, r8);
                    } else { //L14b
                        ChannelInstance.mGroups.put(idx, r8);
                    } //goto L1c2
                } else { //L159
                    if (r6.getType() == 104) {
                        Group r8 = (Group) ChannelInstance.mGroups.get(idx);
                        if (r8 == null) //&&&&&&&&&&& ADDED BY ME
                            continue;
                        if (!r8.restrictedAccess && (r1.getLevel() == 18))
                            continue; //goto Ld7
                        //L17b
                        Group r7 = (Group) ChannelInstance.mGroups.get(idx);
                        r7.channnels.add(r1); //goto L1c2
                    } else { //L191
                        Group r8 = (Group) ChannelInstance.mGroups.get(idx);
                        if (r8 == null) //&&&&&&&&&&& ADDED BY ME
                            continue;
                        if ((!r8.restrictedAccess) && (r1.getLevel() == 18))
                            continue; //goto Ld7
                        //L1ad
                        Group r7 = (Group) ChannelInstance.mGroups.get(idx);
                        r7.channnels.add(r1); //goto L1c2
                    }
                }
                //L1c2, Add to favorites
                idx = Integer.valueOf(Constant.GROUP_ALL); //r5
                if (ChannelInstance.mGroups.get(idx) == null)
                    continue;
                if (!ChannelInstance.mFavoriteChannels.contains("" + r1.getChid()))
                    continue;
                Group grp;//r6
                if (r6.getType() == 104) {
                    grp = (Group) ChannelInstance.mGroups.get(idx);
                } else { //L208
                    grp = (Group) ChannelInstance.mGroups.get(idx);
                }
                if (grp.channnels.indexOf(r1) < 0) {
                    grp.channnels.add(r1);
                }
            }
            //L227
            idx = Integer.valueOf(Constant.GROUP_ALL); //r2
            if (r1.getLevel() < 18) {
                ChannelInstance.mGroups.get(idx).channnels.add(r1);
            }
        }
        //L23c
        idx = Integer.valueOf(Constant.GROUP_FAVORITE); //r5
        if (!RestApiUtils.f13721E) {
            Group grp = (Group) ChannelInstance.mGroups.get(idx);
            if ((grp != null) && (grp.channnels.size() < 1))
                ChannelInstance.mGroups.remove(idx);
            //L25d
            grp = (Group) ChannelInstance.mGroups.get(idx);
            if ((grp != null) && (grp.channnels.size() < 1))
                ChannelInstance.mGroups.remove(idx);
        }
        //String log1 = "############ groupChannelMap " + ChannelInstance.mGroups.size();
        //String log2 = "############ groupChannelMapPlayback " + ChannelInstance.mGroups.size();
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get channel list!");
    }
}
