package com.khalil.pdfviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.khalil.pdfviewer.Model_Class.RecentFile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private RecyclerView recentRecyclerView;
    private List<RecentFile> recentFiles = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "RECENT_FILES";
    private static final String KEY_FILES = "files";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadRecentFiles();

        Button pickPdfBtn = findViewById(R.id.pickPdfBtn);
        pickPdfBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
        });

        recentRecyclerView = findViewById(R.id.recentRecyclerView);
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentRecyclerView.setAdapter(new RecentFilesAdapter(recentFiles, this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            addRecentFile(pdfUri);
            Intent intent = new Intent(this, PdfViewActivity.class);
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
        }
    }

    private void addRecentFile(Uri uri) {
        String name = uri.getLastPathSegment();
        recentFiles.add(0, new RecentFile(name, uri.toString()));
        saveRecentFiles();
    }

    private void saveRecentFiles() {
        JSONArray jsonArray = new JSONArray();
        for (RecentFile file : recentFiles) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", file.getName());
                obj.put("uri", file.getUri());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sharedPreferences.edit().putString(KEY_FILES, jsonArray.toString()).apply();
    }

    private void loadRecentFiles() {
        String data = sharedPreferences.getString(KEY_FILES, "");
        if (!data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    recentFiles.add(new RecentFile(obj.getString("name"), obj.getString("uri")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
