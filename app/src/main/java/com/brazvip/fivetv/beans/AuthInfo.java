package com.brazvip.fivetv.beans;

import java.io.Serializable;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public class AuthInfo implements Serializable {
    public int code;
    public KeysBean keys;
    public String result;
    public ServiceBean service;
    public UserBean user;

    /* compiled from: MyApplication */
    /* loaded from: classes.dex */
    public static class KeysBean {
        public String peer_id;
        public String session_key;
        public String user_id;
    }

    /* compiled from: MyApplication */
    /* loaded from: classes.dex */
    public static class ServiceBean {
        public String auth_url;
        public String auth_url_sdk;
        public String ch_url;
        public String domain_suffix;
        public boolean enabledAppManager;
        public boolean enabledLive;
        public boolean enabledPlayback;
        public boolean enabledVoD;
        public String epg_url;
        public String message_url;
        public String mk_broker;
        public String name;
        public String reseller;
        public String telephone;
        public String token;
        public int type;
        public String update_url;
        public String vod_url;
        public String website;

        public String toString() {
            return "ServiceBean{auth_url='" +
                    this.auth_url +
                    '\'' +
                    ", ch_url='" +
                    this.ch_url +
                    '\'' +
                    ", domain_suffix='" +
                    this.domain_suffix +
                    '\'' +
                    ", epg_url='" +
                    this.epg_url +
                    '\'' +
                    ", update_url='" +
                    this.update_url +
                    '\'' +
                    ", mk_broker='" +
                    this.mk_broker +
                    '\'' +
                    ", name='" +
                    this.name +
                    '\'' +
                    ", reseller='" +
                    this.reseller +
                    '\'' +
                    ", telephone='" +
                    this.telephone +
                    '\'' +
                    ", type=" +
                    this.type +
                    ", website='" +
                    this.website +
                    '\'' +
                    '}';
        }
    }

    /* compiled from: MyApplication */
    /* loaded from: classes.dex */
    public static class UserBean {
        public long EndTime;
        public long StartTime;
        public String reg_time;
        public int user_id;
        public String user_name;
        public int user_status;
    }
}
