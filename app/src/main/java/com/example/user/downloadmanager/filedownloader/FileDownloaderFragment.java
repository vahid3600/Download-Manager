package com.example.user.downloadmanager.filedownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.downloadmanager.R;
import com.example.user.downloadmanager.downloadmanager.DownloadListener;
import com.example.user.downloadmanager.downloadmanager.DownloadManager;
import com.example.user.downloadmanager.downloadmanager.ProgressModel;
import com.liulishuo.filedownloader.FileDownloadMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileDownloaderFragment extends Fragment implements RecyclerViewAdapter.OnListItemClickListener {

    private static final String TAG = "FetchDownloadManagerFra";
    private static final int STORAGE_PERMISSION_CODE = 1;
    private DownloadManager downloadManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<DownloadModel> downloadList;

    public FileDownloaderFragment() {
        // Required empty public constructor
    }

    public static FileDownloaderFragment newInstance() {
        FileDownloaderFragment fragment = new FileDownloaderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.setListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addData(getDownloadList());
    }

    @NonNull
    public static String getSaveDir() {
        return Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/file_downloader";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        downloadManager = DownloadManager.getDownloadManager();
        downloadManager.firstInit(Objects.requireNonNull(getActivity()).getApplication());
        downloadManager.setDownloadListener(new DownloadListener() {
            @Override
            public void error(int id, String message) {
                showToastMessage(message);
            }

            @Override
            public void pending(int id, ProgressModel progressModel) {
                updateProgressBar(id, progressModel);
                Log.e(TAG, "pending: ");
            }

            @Override
            public void progress(int id, ProgressModel progressModel) {
                updateProgressBar(id, progressModel);
                Log.e(TAG, "progress: ");
            }

            @Override
            public void completed(int id, ProgressModel progressModel) {
                updateProgressBar(id, progressModel);
                Log.e(TAG, "completed: ");
            }
        });
    }

    private void updateProgressBar(int id, ProgressModel progressModel) {
        for (DownloadModel downloadModel : downloadList) {
            if (downloadModel.getProgressModel().getId() == id) {
                downloadModel.setProgressModel(progressModel);
            }
        }
        recyclerViewAdapter.addData(downloadList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fetch_download_manager, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        FileDownloadMonitor.releaseGlobalMonitor();
    }

    public List<DownloadModel> getDownloadList() {
        String url;
        String path;
        if (downloadList == null) {
            downloadList = new ArrayList<>();
            downloadList.add(new DownloadModel(
                    url = getString(R.string.url1),
                    path = getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url1)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar6.png",
                    new ProgressModel(downloadManager.getDownloadId(url, path), "", "", 0, 0, 0)));
            downloadList.add(new DownloadModel(
                    url = getString(R.string.url2),
                    path = getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url2)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar2.png",
                    new ProgressModel(downloadManager.getDownloadId(url, path), "", "", 0, 0, 0)));
            downloadList.add(new DownloadModel(
                    url = getString(R.string.url3),
                    path = getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url3)).getLastPathSegment(),
                    "https://www.w3schools.com/howto/img_avatar2.png",
                    new ProgressModel(downloadManager.getDownloadId(url, path), "", "", 0, 0, 0)));
            downloadManager.getDownloadId(url, path);
            downloadList.add(new DownloadModel(
                    url = getString(R.string.url4),
                    path = getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url4)).getLastPathSegment(),
                    "https://www.w3schools.com/howto/img_avatar.png",
                    new ProgressModel(downloadManager.getDownloadId(url, path), "", "", 0, 0, 0)));
            downloadManager.getDownloadId(url, path);
            downloadList.add(new DownloadModel(
                    url = getString(R.string.url5),
                    path = getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url5)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar5.png",
                    new ProgressModel(downloadManager.getDownloadId(url, path), "", "", 0, 0, 0)));
            downloadManager.getDownloadId(url, path);
        }
        return downloadList;
    }

    private void askExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToastMessage(getString(R.string.permission_granted));
                }
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean readExternalStorage() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStartButtonClick(String url, String path) {
        if (readExternalStorage()) {
            downloadManager.addDownloadTask(url, path);
            downloadManager.startDownloadList();
        } else
            askExternalStoragePermission();
    }

    @Override
    public void onPauseButtonClick() {
        downloadManager.pauseDownloadList();
    }

    @Override
    public void onStopButtonClick(String path) {
        new File(path).delete();
    }
}
