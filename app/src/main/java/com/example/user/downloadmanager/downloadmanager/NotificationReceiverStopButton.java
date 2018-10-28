package com.example.user.downloadmanager.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiverStopButton extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        DownloadManager.getDownloadManager().stop();
        Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
    }
}
