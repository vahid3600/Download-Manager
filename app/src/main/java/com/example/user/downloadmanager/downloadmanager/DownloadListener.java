package com.example.user.downloadmanager.downloadmanager;

public interface DownloadListener {

    void error(int id, String message);

    void pending(ProgressModel progressModel);

    void progress(ProgressModel progressModel);

    void completed(ProgressModel progressModel);
}
