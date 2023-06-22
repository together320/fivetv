package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.brazvip.fivetv.cache.CacheManager;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.Utils;

import okhttp3.Cache;
import p129l8.C2053a;

public class SplashActivity extends AppCompatActivity {
    public static Handler mMsgHandler = null;
    public static Context mContext = null;

    public static ProgressBar loading_progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = SopApplication.getAppContext();

        MainActivity.cacheManager = CacheManager.getCacheManager(mContext);

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (applicationInfo != null && applicationInfo.metaData != null) {
            C2053a.f7440f.f7443c = ((Integer) applicationInfo.metaData.get("design_width")).intValue();
            C2053a.f7440f.f7444d = ((Integer) applicationInfo.metaData.get("design_height")).intValue();
        }

        Config.displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Config.displayMetrics);

        loading_progress = findViewById(R.id.loading_progress);
        loading_progress.setVisibility(View.GONE);

        initMessageHandler();

        new Handler().postDelayed(() ->  {
            loading_progress.setVisibility(View.VISIBLE);
            SopApplication.initLibTv();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    public void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_LIBTV_SERVICE_CONNECTED:
                        doLogin();
                        break;
                    case Constant.MSG_LIVTV_SERVICE_DISCONNECTED:
                    case Constant.MSG_LIVTV_SERVICE_BINDING_DIED:
                    case Constant.MSG_LIVTV_SERVICE_BINDING_NULL:
                        onLibTvServiceFailed();
                        break;

                    case Constant.MSG_LOGIN_SUCCESS:
                        onLoginSuccess();
                        break;
                    case Constant.MSG_LOGIN_FAIL:
                        onLoginFail();
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void appLicensing() {
        // FirebaseApp appInstance = FirebaseApp.getInstance();
    }

    private void doLogin() {
        Config.initializeConfig();

        if (AuthInstance.LoadAuthParams()) {
            AuthInstance.Auth(mMsgHandler);
        }
        else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void onLibTvServiceFailed() {
        PrefUtils.ToastShort("Service Connect Failure!");
        new Handler().postDelayed(() -> doExitApp(), 2000);
    }

    private void onLoginSuccess() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void onLoginFail() {
        PrefUtils.ToastShort("Account Login Failure!");
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private void doExitApp() {
        finish();
        Utils.exitApp();
    }
}