package com.brazvip.fivetv.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* loaded from: classes.dex */
public class SystemInfo {
    public static int SYSTEM_ARCH = -1;
    public static boolean SYSTEM_HAS_NEON = false;

    /* JADX DEBUG: Another duplicated slice has different insns count: {[INVOKE, INVOKE, INVOKE, SGET]}, finally: {[INVOKE, INVOKE, INVOKE, SGET, SPUT, IF] complete} */
    public static int getSysArmArchitecture() {
        int i = SYSTEM_ARCH;
        if (i != -1) {
            return i;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream("/proc/cpuinfo");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String[] split = readLine.split(":");
                    if (split.length == 2) {
                        String trim = split[0].trim();
                        String trim2 = split[1].trim();
                        if (trim.compareToIgnoreCase("CPU architecture") == 0) {
                            SYSTEM_ARCH = Integer.parseInt(trim2.substring(0, 1));
                        }
                        if (trim.compareToIgnoreCase("Features") == 0 && trim2.toLowerCase().contains("neon")) {
                            SYSTEM_HAS_NEON = true;
                        }
                    }
                } catch (Exception unused) {
                    bufferedReader.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                    if (SYSTEM_ARCH == -1) {
                        SYSTEM_ARCH = 6;
                    }
                    return SYSTEM_ARCH;
                } catch (Throwable th) {
                    bufferedReader.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                    if (SYSTEM_ARCH == -1) {
                        SYSTEM_ARCH = 6;
                    }
                    throw th;
                }
            }
            int i2 = SYSTEM_ARCH;
            if (i2 != -1) {
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
                if (SYSTEM_ARCH == -1) {
                    SYSTEM_ARCH = 6;
                }
                return i2;
            }
            SYSTEM_ARCH = 6;
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            if (SYSTEM_ARCH == -1) {
                SYSTEM_ARCH = 6;
            }
            return 6;
        } catch (Exception e) {
            e.printStackTrace();
            SYSTEM_ARCH = 6;
        }

        return SYSTEM_ARCH;
    }
}
