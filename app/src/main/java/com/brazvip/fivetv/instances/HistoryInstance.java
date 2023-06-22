package com.brazvip.fivetv.instances;

import android.content.Context;
import com.lzy.okgo.model.Priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.utils.CustomQueue;

/* loaded from: classes.dex */
public class HistoryInstance {
    public static final String TAG = "HistoryInstance";
    public static CustomQueue<HistoryBean> liveHistory;
    public static CustomQueue<HistoryBean> vodHistory;

    public HistoryInstance(Context context) throws IOException {
        Object savedObject = MainActivity.cacheManager.getSavedObject("liveHistory");
        liveHistory = savedObject != null ? (CustomQueue) savedObject : new CustomQueue<>(Priority.UI_LOW);
        Object savedObject2 = MainActivity.cacheManager.getSavedObject("vodHistory");
        vodHistory = savedObject2 != null ? (CustomQueue) savedObject2 : new CustomQueue<>(Priority.UI_NORMAL);
    }

    public static HistoryBean GetLastHistory(String str, String str2) {
        CustomQueue<HistoryBean> customQueue = vodHistory;
        if (customQueue == null) {
            return null;
        }
        for (HistoryBean historyBean : doNotUse__1(customQueue.toArray())) {
            if (historyBean.channelId.equals(str) && historyBean.subId.equals(str2)) {
                return historyBean;
            }
        }
        return null;
    }

    private static List<HistoryBean> doNotUse__1(Object[] objArr) {
        return m1328a(objArr);
    }

    public static HistoryBean getLastHistoryEpisode(String str) {
        CustomQueue<HistoryBean> customQueue;
        int parseInt;
        HistoryBean historyBean = null;
        if (str != null && (customQueue = vodHistory) != null) {
            int i = 0;
            try {
                for (HistoryBean historyBean2 : doNotUse__1(customQueue.toArray())) {
                    if (str.equals(historyBean2.channelId) && (parseInt = Integer.parseInt(historyBean2.subId)) > i) {
                        historyBean = historyBean2;
                        i = parseInt;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return historyBean;
    }

    private static <T> List<T> m1328a(Object[] objArr) {
        List<T> asList = (List<T>) Arrays.asList(objArr);
        Collections.reverse(asList);
        return asList;
    }

    public static void addLiveHistory(HistoryBean historyBean) {
        liveHistory.offer(historyBean);
        MainActivity.cacheManager.saveObject("liveHistory", liveHistory, 315360000);
    }

    public static void addVodHistory(HistoryBean historyBean) {
        ArrayList arrayList = new ArrayList();
        Iterator<HistoryBean> it = vodHistory.iterator();
        while (it.hasNext()) {
            HistoryBean next = it.next();
            if (next.subId.equals(historyBean.subId)) {
                arrayList.add(next);
            }
        }
        vodHistory.removeAll(arrayList);
        vodHistory.offer(historyBean);
        MainActivity.cacheManager.saveObject("vodHistory", vodHistory, 315360000);
    }

    public List<HistoryBean> getLiveHistory() {
        return m1328a(liveHistory.toArray());
    }

    public List<HistoryBean> getVodHistory() {
        return m1328a(vodHistory.toArray());
    }

    public static void saveUpdate() {
        MainActivity.cacheManager.saveObject("liveHistory", liveHistory, 315360000);
        MainActivity.cacheManager.saveObject("vodHistory", vodHistory, 315360000);
    }
}
