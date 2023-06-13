package com.phoenix.libtv;

import go.Seq;

/* loaded from: classes.dex */
public abstract class Libtv {
    static {
        System.out.println("LibTv static init");
        try {
            System.loadLibrary("phx");
        } catch (Throwable unused) {
        }
        Seq.touch();
        _init();
    }

    private static native void _init();

    public static native boolean doLogin();

    public static native String getCacheChannels(String str);

    public static native String getCacheDashboard();

    public static native String getCacheEpg(String str);

    public static native String getCacheVod(String str);

    public static native String getCacheVodGroups();

    public static native String getCacheVodSubgroups(String str);

    public static native String getLoginData();

    public static native String getLoginPrefix();

    public static native String getServerDate();

    public static native String getUserPass(String str);

    public static native boolean profileAuth(String str, String str2);

    public static native String profileCreate(String str, String str2, boolean z, String str3);

    public static native String profileDelete(String str);

    public static native String profileUpdate(String str);

    public static native String profilesGet();

    public static native boolean setAppLicense(String str);

    public static native void setAuthData(String str, String str2);

    public static native void setConfig(String str);

    public static void touch() {
    }
}
