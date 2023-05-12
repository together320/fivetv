package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.util.List;

public class ChannelInstance {
    private static Handler mMsgHandler = null;

    private static String mCacheKey = "ChannelInstance";

    public static void Refresh(Handler msgHandler) {
        mMsgHandler = msgHandler;

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
        List<ChannelBean> result = JSON.parseArray(text, ChannelBean.class);

        Message msg = new Message();
        msg.what = Constant.MSG_LOADED;
        mMsgHandler.sendMessage(msg);
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
