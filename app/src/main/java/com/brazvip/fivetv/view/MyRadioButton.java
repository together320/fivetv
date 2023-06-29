package com.brazvip.fivetv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;


public class MyRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    public Drawable drawable;
    public int drawableResourceId;

    public MyRadioButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.drawable != null) {
            int gravity = getGravity();// & SopHandler.EVENT_FOCUS_HISTORY_BUTTON;
            int i = 0;
            if (gravity == 16) {
                i = (getHeight() - 57) / 2;
            } else if (gravity == 80) {
                i = getHeight() - 57;
            }
            int width = (getWidth() - 65) / 2;
            this.drawable.setBounds(width, i, width + 65, i + 57);
            this.drawable.draw(canvas);
        }
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(int i) {
        if (i == 0 || i != this.drawableResourceId) {
            this.drawableResourceId = i;
            setButtonDrawable(i != 0 ? getResources().getDrawable(this.drawableResourceId) : null);
        }
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(Drawable drawable) {
        if (drawable != null) {
            Drawable drawable2 = this.drawable;
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.drawable);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            drawable.setVisible(getVisibility() == 0, false);
            this.drawable = drawable;
            drawable.setState(null);
            setMinHeight(this.drawable.getIntrinsicHeight());
        }
        refreshDrawableState();
    }
}
