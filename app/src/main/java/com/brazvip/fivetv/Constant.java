package com.brazvip.fivetv;

public class Constant {
    public static final String PREFS_NAME = "bsprefer";

    public static final boolean OFFLINE_TEST = false;
    public static final boolean LOGIN_BY_LIB = false;

    public static final String PREFS_CHANNEL_INFO = "ftv_pref_channel_info";
    public static final String PREFS_EPG_INFO = "ftv_epg_channel_info";

    public static final String PREFS_VOD_INFO = "ftv_pref_vod_info";

    public static final String APP_ID = "com.brazvip.fivetv";
    public static final String LOGIN_URL = "https://auth1.braz.vip/api/v2/auth";
    public static final String REGISTER_URL = "https://auth1.braz.vip/api/v2/register";
    public static final String DEFAULT_MAIL_SUFFIX = "@p2five.com";

    public static final int MSG_LOGIN_SUCCESS = 1;
    public static final int MSG_LOGIN_FAIL = 2;
    public static final int MSG_CHANNEL_LOADED = 3;
    public static final int MSG_EPG_LOADED = 4;
    public static final int MSG_VOD_LOADED = 5;
    public static final int MSG_PLAYER_LOADED = 10;
    public static final int MSG_PLAYER_REFRESHINFO = 71;
    public static final int MSG_PLAYER_PLAY_VIDEO = 80;
    public static final int MSG_PLAYER_START_PLAYBACK = 81;
    public static final int MSG_PLAYER_STOP = 83;
    public static final int MSG_PLAYER_RESUME = 84;
    public static final int MSG_PLAYER_HIDEPROCESSBAR = 96;
    public static final int MSG_PLAYER_CHECKPLAYER = 201;
    public static final int MSG_LIBTV_SERVICE_CONNECTED = 300;
    public static final int MSG_LIVTV_SERVICE_DISCONNECTED = 301;
    public static final int MSG_LIVTV_SERVICE_BINDING_DIED = 302;
    public static final int MSG_LIVTV_SERVICE_BINDING_NULL = 303;

    public static final int GROUP_FAVORITE = -5;
    public static final int GROUP_PLAYBACK = -4;
    public static final int GROUP_ALL = -3;
}
