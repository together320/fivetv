package com.brazvip.fivetv;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brazvip.fivetv.cache.CacheManager;
import com.brazvip.fivetv.dialogs.LogoutDialog;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.DashboardInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.instances.HistoryInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.layouts.DashboardLayout;
import com.brazvip.fivetv.layouts.HistoryLayout;
import com.brazvip.fivetv.layouts.MenuLayout;
import com.brazvip.fivetv.layouts.PlayerLayout;

import com.brazvip.fivetv.layouts.ProfileLayout;
import com.brazvip.fivetv.layouts.SettingLayout;
import com.brazvip.fivetv.layouts.VodLayout;
import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

public class MainActivity extends AutoLayoutActivity implements View.OnClickListener, View.OnKeyListener {

    public String TAG = "MainActivity";

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

    public Context context = null;
    public static Handler handler = null;

    private RadioGroup mRadioGroup = null;
    private LinearLayout mProfileRoot = null;
    private ImageView mProfileAvatar = null;
    private RadioButton mRadioDashboard = null;
    private RadioButton mRadioLive = null;
    private RadioButton mRadioVod = null;
    private RadioButton mRadioHistory = null;
    private RadioButton mRadioSetting = null;

    private FrameLayout mLoadingLayout;
    private PlayerLayout mPlayerLayout;
    private MenuLayout mMenuLayout;
    private DashboardLayout mDashlayout;
    private ProfileLayout mProfileLayout;
    private SettingLayout mSettingLayout;
    private RelativeLayout mUserLayout;
    private HistoryLayout mHistoryLayout;
    private VodLayout mVodLayout;

    public static HistoryInstance history;


    public static CacheManager cacheManager = null;

    public FRAGMENT mPrevFragment = FRAGMENT.NONE;
    public FRAGMENT mCurrFragment = FRAGMENT.NONE;

    private int mLoaded = 0;

    public static boolean isRestrictedAccess = false;

    public static boolean isUpdating = false;

    public static boolean f16802k = false;
    public static boolean f16805n = true;

    public static Toast toastObj = null;

    public static long toastPreTime = 0;

    public static long toastCurTime = 0;

    public static String toastMsg = null;

    public static boolean restrictedGroupsUnlocked = false;

    public static int groupType = 100;

    public static ArrayList<String> adultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initComponents();
        initMessageHandler();

        DashboardInstance.getInstance();
        ChannelInstance.getAllChannels();
        EPGInstance.getAllEPGs();
        VodChannelInstance.getAllVodGroups();

        if (Constant.OFFLINE_TEST) {
            Message msg = new Message();
            msg.what = Constant.MSG_PLAYER_LOADED;
            MainActivity.SendMessage(msg);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initComponents() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_rb);
        mProfileRoot = findViewById(R.id.main_rb_user_button);
        mProfileAvatar = (ImageView) findViewById(R.id.main_rb_profile_avatar);
        mRadioDashboard = (RadioButton) findViewById(R.id.rb_dashboard);
        mRadioLive = (RadioButton) findViewById(R.id.rb_live);
        mRadioVod = (RadioButton) findViewById(R.id.rb_vod);
        mRadioHistory = (RadioButton) findViewById(R.id.rb_history);
        mRadioSetting = (RadioButton) findViewById(R.id.rb_setting);

        mLoadingLayout = (FrameLayout) findViewById(R.id.loading_layout);
        mPlayerLayout = (PlayerLayout) findViewById(R.id.player_layout);
        mMenuLayout = (MenuLayout) findViewById(R.id.menu_layout);
        mDashlayout = (DashboardLayout) findViewById(R.id.dashboard_layout);
        mProfileLayout = (ProfileLayout) findViewById(R.id.profile_layout);
        mSettingLayout = (SettingLayout) findViewById(R.id.setting_layout);
        mUserLayout = (RelativeLayout) findViewById(R.id.user_info_root);
        mHistoryLayout = (HistoryLayout) findViewById(R.id.history_layout);
        mVodLayout = (VodLayout) findViewById(R.id.vod_layout);

        LinearLayout btnChangeProfile = (LinearLayout) findViewById(R.id.btn_change_profile);
        TextView user_dialog_prof_name = (TextView) findViewById(R.id.user_dialog_prof_name);
        user_dialog_prof_name.setText(AuthInstance.mAuthInfo.user.user_name.split("@")[0]);
        Button btnLogout = (Button) findViewById(R.id.btn_logout);

        mProfileRoot.setVisibility(View.GONE);

        mProfileAvatar.setImageResource(R.drawable.profile_avatar_1);
        mProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLayout.setVisibility(View.VISIBLE);
            }
        });

        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLayout.setVisibility(View.GONE);
            }
        });

        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLayout.setVisibility(View.GONE);
                refreshFragment(FRAGMENT.PROFILE);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLayout.setVisibility(View.GONE);
                logOut();
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

        history = new HistoryInstance(this);

        refreshFragment(FRAGMENT.LOADING);
    }

    private void refreshFragment(FRAGMENT fragment) {
        mPlayerLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mMenuLayout.setVisibility(View.GONE);
        mDashlayout.setVisibility(View.GONE);
        mProfileLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.GONE);
        mHistoryLayout.setVisibility(View.GONE);
        mVodLayout.setVisibility(View.GONE);

        mRadioGroup.setVisibility(View.VISIBLE);

        if (mPrevFragment != mCurrFragment) {
            mPrevFragment = mCurrFragment;
        }
        mCurrFragment = fragment;

        switch (fragment) {
            case DASHBOARD:
                mRadioGroup.check(R.id.rb_dashboard);
                if (checkLoadedValue()) {
                    mDashlayout.loadDashLayout();
                    mDashlayout.setVisibility(View.VISIBLE);
                }
                break;
            case LIVE:
                mRadioGroup.check(R.id.rb_live);
                if (checkLoadedValue()) {
                    mMenuLayout.loadMenuLayout();
                    mMenuLayout.setVisibility(View.VISIBLE);
                }
                break;
            case VOD:
                mRadioGroup.check(R.id.rb_vod);
                if (checkLoadedValue()) {
                    mVodLayout.loadVodLayout();
                    mVodLayout.setVisibility(View.VISIBLE);
                }
                break;
            case HISTORY:
                mRadioGroup.check(R.id.rb_history);
                if (checkLoadedValue()) {
                    mHistoryLayout.loadHistoryLayout();
                    mHistoryLayout.setVisibility(View.VISIBLE);
                }
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
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_SHOW_QUIT_DIALOG:
                        showQuitDialog();
                        break;
                    case Constant.MSG_QUIT_APP:
                        finish();
                        Utils.exitApp();
                        break;

                    case Constant.MSG_CHANNEL_LOADED:
                        mLoaded |= 0b0001;
                        checkLoaded();
                        break;
                    case Constant.MSG_EPG_LOADED:
                        mLoaded |= 0b0010;
                        checkLoaded();
                        break;
                    case Constant.MSG_VOD_LOADED:
                        mLoaded |= 0b0100;
                        checkLoaded();
                        break;
                    case Constant.MSG_PLAYER_LOADED:
                        mLoaded |= 0b1000;
                        checkLoaded();
                        break;
                    case Constant.MSG_PLAYER_PLAY_VIDEO:
                        showPlayVideo(message.getData());
                        break;
                    case Constant.MSG_PLAYER_START_PLAYBACK:
                        showStartPlayback(message.getData());
                        break;

                    case Constant.MSG_SHOW_TOAST:
                        Bundle bd = message.getData();
                        if (bd != null && !bd.getString("text").equals("")) {
                            showToast(bd.getString("text"), message.arg2);
                            break;
                        }
                        break;

                    case Constant.EVENT_FOCUS_DASH_BUTTON:
                        mRadioDashboard.requestFocus();
                        mRadioDashboard.setChecked(true);
                        break;
                    case Constant.EVENT_FOCUS_LIVE_BUTTON:
                        mRadioLive.requestFocus();
                        mRadioLive.setChecked(true);
                        break;
                    case Constant.EVENT_FOCUS_VOD_BUTTON:
                        mRadioVod.requestFocus();
                        mRadioVod.setChecked(true);
                        break;
                    case Constant.EVENT_FOCUS_HISTORY_BUTTON:
                        mRadioHistory.requestFocus();
                        mRadioHistory.setChecked(true);
                        break;
                    case Constant.EVENT_FOCUS_SETTINGS_BUTTON:
                        mRadioSetting.requestFocus();
                        mRadioSetting.setChecked(true);
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void checkLoaded() {
        if (mLoaded == 0b1111)
            refreshFragment(FRAGMENT.DASHBOARD);
    }

    private boolean checkLoadedValue() {
        if (mLoaded == 0b1111)
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mCurrFragment == FRAGMENT.PROFILE) {
            refreshFragment(FRAGMENT.DASHBOARD);
        } else if (mCurrFragment == FRAGMENT.PLAYER) {
            mPlayerLayout.stopPlayer();
            refreshFragment(mPrevFragment);
        } else {
            //PrefUtils.logout(this);
            //super.onBackPressed();
            MainActivity.SendMessage(Constant.MSG_SHOW_QUIT_DIALOG);
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

    private void logOut() {
        com.lzy.okgo.db.CacheManager.getInstance().clear();
        cacheManager.clearCache();

        AuthInstance.SaveAuthParams("", "");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void showPlayVideo(Bundle bundle) {
        if (mCurrFragment == FRAGMENT.PLAYER)
            return;

        refreshFragment(FRAGMENT.PLAYER);
        mPlayerLayout.playVideo(bundle);
    }

    public void showStartPlayback(Bundle bundle) {
        mPlayerLayout.startPlayback(bundle.getString("videoPath"));
    }


    public static void SendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static void SendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = 0;
        handler.sendMessage(msg);
    }

    public static boolean logMemoryStats() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.maxMemory() / 1048576) - ((runtime.totalMemory() - runtime.freeMemory()) / 1048576) < 10;
    }

    public static boolean isSystemOnLowMemory() {
        ActivityManager activityManager = (ActivityManager) SopApplication.getSopContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return true;
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    public void setSystemFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(774);
    }

    public void showToast(String text, int type) {
        int delay = type == 1 ? 3500 : 2000;
        if (toastObj == null) {
            toastObj = Toast.makeText(this, text, type);
            try {
                toastObj.show();
            } catch (WindowManager.BadTokenException ignored) {
                Log.e("MainActivity", ignored.toString());
            }
        } else {
            toastCurTime = System.currentTimeMillis();
            if (text.equals(toastMsg)) {
                if (toastCurTime - toastPreTime > delay) {
                    try {
                        toastObj.show();
                    } catch (WindowManager.BadTokenException ignored) {
                        Log.e("MainActivity", ignored.toString());
                    }
                }
            } else {
                toastMsg = text;
                toastObj.setText(text);
                try {
                    toastObj.show();
                } catch (WindowManager.BadTokenException ignored) {
                    Log.e("MainActivity", ignored.toString());
                }
            }
        }
        toastPreTime = toastCurTime;
    }

    public static void prepareToast(int i) {
        prepareToast(i, 0);
    }

    public static void prepareToast(int i, int i2) {
        sendShowToastEvent(SopApplication.getSopContext().getString(i), i2);
    }

    public static void sendShowToastEvent(String str, int i) {
        Message message = new Message();
        message.what = Constant.MSG_SHOW_TOAST;
        Bundle bundle = new Bundle();
        bundle.putString("text", str);
        message.setData(bundle);
        message.arg2 = i;
        if (handler != null) {
            handler.sendMessage(message);
        }
    }

    public void showQuitDialog() {
        final LogoutDialog.Builder dialogBuilder = new LogoutDialog.Builder(context);
        dialogBuilder.message = context.getResources().getString(R.string.confirm_quit);
        dialogBuilder.positiveOption = context.getResources().getString(R.string.quit_later);
        dialogBuilder.positiveListener = (dialogInterface, n) -> {
            dialogInterface.dismiss();
        };
        dialogBuilder.negativeOption = context.getResources().getString(R.string.quit_now);
        dialogBuilder.cancelListener = (dialogInterface, n) -> {
            dialogInterface.dismiss();
            MainActivity.handler.sendEmptyMessage(Constant.MSG_QUIT_APP);
        };
        dialogBuilder.build().show();
    }

}