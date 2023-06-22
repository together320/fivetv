package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import com.brazvip.fivetv.adapters.CustomItemAdapter;

public class ViewOnKeyListener1 implements View.OnKeyListener {

    /* renamed from: r */
    public int type;

    /* renamed from: s */
    public Context context;

    /* renamed from: t */
    public CustomItemAdapter adapter;

    public ViewOnKeyListener1(CustomItemAdapter customItemAdapter, Context context, int type) {
        this.type = type;
        this.adapter = customItemAdapter;
        this.context = context;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
//        switch (this.type) {
//            case 0:
//                return ((DashboardGroupL1Adapter) this.adapter).lambda$new$0(this.context, view, i, keyEvent);
//            case 1:
//                return ((DashboardGroupL2Adapter) this.adapter).lambda$new$0(this.context, view, i, keyEvent);
//            default:
//                return ((DashboardLineAdapter) this.adapter).lambda$new$1(this.context, view, i, keyEvent);
//        }
        return false;
    }
}
