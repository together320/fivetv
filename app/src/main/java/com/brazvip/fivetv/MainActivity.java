package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.utils.PrefUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Handler mMsgHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EPGInstance.Refresh(mMsgHandler);
        ChannelInstance.Refresh(mMsgHandler);
        VodChannelInstance.Refresh(mMsgHandler);

        Button btn_logout = (Button)findViewById(R.id.logout_btn);
        btn_logout.setOnClickListener(this);

        initMessageHandler();
    }

    public void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {

                }
                super.handleMessage(message);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.logout_btn) {
            logout();
        }
    }

    private void logout() {
        AuthInstance.SaveAuthParams("", "");

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}