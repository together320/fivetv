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
    public static CustomQueue<HistoryBean> playbackHistory;
    public static CustomQueue<HistoryBean> vodHistory;

    public static int SAVE_ID = 315360000;

    public HistoryInstance(Context context) {
        try {
            liveHistory = (CustomQueue)MainActivity.cacheManager.getSavedObject("liveHistory");
            if (liveHistory == null)
                liveHistory = new CustomQueue<>(Priority.UI_LOW);

            playbackHistory = (CustomQueue)MainActivity.cacheManager.getSavedObject("playbackHistory");
            if (playbackHistory == null)
                playbackHistory = new CustomQueue<>(Priority.UI_NORMAL);

            vodHistory = (CustomQueue)MainActivity.cacheManager.getSavedObject("vodHistory");
            if (vodHistory == null)
                vodHistory = new CustomQueue<>(Priority.UI_NORMAL);
        } catch (IOException ex) {
        }
    }

    public static HistoryBean GetLastHistory(String str, String str2) {
        for (HistoryBean historyBean : getList(playbackHistory.toArray())) {
            if (historyBean.channelId.equals(str) && historyBean.subId.equals(str2)) {
                return historyBean;
            }
        }
        for (HistoryBean historyBean : getList(vodHistory.toArray())) {
            if (historyBean.channelId.equals(str) && historyBean.subId.equals(str2)) {
                return historyBean;
            }
        }
        return null;
    }

    private static List<HistoryBean> getList(Object[] objArr) {
        return doReverse(objArr);
    }

    public static HistoryBean getLastHistoryEpisode(String str) {
        CustomQueue<HistoryBean> customQueue;
        HistoryBean historyBean = null;
        if (str != null) {
            try {
                int i = 0;
                int parseInt;
                for (HistoryBean history : getList(playbackHistory.toArray())) {
                    if (str.equals(history.channelId) && (parseInt = Integer.parseInt(history.subId)) > i) {
                        historyBean = history;
                        i = parseInt;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (historyBean == null) {
                try {
                    int i = 0;
                    int parseInt;
                    for (HistoryBean history : getList(vodHistory.toArray())) {
                        if (str.equals(history.channelId) && (parseInt = Integer.parseInt(history.subId)) > i) {
                            historyBean = history;
                            i = parseInt;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return historyBean;
    }

    private static <T> List<T> doReverse(Object[] objArr) {
        List<T> asList = (List<T>) Arrays.asList(objArr);
        Collections.reverse(asList);
        return asList;
    }

    public static void addLiveHistory(HistoryBean historyBean) {
        liveHistory.offer(historyBean);
        MainActivity.cacheManager.saveObject("liveHistory", liveHistory, SAVE_ID);
    }

    public static void addPlaybackHistory(HistoryBean historyBean) {
        ArrayList arrayList = new ArrayList();
        Iterator<HistoryBean> it = playbackHistory.iterator();
        while (it.hasNext()) {
            HistoryBean next = it.next();
            if (next.subId.equals(historyBean.subId)) {
                arrayList.add(next);
            }
        }
        playbackHistory.removeAll(arrayList);
        playbackHistory.offer(historyBean);
        MainActivity.cacheManager.saveObject("playbackHistory", playbackHistory, SAVE_ID);
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
        MainActivity.cacheManager.saveObject("vodHistory", vodHistory, SAVE_ID);
    }

    public List<HistoryBean> getLiveHistory() {
        return doReverse(liveHistory.toArray());
    }

    public List<HistoryBean> getPlaybackHistory() {
        return doReverse(playbackHistory.toArray());
    }

    public List<HistoryBean> getVodHistory() {
        return doReverse(vodHistory.toArray());
    }

    public static void saveUpdate() {
        MainActivity.cacheManager.saveObject("liveHistory", liveHistory, SAVE_ID);
        MainActivity.cacheManager.saveObject("playbackHistory", playbackHistory, SAVE_ID);
        MainActivity.cacheManager.saveObject("vodHistory", vodHistory, SAVE_ID);
    }
}
