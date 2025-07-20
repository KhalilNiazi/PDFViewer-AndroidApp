package com.khalil.pdfviewer.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Model_Class.RecentFile;
import com.khalil.pdfviewer.PdfViewActivity;
import com.khalil.pdfviewer.R;

import java.util.List;

public class RecentFilesAdapter extends RecyclerView.Adapter<RecentFilesAdapter.ViewHolder> {

    private final List<RecentFile> recentFiles;
    private final Context context;

    public RecentFilesAdapter(List<RecentFile> recentFiles, Context context) {
        this.recentFiles = recentFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentFile file = recentFiles.get(position);
        holder.fileName.setText(file.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra("pdfUri", file.getUri());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recentFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.recentFileName);
        }
    }
}