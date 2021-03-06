package com.example.user.downloadmanager.filedownloader;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.downloadmanager.R;
import com.example.user.downloadmanager.downloadmanager.DownloadManager;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.downloadmanager.downloadmanager.DownloadManager.COMPLETED;
import static com.example.user.downloadmanager.downloadmanager.DownloadManager.ERROR;
import static com.example.user.downloadmanager.downloadmanager.DownloadManager.PAUSE;
import static com.example.user.downloadmanager.downloadmanager.DownloadManager.PENDING;
import static com.example.user.downloadmanager.downloadmanager.DownloadManager.PROGRESS;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<DownloadModel> downloadList = new ArrayList<>();
    private OnListItemClickListener onListItemClickListener;
//    private ViewHolder viewHolder;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DownloadModel downloadModel = downloadList.get(i);
        viewHolder.onBind(downloadModel);
    }

    @Override
    public int getItemCount() {
        return downloadList.size();
    }

    public void addData(List<DownloadModel> downloadList) {
        this.downloadList = downloadList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = ViewHolder.class.getSimpleName();
        private ImageView avatar;
        private ProgressBar progressBar;
        private Button start, pause, stop;
        private TextView text1;
        private TextView text2;
        private TextView text3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            progressBar = itemView.findViewById(R.id.progressBar);
            text1 = itemView.findViewById(R.id.etaTextView);
            text2 = itemView.findViewById(R.id.downloadSpeedTextView);
            text3 = itemView.findViewById(R.id.progressTextView1);
            start = itemView.findViewById(R.id.start);
            stop = itemView.findViewById(R.id.stop);
            pause = itemView.findViewById(R.id.pause);

            start.setOnClickListener(this);
            pause.setOnClickListener(this);
            stop.setOnClickListener(this);
        }

        public void onBind(DownloadModel downloadModel) {
            Glide.with(itemView.getContext()).load(downloadModel.getAvatar()).into(avatar);
            Log.e(TAG, "onBind: "+ getAdapterPosition() + " " +
                    downloadModel.getProgressModel().getId() + " " +
                    downloadModel.getProgressModel().getName() + " " +
                    downloadModel.getProgressModel().getDownloadSpeed() + " " +
                    downloadModel.getProgressModel().getSoFarBytes() + " " +
                    downloadModel.getProgressModel().getTotalBytes() + " " +
                    downloadModel.getProgressModel().getStatus() + " ");
            switch (downloadModel.getProgressModel().getStatus()) {
                case PENDING:
                    progressBar.setIndeterminate(true);
                    text1.setText(downloadModel.getProgressModel().getName());
                    text2.setText("" + downloadModel.getProgressModel().getDownloadSpeed());
                    text3.setText(downloadModel.getProgressModel().getStatus());
                    break;
                case PAUSE:
                    progressBar.setIndeterminate(true);
                    text1.setText(downloadModel.getProgressModel().getName());
                    text2.setText("" + downloadModel.getProgressModel().getDownloadSpeed());
                    text3.setText(downloadModel.getProgressModel().getStatus());
                    break;
                case ERROR:
                    progressBar.setIndeterminate(true);
                case COMPLETED:
                    progressBar.setIndeterminate(false);
                    progressBar.setMax(1);
                    progressBar.setProgress(1);
                    text1.setText(downloadModel.getProgressModel().getName());
                    text2.setText("" + downloadModel.getProgressModel().getDownloadSpeed());
                    text3.setText(downloadModel.getProgressModel().getStatus());
                    break;
                case PROGRESS:
                    progressBar.setIndeterminate(false);
                    progressBar.setMax(downloadModel.getProgressModel().getTotalBytes());
                    progressBar.setProgress(downloadModel.getProgressModel().getSoFarBytes());
                    text1.setText(downloadModel.getProgressModel().getName());
                    text2.setText("" + downloadModel.getProgressModel().getDownloadSpeed());
                    text3.setText(downloadModel.getProgressModel().getStatus());
                    break;
                case "":
                    progressBar.setIndeterminate(false);
                    progressBar.setMax(0);
                    progressBar.setProgress(0);
                    text1.setText("");
                    text2.setText("");
                    text3.setText("");
                    break;
            }
//            text1.setText(downloadModel.getProgressModel().getName());
//            text2.setText("" + downloadModel.getProgressModel().getDownloadSpeed());
//            text3.setText(downloadModel.getProgressModel().getStatus());
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "getDownloadId: "+FileDownloadUtils.getStack()+" " );
            switch (v.getId()) {
                case (R.id.start):
                    onListItemClickListener.onStartButtonClick(
                            downloadList.get(getAdapterPosition()).getUrl(),
                            downloadList.get(getAdapterPosition()).getPath()
                    );
                    break;
                case R.id.pause:
                    onListItemClickListener.onPauseButtonClick(
                            downloadList.get(getAdapterPosition()).getUrl(),
                            downloadList.get(getAdapterPosition()).getPath()
                    );
                    break;
                case R.id.stop:
                    onListItemClickListener.onStopButtonClick(
                            downloadList.get(getAdapterPosition()).getPath()
                    );
                    break;
            }
        }
    }

    void setListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    interface OnListItemClickListener {
        void onStartButtonClick(String url, String path);

        void onPauseButtonClick(String url, String path);

        void onStopButtonClick(String path);
    }
}
