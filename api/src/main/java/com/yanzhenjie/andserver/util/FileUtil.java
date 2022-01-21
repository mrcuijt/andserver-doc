package com.yanzhenjie.andserver.util;

import com.yanzhenjie.andserver.entity.FileModel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipFile;

public class FileUtil {
    private static FileUtil instance;

    public static List<File> files(String paramString) {
        File file = new File(paramString);
        if (!file.exists())
            return new ArrayList<>();
        ArrayList<File> arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(file.listFiles()));
        return arrayList;
    }

    public static List<FileModel> generateFileModels(List<File> paramList) {
        ArrayList<FileModel> arrayList = new ArrayList();
        if (paramList == null || paramList.size() == 0)
            return arrayList;
        for (File file : paramList)
            arrayList.add(generateFileModel(file));
        return arrayList;
    }

    public static FileModel generateFileModel(File paramFile) {
        FileModel fileModel = new FileModel();
        fileModel.setFileName(paramFile.getName());
        fileModel.setFilePath(paramFile.getAbsolutePath());
        if (paramFile.isFile()) {
            fileModel.setFileType("1");
        } else {
            fileModel.setFileType("2");
        }
        return fileModel;
    }


    private static FileUtil getInstance() {
        if (instance == null)
            instance = new FileUtil();
        return instance;
    }
}
