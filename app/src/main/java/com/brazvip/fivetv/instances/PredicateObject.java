package com.brazvip.fivetv.instances;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Predicate;
import com.brazvip.fivetv.beans.vod.VodChannelBean;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PredicateObject implements Predicate {

    public int type;

    /* renamed from: b */
    public Object object;

    public PredicateObject(Object obj, int i) {
        this.type = i;
        this.object = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.type) {
            case 0:
                return VodChannelInstance.containsInList((String) this.object, (VodChannelBean) obj);
            case 1:
                return VodChannelInstance.doSearch((CharSequence) this.object, (VodChannelBean) obj);
            default:
                return VodChannelInstance.getChannelsByGroupKey((VodChannelBean) this.object, (VodChannelBean) obj);
        }
    }
}
