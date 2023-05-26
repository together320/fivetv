package com.brazvip.fivetv.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.Config;
import com.lzy.okgo.model.Priority;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import okhttp3.internal.ws.RealWebSocket;

public class Utils {
    public static long DELTA_TIME = 0;
    public static int backPressCount = 0;
    public static boolean doubleBackToExitPressedOnce = false;
    public static StringBuilder pattern = new StringBuilder();
    public static Formatter timeFormatter = new Formatter(pattern, Locale.getDefault());

    public static void clearAllPreferences() {
        SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit().clear().commit();
    }

    public static String formatBandwidth(long j) {
        if (j > 1048576) {
            return String.format("%.1fM/S", Float.valueOf(((float) j) / 1048576.0f));
        }
//        if (j > RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
//            return (j / RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) + "K/S";
//        }
        return j + "B/S";
    }

    public static String generateDeviceId() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        return uuid.replace("-", "").substring(0, 12).toUpperCase();
    }

    public static Boolean getBooleanValue(String str, boolean z) {
        return Boolean.valueOf(SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).getBoolean(str, z));
    }

    public static long getDayWithZeroTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static Integer getIntegerValue(String str, int i) {
        return Integer.valueOf(SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).getInt(str, i));
    }

    public static String getMacAddr() {
        try {
            StringBuilder sb = new StringBuilder((int) Priority.UI_NORMAL);
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/sys/class/net/eth0/address"));
            char[] cArr = new char[1024];
            while (true) {
                int read = bufferedReader.read(cArr);
                if (read == -1) {
                    break;
                }
                sb.append(String.valueOf(cArr, 0, read));
            }
            bufferedReader.close();
            String sb2 = sb.toString();
            return sb2.length() == 17 ? sb2.toUpperCase() : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getMobileNetworkRating(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        int subtype = activeNetworkInfo.getSubtype();
        activeNetworkInfo.getTypeName();
        switch (subtype) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 3;
            case 13:
            default:
                return (subtype == 13 || subtype > 15) ? 4 : 2;
        }
    }

    public static int getNetworkRating(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return 0;
        }
        int type = activeNetworkInfo.getType();
        if (type == 1) {
            return 1;
        }
        if (type == 9) {
            return 100;
        }
        if (type == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return 2;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return 3;
                case 13:
                    return 4;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static Set<String> getStringSet(String str, Set<String> set) {
        return SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).getStringSet(str, set);
    }

    public static String getValue(String str, String str2) {
        return SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).getString(str, str2);
    }

    public static boolean isDeviceOffline(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isAvailable();
    }

    public static boolean isSmartPhone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (telephonyManager == null || telephonyManager.getPhoneType() == 0) ? false : true;
    }

    public static String millisToHoursAndMinutes(Long l) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(l);
    }

    public static String millisToTimeString(Long l) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss", Locale.CHINESE);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return simpleDateFormat.format(l);
    }

    public static void removePrefValue(String str) {
        SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit().remove(str).commit();
    }

    public static void saveSPStringSet(String str, Set<String> set) {
        SharedPreferences.Editor edit = SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.putStringSet(str, set);
        set.toString();
        edit.commit();
    }

    public static String secondsToTimeString(int i) {
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        pattern.setLength(0);
        return (i4 > 0 ? timeFormatter.format("%d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2)) : timeFormatter.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2))).toString();
    }

    public static void setBooleanValue(String str, boolean z) {
        SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit().putBoolean(str, z).commit();
    }

    public static void setIntegerValue(String str, int i) {
        SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit().putInt(str, i).commit();
    }

    public static void setValue(String str, String str2) {
        SopApplication.getSopContext().getSharedPreferences(Config.PREFS_NAME, 0).edit().putString(str, str2).commit();
    }
}