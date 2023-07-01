package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.layouts.HistoryLayout;
import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.utils.AutoUtils;

public class HistoryAdapter extends HistoryRecyclerViewAdapter<HistoryAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "HistoryAdapter";
    private Context context;
    private SimpleDateFormat dateFormat;
    private List<HistoryBean> history;
    private Config.VIDEO_TYPE videoType;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView historyName;
        public TextView programNow;
        public TextView visitAt;

        public ViewHolder(HistoryAdapter historyAdapter, View view) {
            super(view);
            this.historyName = (TextView) view.findViewById(R.id.history_name);
            this.programNow = (TextView) view.findViewById(R.id.program_now);
            this.visitAt = (TextView) view.findViewById(R.id.visit_at);
            AutoUtils.auto(view, 3, 3);
        }
    }

    public HistoryAdapter(List<HistoryBean> list, Config.VIDEO_TYPE video_type, Context context) {
        super(context, video_type);
        this.dateFormat = new SimpleDateFormat("d MMM HH:mm", Locale.getDefault());
        this.context = context;
        this.videoType = video_type;
        this.history = list;
        getFilter().filter(null);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                VodChannelBean fullChannelBean;
                Filter.FilterResults filterResults = new Filter.FilterResults();
                List<HistoryBean> historyList = new ArrayList();
                if (HistoryAdapter.this.history != null) {
                    for (int i = 0; i < HistoryAdapter.this.history.size(); i++) {
                        HistoryBean historyBean = (HistoryBean) HistoryAdapter.this.history.get(i);
                        Config.VIDEO_TYPE video_type = historyBean.videoType;
                        if (video_type == Config.VIDEO_TYPE.BSLIVE) {
                            if (ChannelInstance.liveChannels != null) {
                                if (ChannelInstance.liveChannels.get(historyBean.chid) == null) {
                                }
                                historyList.add(historyBean);
                            }
                        } else {
                            if (video_type == Config.VIDEO_TYPE.PLAYBACK) {
                                if (ChannelInstance.liveChannels != null) {
                                    ChannelBean channelBean = ChannelInstance.liveChannels.get(historyBean.chid);
                                    List<EpgBeans.EpgBean> list = EPGInstance.liveEpgs.get(historyBean.chid);
                                    if (channelBean != null && list != null) {
                                        while (true) {
                                            for (EpgBeans.EpgBean epgBean : list) {
                                                if (epgBean.getId().equals(historyBean.subId)) {
                                                    historyList.add(historyBean);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (video_type == Config.VIDEO_TYPE.BSVOD && (fullChannelBean = VodChannelInstance.getFullChannelBean(historyBean.channelId)) != null) {
                                    Iterator<VodChannelBean.Episode> it2 = fullChannelBean.getEpisodes().iterator();
                                    while (true) {
                                        if (!it2.hasNext()) {
                                            break;
                                        }
                                        VodChannelBean.Episode next = it2.next();
                                        if (next.id == Integer.parseInt(historyBean.subId)) {
                                            historyList.add(historyBean);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                filterResults.count = historyList.size();
                filterResults.values = historyList;
                return filterResults;
            }

            @Override // android.widget.Filter
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                history = (List) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override // androidx.recyclerview.widget.AbstractC0409i0
    public int getItemCount() {
        List<HistoryBean> list = this.history;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int backgroundResource) {
        this.getNextSelectedItem();
        boolean b = true;
        final boolean selected = backgroundResource == mSelectedItem;
        if (!selected || this.getNextSelectedItem() < 0) {
            b = false;
        }
        viewHolder.itemView.setSelected(selected);
        final HistoryBean historyBean = this.history.get(backgroundResource);
        viewHolder.visitAt.setText((CharSequence)this.dateFormat.format(historyBean.date));
        viewHolder.historyName.setText((CharSequence)historyBean.name);
        ChannelBean channelBean = null;
        if (ChannelInstance.liveChannels != null) {
            channelBean = ChannelInstance.liveChannels.get(historyBean.chid);
        }
        Label_0258: {
            TextView textView;
            String text;
            if (historyBean.videoType == Config.VIDEO_TYPE.BSLIVE && channelBean != null) {
                if (channelBean.getEpgSameAs() <= 0) {
                    viewHolder.programNow.setText((CharSequence)EPGInstance.getNameById(historyBean.chid));
                    break Label_0258;
                }
                textView = viewHolder.programNow;
                text = EPGInstance.getNameById(channelBean.getEpgSameAs());
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(Utils.millisToTimeString((long)historyBean.lastPosition));
                sb.append(" ");
                sb.append(historyBean.subTitle);
                text = sb.toString();
                textView = viewHolder.programNow;
            }
            textView.setText((CharSequence)text);
        }

        if (b) {
            viewHolder.itemView.setBackgroundResource(R.drawable.history_item_focused_bg);
        }
        else {
            viewHolder.itemView.setBackgroundResource(R.drawable.history_item_bg);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                notifyItemChanged(mSelectedItem);
                nextSelectItem = recyclerView.getChildLayoutPosition(view);
                mSelectedItem = recyclerView.getChildLayoutPosition(view);
                notifyItemChanged(mSelectedItem);
                final Config.VIDEO_TYPE videoType = historyBean.videoType;
                if (videoType == Config.VIDEO_TYPE.BSLIVE && ChannelInstance.liveChannels != null) {
                    final ChannelBean channelBean = ChannelInstance.liveChannels.get(historyBean.chid);
                    if (channelBean != null) {
                        final Message message = new Message();
                        message.what = Constant.MSG_PLAYER_PLAY_VIDEO;
                        final Bundle data = new Bundle();
                        data.putString("chid", String.valueOf(channelBean.getChid()));
                        data.putString("url", ((ChannelBean.SourcesBean)channelBean.getSources().get(0)).getAddress());
                        String s;
                        if (channelBean.getSid() > 0) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(channelBean.getSid());
                            sb.append(".");
                            sb.append(channelBean.getName().getInit());
                            s = sb.toString();
                        }
                        else {
                            s = channelBean.getName().getInit();
                        }
                        data.putString("name", s);
                        data.putString("subId", "");
                        data.putString("subTitle", "");
                        data.putString("type", Config.VIDEO_TYPE.BSLIVE.name());
                        data.putString("menuType", Config.MenuType.HISTORY.name());
                        message.setData(data);
                        MainActivity.handler.sendMessage(message);
                        HistoryLayout.lastFocusVideoType = Config.VIDEO_TYPE.BSLIVE;
                    }
                }
                else {
                    Config.VIDEO_TYPE lastFocusVideoType = null;
                    Label_0818: {
                        if (historyBean.videoType == Config.VIDEO_TYPE.PLAYBACK && ChannelInstance.liveChannels != null) {
                            final ChannelBean channelBean2 = ChannelInstance.liveChannels.get(historyBean.chid);
                            List list;
                            if ((list = (List)EPGInstance.liveEpgs.get(historyBean.chid)) == null) {
                                list = new ArrayList();
                            }
                            while (true) {
                                for (final Object o : list) {
                                    if (((EpgBeans.EpgBean)o).getId().equals(historyBean.subId)) {
                                        if (channelBean2 != null && o != null) {
                                            final Message message2 = new Message();
                                            message2.what = Constant.MSG_PLAYER_PLAY_VIDEO;
                                            final Bundle data2 = new Bundle();
                                            data2.putString("chid", String.valueOf(channelBean2.getChid()));
                                            data2.putString("subId", historyBean.subId);
                                            data2.putString("url", ((EpgBeans.EpgBean)o).getPlaybackUrl());
                                            data2.putString("name", channelBean2.getName().getInit());
                                            data2.putString("subTitle", ((EpgBeans.EpgBean)o).getName());
                                            data2.putString("type", Config.VIDEO_TYPE.PLAYBACK.name());
                                            data2.putBoolean("restricted", false);
                                            data2.putString("menuType", Config.MenuType.HISTORY.name());
                                            message2.setData(data2);
                                            MainActivity.handler.sendMessage(message2);
                                            lastFocusVideoType = Config.VIDEO_TYPE.BSVOD;
                                            break Label_0818;
                                        }
                                        return;
                                    }
                                }
                                Object o = null;
                                continue;
                            }
                        }
                        final HistoryBean value = historyBean;
                        if (value.videoType != Config.VIDEO_TYPE.BSVOD) {
                            return;
                        }
                        final VodChannelBean fullChannelBean = VodChannelInstance.getFullChannelBean(value.channelId);
                        VodChannelBean.Episode episode = null;
                        Label_0653: {
                            if (fullChannelBean != null) {
                                for (final VodChannelBean.Episode episodeBean : fullChannelBean.getEpisodes()) {
                                    if (episodeBean.id == Integer.parseInt(historyBean.subId)) {
                                        episode = episodeBean;
                                        break Label_0653;
                                    }
                                }
                            }
                            episode = null;
                        }
                        if (episode == null) {
                            return;
                        }
                        final Message message3 = new Message();
                        message3.what = 80;
                        final Bundle data3 = new Bundle();
                        data3.putString("url", episode.address);
                        data3.putString("chid", fullChannelBean.getId());
                        data3.putString("name", fullChannelBean.getTitle());
                        data3.putString("subId", historyBean.subId);
                        data3.putString("season", historyBean.Season);
                        data3.putString("episode", historyBean.Episode);
                        data3.putString("subTitle", episode.title);
                        lastFocusVideoType = Config.VIDEO_TYPE.BSVOD;
                        data3.putString("type", lastFocusVideoType.name());
                        data3.putBoolean("restricted", false);
                        data3.putString("menuType", Config.MenuType.HISTORY.name());
                        message3.setData(data3);
                        MainActivity.handler.sendMessage(message3);
                    }
                    HistoryLayout.lastFocusVideoType = lastFocusVideoType;
                }
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override // androidx.recyclerview.widget.AbstractC0409i0
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.history_item, viewGroup, false));
    }
}
