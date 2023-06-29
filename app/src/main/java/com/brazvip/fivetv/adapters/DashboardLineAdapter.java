package com.brazvip.fivetv.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import com.lzy.okgo.model.Priority;
import java.util.List;
import java.util.Locale;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.DashboardInfo;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.dialogs.VodDialog;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.layouts.DashboardLayout;

public class DashboardLineAdapter extends CustomItemAdapter<DashboardLineAdapter.LineViewHolder> {
    public DashboardInfo.Line data;
    private FragmentManager fragmentManager;
    public Context mContext;
    public NavigationListener navigationListener;
    private LineViewHolder oldHolder = null;
    public int rowNumber;

    
    public static class LineViewHolder extends RecyclerView.ViewHolder {
        public ImageView lineItemLiveImg;
        public ImageView lineItemLockImg;
        public ImageView lineItemPosterImg;

        public LineViewHolder(View view) {
            super(view);
            this.lineItemPosterImg = (ImageView) view.findViewById(R.id.line_item_image);
            this.lineItemLiveImg = (ImageView) view.findViewById(R.id.dash_item_live_img);
            this.lineItemLockImg = (ImageView) view.findViewById(R.id.dash_item_lock);
        }
    }

    public DashboardLineAdapter(DashboardInfo.Line line, Context context, NavigationListener navigationListener, int position, FragmentManager FragmentManager) {
        this.data = line;
        this.mContext = context;
        this.navigationListener = navigationListener;
        this.rowNumber = position;
        this.fragmentManager = FragmentManager;
        this.onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                onLineItemFocusChange(view, b);
            }
        };
        this.onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return onLineItemKey(mContext, view, i, keyEvent);
            }
        };
    }

    public void onLineItemFocusChange(View view, boolean z) {
        this.nextSelectItem = z ? 0 : -1;
        notifyItemChanged(this.mSelectedItem);
    }

    public boolean onLineItemKey(Context context, View view, int i, KeyEvent keyEvent) {
        RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        if (keyEvent.getAction() != 0) {
            if (keyEvent.getAction() == 1 && HRecyclerViewAdapter2.isReturnKeycode(keyEvent) && (keyEvent.getFlags() & 128) != 128) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.recyclerView.findViewHolderForAdapterPosition(this.mSelectedItem);
                if (findViewHolderForAdapterPosition != null) {
                    findViewHolderForAdapterPosition.itemView.performClick();
                }
                return true;
            }
            return false;
        } else if (i == 4) {
            //Utils.showQuitDialog(context);
            return true;
        } else {
            switch (i) {
                case 19:
                    return this.navigationListener.navigateAbove();
                case 20:
                    return this.navigationListener.navigateBelow();
                case 21:
                    if (tryMoveSelection(layoutManager, -1)) {
                        return true;
                    }
                    return this.navigationListener.navigateLeft();
                case 22:
                    if (tryMoveSelection(layoutManager, 1)) {
                        return true;
                    }
                    return this.navigationListener.navigateRight();
                default:
                    return false;
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public LineViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new LineViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.dashboard_grid_item, viewGroup, false));
    }

    @Override // com.brazvip.fivetv.adapter.CustomItemAdapter
    public void onItemSelected(int position) {
        super.onItemSelected(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public int getItemCount() {
        return this.data.items.size();
        //return Priority.UI_TOP;
    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(final LineViewHolder lineViewHolder, int position) {

        if (this.mSelectedItem == position && this.recyclerView.hasFocus()) {
            lineViewHolder.itemView.setBackgroundResource(R.drawable.dash_item_selected_bg);
        } else {
            lineViewHolder.itemView.setBackgroundResource(R.drawable.dash_item_bg);
        }

        List<DashboardInfo.Item> list = this.data.items;
        final DashboardInfo.Item item = list.get(position % list.size());
        Glide.with(this.mContext)
                .load(item.image)
                .error(R.drawable.load_error_landscape)
                //.load(com.bumptech.glide.Priority.HIGH)
                //.load(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.loading_landscape)
                .into(lineViewHolder.lineItemPosterImg);

        if (item.content_type == null || !item.content_type.toLowerCase(Locale.ROOT).equals("live")) {
            lineViewHolder.lineItemLiveImg.setVisibility(View.GONE);
        } else {
            lineViewHolder.lineItemLiveImg.setVisibility(View.VISIBLE);
        }

        lineViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                onLineItemClick(item, lineViewHolder, view);
            }
        });
    }

    public void onLineItemClick(DashboardInfo.Item item, LineViewHolder lineViewHolder, View view) {

//        if (this.oldHolder == null || this.oldHolder == lineViewHolder) {
//            lineViewHolder.itemView.setBackgroundResource(R.drawable.dash_item_selected_bg);
//        } else {
//            lineViewHolder.itemView.setBackgroundResource(R.drawable.dash_item_bg);
//        }
//        this.oldHolder = lineViewHolder;

        if (item.content_type == null) {
            // for live channel
            return;
        }

        if (item.content_type.equals("1")) {
            VodDialog createDialog;
            VodChannelBean fullChannelBean = VodChannelInstance.getFullChannelBean(item.content_id);
            if (fullChannelBean == null || fullChannelBean.getEpisodes() == null ||
                    fullChannelBean.getEpisodes().isEmpty() ||
                    (createDialog = VodDialog.createDialog(DashboardLayout.linesScrollView.getContext(), fullChannelBean, Config.MenuType.DASHBOARD)) == null ||
                    createDialog.isShowing()) {
                //createDialog.isAdded() || createDialog.isVisible()) {
                return;
            }
            createDialog.show(); //(this.fragmentManager, createDialog.FRAGMENT_TAG);
        } else if (item.content_type.equals("0")) {
            Message message = new Message();
            message.what = 212;
            Bundle bundle = new Bundle();
            bundle.putString("channelID", item.content_id);
            message.setData(bundle);
            MainActivity.handler.sendMessage(message);
        }
    }
}
