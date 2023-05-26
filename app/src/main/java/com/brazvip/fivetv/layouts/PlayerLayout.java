package com.brazvip.fivetv.layouts;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;
import com.tvbus.engine.TVService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerLayout extends FrameLayout {
    public static TVCore mTVCore = null;

    private TextView mProgramNameText = null;
    private TextView mCurrentTimeText = null;
    private TextView mDurationTimeText = null;
    private TextView mDlRateText = null;

    private int mBuffer;
    private int mDlRate;
    private int mTmPlayerConn;
    private long mMPCheckTime = 0;
    private static String playbackUrl;
    private final static long MP_START_CHECK_INTERVAL = 10 * 1000 * 1000 * 1000L; // 10 second

    private SimpleExoPlayer player;

    public PlayerLayout(Context context) {
        super(context);
        init(context);
    }

    public PlayerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.player_layout, this, true);

        initComponents();
        initExoPlayer();
    }

    private void initComponents() {
        mProgramNameText = findViewById(R.id.program_name);
        mCurrentTimeText = findViewById(R.id.player_current_time);
        mDurationTimeText = findViewById(R.id.player_duration_time);
        mDlRateText = findViewById(R.id.dl_rate);
    }

    private void initExoPlayer() {
        PlayerView playerView = findViewById(R.id.exoPlayerView);
        playerView.requestFocus();
        playerView.setControllerAutoShow(false);
        playerView.setUseController(false);
        playerView.setKeepScreenOn(true);

        DefaultLoadControl.Builder builder = new DefaultLoadControl.Builder();
        builder.setBufferDurationsMs(
                2000,
                15000,
                500,
                0
        );
        LoadControl loadControl = builder.createDefaultLoadControl();

        player = new SimpleExoPlayer.Builder(SopApplication.getAppContext()).setLoadControl(loadControl).build();
        player.addVideoListener(new com.google.android.exoplayer2.video.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame() {
                mMPCheckTime = System.nanoTime();
            }
        });

        playerView.setPlayer(player);
    }

    public void initTVCore() {
        if (Constant.OFFLINE_TEST == true) {
            Message msg = new Message();
            msg.what = Constant.MSG_PLAYER_LOADED;
            MainActivity.SendMessage(msg);
            return;
        }

        if (mTVCore != null)
            return;

        mTVCore = TVCore.getInstance();
        if (mTVCore == null)
            return;
        mTVCore.setMKBroker(AuthInstance.mAuthInfo.service.mk_broker);
        mTVCore.setAuthUrl(AuthInstance.mAuthInfo.service.auth_url_sdk);
        String username = PrefUtils.getPrefString("username", "");
        String password = PrefUtils.getPrefString("password", "");
        if (!username.contains("@"))
            username += Constant.DEFAULT_MAIL_SUFFIX;
        mTVCore.setUsername(username);
        mTVCore.setPassword(password);

        mTVCore.setTVListener(new TVListener() {
            @Override
            public void onInited(String result) {
                parseCallbackInfo("onInited", result);
            }

            @Override
            public void onStart(String result) {
                parseCallbackInfo("onStart", result);
            }

            @Override
            public void onPrepared(String result) {
                if(parseCallbackInfo("onPrepared", result)) {
                    startPlayback();
                }
            }

            @Override
            public void onInfo(String result) {
                parseCallbackInfo("onInfo", result);
                checkPlayer();
            }

            @Override
            public void onStop(String result) {
                parseCallbackInfo("onStop", result);
            }

            @Override
            public void onQuit(String result) {
                parseCallbackInfo("onQuit", result);
            }
        });

        SopApplication.getAppContext().startService(new Intent(SopApplication.getAppContext(), TVService.class));
    }

    private boolean parseCallbackInfo(String event, String result) {
        JSONObject jsonObj = null;
        String statusMessage = null;

        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonObj == null) {
            return false;
        }
        switch (event) {
            case "onInited":
                if ((jsonObj.optInt("tvcore", 1)) == 0) {
                    Message msg = new Message();
                    msg.what = Constant.MSG_PLAYER_LOADED;
                    MainActivity.SendMessage(msg);

                    statusMessage = "Ready to go!";
                }
                else {
                    statusMessage = "Init error!";
                }
                break;

            case "onStart":
                break;

            case "onPrepared": // use http-mpegts as source
                if(jsonObj.optString("http", null) != null) {
                    playbackUrl = jsonObj.optString("http", null);
                    break;
                }

                return false;
            case "onInfo":
                mTmPlayerConn = jsonObj.optInt("hls_last_conn", 0);
                mBuffer = jsonObj.optInt("buffer", 0);
                mDlRate = jsonObj.optInt("download_rate", 0);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDlRateText.setText(PrefUtils.m2251a(mDlRate));
                    }
                });

                statusMessage = "" + mBuffer + "  "
                        + jsonObj.optInt("download_rate", 0) * 8 / 1000 +"K";
                break;

            case "onStop":
                if(jsonObj.optInt("errno", 1) < 0) {
                    statusMessage = "stop: " + jsonObj.optInt("errno", 1);
                }
                break;

            case "onQuit":
                break;
        }
        return true;
    }

    public void startChannel(String videoUrl, String videoName, Config.BS_MODE bsMode) {

        if (Constant.OFFLINE_TEST == true) {
            return;
        }

        stopPlayback();
        mMPCheckTime = Long.MAX_VALUE;
        mTmPlayerConn = mBuffer = 0;

        if (bsMode == Config.BS_MODE.BSPALYBACK) {
            mCurrentTimeText.setText("00:00");
            mDurationTimeText.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_play_back) + ": " + videoName;
        } else if (bsMode == Config.BS_MODE.BSVOD) {
            mCurrentTimeText.setText("00:00");
            mDurationTimeText.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        } else if (bsMode == Config.BS_MODE.BSLIVE) {
            mCurrentTimeText.setText(R.string.buffer);
            mDurationTimeText.setText("0/100");
            videoName = SopApplication.getAppContext().getString(R.string.video_live) + ": " + videoName;
        } else if (bsMode == Config.BS_MODE.STATIC) {
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        }

        mTVCore.start(videoUrl);
        mProgramNameText.setText(videoName);
    }

    // player related
    private void checkPlayer() {
        // Attention
        // check player playing must run in main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mTmPlayerConn > 20 && mBuffer > 50) {
                    stopPlayback();
                }

                if(System.nanoTime() > mMPCheckTime) {
                    int playbackState = player.getPlaybackState();
                    if (! (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED)) {
                        startPlayback();
                    }
                }
            }
        });
    }

    private void stopPlayback() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                player.stop();
            }
        });
    }

    private void startPlayback() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMPCheckTime = System.nanoTime() + MP_START_CHECK_INTERVAL;

                DataSource.Factory dataSourceFactory =
                        new DefaultDataSourceFactory(SopApplication.getAppContext(), "TVBUS");
                MediaSource source = new ProgressiveMediaSource.Factory(dataSourceFactory, TsExtractor.FACTORY)
                        .createMediaSource(Uri.parse(playbackUrl));

                player.prepare(source);
                player.setPlayWhenReady(true);
            }
        });
    };
}