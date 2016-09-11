package com.skymxc.demo.practicenetwork_volley;

/**
 * Created by sky-mxc
 * 版本实体类
 */
public class VersionInfo {
    private String appUrl;
    private int versionCode;
    private String versionName;
    private int vid;

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVid() {
        return vid;
    }
}
