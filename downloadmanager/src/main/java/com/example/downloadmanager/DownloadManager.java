package com.example.downloadmanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import static android.content.Context.BIND_AUTO_CREATE;

public class DownloadManager {

    private static final String TAG = "DownloadManager";
    private Intent serviceIntent;

    public DownloadManager() {
        Log.e(TAG, "DownloadManager: " );
    }

    public void downloadInit(Context context, String url, String path, boolean isDir){
        Log.e(TAG, "downloadInit: ");
        serviceIntent = new Intent(context, ServiceDownloadManager.class);
        serviceIntent.putExtra("URL_KEY", url);
        serviceIntent.putExtra("PATH_KEY", path);
        serviceIntent.putExtra("DIRECTION_KEY", isDir);
        context.bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        context.startService(serviceIntent);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
