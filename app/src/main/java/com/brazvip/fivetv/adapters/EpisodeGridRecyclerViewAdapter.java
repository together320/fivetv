package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.utils.PrefUtils;


/* compiled from: MyApplication */
/* renamed from: e.b.a.a.k 3483 */
/* loaded from: classes.dex */
public abstract class EpisodeGridRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /* renamed from: c 13556 */
    public static String TAG = "EpisodeGridRecyclerViewAdapter";

    /* renamed from: d 13557 */
    public Context mContext;

    /* renamed from: e */
    public int mPosition = 0;

    /* renamed from: f */
    public int mLayoutPosition = 0;

    /* renamed from: g */
    public RecyclerView mRecycleView;

    public EpisodeGridRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    /* renamed from: f 2489 */
    public Context getContext() {
        return mContext;
    }

    /* renamed from: g */
    public void setPosition(int i) {
        mPosition = i;
    }

    /* renamed from: h */
    public RecyclerView getRecycleView() {
        return this.mRecycleView;
    }

    /* renamed from: i */
    public int getPosition() {
        return mPosition;
    }

    @Override 
    /* renamed from: b */
    public void onBindViewHolder(VH vh, int i) {
        onBindViewHolder(vh, i);
    }

    /* renamed from: f */
    public void setLayoutPosition(int layoutPosition) {
        this.mLayoutPosition = layoutPosition;
    }

    /* renamed from: g */
    public int m2487g() {
        return mLayoutPosition;
    }

    @Override 
    /* renamed from: a */
    public void onAttachedToRecyclerView(RecyclerView parent) {
        mRecycleView = parent;
        parent.setOnKeyListener(new View.OnKeyListener() { //new View$OnKeyListenerC3482j(this, parent)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EpisodeGridRecyclerViewAdapter<VH> adapter = EpisodeGridRecyclerViewAdapter.this;
                RecyclerView.LayoutManager lm = mRecycleView.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) { //0, 4
                    //String log = EpisodeGridRecyclerViewAdapter.TAG;
                    PrefUtils.logout(mContext);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_DOWN) { //0
                    if (EpisodeGridRecyclerViewAdapter.isEnterKey(event)) {
                        if ((event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) { //128
                            mRecycleView.findViewHolderForAdapterPosition(adapter.mPosition).itemView.performLongClick();
                        } else {
                            event.startTracking();
                        }
                        return true;
                    } else if (keyCode == 22) {
                        return m2495a(lm, 1);
                    } else if (keyCode == 21) {
                        return m2495a(lm, -1);
                    } else if (keyCode == 19) {
                        return m2495a(lm, -6);
                    } else if (keyCode == 20) {
                        return m2495a(lm, 6);
                    }
                } else if (event.getAction() == 1 && EpisodeGridRecyclerViewAdapter.isEnterKey(event) && (event.getFlags() & 128) != 128) {
                    mRecycleView.findViewHolderForAdapterPosition(mPosition).itemView.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public boolean m2495a(RecyclerView.LayoutManager layoutManager, int i) {
        mLayoutPosition = mPosition + i;
        if (mLayoutPosition < 0) {
            return true;
        }
        if (mLayoutPosition >= getItemCount()) {
            return mLayoutPosition == getItemCount();
        }
        notifyItemChanged(this.mPosition);
        mPosition = this.mLayoutPosition;
        notifyItemChanged(this.mPosition);
        mRecycleView.smoothScrollToPosition(this.mPosition);
        return true;
    }

    /* renamed from: a 2494 */
    public static boolean isEnterKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER ||
                keyCode == KeyEvent.KEYCODE_BUTTON_A; //23, 66, 96
    }
}
