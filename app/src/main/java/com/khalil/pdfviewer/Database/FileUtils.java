package com.khalil.pdfviewer.Database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    // Get absolute file path from URI by copying to app cache
    public static String getPath(Context context, Uri uri) {
        try {
            String fileName = getFileName(context, uri);
            File tempFile = new File(context.getCacheDir(), fileName);

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get filename from Uri
    public static String getFileName(Context context, Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver()
                    .query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    result = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1 && result != null) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }
}
