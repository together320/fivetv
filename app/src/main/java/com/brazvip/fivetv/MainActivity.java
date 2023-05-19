package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.layouts.MenuLayout;
import com.brazvip.fivetv.utils.PrefUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public enum FRAGMENT {
        DASHBOARD,
        LIVE,
        VOD,
        HISTORY,
        SETTING,
        LOADING,
        PROFILE
    }
    public static Handler mMsgHandler = null;

    private RadioGroup mRadioGroup = null;
    private RadioButton mRadioDashboard = null;
    private RadioButton mRadioLive = null;
    private RadioButton mRadioVod = null;
    private RadioButton mRadioHistory = null;
    private RadioButton mRadioSetting = null;

    private FrameLayout mLoadingLayout;
    private FrameLayout mPlayerLayout;
    private MenuLayout mMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initMessageHandler();

        EPGInstance.Refresh(mMsgHandler);
        ChannelInstance.Refresh(mMsgHandler);
        //VodChannelInstance.Refresh(mMsgHandler);
    }

    private void initComponents() {
        mRadioGroup = (RadioGroup)findViewById(R.id.main_rb);
        mRadioDashboard = (RadioButton)findViewById(R.id.rb_dashboard);
        mRadioLive = (RadioButton)findViewById(R.id.rb_live);
        mRadioVod = (RadioButton)findViewById(R.id.rb_vod);
        mRadioHistory = (RadioButton)findViewById(R.id.rb_history);
        mRadioSetting = (RadioButton)findViewById(R.id.rb_setting);

        mLoadingLayout = (FrameLayout)findViewById(R.id.loading_layout);
        mPlayerLayout = (FrameLayout)findViewById(R.id.play_layout);
        mMenuLayout = (MenuLayout) findViewById(R.id.menu_layout);

        mRadioDashboard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    refreshFragment(FRAGMENT.DASHBOARD);
                }
            }
        });
        mRadioLive.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    refreshFragment(FRAGMENT.LIVE);
                }
            }
        });
        mRadioVod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    refreshFragment(FRAGMENT.VOD);
                }
            }
        });
        mRadioHistory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    refreshFragment(FRAGMENT.HISTORY);
                }
            }
        });
        mRadioSetting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    refreshFragment(FRAGMENT.SETTING);
                }
            }
        });

        refreshFragment(FRAGMENT.LOADING);
    }

    private void refreshFragment(FRAGMENT frag) {
        mPlayerLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mMenuLayout.setVisibility(View.GONE);

        mRadioGroup.setVisibility(View.VISIBLE);

        switch (frag) {
            case DASHBOARD:
                mRadioGroup.check(R.id.rb_dashboard);
                break;
            case LIVE:
                mRadioGroup.check(R.id.rb_live);
                mMenuLayout.setVisibility(View.VISIBLE);
                mMenuLayout.loadGroup();
                break;
            case VOD:
                mRadioGroup.check(R.id.rb_vod);
                break;
            case HISTORY:
                mRadioGroup.check(R.id.rb_history);
                break;
            case SETTING:
                mRadioGroup.check(R.id.rb_setting);
                break;
            case LOADING:
                mRadioGroup.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                break;
            case PROFILE:
                break;
        }
    }

    private void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_LOADED:
                        refreshFragment(FRAGMENT.DASHBOARD);
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    private void logout() {
        AuthInstance.SaveAuthParams("", "");

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}