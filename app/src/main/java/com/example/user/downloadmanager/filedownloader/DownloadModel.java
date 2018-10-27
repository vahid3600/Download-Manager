package com.example.user.downloadmanager.filedownloader;

public class DownloadModel {

    private int id;

    private String url;

    private String path;

    private String avatar;

    private ProgressbarModel progressbarModle;

    public DownloadModel(String url, String path, String avatar, int id, ProgressbarModel progressbarModle) {
        this.url = url;
        this.path = path;
        this.avatar = avatar;
        this.id = id;
        this.progressbarModle = progressbarModle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setProgressbarModle(ProgressbarModel progressbarModle) {
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

    public ProgressbarModel getProgressbarModle() {
        return progressbarModle;
    }
}
