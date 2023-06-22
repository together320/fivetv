package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.utils.StringUtil;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import java.util.List;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.dialogs.VodDialog;
import com.brazvip.fivetv.instances.HistoryInstance;
import com.brazvip.fivetv.utils.EpisodeUtil;


public class EpisodeAdapter extends CustomItemAdapter<EpisodeAdapter.EpisodeViewHolder> {
    private static final String TAG = "EpisodeAdapter";
    private VodChannelBean channel;
    private String channelId;
    private Context mContext;
    private String name;
    private List<VodChannelBean.Episode> sourcesBeanList;

    
    public class EpisodeViewHolder extends RecyclerView.ViewHolder {
        public TextView episodeDescription;
        public ImageView episodeImage;
        public TextView episodeName;
        public ProgressBar episodeProgress;

        public EpisodeViewHolder(View view) {
            super(view);
            this.episodeImage = (ImageView) view.findViewById(R.id.episode_image);
            this.episodeName = (TextView) view.findViewById(R.id.episode_name);
            this.episodeDescription = (TextView) view.findViewById(R.id.episode_description);
            this.episodeProgress = (ProgressBar) view.findViewById(R.id.episode_progress);
        }
    }

    public EpisodeAdapter(VodChannelBean vodChannelBean, Context context, Config.MenuType menuType) {
        this.mContext = context;
        this.name = vodChannelBean.getTitle();
        this.channelId = vodChannelBean.getId();
        this.channel = vodChannelBean;
        this.menuType = menuType;
        ArrayList arrayList = new ArrayList();
        this.sourcesBeanList = arrayList;
        arrayList.addAll(vodChannelBean.getEpisodes());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public int getItemCount() {
        return this.sourcesBeanList.size();
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override // com.brazvip.fivetv.adapter.CustomItemAdapter, androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public void onBindViewHolder(final EpisodeViewHolder episodeViewHolder, int i) {
        String str;
        super.onBindViewHolder(episodeViewHolder, i);
        final VodChannelBean.Episode episode = this.sourcesBeanList.get(i);
        if (episode.episode != 0) {
            str = StringUtil.m6214j(new StringBuilder(), episode.episode, " - ");
        } else {
            str = "";
        }
        StringBuilder m6212l = StringUtil.m6212l(str);
        m6212l.append(episode.title);
        String sb = m6212l.toString();
        if (this.sourcesBeanList.size() == 1) {
            sb = this.mContext.getString(R.string.Play);
        }
        episodeViewHolder.episodeName.setText(sb);
        episodeViewHolder.episodeDescription.setText(episode.overview);
        HistoryBean GetLastHistory = HistoryInstance.GetLastHistory(this.channelId, String.valueOf(episode.id));
        int i2 = 0;
        int i3 = (GetLastHistory == null || (i3 = GetLastHistory.lastPosition) <= 0) ? 0 : 0;
        if (i3 > 0 && episode.getDurationInSeconds() > 0.0f) {
            i2 = (int) ((i3 / 10) / episode.getDurationInSeconds());
        }
        episodeViewHolder.episodeProgress.setProgress(i2);
        episodeViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.brazvip.fivetv.adapter.EpisodeAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String str2 = episode.address;
                if (str2 != null && !str2.equals("")) {
                    String m6214j = StringUtil.m6214j(new StringBuilder(), episode.season, "");
                    String m6214j2 = StringUtil.m6214j(new StringBuilder(), episode.episode, "");
                    EpisodeAdapter episodeAdapter = EpisodeAdapter.this;
                    VodChannelBean.Episode episode2 = episode;
                    episodeAdapter.requestVideoPlayback(episode2.title, String.valueOf(episode2.id), str2, m6214j, m6214j2, false);
                }
                EpisodeAdapter.this.onItemSelected(episodeViewHolder.getAbsoluteAdapterPosition());
            }
        });
        try {
            Glide.with(this.mContext)
                    .load(episode.still)
                    .placeholder(R.drawable.loading_landscape)
                    //.load(DiskCacheStrategy.DATA)
                    .error(R.drawable.load_error_landscape)
                    .into(episodeViewHolder.episodeImage);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public EpisodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new EpisodeViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.series_episode_item, viewGroup, false));
    }

    public final void requestVideoPlayback(String str, String str2, String str3, String str4, String str5, boolean z) {
        Message message = new Message();
        message.what = 80;
        Bundle bundle = new Bundle();
        bundle.putString(Progress.URL, str3);
        bundle.putString("chid", this.channelId);
        bundle.putString(SerializableCookie.NAME, this.name);
        bundle.putString("subId", str2);
        bundle.putString("subTitle", str);
        bundle.putString("season", str4);
        bundle.putString("episode", str5);
        bundle.putBoolean("restricted", z);
        bundle.putString("type", (str3.contains("tvcar://") ? Config.VIDEO_TYPE.BSVOD : Config.VIDEO_TYPE.f8646d).name());
        bundle.putString("menuType", this.menuType.name());
        message.setData(bundle);
        //SopCast.handler.sendMessage(message);
        VodDialog.dismissAll();
    }

    public void showEpisodesForSeason(int i) {
        this.sourcesBeanList.clear();
        for (VodChannelBean.Episode episode : this.channel.getEpisodes()) {
            int i2 = episode.season;
            if ((i2 != 0 && i2 == i) || (i2 == 0 && EpisodeUtil.parseSeasonFromEpisode(episode) == i)) {
                this.sourcesBeanList.add(episode);
            }
        }
        this.mSelectedItem = 0;
        notifyDataSetChanged();
    }
}
