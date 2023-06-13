package com.brazvip.fivetv.cache;

import android.content.Context;
import android.os.Process;
import com.lzy.okgo.model.Priority;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import com.brazvip.fivetv.MainActivity;

/* loaded from: classes.dex */
public class CacheManager {
    public static Map<String, CacheManager> cacheManagerMap = new HashMap();
    public String TAG = "CacheManager";
    public C2304a f8488c;

    /* loaded from: classes.dex */
    public class C2304a {
        public final long f8491c;
        public final int f8492d;
        public File f8494f;
        public final Map<File, Long> f8493e = Collections.synchronizedMap(new HashMap());
        public final AtomicLong f8489a = new AtomicLong();
        public final AtomicInteger f8490b = new AtomicInteger();

        public C2304a(CacheManager cacheManager, File file, long j, int i) {
            this.f8494f = file;
            this.f8491c = j;
            this.f8492d = i;
            new Thread(new Runnable() { // from class: com.brazvip.fivetv.cache.CacheManager.C2304a.1
                @Override // java.lang.Runnable
                public void run() {
                    File[] listFiles = C2304a.this.f8494f.listFiles();
                    if (listFiles != null) {
                        int i2 = 0;
                        int i3 = 0;
                        for (File file2 : listFiles) {
                            i2 = (int) (C2304a.this.getFileSize(file2) + i2);
                            i3++;
                            C2304a.this.f8493e.put(file2, Long.valueOf(file2.lastModified()));
                        }
                        C2304a.this.f8489a.set(i2);
                        C2304a.this.f8490b.set(i3);
                    }
                }
            }).start();
        }

        public final long getFileSize(File file) {
            if (file == null) {
                return 0L;
            }
            return file.length();
        }

        public final File m1435b(String str) {
            File file = this.f8494f;
            return new File(file, str.hashCode() + "");
        }

        public final void m1436b(File file) {
            if (file == null) {
                return;
            }
            int i = this.f8490b.get();
            while (i + 1 > this.f8492d) {
                this.f8489a.addAndGet(-m1440a());
                i = this.f8490b.addAndGet(-1);
            }
            this.f8490b.addAndGet(1);
            long fileSize = getFileSize(file);
            long j = this.f8489a.get();
            while (true) {
                int i2 = ((j + fileSize) > this.f8491c ? 1 : ((j + fileSize) == this.f8491c ? 0 : -1));
                AtomicLong atomicLong = this.f8489a;
                if (i2 <= 0) {
                    atomicLong.addAndGet(fileSize);
                    long currentTimeMillis = System.currentTimeMillis();
                    file.setLastModified(currentTimeMillis);
                    this.f8493e.put(file, Long.valueOf(currentTimeMillis));
                    return;
                }
                j = atomicLong.addAndGet(-m1440a());
            }
        }

        public final File m1437a(String str) {
            File m1435b = m1435b(str);
            long currentTimeMillis = System.currentTimeMillis();
            m1435b.setLastModified(currentTimeMillis);
            this.f8493e.put(m1435b, Long.valueOf(currentTimeMillis));
            return m1435b;
        }

        public final long m1440a() {
            File file;
            if (this.f8493e.isEmpty()) {
                return 0L;
            }
            Set<Map.Entry<File, Long>> entrySet = this.f8493e.entrySet();
            synchronized (this.f8493e) {
                file = null;
                Long l = null;
                for (Map.Entry<File, Long> entry : entrySet) {
                    if (file == null) {
                        file = entry.getKey();
                        l = entry.getValue();
                    } else {
                        Long value = entry.getValue();
                        if (value.longValue() < l.longValue()) {
                            file = entry.getKey();
                            l = value;
                        }
                    }
                }
            }
            long fileSize = getFileSize(file);
            if (file != null && file.delete()) {
                this.f8493e.remove(file);
            }
            return fileSize;
        }
    }

    public CacheManager(File file, long j, int i) {
        if (!file.exists() && !file.mkdirs()) {
            StringBuilder m6212l = new StringBuilder();
            m6212l.append("can't make dirs in ");
            m6212l.append(file.getAbsolutePath());
            throw new RuntimeException(m6212l.toString());
        }
        this.f8488c = new C2304a(this, file, j, i);
    }

    public static String formatProcessID() {
        StringBuilder m6212l = new StringBuilder();
        m6212l.append("_");
        m6212l.append(Process.myPid());
        return m6212l.toString();
    }

    public static CacheManager getCacheManager(Context context) {
        File[] fileArr = {new File(context.getCacheDir(), "ACache"), new File(context.getFilesDir(), "ACache"), new File(context.getExternalCacheDir(), "ACache"), new File(context.getExternalFilesDir(null), "ACache")};
        for (int i = 0; i < 4; i++) {
            File file = fileArr[i];
            Map<String, CacheManager> map = cacheManagerMap;
            CacheManager cacheManager = map.get(file.getAbsoluteFile() + formatProcessID());
            if (cacheManager != null) {
                return cacheManager;
            }
        }
        for (int i2 = 0; i2 < 4; i2++) {
            File file2 = fileArr[i2];
            if (file2.exists() || file2.mkdirs()) {
                Map<String, CacheManager> map2 = cacheManagerMap;
                CacheManager cacheManager2 = map2.get(file2.getAbsoluteFile() + formatProcessID());
                if (cacheManager2 == null) {
                    CacheManager cacheManager3 = new CacheManager(file2, 100000000L, Priority.UI_TOP);
                    Map<String, CacheManager> map3 = cacheManagerMap;
                    map3.put(file2.getAbsolutePath() + formatProcessID(), cacheManager3);
                    return cacheManager3;
                }
                return cacheManager2;
            }
        }
        throw new RuntimeException("can't make dirs in all the application directories");
    }

    public static boolean m5817b(byte[] bArr) {
        String[] strArr = m5838a(bArr) ? new String[]{new String(m5835a(bArr, 0, 13)), new String(m5835a(bArr, 14, m5837a(bArr, ' ')))} : null;
        if (strArr != null && strArr.length == 2) {
            String str = strArr[0];
            while (str.startsWith("0")) {
                str = str.substring(1);
            }
            if (System.currentTimeMillis() > Long.parseLong(str) + (Long.parseLong(strArr[1]) * 1000)) {
                return true;
            }
        }
        return false;
    }

    public static String m5830b(int i) {
        StringBuilder sb = new StringBuilder(System.currentTimeMillis() + "");
        while (sb.length() < 13) {
            sb.insert(0, "0 ");
        }
        return ((Object) sb) + "-" + i + ' ';
    }

    public static byte[] m5835a(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i, bArr2, 0, Math.min(bArr.length - i, i3));
            return bArr2;
        }
        throw new IllegalArgumentException(i + " >" + i2);
    }

    public static int m5837a(byte[] bArr, char c) {
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static boolean m5838a(byte[] bArr) {
        return bArr != null && bArr.length > 15 && bArr[13] == 45 && m5837a(bArr, ' ') > 14;
    }

    public void clearCache() {
        C2304a c2304a = this.f8488c;
        c2304a.f8493e.clear();
        c2304a.f8489a.set(0L);
        File[] listFiles = c2304a.f8494f.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                file.delete();
            }
        }
    }

    public Object getSavedObject(String name) throws IOException {
        byte[] randomBuffer = null;
        RandomAccessFile randomFile = null;
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        Object returnObject = null;

        try {
            File file = this.f8488c.m1437a((String) name);
            if (!file.exists())
                return null;
            randomFile = new RandomAccessFile(file, "r");

            int n = (int) randomFile.length();
            randomBuffer = new byte[n];
            randomFile.read(randomBuffer);
            if (CacheManager.m5817b(randomBuffer)) {
                randomFile.close();
                return null;
            }

            byte[] buffer = randomBuffer;
            if (CacheManager.m5838a(randomBuffer)) {
                buffer = CacheManager.m5835a(randomBuffer, CacheManager.m5837a(randomBuffer, ' ') + 1, n);
            }
            inputStream = new ByteArrayInputStream(buffer);
            objectInputStream = new ObjectInputStream(inputStream);

            returnObject = objectInputStream.readObject();

            if (randomFile != null)
                randomFile.close();
            if (inputStream != null)
                inputStream.close();
            if (objectInputStream != null)
                objectInputStream.close();
        }
        catch (IOException | ClassNotFoundException ex) {
            if (randomFile != null)
                randomFile.close();
            if (inputStream != null)
                inputStream.close();
            if (objectInputStream != null)
                objectInputStream.close();
            return null;
        }

        return returnObject;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [boolean] */
    public String m1441b(String str) {
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2 = null;
        if (MainActivity.logMemoryStats() || MainActivity.isSystemOnLowMemory()) {
            return null;
        }
        File m1437a = this.f8488c.m1437a(str);
        boolean exists = m1437a.exists();
        try {
            if (exists == false) {
                return null;
            }
            try {
                bufferedReader = new BufferedReader(new FileReader(m1437a));
            } catch (IOException e) {
                e = e;
                bufferedReader = null;
            } catch (Throwable th) {
                th = th;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                }
                String sb2 = sb.toString();
                if (m5817b(sb2.getBytes())) {
                    this.f8488c.m1437a(str).delete();
                    try {
                        bufferedReader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return null;
                }
                if (m5838a(sb2.getBytes())) {
                    sb2 = sb2.substring(sb2.indexOf(32) + 1);
                }
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                return sb2;
            } catch (IOException e5) {
                e5.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                return null;
            }
        } catch (Throwable th2) {
            bufferedReader2 = null;
        }

        return null;
    }

    public void m1443a(String str, byte[] bArr, int i) {
        byte[] bytes = m5830b(i).getBytes();
        byte[] bArr2 = new byte[bytes.length + bArr.length];
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        System.arraycopy(bArr, 0, bArr2, bytes.length, bArr.length);
        m1444a(str, bArr2);
    }

    public void m1444a(String str, byte[] bArr) {
        File m1435b = this.f8488c.m1435b(str);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(m1435b);
                try {
                    fileOutputStream2.write(bArr);
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e2) {
                            e = e2;
                            e.printStackTrace();
                            this.f8488c.m1436b(m1435b);
                        }
                    }
                    this.f8488c.m1436b(m1435b);
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    this.f8488c.m1436b(m1435b);
                    throw th;
                }
                try {
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    this.f8488c.m1436b(m1435b);
                }
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            this.f8488c.m1436b(m1435b);
        } catch (Throwable th2) {

        }
    }

    public void m1445a(final String str, String str2, int i) {
        final String str3 = m5830b(i) + str2;
        if (MainActivity.logMemoryStats() || MainActivity.isSystemOnLowMemory()) {
            return;
        }
        new Thread() { // from class: com.brazvip.fivetv.cache.CacheManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                BufferedWriter bufferedWriter = null;
                IOException e;
                File m1435b = CacheManager.this.f8488c.m1435b(str);
                BufferedWriter bufferedWriter2 = null;
                try {
                    try {
                        try {
                            bufferedWriter = new BufferedWriter(new FileWriter(m1435b), 1024);
                        } catch (IOException e2) {
                            bufferedWriter = null;
                            e = e2;
                        } catch (Throwable th) {
                            th = th;
                            if (bufferedWriter2 != null) {
                                try {
                                    bufferedWriter2.flush();
                                    bufferedWriter2.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            throw th;
                        }
                        try {
                            bufferedWriter.write(str3);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e4) {
                            e = e4;
                            e.printStackTrace();
                            if (bufferedWriter != null) {
                                bufferedWriter.flush();
                                bufferedWriter.close();
                            }
                            CacheManager.this.f8488c.m1436b(m1435b);
                        }
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                    CacheManager.this.f8488c.m1436b(m1435b);
                } catch (Throwable th2) {
                    bufferedWriter2 = bufferedWriter;
                }
            }
        }.start();
    }

    public void saveObject(String str, Serializable serializable, int i) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectOutputStream objectOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        } catch (IOException unused) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            objectOutputStream.writeObject(serializable);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (i != -1) {
                m1443a(str, byteArray, i);
            } else {
                m1444a(str, byteArray);
            }
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException unused2) {
            objectOutputStream2 = objectOutputStream;
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Throwable th2) {
            objectOutputStream2 = objectOutputStream;
            if (objectOutputStream2 != null) {
                try {
                    objectOutputStream2.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th2;
        }
    }
}
