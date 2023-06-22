package com.brazvip.fivetv.utils;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class StringUtil {
    public static String m6220d(String str, int i) {
        return str + i;
    }

    public static String m6219e(String str, int i, String str2, int i2) {
        return str + i + str2 + i2;
    }

    public static String m6218f(String str, long j) {
        return str + j;
    }

    public static String m6217g(String str, Fragment fragment, String str2) {
        return str + fragment + str2;
    }

    public static String m6216h(String str, String str2) {
        return str + str2;
    }

    public static String m6215i(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public static String m6214j(StringBuilder sb, int i, String str) {
        sb.append(i);
        sb.append(str);
        return sb.toString();
    }

    public static String m6213k(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static StringBuilder m6212l(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    public static StringBuilder m6211m(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        sb.append(str2);
        return sb;
    }

    public static void m6210n(int i, HashMap hashMap, String str, int i2, String str2, int i3, String str3, int i4, String str4) {
        hashMap.put(str, Integer.valueOf(i));
        hashMap.put(str2, Integer.valueOf(i2));
        hashMap.put(str3, Integer.valueOf(i3));
        hashMap.put(str4, Integer.valueOf(i4));
    }
}
