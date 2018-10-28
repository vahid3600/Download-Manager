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
import com.example.user.downloadmanager.filedownloader.DownloadModel;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    public static final String PROGRESS = "progress";
    public static final String COMPLETED = "completed";
    public static final String PENDING = "pending";
    private static final String TAG = "DownloadManager";
    private static DownloadListener downloadListener;
    private static final FileDownloadNotificationHelper<NotificationItem> notificationHelper =
            new FileDownloadNotificationHelper<>();
    static final List<BaseDownloadTask> taskList = new ArrayList<>();
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

    public void addDownloadTask(String url, String path) {
        taskList.add(FileDownloader
                .getImpl()
                .create(url)
                .setPath(path));
        Log.e(TAG, "addDownloadTask: " + taskList.size());
    }

    public void startDownloadList() {
//        if (taskList.size() <= 1)
        Log.e(TAG, "startDownloadList: "+taskList.size() );
            fileDownloadQueueSet
                    .downloadSequentially(taskList)
                    .setCallbackProgressTimes(1)
                    .start();

    }

    public void pauseDownloadList() {
        FileDownloader.getImpl().pauseAll();
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void pause(int downloadID) {
        if (downloadID != 0) {
            FileDownloader.getImpl().pause(downloadID);
        }
    }

//    public void stop() {
//        final File file = new File(path);
//        Log.e(TAG, "stop: " + file.getName());
//        if (file.exists()) {
//            Log.e(TAG, "stop: delete" + file.delete());
//        } else
//            Log.e(TAG, "stop: not exist");
//    }

    public int getDownloadId(String url, String path) {
        return FileDownloadUtils.generateId(url, path);
    }

    private class NotificationListener extends FileDownloadNotificationListener {

        NotificationListener() {
            super(notificationHelper);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Log.e(TAG, "completed: 1" );
            super.completed(task);
            if (downloadListener != null)
                downloadListener.completed(
                        task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                COMPLETED,
                                (int) task.getLargeFileSoFarBytes(),
                                (int) task.getLargeFileTotalBytes(),
                                task.getSpeed()));
            FileDownloader.getImpl().stopForeground(true);
            removeDownloadFromDownloadList(task.getUrl(), task.getPath());
            startDownloadList();
        }

        private void removeDownloadFromDownloadList(String url, String path) {
            Log.d(TAG, "removeDownloadFromDownloadList: " + taskList.size());
            if (taskList.size() != 0)
                for (BaseDownloadTask baseDownloadTask : taskList) {
                    if (baseDownloadTask.getUrl() == url &&
                            baseDownloadTask.getPath() == path) {
                        taskList.remove(baseDownloadTask);
                    }
                }
            Log.e(TAG, "removeDownloadFromDownloadList: " + taskList.size());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Log.e(TAG, "error: 1" );
            super.error(task, e);
            if (downloadListener != null)
                downloadListener.error(task.getId(), e.getMessage());
            FileDownloader.getImpl().stopForeground(true);
            removeDownloadFromDownloadList(task.getUrl(), task.getPath());
            startDownloadList();
        }

        @Override
        protected BaseNotificationItem create(BaseDownloadTask task) {
            Log.e(TAG, "create: 1");
            return new NotificationItem(task.getId(), task.getFilename(), task.getFilename());

        }

        @Override
        public void addNotificationItem(BaseDownloadTask task) {
            super.addNotificationItem(task);
            Log.e(TAG, "addNotificationItem: 1");
        }

        @Override
        public void destroyNotification(BaseDownloadTask task) {
            super.destroyNotification(task);
            Log.e(TAG, "destroyNotification: 1");
        }

        @Override
        protected boolean interceptCancel(BaseDownloadTask task,
                                          BaseNotificationItem n) {
            Log.e(TAG, "interceptCancel: 1");
            // in this demo, I don't want to cancel the builder, just show for the test
            // so return true
            return true;
        }

        @Override
        protected boolean disableNotification(BaseDownloadTask task) {
            Log.e(TAG, "disableNotification: 1");
            return super.disableNotification(task);
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.e(TAG, "pending: 1" );
            super.pending(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.pending(task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                PENDING,
                                soFarBytes,
                                totalBytes,
                                task.getSpeed()));
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.e(TAG, "progress: 1");
            super.progress(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.progress(
                        task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                PROGRESS,
                                soFarBytes,
                                totalBytes,
                                task.getSpeed()));
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
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setContentTitle(getTitle())
                    .setContentText(desc)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_download)
//                    .addAction(0, "Stop", stopAction)
                    .addAction(0, "Pause", pauseAction)
                    .addAction(0, "Start", playAction);
        }

        @Override
        public void show(boolean statusChanged, int status, boolean isShowProgress) {

            String desc = "";
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
