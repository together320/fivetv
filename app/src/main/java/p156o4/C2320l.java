package p156o4;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Map;
import com.brazvip.fivetv.adapters.VodGroupAdapter;
import com.brazvip.fivetv.beans.vod.VodGroupL2;
import com.brazvip.fivetv.instances.VodChannelInstance;
import p211t6.AbstractC2955c1;
import p211t6.C3018y;
//import p241w6.C3259a;

/* renamed from: o4.l */
/* loaded from: classes.dex */
public final /* synthetic */ class C2320l implements Comparator {

    /* renamed from: r */
    public final /* synthetic */ int f8485r;

    /* JADX DEBUG: Marked for inline */
    /* JADX DEBUG: Method not inlined, still used in: [com.brazvip.fivetv.adapter.VodGroupAdapter.<init>(java.util.List<com.brazvip.fivetv.beans.vod.VodGroupL2>, android.content.Context, android.os.Handler, com.brazvip.fivetv.adapter.NavigationListener):void, com.brazvip.fivetv.p220b.BSVodChannel.parseVodGroups(java.lang.String):void] */
    public /* synthetic */ C2320l(int i) {
        this.f8485r = i;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$parseVodGroups$3;
        int lambda$new$0;
        switch (this.f8485r) {
//            case 0:
//                return ((C2321m) obj).f8486a - ((C2321m) obj2).f8486a;
//            case 1:
//                return Float.compare(((C2321m) obj).f8488c, ((C2321m) obj2).f8488c);
//            case 2:
//                return ((C3018y) ((AbstractC2955c1) obj)).f10301a.compareTo(((C3018y) ((AbstractC2955c1) obj2)).f10301a);
//            case 3:
//                Charset charset = C3259a.f11313d;
//                String name = ((File) obj).getName();
//                int i = C3259a.f11314e;
//                return name.substring(0, i).compareTo(((File) obj2).getName().substring(0, i));
//            case 4:
//                Charset charset2 = C3259a.f11313d;
//                return ((File) obj2).getName().compareTo(((File) obj).getName());
            case 5:
                lambda$new$0 = VodGroupAdapter.compare((VodGroupL2) obj, (VodGroupL2) obj2);
                return lambda$new$0;
            default:
                lambda$parseVodGroups$3 = VodChannelInstance.compare((Map.Entry) obj, (Map.Entry) obj2);
                return lambda$parseVodGroups$3;
        }
    }
}
