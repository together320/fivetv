package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.utils.Utils;

public abstract class HistoryRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    public static String TAG = "HistoryRecyclerViewAdapter";
    public Context context;
    public int mSelectedItem = 0;
    public int nextSelectItem = -1;

    public RecyclerView recyclerView;
    public final Config.VIDEO_TYPE video_type;

    public HistoryRecyclerViewAdapter(Context context, Config.VIDEO_TYPE video_type) {
        this.context = context;
        this.video_type = video_type;
    }

    public static boolean isReturnKeyCode(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        return keyCode == 23 || keyCode == 66 || keyCode == 96;
    }

    public int getNextSelectedItem() {
        return this.nextSelectItem;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                RecyclerView.LayoutManager layoutManager = HistoryRecyclerViewAdapter.this.recyclerView.getLayoutManager();
                if (keyEvent.getAction() == 0 && i == 4) {
                    MainActivity.showQuitDialog(HistoryRecyclerViewAdapter.this.context);
                    return true;
                } else if (keyEvent.getAction() != 0) {
                    if (keyEvent.getAction() == 1 && HistoryRecyclerViewAdapter.isReturnKeyCode(keyEvent) && (keyEvent.getFlags() & 128) != 128) {
                        HistoryRecyclerViewAdapter historyRecyclerViewAdapter = HistoryRecyclerViewAdapter.this;
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = historyRecyclerViewAdapter.recyclerView.findViewHolderForAdapterPosition(historyRecyclerViewAdapter.mSelectedItem);
                        if (findViewHolderForAdapterPosition != null) {
                            findViewHolderForAdapterPosition.itemView.performClick();
                        }
                        return true;
                    }
                    return false;
                } else if (HistoryRecyclerViewAdapter.isReturnKeyCode(keyEvent)) {
                    if ((keyEvent.getFlags() & 128) == 128) {
                        HistoryRecyclerViewAdapter historyRecyclerViewAdapter2 = HistoryRecyclerViewAdapter.this;
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = historyRecyclerViewAdapter2.recyclerView.findViewHolderForAdapterPosition(historyRecyclerViewAdapter2.mSelectedItem);
                        if (findViewHolderForAdapterPosition2 != null) {
                            findViewHolderForAdapterPosition2.itemView.performLongClick();
                        }
                    } else {
                        keyEvent.startTracking();
                    }
                    return true;
                } else {
                    HistoryRecyclerViewAdapter historyRecyclerViewAdapter3 = HistoryRecyclerViewAdapter.this;
                    Config.VIDEO_TYPE video_type = historyRecyclerViewAdapter3.video_type;
//                    if (video_type == Config.VIDEO_TYPE.BSLIVE) {
//                        switch (i) {
//                            case 19:
//                                break;
//                            case 20:
//                                HistoryFragment.navHandler.sendEmptyMessage(1);
//                                break;
//                            case 21:
//                                if (historyRecyclerViewAdapter3.mSelectedItem == 0) {
//                                    SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                                    HistoryFragment.lastFocusVideoType = Config.VIDEO_TYPE.BSLIVE;
//                                    HistoryRecyclerViewAdapter.this.nextSelectItem = -100;
//                                    return true;
//                                } else if (historyRecyclerViewAdapter3.tryMoveSelection(layoutManager, -1)) {
//                                    return true;
//                                } else {
//                                    SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                                    return true;
//                                }
//                            case 22:
//                                historyRecyclerViewAdapter3.tryMoveSelection(layoutManager, 1);
//                                return true;
//                            default:
//                                return false;
//                        }
//                        return true;
//                    }
//                    if (video_type == Config.VIDEO_TYPE.BSVOD) {
//                        switch (i) {
//                            case 19:
//                                HistoryFragment.navHandler.sendEmptyMessage(2);
//                                return true;
//                            case 20:
//                                break;
//                            case 21:
//                                if (historyRecyclerViewAdapter3.mSelectedItem % Config.gridSpanCount != 0) {
//                                    if (!historyRecyclerViewAdapter3.tryMoveSelection(layoutManager, -1)) {
//                                        SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                                        break;
//                                    }
//                                } else {
//                                    SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                                    HistoryFragment.lastFocusVideoType = Config.VIDEO_TYPE.BSVOD;
//                                    HistoryRecyclerViewAdapter.this.nextSelectItem = -100;
//                                    return true;
//                                }
//                                break;
//                            case 22:
//                                historyRecyclerViewAdapter3.tryMoveSelection(layoutManager, 1);
//                                return true;
//                            default:
//                                return false;
//                        }
//                        return true;
//                    }
                    return false;
                }
            }
        });
        recyclerView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.brazvip.fivetv.adapter.HistoryRecyclerViewAdapter.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                HistoryRecyclerViewAdapter historyRecyclerViewAdapter = HistoryRecyclerViewAdapter.this;
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = historyRecyclerViewAdapter.recyclerView.findViewHolderForAdapterPosition(historyRecyclerViewAdapter.mSelectedItem);
                if (findViewHolderForAdapterPosition != null) {
                    findViewHolderForAdapterPosition.itemView.performLongClick();
                    return true;
                }
                return true;
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() { // from class: com.brazvip.fivetv.adapter.HistoryRecyclerViewAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HistoryRecyclerViewAdapter historyRecyclerViewAdapter = HistoryRecyclerViewAdapter.this;
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = historyRecyclerViewAdapter.recyclerView.findViewHolderForAdapterPosition(historyRecyclerViewAdapter.mSelectedItem);
                if (findViewHolderForAdapterPosition != null) {
                    findViewHolderForAdapterPosition.itemView.performClick();
                }
            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.brazvip.fivetv.adapter.HistoryRecyclerViewAdapter.4
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                HistoryRecyclerViewAdapter.this.nextSelectItem = z ? 0 : -1;
                String str = HistoryRecyclerViewAdapter.TAG;
                HistoryRecyclerViewAdapter historyRecyclerViewAdapter = HistoryRecyclerViewAdapter.this;
                historyRecyclerViewAdapter.notifyItemChanged(historyRecyclerViewAdapter.mSelectedItem);
            }
        });
    }

    public boolean tryMoveSelection(RecyclerView.LayoutManager layoutManager, int i) {
        this.nextSelectItem = this.mSelectedItem + i;
        int i2 = this.nextSelectItem;
        if (i2 < 0 || i2 >= getItemCount()) {
            return false;
        }
        notifyItemChanged(this.mSelectedItem);
        int nextItem = this.nextSelectItem;
        this.mSelectedItem = nextItem;
        notifyItemChanged(nextItem);
        this.recyclerView.scrollToPosition(this.mSelectedItem);
        return true;
    }

    public abstract void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int backgroundResource);
}
