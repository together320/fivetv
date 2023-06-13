package com.brazvip.fivetv.dialogs;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    public int bottom;
    public int left;
    public int right;
    public int top;

    public MyItemDecoration(int i, int i2, int i3, int i4) {
        this.left = i;
        this.top = i2;
        this.right = i3;
        this.bottom = i4;
    }

    @Override // androidx.recyclerview.widget.AbstractC0433q0
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        rect.set(0, 0, 0, 0);
        rect.set(this.left, this.top, this.right, this.bottom);
    }
}
