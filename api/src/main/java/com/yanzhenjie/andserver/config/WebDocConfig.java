package com.yanzhenjie.andserver.config;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;

public class WebDocConfig {

    private static int port = 8080;

    private static String basePath = "docs";

    public static int getPort() {
        return port;
    }

    public static void setPort(int paramInt) {
        port = paramInt;
    }

    public static String getBasePath(Context mContext) {
        File files = mContext.getExternalFilesDir("");
        String path = files.getParentFile().getAbsolutePath();
        File mPath = new File(path, basePath);
        if(mPath.exists()) mPath.mkdirs();
        return mPath.getAbsolutePath() + "/";
    }

    public static void setBasePath(String paramString) {
        basePath = paramString;
    }
}
