package com.brazvip.fivetv.keyboard.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.R;
import com.brazvip.fivetv.keyboard.custom.entity.MyKeyItemEntity;
import com.brazvip.fivetv.keyboard.custom.utils.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* compiled from: MyApplication */
/* loaded from: classes.dex */
public class MyKeyBoardView extends LinearLayout implements View.OnClickListener {

    /* renamed from: a */
    public static final int f16927a = 7;

    /* renamed from: b */
    public RecyclerView f16928b;

    /* renamed from: c */
    public EditText f16929c;

    /* renamed from: d */
    public String[] f16930d;

    /* renamed from: e 16931 */
    public String mKeyTexts = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";

    /* renamed from: f */
    public boolean f16932f = true;

    /* renamed from: g */
    public boolean f16933g = false;

    /* renamed from: h 16934 */
    public KeyBoardAdapter mAdapter;

    /* renamed from: i 16935 */
    public ArrayList<MyKeyItemEntity> mKeyList = new ArrayList<>();

    /* renamed from: j 16936 */
    public Context mContext;

    /* renamed from: k */
    public View f16937k;

    /* renamed from: l 16938 */
    public OnKeyListener f16938l;

    /* renamed from: m 16939 */
    public GridLayoutManager mLayoutManager;

    /* renamed from: n 16940 */
    public RecyclerView mRecyclerView;

    /* renamed from: o */
    public KeyBoardAdapter.InterfaceC3570a f16941o = new KeyBoardAdapter.InterfaceC3570a() { //C3573a
        @Override
        public void mo2339a(View view, int i) {
            m11a(view, i);
        }
    };

    /* renamed from: p */
    public KeyBoardAdapter.InterfaceC3572c f16942p = new KeyBoardAdapter.InterfaceC3572c() { //C3574b
        @Override
        public void mo2338a(View view) {
            if (f16938l != null) {
                f16938l.onKey(m12a(view));
            }
        }
    };

    /* renamed from: q 16943 */
    public ScheduledExecutorService mExecutorService;

    /* renamed from: r */
    public KeyBoardAdapter.InterfaceC3571b f16944r = new KeyBoardAdapter.InterfaceC3571b() { //C3576d
        @Override
        public void mo2337a(final View view, final int index, boolean delay) {
            if (delay) {
                mExecutorService = Executors.newSingleThreadScheduledExecutor();
                mExecutorService.scheduleWithFixedDelay(new Runnable() { //RunnableC3575c(this, index, view)
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = index;
                        msg.obj = view;
                        f16945s.sendMessage(msg);
                    }
                }, 0L, 50L, TimeUnit.MILLISECONDS);
            } else {
                if (mExecutorService != null) {
                    mExecutorService.shutdownNow();
                    mExecutorService = null;
                }
            }
        }
    };

    /* renamed from: s */
    public Handler f16945s;

    /* compiled from: MyApplication */
    /* renamed from: com.brazvip.fivetv.keyboard.custom.MyKeyBoardView$a  4744 */
    /* loaded from: classes.dex */
    public interface OnKeyListener {
        /* renamed from: a 3 */
        void onKey(String str);
    }

    public class MyMsgHandler extends Handler {
        public MyMsgHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            m11a((View) message.obj, message.what);
        }
    }

    public MyKeyBoardView(Context context) {
        super(context);
        this.f16945s = new MyMsgHandler(context.getMainLooper());
        this.mContext = context;
    }

    public MyKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.f16945s = new MyMsgHandler(context.getMainLooper());
        this.mContext = context;
        this.f16937k = LayoutInflater.from(context).inflate(R.layout.view_keyboard, this);
        this.mRecyclerView = (RecyclerView) this.f16937k.findViewById(R.id.rv_kb_num);
        TypedArray osa = context.obtainStyledAttributes(attrs, R.styleable.MyKeyBoardView);
        if (osa.getBoolean(R.styleable.MyKeyBoardView_isKeyboardTitleGone, false)) { //6
            f16933g = true;
        }
        String prefix = osa.getString(R.styleable.MyKeyBoardView_searchChAppend);
        if (prefix != null && !prefix.isEmpty()) {
            mKeyTexts = prefix + "," + mKeyTexts;
        }
        m4c();
    }

    public MyKeyBoardView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        this.f16945s = new MyMsgHandler(context.getMainLooper());
    }

    private void initViews() {

    }

    /* renamed from: c */
    private void m4c() {
        this.f16928b = this.f16937k.findViewById(R.id.rv_kb_num);
        this.f16930d = this.mKeyTexts.split(",");
        m6b();
        this.mLayoutManager = new GridLayoutManager(mContext, 7);
        this.mAdapter = new KeyBoardAdapter(mContext, mKeyList, f16941o, f16944r, f16942p);
        this.mAdapter.m2341y();
        m14a();
    }

    public EditText getEditText() {
        return this.f16929c;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        //view.getId();
    }

    public void setEditText(EditText editText) {
        this.f16929c = editText;
    }

    public void setOnKeyClickListener(OnKeyListener listener) {
        this.f16938l = listener;
    }

    /* renamed from: b */
    private void m6b() {
        for (int i = 0; i <f16930d.length; i++) {
            this.mKeyList.add(new MyKeyItemEntity(0, 1, f16930d[i]));
        }
        mKeyList.add(new MyKeyItemEntity(9, 1, " "));
    }

    /* renamed from: a */
    private void m14a() {
        if (this.f16932f) {
            this.f16928b.setLayoutManager(this.mLayoutManager);
            this.f16928b.setAdapter(this.mAdapter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public void m11a(View view, int arg) {
        if (f16929c == null) {
            Log.d("=====", "editText null");
            return;
        }
        f16929c.getText().toString().substring(0, this.f16929c.getSelectionStart());
        Editable text = this.f16929c.getText();
        int selectionStart = this.f16929c.getSelectionStart();
        if (view instanceof TextView) {
            String charSequence = ((TextView) view).getText().toString();
            int inputType = this.f16929c.getInputType();
            if (inputType == 1) {
                text.insert(selectionStart, charSequence);
            } else if (inputType == 2) {
                if (StringUtils.isEmpty(this.f16929c.getText().toString()) && charSequence.equals("0")) {
                    return;
                }
                text.insert(selectionStart, charSequence);
            } else if (inputType == 3) {
                text.insert(selectionStart, charSequence);
            } else if (inputType != 8194) {
                text.insert(selectionStart, charSequence);
            } else if (this.f16932f && this.mKeyList.get(arg).getItemType() == 3) {
                m13a(text, selectionStart);
            }
        } else if ((view instanceof ImageView) && this.f16932f) {
            if (this.mKeyList.get(arg).getItemType() == 3) {
                m13a(text, selectionStart);
            } else if (this.mKeyList.get(arg).getItemType() == 9) {
                text.insert(selectionStart, " ");
            }
        }
    }



    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public String m12a(View view) {
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
        return null;
    }

    /* renamed from: a */
    private void m13a(Editable editable, int i) {
        if (editable == null || editable.length() <= 0 || i <= 0) {
            return;
        }
        editable.delete(i - 1, i);
    }
}
