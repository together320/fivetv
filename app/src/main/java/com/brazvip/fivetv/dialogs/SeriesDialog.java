package com.brazvip.fivetv.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.utils.StringUtil;
import com.brazvip.fivetv.utils.Utils;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import com.brazvip.fivetv.adapters.EpisodeAdapter;
import com.brazvip.fivetv.adapters.SeasonAdapter;
import com.brazvip.fivetv.beans.HistoryBean;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.instances.HistoryInstance;
import com.brazvip.fivetv.instances.VodChannelInstance;
import com.brazvip.fivetv.utils.EpisodeUtil;

import p123l2.C2001e;


public class SeriesDialog extends VodDialog {
    private static final String TAG = "SeriesDialog";
    public static Handler handler;
    private static SeriesDialog seriesDialog;
    private Context _context;
    private C2001e binding;
    public EpisodeAdapter episodeAdapter;
    private View.OnClickListener playBtnListener = new DialogOnClickListener1(this, 1);
    private SeasonAdapter seasonAdapter;

    public SeriesDialog(Context context, VodChannelBean vodChannelBean) {
        super(context, R.style.ThemeSplash);
        this._context = context;
        this.channel = vodChannelBean;
        this.FRAGMENT_TAG = TAG;
        HashSet hashSet = new HashSet();
        HashMap hashMap = new HashMap();
        for (VodChannelBean.Episode episode : vodChannelBean.getEpisodes()) {
            int parseSeasonFromEpisode = EpisodeUtil.parseSeasonFromEpisode(episode);
            hashSet.add(Integer.valueOf(parseSeasonFromEpisode));
            Integer num = (Integer) hashMap.get(Integer.valueOf(parseSeasonFromEpisode));
            hashMap.put(Integer.valueOf(parseSeasonFromEpisode), num == null ? 1 : Integer.valueOf(num.intValue() + 1));
        }
        ArrayList arrayList = new ArrayList(hashSet);
        Collections.sort(arrayList);
        try {
            this.episodeAdapter = new EpisodeAdapter(this.channel, this._context, VodDialog.MENU_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.seasonAdapter = new SeasonAdapter(arrayList, hashMap, this._context);
    }

    private void checkFavorite() {
        boolean isFavoriteVod = VodChannelInstance.isFavoriteVod(this.channel.getId());
        this.binding.f7106l.setText(isFavoriteVod ? R.string.rem_favorite : R.string.add_favorite);
        this.binding.f7103i.setVisibility(isFavoriteVod ? View.VISIBLE : View.GONE);
    }

    public static SeriesDialog createDialogImpl(Context context, VodChannelBean vodChannelBean) {
        seriesDialog = new SeriesDialog(context, vodChannelBean);
        seriesDialog.init();
        return seriesDialog;
    }

    public static void destroy() {
        if (seriesDialog != null) {
            seriesDialog.dismiss();
            seriesDialog = null;
        }
    }

    public static SeriesDialog getInstance() {
        if (seriesDialog == null) {
            return null;
        }
        return seriesDialog;
    }

    public static void Hide() {
        if (seriesDialog != null) {
            seriesDialog.dismiss();
        }
    }

    public static void videoPlayback(final SeriesDialog seriesDialog, final View view) {
        seriesDialog.onVideoPlayback(view);
    }

    public void onVideoPlayback(View view) {
        String str;
        String str2;
        String str3;
        String str4;
        HistoryBean lastHistoryEpisode = HistoryInstance.getLastHistoryEpisode(this.channel.getId());
        String str5 = "";
        if (lastHistoryEpisode != null) {
            Iterator<VodChannelBean.Episode> it = this.channel.getEpisodes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    str = "";
                    str2 = str;
                    str3 = str2;
                    break;
                }
                VodChannelBean.Episode next = it.next();
                String str6 = lastHistoryEpisode.subId;
                StringBuilder m6212l = StringUtil.m6212l("");
                m6212l.append(next.id);
                if (str6.equalsIgnoreCase(m6212l.toString())) {
                    String str7 = next.address;
                    String str8 = lastHistoryEpisode.Season;
                    if (str8 == null || str8.trim().isEmpty() || lastHistoryEpisode.Season.equals("null")) {
                        str4 = "";
                    } else {
                        str4 = lastHistoryEpisode.Season;
                    }
                    String str9 = lastHistoryEpisode.Episode;
                    if (str9 != null && !str9.trim().isEmpty() && !lastHistoryEpisode.Episode.equals("null")) {
                        str5 = lastHistoryEpisode.Episode;
                    }
                    str3 = str5;
                    str = str7;
                    str2 = str4;
                }
            }
            requestVideoPlayback(lastHistoryEpisode.subTitle, lastHistoryEpisode.subId, str, str2, str3, Boolean.FALSE);
            return;
        }
        String str10 = this.channel.getEpisodes().get(0).title;
        ArrayList arrayList = new ArrayList(this.channel.getEpisodes());
        Collections.reverse(arrayList);
        VodChannelBean.Episode episode = (VodChannelBean.Episode) arrayList.get(0);
        requestVideoPlayback(episode.title, String.valueOf(episode.id), episode.address, StringUtil.m6214j(new StringBuilder(), episode.season, ""), StringUtil.m6214j(new StringBuilder(), episode.episode, ""), Boolean.FALSE);
    }

    public static boolean onCreateView(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Objects.toString(dialogInterface);
        Objects.toString(keyEvent);
        if (keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0) {
            if (i != 4) {
                if (i == 82) {
                    //SopCast.handler.sendEmptyMessage(100);
                }
            }
            seriesDialog.dismiss();
            return true;
        }
        return false;
    }

    public boolean onCreateView(View view, int i, KeyEvent keyEvent) {
//        View view2 = null;
//        Handler handler2 = null;
//        int i2 = 0;
//        boolean z = keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0;
//        keyEvent.toString();
//        if (z) {
//            if (i == 4) {
//                if (VodDialog.MENU_TYPE == Config.MenuType.VOD) {
//                    handler2 = SopCast.handler;
//                    i2 = SopHandler.EVENT_FOCUS_VOD_BUTTON;
//                } else {
//                    handler2 = SopCast.handler;
//                    i2 = SopHandler.EVENT_DASHBOARD_FOCUS;
//                }
//            } else if (i == 82) {
//                handler2 = SopCast.handler;
//                i2 = 100;
//            } else if (view.getId() == this.binding.f7101g.getId()) {
//                if (i == 21) {
//                    view2 = this.binding.f7099e;
//                } else if (i == 22) {
//                    view2 = this.binding.f7098d;
//                }
//                view2.requestFocus();
//            }
//            handler2.sendEmptyMessage(i2);
//            seriesDialog.dismiss();
//            return true;
//        }
        return false;
    }

    public static void createView(final SeriesDialog seriesDialog, final View view) {
        seriesDialog.onCreateView(view);
    }

    public void onCreateView(View view) {
        if (VodChannelInstance.isFavoriteVod(this.channel.getId())) {
            Context context = this._context;
            Toast.makeText(context, this.channel.getTitle() + " " + this._context.getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
            VodChannelInstance.removeFavoriteChannel(this.channel.getId());
        } else {
            Context context2 = this._context;
            Toast.makeText(context2, this.channel.getTitle() + " " + this._context.getString(R.string.favorited), Toast.LENGTH_LONG).show();
            VodChannelInstance.addFavoriteChannel(this.channel.getId());
        }
        checkFavorite();
    }

    public static View m385i(int i, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View findViewById = viewGroup.getChildAt(i2).findViewById(i);
                if (findViewById != null) {
                    return findViewById;
                }
            }
            return null;
        }
        return null;
    }

    public void init() {

        View inflate = ((LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.episode_layout_dialog_v3, null);
        seriesDialog.addContentView(inflate, new ViewGroup.LayoutParams(-1, -2));

        String str;
        String str2;
        String str3;
        int i = R.id._ep_separator1;
        if (((TextView) Utils.m385i(R.id._ep_separator1, inflate)) != null) {
            i = R.id._ep_separator2;
            if (Utils.m385i(R.id._ep_separator2, inflate) != null) {
                i = R.id.backdrop;
                ImageView imageView = (ImageView) Utils.m385i(R.id.backdrop, inflate);
                if (imageView != null) {
                    i = R.id.backdrop_tint;
                    View m385i = Utils.m385i(R.id.backdrop_tint, inflate);
                    if (m385i != null) {
                        i = R.id.episode_rv;
                        RecyclerView recyclerView = (RecyclerView) Utils.m385i(R.id.episode_rv, inflate);
                        if (recyclerView != null) {
                            i = R.id.play_button;
                            LinearLayout linearLayout = (LinearLayout) Utils.m385i(R.id.play_button, inflate);
                            if (linearLayout != null) {
                                i = R.id.play_button_text;
                                TextView textView = (TextView) Utils.m385i(R.id.play_button_text, inflate);
                                if (textView != null) {
                                    i = R.id.season_rv;
                                    RecyclerView recyclerView2 = (RecyclerView) Utils.m385i(R.id.season_rv, inflate);
                                    if (recyclerView2 != null) {
                                        i = R.id.series_description;
                                        TextView textView2 = (TextView) Utils.m385i(R.id.series_description, inflate);
                                        if (textView2 != null) {
                                            i = R.id.series_fav_img;
                                            ImageView imageView2 = (ImageView) Utils.m385i(R.id.series_fav_img, inflate);
                                            if (imageView2 != null) {
                                                i = R.id.series_name;
                                                TextView textView3 = (TextView) Utils.m385i(R.id.series_name, inflate);
                                                if (textView3 != null) {
                                                    i = R.id.series_toggle_fav;
                                                    LinearLayout linearLayout2 = (LinearLayout) Utils.m385i(R.id.series_toggle_fav, inflate);
                                                    if (linearLayout2 != null) {
                                                        i = R.id.series_toggle_fav_txt;
                                                        TextView textView4 = (TextView) Utils.m385i(R.id.series_toggle_fav_txt, inflate);
                                                        if (textView4 != null) {
                                                            i = R.id.x_desc_scroll;
                                                            if (((ScrollView) Utils.m385i(R.id.x_desc_scroll, inflate)) != null) {
                                                                this.binding = new C2001e((RelativeLayout) inflate, imageView, m385i, recyclerView, linearLayout, textView, recyclerView2, textView2, imageView2, textView3, linearLayout2, textView4);
                                                                if (this.channel.getTitle() != null && !this.channel.getTitle().isEmpty()) {
                                                                    this.binding.f7104j.setText(this.channel.getTitle());
                                                                }
                                                                if (this.channel.getOverview() != null && !this.channel.getOverview().isEmpty()) {
                                                                    this.binding.f7102h.setText(this.channel.getOverview());
                                                                } else {
                                                                    this.binding.f7102h.setVisibility(View.INVISIBLE);
                                                                }
                                                                this.channel.getEpisodes();
                                                                EpisodeAdapter episodeAdapter = this.episodeAdapter;
                                                                if (episodeAdapter != null) {
                                                                    this.binding.f7098d.setAdapter(episodeAdapter);
                                                                }
                                                                HistoryBean lastHistoryEpisode = HistoryInstance.getLastHistoryEpisode(this.channel.getId());
                                                                if (lastHistoryEpisode != null) {
                                                                    Iterator<VodChannelBean.Episode> it = this.channel.getEpisodes().iterator();
                                                                    while (true) {
                                                                        str = "";
                                                                        if (!it.hasNext()) {
                                                                            str2 = "";
                                                                            str3 = str2;
                                                                            break;
                                                                        }
                                                                        String str4 = lastHistoryEpisode.subId;
                                                                        StringBuilder m6212l = StringUtil.m6212l("");
                                                                        m6212l.append(it.next().id);
                                                                        if (str4.equalsIgnoreCase(m6212l.toString())) {
                                                                            String str5 = lastHistoryEpisode.Season;
                                                                            if (str5 == null || str5.trim().isEmpty() || lastHistoryEpisode.Season.equals("null")) {
                                                                                str3 = "";
                                                                            } else {
                                                                                StringBuilder m6212l2 = StringUtil.m6212l(" - S");
                                                                                m6212l2.append(lastHistoryEpisode.Season);
                                                                                str3 = m6212l2.toString();
                                                                            }
                                                                            String str6 = lastHistoryEpisode.Episode;
                                                                            if (str6 == null || str6.trim().isEmpty() || lastHistoryEpisode.Episode.equals("null")) {
                                                                                str2 = "";
                                                                            } else {
                                                                                StringBuilder m6212l3 = StringUtil.m6212l("E");
                                                                                m6212l3.append(lastHistoryEpisode.Episode);
                                                                                str2 = m6212l3.toString();
                                                                            }
                                                                        }
                                                                    }
                                                                    TextView textView5 = this.binding.f7100f;
                                                                    StringBuilder sb = new StringBuilder();
                                                                    sb.append(SopApplication.getAppContext().getString(R.string.ContinueStr));
                                                                    if (str3 == " - Snull") {
                                                                        str3 = "";
                                                                    }
                                                                    sb.append(str3);
                                                                    if (str2 != "Enull") {
                                                                        str = str2;
                                                                    }
                                                                    sb.append(str);
                                                                    textView5.setText(sb.toString());
                                                                } else {
                                                                    this.binding.f7100f.setText(R.string.Play);
                                                                }
                                                                this.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.brazvip.fivetv.dialog.f
                                                                    @Override // android.content.DialogInterface.OnKeyListener
                                                                    public final boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                                                                        boolean lambda$onCreateView$1;
                                                                        lambda$onCreateView$1 = SeriesDialog.onCreateView(dialogInterface, i2, keyEvent);
                                                                        return lambda$onCreateView$1;
                                                                    }
                                                                });
                                                                this.binding.f7095a.setOnKeyListener(new View.OnKeyListener() {
                                                                    @Override
                                                                    public boolean onKey(View view, int i2, KeyEvent keyEvent) {
                                                                        boolean lambda$onCreateView$2;
                                                                        lambda$onCreateView$2 = SeriesDialog.this.onCreateView(view, i2, keyEvent);
                                                                        return lambda$onCreateView$2;
                                                                    }
                                                                });
                                                                this.binding.f7101g.setHasFixedSize(true);
                                                                this.binding.f7101g.setAdapter(this.seasonAdapter);
                                                                checkFavorite();
                                                                this.binding.f7105k.setOnClickListener(new DialogOnClickListener1(this, 0));
                                                                this.binding.f7099e.setOnClickListener(this.playBtnListener);
                                                                this.binding.f7099e.requestFocus(1);
//                                                                InterfaceC3271f interfaceC3271f = new InterfaceC3271f() {
//                                                                    @Override
//                                                                    public boolean onLoadFailed(C1757b0 c1757b0, Object obj, InterfaceC3306e interfaceC3306e, boolean z) {
//                                                                        return false;
//                                                                    }
//
//                                                                    @Override
//                                                                    public boolean onResourceReady(Drawable drawable, Object obj, InterfaceC3306e interfaceC3306e, EnumC1644a enumC1644a, boolean z) {
//                                                                        SeriesDialog.this.binding.f7097c.setVisibility(View.VISIBLE);
//                                                                        return false;
//                                                                    }
//                                                                };
                                                                String str7 = this.channel.backdrop;
                                                                if (str7 != null && !str7.trim().isEmpty()) {
                                                                    this.binding.f7096b.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                                    Glide.with(this._context)
                                                                            .load(this.channel.backdrop)
                                                                            //.load(DiskCacheStrategy.DATA))
                                                                            //.m4978s(interfaceC3271f)
                                                                            .into(this.binding.f7096b);
                                                                }
                                                                //return this.binding.f7095a;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
