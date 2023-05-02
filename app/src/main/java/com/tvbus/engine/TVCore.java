package com.tvbus.engine;

import android.content.Context;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public class TVCore {
    public static final int RM_CLIENT = 2;
    public static final int RM_MASTER = 1;
    public static final int RM_STANDALONE = 0;
    public static TVCore inst;
    public static long nativeHandle;
    public TVListener tvListener = null;
    public String mkBroker = null;

    static {
        try {
            System.loadLibrary("tvcore");
        } catch (Throwable ignored) {
        }
    }

    public static synchronized TVCore getInstance() {
        synchronized (TVCore.class) {
            if (inst == null) {
                inst = new TVCore();
                try {
                    nativeHandle = inst.initialise();
                    if (nativeHandle == 0) {
                        return null;
                    }
                } catch (Throwable unused) {
                    return null;
                }
            }
            return inst;
        }
    }


    private native String description(long handle);

    private native void diagnose(long handle);

    private native String err2String(long handle, int errorCode);

    private native String getVersion(long handle);

    private native int init(long handle, Context context);

    private native long initialise();
    private native int run(long handle);

    private native void quit(long handle);

    private native void setAuthItems(long handle, String userId, String peerId, String sessionKey);

    private native void setAuthUrl(long handle, String url);

    private native void setDomainSuffix(long handle, String suffix);

    private native void setListener(long handle, TVListener listener);

    private native void setMKBroker(long handle, String broker);

    private native void setOption(long handle, String key, String value);

    private native void setPassword(long handle, String password);

    private native void setPlayPort(long handle, int port);

    private native void setRunningMode(long handle, int mode);

    private native void setServPort(long handle, int port);

    private native void setUsername(long handle, String name);

    private native void start(long handle, String url);

    private native void stop(long handle);

    private native void stop2(long handle, int code);



    public String description() {
        try {
            return description(nativeHandle);
        } catch (Throwable unused) {
            return "";
        }
    }

    public void diagnose() {
        try {
            diagnose(nativeHandle);
        } catch (Throwable ignored) {
        }
    }

    public String err2String(int errorCode) {
        try {
            return err2String(nativeHandle, errorCode);
        } catch (Throwable unused) {
            return "";
        }
    }

    public String getVersion() {
        try {
            return getVersion(nativeHandle);
        } catch (Throwable unused) {
            return "";
        }
    }

    public int init(Context context) {
        try {
            return init(nativeHandle, context);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public void quit() {
        try {
            quit(nativeHandle);
        } catch (Throwable ignored) {
        }
    }

    public int run() {
        try {
            return run(nativeHandle);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public void setAuthItems(String userId, String peerId, String sessionKey) {
        try {
            setAuthItems(nativeHandle, userId, peerId, sessionKey);
        } catch (Throwable ignored) {
        }
    }

    public void setAuthUrl(String url) {
        try {
            setAuthUrl(nativeHandle, url);
        } catch (Throwable ignored) {
        }
    }

    public void setDomainSuffix(String suffix) {
        try {
            setDomainSuffix(nativeHandle, suffix);
        } catch (Throwable ignored) {
        }
    }

    public void setMKBroker(String name) {
        try {
            //mkBroker = name;
            setMKBroker(nativeHandle, name);
        } catch (Throwable ignored) {
        }
    }

    public void setOption(String key, String value) {
        try {
            setOption(nativeHandle, key, value);
        } catch (Throwable ignored) {
        }
    }

    public void setPassword(String password) {
        try {
            setPassword(nativeHandle, password);
        } catch (Throwable ignored) {
        }
    }

    public void setPlayPort(int port) {
        try {
            setPlayPort(nativeHandle, port);
        } catch (Throwable ignored) {
        }
    }

    public void setRunningMode(int mode) {
        try {
            setRunningMode(nativeHandle, mode);
        } catch (Throwable ignored) {
        }
    }

    public void setServPort(int port) {
        try {
            setServPort(nativeHandle, port);
        } catch (Throwable ignored) {
        }
    }

    public void setTVListener(TVListener listener) {
        this.tvListener = listener;
        try {
            setListener(nativeHandle, listener);
        } catch (Throwable ignored) {
        }
    }

    public void setUsername(String name) {
        try {
            setUsername(nativeHandle, name);
        } catch (Throwable ignored) {
        }
    }

    public void start(String url) {
        try {
            start(nativeHandle, url);
        } catch (Throwable ignored) {
        }
    }

    public void stop() {
        try {
            stop(nativeHandle);
        } catch (Throwable ignored) {
        }
    }

    public void stop(int code) {
        try {
            stop2(nativeHandle, code);
        } catch (Throwable ignored) {
        }
    }
}
