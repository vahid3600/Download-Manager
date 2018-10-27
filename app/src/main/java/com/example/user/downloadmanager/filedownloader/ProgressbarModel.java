package com.example.user.downloadmanager.filedownloader;

public class ProgressbarModel {

    private int soFarBytes;
    private int totalBytes;
    private int fileSize;
    private String status;

    public ProgressbarModel(int soFarBytes, int totalBytes, int fileSize, String status) {
        this.soFarBytes = soFarBytes;
        this.totalBytes = totalBytes;
        this.fileSize = fileSize;
        this.status = status;
    }

    public void setSoFarBytes(int soFarBytes) {
        this.soFarBytes = soFarBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSoFarBytes() {
        return soFarBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getStatus() {
        return status;
    }
}
