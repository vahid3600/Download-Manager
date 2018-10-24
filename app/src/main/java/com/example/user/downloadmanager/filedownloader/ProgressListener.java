package com.example.user.downloadmanager.filedownloader;

public interface ProgressListener {
    void onProgress(int soFarBytes, int totalBytes);
}
