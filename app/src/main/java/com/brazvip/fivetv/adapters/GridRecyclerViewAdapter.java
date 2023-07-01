package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.layouts.PlayerLayout;


public abstract class GridRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnKeyListener {
    public static String TAG = "GridRecyclerViewAdapter";
    public int columnCount;
    public Context context;
    public Config.MenuType menuType;
    public RecyclerView recyclerView;
    public int mSelectedItem = 0;
    public int nextSelectItem = -1;
    public NavigationListener navigationKeyListener = null;

    public GridRecyclerViewAdapter(Context context, Config.MenuType menuType) {
        this.columnCount = 5;
        this.context = context;
        this.menuType = menuType;
        if (menuType == Config.MenuType.VOD) {
            this.columnCount = Config.maxVodColumns;
        }
        if (menuType == Config.MenuType.APPS) {
            this.columnCount = Config.maxAppColumns;
        }
    }

    private static boolean tryMoveSelection(GridRecyclerViewAdapter gridRecyclerViewAdapter, RecyclerView.LayoutManager layoutManager, int i) {
        gridRecyclerViewAdapter.nextSelectItem = gridRecyclerViewAdapter.mSelectedItem + i;
        int i2 = gridRecyclerViewAdapter.mSelectedItem;
        int i3 = gridRecyclerViewAdapter.nextSelectItem;
        if (i3 < 0) {
            gridRecyclerViewAdapter.notifyItemChanged(i2);
            return false;
        } else if (i3 < 0 || i3 >= gridRecyclerViewAdapter.getItemCount()) {
            return gridRecyclerViewAdapter.nextSelectItem >= gridRecyclerViewAdapter.getItemCount();
        } else {
            gridRecyclerViewAdapter.notifyItemChanged(gridRecyclerViewAdapter.mSelectedItem);
            int i4 = gridRecyclerViewAdapter.nextSelectItem;
            gridRecyclerViewAdapter.mSelectedItem = i4;
            gridRecyclerViewAdapter.notifyItemChanged(i4);
            gridRecyclerViewAdapter.recyclerView.scrollToPosition(gridRecyclerViewAdapter.mSelectedItem);
            return true;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setOnKeyListener(this);
        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem) == null ||
                    recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem).itemView == null) {
                    return true;
                }
                recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem).itemView.performLongClick();
                return true;
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem) == null ||
                    recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem).itemView == null) {
                    return;
                }
                recyclerView.findViewHolderForAdapterPosition(GridRecyclerViewAdapter.this.mSelectedItem).itemView.performClick();
            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    GridRecyclerViewAdapter.this.nextSelectItem = 0;
                }
                if (GridRecyclerViewAdapter.this.mSelectedItem < recyclerView.getAdapter().getItemCount()) {
                    GridRecyclerViewAdapter gridRecyclerViewAdapter = GridRecyclerViewAdapter.this;
                    gridRecyclerViewAdapter.notifyItemChanged(gridRecyclerViewAdapter.mSelectedItem);
                    return;
                }
                GridRecyclerViewAdapter gridRecyclerViewAdapter2 = GridRecyclerViewAdapter.this;
                gridRecyclerViewAdapter2.mSelectedItem = 0;
                gridRecyclerViewAdapter2.notifyItemChanged(0);
            }
        });
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        NavigationListener navigationListener;
        NavigationListener navigationListener2;
        Handler handler;
        int i2;
        RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyEvent.getAction() == 0) {
                if (PlayerLayout.isPlaying() && this.menuType == Config.MenuType.VOD) {
                    //SopCast.handler.sendEmptyMessage(100);
                } else {
                    MainActivity.SendMessage(Constant.MSG_SHOW_QUIT_DIALOG);
                }
            }
            return true;
        } else if (keyEvent.getAction() == 0) {
            switch (keyCode) {
                case 19:
                    if ((this.menuType == Config.MenuType.APPS && this.mSelectedItem <= this.columnCount) || tryMoveSelection(this, layoutManager, this.columnCount * (-1)) || (navigationListener = this.navigationKeyListener) == null) {
                        return true;
                    }
                    return navigationListener.navigateAbove();
                case 20:
                    if (tryMoveSelection(this, layoutManager, this.columnCount) || (navigationListener2 = this.navigationKeyListener) == null) {
                        return true;
                    }
                    return navigationListener2.navigateBelow();
                case 21:
                    if (this.mSelectedItem % this.columnCount != 0) {
                        return tryMoveSelection(this, layoutManager, -1);
                    }
                    Config.MenuType menuType = this.menuType;
                    Config.MenuType menuType2 = Config.MenuType.VOD;
//                    if (menuType == menuType2) {
//                        NavigationListener navigationListener3 = this.navigationKeyListener;
//                        if (navigationListener3 != null) {
//                            return navigationListener3.navigateLeft();
//                        }
//                        if (VodLayo.IS_SEARCH_STATE) {
//                            handler = SopCast.handler;
//                            i2 = 107;
//                        } else {
//                            handler = SopCast.handler;
//                            i2 = SopHandler.EVENT_FOCUS_VOD_BUTTON;
//                        }
//                        handler.sendEmptyMessage(i2);
//                        VodFragment.menuType = menuType2;
//                    } else if (menuType == Config.MenuType.APPS) {
//                        SopCast.handler.sendEmptyMessage(106);
//                    }
                    this.nextSelectItem = -100;
                    return true;
                case 22:
                    return tryMoveSelection(this, layoutManager, 1);
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
