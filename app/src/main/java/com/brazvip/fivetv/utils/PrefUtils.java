package com.brazvip.fivetv.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.SopApplication;

/* compiled from: MyApplication */
/* renamed from: e.b.a.g.n 3612 */
/* loaded from: classes.dex */
public class PrefUtils {

    /* renamed from: e */
    public static boolean m2225e(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isAvailable();
    }

    public static String getPrefString(String keyName, String defValue) {
        return SopApplication.getAppContext().getSharedPreferences(Constant.PREFS_NAME, 0).getString(keyName, defValue);
    }

    public static void setPrefString(String key, String value) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Constant.PREFS_NAME, 0).edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void Toast(String text) {
        Toast.makeText(SopApplication.getAppContext(), text, 2000).show();
    }
}
