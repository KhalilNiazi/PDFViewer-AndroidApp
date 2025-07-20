package com.khalil.pdfviewer.Model_Class;

public class RecentFile {
    private String filePath;
    private String fileName;
    private long lastOpenedTime;

    public RecentFile(String filePath, String fileName, long lastOpenedTime) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.lastOpenedTime = lastOpenedTime;
    }


    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLastOpenedTime() {
        return lastOpenedTime;
    }
}
