package com.brazvip.fivetv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;


public class AutoLayoutRadioGroup extends RadioGroup {
    public AutoLayoutHelper autoLayoutHelper;

    
    public static class LayoutParams extends RadioGroup.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
        public AutoLayoutInfo layoutInfo;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.layoutInfo = AutoLayoutHelper.getAutoLayoutInfo(context, attributeSet);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return this.layoutInfo;
        }
    }

    public AutoLayoutRadioGroup(Context context) {
        super(context);
        this.autoLayoutHelper = new AutoLayoutHelper(this);
    }

    public AutoLayoutRadioGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.autoLayoutHelper = new AutoLayoutHelper(this);
    }

    @Override
    public RadioGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    public void onMeasure(int i, int i2) {
        if (!isInEditMode()) {
            this.autoLayoutHelper.adjustChildren();
        }
        super.onMeasure(i, i2);
    }
}
