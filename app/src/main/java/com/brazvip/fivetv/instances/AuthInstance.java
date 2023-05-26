package com.brazvip.fivetv.instances;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.beans.AuthInfo;
import com.brazvip.fivetv.utils.PrefUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

public class AuthInstance {
    public static AuthInfo mAuthInfo = null;
    private static String mUserName = "";
    private static String mPassword = "";
    private static Handler mMsgHandler = null;

    private static String mCacheKey = "AuthInstance";

    public static boolean LoadAuthParams() {
        mUserName = PrefUtils.getPrefString("username", "");
        mPassword = PrefUtils.getPrefString("password", "");
        if (mUserName == "" || mPassword == "")
            return false;
        return true;
    }

    public static void SaveAuthParams(String username, String password) {
        PrefUtils.setPrefString("username", username);
        PrefUtils.setPrefString("password", password);
    }


    public static void Login(Handler msgHandler) {
        Login(mUserName, mPassword, msgHandler);
    }

    public static void Login(String username, String password, Handler msgHandler) {
        mMsgHandler = msgHandler;

        HttpParams httpParams = new HttpParams();
        if (!username.contains("@"))
            username += Constant.DEFAULT_MAIL_SUFFIX;
        httpParams.put("username", username);
        httpParams.put("password", password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((PostRequest<String>) new PostRequest(Constant.LOGIN_URL)
                        .params(httpParams).tag(this).cacheKey(mCacheKey)).execute(
                        new StringCallback() {
                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                onLoginSuccess(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                onLoginFail();
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.isSuccessful()) {
                                    onLoginSuccess(response.body());
                                } else {
                                    onLoginFail();
                                }
                            }
                        }
                );
            }
        }
        ).start();
    }

    private static void onLoginSuccess(String result) {
        try {
            Log.d("AuthInstance", result);
            mAuthInfo = JSON.parseObject(result, AuthInfo.class);

            if (mAuthInfo.code != 0 && mAuthInfo.code != -12) {
                PrefUtils.Toast(mAuthInfo.result);

                Message msg = new Message();
                msg.what = Constant.MSG_LOGIN_FAIL;
                mMsgHandler.sendMessage(msg);
            }
            else {
                Message msg = new Message();
                msg.what = Constant.MSG_LOGIN_SUCCESS;
                mMsgHandler.sendMessage(msg);
            }
        }
        catch (Exception e) {

        }
    }

    private static void onLoginFail() {
        PrefUtils.Toast("Login server no response, retry later!");
    }

    public enum API_TYPE {
        AUTH,
        CHANNEL,
        VOD_CHANNEL,
        EPG,
        UPDATE,
        MESSAGE
    }

    public static String addApiToken(String baseUrl) {
        if (baseUrl == null || baseUrl.equals("")) {
            return "";
        }
        if (baseUrl.indexOf("?") > 0) {
            return (baseUrl + "&t=" + mAuthInfo.service.token);
        }
        return (baseUrl + "?t=" + mAuthInfo.service.token);
    }

    public static String getApiUrl(API_TYPE type) {
        AuthInfo authInfo = mAuthInfo;
        String url = "";
        if (authInfo != null && authInfo.service != null) {
            switch (type) {
                case AUTH:
                    url = mAuthInfo.service.auth_url;
                    break;
                case CHANNEL:
                    url = addApiToken(mAuthInfo.service.ch_url);
                    break;
                case VOD_CHANNEL:
                    url = addApiToken(mAuthInfo.service.vod_url);
                    break;
                case EPG:
                    url = addApiToken(mAuthInfo.service.epg_url);
                    break;
                case UPDATE:
                    url = addApiToken(mAuthInfo.service.update_url);
                    break;
                case MESSAGE:
                    url = addApiToken(mAuthInfo.service.message_url);
                    break;
            }
            return url;
        }
        return "";
    }
}
