package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.brazvip.fivetv.R;
import java.util.HashMap;
import java.util.List;
import com.brazvip.fivetv.dialogs.SeriesDialog;


public class SeasonAdapter extends CustomItemAdapter<SeasonAdapter.ViewHolder> {
    private Context context;
    private HashMap<Integer, Integer> episodeCount;
    private List<Integer> seasonNumbers;

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView episodeTxt;
        public final TextView seasonNameTxt;
        public int seasonNumber;

        public ViewHolder(View view) {
            super(view);
            this.seasonNameTxt = (TextView) view.findViewById(R.id.season_name);
            this.episodeTxt = (TextView) view.findViewById(R.id.season_episode_count);
        }
    }

    public SeasonAdapter(List<Integer> list, HashMap<Integer, Integer> hashMap, Context context) {
        this.seasonNumbers = list;
        this.episodeCount = hashMap;
        this.context = context;
    }

    public static void m1732c(final SeasonAdapter seasonAdapter, final ViewHolder viewHolder, final View view) {
        seasonAdapter.lambda$onBindViewHolder$0(viewHolder, view);
    }

    public void lambda$onBindViewHolder$0(ViewHolder viewHolder, View view) {
        if (SeriesDialog.getInstance() != null) {
            SeriesDialog.getInstance().episodeAdapter.showEpisodesForSeason(viewHolder.seasonNumber);
        }
        onItemSelected(viewHolder.getAbsoluteAdapterPosition());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public int getItemCount() {
        return this.seasonNumbers.size();
    }

    @Override // com.brazvip.fivetv.adapter.CustomItemAdapter, androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((SeasonAdapter.ViewHolder)viewHolder, i);
        StringBuilder sb;
        int intValue = this.seasonNumbers.get(i).intValue();
        viewHolder.seasonNumber = intValue;
        String string = this.context.getString(R.string.season_prefix);
        if (intValue < 10) {
            sb = new StringBuilder();
            sb.append(string);
            sb.append(" 0");
        } else {
            sb = new StringBuilder();
            sb.append(string);
            sb.append(" ");
        }
        sb.append(intValue);
        viewHolder.seasonNameTxt.setText(sb.toString());
        String strEpisodeCount = this.context.getString(R.string.episode_count_postfix);
        StringBuilder strEpisodeText = new StringBuilder("");
        strEpisodeText.append(this.episodeCount.get(Integer.valueOf(intValue)));
        strEpisodeText.append(" ");
        strEpisodeText.append(strEpisodeCount);
        viewHolder.episodeTxt.setText(strEpisodeText.toString());
        if (i == 0 && this.mSelectedItem == 0 && SeriesDialog.getInstance() != null) {
            SeriesDialog.getInstance().episodeAdapter.showEpisodesForSeason(viewHolder.seasonNumber);
        }
        viewHolder.itemView.setOnClickListener(new ViewOnClickListener2(this, viewHolder, 3));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.series_season_item, viewGroup, false));
    }

    @Override // com.brazvip.fivetv.adapter.CustomItemAdapter
    public void onItemSelected(int i) {
        super.onItemSelected(i);
        if (SeriesDialog.getInstance() != null) {
            SeriesDialog.getInstance().episodeAdapter.showEpisodesForSeason(this.seasonNumbers.get(i).intValue());
        }
    }
}
