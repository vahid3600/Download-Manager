package com.example.user.downloadmanager.downloadmanager;

public class ProgressModel {

    private int id;
    private String name;
    private String status;
    private int soFarBytes;
    private int totalBytes;
    private int downloadSpeed;
    private String downloadError;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getSoFarBytes() {
        return soFarBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public ProgressModel(int id, String name, String status, int soFarBytes, int totalBytes, int downloadSpeed, String downloadError) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.soFarBytes = soFarBytes;
        this.totalBytes = totalBytes;
        this.downloadSpeed = downloadSpeed;
        this.downloadError = downloadError;
    }

    public String getDownloadError() {
        return downloadError;
    }

    public void setDownloadError(String downloadError) {
        this.downloadError = downloadError;
    }
}
