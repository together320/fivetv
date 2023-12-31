package com.brazvip.fivetv.instances;

import android.util.Log;

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

public class DashboardInstance {
    public static String TAG = "DashboardInstance";
    private static DashboardInstance INSTANCE;
    private List<DashboardInfo> dashboardJson = null;
    public Map<String, List<String>> groupL1L2Map = null;
    public Map<String, List<DashboardInfo.Line>> groupL2LinesMap = new LinkedHashMap<>();

    public static class SportsBean_temp {
        public String channelName;

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
            return Objects.hash(this.f8698id, this.team1Name, this.team2Name);
        }
    }

    private DashboardInstance() {
    }

    public static DashboardInstance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DashboardInstance();
        }
        if (INSTANCE.groupL1L2Map == null) {
            INSTANCE.getDashboardCache();
        }
        return INSTANCE;
    }

    public List<SportsBean_temp> getSportsData() {
        return new ArrayList();
    }

    private void levelGrouping(List<DashboardInfo> list) {
        this.groupL1L2Map = new LinkedHashMap<>();
        for (DashboardInfo dashboardInfo : list) {
            List<String> titleList = new ArrayList<>();
            for (DashboardInfo.DashBoardGroup dashBoardGroup : dashboardInfo.groups) {
                titleList.add(dashBoardGroup.title);
                groupL2LinesMap.put(dashBoardGroup.title, dashBoardGroup.lines);
            }
            groupL1L2Map.put(dashboardInfo.title, titleList);
        }
    }

    public boolean getDashboardCache() {
        if (dashboardJson == null) {
            try {
                String strDashboard = LibTvServiceClient.getInstance().getCacheDashboard();
                String dash[] = strDashboard.split("\"items\":");
                for (int i = 0; i < dash.length; i++) {
                    Log.e(TAG, dash[i] + "\"items\":");
                }
                dashboardJson = JSON.parseObject(strDashboard, new TypeReference<List<DashboardInfo>>() { });
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }
        }
        if (dashboardJson != null) {
            levelGrouping(dashboardJson);
            return true;
        }
        return false;
    }
}
