package com.khalil.pdfviewer.Database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.text.DecimalFormat;

public class FileUtils {

    // Get the display name of the file from URI
    public static String getFileName(Context context, Uri uri) {
        String name = null;

        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }

        if (name == null) {
            name = uri.getLastPathSegment();
        }

        return name != null ? name : "Unknown.pdf";
    }

    // Get the file size in bytes
    public static long getFileSize(Context context, Uri uri) {
        long size = 0;

        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (!cursor.isNull(sizeIndex)) {
                        size = cursor.getLong(sizeIndex);
                    }
                }
            }
        }

        return size;
    }

    // Format the file size in readable form (e.g., 1.2 MB)
    public static String formatFileSize(long sizeInBytes) {
        if (sizeInBytes <= 0) return "0 B";

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(sizeInBytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    // Try to get a readable file path (for display only)
    public static String getFilePath(Context context, Uri uri) {
        // For SAF Uris, file paths are not directly accessible — return display info instead
        String name = getFileName(context, uri);
        return "Storage Access Framework > " + name;
    }
}
