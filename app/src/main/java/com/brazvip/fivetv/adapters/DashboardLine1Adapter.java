package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.beans.DashboardInfo;

public class DashboardLine1Adapter extends DashboardLineAdapter {
    public DashboardLine1Adapter(DashboardInfo.Line line, Context context, NavigationListener navigationListener, int i, FragmentManager FragmentManager) {
        super(line, context, navigationListener, i, FragmentManager);
    }

    @Override
    public DashboardLineAdapter.LineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DashboardLineAdapter.LineViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.dashboard_line_1_item, viewGroup, false));
    }
}
