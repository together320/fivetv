package com.brazvip.fivetv.keyboard.custom;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.R;
import com.brazvip.fivetv.keyboard.custom.entity.MyKeyItemEntity;
import com.brazvip.fivetv.keyboard.custom.utils.StringUtils;

/* compiled from: MyApplication */
/* renamed from: e.b.a.e.a.f 3569 */
/* loaded from: classes.dex */
public class KeyBoardAdapter extends BaseMultiItemQuickAdapter<MyKeyItemEntity, BaseViewHolder> {

    /* renamed from: N 13938 */
    public static final String TAG = "KeyBoardAdapter";

    /* renamed from: O */
    public boolean f13939O;

    /* renamed from: P */
    public boolean f13940P;

    /* renamed from: Q */
    public boolean f13941Q;

    /* renamed from: R */
    public boolean f13942R;

    /* renamed from: S */
    public InterfaceC3570a f13943S;

    /* renamed from: T */
    public InterfaceC3571b f13944T;

    /* renamed from: U */
    public InterfaceC3572c f13945U;

    /* renamed from: V */
    public ScheduledExecutorService f13946V;

    /* renamed from: W */
    public long f13947W;

    /* renamed from: X */
    public long f13948X;

    /* renamed from: Y */
    public long f13949Y;

    /* renamed from: Z */
    public boolean f13950Z;

    /* renamed from: aa */
    public boolean f13951aa;

    /* renamed from: ba */
    public Handler f13952ba;


    /* compiled from: MyApplication */
    /* renamed from: e.b.a.e.a.f$a */
    /* loaded from: classes.dex */
    public interface InterfaceC3570a {
        /* renamed from: a */
        void mo2339a(View view, int i);
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.e.a.f$b */
    /* loaded from: classes.dex */
    public interface InterfaceC3571b {
        /* renamed from: a */
        void mo2337a(View view, int i, boolean z);
    }

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.e.a.f$c */
    /* loaded from: classes.dex */
    public interface InterfaceC3572c {
        /* renamed from: a */
        void mo2338a(View view);
    }

    public KeyBoardAdapter(Context context, List<MyKeyItemEntity> list, InterfaceC3570a interfaceC3570a,
                           InterfaceC3571b interfaceC3571b, InterfaceC3572c interfaceC3572c) {
        super(list);
        this.f13939O = false;
        this.f13940P = false;
        this.f13941Q = false;
        this.f13942R = false;
        this.f13949Y = 500L;
        this.f13950Z = false;
        this.f13951aa = false;
        this.f13952ba = new Handler();
        this.f13943S = interfaceC3570a;
        this.f13944T = interfaceC3571b;
        this.f13945U = interfaceC3572c;
        addItemType(0, R.layout.item_keyboard_num);
        addItemType(9, R.layout.item_keyboard_img_num);
        addItemType(7, R.layout.item_keyboard_alphabet_a);
        addItemType(8, R.layout.item_keyboard_char_big);
        addItemType(6, R.layout.item_keyboard_alphabet_normal);
        addItemType(1, R.layout.item_keyboard_alphabet_normal);
        addItemType(2, R.layout.item_keyboard_alphabet_a);
        addItemType(5, R.layout.item_keyboard_img_alphabet);
        addItemType(3, R.layout.item_keyboard_img_num);
        addItemType(4, R.layout.item_keyboard_img_alphabet);
    }

    /* renamed from: d */
    private void m2345d(BaseViewHolder holder) {
        holder.getView(R.id.tv_kb).setOnTouchListener(
            new View.OnTouchListener() { //View$OnTouchListenerC3565b(this, holder)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (f13943S != null)
                            f13943S.mo2339a(v, holder.getLayoutPosition() - getHeaderLayoutCount());
                    }
                    return true;
                }
            }
        );
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: z */
    public boolean m2340z() {
        return this.f13948X - this.f13947W >= this.f13949Y;
    }

    /* renamed from: g */
    public void m2344g(boolean z) {
        this.f13939O = z;
    }

    /* renamed from: w */
    public void m2343w() {
        this.f13941Q = true;
        this.f13940P = false;
        this.f13942R = false;
    }

    /* renamed from: x */
    public void m2342x() {
        this.f13942R = true;
        this.f13941Q = false;
        this.f13940P = false;
    }

    /* renamed from: y */
    public void m2341y() {
        this.f13940P = true;
        this.f13941Q = false;
        this.f13942R = false;
    }

    /* renamed from: c */
    private void m2347c(BaseViewHolder holder) {
        final int position = holder.getLayoutPosition() - getHeaderLayoutCount();
        holder.getView(R.id.tv_kb).setOnKeyListener(new View.OnKeyListener() { //View$OnKeyListenerC3566c(this, holder)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (position % 7 == 0 && event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                    SopCast.mMsgHandler.sendEmptyMessage(105);
//                    BSVod.f13906j = Config.CHANNEL_TYPE.VOD_CHANNEL;
//                    return true;
//                } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (!SopCast.isPlaying() || !SopCast.f16805n) {
//                        PrefUtils.logout(mContext);
//                        return true;
//                    }
//                    SopCast.mMsgHandler.sendEmptyMessage(100);
//                    return true;
//                } else {
//
//                }
                return false;
            }
        });
    }

    /* renamed from: b */
    private void m2350b(BaseViewHolder holder) {
        holder.getView(R.id.tv_kb).setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3564a(this, holder)
            @Override
            public void onClick(View v) {
                if (f13943S != null) {
                    f13943S.mo2339a(v, holder.getLayoutPosition() - getHeaderLayoutCount());
                }
                if (f13945U != null) {
                    f13945U.mo2338a(v);
                }
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override
    /* renamed from: a */
    public void convert(BaseViewHolder holder, MyKeyItemEntity obj) {
        AutoUtils.auto(holder.getConvertView(), Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_HEIGHT);
        int type = holder.getItemViewType();
        if (type != 0) {
            switch (type) {
                case 3:
                    holder.setImageResource(R.id.tv_kb, R.drawable.iv_common_kb_delete_smaller);
                    m2356a(holder, holder.getView(R.id.tv_kb), holder.getLayoutPosition() - getHeaderLayoutCount());
                    break;
                case 4:
                    holder.setImageResource(R.id.tv_kb, R.drawable.iv_common_kb_delete_small);
                    m2356a(holder, holder.getView(R.id.tv_kb), holder.getLayoutPosition() - getHeaderLayoutCount());
                    break;
                case 5:
                    if (!this.f13939O) {
                        holder.setImageResource(R.id.tv_kb, R.drawable.iv_commom_kb_lowercase);
                        holder.setBackgroundRes(R.id.tv_kb, R.drawable.selector_keyboard_key_alphabet);
                    } else {
                        holder.setImageResource(R.id.tv_kb, R.drawable.iv_commom_kb_capital);
                        holder.setBackgroundRes(R.id.tv_kb, R.drawable.shape_keyboard_alphabet_selected);
                    }
                    break;
                case 6:
                case 7:
                case 8:
                    holder.setText(R.id.tv_kb, (CharSequence) obj.m2333b());
                    break;
                case 9:
                    holder.setImageResource(R.id.tv_kb, R.drawable.iv_commom_kb_space);
                    break;
                default:
                    if (!this.f13939O) {
                        if (!StringUtils.isEmpty(obj.m2333b()) && m2351a(obj.m2333b())) {
                            holder.setText(R.id.tv_kb, (CharSequence) obj.m2333b().toLowerCase());
                            break;
                        }
                    } else if (!StringUtils.isEmpty(obj.m2333b()) && m2351a(obj.m2333b())) {
                        holder.setText(R.id.tv_kb, (CharSequence) obj.m2333b().toUpperCase());
                        break;
                    }
                    break;
            }
        } else {
            holder.setText(R.id.tv_kb, (CharSequence) obj.m2333b());
            ((TextView) holder.getConvertView()).setTextSize(0, AutoUtils.getPercentWidthSize(40));
        }
        m2345d(holder);
        m2347c(holder);
        m2350b(holder);
    }

    /* renamed from: a */
    private boolean m2351a(String key) {
        return "abcdefghijklmnopqrstuvwxyz".contains(key.toLowerCase());
    }

    /* renamed from: a */
    private void m2356a(final BaseViewHolder obj, final View view, final int index) {
        view.setOnTouchListener(new View.OnTouchListener() { //View$OnTouchListenerC3568e(obj, index)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                f13948X = event.getEventTime();
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    f13947W = event.getDownTime();
                    if (f13943S != null) {
                        f13943S.mo2339a(view, obj.getLayoutPosition() - getHeaderLayoutCount());
                    }
                    obj.setBackgroundRes(view.getId(), R.color.greySelectedText);
                    f13952ba.postDelayed(new Runnable() { //RunnableC3567d(this, view)
                        @Override
                        public void run() {
                            f13951aa = true;
                            if (f13944T != null) {
                                f13944T.mo2337a(view, index, true);
                            }
                        }
                    }, 500L);
                } else if (action == 1 || action == 3) {
                    f13952ba.removeCallbacksAndMessages(null);
                    if (f13950Z) {
                        f13950Z = false;
                        f13951aa = false;
                    }
                    obj.setBackgroundRes(view.getId(), R.color.white);
                    if (f13944T != null) {
                        f13944T.mo2337a(view, index, false);
                    }
                } else {
                    if (!f13950Z) {
                        f13950Z = m2340z();
                    }
                    if (f13950Z && !f13951aa) {
                        f13952ba.removeCallbacksAndMessages(null);
                        f13951aa = true;
                        if (f13944T != null) {
                            f13944T.mo2337a(view, index, true);
                        }
                    }
                }
                return true;
            }
        });
    }
}
