package com.brazvip.fivetv.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.layouts.VodLayout;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.dialogs.EpisodeDialog;
import com.brazvip.fivetv.dialogs.PasswordChangeDialog;
import com.brazvip.fivetv.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;


public class DashboardChannelAdapter extends GridRecyclerViewAdapter<DashboardChannelAdapter.DashboardChannelViewHolder> implements Filterable {

    /* renamed from: j 13466 */
    public static final String TAG = "DashboardChannelAdapter";

    /* renamed from: k 13467 */
    public static EpisodeDialog.Helper mEpisodeDlgHelper;

    /* renamed from: l 13468 */
    public List<ChannelBean> mChannelList;

    /* renamed from: m 13469 */
    public Context mContext;

    /* renamed from: n */
    public int f13470n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a.B$a 3463 */
    /* loaded from: classes.dex */
    public class DashboardChannelViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;

        public TextView tvName;

        public ImageView ivFavorite;

        public DashboardChannelViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.image);
            tvName = (TextView) view.findViewById(R.id.channel_name);
            ivFavorite = (ImageView) view.findViewById(R.id.favorite_icon);
            AutoUtils.auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_HEIGHT);
        }
    }

    public DashboardChannelAdapter(List<ChannelBean> list, Context context, int i) {
        super(context, Config.CHANNEL_TYPE.VOD_CHANNEL);
        this.mContext = context;
        this.mChannelList = list;
        this.f13470n = i;
        mEpisodeDlgHelper = null;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { //C3461A
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults results = new Filter.FilterResults();
                ArrayList<ChannelBean> needRemoveBlocks = new ArrayList<>();
                String keyword = constraint.toString().toLowerCase();
                List<ChannelBean> list = VodChannelInstance.mChannelList;// mGroupList.get(-10).channnels;
                //String log = "chlist size " + list.size();
                for (int i = 0; i < list.size(); i++) {
                    //String log = list.get(i).getSearch() + " " + keyword + " " + list.get(i).getSearch().toLowerCase().indexOf(keyword);
                    if (list.get(i).getSearch().toLowerCase().contains(keyword)) {
                        needRemoveBlocks.add(list.get(i));
                    }
                }
                results.count = needRemoveBlocks.size();
                results.values = needRemoveBlocks;
                //String log2 = "results.count" + results.count;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mChannelList = (List<ChannelBean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /* JADX DEBUG: Method merged with bridge method */
    @SuppressLint("RecyclerView")
    @Override
    /* renamed from: a */
    public void onBindViewHolder(@NonNull DashboardChannelViewHolder holder, int position) {
        boolean isSelected = position == this.mSelectedItem;
        boolean isUnSelected = (!isSelected || this.mNextSelectItem < 0) ? false : true;
        holder.itemView.setSelected(isSelected);

        final ChannelBean channel = mChannelList.get(position);
        String name = channel.getName().getInit();
        String big = channel.getLogo().getImage().getBig();
        String childTxt = "" + mChannelList.get(position).getChid();
        if (VodChannelInstance.mFavoriteVodChannelList.contains(childTxt)) {
            //name = "★" + name;
            holder.ivFavorite.setImageResource(R.drawable.ic_star_fav);
        }
        holder.tvName.setText(name);

        if (isUnSelected) {
            holder.ivImage.setBackgroundResource(R.color.background_v3_secondary);
        } else {
            holder.ivImage.setBackgroundResource(0);
        }

        Glide.with(mContext).load(big).placeholder(R.mipmap.loading) //4.12.0 version
                                //.diskCacheStrategy(DiskCacheStrategy.RESULT) //3.8.0 version
                                .error(R.mipmap.load_error).into(holder.ivImage);

        holder.itemView.setOnFocusChangeListener((view, b) -> {
            if (b) {
                holder.itemView.setSelected(true);
                ((View)holder.ivImage).setBackgroundResource(R.color.background_v3_secondary);
            }
            else {
                holder.itemView.setSelected(false);
                ((View)holder.ivImage).setBackgroundResource(0);
            }
        });

        //View$OnLongClickListenerC3496x(channel, position)
        holder.itemView.setOnLongClickListener(view -> {
            int chid = channel.getChid();
            if (VodChannelInstance.mFavoriteVodChannelList.contains("" + chid)) {
                Toast.makeText(getContext(), channel.getName().getInit() + " " +
                        getContext().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
                VodChannelInstance.mFavoriteVodChannelList.remove("" + chid);
                PrefUtils.setPrefStringSet(Config.SP_FAV_VOD_CHANNEL, VodChannelInstance.mFavoriteVodChannelList);
                VodChannelInstance.parseVodChannels();
                if (f13470n == -5) {
                    mChannelList = VodChannelInstance.mGroupList.get(-5).channnels;
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    int k = position - 1;
                    if (position == 0 && mChannelList.size() > 0) {
                        k = 0;
                    }
                    if (k >= 0) {
                        notifyItemChanged(k);
                        setNextSelectItem(k);
                        setSelectedItem(k);
                        notifyItemChanged(k);
                    } else {
                        VodLayout.mMsgHandler.removeMessages(3);
                        VodLayout.mMsgHandler.sendMessage(Message.obtain(VodLayout.mMsgHandler, 3, -5, 0));
                        MainActivity.mMsgHandler.sendEmptyMessage(108);
                        VodLayout.channelType = Config.CHANNEL_TYPE.VOD_GROUP;
                    }
                } else {
                    notifyItemChanged(getSelectedItem());
                    setNextSelectItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                    setSelectedItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                    notifyItemChanged(getSelectedItem());
                }
                holder.ivFavorite.setImageResource(0);
            } else {
                Toast.makeText(getContext(), channel.getName().getInit() + " " + getContext().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                VodChannelInstance.mFavoriteVodChannelList.add("" + chid);
                PrefUtils.setPrefStringSet(Config.SP_FAV_VOD_CHANNEL, VodChannelInstance.mFavoriteVodChannelList);
                VodChannelInstance.parseVodChannels();
                notifyDataSetChanged();
                notifyItemChanged(getSelectedItem());
                setNextSelectItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                setSelectedItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                notifyItemChanged(getSelectedItem());
            }
            VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
            return true;
        });
        String finalName = name;
        holder.itemView.setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3497y(this, holder, name, channel)
            @Override
            public void onClick(View view) {
                notifyItemChanged(getSelectedItem());
                setNextSelectItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                setSelectedItem(getOwnerRecyclerView().getChildLayoutPosition(view));
                notifyItemChanged(getSelectedItem());

                VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                VodLayout.mVodChannelView = holder.itemView;

                showEpisodeDialogWithName(finalName, channel);
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override 
    /* renamed from: b */
    public DashboardChannelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DashboardChannelViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.vod_channel_item, viewGroup, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b 2510*/
    public void showEpisodeDialogWithName(String name, ChannelBean channel) {
        if (channel == null || channel.getSources() == null || channel.getSources().size() < 1) {
            return;
        }
        showEpisodeDialog(channel);
    }

    /* renamed from: a 2512 */
    private void showPasswordDialogForEpisode(final String name, final ChannelBean channel) {
        PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(mContext);
        helper.setClickListener(new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3498z (this, name, channel)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEpisodeDialogWithName(name, channel);
            }
        });
        helper.create().show();
    }

    /* renamed from: a 2511 */
    private void showEpisodeDialog(ChannelBean channel) {
        if (mEpisodeDlgHelper == null) {
            mEpisodeDlgHelper = new EpisodeDialog.Helper(mContext);
        }
        mEpisodeDlgHelper.create(channel).show();
    }

    /* renamed from: a 2514 */
//    private void showWebDialog(String url) {
//        WebViewDialog.Helper helper = new WebViewDialog.Helper(mContext);
//        helper.setUrl(url);
//        helper.create().show();
//    }

    /* renamed from: a 2513 */
    private void m2513a(String name, String url) {
        Message msg = new Message();
        msg.what = 80;
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("name", name);
        bundle.putString("type", (url.contains("tvcar://") ? Config.BS_MODE.BSVOD : Config.BS_MODE.STATIC).name());
        msg.setData(bundle);
        MainActivity.mMsgHandler.sendMessage(msg);
    }

    @Override 
    /* renamed from: a */
    public int getItemCount() {
        return mChannelList.size();
    }
}
