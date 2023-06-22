package com.brazvip.fivetv.beans.vod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class VodChannelBean implements Serializable {
    public String _id;
    public String backdrop;
    public String certificate;
    public int duration;
    public List<Episode> episodes;
    public String genres;
    public int level;
    public String overview;
    public String poster;
    public String releaseDate;
    public boolean restricted;
    public List<String> tags;
    public String title;
    public int vodType;
    public int voteAverage;

    /* loaded from: classes.dex */
    public static class Episode {
        public String address;
        public int duration;
        public int episode;

        /* renamed from: id */
        public int f8661id;
        public String overview;
        public int popularity;
        public int season;
        public String still;
        public List<SubtitlesBean> subtitles;
        public String title;
        public int voteAverage;
        public int voteCount;

        /* loaded from: classes.dex */
        public static class SubtitlesBean {
            public String code;
            public String type = "text";
            public String url;

            public String getCode() {
                return this.code;
            }

            public String getType() {
                return this.type;
            }

            public String getUrl() {
                return this.url;
            }
        }

        public float getDurationInSeconds() {
            return this.duration;
        }

        public List<SubtitlesBean> getSubtitles() {
            List<SubtitlesBean> list = this.subtitles;
            return list == null ? new ArrayList() : list;
        }
    }

    public List<Episode> getEpisodes() {
        if (this.episodes == null) {
            this.episodes = new ArrayList();
        }
        return this.episodes;
    }

    public String getId() {
        return this._id;
    }

    public int getLevel() {
        return this.level;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getPoster() {
        return this.poster;
    }

    public Boolean getRestricted() {
        return Boolean.valueOf(this.restricted);
    }

    public List<String> getTags() {
        if (this.tags == null) {
            this.tags = new ArrayList();
        }
        return this.tags;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getVoteAverage() {
        return Integer.valueOf(this.voteAverage);
    }

    public void setEpisodes(List<Episode> list) {
        if (list != null) {
            this.episodes = list;
        }
    }

    public void setId(String str) {
        this._id = this._id;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void setOverview(String str) {
        this.overview = str;
    }

    public void setPoster(String str) {
        this.poster = str;
    }

    public void setTags(List<String> list) {
        this.tags = list;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
