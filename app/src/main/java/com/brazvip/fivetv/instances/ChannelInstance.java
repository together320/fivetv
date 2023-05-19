package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.Group;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.util.HashMap;
import java.util.List;

public class ChannelInstance {

    private static String mCacheKey = "ChannelInstance";

    public static volatile HashMap<Integer, Group> mGroups;
    public static volatile List<ChannelBean> mChannels;

    public static void Refresh() {
        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.CHANNEL);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                asyncParseChannels(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    asyncParseChannels(response.body());
                                } else {
                                    onFail();
                                }
                            }
                        }
                );
            }
        }
        ).start();
    }

    public static void parseJson(String text) {
        mGroups = new HashMap<>();
        mChannels = JSON.parseArray(text, ChannelBean.class);

        Group a = new Group();
        a.name = SopApplication.getAppContext().getString(R.string.Favorites);
        a.id = Constant.GROUP_FAVORITE;
        a.type = 0;
        mGroups.put(a.id, a);

        a = new Group();
        a.name = SopApplication.getAppContext().getString(R.string.All_A_Z);
        a.id = Constant.GROUP_ALL;
        a.type = 0;
        mGroups.put(a.id, a);

        for (ChannelBean channel : mChannels) {
            List<ChannelBean.TagsBean> tags = channel.getTags();

            for (ChannelBean.TagsBean tag : tags) {
                if (mGroups.get(tag.getId()) == null) {
                    a = new Group();
                    a.name = tag.getName().getInit();
                    a.id = tag.getId();
                    a.type = tag.getType();
                    a.restrictedAccess = tag.isRestrictedAccess();
                    mGroups.put(tag.getId(), a);
                }
            }
        }

        Message msg = new Message();
        msg.what = Constant.MSG_CHANNEL_LOADED;
        MainActivity.SendMessage(msg);
    }

    private static void asyncParseChannels(String result) {
        new Thread() {
            @Override
            public void run() {
                try {
                    ChannelInstance.parseJson(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get channel list!");
    }
}
