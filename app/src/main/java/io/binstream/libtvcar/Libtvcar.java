package io.binstream.libtvcar;

import go.Seq;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public abstract class Libtvcar {

    /* compiled from: MyApplication */
    /* loaded from: classes.dex */
    private static final class proxyListener implements Seq.Proxy, Listener {
        public final int refnum;

        public proxyListener(int num) {
            this.refnum = num;
            Seq.trackGoRef(num, this);
        }

        @Override // go.Seq.GoObject
        public final int incRefnum() {
            Seq.incGoRef(this.refnum, this);
            return this.refnum;
        }

        @Override // io.binstream.libtvcar.Listener
        public native void onInfo(String result);

        @Override // io.binstream.libtvcar.Listener
        public native void onInited(String result);

        @Override // io.binstream.libtvcar.Listener
        public native void onPrepared(String result);

        @Override // io.binstream.libtvcar.Listener
        public native void onQuit(String result);

        @Override // io.binstream.libtvcar.Listener
        public native void onStart(String result);

        @Override // io.binstream.libtvcar.Listener
        public native void onStop(String result);
    }

    static {
        Seq.touch();
        _init();
    }

    public static native void _init();

    public static native boolean getDebug();

    public static native long init();

    public static native void release();

    public static native void run();

    public static native void setAuthURL(String str);

    public static native void setDebug(boolean z);

    public static native void setListener(Listener listener);

    public static native void setPassword(String str);

    public static native void setPlayPort(long j);

    public static native void setUsername(String str);

    public static native void start(String str);

    public static void touch() {
    }

    public static void stop() {
        start("stop");
    }
}
