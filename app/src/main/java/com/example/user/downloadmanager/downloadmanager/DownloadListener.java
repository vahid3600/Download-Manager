package com.example.user.downloadmanager.downloadmanager;

public interface DownloadListener {

    void error(int id, String message);

    void pending(int id, ProgressModel progressModel);

    void progress(int id, ProgressModel progressModel);

    void completed(int id, ProgressModel progressModel);
}
