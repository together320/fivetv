package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.UpdateInfo;
import com.brazvip.fivetv.instances.ChannelInstance;
import com.brazvip.fivetv.instances.UpdateInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.db.CacheManager;

public class SettingLayout extends RelativeLayout {

    private CardView mAutoStartCardView;
    private SwitchCompat mAutoStartSwitchCompat;

    private CardView mUsePlayerCardView;
    private TextView mUsePlayerTextView;

    private boolean isAutoStart;
    private boolean isUseSystemPlayer;

    private CardView mClearCacheCardView;

    private CardView mUpdateCardView;
    private TextView mUpdateTextView;

    private UpdateInstance mUpdateInstance = null;

    public SettingLayout(Context context) {
        super(context);
        init(context);
    }

    public SettingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SettingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.setting_layout, this, true);

        initComponents();
        setComponents();
    }

    private void initComponents() {
        mAutoStartCardView = (CardView) findViewById(R.id.auto_start_card);
        mAutoStartSwitchCompat = (SwitchCompat) findViewById(R.id.auto_start_switch);

        mUsePlayerCardView = (CardView) findViewById(R.id.player_switch_card);
        mUsePlayerTextView = (TextView) findViewById(R.id.player_text);

        mClearCacheCardView = (CardView) findViewById(R.id.clear_data_card);

        mUpdateCardView = (CardView) findViewById(R.id.update_card);
        mUpdateTextView = (TextView) findViewById(R.id.update_text);
    }

    private void setComponents() {
        mAutoStartCardView.setClickable(true);
        mAutoStartCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
//                isAutoStart = !isAutoStart;
//                setAutoStart(isAutoStart);
            }
        });

        isAutoStart = PrefUtils.getPrefBool(Config.SP_IS_AUTO_START, false);
        mAutoStartSwitchCompat.setChecked(isAutoStart);

        mUsePlayerCardView.setClickable(true);
        mUsePlayerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
//                isUseSystemPlayer = !isUseSystemPlayer;
//                setUsePlayer(isUseSystemPlayer);
            }
        });

        isUseSystemPlayer = PrefUtils.getPrefBool(Config.SP_PLAYER, false);
        setUsePlayer(isUseSystemPlayer);

        mClearCacheCardView.setClickable(true);
        mClearCacheCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                setClearCache();
            }
        });

        mUpdateCardView.setClickable(true);
        mUpdateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                setUpdateVersion();
            }
        });
        mUpdateTextView.setText(SopApplication.getSopContext().getString(R.string.current_version) + ": " + SopApplication.strAppVersion);
    }

    private void setAutoStart(boolean flag) {
        mAutoStartSwitchCompat.setChecked(flag);
        mAutoStartSwitchCompat.requestFocus();
        PrefUtils.setPrefBool(Config.SP_IS_AUTO_START, flag);
    }

    private void setUsePlayer(boolean flag) {
        if (flag)
            mUsePlayerTextView.setText(R.string.player_system);
        else
            mUsePlayerTextView.setText(R.string.player_exo);
        PrefUtils.setPrefBool(Config.SP_PLAYER, flag);

        //SopCast.switchPlayer();
    }

    private void setClearCache() {
        CacheManager.getInstance().clear();

//        BSHistory.liveHistory = new CustomQueue<>(100);
//        BSHistory.vodHistory = new CustomQueue<>(Priority.UI_NORMAL);
//        SopCast.cacheManager.saveObject("liveHistory", BSHistory.liveHistory, 315360000);
//        SopCast.cacheManager.saveObject("vodHistory", BSHistory.vodHistory, 315360000);
//        SopCast.cacheManager.clearCache();

        ChannelInstance.channelGrouping();
        Message message = new Message();
        message.what = 1;
        MenuLayout.mMsgHandler.sendMessage(message);

//        SopCast.handler.sendEmptyMessage(SopHandler.EVENT_RELOAD_VOD_GROUPS);
//        Message message2 = new Message();
//        message2.what = 1;
//        VodFragment.handler.sendMessage(message2);

        PrefUtils.Toast(R.string.done);
    }

    private void setUpdateVersion() {
        MainActivity.isUpdating = true;

        mUpdateInstance = new UpdateInstance();
        mUpdateInstance.refreshUpdateInfo();
    }
}