package com.brazvip.fivetv.keyboard.custom.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/* compiled from: MyApplication */
/* renamed from: e.b.a.e.d.a */
/* loaded from: classes.dex */
public class StringUtils {

    /* renamed from: a 13976 */
    public static final String PERIOD = ".";

    /* renamed from: a */
    public static boolean isEmpty(String text) {
        return text == null || TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim());
    }

    /* renamed from: b */
    public static boolean m2326b(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return false;
        }
        return str.equalsIgnoreCase(str2);
    }

    /* renamed from: c */
    public static List<String> m2325c(String str, String str2) {
        ArrayList<String> result = new ArrayList<>();
        if (isEmpty(str)) {
            return result;
        }
        StringTokenizer tkr = new StringTokenizer(str, str2);
        while (tkr.hasMoreTokens()) {
            result.add(tkr.nextToken());
        }
        return result;
    }

    /* renamed from: a */
    public static boolean m2329a(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return false;
        }
        return str.equals(str2);
    }

    /* renamed from: b */
    public static boolean m2327b(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    /* renamed from: a */
    public static long m2331a(CharSequence charSequence) {
        double d = 0.0d;
        for (int i = 0; i < charSequence.length(); i++) {
            char charAt = charSequence.charAt(i);
            d += (charAt <= 0 || charAt >= 127) ? 1.0d : 0.5d;
        }
        return Math.round(d);
    }

    /* renamed from: b */
    public static String m2328b(CharSequence charSequence) {
        double d = 0.0d;
        for (int i = 0; i < charSequence.length() && d < 4.0d; i++) {
            char charAt = charSequence.charAt(i);
            d += (charAt <= 0 || charAt >= 127) ? 1.0d : 0.5d;
        }
        return d > 4.0d ? "..." : "";
    }
}
