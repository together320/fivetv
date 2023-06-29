package com.brazvip.fivetv.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.Progress;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.adapters.EpisodeAdapter;
import com.brazvip.fivetv.beans.vod.VodChannelBean;


public class VodDialog extends Dialog implements DialogInterface.OnCancelListener {

    public static Config.MenuType MENU_TYPE = null;
    public static final int MOVIE = 100;
    public static final int SERIES = 101;
    public static int currentDialog;
    public String FRAGMENT_TAG = "VodDialog";
    public VodChannelBean channel;

    public VodDialog(Context context) {
        super(context);
    }

    public VodDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Retention(RetentionPolicy.SOURCE)
    
    public @interface CurrentDialog {
    }

    public static VodDialog createDialog(Context context, VodChannelBean vodChannelBean, Config.MenuType menuType) {
        Objects.toString(menuType);
        vodChannelBean.getTitle();
        if (menuType == null) {
            menuType = Config.MenuType.VOD;
        }
        MENU_TYPE = menuType;
        if (vodChannelBean.vodType == 1) {
            currentDialog = SERIES;
            return SeriesDialog.createDialogImpl(context, vodChannelBean);
        }
        currentDialog = MOVIE;
        return MovieDialog.createDialogImpl(context, vodChannelBean);
    }

    public static void destroyAll() {
        SeriesDialog.destroy();
        MovieDialog.destroy();
    }

    public static void dismissAll() {
        SeriesDialog.Hide();
        MovieDialog.Hide();
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public static VodDialog getLatestInstance() {
        EpisodeAdapter episodeAdapter;
        if (currentDialog == 100) {
            return MovieDialog.getInstance();
        }
        SeriesDialog seriesDialog = SeriesDialog.getInstance();
        if (seriesDialog != null && (episodeAdapter = seriesDialog.episodeAdapter) != null) {
            episodeAdapter.notifyDataSetChanged();
        }
        return seriesDialog;
    }

    @Override // androidx.fragment.app.DialogInterface$OnCancelListenerC0312n, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeSplash);
    }

    public void requestVideoPlayback(String str, String str2, String str3, String str4, String str5, Boolean bool) {
        Message message = new Message();
        message.what = Constant.MSG_PLAYER_PLAY_VIDEO;
        Bundle bundle = new Bundle();
        bundle.putString(Progress.URL, str3);
        bundle.putString("chid", this.channel.getId());
        bundle.putString(SerializableCookie.NAME, this.channel.getTitle());
        bundle.putString("subId", str2);
        bundle.putString("season", str4);
        bundle.putString("episode", str5);
        bundle.putString("subTitle", str);
        bundle.putBoolean("restricted", bool.booleanValue());
        bundle.putString("type", (str3.contains("tvcar://") ? Config.VIDEO_TYPE.BSVOD : Config.VIDEO_TYPE.f8646d).name());
        bundle.putString("menuType", MENU_TYPE.name());
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);
        dismissAll();
    }
}
