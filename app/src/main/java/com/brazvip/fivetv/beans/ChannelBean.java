package com.brazvip.fivetv.beans;

import java.io.Serializable;
import java.util.List;


public class ChannelBean implements Serializable {
    public static final long serialVersionUID = -1553784377666168110L;
    public int chid;
    public long dlEver;
    public List<EpgBeans> epg;

    public String id;
    public int level;
    public LogoBean logo;
    public NameBean name;
    public String search;
    public int sid;
    public List<SourcesBean> sources;
    public List<TagsBean> tags;
    public int type;
    public int epgSameAs = 0;
    public boolean hasPlayBack = false;
    public String description = "";

    public static class LogoBean {

        public int id;
        public ImageBean image;

        public static class ImageBean {
            public String big;
            public String small;

            public String getBig() {
                return this.big;
            }

            public String getSmall() {
                return this.small;
            }

            public void setBig(String big) {
                this.big = big;
            }

            public void setSmall(String small) {
                this.small = small;
            }
        }

        public int getId() {
            return this.id;
        }

        public ImageBean getImage() {
            return this.image;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }
    }

    public static class NameBean {
        public I18nBean i18n;
        public String init;

        public static class I18nBean {

            public String en;

            public String getEn() {
                return this.en;
            }

            public void setEn(String en) {
                this.en = en;
            }
        }

        public I18nBean getI18n() {
            return this.i18n;
        }

        public String getInit() {
            return this.init;
        }

        public void setI18n(I18nBean i18n) {
            this.i18n = i18n;
        }

        public void setInit(String init) {
            this.init = init;
        }
    }

    public static class SourcesBean {
        public String address;
        public int bType;

        public int id;
        public int kbps;
        public String mediaType;
        public int qc;
        public int qs;
        public String subTitle;

        public String getAddress() {
            return this.address;
        }

        public int getBType() {
            return this.bType;
        }

        public int getId() {
            return this.id;
        }

        public int getKbps() {
            return this.kbps;
        }

        public String getMediaType() {
            return this.mediaType;
        }

        public int getQc() {
            return this.qc;
        }

        public int getQs() {
            return this.qs;
        }

        public String getSubTitle() {
            return this.subTitle;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setBType(int BType) {
            this.bType = BType;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setKbps(int kbps) {
            this.kbps = kbps;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public void setQc(int qc) {
            this.qc = qc;
        }

        public void setQs(int qs) {
            this.qs = qs;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
    }

    public static class TagsBean {
        public DescriptionBean description;

        public int id;
        public ImageBean image;
        public NameBean name;
        public boolean restrictedAccess;
        public int type;

        public static class DescriptionBean {
            public String init;

            public String getInit() {
                return this.init;
            }

            public void setInit(String init) {
                this.init = init;
            }
        }

        public static class ImageBean {
            public String big;
            public String small;

            public String getBig() {
                return this.big;
            }

            public String getSmall() {
                return this.small;
            }

            public void setBig(String big) {
                this.big = big;
            }

            public void setSmall(String small) {
                this.small = small;
            }
        }

        public static class NameBean {
            public String init;

            public String getInit() {
                return this.init;
            }

            public void setInit(String init) {
                this.init = init;
            }
        }

        public DescriptionBean getDescription() {
            return this.description;
        }

        public int getId() {
            return this.id;
        }

        public ImageBean getImage() {
            return this.image;
        }

        public NameBean getName() {
            return this.name;
        }

        public int getType() {
            return this.type;
        }

        public boolean isRestrictedAccess() {
            return this.restrictedAccess;
        }

        public void setDescription(DescriptionBean description) {
            this.description = description;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public void setName(NameBean name) {
            this.name = name;
        }

        public void setRestrictedAccess(boolean restrictedAccess) {
            this.restrictedAccess = restrictedAccess;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public int getChid() {
        return this.chid;
    }

    public String getDescription() {
        return this.description;
    }

    public long getDlEver() {
        return this.dlEver;
    }

    public List<?> getEpg() {
        return this.epg;
    }

    public int getEpgSameAs() {
        return this.epgSameAs;
    }

    public String getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public LogoBean getLogo() {
        return this.logo;
    }

    public NameBean getName() {
        return this.name;
    }

    public String getSearch() {
        if (search != null && !search.equals("")) {
            return this.search;
        }
        return this.name.getInit();
    }

    public int getSid() {
        return this.sid;
    }

    public List<SourcesBean> getSources() {
        return this.sources;
    }

    public List<TagsBean> getTags() {
        return this.tags;
    }

    public int getType() {
        return this.type;
    }

    public boolean isHasPlayBack() {
        return this.hasPlayBack;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDlEver(long dlEver) {
        this.dlEver = dlEver;
    }

    public void setEpg(List<EpgBeans> epg) {
        this.epg = epg;
    }

    public void setEpgSameAs(int epgSameAs) {
        this.epgSameAs = epgSameAs;
    }

    public void setHasPlayBack(boolean hasPlayBack) {
        this.hasPlayBack = hasPlayBack;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLogo(LogoBean logo) {
        this.logo = logo;
    }

    public void setName(NameBean name) {
        this.name = name;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setSources(List<SourcesBean> sources) {
        this.sources = sources;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return "ChannelBean{chid=" + chid + ", id='" + id + '\'' + ", logo=" + logo +
                            ", name=" + name + ", sid=" + sid + ", type=" + type +
                            ", epg=" + epg + ", sources=" + sources + ", tags=" + tags + '}';
    }
}
