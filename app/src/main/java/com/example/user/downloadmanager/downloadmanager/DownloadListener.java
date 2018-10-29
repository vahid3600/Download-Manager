package com.example.user.downloadmanager.downloadmanager;

public interface DownloadListener {

    void error(int id, ProgressModel progressModel);

    void pending(int id, ProgressModel progressModel);

    void pause(int id, ProgressModel progressModel);

    void progress(int id, ProgressModel progressModel);

    void completed(int id, ProgressModel progressModel);
}
