package com.example.user.downloadmanager.downloadmanager;

public interface DownloadListener {

    void pending(int soFarBytes, int totalBytes);

    void progress(int soFarBytes, int totalBytes);

    void completed(int fileSize);
}
