package com.brazvip.fivetv.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.adapters.NavigationListener;
import com.brazvip.fivetv.adapters.VodChannelAdapter;
import com.brazvip.fivetv.adapters.VodGroupAdapter;
import com.brazvip.fivetv.adapters.VodGroupL1Adapter;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.beans.vod.VodGroupL2;
import com.brazvip.fivetv.dialogs.MyItemDecoration;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.keyboard.custom.MyKeyBoardView;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;
import java.util.Objects;

public class VodLayout extends RelativeLayout implements View.OnKeyListener, View.OnFocusChangeListener,
                                                     View.OnClickListener, View.OnTouchListener {

    /* renamed from: b */
    public static final String TAG = "VodLayout";

    private static Context mContext = null;

    public static RecyclerView channelRView = null;

    public static RecyclerView groupL1RView = null;
    public static RecyclerView groupRView = null;

    /* renamed from: d */
    public static RelativeLayout favoriteHint = null;

    /* renamed from: e 13901 */
    public static Handler handler = null;

    public static Config.MenuType menuType;

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
    public static boolean IS_SEARCH_STATE = false;

    /* renamed from: l */
    public static View mVodChannelView = null;

    private String currentVodGroupL2 = null;

    /* renamed from: m */
    public static int f13909m = 0;

    /* renamed from: A 13910 */
    public EditText searchEt;

    /* renamed from: B 13911 */
    public MyKeyBoardView mKeyBoardView;

    public VodGroupL1Adapter vodGroupL1Adapter;
    public VodGroupAdapter vodGroupAdapter;
    public VodChannelAdapter vodChannelAdapter;
    /* renamed from: t */
    public GridLayoutManager gridLayoutManager;

    private NavigationListener vodL1NavListener;
    private NavigationListener vodL2NavListener;

    /* renamed from: u */
    public RelativeLayout vodMenu;

    /* renamed from: v */
    public ProgressBar loadingProgress;

    /* renamed from: w */
    public LinearLayout keyboardLayout;

    public RelativeLayout searchBtnRoot = null;

    /* renamed from: x */
    public ImageView searchBtn;

    /* renamed from: y */
    public ImageButton backspaceBtn;

    /* renamed from: z */
    public ImageButton deleteBtn;


    public VodLayout(Context context) {
        super(context);
        init(context);
    }

    public VodLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VodLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    public void init(Context context) {

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.vod_layout, this, true);

        handler = new Handler(Looper.getMainLooper()) { //HandlerC3559v(this, Looper.getMainLooper());
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int msg = message.what;
                if (msg == 0) {
                    vodChannelAdapter.notifyItemRemoved(message.arg1);
                    vodChannelAdapter.notifyDataSetChanged();
                } else if (msg == 1) {
                    loadGroupData();
                } else if (msg == 2) {
                    onGroupL1Selected(message.getData().getString("selectedGroupL1"));
                } else if (msg == 3) {
                    loadVodChannelData(message.getData().getString("groupId"), message.getData().getBoolean("restrictedAccess", false));
                }
                super.handleMessage(message);
            }
        };

        this.vodL1NavListener = new NavigationListener() {

            @Override
            public boolean navigateAbove() {
                return true;
            }

            @Override
            public boolean navigateBelow() {
                VodLayout.groupRView.requestFocus();
                return true;
            }

            @Override
            public boolean navigateLeft() {
                VodLayout.this.searchBtnRoot.requestFocus();
                return true;
            }

            @Override
            public boolean navigateRight() {
                return true;
            }
        };
        this.vodL2NavListener = new NavigationListener() {
            @Override
            public boolean navigateAbove() {
                VodLayout.groupL1RView.requestFocus();
                return true;
            }

            @Override
            public boolean navigateBelow() {
                View view;
                if (VodLayout.IS_SEARCH_STATE && VodLayout.this.searchEt.getVisibility() == View.VISIBLE) {
                    view = VodLayout.this.searchEt;
                } else if (VodLayout.channelRView.getVisibility() != View.VISIBLE) {
                    return true;
                } else {
                    view = VodLayout.channelRView;
                }
                view.requestFocus();
                return true;
            }

            @Override
            public boolean navigateLeft() {
                VodLayout.this.focusVodButton();
                return true;
            }

            @Override
            public boolean navigateRight() {
                return true;
            }
        };

        initComponent();
        loadGroupData();
    }

    public boolean isGroupRViewNull() {
        return groupRView == null;
    }

    public final boolean focusVodButton() {
        //handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_VOD_BUTTON);
        return true;
    }

    public void loadGroupData() {
        if (groupL1RView != null && groupRView != null && VodChannelInstance.newVodL1L2Groups != null && VodChannelInstance.newVodL1L2Groups.size() != 0) {
            try {
                this.vodGroupL1Adapter = new VodGroupL1Adapter(VodChannelInstance.newVodL1L2Groups, mContext, handler, this.vodL1NavListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            groupL1RView.setAdapter(this.vodGroupL1Adapter);
            if (groupL1RView.getVisibility() == View.GONE) {
                groupL1RView.setVisibility(View.VISIBLE);
            }
            try {
                this.vodGroupAdapter = new VodGroupAdapter(VodChannelInstance.newVodL1L2Groups.get(SopApplication.getSopContext().getString(R.string.All)),
                        mContext, handler, vodL2NavListener);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            ProgressBar progressBar = this.loadingProgress;
            if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                this.loadingProgress.setVisibility(View.GONE);
            }
            groupRView.setAdapter(this.vodGroupAdapter);
            if (groupRView.getVisibility() == View.GONE) {
                groupRView.setVisibility(View.VISIBLE);
            }
            loadFirstVodGroupL2();
        }
        if (IS_SEARCH_STATE) {
            this.keyboardLayout.setVisibility(View.VISIBLE);
            groupL1RView.setVisibility(View.GONE);
            groupRView.setVisibility(View.GONE);
            return;
        }
        this.keyboardLayout.setVisibility(View.GONE);
        groupL1RView.setVisibility(View.VISIBLE);
        groupRView.setVisibility(View.VISIBLE);
    }

    private void loadFirstVodGroupL2() {
        List<VodGroupL2> list;
        VodGroupAdapter vodGroupAdapter = this.vodGroupAdapter;
        if (vodGroupAdapter == null || (list = vodGroupAdapter.vodL2Groups) == null || list.isEmpty()) {
            return;
        }
        VodGroupL2 vodGroupL2 = this.vodGroupAdapter.vodL2Groups.get(0);
        loadVodChannelData(vodGroupL2.getId(), vodGroupL2.isRestricted());
    }

    public final void loadVodChannelData(String str, boolean z) {
        if (channelRView == null || VodChannelInstance.newVodL1L2Groups == null) {
            Objects.toString(channelRView);
            Objects.toString(VodChannelInstance.newVodL1L2Groups);
            return;
        }
        if (str != "search" && this.currentVodGroupL2 == "search") {
            toggleSearchMode("hide");
        }
        List<VodChannelBean> favoriteChannels = VodChannelInstance.FAVORITES_GROUP_ID.equals(str) ? VodChannelInstance.getFavoriteChannels() : VodChannelInstance.RESULTS_GROUP_ID.equals(str) ? VodChannelInstance.getChannelsByGroupKey(VodChannelInstance.RESULTS_GROUP_ID, z) : VodChannelInstance.getChannelsByGroupKey(str, z);
        this.currentVodGroupL2 = str;
        if ((favoriteChannels == null || favoriteChannels.size() == 0) && str != "search") {
            channelRView.setVisibility(View.GONE);
            if (VodChannelInstance.FAVORITES_GROUP_ID.equals(str)) {
                this.favoriteHint.setVisibility(View.VISIBLE);
                return;
            }
            return;
        }
        favoriteChannels.size();
        if (MainActivity.restrictedGroupsUnlocked || !z) {
            try {
                this.vodChannelAdapter = new VodChannelAdapter(favoriteChannels, mContext, str, null, new NavigationListener() {
                    @Override
                    public boolean navigateAbove() {
                        if (VodLayout.groupRView.requestFocus()) {
                            return true;
                        }
                        VodLayout.groupL1RView.requestFocus();
                        return true;
                    }

                    @Override
                    public boolean navigateBelow() {
                        return true;
                    }

                    @Override
                    public boolean navigateLeft() {
                        if (VodLayout.IS_SEARCH_STATE) {
                            return VodLayout.this.searchEt.requestFocus();
                        }
                        //SopCast.handler.sendEmptyMessage(SopHandler.EVENT_FOCUS_VOD_BUTTON);
                        return true;
                    }

                    @Override
                    public boolean navigateRight() {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.favoriteHint.setVisibility(View.GONE);
            channelRView.setAdapter(this.vodChannelAdapter);
            if (channelRView.getVisibility() == View.GONE) {
                channelRView.setVisibility(View.VISIBLE);
            }
            channelRView.setItemAnimator(null);
            return;
        }

        groupRView.requestFocus();
        channelRView.setVisibility(View.GONE);
    }

    private boolean m2359e() {
        MainActivity.mMsgHandler.sendEmptyMessage(105);
        return true;
    }

    /* renamed from: f */
    @SuppressLint("ClickableViewAccessibility")
    private void initComponent() {
        this.vodMenu = findViewById(R.id.vod_menu);
        this.vodMenu.setOnKeyListener(this);
        MyItemDecoration decor = new MyItemDecoration(0, 0, 0, 0);
        this.groupL1RView = findViewById(R.id.groupL1_rview);
        this.groupRView = findViewById(R.id.group_rview);
        LinearLayoutManager lm1 = new LinearLayoutManager(getContext());
        lm1.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.groupL1RView.setLayoutManager(lm1);
        this.groupL1RView.setOnFocusChangeListener(this);
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());
        lm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.groupRView.setLayoutManager(lm2);
        this.groupRView.setOnFocusChangeListener(this);
        channelRView = findViewById(R.id.channel_rview);
        this.gridLayoutManager = new GridLayoutManager(getContext(), RestApiUtils.vodGridSpanCount, GridLayoutManager.VERTICAL, false);
        channelRView.addItemDecoration(decor);
        channelRView.setLayoutManager(gridLayoutManager);
        channelRView.setOnFocusChangeListener(this);
        this.groupRView.setOnKeyListener(this);
        this.groupL1RView.setOnKeyListener(this);
        channelRView.setOnKeyListener(this);
        this.loadingProgress = findViewById(R.id.loading_progress);
        favoriteHint = findViewById(R.id.favorite_hint);
        favoriteHint.setVisibility(View.GONE);

        searchBtnRoot = findViewById(R.id.search_btn_rl);
        AutoUtils.auto(searchBtnRoot, 3, 3);
        searchBtnRoot.setOnKeyListener(this);
        searchBtnRoot.setOnClickListener(this);
        searchBtnRoot.setOnTouchListener(this);

        searchBtn = findViewById(R.id.searchBtn);

        this.backspaceBtn = findViewById(R.id.backspace_btn);
        this.backspaceBtn.setOnKeyListener(this);
        this.backspaceBtn.setOnClickListener(this);
        this.backspaceBtn.setOnTouchListener(this);
        this.deleteBtn = findViewById(R.id.delete_btn);
        this.deleteBtn.setOnKeyListener(this);
        this.deleteBtn.setOnClickListener(this);
        this.deleteBtn.setOnTouchListener(this);
        this.mKeyBoardView = findViewById(R.id.keyboardView);
        this.searchEt = findViewById(R.id.searchET);
        this.searchEt.setOnKeyListener(this);
        //C3560w
        this.mKeyBoardView.setOnKeyClickListener(key -> Log.e("===key", key + ""));
        this.mKeyBoardView.setEditText(this.searchEt);
        this.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vodChannelAdapter != null) {
                    vodChannelAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.keyboardLayout = findViewById(R.id.keyboard_layout);
        if (IS_SEARCH_STATE) {
            this.keyboardLayout.setVisibility(View.VISIBLE);
        } else {
            this.keyboardLayout.setVisibility(View.GONE);
        }
    }

    private void onSearchBtnRoot() {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == this.searchBtnRoot.getId()) {
            toggleSearchMode(new String[0]);
        } else if (id != this.backspaceBtn.getId()) {
            if (id == this.deleteBtn.getId()) {
                this.searchEt.setText("");
            } else {
                groupRView.getId();
            }
        } else {
            EditText editText = this.searchEt;
            if (editText == null || editText.length() <= 0 || this.searchEt.getSelectionStart() <= 0) {
                return;
            }
            this.searchEt.getText().delete(this.searchEt.getSelectionStart() - 1, this.searchEt.getSelectionStart());
        }
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        int id = view.getId();
        if (z && id == this.groupRView.getId()) {
            this.groupRView.requestFocus();
            this.groupRView.requestFocusFromTouch();
        } else if (z && id == this.groupL1RView.getId()) {
            this.groupL1RView.requestFocus();
            this.groupL1RView.requestFocusFromTouch();
        } else if (z && id == channelRView.getId()) {
            channelRView.setSelected(true);
            channelRView.requestFocus();
            channelRView.requestFocusFromTouch();
        } else {

        }
    }

    public void onGroupL1Selected(String str) {
        try {
            vodGroupAdapter = new VodGroupAdapter(VodChannelInstance.newVodL1L2Groups.get(str), mContext, handler, this.vodL2NavListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vodGroupAdapter != null) {
            //groupRView.setLayoutFrozen(false);
            groupRView.swapAdapter(vodGroupAdapter, false);
            //groupRView.m5597W(true);
            groupRView.requestLayout();
        }
        loadFirstVodGroupL2();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        EditText editText;
        int id = view.getId();
        //String log = "onkey " + i;
        if (keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0) {
            if (id == this.groupRView.getId()) {
                //String log2 = "groupRView key event " + keyEvent;
                if (i == 20 && channelRView.getVisibility() == View.GONE) {
                    return true;
                }
            } else if (id == this.searchBtnRoot.getId()) {
                //String log3 = "searchBtn key event " + keyEvent;
                if (i == 23) {
                    onSearchBtnRoot();
                    return true;
                } else if (i == 21) {
                    channelType = Config.CHANNEL_TYPE.VOD_GROUP;
                    m2359e();
                    return true;
                }
            } else if (id == this.searchEt.getId()) {
                //String log4 = "searchEt key event " + keyEvent;
                if (i == 23) {
                    return true;
                }
                if (i == 21) {
                    channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                    m2359e();
                    return true;
                }
            } else if (id == this.backspaceBtn.getId()) {
                //String log5 = "backspaceBtn key event " + keyEvent;
                if (i == 23 && (editText = this.searchEt) != null && editText.length() > 0 && this.searchEt.getSelectionStart() > 0) {
                    this.searchEt.getText().delete(this.searchEt.getSelectionStart() - 1, this.searchEt.getSelectionStart());
                }
            } else if (id == this.deleteBtn.getId()) {
                //String log6 = "deleteBtn key event " + keyEvent;
                if (i == 23) {
                    this.searchEt.setText("");
                } else if (i == 21) {
                    channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                    m2359e();
                    return true;
                }
            } else if (id == this.mKeyBoardView.getId()) {
                //String log7 = "keyboardView key event " + keyEvent;
                if (i == 21) {
                    channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
                    m2359e();
                    return true;
                }
            }
        }
        return true; //super.onKey(view, i, keyEvent);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //String log = "onTouch" + event;
            view.callOnClick();
        }
        return true;
    }

    public final void toggleSearchMode(String... strArr) {
        if (strArr.length >= 1 && strArr[0] == "hide" && IS_SEARCH_STATE) {
            String str = strArr[0];
            this.keyboardLayout.getVisibility();
            this.searchBtn.setImageResource(R.drawable.ic_search_7dp);
            IS_SEARCH_STATE = false;
            this.keyboardLayout.setVisibility(View.GONE);
            return;
        }
        groupL1RView.setVisibility(View.GONE);
        groupRView.setVisibility(View.GONE);
        if (IS_SEARCH_STATE) {
            this.searchBtn.setImageResource(R.drawable.ic_search_7dp);
        } else {
            this.searchBtn.setImageResource(R.mipmap.search_return);
        }
        IS_SEARCH_STATE = !IS_SEARCH_STATE;
        loadGroupData();
    }
}
