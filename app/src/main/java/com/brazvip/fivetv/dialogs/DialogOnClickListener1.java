package com.brazvip.fivetv.dialogs;

import android.view.View;

public final class DialogOnClickListener1 implements View.OnClickListener {

    public final int type;

    public final SeriesDialog dialog;

    public DialogOnClickListener1(SeriesDialog seriesDialog, int i) {
        this.type = i;
        this.dialog = seriesDialog;
    }

    @Override
    public void onClick(View view) {
        switch (this.type) {
            case 0:
                SeriesDialog.createView(this.dialog, view);
                break;
            default:
                SeriesDialog.videoPlayback(this.dialog, view);
                break;
        }
    }
}
