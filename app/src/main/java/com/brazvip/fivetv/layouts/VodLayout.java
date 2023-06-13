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
import android.util.SparseArray;
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
import com.brazvip.fivetv.adapters.VodChannelAdapter;
import com.brazvip.fivetv.adapters.VodGroupAdapter;
import com.brazvip.fivetv.adapters.VodGroupL1Adapter;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.dialogs.MyItemDecoration;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.keyboard.custom.MyKeyBoardView;
import com.brazvip.fivetv.utils.RestApiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashSet;
import java.util.List;

public class VodLayout extends RelativeLayout implements View.OnKeyListener, View.OnFocusChangeListener,
                                                     View.OnClickListener, View.OnTouchListener {

    /* renamed from: b */
    public static final String TAG = "VodLayout";

    private static Context mContext = null;

    /* renamed from: c */
    public static RecyclerView channelRView = null;

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
    public boolean isVodDataLoaded = false;

    /* renamed from: o */
    public RecyclerView groupL1RView;

    /* renamed from: p */
    public RecyclerView groupRView;

    /* renamed from: q */
    public VodGroupL1Adapter mVodGroupL1Adapter;

    /* renamed from: r 13917 */
    public VodGroupAdapter mGroupAdapter;

    /* renamed from: s */
    public VodChannelAdapter mVodChannelAdapter;

    /* renamed from: t */
    public GridLayoutManager gridLayoutManager;

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

        mMsgHandler = new Handler(Looper.getMainLooper()) { //HandlerC3559v(this, Looper.getMainLooper());
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int msg = message.what;
                if (msg == 0) {
                    int index = message.arg1;
                    //String text = "EVENT_REMOVE_ITEM " + index;
                    m2367a(index);
                } else if (msg == 1) {
                    showVodChannel();
                } else if (msg == 2) {
                    HashSet<Integer> set = (HashSet<Integer>) message.getData().getSerializable("groupSet");
                    //String text = "groupSet " + set;
                    m2364a(set);
                } else if (msg == 3) {
                    int index = message.arg1;
                    //String text = "EVENT_LOAD_CHANNEL " + index;
                    loadVodChannelData(index);
                }
                super.handleMessage(message);
            }
        };

        this.isVodDataLoaded = true;

        m2358f();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: c 2361 */
    public void loadVodChannelData(int index) {
        if (channelRView == null || VodChannelInstance.mGroupList == null)
            return;
        Group group = VodChannelInstance.mGroupList.get(index);
        //String log3 = "loadVodChannelData :" + f13899c + "|" + VodChannelInstance.f13622h + "|" + group;
        if (group != null) {
            List<ChannelBean> channelList = group.channnels;
            if (channelList != null && channelList.size() != 0) {
                //String chanList = "channelList :" + channelList.size();
                if (!MainActivity.f16802k && group.restrictedAccess) {
                    //String text = "restrictedAccess: " + VodChannelInstance.f13622h.get(i).name;
                    if (!MainActivity.f16805n || channelRView.hasFocus()) {
                        this.groupRView.requestFocus();
                        channelType = Config.CHANNEL_TYPE.GROUP;
                    }
                    channelRView.setVisibility(View.GONE);
                    if (f13909m < RestApiUtils.f13723G) {
                        f13909m++;
                        MainActivity.showMessageFromResource((int) R.string.Click_Restricted_Group);
                        return;
                    }
                    return;
                }
                try {
                    this.mVodChannelAdapter = new VodChannelAdapter(channelList, mContext, index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                favoriteHint.setVisibility(View.GONE);
                channelRView.setAdapter(this.mVodChannelAdapter);
                if (channelRView.getVisibility() == View.GONE) {
                    channelRView.setVisibility(View.VISIBLE);
                }
                channelRView.setItemAnimator(null);
                return;
            }
            channelRView.setVisibility(View.GONE);
            if (index == -5) {
                favoriteHint.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean m2359e() {
        MainActivity.mMsgHandler.sendEmptyMessage(105);
        return true;
    }

    /* renamed from: f */
    @SuppressLint("ClickableViewAccessibility")
    private void m2358f() {
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
                if (mVodChannelAdapter != null) {
                    mVodChannelAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.keyboardLayout = findViewById(R.id.keyboard_layout);
        if (isSearchState) {
            this.keyboardLayout.setVisibility(View.VISIBLE);
        } else {
            this.keyboardLayout.setVisibility(View.GONE);
        }
    }

    /* renamed from: g */
    private void onSearchBtnRoot() {
        if (!isSearchState) {
            isSearchState = true;
            this.searchBtn.setImageResource(R.mipmap.search_return);
            VodChannelInstance.parseVodChannels();
            showVodChannel();
        } else {
            isSearchState = false;
            this.searchBtn.setImageResource(R.mipmap.search_icon);
            VodChannelInstance.parseVodChannels();
            showVodChannel();
            channelType = Config.CHANNEL_TYPE.VOD_CHANNEL;
            m2359e();
        }
    }


    /* renamed from: c 2362 */
    public int getMenuVisibility() {
        if (vodMenu != null) {
            return vodMenu.getVisibility();
        }
        return 0;
    }
    /* renamed from: b 2363*/
    public void setMenuVisibility(int visibility) {
        if (vodMenu != null) {
            vodMenu.setVisibility(visibility);
        }
    }

    /* renamed from: d */
    public void showVodChannel() {
        if (this.isVodDataLoaded) {
            if (isSearchState) {
                this.keyboardLayout.setVisibility(View.VISIBLE);
            } else {
                this.keyboardLayout.setVisibility(View.GONE);
            }
            this.gridLayoutManager.setSpanCount(RestApiUtils.vodGridSpanCount);
            if (this.groupL1RView == null || this.groupRView == null || VodChannelInstance.mGroupList == null || VodChannelInstance.mGroupList.size() == 0) {
                return;
            }
            try {
                this.mVodGroupL1Adapter = new VodGroupL1Adapter(VodChannelInstance.mGroupL1List, mContext, mMsgHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.groupL1RView.setAdapter(this.mVodGroupL1Adapter);
            if (this.groupL1RView.getVisibility() == View.GONE) {
                this.groupL1RView.setVisibility(View.VISIBLE);
            }
            try {
                this.mGroupAdapter = new VodGroupAdapter(VodChannelInstance.mGroupList, mContext, mMsgHandler);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (loadingProgress != null && loadingProgress.getVisibility() == View.VISIBLE) {
                this.loadingProgress.setVisibility(View.GONE);
            }
            this.groupRView.setAdapter(mGroupAdapter);
            if (this.groupRView.getVisibility() == View.GONE) {
                this.groupRView.setVisibility(View.VISIBLE);
            }
            if (mGroupAdapter != null) {
                Integer[] indices = mGroupAdapter.f13486o;
                if ((indices != null) && (indices.length > 0))
                    loadVodChannelData(indices[0]);
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == this.searchBtnRoot.getId()) {
            onSearchBtnRoot();
        } else if (id == this.backspaceBtn.getId()) {
            EditText editText = this.searchEt;
            if (editText == null || editText.length() <= 0 || this.searchEt.getSelectionStart() <= 0) {
                return;
            }
            this.searchEt.getText().delete(this.searchEt.getSelectionStart() - 1, this.searchEt.getSelectionStart());
        } else if (id == this.deleteBtn.getId()) {
            this.searchEt.setText("");
        } else {
            this.groupRView.getId();
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

    @Override // org.MainActivity.android.fragments.MyBaseFragment, android.view.View.OnKeyListener
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

    /* renamed from: a */
    public void m2367a(int index) {
        mVodChannelAdapter.mChannelList = VodChannelInstance.mGroupList.get(-1).channnels;
        mVodChannelAdapter.notifyItemRemoved(index);
        mVodChannelAdapter.notifyDataSetChanged();
    }

    /* renamed from: a */
    public void m2364a(HashSet<Integer> hashSet) {
        SparseArray<Group> grpChannels = new SparseArray<>();
        for (int i = 0; i < VodChannelInstance.mGroupList.size(); i++) {
            Group grp = VodChannelInstance.mGroupList.valueAt(i);
            if (hashSet == null || hashSet.contains(Integer.valueOf(grp.id))) {
                grpChannels.put(VodChannelInstance.mGroupList.keyAt(i), grp);
            }
        }
        try {
            mGroupAdapter = new VodGroupAdapter(grpChannels, mContext, mMsgHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String text = "groupChannelMap.size：" + groupChannelMap.size() + "groupChannelMap：" + groupChannelMap;
        if (mGroupAdapter != null) {
            groupRView.swapAdapter(mGroupAdapter, false);
        }
        if (mGroupAdapter != null) {
            Integer[] indices = mGroupAdapter.f13486o;
            if ((indices != null) && (indices.length > 0))
                loadVodChannelData(indices[0]);
        }
    }
}
