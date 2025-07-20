package com.khalil.pdfviewer.Model_Class;


public class RecentFile {
    private String name;
    private String uri;

    public RecentFile(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}

