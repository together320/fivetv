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
public class HistoryLayout extends RelativeLayout implements View.OnFocusChangeListener, View.OnTouchListener {
    public static final int NAVIGATE_DOWN_FROM_LIVE_RV = 1;
    public static final int NAVIGATE_UP_FROM_VOD_RV = 2;
    private static final String TAG = "HistoryInstance";
    private static Context mContext = null;
    public static Config.VIDEO_TYPE lastFocusVideoType;
    public static Handler navHandler;
    public RelativeLayout historyMenu;
    private boolean inited = false;
    private HistoryAdapter liveHistoryAdapter;
    private RelativeLayout liveHistoryPlacehold;
    public RecyclerView liveHistoryRView;
    private HistoryAdapter vodHistoryAdapter;
    private RelativeLayout vodHistoryPlacehold;
    public RecyclerView vodHistoryRView;

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
                int i = message.what;
                if (i == 1) {
                    HistoryLayout.this.navigateDownFromLiveRv();
                } else if (i != 2) {
                } else {
                    HistoryLayout.this.navigateUpFromVodRv();
                }
            }
        };

        MyItemDecoration MyItemDecoration = new MyItemDecoration(15, 15, 15, 15);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history_live_rview);
        this.liveHistoryRView = recyclerView;
        recyclerView.addItemDecoration(MyItemDecoration);
        this.liveHistoryRView.setOnFocusChangeListener(this);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.vod_history_rview);
        this.vodHistoryRView = recyclerView2;
        recyclerView2.addItemDecoration(MyItemDecoration);
        this.vodHistoryRView.setOnFocusChangeListener(this);
        this.liveHistoryPlacehold = (RelativeLayout) findViewById(R.id.live_history_placehold);
        this.vodHistoryPlacehold = (RelativeLayout) findViewById(R.id.vod_history_placehold);
        this.liveHistoryPlacehold.setFocusable(true);
        this.vodHistoryPlacehold.setFocusable(true);
        //this.liveHistoryPlacehold.setOnKeyListener(this);
        //this.vodHistoryPlacehold.setOnKeyListener(this);
        loadGroupData();
        //mContext.setRequestedOrientation(0);
    }

//    @Override
//    public void focusDefaultView() {
//        RecyclerView recyclerView;
//        Objects.toString(lastFocusVideoType);
//        if ((lastFocusVideoType == Config.VIDEO_TYPE.BSLIVE && (recyclerView = this.liveHistoryRView) != null) || (lastFocusVideoType == Config.VIDEO_TYPE.BSVOD && (recyclerView = this.vodHistoryRView) != null)) {
//            recyclerView.requestFocusFromTouch();
//        } else if (this.liveHistoryPlacehold.requestFocus()) {
//        } else {
//            this.vodHistoryPlacehold.requestFocus();
//        }
//    }

    public void loadGroupData() {
        HistoryInstance historyInstance;
        if (!this.inited || (historyInstance = MainActivity.history) == null) {
            return;
        }
        List<HistoryBean> liveHistory = historyInstance.getLiveHistory();
        Objects.toString(liveHistory != null ? Integer.valueOf(liveHistory.size()) : "null");
        if (liveHistory == null || liveHistory.size() == 0) {
            this.liveHistoryRView.setVisibility(View.GONE);
            this.liveHistoryPlacehold.setVisibility(View.VISIBLE);
        } else {
            try {
                this.liveHistoryAdapter = new HistoryAdapter(liveHistory, Config.VIDEO_TYPE.BSLIVE, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.liveHistoryRView.setAdapter(this.liveHistoryAdapter);
            if (lastFocusVideoType == null) {
                lastFocusVideoType = Config.VIDEO_TYPE.BSLIVE;
            }
            this.liveHistoryRView.setVisibility(View.VISIBLE);
            this.liveHistoryPlacehold.setVisibility(View.GONE);
        }
        List<HistoryBean> vodHistory = MainActivity.history.getVodHistory();
        Objects.toString(vodHistory != null ? Integer.valueOf(vodHistory.size()) : "null");
        if (vodHistory == null || vodHistory.size() == 0) {
            this.vodHistoryRView.setVisibility(View.GONE);
            this.vodHistoryPlacehold.setVisibility(View.VISIBLE);
            return;
        }
        try {
            this.vodHistoryAdapter = new HistoryAdapter(vodHistory, Config.VIDEO_TYPE.BSVOD, mContext);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.vodHistoryRView.setAdapter(this.vodHistoryAdapter);
        if (lastFocusVideoType == null) {
            lastFocusVideoType = Config.VIDEO_TYPE.BSVOD;
        }
        this.vodHistoryRView.setVisibility(View.VISIBLE);
        this.vodHistoryPlacehold.setVisibility(View.GONE);
    }

    public void navigateDownFromLiveRv() {
        RecyclerView recyclerView = this.vodHistoryRView;
        if (recyclerView == null || !recyclerView.requestFocus()) {
            this.vodHistoryPlacehold.requestFocus();
        }
    }

    public void navigateUpFromVodRv() {
        RelativeLayout relativeLayout;
        RecyclerView recyclerView = this.liveHistoryRView;
        if ((recyclerView == null || !recyclerView.requestFocus()) && (relativeLayout = this.liveHistoryPlacehold) != null) {
            relativeLayout.requestFocus();
        }
    }

    @Override
    public void onFocusChange(View view, boolean z) {
        RecyclerView recyclerView;
        int id = view.getId();
        if (z && id == this.liveHistoryRView.getId()) {
            this.liveHistoryRView.requestFocus();
            recyclerView = this.liveHistoryRView;
        } else if (!z || id != this.vodHistoryRView.getId()) {
            return;
        } else {
            this.vodHistoryRView.requestFocus();
            recyclerView = this.vodHistoryRView;
        }
        recyclerView.requestFocusFromTouch();
    }

//    @Override
//    public boolean onKey(View view, int i, KeyEvent keyEvent) {
//        int id = view.getId();
//        if (keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0) {
//            if (id == this.liveHistoryPlacehold.getId()) {
//                switch (i) {
//                    case 19:
//                    case 22:
//                        return true;
//                    case 20:
//                        if (!this.vodHistoryRView.requestFocus()) {
//                            this.vodHistoryPlacehold.requestFocus();
//                        }
//                        return true;
//                    case 21:
//                        MainActivity.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                        return true;
//                }
//            } else if (id == this.vodHistoryPlacehold.getId()) {
//                switch (i) {
//                    case 19:
//                        if (this.liveHistoryRView.requestFocus()) {
//                            return true;
//                        }
//                        this.liveHistoryPlacehold.requestFocus();
//                        return true;
//                    case 20:
//                    case 22:
//                        return true;
//                    case 21:
//                        MainActivity.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_HISTORY_BUTTON);
//                        return true;
//                }
//            }
//        }
//        return super.onKey(view, i, keyEvent);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadGroupData();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        this.inited = true;
//    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            motionEvent.toString();
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
    public void updateDataSet() {
        HistoryAdapter historyAdapter = this.liveHistoryAdapter;
        if (historyAdapter != null) {
            historyAdapter.notifyDataSetChanged();
        }
        HistoryAdapter historyAdapter2 = this.vodHistoryAdapter;
        if (historyAdapter2 != null) {
            historyAdapter2.notifyDataSetChanged();
        }
    }
}