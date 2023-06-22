package com.brazvip.fivetv.adapters;

import android.view.KeyEvent;
import android.view.View;
//import com.brazvip.fivetv.adapters.VerticalProfileAdapter;

public class ViewOnKeyListener2 implements View.OnKeyListener {

    public int type;

    /* renamed from: s */
    public Object obj;

    public ViewOnKeyListener2(Object obj, int type) {
        this.type = type;
        this.obj = obj;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean flag;
        switch (this.type) {
//            case 0:
//                return VerticalProfileAdapter.m1728b((VerticalProfileAdapter.ViewHolder) this.f8631s, view, i, keyEvent);
            case 1:
                flag = ((CustomItemAdapter) this.obj).lambda$new$1(view, i, keyEvent);
                return flag;
//            case 2:
//                return EditProfilesListAdapter.m1735d((EditProfilesListAdapter) this.f8631s, view, i, keyEvent);
//            case 3:
//                return HorizontalProfileAdapter.m1733d((HorizontalProfileAdapter) this.f8631s, view, i, keyEvent);
//            default:
//                flag = ((SportAdapter) this.f8631s).flag(view, i, keyEvent);
//                return flag;
        }
        return false;
    }
}
