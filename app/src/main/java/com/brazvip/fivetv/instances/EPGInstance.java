package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.beans.EpgBeans;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EPGInstance {
    public static volatile List<EpgBeans> mEpgs;

    private static String mCacheKey = "EPGInstance";

    public static void Refresh() {
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

    public static void parseJson(String text) {
        mEpgs = JSON.parseArray(text, EpgBeans.class);

        Message msg = new Message();
        msg.what = Constant.MSG_EPG_LOADED;
        MainActivity.SendMessage(msg);
    }

    private static void asyncParseEpgs(String result) {
        new Thread() {
            @Override
            public void run() {
                try {
                    EPGInstance.parseJson(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void onFail() {
        PrefUtils.Toast("Failed to get epg list!");
    }
}
