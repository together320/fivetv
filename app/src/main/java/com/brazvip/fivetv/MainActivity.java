package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.beans.AuthInfo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;
import com.tvbus.engine.TVService;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static TVCore mTVCore = null;
    public static String username = "";
    public static String password = "";
    public static AuthInfo authInfo = null;
    public static Handler mMsgHandler = null;

    private int mBuffer;
    private int mTmPlayerConn;
    private long mMPCheckTime = 0;
    private static String playbackUrl;

    private final static long MP_START_CHECK_INTERVAL = 10 * 1000 * 1000 * 1000L; // 10 second

    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        Button btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        initMessageHandler();
        initExoPlayer();
    }

    public void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 15:
                        initTVCore();
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void initTVCore() {
        mTVCore = TVCore.getInstance();
        if (mTVCore == null)
            return;
        mTVCore.setMKBroker(authInfo.service.mk_broker);
        mTVCore.setAuthUrl(authInfo.service.auth_url_sdk);
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

        updateStatusView("Initing...");
        startService(new Intent(this, TVService.class));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_login) {
            login();
        }
        else if (id == R.id.btn_play) {
            play();
        }
    }

    private void login() {
        TextView input_username = (TextView)findViewById(R.id.input_username);
        TextView input_password = (TextView)findViewById(R.id.input_password);

        username = input_username.getText().toString() + "@p2five.com";
        password = input_password.getText().toString();

        HttpParams httpParams = new HttpParams();
        httpParams.put("username", username);
        httpParams.put("password", password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((PostRequest<String>) new PostRequest("https://auth1.braz.vip/api/v2/auth")
                    .params(httpParams).tag(this).cacheKey("BSUser")).execute(
                    new StringCallback() {
                        @Override
                        public void onCacheSuccess(Response<String> response) {
                            input_username.setText(response.body());
                        }

                        @Override
                        public void onError(Response<String> response) {
                            input_username.setText("Login server no response, retry later!");
                        }

                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()) {
                                input_username.setText(response.body());
                                authInfo = JSON.parseObject(response.body(), AuthInfo.class);

                                Message msg = new Message();
                                msg.what = 15;
                                MainActivity.mMsgHandler.sendMessage(msg);
                            } else {
                                input_username.setText("Login server no response, retry later!");
                            }
                        }
                    }
                );
            }
        }
        ).start();
    }

    private void play() {
        startChannel("tvbus://3KyG8wJTw9VYdGUvJ8BcuekdoesMMWmDfKs6ovtTTcxePf74kx");
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
        if(statusMessage != null) {
            updateStatusView(statusMessage);
        }
        return true;
    }

    private void updateStatusView(String status) {
        final String fStatus = status;
        TextView input_password = (TextView)findViewById(R.id.input_password);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                input_password.setText(fStatus);
            }
        });
    }

    private void startChannel(String address) {
        stoPlayback();
        mMPCheckTime = Long.MAX_VALUE;
        mTmPlayerConn = mBuffer = 0;

        mTVCore.start(address);
    }

    // player related
    private void checkPlayer() {
        // Attention
        // check player playing must run in main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mTmPlayerConn > 20 && mBuffer > 50) {
                    stoPlayback();
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

    private void stoPlayback() {
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
                        new DefaultDataSourceFactory(MainActivity.this, "TVBUS");
                MediaSource source = new ProgressiveMediaSource.Factory(dataSourceFactory, TsExtractor.FACTORY)
                        .createMediaSource(Uri.parse(playbackUrl));

                player.prepare(source);
                player.setPlayWhenReady(true);
            }
        });
    };


    private void initExoPlayer() {
        PlayerView playerView = findViewById(R.id.exoplayer_view);
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


        player = new SimpleExoPlayer.Builder(this).setLoadControl(loadControl).build();
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
}