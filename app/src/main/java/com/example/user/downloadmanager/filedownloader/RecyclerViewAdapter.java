package com.example.user.downloadmanager.filedownloader;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.downloadmanager.R;

import java.util.ArrayList;
import java.util.List;

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
        viewHolder.onBind(downloadList.get(i));
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

        private ImageView avatar;
        private ProgressBar progressBar;
        private Button start, pause, stop;
        private TextView text1;
        private TextView text2;
        private TextView text3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            progressBar = itemView.findViewById(R.id.progressBar1);
            text1 = itemView.findViewById(R.id.etaTextView1);
            text2 = itemView.findViewById(R.id.downloadSpeedTextView1);
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
            progressBar.setProgress(downloadModel.getProgressbarModle().getSoFarBytes());
            progressBar.setMax(downloadModel.getProgressbarModle().getTotalBytes());
//            if (downloadModel.getProgressbarModle().getStatus().equals("progress") ||
//                    downloadModel.getProgressbarModle().getStatus().equals("completed"))
//                progressBar.setIndeterminate(false);
//            else if (downloadModel.getProgressbarModle().getStatus().equals("pending"))
//                progressBar.setIndeterminate(true);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case (R.id.start):
                    onListItemClickListener.onStartButtonClick(
                            downloadList.get(getAdapterPosition()).getUrl(),
                            downloadList.get(getAdapterPosition()).getPath()
                    );
                    break;
                case R.id.pause:
                    onListItemClickListener.onPauseButtonClick();
                    break;
                case R.id.stop:
                    onListItemClickListener.onStopButtonClick(
                            downloadList.get(getAdapterPosition()).getPath()
                    );
                    break;
            }
        }

//        @Override
//        public void onProgress(int soFarBytes, int totalBytes) {
//            progressBar.setProgress(soFarBytes);
//            progressBar.setMax(totalBytes);
//        }
    }

    void setListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    interface OnListItemClickListener {
        void onStartButtonClick(String url, String path);

        void onPauseButtonClick();

        void onStopButtonClick(String path);
    }
}
