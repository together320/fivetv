package com.brazvip.fivetv.instances;

import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
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
import com.brazvip.fivetv.beans.vod.VodGroupL2;
import com.brazvip.fivetv.layouts.VodLayout;
import com.brazvip.fivetv.utils.BaseCrypt;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.brazvip.fivetv.utils.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Priority;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import p156o4.C2320l;

public class VodChannelInstance {

    public static final String TAG = "VodChannelInstance";

    public static final String FAVORITES_GROUP_ID = "-5";
    public static final String RESULTS_GROUP_ID = "-10";
    public static boolean cancelSearch = false;
    private static volatile Set<String> favoriteVodChannelIds;
    public static volatile Map<String, List<VodGroupL2>> newVodL1L2Groups;
    public static volatile Map<String, List<VodChannelBean>> cacheVodsSubGroups = new ConcurrentHashMap();
    public static volatile List<VodChannelBean> allVods = Collections.synchronizedList(new ArrayList());

    public static void addFavoriteChannel(final String s) {
        VodChannelInstance.favoriteVodChannelIds.add(s);
        Utils.saveSPStringSet(Config.SP_FAV_VOD_CHANNEL, VodChannelInstance.favoriteVodChannelIds);
    }

    public static List<VodChannelBean> doSearch(CharSequence charSequence) {
        cancelSearch = false;
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                arrayList = (ArrayList) allVods.stream().filter(new PredicateObject(charSequence, 1)).distinct().limit(100L).collect(Collectors.toList());
            } catch (Exception e) {
                e.toString();
            }
        }
        return sortVodChannels(arrayList);
    }

    public static boolean containsInList(final List<VodChannelBean> list, final String s) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            return list.stream().anyMatch(new a(s, 0));
//        }
//        final Iterator<Object> iterator = (Iterator<Object>)list.iterator();
//        while (iterator.hasNext()) {
//            if (iterator.next()._id.equals(s)) {
//                return true;
//            }
//        }
        return false;
    }


    public static List<VodChannelBean> getChannelsByGroupKey(String str, boolean z) {
        String cacheVodSubgroups;
        if (cacheVodsSubGroups == null || !cacheVodsSubGroups.containsKey(str) || cacheVodsSubGroups.get(str).size() <= 0) {
            LibTvServiceClient libTvServiceClient = LibTvServiceClient.getInstance();
            synchronized (libTvServiceClient) {
                cacheVodSubgroups = libTvServiceClient.getCacheVodSubgroups(str);
            }
            if (TextUtils.isEmpty(cacheVodSubgroups)) {
                return new ArrayList();
            }
            try {
                List<VodChannelBean> list = (List) JSON.parseObject(cacheVodSubgroups, new TypeReference<List<VodChannelBean>>() { // from class: org.sopcast.android.p220b.BSVodChannel.1
                }, new Feature[0]);
                if (list != null && list.size() > 0) {
                    for (VodChannelBean vodChannelBean : list) {
                        list.get(list.indexOf(vodChannelBean)).restricted = z;
                    }
                    for (VodChannelBean vodChannelBean2 : list) {
                        boolean z2 = vodChannelBean2.restricted;
                    }
                    cacheVodsSubGroups.put(str, list);
                    for (VodChannelBean vodChannelBean3 : list) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            allVods.removeIf(new PredicateObject(vodChannelBean3, 2));
                            allVods.add(vodChannelBean3);
                        }
                    }
                }
                return cacheVodsSubGroups.get(str);
            } catch (Exception unused) {
                return new ArrayList();
            }
        }
        return cacheVodsSubGroups.get(str);
    }

    public static List<VodChannelBean> getFavoriteChannels() {
        ArrayList arrayList = new ArrayList();
        if (favoriteVodChannelIds == null) {
            loadSavedFavoriteIds();
        }
        for (String str : favoriteVodChannelIds) {
            try {
                VodChannelBean vodChannelBean = (VodChannelBean) JSON.parseObject(LibTvServiceClient.getInstance().getCacheVod(str), VodChannelBean.class);
                if (vodChannelBean != null) {
                    arrayList.add(vodChannelBean);
                }
            } catch (Exception unused) {
            }
        }
        return arrayList;
    }

    public static VodChannelBean getFullChannelBean(String str) {
        if (AuthInstance.mAuthInfo == null) {
            return null;
        }
        try {
            return JSON.parseObject(LibTvServiceClient.getInstance().getCacheVod(str), VodChannelBean.class);
        } catch (Exception e) {
            e.toString();
            return null;
        }
    }

    public static boolean isFavoriteVod(String str) {
        if (favoriteVodChannelIds == null) {
            loadSavedFavoriteIds();
        }
        return favoriteVodChannelIds.contains(str);
    }

    public static boolean containsInList(String str, VodChannelBean vodChannelBean) {
        return str.equals(vodChannelBean._id);
    }

    public static boolean doSearch(CharSequence charSequence, VodChannelBean vodChannelBean) {
        return vodChannelBean != null &&
                removeCharSpecial(vodChannelBean.title.toLowerCase()).contains(removeCharSpecial(charSequence.toString().toLowerCase())) &&
                !vodChannelBean.restricted;
    }

    public static boolean getChannelsByGroupKey(VodChannelBean vodChannelBean, VodChannelBean vodChannelBean2) {
        return vodChannelBean2._id.equals(vodChannelBean._id);
    }

    public static int compare(Map.Entry entry, Map.Entry entry2) {
        return ((Integer) entry.getValue()).compareTo((Integer) entry2.getValue());
    }

    private static void loadSavedFavoriteIds() {
        favoriteVodChannelIds = new HashSet(Utils.getStringSet(Config.SP_FAV_VOD_CHANNEL, new HashSet()));
    }

    private static void parseVodGroups(String str) {
        String string;
        ArrayList arrayList;
        Integer decode = null;

        Map<String, List<VodGroupL2>> map = null;
        try {
            try {
                map = JSON.parseObject(str, new TypeReference<Map<String, List<VodGroupL2>>>() { }, new Feature[0]);
                newVodL1L2Groups = new LinkedHashMap();
                newVodL1L2Groups.put(SopApplication.getSopContext().getString(R.string.All), new ArrayList());
                HashMap hashMap = new HashMap();
                for (Map.Entry<String, List<VodGroupL2>> entry : map.entrySet()) {
                    for (VodGroupL2 vodGroupL2 : entry.getValue()) {
                        decode = 0;
                        try {
                            decode = Integer.decode(vodGroupL2._id);
                        } catch (Exception ex) {
                        }
                        if (hashMap.get(entry.getKey()) == null || ((Integer) hashMap.get(entry.getKey())).intValue() > decode.intValue()) {
                            hashMap.put(entry.getKey(), decode);
                        }
                    }
                }
                ArrayList arrayList2 = new ArrayList(hashMap.entrySet());
                Collections.sort(arrayList2, new C2320l(6));
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) it.next();
                    newVodL1L2Groups.put((String) entry2.getKey(), (List) map.get(entry2.getKey()));
                }
                newVodL1L2Groups.size();
                l1l2Grouping();
            } catch (Exception e) {
                e.toString();
                if (newVodL1L2Groups != null) {
                    return;
                }
            }
            if (newVodL1L2Groups == null) {
                newVodL1L2Groups = new LinkedHashMap();
                string = SopApplication.getSopContext().getString(R.string.All);
                arrayList = new ArrayList();
                newVodL1L2Groups.put(string, arrayList);
            }
        } catch (Throwable th) {
            if (newVodL1L2Groups == null) {
                newVodL1L2Groups = new LinkedHashMap();
                newVodL1L2Groups.put(SopApplication.getSopContext().getString(R.string.All), new ArrayList());
            }
            throw th;
        }
    }

    public static String removeCharSpecial(String str) {
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(str, Normalizer.Form.NFD)).replaceAll("");
    }

    public static void removeFavoriteChannel(String str) {
        favoriteVodChannelIds.remove(str);
        Utils.saveSPStringSet(Config.SP_FAV_VOD_CHANNEL, favoriteVodChannelIds);
    }

    private static List<VodChannelBean> sortVodChannels(List<VodChannelBean> list) {
        Collections.sort(list, new Comparator<VodChannelBean>() { // from class: org.sopcast.android.p220b.BSVodChannel.2
            @Override // java.util.Comparator
            public int compare(VodChannelBean vodChannelBean, VodChannelBean vodChannelBean2) {
                return vodChannelBean.getId().compareTo(vodChannelBean2.getId());
            }
        });
        return list;
    }

    public static void getAllVodGroups() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getVodGroups();
            }
        }).start();
    }

    public static synchronized void getVodGroups() {
        if (newVodL1L2Groups == null || newVodL1L2Groups.size() <= 0) {
            String cacheVodGroups = LibTvServiceClient.getInstance().getCacheVodGroups();
            if (TextUtils.isEmpty(cacheVodGroups)) {
                String m1441b = MainActivity.cacheManager.m1441b("vodGroups");
                String decrypt = m1441b != null ? BaseCrypt.decrypt(m1441b) : null;
                if (decrypt != null) {
                    parseVodGroups(decrypt);
                    return;
                }
            } else {
                parseVodGroups(cacheVodGroups);
                if (!MainActivity.logMemoryStats() && !MainActivity.isSystemOnLowMemory()) {
                    MainActivity.cacheManager.m1445a("vodGroups", BaseCrypt.encrypt(cacheVodGroups), Config.f8918p / Priority.UI_NORMAL);
                }
            }
            onSuccess();
        }
    }

    public static void onSuccess() {
        Message msg = new Message();
        msg.what = Constant.MSG_VOD_LOADED;
        MainActivity.SendMessage(msg);
    }

    public static void l1l2Grouping() {
        if (newVodL1L2Groups != null) {
            new Date().getTime();
            loadSavedFavoriteIds();
            favoriteVodChannelIds.toString();
            ArrayList arrayList = new ArrayList();
            VodGroupL2 vodGroupL2 = new VodGroupL2();
            vodGroupL2.name = SopApplication.getSopContext().getString(R.string.Favorites);
            vodGroupL2._id = FAVORITES_GROUP_ID;
            arrayList.add(vodGroupL2);
            for (List<VodGroupL2> list : newVodL1L2Groups.values()) {
                for (VodGroupL2 vodGroupL22 : list) {
                    arrayList.add(vodGroupL22);
                }
            }
            newVodL1L2Groups.put(SopApplication.getSopContext().getString(R.string.All), arrayList);
            if (VodLayout.IS_SEARCH_STATE) {
                Config.maxVodColumns = 4;
                new Group();
                newVodL1L2Groups.put(SopApplication.getSopContext().getString(R.string.Results), new ArrayList());
            }
        }
    }


//    private static String mCacheKey = "VodChannelInstance";
//
//    public static int f13619e = 0;
//
//    /* renamed from: f */
//    public static HashMap<String, Integer> f13620f;
//
//    @JSONField  //("this")
//    /* renamed from: g 13621 */
//    public static volatile List<ChannelBean> mChannelList;
//
//    @JSONField  //("this")
//    /* renamed from: h */
//    public static volatile SparseArray<Group> mGroupList;
//
//    public static volatile SparseArray<GroupL1> mGroupL1List;
//
//    public static volatile Set<String> mFavoriteVodChannelList;
//
//    public static volatile List<ChannelBean> mTopChannelList;
//
//    public static synchronized void Refresh() {
//        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.VOD_CHANNEL);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d("VodInstance", "URL: " + url);
//                if (Constant.OFFLINE_TEST == true) {
//                    String strInfo = PrefUtils.getPrefString(Constant.PREFS_VOD_INFO, "");
//                    if (strInfo.length() > 0) {
//                        asyncParseVodChannels(strInfo, false);
//                        return;
//                    }
//                }
//
//                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
//                        new StringCallback() {
//                            @Override
//                            public void onCacheSuccess(Response<String> response) {
//                                Log.d("VodInstance", "onCacheSuccess");
//                                PrefUtils.ToastShort("Getting vod channel list! - onCacheSuccess");
//                                asyncParseVodChannels(response.body(), true);
//                            }
//
//                            @Override
//                            public void onError(Response<String> response) {
//                                Log.e("VodInstance", "onError");
//                                onFail("onError");
//                            }
//
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                if (response.isSuccessful()) {
//                                    Log.d("VodInstance", "onSuccess - response successful!");
//                                    asyncParseVodChannels(response.body(), true);
//                                } else {
//                                    Log.e("VodInstance", "onSuccess - response failed!");
//                                    onFail("onSuccess");
//                                }
//                            }
//                        }
//                );
//            }
//        }
//        ).start();
//    }
//
//    public static void parseJson(String text) {
//        mChannelList = JSON.parseArray(text, ChannelBean.class);
//        if (mChannelList != null) {
//            parseVodChannels();
//        }
//    }
//
//    private static void asyncParseVodChannels(String result, boolean savePref) {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    if (savePref == true) {
//                        PrefUtils.setPrefString(Constant.PREFS_VOD_INFO, result);
//                    }
//
//                    VodChannelInstance.parseJson(result);
//
//                    Message msg = new Message();
//                    msg.what = Constant.MSG_VOD_LOADED;
//                    MainActivity.SendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//
//
//    public static void parseVodChannels() {
//        if (mChannelList == null) {
//            return;
//        }
//        //long time = new Date().getTime();
//        //String log1 = "start channelGrouping... " + time;
//        mFavoriteVodChannelList = new HashSet<>(PrefUtils.getPrefStringSet(Config.SP_FAV_VOD_CHANNEL, new HashSet<>()));
//        //String log2 = "############ vodchannelGrouping " + f13624j.toString();
//        mGroupList = new SparseArray<>();
//        f13620f = new HashMap<>();
//        mGroupL1List = new SparseArray<>();
//        GroupL1 groupL1 = new GroupL1();
//        int pos = f13619e;
//        f13619e = pos + 1;
//        groupL1.id = pos;
//        groupL1.name = SopApplication.getAppContext().getString(R.string.All);
//        groupL1.groups = new HashSet<>();
//        mGroupL1List.put(groupL1.id, groupL1);
//        f13620f.put(groupL1.name, Integer.valueOf(groupL1.id));
//        //String str2 = "########## VodFragment.IS_SEARCH_STATE: " + VodLayout.f13907k;
//        if (VodLayout.isSearchState) {
//            RestApiUtils.vodGridSpanCount = 4;
//            Collections.sort(mChannelList, new Comparator<ChannelBean>() { //C3529z, e.b.a.b.z
//                @Override
//                public int compare(ChannelBean o1, ChannelBean o2) {
//                    return o1.getDlEver() >= o2.getDlEver() ? -1 : 1;
//                    /*long diff = o1.getDlEver() - o2.getDlEver();
//                    return (diff == 0 ? 0 : (diff > 0 ? 1 : -1));*/
//                }
//            });
//            Group group = new Group();
//            group.name = SopApplication.getAppContext().getString(R.string.Results);
//            group.type = -10;
//            group.channnels = new ArrayList<>();
//            mGroupList.put(-10, group);
//        } else {
//            Collections.sort(mChannelList, new Comparator<ChannelBean>() { //C3500A
//                @Override
//                public int compare(ChannelBean o1, ChannelBean o2) {
//                    int diff = o1.getSid() - o2.getSid();
//                    return diff < 0 ? -(o1.getChid() - o2.getChid()) : diff;
//                }
//            });
//            RestApiUtils.vodGridSpanCount = 6;
//        }
//        if (mChannelList.size() > 0) {
//            Group group = new Group();
//            group.id = 0;
//            group.name = SopApplication.getAppContext().getString(R.string.Favorites);
//            group.type = -5;
//            group.channnels = new ArrayList<>();
//            mGroupList.put(-5, group);
//            groupL1.groups.add(Integer.valueOf(group.id));
//        }
//        for (ChannelBean channel : mChannelList) {
//            for (ChannelBean.TagsBean tags : channel.getTags()) {
//                if (VodLayout.isSearchState) {
//                    if (channel.getLevel() < 18 && mGroupList.get(-10).channnels.indexOf(channel) < 0) {
//                        mGroupList.get(-10).channnels.add(channel);
//                    }
//                } else if (mGroupList.get(tags.getId()) != null) {
//                    if (mGroupList.get(tags.getId()).restrictedAccess || channel.getLevel() != 18) {
//                        mGroupList.get(tags.getId()).channnels.add(channel);
//                    }
//                } else {
//                    Group group = new Group();
//                    group.id = tags.getId();
//                    group.name = tags.getName().getInit();
//                    group.restrictedAccess = tags.isRestrictedAccess();
//                    if (group.restrictedAccess || channel.getLevel() != 18) {
//                        group.type = tags.getType();
//                        group.channnels = new ArrayList<>();
//                        group.channnels.add(channel);
//                        mGroupList.put(tags.getId(), group);
//                        String[] split = tags.getName().getInit().split("/");
//                        if (split.length >= 2) {
//                            if (!f13620f.containsKey(split[0])) {
//                                //String log = "new 2lmenu" + split[0];
//                                GroupL1 l1 = new GroupL1();
//                                int id = f13619e;
//                                f13619e = id + 1;
//                                l1.id = id;
//                                l1.name = split[0];
//                                l1.groups = new HashSet<>();
//                                l1.groups.add(Integer.valueOf(group.id));
//                                mGroupL1List.put(l1.id, l1);
//                                f13620f.put(split[0], Integer.valueOf(l1.id));
//                            } else {
//                                mGroupL1List.get(f13620f.get(split[0]).intValue())
//                                        .groups.add(Integer.valueOf(group.id));
//                            }
//                        }
//                        groupL1.groups.add(Integer.valueOf(group.id));
//                    }
//                }
//                if (channel.getLevel() < 18 && mGroupList.get(-5) != null) {
//                    if (mFavoriteVodChannelList.contains("" + channel.getChid()) &&
//                            mGroupList.get(-5).channnels.indexOf(channel) < 0) {
//                        mGroupList.get(-5).channnels.add(channel);
//                    }
//                }
//            }
//        }
//        //String log = "end channelGrouping" + (new Date().getTime() - time);
//    }
//
//    public static void parseTopVodChannels(String strType) {
//        mTopChannelList = new ArrayList<>();
//
//        int nCount = 0;
//        for (ChannelBean channel : mChannelList) {
//            if (channel.getLevel() == 18)
//                continue;
//
//            boolean isCorrect = false;
//            for (ChannelBean.TagsBean tags : channel.getTags()) {
//                if (tags.restrictedAccess) {
//                    isCorrect = false;
//                    break;
//                }
//
//                if (strType.compareTo("All") == 0) {
//                    isCorrect = true;
//                } else {
//                    String[] type = tags.getName().getInit().split("/");
//                    if (type[0].compareTo(strType) == 0) {
//                        isCorrect = true;
//                        break;
//                    }
//                }
//            }
//            if (isCorrect == false)
//                continue;
//
//            mTopChannelList.add(channel);
//
//            nCount++;
//            if (nCount >= 10)
//                break;
//        }
//    }
//
//    public static VodChannelBean getFullChannelBean(String str) {
//        if (AuthInstance.mAuthInfo == null) {
//            return null;
//        }
//        try {
//            return (VodChannelBean) JSON.parseObject(LibTvServiceClient.getInstance().getCacheVod(str), VodChannelBean.class);
//        } catch (Exception e) {
//            e.toString();
//            return null;
//        }
//    }
//
//    private static void onFail(String strFuncName) {
//        PrefUtils.ToastShort("Failed to get vod channel list! - " + strFuncName);
//    }
}
