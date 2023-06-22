package com.brazvip.fivetv.adapters;

import android.view.View;

public final class ViewOnFocusChangeListener implements View.OnFocusChangeListener {

    public final int type;

    public final Object obj;

    public ViewOnFocusChangeListener(Object obj, int type) {
        this.type = type;
        this.obj = obj;
    }

    @Override // android.view.View.OnFocusChangeListener
    public final void onFocusChange(View view, boolean z) {
        switch (this.type) {
            case 0:
                VodChannelAdapter.onFocusChange((VodChannelAdapter.ViewHolder) this.obj, view, z);
                return;
            case 1:
                VodGroupAdapter.onFocusChange((VodGroupAdapter.ViewHolder) this.obj, view, z);
                return;
            case 2:
                //CustomItemAdapter.m1745a((CustomItemAdapter) this.obj, view, z);
                return;
            default:
                //DashboardLineAdapter.m1739c((DashboardLineAdapter) this.obj, view, z);
                return;
        }
    }
}
