package com.khalil.pdfviewer.Model_Class;

public class RecentFile {
    private String fileName, filePath;
    private long fileSize, openTime;

    public RecentFile(String fileName, String filePath, long fileSize, long lastOpenTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.openTime = openTime;
    }

    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public long getFileSize() { return fileSize; }
    public long getOpenTime() { return openTime; }

    public long getLastOpened() {
        return 0;
    }
}
