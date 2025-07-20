package com.khalil.pdfviewer.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khalil.pdfviewer.Model_Class.RecentFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecentFileManager {

    private static final String PREF_NAME = "pdf_viewer_prefs";
    private static final String KEY_RECENT_FILES = "recent_files";

    public static void saveRecentFile(Context context, RecentFile newFile) {
        List<RecentFile> recentFiles = getRecentFiles(context);

        // Remove any old entry with the same path
        recentFiles.removeIf(file -> file.getFilePath().equals(newFile.getFilePath()));

        // Add new one on top
        recentFiles.add(0, newFile);

        // Limit the list to 10 items
        if (recentFiles.size() > 10) {
            recentFiles = recentFiles.subList(0, 10);
        }

        // Save back to SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentFiles);
        editor.putString(KEY_RECENT_FILES, json);
        editor.apply();
    }

    public static List<RecentFile> getRecentFiles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_RECENT_FILES, null);
        if (json == null) return new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<List<RecentFile>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void deleteRecentFile(Context context, String filePath) {
        List<RecentFile> recentFiles = getRecentFiles(context);
        recentFiles.removeIf(file -> file.getFilePath().equals(filePath));
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_RECENT_FILES, new Gson().toJson(recentFiles)).apply();
    }
}
