package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.brazvip.fivetv.instances.AuthInstance;

public class SplashActivity extends AppCompatActivity {
    public static Handler mMsgHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initMessageHandler();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 1000);
    }

    public void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
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

    private void init() {
        if (AuthInstance.LoadAuthParams()) {
            AuthInstance.Login(mMsgHandler);
        }
        else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void onLoginSuccess() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void onLoginFail() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}