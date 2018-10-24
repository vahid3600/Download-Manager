package com.example.user.downloadmanager.filedownloader;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.downloadmanager.R;
import com.example.user.downloadmanager.downloadmanager.DownloadListener;
import com.example.user.downloadmanager.downloadmanager.DownloadManager;
import com.liulishuo.filedownloader.FileDownloadMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileDownloaderFragment extends Fragment implements RecyclerViewAdapter.OnListItemClickListener {

    private Button start, stop, pause;
    private static final String TAG = "FetchDownloadManagerFra";

    //    private TextView progressTextView1;
//    private TextView etaTextView1;
//    private TextView downloadSpeedTextView1;
//    private ProgressBar progressBar1;
    private DownloadManager downloadManager1;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<DownloadModel> downloadList;
    private int downloadId;
    //    public static int downloadSoFarBytes;
//    public static int downloadTotalBytes;
//    private static int downloadFileSize;
    private String downloadStatus;

    private DownloadModel downloadModel1;
    private DownloadModel downloadModel2;
    private DownloadModel downloadModel3;
    private DownloadModel downloadModel4;
    private DownloadModel downloadModel5;

//    private ProgressListener progressListener;

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
//        progressListener = ;
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addData(getDownloadList());


//        progressTextView1 = view.findViewById(R.id.progressTextView1);
//        etaTextView1 = view.findViewById(R.id.etaTextView1);
//        downloadSpeedTextView1 = view.findViewById(R.id.downloadSpeedTextView1);
//        progressBar1 = view.findViewById(R.id.progressBar1);

//        start.setOnClickListener(v ->
//                downloadId1 = downloadManager1.startDownload());
//
//        stop.setOnClickListener(v -> {
//            downloadManager1.stop();
//            downloadId1 = 0;
//        });
//
//        pause.setOnClickListener(v -> downloadManager1.pause(downloadId1));
    }

    @NonNull
    public static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/file_downloader";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url1 = "http://www.pdf995.com/samples/pdf.pdf";
        String file1 = getSaveDir() + "/downloads/" + Uri.parse(url1).getLastPathSegment();

        downloadManager1 = DownloadManager.getDownloadManager();
        downloadManager1.firstInit(Objects.requireNonNull(getActivity()).getApplication());
//        downloadManager1.downloadInit(getContext(), url1, file1, true);
        downloadManager1.setDownloadListener(new DownloadListener() {
            @Override
            public void pending(int soFarBytes, int totalBytes) {
//                progressBar1.setIndeterminate(true);
                downloadStatus = "pending";
                Log.e(TAG, "pending: 1");
            }

            @Override
            public void progress(int soFarBytes, int totalBytes) {
                downloadList.get(0).setProgressbarModle(new ProgressbarModle(soFarBytes, totalBytes, totalBytes, "progress"));
                recyclerViewAdapter.addData(downloadList);
                Log.e(TAG, "progress: 1" + soFarBytes + " " + totalBytes);
            }

            @Override
            public void completed(int fileSize) {
                downloadStatus = "completed";
//                downloadList.get(0).setProgressbarModle(new ProgressbarModle(soFarBytes, totalBytes, totalBytes, "progress"));
//                downloadFileSize = fileSize;
//                progressBar1.setIndeterminate(false);
//                progressBar1.setProgress(fileSize);
                Log.e(TAG, "completed: 1");
            }
        });
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
        if (downloadList == null) {
            downloadList = new ArrayList<>();
            downloadList.add(downloadModel1 = new DownloadModel(
                    getString(R.string.url1),
                    getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url1)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar6.png",
                    new ProgressbarModle(
                            0,
                            0,
                            0,
                            downloadStatus)
            ));
            downloadList.add(downloadModel2 = new DownloadModel(
                    getString(R.string.url2),
                    getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url2)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar2.png",
                    new ProgressbarModle(
                            0,
                            0,
                            0,
                            downloadStatus)
            ));
            downloadList.add(downloadModel3 = new DownloadModel(
                    getString(R.string.url3),
                    getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url3)).getLastPathSegment(),
                    "https://www.w3schools.com/howto/img_avatar2.png",
                    new ProgressbarModle(
                            0,
                            0,
                            0,
                            downloadStatus)
            ));
            downloadList.add(downloadModel4 = new DownloadModel(
                    getString(R.string.url4),
                    getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url4)).getLastPathSegment(),
                    "https://www.w3schools.com/howto/img_avatar.png",
                    new ProgressbarModle(
                            0,
                            0,
                            0,
                            downloadStatus)
            ));
            downloadList.add(downloadModel5 = new DownloadModel(
                    getString(R.string.url5),
                    getSaveDir() + "/downloads/" + Uri.parse(getString(R.string.url5)).getLastPathSegment(),
                    "https://www.w3schools.com/w3images/avatar5.png",
                    new ProgressbarModle(
                            0,
                            0,
                            0,
                            downloadStatus)
            ));
        }
        return downloadList;
    }

    @Override
    public void onStartButtonClick(String url, String path) {
        downloadManager1.addTaskDownload(url, path);
        downloadManager1.startDownloadList();
    }

    @Override
    public void onPauseButtonClick() {
        downloadManager1.pauseDownloadList();
    }

    @Override
    public void onStopButtonClick(String path) {
        new File(path).delete();
    }
}
