package com.example.user.downloadmanager.filedownloader;

public class DownloadModel {

    private String url;

    private String path;

    private String avatar;

    private ProgressbarModle progressbarModle;

    public DownloadModel(String url, String path, String avatar, ProgressbarModle progressbarModle) {
        this.url = url;
        this.path = path;
        this.avatar = avatar;
        this.progressbarModle = progressbarModle;
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

    public void setProgressbarModle(ProgressbarModle progressbarModle) {
        this.progressbarModle = progressbarModle;
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

    public ProgressbarModle getProgressbarModle() {
        return progressbarModle;
    }
}
