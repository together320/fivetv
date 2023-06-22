package com.brazvip.fivetv.dialogs;

import android.view.View;
//import com.brazvip.fivetv.dialogs.ProfilePinDialog;

public final class DialogOnClickListener2 implements View.OnClickListener {

    public final int type;

    public final Object obj1;

    public final Object obj2;

    public DialogOnClickListener2(int i, Object obj, Object obj2) {
        this.type = i;
        this.obj1 = obj;
        this.obj2 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (this.type) {
            case 0:
                MovieDialog.RecommendationAdapter.getVodInfo((MovieDialog.RecommendationAdapter) this.obj1, (MovieDialog.RecommendationAdapter.ViewHolder) this.obj2, view);
                break;
            default:
                //ProfilePinDialog.Builder.m1718a((ProfilePinDialog.Builder) this.f8666s, (ProfilePinDialog) this.f8667t, view);
                break;
        }
    }
}
