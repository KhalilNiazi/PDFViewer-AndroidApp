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

    private static final String PREF_NAME = "recent_files_pref";
    private static final String KEY_RECENT_FILES = "recent_files";

    // Save a recent file
    public static void saveRecentFile(Context context, RecentFile recentFile) {
        List<RecentFile> recentFiles = getRecentFiles(context);

        // Remove existing if already present (avoid duplicate)
        for (int i = 0; i < recentFiles.size(); i++) {
            if (recentFiles.get(i).getFilePath().equals(recentFile.getFilePath())) {
                recentFiles.remove(i);
                break;
            }
        }

        // Add new at the top
        recentFiles.add(0, recentFile);

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(recentFiles);
        editor.putString(KEY_RECENT_FILES, json);
        editor.apply();
    }

    // Get all recent files
    public static List<RecentFile> getRecentFiles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_RECENT_FILES, null);

        if (json != null) {
            Type type = new TypeToken<List<RecentFile>>() {}.getType();
            return new Gson().fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Delete a single recent file entry
    public static void deleteRecentFile(Context context, String filePath) {
        List<RecentFile> recentFiles = getRecentFiles(context);
        for (int i = 0; i < recentFiles.size(); i++) {
            if (recentFiles.get(i).getFilePath().equals(filePath)) {
                recentFiles.remove(i);
                break;
            }
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_RECENT_FILES, new Gson().toJson(recentFiles));
        editor.apply();
    }

    // Clear all recent files
    public static void clearRecentFiles(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_RECENT_FILES);
        editor.apply();
    }
}
