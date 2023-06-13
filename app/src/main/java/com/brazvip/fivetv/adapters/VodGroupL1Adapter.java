package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.R;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.beans.GroupL1;

import java.util.HashSet;
import java.util.List;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.G 3469 */
/* loaded from: classes.dex */
public class VodGroupL1Adapter extends HRecyclerViewAdapter<VodGroupL1Adapter.VodGroupL1ViewHolder> {

    /* renamed from: i 13495 */
    public static final String TAG = "VodGroupL1Adapter";

    /* renamed from: j */
    public static final int f13496j = -1;

    /* renamed from: k */
    public static final int f13497k = -1879048193;

    /* renamed from: l 13498 */
    public Context mContext;

    /* renamed from: m 13499 */
    public List<GroupL1> mGroupL1List;

    /* renamed from: n 13500 */
    public SparseArray<GroupL1> mGroupL1Array;

    /* renamed from: o */
    public final Integer[] f13501o;

    /* renamed from: p */
    public int f13502p;

    /* renamed from: q */
    public Handler f13503q;

    /* renamed from: r */
    public Drawable f13504r;

    /* renamed from: s */
    public Drawable f13505s;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MyApplication */
    /* renamed from: e.b.a.a.G$a 3470 */
    /* loaded from: classes.dex */
    public class VodGroupL1ViewHolder extends RecyclerView.ViewHolder {

        /* renamed from: I 13506 */
        public TextView tvName;

        /* renamed from: J */
        public ImageView f13507J;

        /* renamed from: K */
        public LinearLayout f13508K;

        /* renamed from: L */
        public LinearLayout f13509L;

        public VodGroupL1ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.group_l1_name);
            AutoUtils.auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_HEIGHT);
        }
    }

    public VodGroupL1Adapter(SparseArray<GroupL1> sparseArray, Context context, Handler handler) {
        super(context, 1);
        this.f13502p = 0;
        this.f13504r = context.getResources().getDrawable(R.drawable.dialog_btn_bg);
        this.f13505s = context.getResources().getDrawable(R.drawable.dialog_btn_bg_focused);
        this.mContext = context;
        this.f13503q = handler;
        this.mGroupL1Array = sparseArray;
        HashSet hashSet = new HashSet();
        for (int i = 0; i < sparseArray.size(); i++) {
            hashSet.add(Integer.valueOf(sparseArray.keyAt(i)));
        }
        this.f13501o = (Integer[]) hashSet.toArray(new Integer[hashSet.size()]);
        //Arrays.sort(this.f13501o);
        //String log = "groupKeyArray: " + this.f13501o.length;
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override
    /* renamed from: a */
    public void onBindViewHolder(VodGroupL1ViewHolder holder, int position) {
        boolean z = position == getSelectedItem();
        if (z) {
            getNextSelectItem();
        }
        holder.itemView.setSelected(z);
        int groupId = this.f13501o[position].intValue();
        //String log = "groupL1L2Map: " + mGroupL1Array.size() + " groupId: " + groupId + " groupName: " + mGroupL1Array.get(groupId).name;
        holder.tvName.setText(this.mGroupL1Array.get(groupId).name);
        holder.tvName.setTag(Integer.valueOf(groupId));
        if (z) {
            holder.tvName.setTextColor(-1);
//            if (m2459f() >= 0) {
//                holder.tvName.setBackgroundResource(R.drawable.dialog_btn_bg_focused);
//            } else {
//                holder.tvName.setBackgroundResource(R.drawable.dialog_btn_bg_last_focus);
//            }
            if (this.f13502p != getSelectedItem()) {
                this.f13503q.removeMessages(2);
                Message msg = new Message();
                msg.what = 2;
                Bundle data = new Bundle();
                data.putSerializable("groupSet", this.mGroupL1Array.get(groupId).groups);
                msg.setData(data);
                this.f13503q.sendMessage(msg);
                this.f13502p = getSelectedItem();
            }
        } else {
            holder.tvName.setTextColor(-1879048193);
//            holder.tvName.setBackgroundResource(R.drawable.dialog_btn_bg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { //View$OnClickListenerC3468F
            @Override
            public void onClick(View view) {
                notifyItemChanged(getSelectedItem());
                setNextSelectItem(getOwnerView().getChildLayoutPosition(view));
                setSelectedItem(getOwnerView().getChildLayoutPosition(view));
                notifyItemChanged(getSelectedItem());
            }
        });
    }

    /* JADX DEBUG: Method merged with bridge method */
    @Override 
    /* renamed from: b */
    public VodGroupL1ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VodGroupL1ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.vod_group_l1_item, viewGroup, false));
    }

    @Override 
    /* renamed from: a */
    public int getItemCount() {
        return this.mGroupL1Array.size();
    }
}
