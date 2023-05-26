package com.brazvip.fivetv.beans;

import java.util.List;

public class UpdateInfo {
    public int code;
    public String description;
    public int incompatibleVersion;
    public String info;
    public List<String> mkbrokers;
    public String name;
    public ReleaseBean release;

    public ScBean f8660sc;

    public static class ReleaseBean {
        public String changeLog;
        public String channel;
        public String md5;
        public int minSdkVersion;
        public int size;
        public int targetSdkVersion;
        public String updateDate;
        public String url;
        public int versionCode;
        public String versionName;
    }

    public static class ScBean {
        public int sysId;
        public String xDomain;
    }
}
