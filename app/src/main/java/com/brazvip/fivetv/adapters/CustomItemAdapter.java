package com.brazvip.fivetv.adapters;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import com.brazvip.fivetv.Config;

public abstract class CustomItemAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public Config.MenuType menuType;

    public RecyclerView recyclerView;

    public int mSelectedItem = 0;
    public int nextSelectItem = -1;
    public View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            onItemFocusChange(view, b);
        }
    };

    public View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            return onItemKey(view, i, keyEvent);
        }
    };

    public void onItemFocusChange(View view, boolean z) {
        if (z) {
            this.nextSelectItem = 0;
        }
        notifyItemChanged(this.mSelectedItem);
    }

    public boolean onItemKey(View view, int keyCode, KeyEvent keyEvent) {
        RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                return tryMoveSelection(layoutManager, 1);
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                return tryMoveSelection(layoutManager, -1);
            }
            return false;
        } else if (keyEvent.getAction() == KeyEvent.ACTION_UP && HRecyclerViewAdapter2.isReturnKeycode(keyEvent) &&
                (keyEvent.getFlags() & KeyEvent.FLAG_LONG_PRESS) != KeyEvent.FLAG_LONG_PRESS) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.recyclerView.findViewHolderForAdapterPosition(this.mSelectedItem);
            if (findViewHolderForAdapterPosition != null) {
                findViewHolderForAdapterPosition.itemView.performClick();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setOnFocusChangeListener(this.onFocusChangeListener);
        recyclerView.setOnKeyListener(this.onKeyListener);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int i) {
        viewHolder.itemView.setSelected(this.mSelectedItem == i);
    }

    public void onItemSelected(int i) {
        this.mSelectedItem = i;
        notifyDataSetChanged();
        notifyItemChanged(this.mSelectedItem);
    }

    public boolean tryMoveSelection(RecyclerView.LayoutManager layoutManager, int i) {
        int i2 = this.mSelectedItem + i;
        LinearSmoothScroller LinearSmoothScroller = new LinearSmoothScroller(this.recyclerView.getContext()) {
            private static final float SPEED = 50.0f;

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return SPEED / displayMetrics.densityDpi;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int getHorizontalSnapPreference() {
                return -1;
            }
        };
        if (i2 < 0 || i2 >= getItemCount()) {
            return false;
        }
        onItemSelected(i2);
        LinearSmoothScroller.setTargetPosition(i2);
        layoutManager.startSmoothScroll(LinearSmoothScroller);
        return true;
    }
}
