package com.yanzhenjie.andserver.entity;

public class FileModel {
    public static final String FILE = "1";

    public static final String DIRECTORY = "2";

    private String fileName;

    private String fileType;

    private String filePath;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String paramString) {
        this.fileName = paramString;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String paramString) {
        this.fileType = paramString;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String paramString) {
        this.filePath = paramString;
    }
}
