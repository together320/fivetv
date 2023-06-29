package com.brazvip.fivetv;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import com.brazvip.fivetv.utils.SystemInfo;
import com.brazvip.fivetv.utils.Utils;

public class BSCF {
    public static final String TAG = "BSCf";
    public static String appName = null;
    public static int appVersionCode = 1;
    public static String appVersionName = "";
    public static float density = 0.0f;
    public static int height = 0;
    public static String macAddress = "";
    public static int maxCF = 1500000;
    public static String packageName = "com.brazvip.fivetv";
    public static int sysArmArchitecture = -1;
    public static boolean sysHasNeon = false;
    public static int sysVerCode = 0;
    public static String sysVersion = "";
    public static String userAgent;
    public static int width;
    public Context context;
    public static Config.VType videoType = Config.VType.TS;
    public static String language = "";
    public static String countryCode = "";

    public BSCF(Context context) {
        StringBuilder sb;
        int i;
        Config.VType vType;
        this.context = context;
        Locale locale = Build.VERSION.SDK_INT >= 24 ? LocaleList.getDefault().get(0) : Locale.getDefault();
        language = locale.getLanguage();
        countryCode = locale.getCountry();
        packageName = this.context.getPackageName();
        try {
            appVersionName = this.context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            appVersionCode = this.context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        String string = this.context.getString(R.string.app_name);
        appName = string;
        String str3 = Build.VERSION.RELEASE;
        userAgent = String.format("Apache-HttpClient/Null (%s %s; Android %s; %s)", string, appVersionName, str3, Build.MODEL);
        DisplayMetrics displayMetrics = Config.displayMetrics;
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        density = displayMetrics.density;
        SystemInfo.getSysArmArchitecture();
        sysArmArchitecture = SystemInfo.getSysArmArchitecture();
        sysHasNeon = SystemInfo.SYSTEM_HAS_NEON;
        sysVersion = str3;
        sysVerCode = Build.VERSION.SDK_INT;
        try {
            InputStream inputStream = new ProcessBuilder("/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").start().getInputStream();
            sb = new StringBuilder();
            byte[] bArr = new byte[24];
            while (inputStream.read(bArr) != -1) {
                sb.append(new String(bArr));
            }
            inputStream.close();
        } catch (IOException e3) {
            e3.printStackTrace();
            sb = new StringBuilder("N/A");
        }
        try {
            i = Integer.parseInt(sb.toString().trim());
        } catch (NumberFormatException unused) {
            i = 0;
        }
        if (i > 0) {
            maxCF = Runtime.getRuntime().availableProcessors() * i;
        } else {
            maxCF = Runtime.getRuntime().availableProcessors() * maxCF;
        }
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        String macAddr = (connectionInfo == null || connectionInfo.getMacAddress() == null) ? Utils.getMacAddr() : connectionInfo.getMacAddress();
        if (!macAddr.equals("") && macAddr.length() == 17) {
            String str4 = macAddr.substring(0, 2) + macAddr.substring(3, 5) + macAddr.substring(6, 8) + macAddr.substring(9, 11) + macAddr.substring(12, 14) + macAddr.substring(15, 17);
            macAddress = str4;
            macAddress = str4.toUpperCase();
        }
        String str5 = Build.MANUFACTURER;
        int intValue = Utils.getIntegerValue(Config.VOD_MEDIA_TYPE, -1).intValue();
        if ((str5.equalsIgnoreCase("huawei") || str5.equalsIgnoreCase("google")) && intValue == -1) {
            Utils.setIntegerValue(Config.VOD_MEDIA_TYPE, 1);
        }
        int intValue2 = Utils.getIntegerValue(Config.VOD_MEDIA_TYPE, -1).intValue();
        if (intValue2 == 1) {
            vType = Config.VType.TS;
        } else if (intValue2 != 0) {
            return;
        } else {
            vType = Config.VType.M3U8;
        }
        videoType = vType;
    }
}
