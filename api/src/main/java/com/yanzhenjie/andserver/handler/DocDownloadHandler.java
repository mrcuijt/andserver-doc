package com.yanzhenjie.andserver.handler;

import android.content.Context;

import com.yanzhenjie.andserver.DispatcherHandler;
import com.yanzhenjie.andserver.config.WebDocConfig;
import com.yanzhenjie.andserver.entity.FileModel;
import com.yanzhenjie.andserver.framework.body.FileBody;
import com.yanzhenjie.andserver.framework.body.StreamBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.FileUtil;
import com.yanzhenjie.andserver.util.MediaType;
import com.yanzhenjie.andserver.util.MimeTypeUtil;
import com.yanzhenjie.andserver.util.RequestLineUtil;
import com.yanzhenjie.andserver.util.StringUtils;
import com.yanzhenjie.andserver.util.WebDocUtil;
import com.yanzhenjie.andserver.util.ZipUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.zip.ZipFile;

public class DocDownloadHandler extends DispatcherHandler {

    public static final String NAME = "doc-download";
    private static final String PATH = "/doc-download/";
    private URLDecoder decoder = new URLDecoder();

    private final Context mContext;

    public DocDownloadHandler(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String str1 = RequestLineUtil.getRequestURI(request, response);
        String str2 = WebDocConfig.getBasePath(mContext);
        String str3 = request.getURI();
        str3 = str3.replace("scheme:", "");
        try {
            str3 = URLDecoder.decode(str3, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str4 = RequestLineUtil.processDocEntry(str3).getDoc();
        System.out.println(str4);
        File file = new File(str2 + str4);
        String str5 = "";
        if (!file.exists()) {
            List<File> list1 = FileUtil.files(str2);
            List<FileModel> list2 = FileUtil.generateFileModels(list1);
            List list3 = WebDocUtil.generateGroupListByFileType(list2);
            list2 = WebDocUtil.mergeAndSortFileModel(list3);
            StringBuffer handles = new StringBuffer();
            if (!list2.isEmpty())
                for (FileModel fileModel : list2) {
                    handles.append("    <li><a href=\"");
                    handles.append(str1);
                    handles.append("doc-download");
                    handles.append("/");
                    handles.append(fileModel.getFileName());
                    handles.append("\" target=\"_self\">");
                    handles.append(fileModel.getFileName());
                    handles.append("</a></li>\r\n");
                }

            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setStatus(200);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<!DOCTYPE html>");
            stringBuffer.append("<html>");
            stringBuffer.append("<head>");
            stringBuffer.append("<meta charset=\"UTF-8\">");
            stringBuffer.append("<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">");
            stringBuffer.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,Chrome=1\" />");
            stringBuffer.append("<title>Index</title>");
            stringBuffer.append("</head>");
            stringBuffer.append("<body>");
            stringBuffer.append("<h3>DocDownload Handler:</h3>");
            stringBuffer.append("  <ul>");
            stringBuffer.append(handles.toString());
            stringBuffer.append("  </ul>");
            stringBuffer.append("</body>");
            stringBuffer.append("</html>");
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(stringBuffer.toString().getBytes());
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                response.setBody(new StreamBody(bais, baos.toByteArray().length, MediaType.TEXT_HTML));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // ????????????
        ResponseBody responseBody = new FileBody(file);
        response.setBody(responseBody);
    }
}
