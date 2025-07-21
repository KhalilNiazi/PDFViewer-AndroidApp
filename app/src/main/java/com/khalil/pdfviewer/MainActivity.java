package com.khalil.pdfviewer;

import android.content.Intent;
import android.content.ContentResolver;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Adapter.RecentFilesAdapter;
import com.khalil.pdfviewer.Database.FileUtils;
import com.khalil.pdfviewer.Database.RecentFileManager;
import com.khalil.pdfviewer.Model_Class.RecentFile;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recentRecyclerView;
    private RecentFilesAdapter adapter;

    private final ActivityResultLauncher<String[]> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    openPdf(uri);
                } else {
                    Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recentRecyclerView = findViewById(R.id.recentRecyclerView);
        Button pickFileBtn = findViewById(R.id.pickPdf_Btn);
        Button clearBtn = findViewById(R.id.clearRecentBtn);

        setupRecyclerView();
        loadRecentFiles();

        pickFileBtn.setOnClickListener(view ->
                filePickerLauncher.launch(new String[]{"application/pdf"}));

        clearBtn.setOnClickListener(view -> {
            RecentFileManager.clearRecentFiles(this);
            loadRecentFiles();
        });
    }

    private void setupRecyclerView() {
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadRecentFiles() {
        List<RecentFile> recentFiles = RecentFileManager.getRecentFiles(this);
        adapter = new RecentFilesAdapter(this, recentFiles, this::openPdf);
        recentRecyclerView.setAdapter(adapter);
    }

    private void openPdf(Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "Invalid PDF URI", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        String name = FileUtils.getFileName(this, uri);
        long size = FileUtils.getFileSize(this, uri);
        String path = uri.toString();

        RecentFile file = new RecentFile(name, path, size);
        RecentFileManager.saveRecentFile(this, file);

        Intent intent = new Intent(this, PdfViewActivity.class);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
