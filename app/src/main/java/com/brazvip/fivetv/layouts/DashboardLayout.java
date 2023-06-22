package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.adapters.DashboardChannelAdapter;
import com.brazvip.fivetv.adapters.DashboardGroupAdapter;
import com.brazvip.fivetv.adapters.VodGroupAdapter;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.keyboard.custom.MyKeyBoardView;

import java.util.List;

public class DashboardLayout extends RelativeLayout implements View.OnKeyListener, View.OnFocusChangeListener,
                                                     View.OnClickListener, View.OnTouchListener {

    public static final String TAG = "DashboardLayout";

    public static final int MSG_DASH_GROUP_SHOW = 1;
    public static final int MSG_DASH_CHANNEL_TOP_SHOW = 2;

    private static Context mContext = null;

    public static RecyclerView dashChannelView = null;

    /* renamed from: d */
    public static RelativeLayout favoriteHint = null;

    /* renamed from: e 13901 */
    public static Handler mMsgHandler = null;

    /* renamed from: f */
    public static final int f13902f = 0;

    /* renamed from: g */
    public static final int f13903g = 1;

    /* renamed from: h */
    public static final int f13904h = 2;

    /* renamed from: i */
    public static final int f13905i = 3;

    /* renamed from: j */
    public static Config.CHANNEL_TYPE channelType = Config.CHANNEL_TYPE.VOD_GROUP;

    /* renamed from: k */
    public static boolean isSearchState = false;

    /* renamed from: l */
    public static View mVodChannelView = null;

    /* renamed from: m */
    public static int f13909m = 0;

    /* renamed from: A 13910 */
    public EditText searchEt;

    /* renamed from: B 13911 */
    public MyKeyBoardView mKeyBoardView;

    /* renamed from: C */
    public boolean isInitialized = false;

    /* renamed from: o */
    public RecyclerView dashGroupView;

    /* renamed from: p */
    public RecyclerView groupRView;
    public GalleryLayout galleryLayout;

    /* renamed from: q */
    public DashboardGroupAdapter mDashboardGroupAdapter;

    /* renamed from: r 13917 */
    public VodGroupAdapter mGroupAdapter;

    /* renamed from: s */
    public DashboardChannelAdapter mDashboardChannelAdapter;

    /* renamed from: v */
    public ProgressBar dashLoadingProgress;


    public DashboardLayout(Context context) {
        super(context);
        init(context);
    }

    public DashboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DashboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    public void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.dashboard_layout, this, true);

        initComponent();
        initMsgHandler();
    }

    private void initComponent() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dashGroupView = findViewById(R.id.dashGroupView);
        dashGroupView.setLayoutManager(layoutManager);
        dashGroupView.setOnFocusChangeListener(this);

        galleryLayout = findViewById(R.id.dashChannelView);

        dashLoadingProgress = findViewById(R.id.dashLoadingProgress);
        dashLoadingProgress.setVisibility(View.VISIBLE);

        isInitialized = true;
    }

    private void initMsgHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                int msg = message.what;
                if (msg == DashboardLayout.MSG_DASH_GROUP_SHOW) {
                    showDashboard();
                }
                if (msg == DashboardLayout.MSG_DASH_CHANNEL_TOP_SHOW) {
                    int index = message.arg1;
                    showChannelTop(index);
                }
                super.handleMessage(message);
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }

    @Override
    public void onFocusChange(View view, boolean z) {
        int id = view.getId();
        if (z && id == this.dashGroupView.getId()) {
            this.dashGroupView.requestFocus();
            this.dashGroupView.requestFocusFromTouch();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            view.callOnClick();
        }
        return true;
    }

    public void showDashboard() {
        if (isInitialized) {
            try {
//                if (dashGroupView == null || VodChannelInstance.mGroupL1List == null || VodChannelInstance.mGroupL1List.size() == 0) {
//                    return;
//                }
//                mDashboardGroupAdapter = new DashboardGroupAdapter(VodChannelInstance.mGroupL1List, mContext, mMsgHandler);
//                if (mDashboardGroupAdapter != null) {
//                    dashGroupView.setAdapter(mDashboardGroupAdapter);
//                    dashGroupView.setVisibility(View.VISIBLE);
//
//                    Integer[] indices = mDashboardGroupAdapter.mGroupItemList;
//                    if ((indices != null) && (indices.length > 0))
//                        showChannelTop(indices[0]);
//
//                    if (dashLoadingProgress != null && dashLoadingProgress.getVisibility() == View.VISIBLE) {
//                        this.dashLoadingProgress.setVisibility(View.GONE);
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showChannelTop(int groupId) {
        galleryLayout.setVisibility(View.GONE);
        try {
//            String strType = VodChannelInstance.mGroupL1List.get(groupId).name;
//            VodChannelInstance.parseTopVodChannels(strType);
//
//            galleryLayout.setGalleryData(VodChannelInstance.mTopChannelList);
//            galleryLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
