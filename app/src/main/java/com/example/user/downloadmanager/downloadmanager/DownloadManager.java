package com.example.user.downloadmanager.downloadmanager;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.user.downloadmanager.MainActivity;
import com.example.user.downloadmanager.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    private static final String TAG = "DownloadManager";
    private static DownloadListener downloadListener;
    private static final FileDownloadNotificationHelper<NotificationItem> notificationHelper =
            new FileDownloadNotificationHelper<>();
    final List<BaseDownloadTask> taskList = new ArrayList<>();
    FileDownloadQueueSet fileDownloadQueueSet;
    private static DownloadManager downloadManager;
    private static Context context;
    private String path;
    private String url;
    private Boolean isDir;

    public DownloadManager() {
    }

    public static DownloadManager getDownloadManager() {
        if (downloadManager == null)
            downloadManager = new DownloadManager();
        return downloadManager;
    }

    public void firstInit(Application application) {

        context = application.getApplicationContext();
        NotificationListener notificationListener = new NotificationListener();

        FileDownloader.setupOnApplicationOnCreate(application)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();

        fileDownloadQueueSet = new FileDownloadQueueSet(notificationListener);
    }

    public void downloadInit(Context context, String url, String path, boolean isDir) {
        this.context = context;
        this.path = path;
        this.url = url;
        this.isDir = isDir;
    }

    public void addTaskDownload(String url, String path) {
        taskList.add(FileDownloader
                .getImpl()
                .create(url)
                .setPath(path));
        fileDownloadQueueSet.downloadSequentially(taskList);
    }

    public void startDownloadList() {
        fileDownloadQueueSet
                .setCallbackProgressTimes(1)
                .start();
    }

    public void pauseDownloadList() {
        FileDownloader.getImpl().pauseAll();
    }

    public Integer startDownload() {
        NotificationListener notificationListener = new NotificationListener();

        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setListener(notificationListener)
                .start();
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void pause(int downloadID) {
        if (downloadID != 0) {
            FileDownloader.getImpl().pause(downloadID);
        }
    }

    public void stop() {
        final File file = new File(path);
        Log.e(TAG, "stop: " + file.getName());
        if (file.exists()) {
            Log.e(TAG, "stop: delete" + file.delete());
        } else
            Log.e(TAG, "stop: not exist");
    }

    private static class NotificationListener extends FileDownloadNotificationListener {

        public NotificationListener() {
            super(notificationHelper);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            if (downloadListener != null)
                downloadListener.completed(task.getSmallFileTotalBytes());
        }

        @Override
        protected BaseNotificationItem create(BaseDownloadTask task) {
            return new NotificationItem(task.getId(), task.getFilename(), task.getFilename());

        }

        @Override
        public void addNotificationItem(BaseDownloadTask task) {
            super.addNotificationItem(task);
        }

        @Override
        public void destroyNotification(BaseDownloadTask task) {
            super.destroyNotification(task);
        }

        @Override
        protected boolean interceptCancel(BaseDownloadTask task,
                                          BaseNotificationItem n) {
            // in this demo, I don't want to cancel the builder, just show for the test
            // so return true
            return true;
        }

        @Override
        protected boolean disableNotification(BaseDownloadTask task) {
            return super.disableNotification(task);
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.pending(soFarBytes, totalBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.progress(soFarBytes, totalBytes);
        }

    }

    public static class NotificationItem extends BaseNotificationItem {

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        private NotificationItem(int id, String title, String desc) {
            super(id, title, desc);
            Intent[] intents = new Intent[2];
            intents[0] = Intent.makeMainActivity(new ComponentName(context,
                    MainActivity.class));
            intents[1] = new Intent(context, DownloadManager.class);

            Intent broadcastIntentPauseButton = new Intent(context, NotificationReceiverPauseButton.class);
            broadcastIntentPauseButton.putExtra("download_id", id);
            PendingIntent pauseAction = PendingIntent.getBroadcast(
                    context,
                    0,
                    broadcastIntentPauseButton,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent broadcastIntentStartButton = new Intent(context, NotificationReceiverStartButton.class);
            broadcastIntentPauseButton.putExtra("download_id", id);
            PendingIntent playAction = PendingIntent.getBroadcast(
                    context,
                    1,
                    broadcastIntentStartButton,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent broadcastIntentStopButton = new Intent(context, NotificationReceiverStopButton.class);
            PendingIntent stopAction = PendingIntent.getBroadcast(
                    context,
                    2,
                    broadcastIntentStopButton,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            this.pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder = new NotificationCompat.
                    Builder(FileDownloadHelper.getAppContext(), "");

            builder.setDefaults(Notification.DEFAULT_LIGHTS)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setContentTitle(getTitle())
                    .setContentText(desc)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_download)
                    .addAction(0, "Stop", stopAction)
                    .addAction(0, "Pause", pauseAction)
                    .addAction(0, "Start", playAction);
        }

        @Override
        public void show(boolean statusChanged, int status, boolean isShowProgress) {

            String desc = getDesc();
            switch (status) {
                case FileDownloadStatus.pending:
                    desc += " pending";
                    break;
                case FileDownloadStatus.started:
                    desc += " started";
                    break;
                case FileDownloadStatus.progress:
                    desc += " progress";
                    break;
                case FileDownloadStatus.retry:
                    desc += " retry";
                    break;
                case FileDownloadStatus.error:
                    desc += " error";
                    break;
                case FileDownloadStatus.paused:
                    desc += " paused";
                    break;
                case FileDownloadStatus.completed:
                    desc += " completed";
                    break;
                case FileDownloadStatus.warn:
                    desc += " warn";
                    break;
            }

            builder.setContentTitle(getTitle())
                    .setContentText(desc);


            if (statusChanged) {
                builder.setTicker(desc);
            }

            builder.setProgress(getTotal(), getSofar(), !isShowProgress);
            getManager().notify(getId(), builder.build());
        }

    }

}
