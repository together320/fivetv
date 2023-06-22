package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public abstract class HRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final String TAG = "HRecyclerViewAdapter";
    public Context context;
    public int mSelectedItem = 0;
    public int nextSelectItem = 0;
    public RecyclerView recyclerView;

    public HRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static boolean m1380a(HRecyclerViewAdapter hRecyclerViewAdapter, RecyclerView.LayoutManager layoutManager, int i) {
        hRecyclerViewAdapter.nextSelectItem = hRecyclerViewAdapter.mSelectedItem + i;
//        int i2 = hRecyclerViewAdapter.nextSelectItem;
//        if (i2 < 0 || i2 >= ((SubtitleAdapter) hRecyclerViewAdapter).subtitles.size()) {
//            return hRecyclerViewAdapter.nextSelectItem == ((SubtitleAdapter) hRecyclerViewAdapter).subtitles.size() || hRecyclerViewAdapter.nextSelectItem == -1;
//        }
        hRecyclerViewAdapter.notifyItemChanged(hRecyclerViewAdapter.mSelectedItem);
        int i3 = hRecyclerViewAdapter.nextSelectItem;
        hRecyclerViewAdapter.mSelectedItem = i3;
        hRecyclerViewAdapter.notifyItemChanged(i3);
        hRecyclerViewAdapter.recyclerView.scrollToPosition(hRecyclerViewAdapter.mSelectedItem);
        return true;
    }

    public static boolean m1383a(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        return keyCode == 23 || keyCode == 66 || keyCode == 96;
    }

    public int getNextSelectedItem() {
        return this.nextSelectItem;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setOnKeyListener(new View.OnKeyListener() { // from class: com.brazvip.fivetv.adapter.HRecyclerViewAdapter.1
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (keyEvent.getAction() == 0 && keyCode == 4) {
//                    if (SopCast.isPlaying() && SopCast.isMenuDisplayed) {
//                        SopCast.handler.sendEmptyMessage(100);
//                        return true;
//                    }
//                    Utils.showQuitDialog(HRecyclerViewAdapter.this.context);
                    return true;
                } else if (keyEvent.getAction() != 0) {
                    if (keyEvent.getAction() == 1 && HRecyclerViewAdapter.m1383a(keyEvent) && (keyEvent.getFlags() & 128) != 128) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(HRecyclerViewAdapter.this.mSelectedItem);
                        if (findViewHolderForAdapterPosition != null) {
                            findViewHolderForAdapterPosition.itemView.performClick();
                        }
                        return true;
                    }
                    return false;
                } else if (HRecyclerViewAdapter.m1383a(keyEvent)) {
                    if ((keyEvent.getFlags() & 128) == 128) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = recyclerView.findViewHolderForAdapterPosition(HRecyclerViewAdapter.this.mSelectedItem);
                        if (findViewHolderForAdapterPosition2 != null) {
                            findViewHolderForAdapterPosition2.itemView.performLongClick();
                        }
                    } else {
                        keyEvent.startTracking();
                    }
                    return true;
                } else if (keyEvent.getAction() == 19 || keyEvent.getAction() == 20) {
                    return true;
                } else {
                    if (keyCode == 22) {
                        return HRecyclerViewAdapter.m1380a(HRecyclerViewAdapter.this, layoutManager, 1);
                    }
                    if (keyCode == 21) {
                        return HRecyclerViewAdapter.m1380a(HRecyclerViewAdapter.this, layoutManager, -1);
                    }
                    return false;
                }
            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    HRecyclerViewAdapter.this.nextSelectItem = 0;
                }
                HRecyclerViewAdapter hRecyclerViewAdapter = HRecyclerViewAdapter.this;
                hRecyclerViewAdapter.notifyItemChanged(hRecyclerViewAdapter.mSelectedItem);
                HRecyclerViewAdapter hRecyclerViewAdapter2 = HRecyclerViewAdapter.this;
                if (hRecyclerViewAdapter2.nextSelectItem != -100) {
                    hRecyclerViewAdapter2.notifyItemChanged(hRecyclerViewAdapter2.mSelectedItem);
                }
            }
        });
    }
}
