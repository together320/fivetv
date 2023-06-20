package com.brazvip.fivetv.instances;

import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.LibTvServiceClient;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.beans.GroupL1;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.layouts.VodLayout;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VodChannelInstance {

    public static final String TAG = "VodChannelInstance";

    private static String mCacheKey = "VodChannelInstance";

    public static int f13619e = 0;

    /* renamed from: f */
    public static HashMap<String, Integer> f13620f;

    @JSONField  //("this")
    /* renamed from: g 13621 */
    public static volatile List<ChannelBean> mChannelList;

    @JSONField  //("this")
    /* renamed from: h */
    public static volatile SparseArray<Group> mGroupList;

    public static volatile SparseArray<GroupL1> mGroupL1List;

    public static volatile Set<String> mFavoriteVodChannelList;

    public static volatile List<ChannelBean> mTopChannelList;

    public static synchronized void Refresh() {
        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.VOD_CHANNEL);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("VodInstance", "URL: " + url);
                if (Constant.OFFLINE_TEST == true) {
                    String strInfo = PrefUtils.getPrefString(Constant.PREFS_VOD_INFO, "");
                    if (strInfo.length() > 0) {
                        asyncParseVodChannels(strInfo, false);
                        return;
                    }
                }

                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                Log.d("VodInstance", "onCacheSuccess");
                                PrefUtils.ToastShort("Getting vod channel list! - onCacheSuccess");
                                asyncParseVodChannels(response.body(), true);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                Log.e("VodInstance", "onError");
                                onFail("onError");
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d("VodInstance", "onSuccess - response successful!");
                                    asyncParseVodChannels(response.body(), true);
                                } else {
                                    Log.e("VodInstance", "onSuccess - response failed!");
                                    onFail("onSuccess");
                                }
                            }
                        }
                );
            }
        }
        ).start();
    }

    public static void parseJson(String text) {
        mChannelList = JSON.parseArray(text, ChannelBean.class);
        if (mChannelList != null) {
            parseVodChannels();
        }
    }

    private static void asyncParseVodChannels(String result, boolean savePref) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (savePref == true) {
                        PrefUtils.setPrefString(Constant.PREFS_VOD_INFO, result);
                    }
Log.e(TAG, "Size: " + result.length());
                    VodChannelInstance.parseJson(result);

                    Message msg = new Message();
                    msg.what = Constant.MSG_VOD_LOADED;
                    MainActivity.SendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void parseVodChannels() {
        if (mChannelList == null) {
            return;
        }
        //long time = new Date().getTime();
        //String log1 = "start channelGrouping... " + time;
        mFavoriteVodChannelList = new HashSet<>(PrefUtils.getPrefStringSet(Config.SP_FAV_VOD_CHANNEL, new HashSet<>()));
        //String log2 = "############ vodchannelGrouping " + f13624j.toString();
        mGroupList = new SparseArray<>();
        f13620f = new HashMap<>();
        mGroupL1List = new SparseArray<>();
        GroupL1 groupL1 = new GroupL1();
        int pos = f13619e;
        f13619e = pos + 1;
        groupL1.id = pos;
        groupL1.name = SopApplication.getAppContext().getString(R.string.All);
        groupL1.groups = new HashSet<>();
        mGroupL1List.put(groupL1.id, groupL1);
        f13620f.put(groupL1.name, Integer.valueOf(groupL1.id));
        //String str2 = "########## VodFragment.IS_SEARCH_STATE: " + VodLayout.f13907k;
        if (VodLayout.isSearchState) {
            RestApiUtils.vodGridSpanCount = 4;
            Collections.sort(mChannelList, new Comparator<ChannelBean>() { //C3529z, e.b.a.b.z
                @Override
                public int compare(ChannelBean o1, ChannelBean o2) {
                    return o1.getDlEver() >= o2.getDlEver() ? -1 : 1;
                    /*long diff = o1.getDlEver() - o2.getDlEver();
                    return (diff == 0 ? 0 : (diff > 0 ? 1 : -1));*/
                }
            });
            Group group = new Group();
            group.name = SopApplication.getAppContext().getString(R.string.Results);
            group.type = -10;
            group.channnels = new ArrayList<>();
            mGroupList.put(-10, group);
        } else {
            Collections.sort(mChannelList, new Comparator<ChannelBean>() { //C3500A
                @Override
                public int compare(ChannelBean o1, ChannelBean o2) {
                    int diff = o1.getSid() - o2.getSid();
                    return diff < 0 ? -(o1.getChid() - o2.getChid()) : diff;
                }
            });
            RestApiUtils.vodGridSpanCount = 6;
        }
        if (mChannelList.size() > 0) {
            Group group = new Group();
            group.id = 0;
            group.name = SopApplication.getAppContext().getString(R.string.Favorites);
            group.type = -5;
            group.channnels = new ArrayList<>();
            mGroupList.put(-5, group);
            groupL1.groups.add(Integer.valueOf(group.id));
        }
        for (ChannelBean channel : mChannelList) {
            for (ChannelBean.TagsBean tags : channel.getTags()) {
                if (VodLayout.isSearchState) {
                    if (channel.getLevel() < 18 && mGroupList.get(-10).channnels.indexOf(channel) < 0) {
                        mGroupList.get(-10).channnels.add(channel);
                    }
                } else if (mGroupList.get(tags.getId()) != null) {
                    if (mGroupList.get(tags.getId()).restrictedAccess || channel.getLevel() != 18) {
                        mGroupList.get(tags.getId()).channnels.add(channel);
                    }
                } else {
                    Group group = new Group();
                    group.id = tags.getId();
                    group.name = tags.getName().getInit();
                    group.restrictedAccess = tags.isRestrictedAccess();
                    if (group.restrictedAccess || channel.getLevel() != 18) {
                        group.type = tags.getType();
                        group.channnels = new ArrayList<>();
                        group.channnels.add(channel);
                        mGroupList.put(tags.getId(), group);
                        String[] split = tags.getName().getInit().split("/");
                        if (split.length >= 2) {
                            if (!f13620f.containsKey(split[0])) {
                                //String log = "new 2lmenu" + split[0];
                                GroupL1 l1 = new GroupL1();
                                int id = f13619e;
                                f13619e = id + 1;
                                l1.id = id;
                                l1.name = split[0];
                                l1.groups = new HashSet<>();
                                l1.groups.add(Integer.valueOf(group.id));
                                mGroupL1List.put(l1.id, l1);
                                f13620f.put(split[0], Integer.valueOf(l1.id));
                            } else {
                                mGroupL1List.get(f13620f.get(split[0]).intValue())
                                        .groups.add(Integer.valueOf(group.id));
                            }
                        }
                        groupL1.groups.add(Integer.valueOf(group.id));
                    }
                }
                if (channel.getLevel() < 18 && mGroupList.get(-5) != null) {
                    if (mFavoriteVodChannelList.contains("" + channel.getChid()) &&
                            mGroupList.get(-5).channnels.indexOf(channel) < 0) {
                        mGroupList.get(-5).channnels.add(channel);
                    }
                }
            }
        }
        //String log = "end channelGrouping" + (new Date().getTime() - time);
    }

    public static void parseTopVodChannels(String strType) {
        mTopChannelList = new ArrayList<>();

        int nCount = 0;
        for (ChannelBean channel : mChannelList) {
            if (channel.getLevel() == 18)
                continue;

            boolean isCorrect = false;
            for (ChannelBean.TagsBean tags : channel.getTags()) {
                if (tags.restrictedAccess) {
                    isCorrect = false;
                    break;
                }

                if (strType.compareTo("All") == 0) {
                    isCorrect = true;
                } else {
                    String[] type = tags.getName().getInit().split("/");
                    if (type[0].compareTo(strType) == 0) {
                        isCorrect = true;
                        break;
                    }
                }
            }
            if (isCorrect == false)
                continue;

            mTopChannelList.add(channel);

            nCount++;
            if (nCount >= 10)
                break;
        }
    }

    public static VodChannelBean getFullChannelBean(String str) {
        if (AuthInstance.mAuthInfo == null) {
            return null;
        }
        try {
            return (VodChannelBean) JSON.parseObject(LibTvServiceClient.getInstance().getCacheVod(str), VodChannelBean.class);
        } catch (Exception e) {
            e.toString();
            return null;
        }
    }

    private static void onFail(String strFuncName) {
        PrefUtils.ToastShort("Failed to get vod channel list! - " + strFuncName);
    }
}
