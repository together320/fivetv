package com.brazvip.fivetv.keyboard.custom.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* compiled from: MyApplication */
/* renamed from: e.b.a.e.d.c */
/* loaded from: classes.dex */
public class DeviceUtils {
    /* renamed from: a */
    public static void m2323a(Activity activity, String str) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + str));
        activity.startActivity(intent);
    }

    /* renamed from: b */
    public static void m2317b(Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();
        Uri uriFor = Settings.System.getUriFor("screen_brightness");
        int getScreenBrightness = getScreenBrightness(activity);
        int i = getScreenBrightness <= 225 ? 30 + getScreenBrightness : 30;
        PrintStream printStream = System.out;
        printStream.println("nowScreenBri==" + i);
        Settings.System.putInt(contentResolver, "screen_brightness", i);
        contentResolver.notifyChange(uriFor, null);
    }

    /* renamed from: c */
    public static void m2315c(Activity activity) {
        activity.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { //View$OnTouchListenerC3580b(activity)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    } else {
                        imm.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0); //16908290
                    }
                    return false;
                }
            }
        );
    }

    /* renamed from: d */
    public static String m2313d(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = tm != null ? tm.getSimOperator() : null;
        if (simOperator == null) {
            return null;
        }
        char c = 65535;
        int hashCode = simOperator.hashCode();
        if (hashCode != 49679477) {
            switch (hashCode) {
                case 49679470:
                    if (simOperator.equals("46000")) {
                        c = 0;
                        break;
                    }
                    break;
                case 49679471:
                    if (simOperator.equals("46001")) {
                        c = 3;
                        break;
                    }
                    break;
                case 49679472:
                    if (simOperator.equals("46002")) {
                        c = 1;
                        break;
                    }
                    break;
                case 49679473:
                    if (simOperator.equals("46003")) {
                        c = 4;
                        break;
                    }
                    break;
            }
        } else if (simOperator.equals("46007")) {
            c = 2;
        }
        return (c == 0 || c == 1 || c == 2) ? "中国移动" : c != 3 ? c != 4 ? simOperator : "中国电信" : "中国联通";
    }

    /* renamed from: e */
    public static String m2312e(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSimOperatorName();
        }
        return null;
    }

    /* renamed from: f */
    public static void m2311f(Context context) {
        ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).adjustStreamVolume(1, 1, 1);
    }

    /* renamed from: g */
    public static boolean m2310g(Context context) {
        int status = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("status", -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL; //2, 5
    }

    /* renamed from: h */
    public static boolean supportedGPS(Context context) {
        for (String provider : ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).getProviders(true)) {
            if ("gps".equals(provider)) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: i */
    public static boolean isTelephonySupported(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() != 0;
    }

    /* renamed from: j */
    public static void m2307j(Context context) {
        context.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    /* renamed from: k */
    @SuppressLint("UnspecifiedImmutableFlag")
    public static void showWidgetSettings(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        intent.addCategory("android.intent.category.ALTERNATIVE");
        intent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, intent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"HardwareIds"})
    /* renamed from: c */
    public static String getSIMCardId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSubscriberId();
        }
        return null;
    }

    /* renamed from: a */
    public static void dialPhone(Context context, String phone) {
        context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone)));
    }

    /* renamed from: a */
    public static void sendSMS(Context context, String message, String target) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto://" + target));
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }

    /* renamed from: b */
    public static String getAndroidDeviceId(Context context) {
        if (!isTelephonySupported(context)) {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /* renamed from: a */
    public static void showInputForEdit(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED); //2
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); //1
    }

    /* renamed from: a */
    public static void hideInputForEdit(EditText editText) {
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED); //2
    }

    /* renamed from: a */
    public static int getScreenBrightness(Activity activity) {
        try {
            return Settings.System.getInt(activity.getContentResolver(), "screen_brightness");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* renamed from: a */
    public static List<HashMap<String, String>> getContactsList(Context context) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri rawUrl = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUrl = Uri.parse("content://com.android.contacts/data");
        Cursor qid = contentResolver.query(rawUrl, new String[]{"contact_id"}, null, null, null);
        while (qid.moveToNext()) {
            String string = qid.getString(0);
            if (!TextUtils.isEmpty(string)) {
                Cursor qdt = contentResolver.query(dataUrl, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{string}, null);
                HashMap<String, String> map = new HashMap<>();
                while (qdt.moveToNext()) {
                    String data = qdt.getString(0);
                    String type = qdt.getString(1);
                    if (type.equals("vnd.android.cursor.item/phone_v2")) {
                        map.put("phone", data);
                    } else if (type.equals("vnd.android.cursor.item/name")) {
                        map.put("name", data);
                    }
                }
                result.add(map);
                qdt.close();
            }
        }
        qid.close();
        return result;
    }
}
