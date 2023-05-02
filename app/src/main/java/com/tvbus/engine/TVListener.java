package com.tvbus.engine;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public interface TVListener {
    void onInfo(String result);

    void onInited(String result);

    void onPrepared(String result);

    void onQuit(String result);

    void onStart(String result);

    void onStop(String result);
}
