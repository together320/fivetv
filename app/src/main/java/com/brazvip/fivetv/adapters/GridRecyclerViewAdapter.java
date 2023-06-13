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
import com.brazvip.fivetv.utils.RestApiUtils;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.p 3488 */
/* loaded from: classes.dex */
public abstract class GridRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /* renamed from: c 13566 */
    public static String TAG = "GridRecyclerViewAdapter";

    /* renamed from: d 13567 */
    public Context mContext;

    /* renamed from: e */
    public int mSelectedItem;

    /* renamed from: f */
    public int mNextSelectItem;

    /* renamed from: g 13570 */
    public RecyclerView mOwnerRecyclerView;

    /* renamed from: h 13571 */
    public Config.CHANNEL_TYPE mChannelType;

    /* renamed from: i */
    public int f13572i;


    public GridRecyclerViewAdapter(Context context) {
        this.mSelectedItem = 0;
        this.mNextSelectItem = -1;
        this.f13572i = 5;
        this.mContext = context;
    }

    /* renamed from: f 2477 */
    public Context getContext() {
        return this.mContext;
    }

    /* renamed from: g */
    public void setSelectedItem(int i) {
        mSelectedItem = i;
    }

    /* renamed from: h */
    public RecyclerView getOwnerRecyclerView() {
        return mOwnerRecyclerView;
    }

    /* renamed from: i */
    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    @Override 
    /* renamed from: b */
    public void onBindViewHolder(VH vh, int i) {
        onBindViewHolder(vh, i);
    }

    /* renamed from: f */
    public void setNextSelectItem(int i) {
        this.mNextSelectItem = i;
    }

    /* renamed from: g */
    public int getNextSelectItem() {
        return this.mNextSelectItem;
    }

    @Override 
    /* renamed from: a */
    public void onAttachedToRecyclerView(RecyclerView view) {
        mOwnerRecyclerView = view;
        view.setOnKeyListener(new View.OnKeyListener() { //new View$OnKeyListenerC3484l(this, view)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager layoutManager = mOwnerRecyclerView.getLayoutManager();
                //String text1 = GridRecyclerViewAdapter.TAG;
                //String text2 = "=========== ============ onKey:" + keyCode + " event:" + event;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == 0) {
                        if (PlayerLayout.isPlaying() && MainActivity.f16805n && mChannelType == Config.CHANNEL_TYPE.VOD_CHANNEL) {
                            MainActivity.mMsgHandler.sendEmptyMessage(100);
                            return true;
                        }
                        PrefUtils.logout(mContext);
                        return true;
                    }
                    return true;
                } else if (event.getAction() == 0) {
                    if (keyCode == 22) {
                        return m2483a(layoutManager, 1);
                    } else if (keyCode != 21) {
                        if (keyCode != 19) {
                            if (keyCode == 20) {
                                return m2483a(layoutManager, f13572i);
                            }
                            return false;
                        }
                        if (mChannelType != Config.CHANNEL_TYPE.APPS || mSelectedItem > f13572i) {
                            return m2483a(layoutManager, -f13572i);
                        }
                        return true;
                    } else {
                        if (mSelectedItem % f13572i != 0) {
                            return m2483a(layoutManager, -1);
                        }
                        if (mChannelType == Config.CHANNEL_TYPE.VOD_CHANNEL && !VodLayout.isSearchState) {
                            MainActivity.mMsgHandler.sendEmptyMessage(105);
                            VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                        } else {
                            if (mChannelType == Config.CHANNEL_TYPE.APPS) {
                                MainActivity.mMsgHandler.sendEmptyMessage(106);
                            } else if (mChannelType == Config.CHANNEL_TYPE.VOD_CHANNEL && VodLayout.isSearchState) {
                                MainActivity.mMsgHandler.sendEmptyMessage(107);
                                VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                            }
                        }
                        mNextSelectItem = -100;
                        return true;
                    }
                } else
                    return false;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() { //View$OnLongClickListenerC3485m
            @Override
            public boolean onLongClick(View v) {
                RecyclerView.ViewHolder holder = mOwnerRecyclerView.findViewHolderForAdapterPosition(mSelectedItem);
                if (holder != null) {
                    if (holder.itemView != null) {
                        holder.itemView.performLongClick();
                    }
                }
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3486n
            @Override
            public void onClick(View v) {
                RecyclerView.ViewHolder holder = mOwnerRecyclerView.findViewHolderForAdapterPosition(mSelectedItem);
                if (holder != null) {
                    if (holder.itemView != null) {
                        holder.itemView.performClick();
                    }
                }
            }
        });
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() { //View$OnFocusChangeListenerC3487o
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mNextSelectItem = 0;
                }
                //String log = "-----------onFocusChange " + hasFocus + " mSelectedItem " + f13568e;
                notifyItemChanged(mSelectedItem);
                setSelectedItem(0);
                notifyItemChanged(mSelectedItem);
            }
        });
    }

    public GridRecyclerViewAdapter(Context context, Config.CHANNEL_TYPE channelType) {
        this.mSelectedItem = 0;
        this.mNextSelectItem = -1;
        this.f13572i = 5;
        this.mContext = context;
        this.mChannelType = channelType;
        if (this.mChannelType == Config.CHANNEL_TYPE.VOD_CHANNEL) {
            this.f13572i = RestApiUtils.vodGridSpanCount;
        }
        if (this.mChannelType == Config.CHANNEL_TYPE.APPS) {
            this.f13572i = RestApiUtils.f13726J;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public boolean m2483a(RecyclerView.LayoutManager layoutManager, int position) {
        mNextSelectItem = mSelectedItem + position;
        //String text1 = TAG;
        //String text2 = "mSelectedItem: " + f13568e + " nextSelectItem: " + f13569f;
        if (mNextSelectItem < 0) {
            notifyItemChanged(mSelectedItem);
            return false;
        } else if (mNextSelectItem < 0 || mNextSelectItem >= getItemCount()) {
            return mNextSelectItem >= getItemCount();
        } else {
            notifyItemChanged(mSelectedItem);
            mSelectedItem = mNextSelectItem;
            notifyItemChanged(mSelectedItem);
            mOwnerRecyclerView.scrollToPosition(mSelectedItem);
            return true;
        }
    }

    /* renamed from: a 2482 */
    public static boolean isEnterKey(KeyEvent event) {
        int keyCode = event.getKeyCode();
        return  keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_ENTER ||
                keyCode == KeyEvent.KEYCODE_BUTTON_A;
    }
}
