package com.brazvip.fivetv.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.dialogs.VodDialog;
import com.brazvip.fivetv.layouts.VodLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.brazvip.fivetv.R;
import java.util.List;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.instances.VodChannelInstance;

public class VodChannelAdapter extends GridRecyclerViewAdapter<VodChannelAdapter.ViewHolder> {

    private static final String TAG = "VodChannelAdapter";
    public static int searchCount;
    private List<VodChannelBean> channels;
    private Context context;
    private FragmentManager fragmentManager;
    private String groupKey;

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView channelName;
        public ImageView favorite;
        public ImageView image;

        public ViewHolder(VodChannelAdapter vodChannelAdapter, View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.channelName = (TextView) view.findViewById(R.id.channel_name);
            this.favorite = (ImageView) view.findViewById(R.id.favorite_icon);
        }
    }

    public VodChannelAdapter(List<VodChannelBean> list, Context context, String str, FragmentManager FragmentManager, NavigationListener navigationListener) {
        super(context, Config.MenuType.VOD);
        this.context = context;
        this.channels = list;
        this.groupKey = str;
        this.fragmentManager = FragmentManager;
        this.navigationKeyListener = navigationListener;
    }

    public static void onFocusChange(ViewHolder viewHolder, View view, boolean z) {
        View view2 = viewHolder.itemView;
        if (z) {
            view2.setSelected(true);
            viewHolder.image.setBackgroundResource(R.color.background_v3_secondary);
        } else {
            view2.setSelected(false);
            viewHolder.image.setBackgroundResource(0);
        }
    }

    public Filter getFilter() {
        VodChannelInstance.cancelSearch = true;
        return new Filter() {
            @Override // android.widget.Filter
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                List<VodChannelBean> doSearch = VodChannelInstance.doSearch(charSequence);
                filterResults.count = doSearch.size();
                filterResults.values = doSearch;
                VodChannelAdapter.searchCount = doSearch.size();
                return filterResults;
            }

            @Override // android.widget.Filter
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                VodChannelAdapter.this.channels = (List) filterResults.values;
                VodChannelAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public String getGroupKey() {
        return this.groupKey;
    }

    @Override
    public int getItemCount() {
        return this.channels.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(VodChannelAdapter.ViewHolder viewHolder, int position) {
        boolean isSelected = position == this.mSelectedItem;
        boolean isUnSelected = isSelected && this.nextSelectItem >= 0;
        viewHolder.itemView.setSelected(isSelected);

        VodChannelBean vodChannelBean = this.channels.get(viewHolder.getAbsoluteAdapterPosition());
        String title = vodChannelBean.getTitle();
        String poster = vodChannelBean.getPoster();
        if (VodChannelInstance.isFavoriteVod(this.channels.get(viewHolder.getAbsoluteAdapterPosition()).getId())) {
            viewHolder.favorite.setImageResource(R.drawable.ic_star_fav);
        }
        viewHolder.channelName.setText(title);
        if (isUnSelected) {
            viewHolder.image.setBackgroundResource(R.color.background_v3_secondary);
        } else {
            viewHolder.image.setBackgroundResource(0);
        }
        try {
            Glide.with(this.context)
                    .load(poster)
                    .placeholder(R.mipmap.loading)
                    .error(R.mipmap.load_error)
                    .into(viewHolder.image);
        } catch (Exception unused) {
        }
        viewHolder.itemView.setOnFocusChangeListener(new ViewOnFocusChangeListener(viewHolder, 0));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String id = vodChannelBean.getId();
                if (VodChannelInstance.isFavoriteVod(id)) {
                    Context context = VodChannelAdapter.this.context;
                    Toast.makeText(context, vodChannelBean.getTitle() + " " + VodChannelAdapter.this.context.getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
                    VodChannelInstance.removeFavoriteChannel(id);
                    if (VodChannelInstance.FAVORITES_GROUP_ID.equals(VodChannelAdapter.this.groupKey)) {
                        VodChannelAdapter.this.channels = VodChannelInstance.getFavoriteChannels();
                        VodChannelAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                        VodChannelAdapter.this.notifyDataSetChanged();
                        int adapterPosition = viewHolder.getAbsoluteAdapterPosition() - 1;
                        if (viewHolder.getAbsoluteAdapterPosition() == 0 && VodChannelAdapter.this.channels.size() > 0) {
                            adapterPosition = 0;
                        }
                        if (adapterPosition >= 0) {
                            VodChannelAdapter.this.notifyItemChanged(adapterPosition);
                            VodChannelAdapter vodChannelAdapter = VodChannelAdapter.this;
                            vodChannelAdapter.nextSelectItem = adapterPosition;
                            vodChannelAdapter.mSelectedItem = adapterPosition;
                            vodChannelAdapter.notifyItemChanged(adapterPosition);
                        } else {
                            VodLayout.handler.removeMessages(3);
                            Message obtainMessage = VodLayout.handler.obtainMessage();
                            obtainMessage.what = 3;
                            Bundle bundle = new Bundle();
                            bundle.putString("groupId", VodChannelInstance.FAVORITES_GROUP_ID);
                            bundle.putBoolean("restrictedAccess", false);
                            obtainMessage.setData(bundle);
                            VodLayout.handler.sendMessage(obtainMessage);
//                            SopCast.handler.sendEmptyMessage(108);
//                            VodLayout.menuType = Config.MenuType.f8635d;
                        }
                    } else {
                        notifyItemChanged(mSelectedItem);
                        nextSelectItem = recyclerView.getChildLayoutPosition(view);
                        mSelectedItem = recyclerView.getChildLayoutPosition(view);
                        notifyItemChanged(mSelectedItem);
                    }
                    viewHolder.favorite.setImageResource(0);
                } else {
                    Context context2 = VodChannelAdapter.this.context;
                    Toast.makeText(context2, vodChannelBean.getTitle() + " " + VodChannelAdapter.this.context.getString(R.string.favorited), Toast.LENGTH_SHORT).show();
                    VodChannelInstance.addFavoriteChannel(id);
                    VodChannelAdapter.this.notifyDataSetChanged();

                    notifyItemChanged(mSelectedItem);
                    nextSelectItem = recyclerView.getChildLayoutPosition(view);
                    mSelectedItem = recyclerView.getChildLayoutPosition(view);
                    notifyItemChanged(mSelectedItem);
                }
                VodLayout.menuType = Config.MenuType.VOD;
                return true;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                notifyItemChanged(mSelectedItem);
                nextSelectItem = recyclerView.getChildLayoutPosition(view);
                mSelectedItem = recyclerView.getChildLayoutPosition(view);
                notifyItemChanged(mSelectedItem);
                Config.MenuType menuType = Config.MenuType.VOD;
                VodLayout.menuType = menuType;
                VodChannelBean fullChannelBean = VodChannelInstance.getFullChannelBean(vodChannelBean.getId());
                if (fullChannelBean == null || fullChannelBean.getEpisodes() == null || fullChannelBean.getEpisodes().isEmpty()) {
                    return;
                }
                fullChannelBean.restricted = vodChannelBean.restricted;

                VodDialog.createDialog(VodChannelAdapter.this.context, fullChannelBean, menuType).show();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.vod_channel_item, viewGroup, false));
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (keyEvent.getAction() == 1) {
                this.mSelectedItem = 0;
                this.nextSelectItem = -1;
                notifyDataSetChanged();
                VodLayout.channelRView.scrollToPosition(0);
                VodLayout.groupRView.requestFocus();
                VodLayout.groupRView.requestFocusFromTouch();
                keyEvent.getAction();
            }
            return true;
        }
        return super.onKey(view, i, keyEvent);
    }
}
