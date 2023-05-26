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
import java.util.UUID;
import java.util.regex.Pattern;

/* compiled from: MyApplication */
/* renamed from: e.b.a.g.n 3612 */
/* loaded from: classes.dex */
public class PrefUtils {

    /* renamed from: a */
    public static long f14035a = 0;

    /* renamed from: b */
    public static boolean f14036b = false;

    /* renamed from: c */
    public static int f14037c;

    /* renamed from: d */
    public static StringBuilder f14038d = new StringBuilder();

    /* renamed from: e */
    public static Formatter f14039e = new Formatter(f14038d, Locale.getDefault());

    public static void Toast(int i) {
        Toast(SopApplication.getAppContext().getString(i));
    }

    public static void Toast(String text) {
        Toast.makeText(SopApplication.getAppContext(), text, 2000).show();
    }

    /* renamed from: a 2246 */
    public static void setPrefLong(String key, long time) {
//        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
//        edit.putLong(key, time);
//        edit.apply(); //commit();
    }

    /* renamed from: a 2252 */
    public static boolean m2252a(int key1, int key2) {
        if (key1 == 1) {
            return true;
        }
        if (key1 == 0) {
            switch (key2) {
                case 1: case 2:
                    break;
                case 3:
                    return true;
                case 4:
                    return false;
                case 5: case 6:
                    return true;
                case 7:
                    return false;
                case 8: case 9: case 10:
                    return true;
                case 11:
                    return false;
                case 12: case 13: case 14: case 15:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /* renamed from: b */
    public static void setPrefInt(String key, int value) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /* renamed from: b */
    public static void m2236b(String key1, String key2) {
    }

    /* renamed from: c 2232 */
    public static int getNetworkType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder().append("phoneNetType:");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        int networkType = tm.getNetworkType();
        sb.append(networkType);
        Log.d("Utils", sb.toString());
        if (networkType == 0 || networkType == 1 || networkType == 2 ||
                networkType == 4 || networkType == 7 || networkType == 11) {
            return 2;
        }
        if (networkType == 5 || networkType == 6 || networkType == 3 ||
                networkType == 8 || networkType == 12 || networkType == 10 ||
                networkType == 15 || networkType == 9 || networkType == 14) {
            return 3;
        }
        return (networkType == 13 || networkType > 15) ? 4 : 2;
    }

    /* renamed from: c */
    public static void m2230c(String key1, String key2) {
    }

    /* renamed from: d */
    public static void setPrefString(String key, String value) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.putString(key, value);
        edit.apply();
    }

    /* renamed from: e */
    public static boolean m2225e(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isAvailable();
    }

    /* renamed from: f */
    public static boolean m2223f(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != 0;
    }

    /* renamed from: g 2221 */
    public static void logout(Context context) {
//        if (RestApiUtils.f13727K && !m2223f(context)) {
//            LogoutConfirmDialog.LogoutConfirmKeyListener listener = new LogoutConfirmDialog.LogoutConfirmKeyListener(context);
//            listener.m2196a(context.getResources().getString(R.string.confirm_reload));
//            String noBtn  = context.getResources().getString(R.string.No);
//            String yesBtn = context.getResources().getString(R.string.Yes);
//            listener.m2191b(noBtn, new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3607i
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            listener.m2195a(yesBtn, new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3608j
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    SopCast.mMsgHandler.sendEmptyMessage(75);
//                    dialog.dismiss();
//                }
//            });
//            listener.create().show();
//            return;
//        }
//        //String log = "showQuitDialog doubleBackToExitPressedOnce: " + f14036b + " SopCast.isPlaying(): " + SopCast.isPlaying() + " SopCast.isMenuDisplayed: " + SopCast.f16805n;
//        if (f14036b) {
//            LogoutConfirmDialog.LogoutConfirmKeyListener listener = new LogoutConfirmDialog.LogoutConfirmKeyListener(context);
//            listener.m2196a(context.getResources().getString(R.string.confirm_quit));
//            String later = context.getResources().getString(R.string.quit_later);
//            String now = context.getResources().getString(R.string.quit_now);
//            listener.m2191b(later, new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3609k
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            listener.m2195a(now, new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3610l
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    SopCast.mMsgHandler.sendEmptyMessage(9999);
//                    dialog.dismiss();
//                }
//            });
//            listener.create().show();
//            return;
//        }
//        if (SopCast.f16805n && !SopCast.f16785E) {
//            SopCast.mMsgHandler.sendEmptyMessage(100);
//        } else if (SopCast.f16805n && SopCast.f16785E) {
//            m2229d();
//        } else if (!SopCast.f16805n) {
//            SopCast.mMsgHandler.sendEmptyMessage(101);
//            m2229d();
//        }
//        f14036b = true;
//        new Handler().postDelayed(new Runnable() { //RunnableC3611m
//            @Override
//            public void run() {
//                PrefUtils.f14036b = false;
//            }
//        }, 1000L);
    }

    /* renamed from: h */
    public static void removePrefItem(String key) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.remove(key);
        edit.apply();
    }

    /* renamed from: f */
    public static boolean m2222f(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.compile("^[a-zA-Z]{2,4}://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z0-9/\\:]+)[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\|\\%]*").matcher(str).matches();
    }

    /* renamed from: c */
    public static boolean m2231c(String str) {
        return Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+").matcher(str).matches();
    }

    /* renamed from: e 2224 */
    public static boolean m2224e(String key) {
        int result;
        try {
            if (key != null && !key.equals("") && Pattern.compile("^[0-9]+$").matcher(key).matches()) {
                result = Integer.parseInt(key);
                return result >= 1 && result <= 1440;
            }
        } catch (Exception ignored) { }
        return false;
    }

    /* renamed from: a */
    public static Integer getPrefInt(String keyName, int defValue) {
        return Integer.valueOf(SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).getInt(keyName, defValue));
    }

    /* renamed from: b */
    public static void setPrefBool(String key, boolean value) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    /* renamed from: d 2228 */
    public static Integer getAppVersionCode(Context context) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return Integer.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            pi = null;
        }
        //String log = pi.versionName;
        return Integer.valueOf(0);
    }

    /* renamed from: c 2233 */
    public static String getEthernetMacAddress() {
        try {
            String text = readAllTextFromFile("/sys/class/net/eth0/address");
            return text.length() == 17 ? text.toUpperCase() : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: a */
    public static Boolean getPrefBool(String keyName, boolean defValue) {
        return SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).getBoolean(keyName, defValue);
    }

    /* renamed from: a */
    public static String getPrefString(String keyName, String defValue) {
        return SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).getString(keyName, defValue);
    }

    /* renamed from: b */
    public static void setPrefStringSet(String key, Set<String> value) {
        SharedPreferences.Editor edit = SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.putStringSet(key, value);
        //String m9114a = "#########" + key + value.toString();
        edit.apply();
    }

    /* renamed from: a */
    public static Set<String> getPrefStringSet(String key, Set<String> value) {
        return SopApplication.getAppContext().getSharedPreferences(Config.PREFS_NAME, 0).getStringSet(key, value);
    }

    /* renamed from: d */
    public static void m2229d() {
        if (f14037c < RestApiUtils.f13722F) {
            f14037c++;
            //SopCast.showMessageFromResource(R.string.Back_twice_to_exit);
        }
    }

    /* renamed from: a */
    public static String m2249a(Long l) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(l);
    }

    /* renamed from: b */
    public static boolean m2238b(String text) {
        if (text == null || text.equals("")) {
            return false;
        }
        return Pattern.compile("^[0-9]{2,4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$").matcher(text).matches();
    }

    /* renamed from: d */
    public static boolean m2227d(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.compile("^[0-9]+$").matcher(str).matches();
    }

    /* renamed from: a 2254 */
    public static void clearPreferences() {
        SharedPreferences.Editor edit = SopApplication.getAppContext()
                .getSharedPreferences(Config.PREFS_NAME, 0).edit();
        edit.clear();
        edit.apply();
    }

    /* renamed from: b 2240 */
    public static long getDateOfTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);  //11
        cal.set(Calendar.MINUTE, 0);       //12
        cal.set(Calendar.SECOND, 0);       //13
        cal.set(Calendar.MILLISECOND, 0);  //14
        return cal.getTimeInMillis();
    }

    /* renamed from: a */
    public static List<String> m2242a(List<String> list) {
        ArrayList needRemoveBlocks = new ArrayList();
        for (String str : list) {
            if (!needRemoveBlocks.contains(str)) {
                needRemoveBlocks.add(str);
            }
        }
        return needRemoveBlocks;
    }

    /* renamed from: a */
    public static boolean m2248a(String str) {
        return str == null || str.equals("");
    }

    /* renamed from: a 2250 */
    public static int getTelephonyNetworkType(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
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

    /* renamed from: b */
    public static String m2241b() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        return uuid.replace("-", "").substring(0, 12).toUpperCase();
    }

    /* renamed from: a */
    public static String m2253a(int secs) {
        int sc = secs % 60;
        int mn = (secs / 60) % 60;
        int hr = secs / 3600;
        f14038d.setLength(0);
        return hr > 0 ? f14039e.format("%d:%02d:%02d", hr, mn, sc).toString() : f14039e.format("%02d:%02d", mn, sc).toString();
    }

    /* renamed from: g 2220 */
    public static String readAllTextFromFile(String path) throws IOException {
        StringBuilder buffer = new StringBuilder(1000); //StringBuffer
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        char[] cArr = new char[1024];
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read != -1) {
                buffer.append(String.valueOf(cArr, 0, read));
            } else {
                bufferedReader.close();
                return buffer.toString();
            }
        }
    }

    /* renamed from: b 2239 */
    public static int getShortNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            int type = info.getType();
            if (type == 1) {
                return 1;
            }
            if (type == 9) {
                return 100;
            }
            if (type == 0) {
                return getTelephonyNetworkType(context);
            }
        }
        return 0;
    }

    /* renamed from: a */
    public static String m2251a(long speed) {
        if (speed > 1048576) {
            return String.format("%.1fM/S", ((float) speed) / 1048576.0f);
        }
        if (speed > 1024) {
            return (speed / 1024) + "K/S";
        }
        return speed + "B/S";
    }

    public static Looper getLooper() {
        Looper myLooper = Looper.myLooper();
        return myLooper != null ? myLooper : Looper.getMainLooper();
    }
}
