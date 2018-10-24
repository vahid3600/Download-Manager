package com.example.user.downloadmanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.user.downloadmanager.downloadmanager.DownloadManager
import com.example.user.downloadmanager.filedownloader.FileDownloaderFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout, FileDownloaderFragment.newInstance())
                .commit()
    }
}
