package com.khalil.pdfviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.khalil.pdfviewer.Model_Class.RecentFile;
import com.khalil.pdfviewer.Database.RecentFileManager;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button pickPdfBtn;
    private RecyclerView recentRecyclerView;

    private final ActivityResultLauncher<String> pdfPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::onPdfPicked);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickPdfBtn = findViewById(R.id.pickPdfBtn);
        recentRecyclerView = findViewById(R.id.recentRecyclerView);


        pickPdfBtn.setOnClickListener(v -> pickPdf());

        setupRecentFiles();
    }

    private void pickPdf() {
        pdfPickerLauncher.launch("application/pdf");
    }

    private void onPdfPicked(Uri uri) {
        if (uri != null) {
            String path = FileUtils.getPath(this, uri);
            if (path == null) {
                Toast.makeText(this, "Unable to get file path", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(path);
            String name = file.getName();

            // Save to recent files
            RecentFileManager.saveRecentFile(this, new RecentFile(name, path, System.currentTimeMillis()));

            // Open PDF viewer
            Intent intent = new Intent(this, PdfViewActivity.class);
            intent.putExtra("pdfUri", uri.toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecentFiles() {
        List<RecentFile> recentFiles = RecentFileManager.getRecentFiles(this);

        if (recentFiles.isEmpty()) {
            Toast.makeText(this, "No recent PDFs found", Toast.LENGTH_SHORT).show();
            return;
        }

        RecentFilesAdapter adapter = new RecentFilesAdapter(this, recentFiles, new RecentFilesAdapter.OnItemClickListener() {
            @Override
            public void onFileClick(RecentFile file) {
                Intent intent = new Intent(MainActivity.this, PdfViewActivity.class);
                intent.putExtra("pdfUri", Uri.fromFile(new File(file.getFilePath())).toString());
                startActivity(intent);
            }

            @Override
            public void onFileDelete(RecentFile file, int position) {
                RecentFileManager.deleteRecentFile(MainActivity.this, file.getFilePath());
                setupRecentFiles(); // Refresh the list
            }
        });

        recentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recentRecyclerView.setAdapter(adapter);
        // Apply layout animation
      /*  Animation animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down).getAnimation();
        recentRecyclerView.setLayoutAnimation(animation);
        recentRecyclerView.scheduleLayoutAnimation();*/
    }
}
