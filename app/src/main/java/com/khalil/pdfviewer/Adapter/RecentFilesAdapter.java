package com.khalil.pdfviewer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Model_Class.RecentFile;
import com.khalil.pdfviewer.R;

import java.io.File;
import java.util.List;
public class RecentFilesAdapter extends RecyclerView.Adapter<RecentFilesAdapter.ViewHolder> {

    private final Context context;
    private final List<RecentFile> fileList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Uri uri);
        void onFileDeleted(); // Trigger UI refresh if needed
    }

    public RecentFilesAdapter(Context context, List<RecentFile> fileList, OnItemClickListener listener) {
        this.context = context;
        this.fileList = fileList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFileName, textFileInfo, textFilePath;
        ImageButton shareButton, deleteButton;

        public ViewHolder(View view) {
            super(view);
            textFileName = view.findViewById(R.id.textFileName);
            textFileInfo = view.findViewById(R.id.textFileInfo);
            textFilePath = view.findViewById(R.id.textFilePath);
            shareButton = view.findViewById(R.id.shareButton);
        }

        public void bind(Context ctx, RecentFile file, OnItemClickListener listener, List<RecentFile> fileList, RecentFilesAdapter adapter) {
            Uri fileUri = Uri.parse(file.getFilePath());

            textFileName.setText(file.getFileName());

            String fileSizeStr = Formatter.formatShortFileSize(ctx, file.getFileSize());
            String lastOpened = android.text.format.DateFormat.format("hh:mm a", file.getOpenTime()).toString();

            textFileInfo.setText("Size: " + fileSizeStr);
            textFilePath.setText(fileUri.getPath());

            itemView.setOnClickListener(v -> listener.onItemClick(fileUri));

            shareButton.setOnClickListener(v -> {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    ctx.startActivity(Intent.createChooser(shareIntent, "Share PDF via"));
                } catch (Exception e) {
                    Toast.makeText(ctx, "Sharing failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, fileList.get(position), listener, fileList, this);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
