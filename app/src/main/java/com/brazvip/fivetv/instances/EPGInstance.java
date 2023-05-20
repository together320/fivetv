package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;
import android.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.annotation.JSONField;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EPGInstance {
    public static volatile List<EpgBeans> mEpgs;

    private static String mCacheKey = "EPGInstance";

    public static final String TIME_FORMAT_TEXT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /* renamed from: c 13652 */
    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    @JSONField  //("this")
    /* renamed from: d 13653 */
    public static volatile LruCache<Integer, List<EpgBeans.EpgBean>> mEpgListCache;

    @JSONField  //("this")
    /* renamed from: e 13654 */
    public static volatile LruCache<Integer, HashMap<Long, List<EpgBeans.EpgBean>>> mEpgCacheMap;

    /* renamed from: f */
    public SimpleDateFormat f13655f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /* renamed from: g */
    public SimpleDateFormat f13656g = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /* renamed from: h */
    public SimpleDateFormat f13657h = new SimpleDateFormat("EEE, MM-dd", Locale.getDefault());

    public static void parseEPG(final String text) {
        new Thread() {
            @Override
            public void run() {
                long totalMemory = Runtime.getRuntime().totalMemory();
                int cacheSize = (int) (totalMemory / 8);
                //String log = "=============totalMemory: " + totalMemory + " cacheSize: " + cacheSize;
                EPGInstance.mEpgListCache = new LruCache<>(cacheSize);
                EPGInstance.mEpgCacheMap = new LruCache<>(cacheSize);
                long tomorrowZeroTime = PrefUtils.getDateOfTime(new Date().getTime() + PrefUtils.f14035a + 86400000);
                //String log = "tomorrowZeroTime " + f13649b.f13655f.format(tomorrowZeroTime);
                //try {
                try {
                    //long time = new Date().getTime();
                    //String log2 = "parse epg start ... " + new Date().getTime();
                    List<EpgBeans> chEpg = EPGInstance.parseEpgJson(text);
                    //String log3 = "parse epg done ..., parseEpgJson chEpg.size: " + chEpg.size() + " " + (new Date().getTime() - time);
                    for (int i = 0; i < chEpg.size(); i++) {
                        HashMap<Long, List<EpgBeans.EpgBean>> map = new HashMap<>();
                        ArrayList<EpgBeans.EpgBean> dayEpgs = new ArrayList<>();
                        List<EpgBeans.EpgBean> epg = chEpg.get(i).getEpg();
                        if (RestApiUtils.f13740e) {
                            Collections.sort(epg, new Comparator<EpgBeans.EpgBean>() { //C3514l(this)
                                @Override
                                public int compare(EpgBeans.EpgBean o1, EpgBeans.EpgBean o2) {
                                    if (Objects.equals(o1.getTime(), o2.getTime())) {
                                        return 0;
                                    }
                                    return o1.getTime() < o2.getTime() ? 1 : -1;
                                }
                            });
                        }
                        long selDate = 0;
                        for (int k = 0; k < epg.size(); k++) {
                            if (!RestApiUtils.f13742g || epg.get(k).getTime().longValue() < tomorrowZeroTime) {
                                long curDate = PrefUtils.getDateOfTime(epg.get(k).getTime().longValue());
                                if (selDate != curDate) {
                                    if (selDate != 0) {
                                        map.put(Long.valueOf(selDate), dayEpgs);
                                        dayEpgs = new ArrayList<>();
                                    }
                                    dayEpgs.add(epg.get(k));
                                    selDate = curDate;
                                } else {
                                    dayEpgs.add(epg.get(k));
                                }
                            }
                        }
                        if (dayEpgs.size() > 0) {
                            map.put(Long.valueOf(selDate), dayEpgs);
                        }
                        EPGInstance.mEpgListCache.put(Integer.valueOf(chEpg.get(i).getId()), chEpg.get(i).getEpg());
                        if (map.size() > 0) {
                            EPGInstance.mEpgCacheMap.put(Integer.valueOf(chEpg.get(i).getId()), map);
                        }
                    }
                    //String log4 = "deal with epg done ..." + (new Date().getTime() - time);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.what = Constant.MSG_EPG_LOADED;
                MainActivity.SendMessage(msg);

                //MainActivity.mMsgHandler.sendEmptyMessage(30);
                //} catch (Throwable th) {
                //  MainActivity.mMsgHandler.sendEmptyMessage(30);
                //  throw th;
                //}
            }
        }.start(); //C3515m(this, text)
    }

    /* renamed from: a 2434 */
    public static String getNameById(int id) {
        String name = "";
        if (mEpgListCache != null && mEpgListCache.size() > 0) {
            long time = new Date().getTime() + PrefUtils.f14035a;
            List<EpgBeans.EpgBean> list = mEpgListCache.get(Integer.valueOf(id));
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTime().longValue() <= time &&
                            time <= list.get(i).getEndTime().longValue()) {
                        name = list.get(i).getName();
                    }
                }
            }
        }
        return name;
    }

    public static void Refresh() {
        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.EPG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                parseEPG(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    parseEPG(response.body());
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
        mEpgs = JSON.parseArray(text, EpgBeans.class);

        Message msg = new Message();
        msg.what = Constant.MSG_EPG_LOADED;
        MainActivity.SendMessage(msg);
    }

    private static void asyncParseEpgs(String result) {
        new Thread() {
            @Override
            public void run() {
                try {
                    EPGInstance.parseJson(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static List<EpgBeans> parseEpgJson(String text) {
        JSONReader jsr = new JSONReader(new StringReader(text));
        jsr.startArray();
        ArrayList<EpgBeans> result = new ArrayList<>();
        while (jsr.hasNext()) {
            jsr.startObject();
            EpgBeans beans = new EpgBeans();
            while (jsr.hasNext()) {
                String key = jsr.readString();
                switch (key) {
                    case "epg":
                        jsr.startArray();
                        ArrayList<EpgBeans.EpgBean> tmpList = new ArrayList<>();
                        while (jsr.hasNext()) {
                            jsr.startObject();
                            EpgBeans.EpgBean epg = new EpgBeans.EpgBean();
                            while (jsr.hasNext()) {
                                String subKey = jsr.readString();
                                Object subObj = jsr.readObject();
                                String subVal = subObj != null ? subObj.toString() : "";
                                switch (subKey) {
                                    case "endTime": {
                                        long time = 0;
                                        try {
                                            time = TIME_FORMAT.parse(subVal.replace("Z", "+0000")).getTime();
                                        } catch (ParseException e) {
                                            time = 0;
                                        }
                                        epg.setEndTime(Long.valueOf(time));
                                        break;
                                    }
                                    case "id":
                                        epg.setId(subVal);
                                        break;
                                    case "name":
                                        epg.setName(subVal);
                                        break;
                                    case "playbackUrl":
                                        epg.setPlaybackUrl(subVal);
                                        break;
                                    case "time": {
                                        long time = 0;
                                        try {
                                            time = TIME_FORMAT.parse(subVal.replace("Z", "+0000")).getTime();
                                        } catch (ParseException e) {
                                            time = 0;
                                        }
                                        epg.setTime(time);
                                        break;
                                    }
                                }
                            }
                            jsr.endObject();
                            tmpList.add(epg);
                        }
                        jsr.endArray();
                        beans.setEpg(tmpList);
                        break;
                    case "hasPlayBack":
                        beans.setHasPlayBack(Boolean.parseBoolean(jsr.readObject().toString()));
                        break;
                    case "id":
                        beans.setId(Integer.parseInt(jsr.readObject().toString()));
                        break;
                    default:
                        jsr.readObject();
                        break;
                }
            }
            result.add(beans);
            jsr.endObject();
        }
        jsr.endArray();
        jsr.close();
        return result;
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get epg list!");

        Message msg = new Message();
        msg.what = Constant.MSG_EPG_LOADED;
        MainActivity.SendMessage(msg);
    }
}
