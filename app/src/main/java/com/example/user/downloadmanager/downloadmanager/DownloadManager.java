package com.example.user.downloadmanager.downloadmanager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
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
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    public static final String PROGRESS = "progress";
    public static final String COMPLETED = "completed";
    public static final String PENDING = "pending";
    public static final String ERROR = "error";
    public static final String PAUSE = "pause";
    private static final String NOT_CHECKED = "not checked yet";
    private static final String TAG = "DownloadManager";
    private static DownloadListener downloadListener;
    private static final FileDownloadNotificationHelper<NotificationItem> notificationHelper =
            new FileDownloadNotificationHelper<>();
    FileDownloadQueueSet fileDownloadQueueSet;
    private List<BaseDownloadTask> downloadTasks = new ArrayList<>();
    private static DownloadManager downloadManager;
    private static Context context;

    public DownloadManager() {
    }

    public static DownloadManager getDownloadManager() {
        if (downloadManager == null)
            downloadManager = new DownloadManager();
        return downloadManager;
    }

    public void firstInit(Application application) {

        context = application.getApplicationContext();
        FileDownloader
                .setupOnApplicationOnCreate(application)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }

    public void addDownloadTask(String url, String path) {

        fileDownloadQueueSet = new FileDownloadQueueSet(new NotificationListener());
        fileDownloadQueueSet.downloadSequentially(
                        FileDownloader
                                .getImpl()
                                .create(url)
                                .setPath(path))
                .start();

//        FileDownloader.getImpl().start(new NotificationListener(), true);
        FileDownloader.getImpl().create(url).setPath(path).asInQueueTask().enqueue();

    }

    /**
     * set a listener to get your download's status in time.
     */
    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    /**
     * Pause your download with ID.
     */
    public void pause(int downloadID) {
        if (downloadID != 0) {
            FileDownloader.getImpl().pause(downloadID);
        }
        Log.e(TAG, "pause: " + downloadTasks.size());
    }

    /**
     * Pause all download.
     */
    public void pauseAllDownloads() {
        FileDownloader.getImpl().pauseAll();
    }

    public void startForegroundService() {
        stopNotification();
        FileDownloader.getImpl().startForeground(10001, NotificationItem.getNotificationInstance());
    }

    private static void stopNotification() {
        FileDownloader.getImpl().stopForeground(true);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).
                cancelAll();
    }

//    public void stop() {
//        final File file = new File(path);
//        Log.e(TAG, "stop: " + file.getName());
//        if (file.exists()) {
//            Log.e(TAG, "stop: delete" + file.delete());
//        } else
//            Log.e(TAG, "stop: not exist");
//    }

    /**
     * Get your download specific ID and use it for pause or detect your download file's status.
     */
    public int getDownloadId(String url, String path) {
                Log.e(TAG, "getDownloadId: ");
        return FileDownloadUtils.generateId(url, path);
    }


    // Notification Listener for getting download file status
    public static class NotificationListener extends FileDownloadNotificationListener {

        NotificationListener() {
            super(notificationHelper);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Log.e(TAG, "completed: 1");
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
                                task.getSpeed(),
                                ""));
            stopNotification();
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Log.e(TAG, "error: 1");
            super.error(task, e);
            if (downloadListener != null)
                downloadListener.error(task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                ERROR,
                                (int) task.getLargeFileSoFarBytes(),
                                (int) task.getLargeFileTotalBytes(),
                                task.getSpeed(),
                                e.getMessage()));
            FileDownloader.getImpl().stopForeground(true);
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
            return true;
        }

        @Override
        protected boolean disableNotification(BaseDownloadTask task) {
            Log.e(TAG, "disableNotification: 1");
            return super.disableNotification(task);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.pause(
                        task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                PAUSE,
                                soFarBytes,
                                totalBytes,
                                task.getSpeed(),
                                ""
                        )
                );
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.e(TAG, "pending: 1");
            super.pending(task, soFarBytes, totalBytes);
            if (downloadListener != null)
                downloadListener.pending(
                        task.getId(),
                        new ProgressModel(
                                task.getId(),
                                task.getFilename(),
                                PENDING,
                                soFarBytes,
                                totalBytes,
                                task.getSpeed(),
                                ""));
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
                                task.getSpeed(),
                                ""));
        }

    }

    /**
     * Get your download status as String.
     */
    public String getDownloadStatus(String url, String path) {
        switch (FileDownloader.getImpl().getStatus(url, path)) {
            case FileDownloadStatus.completed:
                return COMPLETED;
            case FileDownloadStatus.pending:
                return PENDING;
            case FileDownloadStatus.error:
                return ERROR;
            case FileDownloadStatus.paused:
                return PAUSE;
            case FileDownloadStatus.progress:
                return PROGRESS;
            default:
                return NOT_CHECKED;
        }
    }

    public static class NotificationItem extends BaseNotificationItem {

        PendingIntent pendingIntent;
        private NotificationManager mNotificationManager;
        public static final String NOTIFICATION_CHANNEL_ID = "10001";
        static NotificationCompat.Builder builder;

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
                    Builder(FileDownloadHelper.getAppContext(), NOTIFICATION_CHANNEL_ID);

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

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
        }

        public static Notification getNotificationInstance() {
            return builder.build();
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
            mNotificationManager.notify(getId(), builder.build());
        }

    }

    public void startService() {
        notificationHelper.clear();
    }

}
