package com.example.user.downloadmanager.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiverPauseButton extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager.getDownloadManager().pause(intent.getIntExtra("download_id", 0));
        Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show();
    }
}
