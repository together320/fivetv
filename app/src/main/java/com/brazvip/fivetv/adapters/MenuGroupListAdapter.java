package com.brazvip.fivetv.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.EPGInstance;

import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: MyApplication */
/* renamed from: e.b.a.a.r 3490 */
/* loaded from: classes.dex */
public class MenuGroupListAdapter extends BaseAdapter {

    /* renamed from: a 13576 */
    public List<ChannelBean> mChannels;

    /* renamed from: b 13577 */
    public Map<Integer, Group> mGroupData;

    /* renamed from: c 13578 */
    public final Integer[] mIndices;

    /* renamed from: d */
    public ListView mListView;

    /* renamed from: e 13580 */
    public Context mContext;

    public MenuGroupListAdapter(Context context, Map<Integer, Group> mapData, ListView listView) {
        mContext = context;
        mGroupData = mapData;
        Set<Integer> keySet = mapData.keySet();
        mIndices = (Integer[]) keySet.toArray(new Integer[0]); //new Integer[keySet.size()]
        Arrays.sort(mIndices);
        mListView = listView;
    }

    /* renamed from: a */
    public void m2470a(final TextView textView, final int index) {
//        PasswordChangeDialog.Helper helper = new PasswordChangeDialog.Helper(mContext);
//        helper.setClickListener(new DialogInterface.OnClickListener() { //DialogInterface$OnClickListenerC3489q
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                SopCast.f16802k = true;
//                String text = "🔓 " + mGroupData.get(Integer.valueOf(index)).name;
//                textView.setText(text);
//                BSMenu.mMsgHandler.removeMessages(2);
//                BSMenu.mMsgHandler.sendMessage(Message.obtain(BSMenu.mMsgHandler, 2, index, 0));
//            }
//        });
//        helper.create().show();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mGroupData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mGroupData.get(mIndices[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.group_item, null);
            AutoUtils.auto(convertView, 3, 3);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.group_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.group_item_bg);
        Integer index = mIndices[position];
        Group group = mGroupData.get(index);
        if (group != null) {
            textView.setText(group.name);
            if (group.restrictedAccess) {
                textView.setText("🔒 " + group.name);
            }
        }
        convertView.setTag(index);
        return convertView;
    }

    /* renamed from: a */
    public void m2471a(ListView listView, int position) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (position < firstVisiblePosition || position > lastVisiblePosition) {
            return;
        }
        getView(position, listView.getChildAt(position - firstVisiblePosition), listView);
    }
}
