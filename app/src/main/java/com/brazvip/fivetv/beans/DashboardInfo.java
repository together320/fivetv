package com.brazvip.fivetv.beans;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DashboardInfo {
    public List<DashBoardGroup> groups = new ArrayList();
    public boolean restrictedAccess;
    public int sid;
    public String title;

    /* loaded from: classes.dex */
    public static class DashBoardGroup {
        public List<Line> lines = new ArrayList();
        public boolean restrictedAccess;
        public int sid;
        public String title;
        public String type;
    }

    /* loaded from: classes.dex */
    public static class Item {
        public String content_id;
        public String content_type;
        public String event_end_at;
        public String event_start_at;
        public String image;
        public boolean restrictedAccess;
        public int sid;
        public String title;
        public String view_end_at;
        public String view_start_at;
    }

    /* loaded from: classes.dex */
    public static class Line {
        public List<Item> items = new ArrayList();
        public int sid;
    }
}
