package com.brazvip.fivetv.instances;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.beans.UpdateInfo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

public class UpdateInstance {
    public static UpdateInfo updateInfo;

    public void refreshUpdateInfo() {
        final String url = AuthInstance.getApiUrl(AuthInstance.API_TYPE.UPDATE);
        if (url.equals("")) {
            return;
        }
        new Thread() { // from class: org.sopcast.android.p220b.UpdateInstance.1
            /* JADX WARN: Type inference failed for: r1v2, types: [com.lzy.okgo.request.base.Request] */
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    new PostRequest(url).removeHeader(HttpHeaders.HEAD_KEY_USER_AGENT).headers(
                            HttpHeaders.HEAD_KEY_USER_AGENT,
                            SopApplication.strUserAgent).tag(this).cacheMode(CacheMode.NO_CACHE).params(
                                    "package", SopApplication.strUserAgent, new boolean[0]).params(
                                            "channel", Config.crashReportAppChannel, new boolean[0]).execute(new StringCallback() {
                        @Override // com.lzy.okgo.callback.Callback
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()) {
                                try {
                                    UpdateInstance.updateInfo = (UpdateInfo) JSON.parseObject(response.body(), UpdateInfo.class);
                                    MainActivity.handler.sendEmptyMessage(60);
                                    return;
                                } catch (Exception unused) {
                                }
                            }
                            MainActivity.handler.sendEmptyMessage(61);
                        }
                    });
                } catch (Exception unused) {
                    MainActivity.handler.sendEmptyMessage(61);
                }
            }
        }.start();
    }
}