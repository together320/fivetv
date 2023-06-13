package com.brazvip.fivetv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;

import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.adapters.EpisodeAdapter;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.dialogs.MyItemDecoration;
import com.brazvip.fivetv.instances.VodChannelInstance;

import java.util.List;


/* compiled from: MyApplication */
/* renamed from: e.b.a.h.b 3617 */
/* loaded from: classes.dex */
public class EpisodeDialog extends Dialog {

    /* renamed from: a 14052 */
    public static String TAG = "EpisodeDialog";

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.h.b$a */
    /* loaded from: classes.dex */
    public static class Helper {

        /* renamed from: a 14053 */
        public static EpisodeDialog mDialog;

        /* renamed from: b */
        public Context mContext;

        /* renamed from: c */
        public ChannelBean mChannel;

        /* renamed from: d */
        public int f14056d;

        /* renamed from: e 14057 */
        public RecyclerView mListView;

        /* renamed from: f 14058 */
        public EpisodeAdapter mAdapter;

        public Helper(Context context) {
            this.mContext = context;
        }

        /* renamed from: b 2207 */
        public static void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        /* renamed from: c 2206 */
        public static void hide() {
            if (mDialog != null) {
                mDialog.hide();
                //String log = "hide..... " + mDialog.isShowing();
            }
        }

        /* renamed from: d 2205 */
        public static void show() {
            if (mDialog != null) {
                mDialog.show();
                //String log = "show..... " + mDialog.isShowing();
            }
        }

        /* renamed from: a 2208 */
        public EpisodeDialog create(ChannelBean channel) {
            ChannelBean curChan = this.mChannel;
            VodChannelBean vodChannel = VodChannelInstance.getFullChannelBean("" + channel.id);
            if (mDialog != null && (curChan != null) && (curChan.getChid() == channel.getChid())) {
                /*StringBuilder sb = new StringBuilder().append("------ this.channel.getChid(): ");
                sb.append(this.mChannel.getChid());
                sb.append("channel.getChid(): ");
                sb.append(channel.getChid());
                sb.toString();*/
                return mDialog;
            }
            this.mChannel = channel;
            mDialog = new EpisodeDialog(this.mContext, R.style.MyDialog);
            View content = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                                    .inflate(R.layout.dialog_episode_layout, (ViewGroup) null);
            mDialog.addContentView(content, new ViewGroup.LayoutParams(-1, -2));
            TextView tvName = (TextView) content.findViewById(R.id.name);
            TextView tvDesc = (TextView) content.findViewById(R.id.description);
            TextView tvGenre = (TextView) content.findViewById(R.id.genre);
            TextView tvDuration = (TextView) content.findViewById(R.id.duration);
            this.mListView = (RecyclerView) content.findViewById(R.id.episode_rview);
            if (channel.getName() != null && channel.getName().getInit() != null && !channel.getName().getInit().equals("")) {
                tvName.setText(channel.getName().getInit());
            }
            if (channel.getDescription() != null && !channel.getDescription().equals("")) {
                tvDesc.setText(channel.getDescription());
            } else {
                tvDesc.setVisibility(View.GONE);
            }

            if (vodChannel != null) {
                if (vodChannel.genres != null && vodChannel.genres.length() > 0) {
                    tvGenre.setText(vodChannel.genres);
                } else {
                    tvGenre.setVisibility(View.GONE);
                }

                if (vodChannel.duration > 0) {
                    tvDuration.setText("" + vodChannel.duration);
                } else {
                    tvDuration.setVisibility(View.GONE);
                }
            } else {
                tvGenre.setVisibility(View.GONE);
                tvDuration.setVisibility(View.GONE);
            }

            if (channel.getSources() != null && channel.getSources().size() > 0) {
                List<ChannelBean.SourcesBean> sources = channel.getSources();
                GridLayoutManager glm = new GridLayoutManager(mContext, 6, RecyclerView.VERTICAL, false);
                mListView.addItemDecoration(new MyItemDecoration(0, 0, 15, 22));
                mListView.setLayoutManager(glm);
                mListView.setHasFixedSize(true);
                try {
                    mAdapter = new EpisodeAdapter(sources, channel.getName().getInit(), this.mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mListView.setAdapter(mAdapter);
            }
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { //new DialogInterface$OnKeyListenerC3616a(this));
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    boolean uniqueDown = event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN;
                    //String log = EpisodeDialog.TAG;
                    //String log = "=========== ============ onKey:" + keyCode + " event:" + event + " uniqueDown:" + uniqueDown;
                    if (uniqueDown) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) { //4
                            //String str3 = EpisodeDialog.TAG;
                            EpisodeDialog.Helper.mDialog.dismiss();
                            return true;
                        } else if (keyCode == KeyEvent.KEYCODE_MENU) { //82
                            MainActivity.mMsgHandler.sendEmptyMessage(100);
                            EpisodeDialog.Helper.hide();
                            return true;
                        }
                    }
                    return false;
                }
            });
            mDialog.setContentView(content);
            return mDialog;
        }
    }

    public EpisodeDialog(Context context) {
        super(context);
    }

    public EpisodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
}
