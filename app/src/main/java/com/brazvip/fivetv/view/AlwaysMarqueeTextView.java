package com.brazvip.fivetv.view;

import android.content.Context;
import android.util.AttributeSet;

public class AlwaysMarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AlwaysMarqueeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public boolean isFocused() {
        return true;
    }
}
