package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.instances.DashboardInstance;
import com.brazvip.fivetv.utils.Utils;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.brazvip.fivetv.view.AlwaysMarqueeTextView;
import p123l2.C2012p;

public class SportAdapter extends CustomItemAdapter<SportAdapter.ViewHolder> {
    private Context mContext;
    private List<DashboardInstance.SportsBean_temp> sportsData;
    private SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh/mm", Locale.getDefault());
    private SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("hh/mm", Locale.getDefault());
    private int gridColumns = 3;

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public C2012p binding;
        public Date end;
        public Date start;

        public ViewHolder(C2012p c2012p) {
            super(c2012p.f7175a);
            this.binding = c2012p;
        }
    }

    public SportAdapter(List<DashboardInstance.SportsBean_temp> list, Context context) {
        this.sportsData = list;
        this.mContext = context;
        this.onKeyListener = new View.OnKeyListener() { // new View$OnKeyListenerC2429e(this, 4);
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return lambda$new$0(view, i, keyEvent);
            }
        };
    }

    public boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        int i2;
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
        } else if (i == 22) {
            return tryMoveSelection(layoutManager, 1);
        } else {
            if (i == 21) {
                return tryMoveSelection(layoutManager, -1);
            }
            if (i == 19) {
                i2 = this.gridColumns * (-1);
            } else if (i != 20) {
                return false;
            } else {
                i2 = this.gridColumns;
            }
            return tryMoveSelection(layoutManager, i2);
        }
    }

    public static void lambda$onBindViewHolder$1(View view) {
    }

    @Override
    public int getItemCount() {
        return this.sportsData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);

        boolean z;
        String str;
        String str2;
        Date parse = null;
        boolean after = false;
        boolean z2 = false;
        if (i == this.mSelectedItem) {
            z = true;
        } else {
            z = false;
        }
        viewHolder.binding.f7185k.setSelected(z);
        DashboardInstance.SportsBean_temp sportsBean_temp = this.sportsData.get(i);
        viewHolder.binding.f7177c.setText(sportsBean_temp.leagueName);
        String str3 = sportsBean_temp.channelName;
        String str4 = "";
        if (str3 == null || str3.trim().isEmpty()) {
            str = "";
        } else {
            str = sportsBean_temp.channelName;
        }
        viewHolder.binding.f7176b.setText(str);
        viewHolder.binding.f7180f.setText(sportsBean_temp.team1Name);
        viewHolder.binding.f7181g.setText(String.valueOf(sportsBean_temp.team1Score));

        Glide.with(this.mContext)
                .load(sportsBean_temp.team1Logo)
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.load_error)
                .into(viewHolder.binding.f7179e);

        viewHolder.binding.f7183i.setText(sportsBean_temp.team2Name);
        viewHolder.binding.f7184j.setText(String.valueOf(sportsBean_temp.team2Score));

        Glide.with(this.mContext)
                .load(sportsBean_temp.team2Logo)
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.load_error)
                .into(viewHolder.binding.f7182h);
        
        try {
            parse = this.jsonDateFormat.parse(sportsBean_temp.matchStartTime);
            Date parse2 = this.jsonDateFormat.parse(sportsBean_temp.matchStartTime);
            viewHolder.start = parse;
            viewHolder.end = parse2;
            after = parse2.after(new Date());
            if (new Date().after(parse) && new Date().before(parse2)) {
                z2 = true;
            }
        } catch (ParseException unused) {
        }
        if (after) {
            str2 = this.mContext.getString(R.string.match_completed);
        } else if (z2) {
            str2 = "timerTask:now-start";
        } else if (parse != null && DateUtils.isToday(parse.getTime())) {
            str2 = this.mContext.getString(R.string.today) + " " + this.timeOnlyFormat.format(parse);
        } else {
            if (parse != null) {
                str2 = this.dateAndTimeFormat.format(parse);
            }
            str2 = "";
        }
        if (str2 != null) {
            str4 = str2;
        }
        viewHolder.binding.f7178d.setText(str4);
        viewHolder.binding.f7175a.setOnClickListener(new View.OnClickListener() { //new ViewOnClickListenerC2434b(1));
            @Override
            public void onClick(View v) {
                SportAdapter.lambda$onBindViewHolder$1(v);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sports_item, viewGroup, false);

        AlwaysMarqueeTextView channelName = (AlwaysMarqueeTextView) Utils.m385i(R.id.channel_name, inflate);
        AlwaysMarqueeTextView leagueName = (AlwaysMarqueeTextView) Utils.m385i(R.id.league_name, inflate);
        TextView matchDateTime = (TextView) Utils.m385i(R.id.match_date_time, inflate);
        ImageView team1Logo = (ImageView) Utils.m385i(R.id.team1_logo, inflate);
        AlwaysMarqueeTextView team1Name = (AlwaysMarqueeTextView) Utils.m385i(R.id.team1_name, inflate);
        TextView team1Score = (TextView) Utils.m385i(R.id.team1_score, inflate);
        ImageView team2Logo = (ImageView) Utils.m385i(R.id.team2_logo, inflate);
        AlwaysMarqueeTextView team2Name = (AlwaysMarqueeTextView) Utils.m385i(R.id.team2_name, inflate);
        TextView team2Score = (TextView) Utils.m385i(R.id.team2_score, inflate);
        RelativeLayout timeLayout = (RelativeLayout) Utils.m385i(R.id.time_layout, inflate);

        return new ViewHolder(new C2012p((ConstraintLayout) inflate, channelName, leagueName, matchDateTime, team1Logo, team1Name, team1Score, team2Logo, team2Name, team2Score, timeLayout));
    }

    public void updateSportsData(List<DashboardInstance.SportsBean_temp> list) {
        if (this.sportsData == null) {
            this.sportsData = new ArrayList();
        }
        for (DashboardInstance.SportsBean_temp sportsBean_temp : list) {
            this.sportsData.remove(sportsBean_temp);
            this.sportsData.add(sportsBean_temp);
        }
    }
}
