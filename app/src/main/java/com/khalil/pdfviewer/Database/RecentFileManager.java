package com.khalil.pdfviewer.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.khalil.pdfviewer.Model_Class.RecentFile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RecentFileManager {
    private static final String PREF_NAME = "recent_files";
    private static final String KEY_URIS = "recent_uris";

    public static void addRecentFile(Context context, Uri uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> uriSet = prefs.getStringSet(KEY_URIS, new LinkedHashSet<>());
        if (uriSet == null) uriSet = new LinkedHashSet<>();

        uriSet.remove(uri.toString());
        uriSet.add(uri.toString());

        editor.putStringSet(KEY_URIS, uriSet);
        editor.apply();
    }

    public static List<RecentFile> getRecentFiles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> uriSet = prefs.getStringSet(KEY_URIS, new LinkedHashSet<>());
        if (uriSet == null) uriSet = new LinkedHashSet<>();

        List<RecentFile> recentFiles = new ArrayList<>();
        for (String uriStr : uriSet) {
            Uri uri = Uri.parse(uriStr);
            String name = getFileName(context, uri);
            long size = getFileSize(context, uri);
            long lastOpenTime = System.currentTimeMillis();
            recentFiles.add(new RecentFile(name, uriStr, size, lastOpenTime));
        }

        return recentFiles;
    }

    public static void removeRecentFile(Context context, String uriStr) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> uriSet = new LinkedHashSet<>(prefs.getStringSet(KEY_URIS, new LinkedHashSet<>()));
        if (uriSet.remove(uriStr)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(KEY_URIS, uriSet);
            editor.apply();
        }
    }

    public static void clearAllRecentFiles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_URIS);
        editor.apply();
    }

    private static String getFileName(Context context, Uri uri) {
        String name = "Unknown";
        try (android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    name = cursor.getString(nameIndex);
                }
            }
        } catch (Exception ignored) {
        }
        return name;
    }

    private static long getFileSize(Context context, Uri uri) {
        long size = 0;
        try (android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex >= 0) {
                    size = cursor.getLong(sizeIndex);
                }
            }
        } catch (Exception ignored) {
        }
        return size;
    }
}
