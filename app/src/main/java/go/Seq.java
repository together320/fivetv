package go;

import android.content.Context;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.logging.Logger;

/* compiled from: MyApplication */
/* renamed from: go.Seq */
/* loaded from: classes.dex */
public class Seq {
    public static final int NULL_REFNUM = 41;
    public static final RefTracker tracker;
    public static Logger log = Logger.getLogger("GoSeq");
    public static final Ref nullRef = new Ref(NULL_REFNUM, null);
    public static final GoRefQueue goRefQueue = new GoRefQueue();

    /* compiled from: MyApplication */
    /* renamed from: go.Seq$GoObject */
    /* loaded from: classes.dex */
    public interface GoObject {
        int incRefnum();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: go.Seq$GoRef */
    /* loaded from: classes.dex */
    public static class GoRef extends PhantomReference<GoObject> {
        public final int refnum;

        public GoRef(int r_num, GoObject object, GoRefQueue queue) {
            super(object, queue);
            if (r_num <= 0) {
                this.refnum = r_num;
                return;
            }
            throw new RuntimeException("GoRef instantiated with a Java r_num " + r_num);
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: go.Seq$GoRefQueue */
    /* loaded from: classes.dex */
    static class GoRefQueue extends ReferenceQueue<GoObject> {
        public final Collection<GoRef> refs = Collections.synchronizedCollection(new HashSet<GoRef>());

        public GoRefQueue() {
            Thread thread = new Thread(new Runnable() { // from class: go.Seq.GoRefQueue.1
                @Override // java.lang.Runnable
                public void run() {
                    while (true) {
                        try {
                            GoRef goRef = (GoRef) GoRefQueue.this.remove();
                            GoRefQueue.this.refs.remove(goRef);
                            Seq.destroyRef(goRef.refnum);
                            goRef.clear();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            });
            thread.setDaemon(true);
            thread.setName("GoRefQueue Finalizer Thread");
            thread.start();
        }

        public void track(int i, GoObject goObject) {
            this.refs.add(new GoRef(i, goObject, this));
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: go.Seq$Proxy */
    /* loaded from: classes.dex */
    public interface Proxy extends GoObject {
    }

    /* compiled from: MyApplication */
    /* renamed from: go.Seq$Ref */
    /* loaded from: classes.dex */
    public static final class Ref {
        public final Object obj;
        public int refcnt;
        public final int refnum;

        public Ref(int num, Object obj) {
            if (num >= 0) {
                this.refnum = num;
                this.refcnt = 0;
                this.obj = obj;
                return;
            }
            throw new RuntimeException("Ref instantiated with a Go refnum " + num);
        }

        public static /* synthetic */ int access$110(Ref ref) {
            int i = ref.refcnt;
            ref.refcnt = i - 1;
            return i;
        }

        public void inc() {
            int i = this.refcnt;
            if (i != Integer.MAX_VALUE) {
                this.refcnt = i + 1;
                return;
            }
            throw new RuntimeException("refnum " + this.refnum + " overflow");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: go.Seq$RefMap */
    /* loaded from: classes.dex */
    public static final class RefMap {
        public int next = 0;
        public int live = 0;
        public int[] keys = new int[16];
        public Ref[] objs = new Ref[16];

        private void grow() {
            Ref[] refArr;
            int roundPow2 = roundPow2(this.live) * 2;
            int[] iArr = this.keys;
            if (roundPow2 > iArr.length) {
                iArr = new int[iArr.length * 2];
                refArr = new Ref[this.objs.length * 2];
            } else {
                refArr = this.objs;
            }
            int i = 0;
            int i2 = 0;
            while (true) {
                int[] iArr2 = this.keys;
                if (i >= iArr2.length) {
                    break;
                }
                Ref[] refArr2 = this.objs;
                if (refArr2[i] != null) {
                    iArr[i2] = iArr2[i];
                    refArr[i2] = refArr2[i];
                    i2++;
                }
                i++;
            }
            for (int i3 = i2; i3 < iArr.length; i3++) {
                iArr[i3] = 0;
                refArr[i3] = null;
            }
            this.keys = iArr;
            this.objs = refArr;
            this.next = i2;
            if (this.live == this.next) {
                return;
            }
            StringBuilder m9120a = new StringBuilder().append("bad state: live=");
            m9120a.append(this.live);
            m9120a.append(", next=");
            m9120a.append(this.next);
            throw new RuntimeException(m9120a.toString());
        }

        public static int roundPow2(int i) {
            int i2 = 1;
            while (i2 < i) {
                i2 *= 2;
            }
            return i2;
        }

        public Ref get(int i) {
            int binarySearch = Arrays.binarySearch(this.keys, 0, this.next, i);
            if (binarySearch >= 0) {
                return this.objs[binarySearch];
            }
            return null;
        }

        public void put(int i, Ref ref) {
            if (ref != null) {
                int binarySearch = Arrays.binarySearch(this.keys, 0, this.next, i);
                if (binarySearch >= 0) {
                    Ref[] refArr = this.objs;
                    if (refArr[binarySearch] == null) {
                        refArr[binarySearch] = ref;
                        this.live++;
                    }
                    if (this.objs[binarySearch] == ref) {
                        return;
                    }
                    throw new RuntimeException("replacing an existing ref (with key " + i + ")");
                }
                if (this.next >= this.keys.length) {
                    grow();
                    binarySearch = Arrays.binarySearch(this.keys, 0, this.next, i);
                }
                int i2 = ~binarySearch; //binarySearch ^ (-1);
                int i3 = this.next;
                if (i2 < i3) {
                    int[] iArr = this.keys;
                    int i4 = i2 + 1;
                    System.arraycopy(iArr, i2, iArr, i4, i3 - i2);
                    Ref[] refArr2 = this.objs;
                    System.arraycopy(refArr2, i2, refArr2, i4, this.next - i2);
                }
                this.keys[i2] = i;
                this.objs[i2] = ref;
                this.live++;
                this.next++;
                return;
            }
            throw new RuntimeException("put a null ref (with key " + i + ")");
        }

        public void remove(int i) {
            int binarySearch = Arrays.binarySearch(this.keys, 0, this.next, i);
            if (binarySearch >= 0) {
                Ref[] refArr = this.objs;
                if (refArr[binarySearch] != null) {
                    refArr[binarySearch] = null;
                    this.live--;
                }
            }
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: go.Seq$RefTracker */
    /* loaded from: classes.dex */
    static final class RefTracker {
        public static final int REF_OFFSET = 42;
        public int next = 42;
        public final RefMap javaObjs = new RefMap();
        public final IdentityHashMap<Object, Integer> javaRefs = new IdentityHashMap<>();

        public synchronized void dec(int i) {
            if (i <= 0) {
                Seq.log.severe("dec request for Go object " + i);
            } else if (i == Seq.nullRef.refnum) {
            } else {
                Ref ref = this.javaObjs.get(i);
                if (ref != null) {
                    ref.refcnt--;
                    if (ref.refcnt <= 0) {
                        this.javaObjs.remove(i);
                        this.javaRefs.remove(ref.obj);
                    }
                    return;
                }
                throw new RuntimeException("referenced Java object is not found: refnum=" + i);
            }
        }

        public synchronized Ref get(int i) {
            if (i < 0) {
                throw new RuntimeException("ref called with Go refnum " + i);
            } else if (i == NULL_REFNUM) {
                return Seq.nullRef;
            } else {
                Ref ref = this.javaObjs.get(i);
                if (ref != null) {
                    return ref;
                }
                throw new RuntimeException("unknown java Ref: " + i);
            }
        }

        public synchronized int inc(Object obj) {
            if (obj == null) {
                return NULL_REFNUM;
            }
            if (obj instanceof Proxy) {
                return ((Proxy) obj).incRefnum();
            }
            Integer num = this.javaRefs.get(obj);
            if (num == null) {
                if (this.next != Integer.MAX_VALUE) {
                    int i = this.next;
                    this.next = i + 1;
                    num = Integer.valueOf(i);
                    this.javaRefs.put(obj, num);
                } else {
                    throw new RuntimeException("createRef overflow for " + obj);
                }
            }
            int intValue = num.intValue();
            Ref ref = this.javaObjs.get(intValue);
            if (ref == null) {
                ref = new Ref(intValue, obj);
                this.javaObjs.put(intValue, ref);
            }
            ref.inc();
            return intValue;
        }

        public synchronized void incRefnum(int i) {
            Ref ref = this.javaObjs.get(i);
            if (ref != null) {
                ref.inc();
            } else {
                throw new RuntimeException("referenced Java object is not found: refnum=" + i);
            }
        }
    }

    static {
        System.loadLibrary("gojni");
        init();
        Universe.touch();
        tracker = new RefTracker();
    }

    public static void decRef(int i) {
        tracker.dec(i);
    }

    public static native void destroyRef(int i);

    public static Ref getRef(int i) {
        return tracker.get(i);
    }

    public static int incGoObjectRef(GoObject goObject) {
        return goObject.incRefnum();
    }

    public static native void incGoRef(int i, GoObject goObject);

    public static int incRef(Object obj) {
        return tracker.inc(obj);
    }

    public static void incRefnum(int i) {
        tracker.incRefnum(i);
    }

    public static native void init();

    public static void setContext(Context context) {
        setContext((Object) context);
    }

    public static native void setContext(Object obj);

    public static void touch() {
    }

    public static void trackGoRef(int num, GoObject object) {
        if (num <= 0) {
            goRefQueue.track(num, object);
            return;
        }
        throw new RuntimeException("trackGoRef called with Java refnum " + num);
    }
}
