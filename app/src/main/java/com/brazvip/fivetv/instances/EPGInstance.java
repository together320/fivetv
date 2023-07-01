package com.brazvip.fivetv.instances;

import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import com.alibaba.fastjson.JSONReader;
import com.brazvip.fivetv.BSCF;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.utils.BaseCrypt;
import com.brazvip.fivetv.utils.Utils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Priority;
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
    public static final String TAG = "EPGInstance";
    public static SimpleDateFormat dateFormatSSSZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
    public static volatile LruCache<Integer, HashMap<Long, List<EpgBeans.EpgBean>>> liveEpgsMap;
    public static volatile LruCache<Integer, List<EpgBeans.EpgBean>> liveEpgs;
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static List<EpgBeans> m1332b(String str) throws ParseException {
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
                                    epgBean.setEndTime(Long.valueOf(dateFormatSSSZ.parse(obj.replace("Z", "+0000")).getTime()));
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
                                    epgBean.setTime(Long.valueOf(dateFormatSSSZ.parse(obj.replace("Z", "+0000")).getTime()));
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

    public static String getNameById(int i) {
        int i2;
        if (liveEpgs != null) {
            synchronized (liveEpgs) {
                i2 = liveEpgs.size();
            }
            if (i2 > 0) {
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

    public static synchronized void getAllEPGs() {
//        final String url = BSUser.getUrl(BSUser.ServiceType.EPG);
//        if (url.equals("")) {
//            SopCast.handler.sendEmptyMessage(30);
//        } else {
        {
            final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.EPG);
            
            new Thread() {
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        Log.d("EPGInstance", "URL: " + url);

                        new GetRequest(url)
                                .removeHeader(HttpHeaders.HEAD_KEY_USER_AGENT)
                                .headers(HttpHeaders.HEAD_KEY_USER_AGENT, BSCF.userAgent)
                                .tag(this).cacheKey(TAG)
                                .cacheMode(CacheMode.NO_CACHE)
                                .execute(new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                parseEPGInfo(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                String m1441b = MainActivity.cacheManager.m1441b("epg");
                                String decrypt = m1441b != null ? BaseCrypt.decrypt(m1441b) : null;
                                if (decrypt == null) {
                                    MainActivity.handler.sendEmptyMessage(30);
                                    return;
                                }
                                if (Config.f8900T) {
                                    MainActivity.handler.sendEmptyMessage(30);
                                }
                                parseEPGInfo(decrypt);
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (Config.f8900T) {
                                    MainActivity.handler.sendEmptyMessage(30);
                                }
                                parseEPGInfo(response.body());
                                if (MainActivity.logMemoryStats() || MainActivity.isSystemOnLowMemory()) {
                                    return;
                                }
                                MainActivity.cacheManager.m1445a("epg", BaseCrypt.encrypt(response.body()), (Config.f8918p / Priority.UI_NORMAL) / 2);
                            }
                        });
                    } catch (Exception unused) {
                        MainActivity.handler.sendEmptyMessage(30);
                    }
                }
            }.start();
        }
    }

    public static void parseEPGInfo(final String str) {
        new Thread() {
            @Override
            public void run() {
                int cacheSize = (int) (Runtime.getRuntime().totalMemory() / 8);
                liveEpgs = new LruCache<>(cacheSize);
                liveEpgsMap = new LruCache<>(cacheSize);
                long dayWithZeroTime = Utils.getDayWithZeroTime(new Date().getTime() + Utils.DELTA_TIME + 86400000);
                dateFormat2.format(dayWithZeroTime);
                try {
                    try {
                        new Date().getTime();
                        List<EpgBeans> m1332b = m1332b(str);
                        m1332b.size();
                        new Date().getTime();
                        for (int i2 = 0; i2 < m1332b.size(); i2++) {
                            LinkedHashMap linkedHashMap = new LinkedHashMap();
                            ArrayList arrayList = new ArrayList();
                            List<EpgBeans.EpgBean> epg = m1332b.get(i2).getEpg();
                            Collections.sort(epg, new Comparator<EpgBeans.EpgBean>() {
                                @Override // java.util.Comparator
                                public int compare(EpgBeans.EpgBean epgBean, EpgBeans.EpgBean epgBean2) {
                                    if (epgBean.getTime().equals(epgBean2.getTime())) {
                                        return 0;
                                    }
                                    boolean z = Config.f8907e;
                                    Long time = epgBean.getTime();
                                    return (z ? time >= epgBean2.getTime() : time < epgBean2.getTime()) ? -1 : 1;
                                }
                            });
                            long j = 0;
                            for (int i3 = 0; i3 < epg.size(); i3++) {
                                if (!Config.f8909g || epg.get(i3).getTime() < dayWithZeroTime) {
                                    long dayWithZeroTime2 = Utils.getDayWithZeroTime(epg.get(i3).getTime());
                                    if (j != dayWithZeroTime2) {
                                        if (j != 0) {
                                            linkedHashMap.put(j, arrayList);
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
                                linkedHashMap.put(j, arrayList);
                            }
                            liveEpgs.put(m1332b.get(i2).getId(), m1332b.get(i2).getEpg());
                            if (linkedHashMap.size() > 0) {
                                liveEpgsMap.put(m1332b.get(i2).getId(), linkedHashMap);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } finally {
                    //MainActivity.handler.sendEmptyMessage(30);

                    Message msg = new Message();
                    msg.what = Constant.MSG_EPG_LOADED;
                    MainActivity.SendMessage(msg);
                }
            }
        }.start();
    }
}