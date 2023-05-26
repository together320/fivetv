package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileLayout extends RelativeLayout {
    public ImageView mProfileAvatarView;
    public TextView mProfileIdView;

    public RelativeLayout mExpiredMsgView;

    public TextView mAccountInfoUser;
    public TextView mAccountInfoPackage;
    public TextView mAccountInfoDate;

    public ProfileLayout(Context context) {
        super(context);
        init(context);
    }

    public ProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfileLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.profile_layout, this, true);

        initComponents();
        setComponents();
    }

    private void initComponents() {
        mProfileAvatarView = (ImageView) findViewById(R.id.profile_avatar);
        mProfileIdView = (TextView) findViewById(R.id.profile_id);

        mExpiredMsgView = (RelativeLayout) findViewById(R.id.expired_acc_message);

        mAccountInfoUser = (TextView) findViewById(R.id.account_info_user);
        mAccountInfoPackage = (TextView) findViewById(R.id.account_info_package);
        mAccountInfoDate = (TextView) findViewById(R.id.account_info_date);
    }

    private void setComponents() {

        mProfileAvatarView.setClickable(true);
        mProfileAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                MainActivity.SendMessage(Constant.MSG_PLAYER_LOADED);
            }
        });

        mExpiredMsgView.setVisibility(View.GONE);

        String  strName = AuthInstance.mAuthInfo.user.user_name.split("@")[0];

        mProfileIdView.setText(strName);
        mAccountInfoUser.setText(strName);
        mAccountInfoPackage.setText(AuthInstance.mAuthInfo.service.name);

        if (AuthInstance.mAuthInfo.user.EndTime > 0) {
            mAccountInfoDate.setText(new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).format(Long.valueOf(AuthInstance.mAuthInfo.user.EndTime)));

//            long currentTimeMillis = ((AuthInstance.mAuthInfo.user.EndTime - (System.currentTimeMillis() + PrefUtils.DELTA_TIME)) / 3600) / 1000;
//            int i = (int) (currentTimeMillis / 24);
//            int i2 = (int) (currentTimeMillis % 24);
//            if (i < 0) {
//                i = 0;
//            }
//            if (i2 < 0) {
//                i2 = 0;
//            }
//            if (i != 0) {
//                if (i == 1) {
//                    mAccountInfoDate.setText(String.format(getResources().getString(R.string.service_expire_info), Integer.valueOf(i)));
//                    return;
//                }
//                return;
//            }
//            mAccountInfoDate.setText(String.format(getResources().getString(R.string.service_expire_info_hours), Integer.valueOf(i2));
        } else {
            mAccountInfoDate.setText(getResources().getString(R.string.nolimit));
        }
    }
}