package com.khalil.pdfviewer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Model_Class.RecentFile;
import com.khalil.pdfviewer.R;

import java.util.List;

public class RecentFilesAdapter extends RecyclerView.Adapter<RecentFilesAdapter.ViewHolder> {

    private final Context context;
    private final List<RecentFile> fileList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Uri uri);
    }

    public RecentFilesAdapter(Context context, List<RecentFile> fileList, OnItemClickListener listener) {
        this.context = context;
        this.fileList = fileList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, size, time, path;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.fileNameText);
            size = view.findViewById(R.id.fileSizeText);
            time = view.findViewById(R.id.lastOpenText);
            path = view.findViewById(R.id.filePathText);
        }

        public void bind(Context ctx, RecentFile file, OnItemClickListener listener) {
            name.setText(file.getFileName());
            size.setText(Formatter.formatShortFileSize(ctx, file.getFileSize()));
            time.setText(DateFormat.format("dd MMM yyyy, hh:mm a", file.getOpenTime()));
            path.setText(Uri.parse(file.getFilePath()).getLastPathSegment());

            itemView.setOnClickListener(v -> listener.onItemClick(Uri.parse(file.getFilePath())));
        }
    }

    @Override
    public RecentFilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, fileList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
