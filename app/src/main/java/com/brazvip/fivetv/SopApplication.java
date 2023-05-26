package com.brazvip.fivetv;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;

import com.brazvip.fivetv.utils.RestApiUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.local.Resolver;
import com.zhy.autolayout.config.AutoLayoutConifg;

import com.brazvip.fivetv.utils.PrefUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import go.Seq;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public class SopApplication extends Application implements InvocationHandler {

    /* renamed from: a */
    //public static final String f16776a = "MyApp";

    /* renamed from: b 16777 */
    public static SopApplication instance;

    public static String packageName = "";
    public static String strAppName = null;
    public static String strAppVersion = "";
    public static int appVersionCode = 1;
    public static String strUserAgent = "";

    /* renamed from: org.sopcast.android.SopApplication$a 4741 */
    public static class SopHttpInterceptor implements Interceptor {

        /* renamed from: a */
        public Context mContext;

        public SopHttpInterceptor(Context context) {
            this.mContext = context;
        }

        @Override // okhttp3.Interceptor
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Response proceed = chain.proceed(chain.request());
            if (PrefUtils.m2225e(this.mContext)) {
                proceed.newBuilder().removeHeader(HttpHeaders.HEAD_KEY_PRAGMA)
                        .removeHeader(HttpHeaders.HEAD_KEY_CACHE_CONTROL)
                        .header(HttpHeaders.HEAD_KEY_CACHE_CONTROL,
                                "public, max-age=100").build();
            } else {
                proceed.newBuilder().removeHeader(HttpHeaders.HEAD_KEY_PRAGMA)
                        .removeHeader(HttpHeaders.HEAD_KEY_CACHE_CONTROL)
                        .header(HttpHeaders.HEAD_KEY_CACHE_CONTROL,
                                "public, max-age=" + 86400).build();
            }
            return proceed;
        }
    }

    /* compiled from: MyApplication */
    /* renamed from: org.sopcast.android.SopApplication$b */
    /* loaded from: classes.dex */
    public class MyDnsManager implements Dns {

        /* renamed from: a 16779 */
        public DnsManager mDnsMgr;

        public MyDnsManager() {
            IResolver[] resolvers = new IResolver[3];
            try {
                resolvers[1] = new Resolver(InetAddress.getByName("1.1.1.1"));
                resolvers[0] = new Resolver(InetAddress.getByName("8.8.4.4"));
                resolvers[2] = new Resolver(InetAddress.getByName("104.16.249.249"));
                mDnsMgr = new DnsManager(NetworkInfo.normal, resolvers, null);
            } catch (UnknownHostException ignored) {
            }
        }

        @Override
        public List<InetAddress> lookup(String ip) {
            try {
                if (mDnsMgr == null) {
                    return Dns.SYSTEM.lookup(ip);
                }
                String[] list = mDnsMgr.query(ip);
                if (list != null && list.length != 0) {
                    ArrayList<InetAddress> result = new ArrayList<>();
                    for (String name : list) {
                        result.addAll(Arrays.asList(InetAddress.getAllByName(name)));
                    }
                    return result;
                }
                return Dns.SYSTEM.lookup(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    /* renamed from: b 147 */
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    /* renamed from: c */
    private void initInterceptor() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /*if (AppConfig.f13611h.booleanValue()) {
            builder.dns(new MyDnsManager());
        }*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("===== OkGo");
        interceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);
        interceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(interceptor);
        builder.readTimeout(20000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(20000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(20000, TimeUnit.MILLISECONDS);
        HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager);
        OkGo.getInstance().init(this).setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .setCacheTime(1209600000).setRetryCount(1);
    }

    /* renamed from: a */
    /*public Context m149a() {
        return getApplicationContext();
    }*/

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        instance = this;

        strAppName = this.getString(R.string.app_name);
        packageName = this.getPackageName();
        try {
            strAppVersion = this.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            appVersionCode = this.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        strUserAgent = String.format("Apache-HttpClient/Null (%s %s; Android %s; %s)", strAppName, strAppVersion, Build.VERSION.RELEASE, Build.MODEL);

        //CrashReport.initCrashReport(getApplicationContext(), AppConfig.BUGLY_APP_ID, false);
        //CrashReport.setAppChannel(getApplicationContext(), RestApiUtils.BUGLY_APP_CHANNEL);
        RestApiUtils.initValues();
        initInterceptor();
        initHttpClient(getApplicationContext());
        Seq.setContext((Object) this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 18) {
            builder.detectFileUriExposure();
        }
    }

    public static Context getSopContext() {
        return instance.getApplicationContext();
    }

    /* renamed from: a */
    public static OkHttpClient initHttpClient(Context context) {
        SSLContext sslContext = null;
        try {
            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            TrustManager[] trustAllCerts = new X509TrustManager[1];
            trustAllCerts[0] = tm;

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        } catch (KeyManagementException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        if (sslContext == null)
            return null;

        HostnameVerifier verifier = new HostnameVerifier() { //r0
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        File rootDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (rootDir.exists())
            rootDir.mkdirs();
        okhttp3.Cache cache = new okhttp3.Cache(rootDir, 52428800L); //r3
        return new OkHttpClient.Builder()
                .hostnameVerifier(verifier)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new SopHttpInterceptor(context))
                .cache(cache).build();
    }

    private String appPkgName = "";
    private Object base;
    private byte[][] sign;


    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context base) {
        hook(base);
        super.attachBaseContext(base);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            int flag = Integer.parseInt(args[1].toString());
            if ((flag & PackageManager.GET_SIGNATURES) != 0 && appPkgName.equals(pkgName)) {
                PackageInfo info = (PackageInfo) method.invoke(this.base, args);
                if (info != null) {
                    info.signatures = new Signature[this.sign.length];
                    for (int i = 0; i < info.signatures.length; i++) {
                        info.signatures[i] = new Signature(this.sign[i]);
                    }
                }
                return info;
            }
        }
        return method.invoke(this.base, args);
    }

    /*public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }*/

    private void hook(Context context) {
        try {
            //String text = "AQAAAwMwggL/MIIB56ADAgECAgQ1IegZMA0GCSqGSIb3DQEBCwUAMDAxCzAJBgNVBAYTAjU1MQ4wDAYDVQQLEwVwMnR2czERMA8GA1UEAxMIZGV2IHRlYW0wHhcNMTkxMDA5MDkxMTI0WhcNNDQxMDAyMDkxMTI0WjAwMQswCQYDVQQGEwI1NTEOMAwGA1UECxMFcDJ0dnMxETAPBgNVBAMTCGRldiB0ZWFtMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgIKf63Usyl02OQsubZPwFNh4/80UbXezJDhJ5lBTS7gQIjZdFpIvd5apOs0JX0oxsckqcZhfrwzFMW3f6fdmUE+Ug1LVOjdVKtsPvx4/6sCmz/px3hlvWPq16SfhAagOnwXe4ciPyFTGBR/DZksR6on/HzMxoya+OdIOhnm+Jl32IXbxtIHjm/bbA/JGcQaVH67CGDtFqEc8NLJAANddMHjlJG4rVELeAEWbjV3drVH9R7GNThNDDoHQ9ivobH0wd64vS0dgaPyCJCjTl5Tlln3QFPH88LnYKH9sbNIgIi5lk3v47cqFvmVx4R3cgcGjxV5gLfu9mBUu8FnyTcng5wIDAQABoyEwHzAdBgNVHQ4EFgQUqTHRwgM5fVslAkkgv7pwej0MzqkwDQYJKoZIhvcNAQELBQADggEBAIBQ1sBkE/ErtG3Ax6o/fmyjd3NDUEZTO+kqdHDJEOq8bHlSfWU25kqUAiuWMXOOczhvULk4YfJYDQrfLw55E7xX2VuOBWzGLmSbwLoMkxGikWiPqk9+f7F+zcL36D4ARdZ+ZdpxXRXdtkDkWIpVL773dDXG3hpsjFAgrrH9Bwk3zGfN0/1eS0kQXteet9wYN+J2ZkJ9g0FvUkFQKPE/+/g8AUz7hwUm3OWO5fcZKyoOlDbIxkaDTC7S7HkKUXPbMSC0DL/ZpbHLpktc+lY1CQBGoZ7ZZDgxNUjeW8984Ta9YIf0k/kzC0k89Ybtzsj7H0Ob+lQIgW3BCv8VFfmX6Mc="; //5.2 version
            String text = "AQAAA3MwggNvMIICV6ADAgECAgQxzQ4+MA0GCSqGSIb3DQEBCwUAMGgxCzAJBgNVBAYTAjU1MRMwEQYDVQQIDApTw6NvIFBhdWxvMREwDwYDVQQHEwhCYXJyZXRvczENMAsGA1UEChMERml2ZTEPMA0GA1UECxMGUDJGaXZlMREwDwYDVQQDEwhEZXYgVGVhbTAeFw0xOTEwMDkwODUzMjVaFw00NDEwMDIwODUzMjVaMGgxCzAJBgNVBAYTAjU1MRMwEQYDVQQIDApTw6NvIFBhdWxvMREwDwYDVQQHEwhCYXJyZXRvczENMAsGA1UEChMERml2ZTEPMA0GA1UECxMGUDJGaXZlMREwDwYDVQQDEwhEZXYgVGVhbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALabztCglNguWY5hG5k3H6P47s7cSf9KiwGI3ja5+oFudgqBtvJgbqzMagm5y2NUvRTkLfUj71wwW4IG0HdS5A6zZlCyuy614e676m4UyhmAX42wyiQq1VqRgrDGJ69YWYsfAnfgMCZeqJCQS3rgVpqAL//81OSBxtbFbpjAJ2mwJQriHuSCwHFQmX8EGTZratyajrsVF0tOmqDs+FYpdIRgDS0sGimcaVLOPl4WmOYNXCw5xi2Y1JAwCV2Uw3G/Wdz5ED42gWFKNE+Nr4giepiTyzeClL3BFXiXJX5LhsdvbwW/7KVdphqm7UHqiSPvq6uxO4nEI6yzT8GaIw3czOsCAwEAAaMhMB8wHQYDVR0OBBYEFE3o4HmPnhN22XZEy0hUt+RKIYglMA0GCSqGSIb3DQEBCwUAA4IBAQBJXHIq9Qqq7JX3BXpWOdyscWpyK3Ye9ZrMk2K79RhFuijdJ/HIrWuB8RYkN6/IllO1JPY+uA0xT49QJcMSXRPdKqxQS8Xe06jDbEAgUO7+eKT0USDieWbKvWfDMJPo7HtnTLmBrKJOop7sJmoRYWYnmTTIngmtJvqohmgFv+TAn2FpPmk4ILk+7+6RBnu83MYrS/VheO0iU46peSENrHVZvoQ5al50dw2+Ha+3r93XmcQLQVDa1wgI5yAn+IGlIrLRCHiASQhz53JjqoCBP2NF5/qi7nK8pWRAUq/02AGJ7Jem8dbg8dr1pxUY00qMuy82PQO+wmJQSMTIN9IIX5Z2";  //4.58 version
            DataInputStream is = new DataInputStream(new ByteArrayInputStream(Base64.decode(text, 0)));

            int len = is.read() & 255;
            byte[][] sign = new byte[len][];
            for (int i = 0; i < sign.length; i++) {
                sign[i] = new byte[is.readInt()];
                is.readFully(sign[i]);
            }

            /*if ((sign.length > 0) && (sign[0] != null)) {
                byte[] suffix = hexStringToByteArray("05bd6f509b8f24bdb4c32735632b31b4");
                len = sign[0].length;
                byte[] data = new byte[len + suffix.length];
                System.arraycopy(sign[0], 0, data, 0, len);
                System.arraycopy(suffix, 0, data, len, suffix.length);
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(data);
                data = digest.digest();
                Log.e("TEST", byteArrayToHexString(data));
            }*/

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread", new Class[0]);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null, new Object[0]);
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            this.base = sPackageManager;
            this.sign = sign;
            this.appPkgName = context.getPackageName();
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(), new Class[]{iPackageManagerInterface}, this);
            sPackageManagerField.set(currentActivityThread, proxy);
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
            System.out.println("PmsHook success.");
        } catch (Exception e) {
            System.err.println("PmsHook failed.");
            e.printStackTrace();
        }
    }
}
