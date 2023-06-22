package com.brazvip.fivetv.utils;

import android.util.Base64;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public final class BaseCrypt {
    private static final String encryptionKey = "eabae9cc9b62c113f156d125539d24b2";

    public static String decrypt(String str) {
        byte[] decode = Base64.decode(str, 2);
        xorCipher(decode, encryptionKey);
        try {
            return new String(decode, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return new String(decode);
        }
    }

    public static String encrypt(String str) {
        byte[] bytes;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            bytes = str.getBytes();
        }
        xorCipher(bytes, encryptionKey);
        return new String(Base64.encode(bytes, 2));
    }

    private static byte[] xorCipher(byte[] bArr, String str) {
        int length = bArr.length;
        int length2 = str.length();
        int i = 0;
        int i2 = 0;
        while (i < length) {
            if (i2 >= length2) {
                i2 = 0;
            }
            bArr[i] = (byte) (bArr[i] ^ str.charAt(i2));
            i++;
            i2++;
        }
        return bArr;
    }
}
