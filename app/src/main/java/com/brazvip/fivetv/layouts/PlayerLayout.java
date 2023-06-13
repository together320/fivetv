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

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.TVCarService;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.utils.PrefUtils;
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

import io.binstream.libtvcar.Libtvcar;

public class PlayerLayout extends FrameLayout {
    public static final String TAG = "PlayerLayout";
    public static TVCore mTVCore = null;
    public static StyledPlayerView mPlayerView = null;
    public static VideoView mVideoView = null;
    public static Handler mMsgHandler = null;
    public static ExoPlayer mExoPlayer = null;

    private TextView mProgramNameText = null;
    private TextView mCurrentTimeText = null;
    private TextView mDurationTimeText = null;
    private TextView mDlRateText = null;
    public SeekBar mPlayerSeekBar;
    public RelativeLayout mDlLayout;
    public RelativeLayout mPlayerProcessBar;
    public ProgressBar mLoadingProgress;
    public ImageView mPlayerStatusImage;

    public static int mPlayerMode = 1;   //0 - Android Media Player, 1 - ExoPlayer
    private int mBuffer;
    private int mDlRate;
    private int mTmPlayerConn;
    private long mMPCheckTime = 0;
    private static String playbackUrl;
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
                        mPlayerSeekBar.setProgress(mBuffer);
                        mDurationTimeText.setText(mBuffer + "/100");
                        SetDownloadRate(PrefUtils.m2251a(mDlRate));
                        break;
                    case Constant.MSG_PLAYER_PLAY:
                        mCurrentVideoPath = message.getData().getString("videoPath");
                        startPlaying(mCurrentVideoPath);
                        break;
                    case Constant.MSG_PLAYER_HIDEPROCESSBAR:
                        mPlayerProcessBar.setVisibility(View.GONE);
                        break;
                    case Constant.MSG_PLAYER_CHECKPLAYER:
                        checkPlayer();
                        break;
                    case Constant.MSG_PLAYER_RESUME:
                        mMPCheckTime = System.nanoTime() + 4000000000L;
                        resumePlayer();
                        break;
                    case Constant.MSG_PLAYER_STOP:
                        showAuthError(message.arg1);
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void initComponents() {
        this.mProgramNameText = (TextView) findViewById(R.id.program_name);
        this.mDlLayout = (RelativeLayout) findViewById(R.id.dl_layout);
        this.mDlRateText = (TextView) findViewById(R.id.dl_rate);
        this.mDlLayout.setVisibility(View.GONE);
        this.mLoadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        this.mPlayerProcessBar = (RelativeLayout) findViewById(R.id.player_process_bar);
        this.mPlayerProcessBar.setVisibility(View.GONE);
        this.mCurrentTimeText = (TextView) findViewById(R.id.player_current_time);
        this.mDurationTimeText = (TextView) findViewById(R.id.player_duration_time);
        this.mPlayerSeekBar = (SeekBar) findViewById(R.id.player_seekbar);
        this.mPlayerStatusImage = (ImageView) findViewById(R.id.player_status);
    }

    private void initExoPlayer() {
        mPlayerView = findViewById(R.id.exoPlayerView);
        //mPlayerView.setOnKeyListener(this);
        //mPlayerView.setOnClickListener(this);
        //mPlayerView.setOnTouchListener(this);
        mPlayerView.setClickable(true);
        mPlayerView.setFocusable(true);
        mPlayerView.setControllerAutoShow(false);
        mPlayerView.setUseController(false);
        mPlayerView.setKeepScreenOn(true);
        //ExoPlayer 2.11.3
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(SopApplication.getAppContext()).build();
//        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(SopApplication.getAppContext());
//        LoadControl loadControl = new DefaultLoadControl.Builder().build();
//        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(SopApplication.getAppContext());
//        //renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);
//        Looper looper = PrefUtils.getLooper();
//        AnalyticsCollector analyticsCollector = new AnalyticsCollector(Clock.DEFAULT);
        mExoPlayer = new ExoPlayer.Builder(SopApplication.getAppContext()).build();

        mExoPlayer.addListener(new Player.Listener() {

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
                    mLoadingProgress.setVisibility(View.GONE);
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
                mExoPlayer.stop();
                mMPCheckTime = System.nanoTime();
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

        mPlayerView.setPlayer(mExoPlayer);
        //mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    public void hideProcessBarWithDelay(int delay) {
        this.mPlayerProcessBar.setVisibility(View.VISIBLE);
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
        mTVCore.setAuthUrl(AuthInstance.mAuthInfo.service.auth_url_sdk);
        String username = PrefUtils.getPrefString("username", "");
        String password = PrefUtils.getPrefString("password", "");
        if (!username.contains("@"))
            username += Constant.DEFAULT_MAIL_SUFFIX;
        mTVCore.setUsername(username);
        mTVCore.setPassword(password);

        mTVCore.setTVListener(new TVListener() { //C3647k
            @Override // com.tvbus.engine.TVListener
            public void onInfo(String result) {
                boolean isOk = onTVCoreEvent("onInfo", result);
                if (isOk) {
                    //text = "TVCore onInfo ... " + result;
                    mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_REFRESHINFO);
                }
                mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_CHECKPLAYER);
            }

            @Override // com.tvbus.engine.TVListener
            public void onInited(String result) {
                boolean isOk = onTVCoreEvent("onInited", result);

                if (isOk) {
                    Message msg = new Message();
                    msg.what = Constant.MSG_PLAYER_LOADED;
                    MainActivity.SendMessage(msg);
                }
            }

            @Override // com.tvbus.engine.TVListener
            public void onPrepared(String result) {
                boolean isOk = onTVCoreEvent("onPrepared", result);
                if (isOk) {
                    Message msg = new Message();
                    Bundle params = new Bundle();
                    params.putString("videoPath", playbackUrl);

                    msg.what = Constant.MSG_PLAYER_PLAY;
                    msg.setData(params);
                    mMsgHandler.sendMessage(msg);
                    mIsEnded = false;
                }
                //text = "TVCore onPrepared ... " + text;
            }

            @Override // com.tvbus.engine.TVListener
            public void onQuit(String result) {
                //text = "TVCore onQuit ... " + text;
            }

            @Override // com.tvbus.engine.TVListener
            public void onStart(String result) {
                onTVCoreEvent("onStart", result);
                //text = "TVCore onStart ... " + text;
            }

            @Override // com.tvbus.engine.TVListener
            public void onStop(String result) {
                onTVCoreEvent("onStop", result);
                //text = "TVCore onStop ... " + text;
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
                playbackUrl = url;
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
        msg.what = Constant.MSG_PLAYER_PLAY;
        msg.setData(data);
        mMsgHandler.sendMessage(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TVCarService.InfoEvent event) {
        vod_ecode = event.errno;
        if (mBsMode == Config.BS_MODE.BSPALYBACK || mBsMode == Config.BS_MODE.BSVOD) {
            mMsgHandler.sendEmptyMessage(Constant.MSG_PLAYER_CHECKPLAYER);
            SetDownloadRate(PrefUtils.m2251a(event.download_rate));
            if (mPlayerMode == 1 && mExoPlayer.getPlayWhenReady()) {
                this.mPlayerCurrentPos = (int) mExoPlayer.getCurrentPosition();
                this.mPlayerDuration = (int) mExoPlayer.getDuration();
                int progress = mExoPlayer.getBufferedPercentage();
                //String log = "EXO_PLAYER currentPosition " + f16889rb + " duration " + f16891sb;
                if (mPlayerDuration > 0) {
                    this.mCurrentTimeText.setText(PrefUtils.m2253a(mPlayerCurrentPos / 1000));
                    this.mDurationTimeText.setText(PrefUtils.m2253a(mPlayerDuration / 1000));
                    this.mPlayerSeekBar.setProgress((mPlayerCurrentPos * 100) / mPlayerDuration);
                    this.mPlayerSeekBar.setSecondaryProgress(progress);
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
            msg.what = Constant.MSG_PLAYER_PLAY;
            msg.setData(params);
            mMsgHandler.sendMessage(msg);
        } else {
            return;
        }
        mIsEnded = true;
        stopVideoPlaying();
        showPlayerUI();
        mLoadingProgress.setVisibility(View.VISIBLE);
        if (mBsMode == Config.BS_MODE.BSPALYBACK) {
            mCurrentTimeText.setText("00:00");
            mDurationTimeText.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_play_back) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.BSVOD) {
            mCurrentTimeText.setText("00:00");
            mDurationTimeText.setText("00:00");
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.BSLIVE) {
            mCurrentTimeText.setText(R.string.buffer);
            mDurationTimeText.setText("0/100");
            videoName = SopApplication.getAppContext().getString(R.string.video_live) + ": " + videoName;
        } else if (mBsMode == Config.BS_MODE.STATIC) {
            videoName = SopApplication.getAppContext().getString(R.string.video_vod) + ": " + videoName;
        }
        mProgramNameText.setText(videoName);
        mPlayerSeekBar.setProgress(0);
        mPlayerSeekBar.setSecondaryProgress(0);
        SetDownloadRate("0B/S");
        hideProcessBarWithDelay(0);
    }

    public static boolean isPlaying() {
        if (mPlayerMode == 0) {
            return (mVideoView != null && mVideoView.isPlaying());
        } else {
            return mExoPlayer != null && mExoPlayer.getPlayWhenReady() &&
                    mExoPlayer.getPlaybackState() == Player.STATE_READY; //3
        }
    }

    // player related
    private void checkPlayer() {
        if (mIsEnded) {
            return;
        }
        if (mBsMode == Config.BS_MODE.BSLIVE && mTmPlayerConn > 15 && mBuffer > 50) {
            if (mPlayerMode == 0) {
                mVideoView.stopPlayback();
            }
            if (mPlayerMode == 1) {
                mExoPlayer.setPlayWhenReady(false);
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
            if (mVideoView != null) {
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                }
                mVideoView.setVideoPath(this.mCurrentVideoPath);
                mVideoView.start();
                if (this.mPlayerSeekPos > 0) {
                    //String text = "resumePlayer seek to:" + mPlayerSeekPos;
                    mVideoView.seekTo(this.mPlayerSeekPos);
                }
            }
        } else
            mExoPlayer.setPlayWhenReady(true);
    }

    public void SetDownloadRate(String text) {
        if (this.mDlLayout.getVisibility() == View.GONE) {
            this.mDlLayout.setVisibility(View.VISIBLE);
        }
        this.mDlRateText.setText(text);
    }

    public void startPlaying(String url) {
        this.mMPCheckTime = System.nanoTime() + MP_START_CHECK_INTERVAL;
        if (this.mPlayerStatusImage.getVisibility() == View.VISIBLE) {
            this.mPlayerStatusImage.setVisibility(View.GONE);
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
                mExoPlayer.setMediaSource(new HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(url)));
                videoType = "-HLS";
            } else {
                //mExoPlayer.prepare(new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(url)));

                DefaultHttpDataSource.Factory httpFactory = new DefaultHttpDataSource.Factory();
                httpFactory.setUserAgent("tvbus");
                httpFactory.setTransferListener(null);

                mExoPlayer.setMediaSource(new ProgressiveMediaSource.Factory(httpFactory, TsExtractor.FACTORY).createMediaSource(MediaItem.fromUri(url)));
            }
            mExoPlayer.prepare();
            mExoPlayer.setPlayWhenReady(true);
        } else if (mPlayerMode == 0) {
            videoType = url.indexOf(".m3u8") >= 0 ? "-HLS" : "-MPEGTS";
            mVideoView.setVideoURI(Uri.parse(url));
            playerType = "SYS";
        }
    }

    public void stopVideoPlaying() {
        if (mPlayerMode == 0 && mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.setVisibility(View.INVISIBLE);
        } else if (mPlayerView != null) {
            mExoPlayer.stop();
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showPlayerUI() {
        if (mPlayerMode == 0) {
            if (mVideoView != null)
                mVideoView.setVisibility(View.VISIBLE);
        } else {
            if (mPlayerView != null)
                mPlayerView.setVisibility(View.VISIBLE);
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
}