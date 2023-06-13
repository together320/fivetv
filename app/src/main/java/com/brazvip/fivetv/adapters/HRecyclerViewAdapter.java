package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.layouts.PlayerLayout;
import com.brazvip.fivetv.layouts.VodLayout;
import com.brazvip.fivetv.utils.PrefUtils;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.u 3493 */
/* loaded from: classes.dex */
public abstract class HRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /* renamed from: c 13584 */
    public static final String TAG = "HRecyclerViewAdapter";

    /* renamed from: d */
    public Context f13585d;

    /* renamed from: e */
    public int mSelectedItem = 0;

    /* renamed from: f */
    public int mNextSelectItem = -1;

    /* renamed from: g 13588 */
    public RecyclerView mParentView;

    /* renamed from: h */
    public int f13589h;

    public HRecyclerViewAdapter(Context context, int i) {
        this.f13585d = context;
        this.f13589h = i;
    }

    /* renamed from: e */
    public Context m2460e() {
        return this.f13585d;
    }

    /* renamed from: f */
    public int getNextSelectItem() {
        return this.mNextSelectItem;
    }

    /* renamed from: g */
    public void setSelectedItem(int i) {
        this.mSelectedItem = i;
    }

    /* renamed from: h */
    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    @Override 
    /* renamed from: b */
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position);
    }

    /* renamed from: f */
    public void setNextSelectItem(int i) {
        this.mNextSelectItem = i;
    }

    /* renamed from: g */
    public RecyclerView getOwnerView() {
        return this.mParentView;
    }

    @Override 
    /* renamed from: a */
    public void onAttachedToRecyclerView(RecyclerView view) {
        mParentView = view;
        view.setOnKeyListener(new View.OnKeyListener() { //View$OnKeyListenerC3491s
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    RecyclerView.LayoutManager lm = mParentView.getLayoutManager();
                    if (event.getAction() == 0 && keyCode == 4) {
                        if (!PlayerLayout.isPlaying() || !MainActivity.f16805n) {
                            PrefUtils.logout(f13585d);
                            return true;
                        }
                        MainActivity.mMsgHandler.sendEmptyMessage(100);
                        return true;
                    }
                    if (event.getAction() == 0) {
                        if (HRecyclerViewAdapter.isEnterKey(event)) {
                            if ((event.getFlags() & 128) == 128) {
                                RecyclerView.ViewHolder holder = mParentView.findViewHolderForAdapterPosition(mSelectedItem);
                                if (holder != null) {
                                    holder.itemView.performLongClick();
                                }
                            } else {
                                event.startTracking();
                            }
                            return true;
                        } else if (keyCode == 19) {
                            mNextSelectItem = -100;
                            return false;
                        } else if (keyCode == 20) {
                            VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                            mNextSelectItem = -100;
                            return f13589h == 2 && VodLayout.channelRView.getVisibility() == View.GONE;
                        } else if (keyCode == 22) {
                            return m2469a(lm, 1);
                        } else if (keyCode == 21) {
                            return m2469a(lm, -1);
                        }
                    } else if (event.getAction() == KeyEvent.ACTION_UP &&
                            HRecyclerViewAdapter.isEnterKey(event) &&
                            (event.getFlags() & KeyEvent.FLAG_LONG_PRESS) != KeyEvent.FLAG_LONG_PRESS) {
                        RecyclerView.ViewHolder holder = mParentView.findViewHolderForAdapterPosition(mSelectedItem);
                        if (holder != null) {
                            holder.itemView.performClick();
                        }
                        return true;
                    }
                    return false;
                }
            }
        );
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() { //View$OnFocusChangeListenerC3492t
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mNextSelectItem = 0;
                }
                notifyItemChanged(mSelectedItem);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public boolean m2469a(RecyclerView.LayoutManager lm, int position) {
        mNextSelectItem = mSelectedItem + position;
        //String log = "mSelectedItem: " + f13586e + " nextSelectItem: " + f13587f;
        int pos = mNextSelectItem;
        if (pos >= 0 && pos < getItemCount()) {
            notifyItemChanged(mSelectedItem);
            mSelectedItem = mNextSelectItem;
            notifyItemChanged(mSelectedItem);
            mParentView.scrollToPosition(mSelectedItem);
            return true;
        } else if (this.mNextSelectItem == getItemCount()) {
            return true;
        } else {
            if (this.mNextSelectItem == -1) {
                VodLayout.channelType = Config.CHANNEL_TYPE.VOD_GROUP;
                notifyItemChanged(0);
                if (f13589h == 2) {
                    MainActivity.mMsgHandler.sendEmptyMessage(105);
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: a 2467 */
    public static boolean isEnterKey(KeyEvent event) {
        int keyCode = event.getKeyCode();
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER ||
                                            keyCode == KeyEvent.KEYCODE_BUTTON_A; //23, 66, 96
    }
}
