package com.example.downloadmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class ServiceDownloadManager extends Service {

    private static final String TAG = "ServiceDownloadManager";
    private String url;
    private String path;
    private Boolean isDir;

    public ServiceDownloadManager() {
        Log.e(TAG, "ServiceDownloadManager: " );
    }

    private static final String NOTIFICATION_ID = "0";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.url = intent.getStringExtra("URL_KEY");
        this.path = intent.getStringExtra("PATH_KEY");
        this.isDir = intent.getBooleanExtra("DIRECTION_KEY", false);
        Log.e(TAG, "onStartCommand: " );
        initDownload();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BaseDownloadTask initDownload() {

        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener(){
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        Log.e(TAG, "pending: "+soFarBytes+" "+totalBytes );
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        Log.e(TAG, "progress: "+soFarBytes+" "+totalBytes );
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        super.blockComplete(task);
                        Log.e(TAG, "blockComplete: " );
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        Log.e(TAG, "completed: " );
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        Log.e(TAG, "paused: "+soFarBytes+" "+totalBytes );
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        Log.e(TAG, "error: "+e.getMessage() );
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        Log.e(TAG, "warn: " );
                    }
                });
    }

    private void showNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_ID);
        builder.setCustomContentView(remoteViews);

//        view.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        view.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        view.findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }


    public int start(BaseDownloadTask baseDownloadTask){
        return baseDownloadTask.start();
    }

    public void pause(int downloadID){
        FileDownloader.getImpl().pause(downloadID);
    }

    public void stop(){
        new File(path).delete();
    }

//    public void destroy(){
//        FileDownloader.getImpl().pause(downloadID);
//    }
}
