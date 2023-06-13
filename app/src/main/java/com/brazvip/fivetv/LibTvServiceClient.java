package com.brazvip.fivetv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.phoenix.libtv.ILibTvService;
import com.phoenix.libtv.service.LibTvService;

public class LibTvServiceClient {
    private static final String TAG = "LibTvServiceClient";
    private final SopApplication application;
    private boolean bindingWait = false;
    private ILibTvService libTvService;
    private TvServiceConnection svcConnection;
    
    public class TvServiceConnection implements ServiceConnection {
        private boolean serviceConnected = false;

        public TvServiceConnection() {
        }

        public boolean isServiceConnected() {
            return this.serviceConnected;
        }

        @Override
        public void onBindingDied(ComponentName componentName) {
            Log.d("LibTVServiceClient", "onBindingDied() - componetName: " + componentName);

            LibTvServiceClient.this.bindingWait = false;

            SplashActivity.mMsgHandler.sendEmptyMessage(Constant.MSG_LIVTV_SERVICE_BINDING_DIED);
        }

        @Override
        public void onNullBinding(ComponentName componentName) {
            Log.d("LibTVServiceClient", "onNullBinding() - componetName: " + componentName);

            LibTvServiceClient.this.bindingWait = false;

            SplashActivity.mMsgHandler.sendEmptyMessage(Constant.MSG_LIVTV_SERVICE_BINDING_NULL);
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("LibTVServiceClient", "onServiceConnected() - componetName: " + componentName);

            LibTvServiceClient.this.libTvService = ILibTvService.Stub.asInterface(iBinder);
            LibTvServiceClient.this.bindingWait = false;
            this.serviceConnected = true;

            SplashActivity.mMsgHandler.sendEmptyMessage(Constant.MSG_LIBTV_SERVICE_CONNECTED);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("LibTVServiceClient", "onServiceDisconnected() - componetName: " + componentName);

            LibTvServiceClient.this.libTvService = null;
            LibTvServiceClient.this.bindingWait = false;
            this.serviceConnected = false;

            SplashActivity.mMsgHandler.sendEmptyMessage(Constant.MSG_LIVTV_SERVICE_DISCONNECTED);
        }
    }

    public LibTvServiceClient(SopApplication sopApplication) {
        this.application = sopApplication;
        bindToLibTvService();
    }

    public static LibTvServiceClient getInstance() {
        return SopApplication.libTvClient;
    }

    private ILibTvService getLibService() {
        if (this.libTvService == null) {
            bindToLibTvService();
        }
        return this.libTvService;
    }

    public void bindToLibTvService() {
        if (this.bindingWait) {
            return;
        }
        this.bindingWait = true;
        this.application.startLibTvService();
        this.svcConnection = new TvServiceConnection();
        this.application.bindService(new Intent(LibTvService.ACTION).setPackage(Constant.APP_ID), this.svcConnection, Context.BIND_AUTO_CREATE);
    }

    public boolean doLogin() {
        try {
            return getLibService().doLogin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCacheChannels(String str) {
        try {
            return getLibService().getCacheChannels(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCacheDashboard() {
        try {
            return getLibService().getCacheDashboard();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCacheEpg(String str) {
        try {
            return getLibService().getCacheEpg(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCacheVod(String str) {
        try {
            return getLibService().getCacheVod(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCacheVodGroups() {
        try {
            return getLibService().getCacheVodGroups();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getCacheVodSubgroups(String str) {
        try {
            return getLibService().getCacheVodSubgroups(str);
        } catch (Exception e) {
            e.getMessage();
            return "";
        }
    }

    public String getLoginData() {
        try {
            return getLibService().getLoginData();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLoginPrefix() {
        try {
            return getLibService().getLoginPrefix();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getServerDate() {
        try {
            return getLibService().getServerDate();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUserPass(String str) {
        try {
            return getLibService().getUserPass(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean profileAuth(String str, String str2) {
        try {
            return getLibService().profileAuth(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String profileCreate(String str, String str2, boolean z, String str3) {
        try {
            return getLibService().profileCreate(str, str2, z, str3);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String profileDelete(String str) {
        try {
            return getLibService().profileDelete(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String profileUpdate(String str) {
        try {
            return getLibService().profileUpdate(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String profilesGet() {
        try {
            return getLibService().profilesGet();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean setAppLicense(String str) {
        try {
            return getLibService().setAppLicense(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setAuthData(String str, String str2) {
        try {
            getLibService().setAuthData(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfig(String str) {
        try {
            getLibService().setConfig(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unbindAndStop() {
        if (this.svcConnection.isServiceConnected()) {
            try {
                this.application.unbindService(this.svcConnection);
            } catch (RuntimeException e) {
                e.getMessage();
            }
        }
        this.application.stopService(new Intent(LibTvService.ACTION).setPackage(Constant.APP_ID));
    }
}
