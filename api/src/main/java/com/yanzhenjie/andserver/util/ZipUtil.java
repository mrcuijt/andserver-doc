package com.yanzhenjie.andserver.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static Iterator<ZipEntry> iterator(ZipFile paramZipFile) {
        Iterator<ZipEntry> iterator = null;
        try {
            iterator = (Iterator)paramZipFile.entries();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return iterator;
    }

    public static Enumeration<ZipEntry> entries(ZipFile paramZipFile) {
        Enumeration<? extends ZipEntry> enumeration = null;
        try {
            enumeration = paramZipFile.entries();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return (Enumeration)enumeration;
    }

    public static List<String> entryList2(String paramString){
        ZipFile zipFile = null;
        ArrayList<String> arrayList = new ArrayList();
        try {
            zipFile = getZipFile(paramString);
            Enumeration<ZipEntry> entries = entries(zipFile);
            while (entries.hasMoreElements()){
                ZipEntry zipEntry = entries.nextElement();
                arrayList.add(zipEntry.getName());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (zipFile != null)
                    zipFile.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return (arrayList.size() == 0) ? Collections.<String>emptyList() : arrayList;
    }

    public static List<String> entryList(String paramString) {
        ZipFile zipFile = null;
        ArrayList<String> arrayList = new ArrayList();
        try {
            zipFile = getZipFile(paramString);
            Iterator<ZipEntry> iterator = iterator(zipFile);
            if (iterator == null)
                return (List)Collections.emptyList();
            while (iterator.hasNext()) {
                ZipEntry zipEntry = iterator.next();
                arrayList.add(zipEntry.getName());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (zipFile != null)
                    zipFile.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return (arrayList.size() == 0) ? Collections.<String>emptyList() : arrayList;
    }

    public static InputStream getResource(ZipFile paramZipFile, String paramString) {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ZipEntry zipEntry = paramZipFile.getEntry(paramString);
            inputStream = paramZipFile.getInputStream(zipEntry);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return inputStream;
    }

    public static InputStream getResource(ZipFile paramZipFile, ZipEntry zipEntry) {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            inputStream = paramZipFile.getInputStream(zipEntry);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return inputStream;
    }

    public static boolean verify(String paramString) {
        List<String> list = entryList(paramString);
        return !list.isEmpty();
    }

    public static ZipFile getZipFile(String paramString) throws ZipException, IOException {
        return new ZipFile(new File(paramString));
    }


}
