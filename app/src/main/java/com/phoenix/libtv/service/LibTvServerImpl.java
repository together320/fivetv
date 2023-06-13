package com.phoenix.libtv.service;

import com.phoenix.libtv.ILibTvService;
import com.phoenix.libtv.Libtv;

/* loaded from: classes.dex */
public class LibTvServerImpl extends ILibTvService.Stub {
    static {
        Libtv.touch();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public boolean doLogin() {
        return Libtv.doLogin();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheChannels(String str) {
        return Libtv.getCacheChannels(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheDashboard() {
        return Libtv.getCacheDashboard();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheEpg(String str) {
        return Libtv.getCacheEpg(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheVod(String str) {
        return Libtv.getCacheVod(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheVodGroups() {
        return Libtv.getCacheVodGroups();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getCacheVodSubgroups(String str) {
        return Libtv.getCacheVodSubgroups(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getLoginData() {
        return Libtv.getLoginData();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getLoginPrefix() {
        return Libtv.getLoginPrefix();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getServerDate() {
        return Libtv.getServerDate();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String getUserPass(String str) {
        return Libtv.getUserPass(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public boolean profileAuth(String str, String str2) {
        return Libtv.profileAuth(str, str2);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String profileCreate(String str, String str2, boolean z, String str3) {
        return Libtv.profileCreate(str, str2, z, str3);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String profileDelete(String str) {
        return Libtv.profileDelete(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String profileUpdate(String str) {
        return Libtv.profileUpdate(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public String profilesGet() {
        return Libtv.profilesGet();
    }

    @Override // com.phoenix.libtv.ILibTvService
    public boolean setAppLicense(String str) {
        return Libtv.setAppLicense(str);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public void setAuthData(String str, String str2) {
        Libtv.setAuthData(str, str2);
    }

    @Override // com.phoenix.libtv.ILibTvService
    public void setConfig(String str) {
        Libtv.setConfig(str);
    }
}
