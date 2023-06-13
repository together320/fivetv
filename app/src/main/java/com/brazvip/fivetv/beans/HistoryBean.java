package com.brazvip.fivetv.beans;

import java.io.Serializable;
import java.util.Date;
import com.brazvip.fivetv.Config;

/* loaded from: classes.dex */
public class HistoryBean implements Serializable {
    public static final long serialVersionUID = -8734287124637627888L;
    public int chid;
    public Date date;
    public int duration;
    public int lastPosition;
    public String name;
    public String subId;
    public String subTitle;
    public Config.VIDEO_TYPE videoType;
    public String channelId = "";
    public String Season = "";
    public String Episode = "";
}
