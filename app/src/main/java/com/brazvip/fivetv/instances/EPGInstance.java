package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

public class EPGInstance {
    private static Handler mMsgHandler = null;

    private static String mCacheKey = "EPGInstance";

    public static void Refresh(Handler msgHandler) {
        mMsgHandler = msgHandler;

        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.EPG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((GetRequest) ((GetRequest) new GetRequest(url).tag(this)).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                asyncParseEpgs(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    asyncParseEpgs(response.body());
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

    private static void asyncParseEpgs(String result) {
        try {

        }
        catch (Exception e) {

        }
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get epg list!");
    }
}
