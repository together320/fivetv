package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.brazvip.fivetv.BSCF;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.TVCarService;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.dialogs.PopMsg;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.instances.HistoryInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.Utils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.video.VideoSize;
import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;
import com.tvbus.engine.TVService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.binstream.libtvcar.Libtvcar;

public class PlayerLayout extends FrameLayout {
    public static final String TAG = "PlayerLayout";
    public static Context mContext;
    public static TVCore mTVCore = null;
    public static StyledPlayerView exoPlayerView = null;
    public static VideoView SYS_PLAYER = null;
    public static Handler mMsgHandler = null;
    public static ExoPlayer EXO_PLAYER = null;

    private TextView programName = null;
    private TextView playerCurrentTime = null;
    private TextView playerDurationTime = null;
    private TextView dlRate = null;
    public SeekBar playerSeekbar;
    public RelativeLayout dlLayout;
    public RelativeLayout playerProcessBar;
    public ProgressBar loadingProgress;
    public ImageView playerStatus;
    public FrameLayout rootFrameLayout;

    public static int mPlayerMode = 1;   //0 - Android Media Player, 1 - ExoPlayer
    private int mBuffer;
    private int mDlRate;
    private int mTmPlayerConn;
    private long mMPCheckTime = 0;
    public static final long MP_START_CHECK_INTERVAL = 8000000000L;
    public static boolean mIsEnded = true;
    public static int vod_ecode = 0;
    public Config.BS_MODE mBsMode = Config.BS_MODE.BSLIVE;
    public int mPlayerCurrentPos;
    public int mPlayerDuration;
    public int mPlayerSeekPos;
    public String mCurrentVideoPath = "";

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
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.player_layout, this, true);

        EventBus.getDefault().register(this);

        initComponents();
        initExoPlayer();
        initMessageHandler();

        if (Constant.OFFLINE_TEST == true) {

        } else {
            initTVCore();
            initTVCarService();
        }
    }

    private void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_PLAYER_REFRESHINFO:
                        playerSeekbar.setProgress(mBuffer);
                        playerDurationTime.setText(mBuffer + "/100");
                        SetDownloadRate(PrefUtils.m2251a(mDlRate));
                        break;
                    case Constant.MSG_PLAYER_START_PLAYBACK:
                        mCurrentVideoPath = message.getData().getString("videoPath");
                        startPlaying(mCurrentVideoPath);
                        break;
                    case Constant.MSG_PLAYER_HIDEPROCESSBAR:
                        playerProcessBar.setVisibility(View.GONE);
                        break;
                    case Constant.MSG_PLAYER_CHECKPLAYER:
                        checkPlayer();
                        break;
                    case Constant.MSG_PLAYER_RESUME:
                        mMPCheckTime = System.nanoTime() + 4000000000L;
                        resumePlayer();
                        break;
                    case Constant.MSG_PLAYER_STOP:
                        showError(message.arg1);
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void initComponents() {
        this.programName = (TextView) findViewById(R.id.program_name);
        this.dlLayout = (RelativeLayout) findViewById(R.id.dl_layout);
        this.dlRate = (TextView) findViewById(R.id.dl_rate);
        this.dlLayout.setVisibility(View.GONE);
        this.loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        this.playerProcessBar = (RelativeLayout) findViewById(R.id.player_process_bar);
        this.playerProcessBar.setVisibility(View.GONE);
        this.playerCurrentTime = (TextView) findViewById(R.id.player_current_time);
        this.playerDurationTime = (TextView) findViewById(R.id.player_duration_time);
        this.playerSeekbar = (SeekBar) findViewById(R.id.player_seekbar);
        this.playerStatus = (ImageView) findViewById(R.id.player_status);
        rootFrameLayout = findViewById(R.id.root);
    }

    private void initExoPlayer() {
        exoPlayerView = findViewById(R.id.exoPlayerView);
        //mPlayerView.setOnKeyListener(this);
        //mPlayerView.setOnClickListener(this);
        //mPlayerView.setOnTouchListener(this);
        exoPlayerView.setClickable(true);
        exoPlayerView.setFocusable(true);
        exoPlayerView.setControllerAutoShow(false);
        exoPlayerView.setUseController(false);
        exoPlayerView.setKeepScreenOn(true);
        //ExoPlayer 2.11.3
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(SopApplication.getAppContext()).build();
//        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(SopApplication.getAppContext());
//        LoadControl loadControl = new DefaultLoadControl.Builder().build();
//        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(SopApplication.getAppContext());
//        //renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);
//        Looper looper = PrefUtils.getLooper();
//        AnalyticsCollector analyticsCollector = new AnalyticsCollector(Clock.DEFAULT);
        EXO_PLAYER = new ExoPlayer.Builder(SopApplication.getAppContext()).build();

        EXO_PLAYER.addListener(new Player.Listener() {

            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {

            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {

            }

            @Override
            public void onRenderedFirstFrame() {
                mMPCheckTime = System.nanoTime();
            }

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    mMPCheckTime = System.nanoTime();
                    if (mBsMode == Config.BS_MODE.BSLIVE) {
                        mMPCheckTime = System.nanoTime();
                    } else if ((mBsMode == Config.BS_MODE.BSPALYBACK) || (mBsMode == Config.BS_MODE.BSVOD)) {
                    }
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                if (playWhenReady) {
                    hideProcessBarWithDelay(5000);
                    loadingProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(PlaybackException error) {
                EXO_PLAYER.stop();
                mMPCheckTime = System.nanoTime();
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

        exoPlayerView.setPlayer(EXO_PLAYER);
        //mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    public void hideProcessBarWithDelay(int delay) {
        this.playerProcessBar.setVisibility(View.VISIBLE);
        if (delay > 0) {
            mMsgHandler.removeMessages(Constant.MSG_PLAYER_HIDEPROCESSBAR);
            mMsgHandler.sendEmptyMessageDelayed(Constant.MSG_PLAYER_HIDEPROCESSBAR, delay);
        }
    }

    public void initTVCarService() {
        Libtvcar.setAuthURL(AuthInstance.mAuthInfo.service.auth_url_sdk);
        String username = PrefUtils.getPrefString("username", "");
        String password = PrefUtils.getPrefString("password", "");
        if (!username.contains("@"))
            username += Constant.DEFAULT_MAIL_SUFFIX;
        Libtvcar.setUsername(username);
        Libtvcar.setPassword(password);
        TVCarService.runServiceWithInit();
    }

    public void initTVCore() {
        mTVCore = TVCore.getInstance();
        if (mTVCore == null)
            return;
        mTVCore.setMKBroker(AuthInstance.mAuthInfo.service.mk_broker);
        if (Config.f8893M == 3) {
            AuthInfo.ServiceBean serviceBean = AuthInstance.mAuthInfo.service;
            if (serviceBean != null && serviceBean.auth_url_sdk != null) {
                mTVCore.setAuthUrl(serviceBean.auth_url_sdk);
                if (Constant.LOGIN_BY_LIB) {
                    mTVCore.setUsername(Utils.getValue(Config.HASH_USERNAME, ""));
                    mTVCore.setPassword(Utils.getValue(Config.HASH_PASSWORD, ""));
                } else {
                    mTVCore.setUsername(Utils.getValue(Config.USERNAME, "") + Constant.DEFAULT_MAIL_SUFFIX);
                    mTVCore.setPassword(Utils.getValue(Config.PASSWORD, ""));
                }
            }
        } else {
            String str;
            String str2;
            String str3;
            AuthInfo.KeysBean keysBean = AuthInstance.mAuthInfo.keys;
            if (keysBean != null && (str = keysBean.user_id) != null && !str.equals("") &&
                    (str2 = keysBean.peer_id) != null && !str2.equals("") &&
                    (str3 = keysBean.session_key) != null && !str3.equals("")) {
                mTVCore.setAuthItems(keysBean.user_id, keysBean.peer_id, keysBean.session_key);
            }
        }

        mTVCore.setTVListener(new TVListener() { //C3647k
            @Override // com.tvbus.engine.TVListener
            public void onInfo(String result) {
                Log.i(TAG, "[TVCore] onInfo ... " + result);
                boolean isOk = onTVCoreEvent("onInfo", result);
                if (isOk) {
                    mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_REFRESHINFO);
                }
                mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_CHECKPLAYER);
            }

            @Override // com.tvbus.engine.TVListener
            public void onInited(String result) {
                Log.i(TAG, "[TVCore] onInited ... " + result);
                boolean isOk = onTVCoreEvent("onInited", result);
                if (isOk) {
                    Message msg = new Message();
                    msg.what = Constant.MSG_PLAYER_LOADED;
                    MainActivity.SendMessage(msg);
                }
            }

            @Override // com.tvbus.engine.TVListener
            public void onPrepared(String result) {
                Log.i(TAG, "[TVCore] onPrepared ... " + result);
                boolean isOk = onTVCoreEvent("onPrepared", result);
                if (isOk) {
                    Message msg = new Message();
                    Bundle params = new Bundle();
                    params.putString("videoPath", mPlaybackUrl);

                    msg.what = Constant.MSG_PLAYER_START_PLAYBACK;
                    msg.setData(params);
                    mMsgHandler.sendMessage(msg);
                    mIsEnded = false;
                }
            }

            @Override // com.tvbus.engine.TVListener
            public void onQuit(String result) {
                Log.i(TAG, "[TVCore] onQuit ... " + result);
            }

            @Override // com.tvbus.engine.TVListener
            public void onStart(String result) {
                onTVCoreEvent("onStart", result);
                Log.i(TAG, "[TVCore] onStart ... " + result);
            }

            @Override // com.tvbus.engine.TVListener
            public void onStop(String result) {
                onTVCoreEvent("onStop", result);
                Log.e(TAG, "TVCore onStop ... " + result);
            }
        });

        SopApplication.getAppContext().startService(new Intent(SopApplication.getAppContext(), TVService.class));
    }

    public boolean onTVCoreEvent(String key, String result) {
        JSONObject obj;
        try {
            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            obj = null;
        }
        if (obj == null)
            return false;
        int code = key.hashCode();
        if (key.equals("onInited") && code == 1214334062) { //0 cond_b
            int tvcore = obj.optInt("tvcore", 1);
            return tvcore != 1;
        } else if (key.equals("onStart") && code == -1336895037) { //1 cond_a
            return true;
        } else if (key.equals("onPrepared") && code == 1490401084) { //2 cond_6
            String url = obj.optString("http", "");
            if (!url.isEmpty()) {
                mPlaybackUrl = url;
                return true;
            }
            return false;
        } else if (key.equals("onInfo") && code == -1013260499) { //3
            this.mBuffer  = obj.optInt("buffer", 0);
            this.mDlRate = obj.optInt("download_rate", 0);
            this.mTmPlayerConn = obj.optInt("hls_last_conn", 0);
            return true;
        } else if (key.equals("onStop") && code == -1012956543) { //4
            int errno = obj.optInt("errno", 0);
            if (errno != 0) {
                Message msg = new Message();
                msg.what = Constant.MSG_PLAYER_STOP;
                msg.arg1 = errno;
                mMsgHandler.sendMessage(msg);
                return true;
            }
            return false;
        } else if (key.equals("onQut") && code == 105869425) { //5
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.InitedEvent event) {
        vod_ecode = event.errno;
        //String msg = "##### initMsg:" + event.errno;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.StartEvent event) {
        String log = "##### StartMessage:" + event.errno + " url:" + event.url;
        Log.e(TAG, log);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.StopEvent event) {
        String log = "##### StopMessage:" + event.errno + " url:" + event.url;
        Log.e(TAG, log);

        vod_ecode = event.errno;
        if (vod_ecode != 0) {
            Message msg = new Message();
            msg.what = Constant.MSG_PLAYER_STOP;
            msg.arg1 = vod_ecode;
            mMsgHandler.sendMessage(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.PreparedEvent event) {
        String url = event.url;
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("videoPath", url);
        msg.what = Constant.MSG_PLAYER_START_PLAYBACK;
        msg.setData(data);
        mMsgHandler.sendMessage(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.InfoEvent event) {
        vod_ecode = event.errno;
        if (mBsMode == Config.BS_MODE.BSPALYBACK || mBsMode == Config.BS_MODE.BSVOD) {
            mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_CHECKPLAYER);
            SetDownloadRate(PrefUtils.m2251a(event.download_rate));
            if (mPlayerMode == 1 && EXO_PLAYER.getPlayWhenReady()) {
                this.mPlayerCurrentPos = (int) EXO_PLAYER.getCurrentPosition();
                this.mPlayerDuration = (int) EXO_PLAYER.getDuration();
                int progress = EXO_PLAYER.getBufferedPercentage();
                //String log = "EXO_PLAYER currentPosition " + f16889rb + " duration " + f16891sb;
                if (mPlayerDuration > 0) {
                    this.playerCurrentTime.setText(PrefUtils.m2253a(mPlayerCurrentPos / 1000));
                    this.playerDurationTime.setText(PrefUtils.m2253a(mPlayerDuration / 1000));
                    this.playerSeekbar.setProgress((mPlayerCurrentPos * 100) / mPlayerDuration);
                    this.playerSeekbar.setSecondaryProgress(progress);
                    //String log = "EXO_PLAYER buffer: " + progress;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.QuitEvent event) {
        String log = "##### QuitMessage:" + event.errno;
        Log.e(TAG, log);
    }

    public void startChannel(String videoURL, String videoName, Config.BS_MODE bsMode) {
        Log.e(TAG, "startChannel videoURL: " + videoURL);
        Log.e(TAG, "startChannel videoName: " + videoName);
        Log.e(TAG, "startChannel bsMode: " + bsMode);
        this.mBsMode = bsMode;
        if (videoURL == null || videoURL.equals("")) {
            return;
        }
        if (mBsMode == Config.BS_MODE.BSLIVE) {
            //String log5 = "mTVCore.start " + videoURL;
            if (vod_ecode == Config.ErrorTypes.Err_210.value) {
                Message msg = new Message();
                msg.what = Constant.MSG_PLAYER_STOP;
                msg.arg1 = Config.ErrorTypes.Err_210.value;
                mMsgHandler.sendMessage(msg);
                return;
            }
            mMPCheckTime = Long.MAX_VALUE;
            mTVCore.start(videoURL);
        } else if (mBsMode == Config.BS_MODE.BSPALYBACK || mBsMode == Config.BS_MODE.BSVOD) {
            if (vod_ecode == Config.ErrorTypes.Err_210.value) {
                Message msg = new Message();
                msg.what = Constant.MSG_PLAYER_STOP;
                msg.arg1 = Config.ErrorTypes.Err_210.value;
                mMsgHandler.sendMessage(msg);
                return;
            }
            if (mTVCore != null) {
                mTVCore.stop();
            }
            int server = PrefUtils.getPrefInt(Config.SERVER, 0).intValue();
            if (server != 0) {
                videoURL = videoURL.replaceFirst("\\.", "-b" + server + ".");
                //String log5_1 = "vidoeURL change -> " + videoURL;
            }
            mPlayerCurrentPos = 0;
            mPlayerSeekPos = 0;
            Libtvcar.start(videoURL);
        } else if (mBsMode == Config.BS_MODE.STATIC) {
            //String log6 = "STATIC vidoeURL " + videoURL;
            Message msg = new Message();
            Bundle params = new Bundle();
            params.putString("videoPath", videoURL);
            msg.what = Constant.MSG_PLAYER_START_PLAYBACK;
            msg.setData(params);
            mMsgHandler.sendMessage(msg);
        } else {
            return;
        }
        mIsEnded = true;
        stopVideoPlaying();
        showPlayerUI();
        loadingProgress.setVisibility(View.VISIBLE);
        if (mBsMode == Config.BS_MODE.BSPALYBACK) {
            playerCurrentTime.setText("00:00");
            playerDurationTime.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_play_back) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.BSVOD) {
            playerCurrentTime.setText("00:00");
            playerDurationTime.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.BSLIVE) {
            playerCurrentTime.setText(R.string.buffer);
            playerDurationTime.setText("0/100");
            videoName = SopApplication.getAppContext().getString(R.string.video_live) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.STATIC) {
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        }
        programName.setText(videoName);
        playerSeekbar.setProgress(0);
        playerSeekbar.setSecondaryProgress(0);
        SetDownloadRate("0B/S");
        hideProcessBarWithDelay(0);
    }

    public static boolean isPlaying() {
        if (mPlayerMode == 0) {
            return (SYS_PLAYER != null && SYS_PLAYER.isPlaying());
        } else {
            return EXO_PLAYER != null && EXO_PLAYER.getPlayWhenReady() &&
                    EXO_PLAYER.getPlaybackState() == Player.STATE_READY; //3
        }
    }

    // player related
    private void checkPlayer() {
        if (mIsEnded) {
            return;
        }
        if (mBsMode == Config.BS_MODE.BSLIVE && mTmPlayerConn > 15 && mBuffer > 50) {
            if (mPlayerMode == 0) {
                SYS_PLAYER.stopPlayback();
            }
            if (mPlayerMode == 1) {
                EXO_PLAYER.setPlayWhenReady(false);
            }
        }
        if (System.nanoTime() > mMPCheckTime) {
            //String log = "MPlayerChecker is running - isPlaying:" + isPlaying() + " playerRestartCount:" + f16810s + " mTmPlayerConn:" + mTmPlayerConn + " isVideoPausedByUser:" + f16859cb;
            if (isPlaying()) {
                return;
            }
            mMsgHandler.removeMessages(Constant.MSG_PLAYER_RESUME);
            mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_RESUME);
        }
    }

    public void resumePlayer() {
        //String text = "resumePlayer isPlaying:" + isPlaying();
        if (mPlayerMode == 0) {
            if (SYS_PLAYER != null) {
                if (SYS_PLAYER.isPlaying()) {
                    SYS_PLAYER.stopPlayback();
                }
                SYS_PLAYER.setVideoPath(this.mCurrentVideoPath);
                SYS_PLAYER.start();
                if (this.mPlayerSeekPos > 0) {
                    //String text = "resumePlayer seek to:" + mPlayerSeekPos;
                    SYS_PLAYER.seekTo(this.mPlayerSeekPos);
                }
            }
        } else
            EXO_PLAYER.setPlayWhenReady(true);
    }

    public void SetDownloadRate(String text) {
        if (this.dlLayout.getVisibility() == View.GONE) {
            this.dlLayout.setVisibility(View.VISIBLE);
        }
        this.dlRate.setText(text);
    }

    public void startPlaying(String url) {
        Log.e(TAG, "startPlaying URL: " + url);
        this.mMPCheckTime = System.nanoTime() + MP_START_CHECK_INTERVAL;
        if (this.playerStatus.getVisibility() == View.VISIBLE) {
            this.playerStatus.setVisibility(View.GONE);
        }
        mIsEnded = false;
        //DefaultDataSourceFactory factory = new DefaultDataSourceFactory(SopApplication.getAppContext(), "tvbus", (TransferListener) null);
        DefaultDataSource.Factory factory = new DefaultDataSource.Factory(SopApplication.getAppContext());
        factory.setTransferListener(null);

        String videoType = "-MPEGTS";
        String playerType = "EXO";
        if (mPlayerMode == 1) {
            if (url.indexOf(".m3u8") >= 0) {
                //mExoPlayer.prepare(new HlsMediaSource.Factory(factory).createMediaSource(Uri.parse(url)));
                EXO_PLAYER.setMediaSource(new HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(url)));
                videoType = "-HLS";
            } else {
                //mExoPlayer.prepare(new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(url)));

                DefaultHttpDataSource.Factory httpFactory = new DefaultHttpDataSource.Factory();
                httpFactory.setUserAgent("tvbus");
                httpFactory.setTransferListener(null);

                EXO_PLAYER.setMediaSource(new ProgressiveMediaSource.Factory(httpFactory, TsExtractor.FACTORY).createMediaSource(MediaItem.fromUri(url)));
            }
            EXO_PLAYER.prepare();
            EXO_PLAYER.setPlayWhenReady(true);
        } else if (mPlayerMode == 0) {
            videoType = url.indexOf(".m3u8") >= 0 ? "-HLS" : "-MPEGTS";
            SYS_PLAYER.setVideoURI(Uri.parse(url));
            playerType = "SYS";
        }
    }

    public void stopPlayer() {
        if (mTVCore != null) {
            mTVCore.stop();
        }
        stopVideoPlaying();
    }

    public void stopVideoPlaying() {
        if (mPlayerMode == 0 && SYS_PLAYER != null) {
            SYS_PLAYER.stopPlayback();
            SYS_PLAYER.setVisibility(View.INVISIBLE);
        } else if (exoPlayerView != null) {
            EXO_PLAYER.stop();
            exoPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showPlayerUI() {
        if (mPlayerMode == 0) {
            if (SYS_PLAYER != null)
                SYS_PLAYER.setVisibility(View.VISIBLE);
        } else {
            if (exoPlayerView != null)
                exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    public void showAuthError(int errorCode) {
        String text;
        if (errorCode == Config.ErrorTypes.Err_104.value) {
            text = SopApplication.getAppContext().getString(R.string.channel_offline);
        } else if (errorCode == Config.ErrorTypes.Err_105.value) {
            text = SopApplication.getAppContext().getString(R.string.user_no_login);
        } else {
            text = errorCode == Config.ErrorTypes.Err_210.value ? SopApplication.getAppContext().getString(R.string.user_repeated_logon) : "";
        }
        if (text.equals("")) {
            return;
        }
    }

    public static int SP_PLAYER = Config.DEFAULT_PLAYER;
    public Config.VIDEO_TYPE videoType;
    private Config.MenuType menuType;
    public String channelId;
    public String vodSubTitlesId = null;
    public String mPlaybackUrl = null;
    public long systemStartTimeVod = 0;
    public static int VOD_AUTH_ECODE = 0;
    public List<VodChannelBean.Episode.SubtitlesBean> subtitles = null;
    public String subtitleUrl = null;
    public static int subtitleIndex = 0;

    public static boolean userPlayVideo = true;

    public HistoryBean mHistoryBean;
    public int lastCurrentPosition = 0;
    public int position = 0;
    public int duration = 0;
    public static long playerRestartCount = 0;

    public static long f10865P = 0;
    public static long f10871V = 0;
    public static long f10873X = 0;
    public static long f10874Y = 0;
    public static long f10875Z = 0;
    public static long peers = 0;

    public void saveHistoryUpdate() {
        int i;
        HistoryBean historyBean;
        Config.VIDEO_TYPE video_type;
        if (this.mHistoryBean != null) {
            int i2 = this.lastCurrentPosition;
            if (i2 > 0 && (i = this.duration) > 0 && ((video_type = (historyBean = this.mHistoryBean).videoType) == Config.VIDEO_TYPE.PLAYBACK || video_type == Config.VIDEO_TYPE.BSVOD)) {
                historyBean.duration = i;
                historyBean.lastPosition = i2;
            }
            HistoryInstance.saveUpdate();
            HistoryLayout.updateDataSet();
        }
    }

    public final void showError(final int errCode) {
        String strError = "";
        if (errCode == Config.Errors.CHANNEL_OFFLINE.code) {
            strError = mContext.getString(R.string.channel_offline);
        }
        else if (errCode == Config.Errors.NEED_AUTH.code) {
            strError = mContext.getString(R.string.user_no_login);
        }
        else if (errCode == Config.Errors.MULTIPLE_LOGIN.code) {
            strError = mContext.getString(R.string.user_repeated_logon);
        }
        if (!strError.equals("")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(strError);
            sb.append(" (");
            sb.append(errCode);
            sb.append(")");
            new PopMsg(mContext.getApplicationContext(), mContext.getString(R.string.errorTitle), sb.toString())
                    .showAtLocation((View)this.rootFrameLayout, 17, 0, 0);
        }
    }

    public final void playVideo(final Bundle bundle) {
        final String string = bundle.getString("chid");
        final String string2 = bundle.getString("name");
        final String string3 = bundle.getString("subId");
        final String string4 = bundle.getString("subTitle");
        final String string5 = bundle.getString("url");
        final String string6 = bundle.getString("season");
        final String string7 = bundle.getString("episode");
        final boolean boolean1 = bundle.getBoolean("restricted");
        this.videoType = Config.VIDEO_TYPE.valueOf(bundle.getString("type"));
        this.menuType = Config.MenuType.valueOf(bundle.getString("menuType"));
        this.channelId = string;
        this.vodSubTitlesId = string3;
        this.subtitles = null;
        this.subtitleUrl = null;
        if (this.videoType == Config.VIDEO_TYPE.BSVOD) {
            final VodChannelBean fullChannelBean = VodChannelInstance.getFullChannelBean(string);
            this.subtitles = null;
            if (fullChannelBean != null) {
                final List<VodChannelBean.Episode> episodes = fullChannelBean.getEpisodes();
                if (episodes != null) {
                    for (final VodChannelBean.Episode episode : episodes) {
                        if (episode.id == Integer.parseInt(this.vodSubTitlesId)) {
                            final List<VodChannelBean.Episode.SubtitlesBean> subtitle = episode.getSubtitles();
                            if (subtitle != null && subtitle.size() != 0) {
                                this.subtitles = new ArrayList();
                                final VodChannelBean.Episode.SubtitlesBean subtitlesBean = new VodChannelBean.Episode.SubtitlesBean();
                                subtitlesBean.code = "of_OF";
                                subtitlesBean.url = null;
                                this.subtitles.add(0, subtitlesBean);
                                this.subtitles.addAll(subtitle);
                            }
                            break;
                        }
                    }
                }
            }
            this.subtitleUrl = this.getDefaultSubtitleUrl();
        }
        userPlayVideo = true;
        stopVideoPlaying();
        showPlayerUI();

        final HistoryBean mHistoryBean = new HistoryBean();
        this.mHistoryBean = mHistoryBean;
        mHistoryBean.channelId = string;
        final Config.VIDEO_TYPE videoType = this.videoType;
        if ((videoType == Config.VIDEO_TYPE.BSLIVE || videoType == Config.VIDEO_TYPE.PLAYBACK) && string != null) {
            try {
                mHistoryBean.chid = Integer.parseInt(string);
            }
            catch (final Exception ex) {}
        }
        final HistoryBean mHistoryBean2 = this.mHistoryBean;
        mHistoryBean2.name = string2;
        mHistoryBean2.subId = string3;
        mHistoryBean2.subTitle = string4;
        mHistoryBean2.Season = string6;
        mHistoryBean2.Episode = string7;
        mHistoryBean2.date = new Date();
        this.mHistoryBean.videoType = this.videoType;
        String text = string4;
        if (!"".equals(string6)) {
            text = string4;
            if (!"".equals(string7)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("S");
                sb.append(string6);
                sb.append("E");
                sb.append(string7);
                sb.append(" - ");
                sb.append(string4);
                text = sb.toString();
            }
        }
        final HistoryBean mHistoryBean3 = this.mHistoryBean;
        final Config.VIDEO_TYPE videoType2 = mHistoryBean3.videoType;
        final Config.VIDEO_TYPE playback = Config.VIDEO_TYPE.PLAYBACK;
        if (videoType2 != playback && videoType2 != Config.VIDEO_TYPE.BSVOD) {
            if (videoType2 == Config.VIDEO_TYPE.BSLIVE) {
                if (!boolean1) {
                    HistoryInstance.addLiveHistory(mHistoryBean3);
                }
            }
        }
        else {
            final HistoryBean getLastHistory = HistoryInstance.GetLastHistory(string, string3);
            int lastPosition;
            if (getLastHistory == null || (lastPosition = getLastHistory.lastPosition) <= 0) {
                lastPosition = 0;
            }
            int duration;
            if (getLastHistory == null || (duration = getLastHistory.duration) <= 0) {
                duration = 0;
            }
            final HistoryBean mHistoryBean4 = this.mHistoryBean;
            mHistoryBean4.lastPosition = lastPosition;
            mHistoryBean4.duration = duration;
            if (lastPosition > 0) {
                //sendShowToastEvent(SopApplication.getSopContext().getString(2131820563), Config.f8899S);
                //SopCast.handler.sendEmptyMessageDelayed(260, Config.f8899S * 1000L);
            }
            if (!boolean1) {
                HistoryInstance.addVodHistory(this.mHistoryBean);
            }
        }
        HistoryLayout.updateDataSet();
        if (string5 != null && !string5.equals("")) {
            this.mPlaybackUrl = null;
            final Config.VIDEO_TYPE videoType3 = this.videoType;
            final Config.VIDEO_TYPE bslive = Config.VIDEO_TYPE.BSLIVE;
            Label_1086: {
                Config.Errors errors;
                Message message;
                if (videoType3 == bslive) {
                    errors = Config.Errors.MULTIPLE_LOGIN;
                    if (VOD_AUTH_ECODE != errors.code) {
                        this.systemStartTimeVod = Long.MAX_VALUE;
                        mTVCore.start(string5);
                        break Label_1086;
                    }
                    message = new Message();
                }
                else if ((videoType3 == playback || videoType3 == Config.VIDEO_TYPE.BSVOD) && Config.enableVodFragment) {
                    errors = Config.Errors.MULTIPLE_LOGIN;
                    if (VOD_AUTH_ECODE != errors.code) {
                        if (mTVCore != null) {
                            mTVCore.stop();
                        }
                        final int intValue = Utils.getIntegerValue(Config.SERVER, 0);
                        String replaceFirst = string5;
                        if (intValue != 0) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("-b");
                            sb2.append(intValue);
                            sb2.append(".");
                            replaceFirst = string5.replaceFirst("\\.", sb2.toString());
                        }
                        this.position = 0;
                        this.lastCurrentPosition = 0;
                        this.duration = 0;
                        Libtvcar.start(replaceFirst);
                        break Label_1086;
                    }
                    message = new Message();
                }
                else {
                    if (videoType3 != Config.VIDEO_TYPE.f8646d) {
                        return;
                    }
                    final Message message2 = new Message();
                    final Bundle data = new Bundle();
                    data.putString("videoPath", string5);
                    message2.what = 81;
                    message2.setData(data);
                    MainActivity.mMsgHandler.sendMessage(message2);
                    break Label_1086;
                }
                message.what = 99;
                message.arg1 = errors.code;
                MainActivity.mMsgHandler.sendMessage(message);
                return;
            }
            ((View)this.loadingProgress).setVisibility(View.VISIBLE);
            final Config.VIDEO_TYPE videoType4 = this.videoType;
            Label_1292: {
                StringBuilder sb4;
                if (videoType4 == playback) {
                    this.playerCurrentTime.setText("00:00");
                    this.playerDurationTime.setText("00:00");
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(mContext.getString(R.string.video_play_back));
                    sb3.append(": ");
                    sb3.append(string2);
                    sb3.append(" - ");
                    sb3.append(text);
                    sb4 = sb3;
                }
                else {
                    if (videoType4 == Config.VIDEO_TYPE.BSVOD) {
                        this.playerCurrentTime.setText("00:00");
                        this.playerDurationTime.setText("00:00");
                        break Label_1292;
                    }
                    if (videoType4 != bslive) {
                        text = string2;
                        break Label_1292;
                    }
                    this.playerCurrentTime.setText(R.string.buffer);
                    this.playerDurationTime.setText((CharSequence)"0/100");
                    sb4 = new StringBuilder();
                    sb4.append(mContext.getString(R.string.video_live));
                    sb4.append(": ");
                    sb4.append(string2);
                }
                text = sb4.toString();
            }
            programName.setText((CharSequence)text);
            playerSeekbar.setProgress(0);
            playerSeekbar.setSecondaryProgress(0);
            this.updateDlRate("0B/S");
            this.showProcessBar(0);
            MainActivity.mMsgHandler.sendEmptyMessage(100);
//            if (this.videoType == Config.VIDEO_TYPE.BSVOD && this.subtitles != null) {
//                this.showSubtitle();
//            }
//            else {
//                ((View)this.subtitleLayout).setVisibility(8);
//            }
            playerRestartCount = 0L;
            this.systemStartTimeVod = Long.MAX_VALUE;
            f10865P = System.currentTimeMillis();
            f10871V = 0L;
            peers = 0L;
            f10873X = 0L;
            f10874Y = 0L;
            f10875Z = 0L;
//            this.bufferView.setText((CharSequence)"0");
//            this.drView.setText((CharSequence)"0");
//            this.urView.setText((CharSequence)"0");
//            this.totalView.setText((CharSequence)"0");
//            this.peersView.setText((CharSequence)"0");
//            this.retvView.setText((CharSequence)"0");
//            this.prepareView.setText((CharSequence)"0.0/0.0");
//            this.durationView.setText((CharSequence)"0D 00:00:00");
        }
    }

    public void showProcessBar(int i) {
        this.playerProcessBar.setVisibility(View.VISIBLE);
        if (i > 0) {
            MainActivity.mMsgHandler.removeMessages(96);
            MainActivity.mMsgHandler.sendEmptyMessageDelayed(96, i);
        }
    }

    public final void updateDlRate(String str) {
        if (Config.showDownloadRate) {
            if (this.dlLayout.getVisibility() == View.GONE) {
                this.dlLayout.setVisibility(View.VISIBLE);
            }
            this.dlRate.setText(str);
        }
    }

    public final String getDefaultSubtitleUrl() {
        VodChannelBean.Episode.SubtitlesBean subtitlesBean;
        if (this.subtitles == null) {
            return null;
        }
        String str = Config.DEFAULT_CHOOSED_LANG;
        String value = Utils.getValue(str, BSCF.language + "_\n" + BSCF.countryCode);
        String[] strArr = {value, BSCF.language + "_\n" + BSCF.countryCode};
        int i = 0;
        loop0: while (true) {
            if (i >= 2) {
                String value2 = Utils.getValue(Config.DEFAULT_CHOOSED_AUDIO_LANG, BSCF.language);
                for (int i2 = 0; i2 < this.subtitles.size(); i2++) {
                    if (this.subtitles.get(i2).getCode().equals(value2)) {
                        this.subtitles.get(i2).getCode();
                        subtitleIndex = i2;
                        subtitlesBean = this.subtitles.get(i2);
                    }
                }
                return null;
            }
            String str2 = strArr[i];
            for (int i3 = 0; i3 < this.subtitles.size(); i3++) {
                if (this.subtitles.get(i3).getCode().contains(str2)) {
                    this.subtitles.get(i3).getCode();
                    subtitleIndex = i3;
                    subtitlesBean = this.subtitles.get(i3);
                    break loop0;
                }
            }
            i++;
        }
        return subtitlesBean.getUrl();
    }
}