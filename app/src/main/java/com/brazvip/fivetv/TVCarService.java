package com.brazvip.fivetv;

import org.json.JSONException;
import org.json.JSONObject;

import io.binstream.libtvcar.Libtvcar;
import io.binstream.libtvcar.Listener;
import org.greenrobot.eventbus.EventBus;

/* compiled from: MyApplication */
/* renamed from: e.b.a.H 3448 */
/* loaded from: classes.dex */
public class TVCarService {

    /* renamed from: a 13300 */
    public static final String TAG = "TVCarService";

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$a 3449 */
    /* loaded from: classes.dex */
    public static class InfoEvent {

        /* renamed from: a 13301 */
        public int errno = -1000;

        /* renamed from: b 13302 */
        public int download_rate;

        /* renamed from: c 13303 */
        public int upload_rate;

        /* renamed from: d 13304 */
        public int download_total;

        /* renamed from: e 13305 */
        public int upload_total;

        public InfoEvent(String info) {
            JSONObject obj;
            try {
                obj = new JSONObject(info);
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj == null) {
                return;
            }
            download_rate   = obj.optInt("download_rate");
            upload_rate     = obj.optInt("upload_rate");
            download_total  = obj.optInt("download_total");
            upload_total    = obj.optInt("upload_total");
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$b 3450 */
    /* loaded from: classes.dex */
    public static class InitedEvent {

        /* renamed from: a 13306 */
        public int errno;

        public InitedEvent(String key) {
            errno = -1000;

            JSONObject obj;
            try {
                obj = new JSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj == null) {
                return;
            }
            errno = obj.optInt("errno", -1000);
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$c 3451 */
    /* loaded from: classes.dex */
    public static class QuitEvent {

        /* renamed from: a 13307 */
        public int errno;

        public QuitEvent(String key) {
            errno = -1000;

            JSONObject jSONObject;
            try {
                jSONObject = new JSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                jSONObject = null;
            }
            if (jSONObject == null) {
                return;
            }
            errno = jSONObject.optInt("errno", -1000);
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$d 3452 */
    /* loaded from: classes.dex */
    public static class StartEvent {

        /* renamed from: a 13308 */
        public int errno;

        /* renamed from: b 13309 */
        public String url;

        public StartEvent(String str) {
            errno = -1000;

            JSONObject obj;
            try {
                obj = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj == null) {
                return;
            }
            errno = obj.optInt("errno", -1000);
            if (errno != 0) {
                return;
            }
            url = obj.optString("url", "null");
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$e 3453 */
    /* loaded from: classes.dex */
    public static class StopEvent {

        /* renamed from: a 13310 */
        public int errno;

        /* renamed from: b 13311 */
        public String url;

        public StopEvent(String key) {
            errno = -1000;
            
            JSONObject obj;
            try {
                obj = new JSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj == null) {
                return;
            }
            errno = obj.optInt("errno", -1000);
            if (errno != 0) {
                return;
            }
            url = obj.optString("url", "null");
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.H$f 3454 */
    /* loaded from: classes.dex */
    public static class PreparedEvent {

        /* renamed from: a 13312 */
        public int errno;

        /* renamed from: b 13313 */
        public String url;

        public PreparedEvent(String key) {
            errno = -1000;

            JSONObject obj;
            try {
                obj = new JSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj == null) {
                return;
            }
            errno = obj.optInt("errno", -1000);
            if (errno != 0) {
                return;
            }
            url = obj.optString("url", "null");
        }
    }

    /* renamed from: a */
    public static boolean start() {
        Libtvcar.setListener(new Listener() {
            @Override // io.binstream.libtvcar.Listener
            public void onInfo(String result) {
                //String text = "onInfo:" + key;
                EventBus.getDefault().post(new TVCarService.InfoEvent(result));
            }

            @Override // io.binstream.libtvcar.Listener
            public void onInited(String result) {
                //String text = "onInited:" + result;
                EventBus.getDefault().post(new TVCarService.InitedEvent(result));
            }

            @Override // io.binstream.libtvcar.Listener
            public void onPrepared(String result) {
                //String text = "onPrepared:" + result;
                EventBus.getDefault().post(new TVCarService.PreparedEvent(result));
            }

            @Override // io.binstream.libtvcar.Listener
            public void onQuit(String result) {
                //String text = "onQuit:" + result;
                EventBus.getDefault().post(new TVCarService.QuitEvent(result));
            }

            @Override // io.binstream.libtvcar.Listener
            public void onStart(String result) {
                //String text = "onStart:" + result;
                EventBus.getDefault().post(new TVCarService.StartEvent(result));
            }

            @Override // io.binstream.libtvcar.Listener
            public void onStop(String result) {
                //String text = "onStop:" + result;
                EventBus.getDefault().post(new TVCarService.StopEvent(result));
            }
        });
        if (Libtvcar.init() != 0) {
            return false;
        }
        runService();
        return true;
    }

    /* renamed from: b 2518 */
    public static void runService() {
        new Thread(new Runnable() { //RunnableC3447G
            @Override
            public void run() {
                Libtvcar.run();
            }
        }).start();
    }
}
