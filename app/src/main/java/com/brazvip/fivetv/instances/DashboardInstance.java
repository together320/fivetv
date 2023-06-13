package com.brazvip.fivetv.instances;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.brazvip.fivetv.LibTvServiceClient;
import com.brazvip.fivetv.beans.DashboardInfo;

/* loaded from: classes.dex */
public class DashboardInstance {
    private static DashboardInstance INSTANCE;
    private List<DashboardInfo> dashboardJson = null;
    public Map<String, List<String>> groupL1L2Map = new LinkedHashMap();
    public Map<String, List<DashboardInfo.Line>> groupL2LinesMap = new LinkedHashMap();

    /* loaded from: classes.dex */
    public static class SportsBean_temp {
        public String channelName;

        /* renamed from: id */
        public int f8698id;
        public String leagueName;
        public String matchEndTime;
        public String matchStartTime;
        public List<String> tags;
        public String team1Logo;
        public String team1Name;
        public int team1Score;
        public String team2Logo;
        public String team2Name;
        public int team2Score;
        public boolean waiting;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SportsBean_temp sportsBean_temp = (SportsBean_temp) obj;
            return this.f8698id == sportsBean_temp.f8698id && Objects.equals(this.team1Name, sportsBean_temp.team1Name) && Objects.equals(this.team2Name, sportsBean_temp.team2Name);
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.f8698id), this.team1Name, this.team2Name);
        }
    }

    private DashboardInstance() {
    }

    public static DashboardInstance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DashboardInstance();
        }
        DashboardInstance DashboardInstance = INSTANCE;
        if (DashboardInstance.groupL1L2Map == null) {
            DashboardInstance.getDashboardCache();
        }
        return INSTANCE;
    }

    public static List<SportsBean_temp> getSportsData() {
        return new ArrayList();
    }

    private void levelGrouping(List<DashboardInfo> list) {
        this.groupL1L2Map = new LinkedHashMap();
        for (DashboardInfo dashboardInfo : list) {
            ArrayList arrayList = new ArrayList();
            for (DashboardInfo.DashBoardGroup dashBoardGroup : dashboardInfo.groups) {
                arrayList.add(dashBoardGroup.title);
                this.groupL2LinesMap.put(dashBoardGroup.title, dashBoardGroup.lines);
            }
            this.groupL1L2Map.put(dashboardInfo.title, arrayList);
        }
    }

    public boolean getDashboardCache() {
        if (this.dashboardJson == null) {
            try {
                this.dashboardJson = (List) JSON.parseObject(LibTvServiceClient.getInstance().getCacheDashboard(), new TypeReference<List<DashboardInfo>>() { // from class: org.sopcast.android.p220b.DashboardInstance.1
                }, new Feature[0]);
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }
        }
        List<DashboardInfo> list = this.dashboardJson;
        if (list != null) {
            levelGrouping(list);
            return true;
        }
        return false;
    }
}
