package com.brazvip.fivetv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.layouts.MenuLayout;
import com.brazvip.fivetv.layouts.PlayerLayout;

import com.brazvip.fivetv.layouts.ProfileLayout;
import com.brazvip.fivetv.layouts.SettingLayout;
import com.zhy.autolayout.AutoLayoutActivity;

public class MainActivity extends AutoLayoutActivity implements View.OnClickListener, View.OnKeyListener {

    public enum FRAGMENT {
        NONE,
        DASHBOARD,
        LIVE,
        VOD,
        HISTORY,
        SETTING,
        LOADING,
        PLAYER,
        PROFILE
    }

    public static Handler mMsgHandler = null;

    private RadioGroup mRadioGroup = null;
    private ImageView mProfileAvatar = null;
    private RadioButton mRadioDashboard = null;
    private RadioButton mRadioLive = null;
    private RadioButton mRadioVod = null;
    private RadioButton mRadioHistory = null;
    private RadioButton mRadioSetting = null;

    private FrameLayout mLoadingLayout;
    private PlayerLayout mPlayerLayout;
    private MenuLayout mMenuLayout;
    private ProfileLayout mProfileLayout;
    private SettingLayout mSettingLayout;


    public FRAGMENT mPrevFragment = FRAGMENT.NONE;
    public FRAGMENT mCurrFragment = FRAGMENT.NONE;

    private int mLoaded = 0;

    public static boolean isRestrictedAccess = false;

    public static boolean isUpdating = false;

    public static boolean f16805n = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initMessageHandler();

        ChannelInstance.getChannels();
        EPGInstance.Refresh();
        //VodChannelInstance.Refresh();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initComponents() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_rb);
        mProfileAvatar = (ImageView) findViewById(R.id.main_rb_profile_avatar);
        mRadioDashboard = (RadioButton) findViewById(R.id.rb_dashboard);
        mRadioLive = (RadioButton) findViewById(R.id.rb_live);
        mRadioVod = (RadioButton) findViewById(R.id.rb_vod);
        mRadioHistory = (RadioButton) findViewById(R.id.rb_history);
        mRadioSetting = (RadioButton) findViewById(R.id.rb_setting);

        mLoadingLayout = (FrameLayout) findViewById(R.id.loading_layout);
        mPlayerLayout = (PlayerLayout) findViewById(R.id.player_layout);
        mMenuLayout = (MenuLayout) findViewById(R.id.menu_layout);
        mProfileLayout = (ProfileLayout) findViewById(R.id.profile_layout);
        mSettingLayout = (SettingLayout) findViewById(R.id.setting_layout);

        mProfileAvatar.setImageResource(R.drawable.profile_avatar_1);
        mProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFragment(FRAGMENT.PROFILE);
            }
        });

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
        mProfileLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.GONE);

        mRadioGroup.setVisibility(View.VISIBLE);

        if (mPrevFragment != mCurrFragment)
            mPrevFragment = mCurrFragment;
        mCurrFragment = frag;

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
                mSettingLayout.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                mRadioGroup.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                break;
            case PLAYER:
                mRadioGroup.setVisibility(View.GONE);
                mPlayerLayout.setVisibility(View.VISIBLE);
                break;
            case PROFILE:
                mRadioGroup.setVisibility(View.GONE);
                mProfileLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_CHANNEL_LOADED:
                        mLoaded |= 0b001;
                        checkLoaded();
                        break;
                    case Constant.MSG_EPG_LOADED:
                        mLoaded |= 0b010;
                        checkLoaded();
                        break;
                    case Constant.MSG_PLAYER_LOADED:
                        mLoaded |= 0b100;
                        checkLoaded();
                        break;
                    case Constant.MSG_PLAYER_START:
                        onMsgVideoStart(message.getData());
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void checkLoaded() {
        if (mLoaded == 0b011)
            mPlayerLayout.initTVCore();
        else if (mLoaded == 0b111)
            refreshFragment(FRAGMENT.DASHBOARD);
    }

    @Override
    public void onBackPressed() {
        if (mCurrFragment == FRAGMENT.PROFILE) {
            refreshFragment(FRAGMENT.DASHBOARD);
        } else if (mCurrFragment == FRAGMENT.PLAYER) {
            refreshFragment(mPrevFragment);
        } else {
            //PrefUtils.logout(this);
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        return false;
    }

    private void logout() {
        //AuthInstance.SaveAuthParams("", "");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void onMsgVideoStart(Bundle bundle) {
        refreshFragment(FRAGMENT.PLAYER);

        String videoURL = bundle.getString("url");
        String videoName = bundle.getString("name");
        Config.BS_MODE bsMode = Config.BS_MODE.valueOf(bundle.getString("type"));

        mPlayerLayout.startChannel(videoURL, videoName, bsMode);
    }

    public static void SendMessage(Message msg) {
        mMsgHandler.sendMessage(msg);
    }

    public static void SendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = 0;
        mMsgHandler.sendMessage(msg);
    }

}