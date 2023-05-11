package com.brazvip.fivetv.beans;

import java.io.Serializable;
import java.util.List;

public class EpgBeans implements Serializable {
    public static final long serialVersionUID = -8577710898966539448L;
    public List<EpgBean> epg;
    public boolean hasPlayBack;

    public int id;

    public static class EpgBean {
        public Long endTime;

        public String id;
        public String name;
        public String playbackUrl;
        public Long time;

        public Long getEndTime() {
            return this.endTime;
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getPlaybackUrl() {
            return this.playbackUrl;
        }

        public Long getTime() {
            return this.time;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlaybackUrl(String url) {
            this.playbackUrl = url;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }

    public List<EpgBean> getEpg() {
        return this.epg;
    }

    public int getId() {
        return this.id;
    }

    public boolean isHasPlayBack() {
        return this.hasPlayBack;
    }

    public void setEpg(List<EpgBean> epg) {
        this.epg = epg;
    }

    public void setHasPlayBack(boolean hasPlayBack) {
        this.hasPlayBack = hasPlayBack;
    }

    public void setId(int id) {
        this.id = id;
    }
}
