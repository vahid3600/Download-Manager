package com.example.user.downloadmanager.filedownloader;

import com.example.user.downloadmanager.downloadmanager.ProgressModel;

public class DownloadModel {

    private String url;

    private String path;

    private String avatar;

    private ProgressModel progressModel;

    public DownloadModel(String url, String path, String avatar, ProgressModel progressModel) {
        this.url = url;
        this.path = path;
        this.avatar = avatar;
        this.progressModel = progressModel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setProgressModel(ProgressModel progressModel) {
        this.progressModel = progressModel;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getAvatar() {
        return avatar;
    }

    public ProgressModel getProgressModel() {
        return progressModel;
    }
}
