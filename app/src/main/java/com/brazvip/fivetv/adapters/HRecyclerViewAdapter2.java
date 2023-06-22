package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public abstract class HRecyclerViewAdapter2<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public static final String TAG = "HRecyclerViewAdapter";
    public Context context1;
    public int groupLevel;
    private final NavigationListener navigationListener;

    public RecyclerView recyclerView;
    public int mSelectedItem = 0;
    public int nextSelectItem = -1;

    public HRecyclerViewAdapter2(Context context, int i, NavigationListener navigationListener) {
        this.context1 = context;
        this.groupLevel = i;
        this.navigationListener = navigationListener;
    }

    public boolean handleHorizontalScroll(RecyclerView.LayoutManager layoutManager, int i) {
        this.nextSelectItem = this.mSelectedItem + i;
        int i2 = this.nextSelectItem;
        if (i2 >= 0 && i2 < getItemCount()) {
            notifyItemChanged(this.mSelectedItem);
            int i3 = this.nextSelectItem;
            this.mSelectedItem = i3;
            notifyItemChanged(i3);
            this.recyclerView.scrollToPosition(this.mSelectedItem);
            return true;
        } else if (this.nextSelectItem == getItemCount()) {
            return onLastItemScrolled();
        } else {
            if (this.nextSelectItem == -1) {
                //VodFragment.menuType = Config.MenuType.f8635d;
                notifyItemChanged(0);
                if (this.groupLevel == 2) {
                    //SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_VOD_BUTTON);
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isReturnKeycode(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        return keyCode == 23 || keyCode == 66 || keyCode == 96;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                RecyclerView.LayoutManager layoutManager = HRecyclerViewAdapter2.this.recyclerView.getLayoutManager();
                if (keyEvent.getAction() == 0 && i == 4) {
//                    if (SopCast.isPlaying() && SopCast.isMenuDisplayed) {
//                        SopCast.handler.sendEmptyMessage(100);
//                    } else {
//                        Utils.showQuitDialog(HRecyclerViewAdapter2.this.context1);
//                    }
                    return true;
                } else if (keyEvent.getAction() != 0) {
                    if (keyEvent.getAction() == 1 && HRecyclerViewAdapter2.isReturnKeycode(keyEvent) && (keyEvent.getFlags() & 128) != 128) {
                        HRecyclerViewAdapter2 hRecyclerViewAdapter2 = HRecyclerViewAdapter2.this;
                        if (hRecyclerViewAdapter2.recyclerView.findViewHolderForAdapterPosition(hRecyclerViewAdapter2.mSelectedItem) != null) {
                            HRecyclerViewAdapter2.this.recyclerView.findViewHolderForAdapterPosition(mSelectedItem).itemView.performClick();
                        }
                        return true;
                    }
                    return false;
                } else if (HRecyclerViewAdapter2.isReturnKeycode(keyEvent)) {
                    if ((keyEvent.getFlags() & 128) == 128) {
                        if (HRecyclerViewAdapter2.this.recyclerView.findViewHolderForAdapterPosition(mSelectedItem) != null) {
                            HRecyclerViewAdapter2.this.recyclerView.findViewHolderForAdapterPosition(mSelectedItem).itemView.performLongClick();
                        }
                    } else {
                        keyEvent.startTracking();
                    }
                    return true;
                } else if (i == 19) {
                    nextSelectItem = -100;
                    return navigationListener.navigateAbove();
                } else if (i == 20) {
                    //VodFragment.menuType = Config.MenuType.VOD;
                    nextSelectItem = -100;
                    return navigationListener.navigateBelow();
                } else if (i == 22) {
                    if (HRecyclerViewAdapter2.this.handleHorizontalScroll(layoutManager, 1)) {
                        return true;
                    }
                    return HRecyclerViewAdapter2.this.navigationListener.navigateRight();
                } else if (i == 21) {
                    if (HRecyclerViewAdapter2.this.handleHorizontalScroll(layoutManager, -1)) {
                        return true;
                    }
                    return HRecyclerViewAdapter2.this.navigationListener.navigateLeft();
                } else {
                    return false;
                }
            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    HRecyclerViewAdapter2.this.nextSelectItem = 0;
                }
                HRecyclerViewAdapter2 hRecyclerViewAdapter2 = HRecyclerViewAdapter2.this;
                hRecyclerViewAdapter2.notifyItemChanged(hRecyclerViewAdapter2.mSelectedItem);
            }
        });
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position);
    }

    public abstract boolean onLastItemScrolled();
}
