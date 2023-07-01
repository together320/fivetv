package com.brazvip.fivetv.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.instances.HistoryInstance;
import java.util.List;
import java.util.Objects;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.adapters.HistoryAdapter;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.dialogs.MyItemDecoration;

/* loaded from: classes.dex */
public class HistoryLayout extends RelativeLayout implements View.OnFocusChangeListener, View.OnKeyListener, View.OnTouchListener {
    public static final int NAVIGATE_DOWN_FROM_LIVE_RV = 1;
    public static final int NAVIGATE_UP_FROM_VOD_RV = 2;
    private static final String TAG = "HistoryInstance";
    private static Context mContext = null;
    public static Config.VIDEO_TYPE lastFocusVideoType;
    public static Handler navHandler;
    public RelativeLayout historyMenu;
    public static boolean inited = false;
    private static HistoryAdapter liveHistoryAdapter;
    private static RelativeLayout liveHistoryPlacehold;
    public static RecyclerView liveHistoryRView;
    private static HistoryAdapter playbackHistoryAdapter;
    private static RelativeLayout playbackHistoryPlacehold;
    public static RecyclerView playbackHistoryRView;
    private static HistoryAdapter vodHistoryAdapter;
    private static RelativeLayout vodHistoryPlacehold;
    public static RecyclerView vodHistoryRView;

    public HistoryLayout(Context context) {
        super(context);
        init(context);
    }

    public HistoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.history_layout, this, true);
        
        navHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int what = message.what;
                if (what == NAVIGATE_DOWN_FROM_LIVE_RV) {
                    navigateDownFromLiveRv();
                }
                if (what == NAVIGATE_UP_FROM_VOD_RV) {
                    navigateUpFromVodRv();
                }
            }
        };

        MyItemDecoration MyItemDecoration = new MyItemDecoration(15, 15, 15, 15);

        liveHistoryRView = findViewById(R.id.history_live_rview);
        liveHistoryRView.addItemDecoration(MyItemDecoration);
        liveHistoryRView.setOnFocusChangeListener(this);
        liveHistoryPlacehold = findViewById(R.id.live_history_placehold);
        liveHistoryPlacehold.setFocusable(true);
        liveHistoryPlacehold.setOnKeyListener(this);

        playbackHistoryRView = findViewById(R.id.playback_history_rview);
        playbackHistoryRView.addItemDecoration(MyItemDecoration);
        playbackHistoryRView.setOnFocusChangeListener(this);
        playbackHistoryPlacehold = findViewById(R.id.playback_history_placehold);
        playbackHistoryPlacehold.setFocusable(true);
        playbackHistoryPlacehold.setOnKeyListener(this);

        vodHistoryRView = findViewById(R.id.vod_history_rview);
        vodHistoryRView.addItemDecoration(MyItemDecoration);
        vodHistoryRView.setOnFocusChangeListener(this);
        vodHistoryPlacehold = findViewById(R.id.vod_history_placehold);
        vodHistoryPlacehold.setFocusable(true);
        vodHistoryPlacehold.setOnKeyListener(this);

        focusDefaultView();

        inited = true;
    }

    public void focusDefaultView() {
        if (lastFocusVideoType == Config.VIDEO_TYPE.BSLIVE && liveHistoryRView != null) {
            liveHistoryRView.requestFocusFromTouch();
        } else if (lastFocusVideoType == Config.VIDEO_TYPE.PLAYBACK && playbackHistoryRView != null) {
            playbackHistoryRView.requestFocusFromTouch();
        } else if (lastFocusVideoType == Config.VIDEO_TYPE.BSVOD && vodHistoryRView != null) {
            vodHistoryRView.requestFocusFromTouch();
        } else if (liveHistoryPlacehold.requestFocus()) {

        } else {
            vodHistoryPlacehold.requestFocus();
        }
    }

    public static void loadHistoryLayout() {
        loadGroupData();
    }

    public static void loadGroupData() {
        if (!inited || MainActivity.history == null) {
            return;
        }

        List<HistoryBean> liveHistory = MainActivity.history.getLiveHistory();
        if (liveHistory == null || liveHistory.size() == 0) {
            liveHistoryRView.setVisibility(View.GONE);
            liveHistoryPlacehold.setVisibility(View.VISIBLE);
        } else {
            try {
                liveHistoryAdapter = new HistoryAdapter(liveHistory, Config.VIDEO_TYPE.BSLIVE, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            liveHistoryRView.setAdapter(liveHistoryAdapter);
            if (lastFocusVideoType == null) {
                lastFocusVideoType = Config.VIDEO_TYPE.BSLIVE;
            }
            liveHistoryRView.setVisibility(View.VISIBLE);
            liveHistoryPlacehold.setVisibility(View.GONE);
        }

        List<HistoryBean> playbackHistory = MainActivity.history.getPlaybackHistory();
        if (playbackHistory == null || playbackHistory.size() == 0) {
            playbackHistoryRView.setVisibility(View.GONE);
            playbackHistoryPlacehold.setVisibility(View.VISIBLE);
        } else {
            try {
                playbackHistoryAdapter = new HistoryAdapter(playbackHistory, Config.VIDEO_TYPE.PLAYBACK, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            playbackHistoryRView.setAdapter(playbackHistoryAdapter);
            if (lastFocusVideoType == null) {
                lastFocusVideoType = Config.VIDEO_TYPE.BSLIVE;
            }
            playbackHistoryRView.setVisibility(View.VISIBLE);
            playbackHistoryPlacehold.setVisibility(View.GONE);
        }

        List<HistoryBean> vodHistory = MainActivity.history.getVodHistory();
        if (vodHistory == null || vodHistory.size() == 0) {
            vodHistoryRView.setVisibility(View.GONE);
            vodHistoryPlacehold.setVisibility(View.VISIBLE);
        } else {
            try {
                vodHistoryAdapter = new HistoryAdapter(vodHistory, Config.VIDEO_TYPE.BSVOD, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            vodHistoryRView.setAdapter(vodHistoryAdapter);
            if (lastFocusVideoType == null) {
                lastFocusVideoType = Config.VIDEO_TYPE.BSVOD;
            }
            vodHistoryRView.setVisibility(View.VISIBLE);
            vodHistoryPlacehold.setVisibility(View.GONE);
        }
    }

    public void navigateDownFromLiveRv() {
        if (vodHistoryRView == null || !vodHistoryRView.requestFocus()) {
            vodHistoryPlacehold.requestFocus();
        }
    }

    public void navigateUpFromVodRv() {
        if ((liveHistoryRView == null || !liveHistoryRView.requestFocus()) && liveHistoryPlacehold != null) {
            liveHistoryPlacehold.requestFocus();
        }
    }

    @Override
    public void onFocusChange(View view, boolean z) {
        RecyclerView recyclerView;
        int id = view.getId();
        if (z && id == liveHistoryRView.getId()) {
            liveHistoryRView.requestFocus();
            recyclerView = liveHistoryRView;
        } else if (z && id == playbackHistoryRView.getId()) {
            playbackHistoryRView.requestFocus();
            recyclerView = playbackHistoryRView;
        } else if (z && id == vodHistoryRView.getId()) {
            vodHistoryRView.requestFocus();
            recyclerView = vodHistoryRView;
        } else {
            return;
        }
        recyclerView.requestFocusFromTouch();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        int id = view.getId();
        if (keyEvent.getRepeatCount() == KeyEvent.ACTION_DOWN && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (id == liveHistoryPlacehold.getId()) {
                switch (i) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (!playbackHistoryRView.requestFocus())
                            playbackHistoryRView.requestFocus();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_HISTORY_BUTTON);
                        return true;
                }
            } else if (id == playbackHistoryPlacehold.getId()) {
                switch (i) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (!liveHistoryRView.requestFocus())
                            liveHistoryPlacehold.requestFocus();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (!vodHistoryRView.requestFocus())
                            vodHistoryPlacehold.requestFocus();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_HISTORY_BUTTON);
                        return true;
                }
            } else if (id == vodHistoryPlacehold.getId()) {
                switch (i) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (!playbackHistoryRView.requestFocus())
                            playbackHistoryRView.requestFocus();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_HISTORY_BUTTON);
                        return true;
                }
            }
        }
        return true; //super.onKey(view, i, keyEvent);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.callOnClick();
        }
        return true;
    }

    public void setHistoryMenuVisibility(int i) {
        RelativeLayout relativeLayout = this.historyMenu;
        if (relativeLayout == null || relativeLayout.getVisibility() == i) {
            return;
        }
        this.historyMenu.setVisibility(i);
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public static void updateDataSet() {
        if (liveHistoryAdapter != null) {
            liveHistoryAdapter.notifyDataSetChanged();
        }
        if (playbackHistoryAdapter != null) {
            playbackHistoryAdapter.notifyDataSetChanged();
        }
        if (vodHistoryAdapter != null) {
            vodHistoryAdapter.notifyDataSetChanged();
        }
    }
}
