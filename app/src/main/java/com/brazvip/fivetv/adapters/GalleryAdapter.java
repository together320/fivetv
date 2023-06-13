package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.dialogs.EpisodeDialog;
import com.brazvip.fivetv.layouts.VodLayout;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context mContext;
    private List<ChannelBean> mChannelList;

    public static EpisodeDialog.Helper mEpisodeDlgHelper;

    public GalleryAdapter(Context context, List<ChannelBean> list) {
        this.mContext = context;
        this.mChannelList = list;
        mEpisodeDlgHelper = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position > 10)
            return;
        final ChannelBean channel = mChannelList.get(position);
        String name = channel.getName().getInit();
        String big = channel.getLogo().getImage().getBig();
        String childTxt = "" + mChannelList.get(position).getChid();

        Glide.with(holder.itemView.getContext())
                .load(big)
                .into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            VodLayout.channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
            VodLayout.mVodChannelView = holder.itemView;

            showEpisodeDialogWithName(name, channel);
        });
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public void showEpisodeDialogWithName(String name, ChannelBean channel) {
        if (channel == null || channel.getSources() == null || channel.getSources().size() < 1) {
            return;
        }
        showEpisodeDialog(channel);
    }

    private void showEpisodeDialog(ChannelBean channel) {
        if (mEpisodeDlgHelper == null) {
            mEpisodeDlgHelper = new EpisodeDialog.Helper(mContext);
        }
        mEpisodeDlgHelper.create(channel).show();
    }
}
