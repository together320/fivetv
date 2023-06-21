package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.Utils;

public class SplashActivity extends AppCompatActivity {
    public static Handler mMsgHandler = null;

    public static ProgressBar loading_progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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