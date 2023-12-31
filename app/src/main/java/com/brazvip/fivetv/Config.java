package com.brazvip.fivetv;

import android.os.Build;
import android.util.DisplayMetrics;

import com.brazvip.fivetv.utils.Utils;
import com.phoenix.libtv.Libtv;
import com.phoenix.libtv.service.LibTvService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Config {

    public static String DEFAULT_CHOOSED_AUDIO_LANG = "DEFAULT_CHOOSED_AUDIO_LANG";
    public static String DEFAULT_CHOOSED_LANG = "DEFAULT_CHOOSED_LANG";

    public static int DEFAULT_PLAYER = 1;
    public static boolean enableVodFragment = true;
    public static boolean showDownloadRate = true;
    public static boolean isPlayback = true;
    public static int f8899S = 5;
    public static boolean f8896P = true;
    public static boolean FLAG_PLAYBACK_ENABLE = true;
    public static boolean FLAG_FAVORITE_INIT = true;

    public static String crashReportAppChannel = "official";

    public static boolean isEPGAssendingSort = false;

    public static boolean f8909g = false;

    public static boolean f8924v = true;

    public static boolean isEpgReverseOrder = false;
    public static boolean f8907e = false;
    public static boolean f8900T = false;
    public static String socketAddr = "";

    public static boolean LoginByLib = true;

    public static int backToExitMinPress = 2;

    /* renamed from: A */
    public static final int f13314A = 81;

    /* renamed from: Aa */
    public static final int f13315Aa = -10;

    /* renamed from: B */
    public static final int f13316B = 82;

    /* renamed from: Ba */
    public static final int f13317Ba = -11;

    /* renamed from: C */
    public static final int f13318C = 83;

    /* renamed from: Ca */
    public static final int f13319Ca = -12;

    /* renamed from: D */
    public static final int f13320D = 84;

    /* renamed from: Da */
    public static final int f13321Da = -20;

    /* renamed from: E */
    public static final int f13322E = 93;

    /* renamed from: Ea */
    public static final int f13323Ea = -20;

    /* renamed from: F */
    public static final int f13324F = 94;

    /* renamed from: Fa */
    public static int f13325Fa = 0;

    /* renamed from: G */
    public static final int f13326G = 95;

    /* renamed from: Ga */
    public static int f13327Ga = -1;

    /* renamed from: H */
    public static final int f13328H = 96;

    /* renamed from: Ha */
    public static int f13329Ha = -2;

    /* renamed from: I */
    public static final int f13330I = 97;

    /* renamed from: Ia */
    public static int f13331Ia = -3;

    /* renamed from: J */
    public static final int f13332J = 98;

    /* renamed from: Ja */
    public static int f13333Ja = -4;

    /* renamed from: K */
    public static final int f13334K = 99;

    /* renamed from: Ka */
    public static final int f13335Ka = 0;

    /* renamed from: L */
    public static final int f13336L = 100;

    /* renamed from: La */
    public static final int f13337La = 1;

    /* renamed from: M */
    public static final int f13338M = 101;

    /* renamed from: Ma */
    public static Integer[] f13339Ma = {0, 1};

    /* renamed from: N */
    public static final int f13340N = 105;

    /* renamed from: Na */
    public static final int f13341Na = 100;

    /* renamed from: O */
    public static final int f13342O = 106;

    /* renamed from: Oa */
    public static final int f13343Oa = 104;

    /* renamed from: P */
    public static final int f13344P = 107;

    /* renamed from: Pa */
    public static final int f13345Pa = -5;

    /* renamed from: Q */
    public static final int f13346Q = 108;

    /* renamed from: Qa */
    public static final int f13347Qa = -4;

    /* renamed from: R */
    public static final int f13348R = 110;

    /* renamed from: Ra */
    public static final int f13349Ra = -10;

    /* renamed from: S */
    public static final int f13350S = 111;

    /* renamed from: Sa */
    public static final int f13351Sa = 1;

    /* renamed from: T */
    public static final int f13352T = 112;

    /* renamed from: Ta */
    public static final int f13353Ta = 2;

    /* renamed from: U */
    public static final int f13354U = 200;

    /* renamed from: Ua */
    public static final int f13355Ua = 3;

    /* renamed from: V */
    public static final int f13356V = 201;

    /* renamed from: Va 13357 */
    public static HashMap<Integer, TAB_MODE> mTabModesMap = null;

    /* renamed from: W */
    public static final int f13358W = 202;

    /* renamed from: Wa */
    public static final int f13359Wa = 1;

    /* renamed from: X */
    public static final int f13360X = 250;

    /* renamed from: Xa */
    public static final int f13361Xa = 2;

    /* renamed from: Y */
    public static final int f13362Y = 1000;

    /* renamed from: Z */
    public static final int f13363Z = 9999;

    /* renamed from: a */
    public static final int f13364a = 7;

    /* renamed from: aa 13365 */
    public static final int DEF_LOGIN_STATE = -65535;

    /* renamed from: b */
    public static final int f13366b = 8;

    /* renamed from: ba */
    public static final int f13367ba = 5000;

    /* renamed from: c */
    public static final int f13368c = 9;

    /* renamed from: ca */
    public static final int f13369ca = 60000;

    /* renamed from: d */
    public static final int f13370d = 10;

    /* renamed from: da */
    public static final String f13371da = "sc";

    /* renamed from: e */
    public static final int f13372e = 11;

    /* renamed from: ea */
    public static final String f13373ea = "ap";

    /* renamed from: f */
    public static final int f13374f = 12;

    /* renamed from: fa 13375 */
    public static final String PREFS_NAME = "bsprefer";

    /* renamed from: g */
    public static final int f13376g = 13;

    /* renamed from: ga 13377 */
    public static String KEY_LOGIN_STATE = "LOGIN_STATE";

    /* renamed from: h */
    public static final int f13378h = 15;

    /* renamed from: ha 13379 */
    public static String LOGIN_TYPE = "LOGIN_TYPE";

    /* renamed from: i */
    public static final int f13380i = 16;

    /* renamed from: ia */
    public static final int f13381ia = 2;

    /* renamed from: j */
    public static final int f13382j = 18;

    /* renamed from: ja */
    public static final int f13383ja = 1;

    /* renamed from: k */
    public static final int f13384k = 19;

    /* renamed from: ka 13385 */
    public static String DEVICE_ID = "DEVICE_ID";

    /* renamed from: l */
    public static final int f13386l = 20;

    /* renamed from: la 13387 */
    public static String USERNAME = "USERNAME";

    /* renamed from: m */
    public static final int f13388m = 21;

    /* renamed from: ma */
    public static String PASSWORD = "PASSWORD";
    public static String defaultPassword = "1321";


    /* renamed from: n */
    public static final int f13390n = 22;

    public static String HASH_USERNAME = "HASH_USERNAME";
    public static String HASH_PASSWORD = "HASH_PASSWORD";

    public static int f8915m = 43200000;
    public static int f8916n = 43200000;
    public static int f8917o = 7200000;
    public static int f8918p = 1209600000;
    public static int restrictedGroupMinPress = 2;
    public static int maxVodColumns = 7;
    public static int gridSpanCount = 5;
    public static int maxAppColumns = 8;

    public static int LOGIN_OK = 0;
    public static int LOGIN_FAIL = -11;
    public static int LOGIN_DISABLED_OR_EXPIRED = -12;

    /* renamed from: na 13391 */
    public static final String SP_PLAYER = "SP_PLAYER";

    /* renamed from: o */
    public static final int f13392o = 23;

    /* renamed from: oa */
    public static final int f13393oa = 0;

    /* renamed from: p */
    public static final int f13394p = 30;

    /* renamed from: pa */
    public static final int f13395pa = 1;

    /* renamed from: q */
    public static final int f13396q = 31;

    /* renamed from: qa 13397 */
    public static final String VOD_MEDIA_TYPE = "VOD_MEDIA_TYPE";

    /* renamed from: r */
    public static final int f13398r = 32;

    /* renamed from: ra 13399 */
    public static final String VOD_CONN_TYPE = "VOD_CONN_TYPE";

    /* renamed from: s */
    public static final int f13400s = 50;

    /* renamed from: sa 13401 */
    public static final String SERVER = "SERVER";

    /* renamed from: t */
    public static final int f13402t = 651;

    /* renamed from: ta 13403 */
    public static final String SP_IS_AUTO_START = "SP_IS_AUTO_START";

    /* renamed from: u */
    public static final int f13404u = 60;

    /* renamed from: ua 13405 */
    public static final String SP_FAV_LIVE_CHANNEL = "SP_FAV_LIVE_CHANNEL";

    /* renamed from: v */
    public static final int f13406v = 61;

    /* renamed from: va 13407 */
    public static final String SP_FAV_VOD_CHANNEL = "SP_FAV_VOD_CHANNEL";

    /* renamed from: w */
    public static final int f13408w = 71;

    /* renamed from: wa 13409 */
    public static final String EPG = "EPG";

    /* renamed from: x */
    public static final int f13410x = 72;

    /* renamed from: xa */
    public static final int f13411xa = 0;

    /* renamed from: y */
    public static final int f13412y = 75;

    /* renamed from: ya */
    public static final int f13413ya = -1;

    /* renamed from: z */
    public static final int f13414z = 80;

    /* renamed from: za */
    public static final int f13415za = -2;


    public enum MenuType {
        f8632a,
        LIVE,
        f8634c,
        f8635d,
        VOD,
        HISTORY,
        APPS,
        DASHBOARD
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a$a 3456 */
    /* loaded from: classes.dex */
    public enum ErrorTypes {
        Err_100("DNS error", -100),
        Err_101("Can not bind to service port", -101),
        Err_102("Service is not available", -102),
        Err_103("Socket error", -103),
        Err_104("Channel is off line now", -104),
        Err_105("Need authentication", -105),
        Err_106("No memory", -106),
        Err_107("Time error", -107),
        Err_108("No peers", -108),
        Err_109("No buffer", -109),
        Err_110("Open stream timeout", -110),
        Err_111("Stream redirect", -111),
        Err_112("Removed Channel", -112),
        Err_120("Invalid certification", -120),
        Err_130("Invalid address", -130),
        Err_201("Auth socket error", -201),
        Err_202("No auth address", -202),
        Err_203("Auth server error", -203),
        Err_204("Auth message error", -204),
        Err_205("Invalid username or password", -205),
        Err_206("Auth incompatible", -206),
        Err_210("Multiple user login", -210),
        Err_211("Wrong access code", -211),
        Err_301("MK socket error", -301),
        Err_302("MK service timeout", -302);
        

        /* renamed from: A 13442 */
        public String text;

        /* renamed from: B 13443 */
        public int value;

        ErrorTypes(String text, int value) {
            this.text = text;
            this.value = value;
        }
    }

    public enum Errors {
        f8598a("DNS error", -100),
        f8599b("Can not bind to service port", -101),
        f8600c("Service is not available", -102),
        f8601d("Socket error", -103),
        CHANNEL_OFFLINE("Channel is off line now", -104),
        NEED_AUTH("Need authentication", -105),
        f8604g("No memory", -106),
        f8605h("Time error", -107),
        f8606i("No peers", -108),
        f8607j("No buffer", -109),
        f8608k("Open stream timeout", -110),
        f8609l("Stream redirect", -111),
        f8610m("Removed Channel", -112),
        f8611n("Invalid certification", -120),
        f8612o("Invalid address", -130),
        f8613p("Auth socket error", -201),
        f8614q("No auth address", -202),
        AUTH_SERVER_ERROR("Auth server error", -203),
        f8616s("Auth message error", -204),
        f8617t("Invalid username or password", -205),
        f8618u("Auth incompatible", -206),
        MULTIPLE_LOGIN("Multiple user login", -210),
        f8620w("Wrong access code", -211),
        f8621x("MK socket error", -301),
        f8622y("MK service timeout", -302);

        public final int code;

        Errors(String str, int i) {
            this.code = i;
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a$b 3457 */
    /* loaded from: classes.dex */
    public enum TAB_MODE {
        LIVE,
        VOD,
        USER,
        APPS,
        SETTING
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a$c 3458 */
    /* loaded from: classes.dex */
    public enum CHANNEL_TYPE {
        GROUP,
        CHANNEL,
        EPG,
        VOD_GROUP,
        VOD_CHANNEL,
        APPS
    }

    public enum VIDEO_TYPE {
        BSLIVE,
        PLAYBACK,
        BSVOD,
        f8646d
    }

    /* loaded from: classes.dex */
    public enum VType {
        M3U8,
        TS
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a$e 3460 */
    /* loaded from: classes.dex */
    public enum BS_MODE {
        BSLIVE,
        BSPALYBACK,
        BSVOD,
        STATIC
    }

    /* renamed from: a 2517 */
    public static void m2517a(boolean z, boolean runTVCarService, boolean z3, boolean z4, boolean z5) {
        mTabModesMap = new HashMap<>();
        int i = 0;
        if (z) {
            f13325Fa = 0;
            mTabModesMap.put(0, TAB_MODE.LIVE);
            i = 1;
        }
        if (runTVCarService) {
            f13327Ga = i;
            mTabModesMap.put(i, TAB_MODE.VOD);
            i++;
        }
        if (z3) {
            f13329Ha = i;
            mTabModesMap.put(i, TAB_MODE.USER);
            i++;
        }
        if (z4) {
            f13331Ia = i;
            mTabModesMap.put(i, TAB_MODE.APPS);
            i++;
        }
        if (z5) {
            f13333Ja = i;
            mTabModesMap.put(i, TAB_MODE.SETTING);
        }
        //String text = "" + f13325Fa + " " + f13327Ga + " " + f13329Ha + " " + f13331Ia + " " + f13333Ja;
    }

    public static String domain = "@sp1.com";

    public static DisplayMetrics displayMetrics = null;

    public static int f8893M = 2;

    private static void getInfo() {
//        String m5118c = SopCast.rConfig.m5118c("adult_words");
//        if (m5118c.isEmpty()) {
//            return;
//        }
//        SopCast.adultList = new ArrayList<>(Arrays.asList(m5118c.split(",")));
    }

    private static void initLibTV() {
//        String m5118c = SopCast.rConfig.m5118c("app_license");
//        socketAddr = SopCast.rConfig.m5118c("socket_addr");
//        LibTvServiceClient.getInstance().setAppLicense(m5118c);

        LibTvServiceClient.getInstance().setAppLicense("3e3925c5f54e2d6538b8bef61e868aac0527d2c11cc6b49b684b0753294314b43ebd6dffe48005a967d9d70c117676776ae472b7163b8675578e917a038c3184e7fa0a0c0c463dc9aeb5738d074b6cfc149c450d71052d52244d5ebdbbe45014ebea5d721de25db318dcdd68019f3539f02c88eff2f7b8f226131850b5834a619186eba742cdcb3c3b0d58f425e08362e527b53fae640053a01d7cecdfc009e6d2ddec446b53670845109ff52927860fdd");

        try {
            new BSCF(SopApplication.getAppContext());

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("did", (Object) Utils.getValue(Config.DEVICE_ID, ""));
            jSONObject.put("username", (Object) Utils.getValue(Config.USERNAME, ""));
            jSONObject.put("password", (Object) Utils.getValue(Config.PASSWORD, ""));
            jSONObject.put("packageName", (Object) BSCF.packageName);
            jSONObject.put("appName", (Object) BSCF.appName);
            jSONObject.put("appVersion", (Object) Integer.valueOf(BSCF.appVersionCode));
            jSONObject.put("buildBrand", (Object) Build.BRAND);
            jSONObject.put("buildProduct", (Object) Build.PRODUCT);
            jSONObject.put("buildBoard", (Object) Build.BOARD);
            jSONObject.put("buildABI", (Object) Build.CPU_ABI);
            jSONObject.put("buildDisplay", (Object) Build.DISPLAY);
            jSONObject.put("buildModel", (Object) Build.MODEL);
            jSONObject.put("sysVersion", (Object) Build.VERSION.RELEASE);
            LibTvServiceClient.getInstance().setConfig(jSONObject.toString());
        } catch (JSONException ex) {

        }

        domain = LibTvServiceClient.getInstance().getLoginPrefix();

        getInfo();
    }

    public static void initializeConfig() {
        if (Constant.LOGIN_BY_LIB) {
            initLibTV();
        }

        f8909g = false;
        f8924v = true;
        f8893M = 3;
    }

    public static boolean isPlayStarted = false;

}
