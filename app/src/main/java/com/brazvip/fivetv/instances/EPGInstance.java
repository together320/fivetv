package com.brazvip.fivetv.instances;

import android.os.Message;
import android.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.annotation.JSONField;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class EPGInstance {
    private static String mCacheKey = "EPGInstance";

    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    public static volatile List<EpgBeans> mEpgs;
    public static volatile LruCache<Integer, List<EpgBeans.EpgBean>> liveEpgs;
    public static volatile LruCache<Integer, HashMap<Long, List<EpgBeans.EpgBean>>> liveEpgsMap;

    public static void parseEPGInfo(final String text, boolean savePref) {
        new Thread() {
            @Override
            public void run() {
                if (savePref == true) {
                    PrefUtils.setPrefString(Constant.PREFS_EPG_INFO, text);
                }

                int cacheSize = (int) (Runtime.getRuntime().totalMemory() / 8);
                liveEpgs = new LruCache(cacheSize);
                liveEpgsMap = new LruCache(cacheSize);
                long dayWithZeroTime = Utils.getDayWithZeroTime(new Date().getTime() + 86400000);
                try {
                    try {
                        List<EpgBeans> epgList = parseEpgJson(text);
                        for (int i = 0; i < epgList.size(); i++) {
                            LinkedHashMap linkedHashMap = new LinkedHashMap();
                            ArrayList arrayList = new ArrayList();
                            List<EpgBeans.EpgBean> epg = epgList.get(i).getEpg();
                            Collections.sort(epg, new Comparator<EpgBeans.EpgBean>() {
                                @Override
                                public int compare(EpgBeans.EpgBean epgBean, EpgBeans.EpgBean epgBean2) {
                                    if (epgBean.getTime().equals(epgBean2.getTime())) {
                                        return 0;
                                    }
                                    boolean z = Config.isEPGAssendingSort;
                                    Long time = epgBean.getTime();
                                    return (z ? time.longValue() >= epgBean2.getTime().longValue() : time.longValue() < epgBean2.getTime().longValue()) ? -1 : 1;
                                }
                            });
                            long j = 0;
                            for (int i3 = 0; i3 < epg.size(); i3++) {
                                if (!Config.f8909g || epg.get(i3).getTime().longValue() < dayWithZeroTime) {
                                    long dayWithZeroTime2 = Utils.getDayWithZeroTime(epg.get(i3).getTime().longValue());
                                    if (j != dayWithZeroTime2) {
                                        if (j != 0) {
                                            linkedHashMap.put(Long.valueOf(j), arrayList);
                                            arrayList = new ArrayList();
                                        }
                                        arrayList.add(epg.get(i3));
                                        j = dayWithZeroTime2;
                                    } else {
                                        arrayList.add(epg.get(i3));
                                    }
                                }
                            }
                            if (arrayList.size() > 0) {
                                linkedHashMap.put(Long.valueOf(j), arrayList);
                            }
                            liveEpgs.put(Integer.valueOf(epgList.get(i).getId()), epgList.get(i).getEpg());
                            if (linkedHashMap.size() > 0) {
                                liveEpgsMap.put(Integer.valueOf(epgList.get(i).getId()), linkedHashMap);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } finally {
                    //SopCast.handler.sendEmptyMessage(30);

                    Message msg = new Message();
                    msg.what = Constant.MSG_EPG_LOADED;
                    MainActivity.SendMessage(msg);
                }
            }
        }.start();
    }

    public static String getNameById(int i) {
        int size;
        if (liveEpgs != null) {
            synchronized (liveEpgs) {
                size = liveEpgs.size();
            }
            if (size > 0) {
                long time = new Date().getTime() + Utils.DELTA_TIME;
                List list = (List) liveEpgs.get(Integer.valueOf(i));
                if (list != null) {
                    for (int i3 = 0; i3 < list.size(); i3++) {
                        if (((EpgBeans.EpgBean) list.get(i3)).getTime().longValue() <= time && time <= ((EpgBeans.EpgBean) list.get(i3)).getEndTime().longValue()) {
                            return ((EpgBeans.EpgBean) list.get(i3)).getName();
                        }
                    }
                    return "";
                }
                return "";
            }
            return "";
        }
        return "";
    }


    public static void Refresh() {

        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.EPG);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (Constant.OFFLINE_TEST == true) {
                    String strInfo = PrefUtils.getPrefString(Constant.PREFS_EPG_INFO, "");
                    if (strInfo.length() > 0) {
                        parseEPGInfo(strInfo, false);
                        return;
                    }
                }

                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                parseEPGInfo(response.body(), true);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    parseEPGInfo(response.body(), true);
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

    public static List<EpgBeans> parseEpgJson(String str) {
        JSONReader jSONReader = new JSONReader(new StringReader(str));
        jSONReader.startArray();
        ArrayList arrayList = new ArrayList();
        while (jSONReader.hasNext()) {
            jSONReader.startObject();
            EpgBeans epgBeans = new EpgBeans();
            while (jSONReader.hasNext()) {
                String readString = jSONReader.readString();
                readString.getClass();
                char c = 65535;
                switch (readString.hashCode()) {
                    case 3355:
                        if (readString.equals("id")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 100636:
                        if (readString.equals("epg")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 457705205:
                        if (readString.equals("hasPlayBack")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        epgBeans.setId(Integer.parseInt(jSONReader.readObject().toString()));
                        break;
                    case 1:
                        jSONReader.startArray();
                        ArrayList arrayList2 = new ArrayList();
                        while (jSONReader.hasNext()) {
                            jSONReader.startObject();
                            EpgBeans.EpgBean epgBean = new EpgBeans.EpgBean();
                            while (jSONReader.hasNext()) {
                                String readString2 = jSONReader.readString();
                                Object readObject = jSONReader.readObject();
                                String obj = readObject != null ? readObject.toString() : "";
                                if (readString2.equals("endTime")) {
                                    try {
                                        epgBean.setEndTime(Long.valueOf(TIME_FORMAT.parse(obj.replace("Z", "+0000")).getTime()));
                                    } catch(ParseException pe) {
                                        pe.printStackTrace();
                                    }
                                }
                                if (readString2.equals("id")) {
                                    epgBean.setId(obj);
                                }
                                if (readString2.equals(SerializableCookie.NAME)) {
                                    epgBean.setName(obj);
                                }
                                if (readString2.equals("playbackUrl")) {
                                    epgBean.setPlaybackUrl(obj);
                                } else if (readString2.equals("time")) {
                                    try {
                                        epgBean.setTime(Long.valueOf(TIME_FORMAT.parse(obj.replace("Z", "+0000")).getTime()));
                                    } catch(ParseException pe) {
                                        pe.printStackTrace();
                                    }
                                }
                            }
                            jSONReader.endObject();
                            arrayList2.add(epgBean);
                        }
                        jSONReader.endArray();
                        epgBeans.setEpg(arrayList2);
                        break;
                    case 2:
                        epgBeans.setHasPlayBack(Boolean.parseBoolean(jSONReader.readObject().toString()));
                        break;
                    default:
                        jSONReader.readObject();
                        break;
                }
            }
            arrayList.add(epgBeans);
            jSONReader.endObject();
        }
        jSONReader.endArray();
        jSONReader.close();
        return arrayList;
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get epg list!");

        Message msg = new Message();
        msg.what = Constant.MSG_EPG_LOADED;
        MainActivity.SendMessage(msg);
    }
}
