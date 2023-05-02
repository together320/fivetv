package com.brazvip.fivetv.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* compiled from: MyApplication */
/* renamed from: e.b.a.g.n 3612 */
/* loaded from: classes.dex */
public class PrefUtils {

    /* renamed from: e */
    public static boolean m2225e(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isAvailable();
    }
}
