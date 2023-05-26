package com.brazvip.fivetv;

public class Constant {
    public static final String PREFS_NAME = "bsprefer";

    public static final boolean OFFLINE_TEST = false;

    public static final String PREFS_CHANNEL_INFO = "ftv_pref_channel_info";
    public static final String PREFS_EPG_INFO = "ftv_epg_channel_info";

    public static final String LOGIN_URL = "https://auth1.braz.vip/api/v2/auth";
    public static final String REGISTER_URL = "https://auth1.braz.vip/api/v2/register";
    public static final String DEFAULT_MAIL_SUFFIX = "@p2five.com";

    public static final int MSG_LOGIN_SUCCESS = 1;
    public static final int MSG_LOGIN_FAIL = 2;
    public static final int MSG_CHANNEL_LOADED = 3;
    public static final int MSG_EPG_LOADED = 4;
    public static final int MSG_PLAYER_LOADED = 5;
    public static final int MSG_PLAYER_REFRESHINFO = 71;
    public static final int MSG_PLAYER_START = 80;
    public static final int MSG_PLAYER_PLAY = 81;
    public static final int MSG_PLAYER_RESUME = 84;
    public static final int MSG_PLAYER_HIDEPROCESSBAR = 96;
    public static final int MSG_PLAYER_STOP = 99;
    public static final int MSG_PLAYER_CHECKPLAYER = 201;

    public static final int GROUP_FAVORITE = -3;
    public static final int GROUP_ALL = -2;
}
