package com.brazvip.fivetv.bindings;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public final class DashboardBinding {

    public final RelativeLayout dashboard_root;

    public final RecyclerView groupL1_rv;

    public final RecyclerView groupL2_rv;

    public final TextView line_1_placeholder;

    public final ScrollView lines_scroll_view;

    public final RecyclerView new_releases_rv;

    public final LinearLayout others_grid_ll;

    public final RecyclerView sports_rv;

    public DashboardBinding(RelativeLayout dashboard_root,
                            RecyclerView groupL1_rv,
                            RecyclerView groupL2_rv,
                            TextView line_1_placeholder,
                            ScrollView lines_scroll_view,
                            RecyclerView new_releases_rv,
                            LinearLayout others_grid_ll,
                            RecyclerView sports_rv) {
        this.dashboard_root = dashboard_root;
        this.groupL1_rv = groupL1_rv;
        this.groupL2_rv = groupL2_rv;
        this.line_1_placeholder = line_1_placeholder;
        this.lines_scroll_view = lines_scroll_view;
        this.new_releases_rv = new_releases_rv;
        this.others_grid_ll = others_grid_ll;
        this.sports_rv = sports_rv;
    }
}
