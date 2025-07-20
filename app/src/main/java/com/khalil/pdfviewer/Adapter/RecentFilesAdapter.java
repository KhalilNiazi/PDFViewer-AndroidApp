package com.khalil.pdfviewer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Model_Class.RecentFile;
import com.khalil.pdfviewer.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentFilesAdapter extends RecyclerView.Adapter<RecentFilesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onFileClick(RecentFile file);
        void onFileDelete(RecentFile file, int position);
    }

    private final Context context;
    private final List<RecentFile> recentFiles;
    private final OnItemClickListener listener;

    public RecentFilesAdapter(Context context, List<RecentFile> recentFiles, OnItemClickListener listener) {
        this.context = context;
        this.recentFiles = recentFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentFile recentFile = recentFiles.get(position);
        File file = new File(recentFile.getFilePath());

        holder.fileNameTextView.setText(file.getName());

        // Format last opened time
        String formattedTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date(recentFile.getLastOpenedTime()));
        holder.fileTimeTextView.setText("Last opened: " + formattedTime);

        // Click to view
        holder.itemView.setOnClickListener(v -> listener.onFileClick(recentFile));

        // Delete file
        holder.deleteButton.setOnClickListener(v -> listener.onFileDelete(recentFile, position));

        // Share file
        holder.shareButton.setOnClickListener(v -> {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "Share PDF using"));
        });
    }

    @Override
    public int getItemCount() {
        return recentFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, fileTimeTextView;
        ImageButton deleteButton, shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.textFileName);
            fileTimeTextView = itemView.findViewById(R.id.textFileTime);
            deleteButton = itemView.findViewById(R.id.btnDeleteFile);
            shareButton = itemView.findViewById(R.id.btnShareFile);
        }
    }
}
