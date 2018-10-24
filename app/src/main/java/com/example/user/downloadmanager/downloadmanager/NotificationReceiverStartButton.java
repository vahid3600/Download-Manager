package com.example.user.downloadmanager.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiverStartButton extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager.getDownloadManager().startDownloadList();
        Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
    }
}
