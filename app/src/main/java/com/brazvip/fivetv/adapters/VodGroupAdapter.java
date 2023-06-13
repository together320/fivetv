package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.layouts.VodLayout;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.dialogs.PasswordChangeDialog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.E 3466 */
/* loaded from: classes.dex */
public class VodGroupAdapter extends HRecyclerViewAdapter<VodGroupAdapter.VodGroupViewHolder> {

    /* renamed from: i */
    public static final String TAG = "VodGroupAdapter";

    /* renamed from: j */
    public static final int f13481j = -1;

    /* renamed from: k */
    public static final int f13482k = -1879048193;

    /* renamed from: l 13483 */
    public Context mContext;

    /* renamed from: m 13484 */
    public List<ChannelBean> mChannelList;

    /* renamed from: n 13485 */
    public SparseArray<Group> mGroupArray;

    /* renamed from: o */
    public final Integer[] f13486o;

    /* renamed from: p */
    public int f13487p;

    /* renamed from: q 13488 */
    public Handler mMsgHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a.E$a 3467 */
    /* loaded from: classes.dex */
    public class VodGroupViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public LinearLayout f13491K;

        public LinearLayout f13492L;

        public VodGroupViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.group_name);
            AutoUtils.auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_HEIGHT);
        }
    }

    public VodGroupAdapter(SparseArray<Group> array, Context context, Handler handler) {
        super(context, 2);
        f13487p  = 0;
        mContext = context;
        mMsgHandler = handler;
        mGroupArray = array;
        HashSet<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < array.size(); i++) {
            hashSet.add(Integer.valueOf(array.keyAt(i)));
        }
        f13486o = (Integer[]) hashSet.toArray(new Integer[hashSet.size()]);
        Arrays.sort(f13486o);
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override
    /* renamed from: a */
    public void onBindViewHolder(VodGroupViewHolder holder, int position) {
        boolean z = position == getSelectedItem();
        if (z) {
            getNextSelectItem();
        }
        holder.itemView.setSelected(z);
        int id = f13486o[position].intValue();
        String name = mGroupArray.get(id).name.replaceAll(".*\\/", "");
        if (this.mGroupArray.get(id).restrictedAccess) {
            if (MainActivity.f16802k) {
                name = "🔓 " + name;
            } else {
                name = "🔒 " + name;
            }
        }
        holder.tvName.setText(name);
        holder.tvName.setTag(Integer.valueOf(id));
        if (z) {
            holder.tvName.setTextColor(-1);
            if (this.f13487p != getSelectedItem()) {
                this.mMsgHandler.removeMessages(3);
                this.mMsgHandler.sendMessage(Message.obtain(this.mMsgHandler, 3, id, 0));
                this.f13487p = getSelectedItem();
            }
        } else {
            holder.tvName.setTextColor(-1879048193);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3464C(id, holder)
            @Override
            public void onClick(View view) {
                //String log = "Click1 " + f13476c.m2457g().getChildLayoutPosition(view) + "!";
                notifyItemChanged(getSelectedItem());
                setNextSelectItem(getOwnerView().getChildLayoutPosition(view));
                setSelectedItem(getOwnerView().getChildLayoutPosition(view));
                notifyItemChanged(getSelectedItem());
                if (((Group) mGroupArray.get(id)).restrictedAccess) {
                    if (MainActivity.f16802k) {
                        MainActivity.f16802k = false;
                        notifyItemChanged(getSelectedItem());
                        String text = "🔒 " + ((Group) mGroupArray.get(id)).name.replaceAll(".*\\/", "");
                        holder.tvName.setText(text);
                        VodLayout.mMsgHandler.removeMessages(3);
                        VodLayout.mMsgHandler.sendMessage(Message.obtain(VodLayout.mMsgHandler, 3, id, 0));
                    } else
                        m2509a(holder.tvName, id);
                }
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override 
    /* renamed from: b */
    public VodGroupViewHolder onCreateViewHolder(ViewGroup group, int position) {
        return new VodGroupViewHolder(LayoutInflater.from(mContext).inflate(R.layout.vod_group_item, group, false));
    }

    /* renamed from: a */
    public void m2509a(final TextView textView, final int arg) {
        PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(mContext);
        helper.setClickListener(new DialogInterface.OnClickListener() { //new DialogInterface$OnClickListenerC3465D(textView, arg)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.f16802k = true;
                String text = "🔓 " + ((Group) mGroupArray.get(arg)).name.replaceAll(".*\\/", "");
                textView.setText(text);
                notifyItemChanged(getSelectedItem());
                VodLayout.mMsgHandler.removeMessages(3);
                VodLayout.mMsgHandler.sendMessage(Message.obtain(VodLayout.mMsgHandler, 3, arg, 0));
            }
        });
        helper.create().show();
    }

    @Override 
    /* renamed from: a */
    public int getItemCount() {
        return (mGroupArray == null) ? 0 : mGroupArray.size();
    }
}
