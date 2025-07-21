package com.khalil.pdfviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Adapter.RecentFilesAdapter;
import com.khalil.pdfviewer.Database.RecentFileManager;
import com.khalil.pdfviewer.Model_Class.RecentFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView clearRecentBtn;
    private Button openFileButton, sortBtn;
    private EditText searchInput;
    private RecentFilesAdapter adapter;
    private List<RecentFile> recentFiles = new ArrayList<>();
    private List<RecentFile> filteredList = new ArrayList<>();

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        RecentFileManager.addRecentFile(this, uri);
                        openPdf(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recentRecyclerView);
        clearRecentBtn = findViewById(R.id.clearRecentBtn);
        openFileButton = findViewById(R.id.pickPdf_Btn);
        sortBtn = findViewById(R.id.sortBtn);
        searchInput = findViewById(R.id.searchInput);

        openFileButton.setOnClickListener(v -> pickPdfFile());

        clearRecentBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear Recent Files")
                    .setMessage("Are you sure you want to clear all recent files?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        RecentFileManager.clearAllRecentFiles(this);
                        loadRecentFiles();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        sortBtn.setOnClickListener(this::showSortMenu);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        loadRecentFiles();
    }

    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        }
        filePickerLauncher.launch(intent);
    }

    private void openPdf(Uri uri) {
        Intent intent = new Intent(this, PdfViewActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private void loadRecentFiles() {
        recentFiles = RecentFileManager.getRecentFiles(this);
        filteredList = new ArrayList<>(recentFiles);

        if (recentFiles.isEmpty()) {
            clearRecentBtn.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            clearRecentBtn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new RecentFilesAdapter(this, filteredList, new RecentFilesAdapter.OnItemClickListener() {
                @Override public void onItemClick(Uri uri) {
                    openPdf(uri);
                }

                @Override public void onFileDeleted() {
                    loadRecentFiles();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    private void filterList(String query) {
        filteredList.clear();
        for (RecentFile file : recentFiles) {
            if (file.getFileName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(file);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.filter_name_asc) {
                Collections.sort(filteredList, Comparator.comparing(RecentFile::getFileName));
            } else if (itemId == R.id.filter_name_desc) {
                Collections.sort(filteredList, (a, b) -> b.getFileName().compareTo(a.getFileName()));
            } else if (itemId == R.id.filter_newest) {
                Collections.sort(filteredList, (a, b) -> Long.compare(b.getLastOpened(), a.getLastOpened()));
            } else if (itemId == R.id.filter_oldest) {
                Collections.sort(filteredList, Comparator.comparingLong(RecentFile::getLastOpened));
            }
            adapter.notifyDataSetChanged();
            return true;
        });

        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentFiles();
    }
}
