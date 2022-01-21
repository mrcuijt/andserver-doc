package com.yanzhenjie.andserver.util;

import com.yanzhenjie.andserver.entity.DocEntry;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;

public class RequestLineUtil {
    public static DocEntry processDocEntry(String paramString) {
        if (paramString.indexOf("?") != -1)
            paramString = paramString.substring(0, paramString.indexOf("?"));
        String[] arrayOfString = paramString.split("!/");
        boolean bool = (arrayOfString.length == 2) ? true : false;
        String str1 = getFileName(arrayOfString[0]);
        String str2 = "index.html";
        str2 = bool ? arrayOfString[1] : str2;
        DocEntry docEntry = new DocEntry();
        docEntry.setDoc(str1);
        docEntry.setEntry(str2);
        return docEntry;
    }

    private static String getFileName(String paramString) {
        if (paramString == null || paramString.length() == 0)
            return "NULL";
        if (paramString.endsWith("/"))
            paramString = paramString.substring(0, paramString.length() - 1);
        String str = paramString.substring(paramString.lastIndexOf("/") + 1);
        return (str.indexOf("?") > 0) ? str.substring(0, str.lastIndexOf("?")) : str;
    }

    public static String getRequestURI(HttpRequest request, HttpResponse response) {
        return String.format("//%s:%d/", new Object[] { request.getLocalAddr(), Integer.valueOf(request.getLocalPort()) });
    }
}
