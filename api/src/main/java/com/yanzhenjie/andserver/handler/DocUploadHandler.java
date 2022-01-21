package com.yanzhenjie.andserver.handler;

import android.content.Context;

import com.yanzhenjie.andserver.DispatcherHandler;
import com.yanzhenjie.andserver.config.WebDocConfig;
import com.yanzhenjie.andserver.framework.body.StreamBody;
import com.yanzhenjie.andserver.http.ApacheRequestContext;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.multipart.BodyContext;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;
import com.yanzhenjie.andserver.http.multipart.MultipartRequest;
import com.yanzhenjie.andserver.util.DateUtil;
import com.yanzhenjie.andserver.util.MediaType;
import com.yanzhenjie.andserver.util.RequestLineUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.yanzhenjie.andserver.http.HttpHeaders.LOCATION;
import static com.yanzhenjie.andserver.http.StatusCode.SC_FOUND;

public class DocUploadHandler extends DispatcherHandler {

    public static final String NAME = "doc-upload";

    private final Context mContext;

    public DocUploadHandler(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response){
        try {
            String str = request.getMethod().value();
            if (str.equals("POST")) {
                doPost(request, response);
            } else {
                doGet(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        String str1 = RequestLineUtil.getRequestURI(request, response);
        String str2 = WebDocConfig.getBasePath(mContext);
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        FileUpload servletFileUpload = new FileUpload((FileItemFactory)diskFileItemFactory);
        ArrayList arrayList = new ArrayList();
        ArrayList<FileItem> arrayList1 = new ArrayList();
        try {
            List list = servletFileUpload.parseRequest(new BodyContext(request.getBody()));
            Iterator<FileItem> iterator = list.iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                if (fileItem.isFormField())
                    continue;
                arrayList1.add(fileItem);
            }
        } catch (FileUploadException fileUploadException) {
            fileUploadException.printStackTrace();
        }
        for (FileItem fileItem : arrayList1) {
            String str = fileItem.getName();
            long l = fileItem.getSize();
            File file1 = new File(str);
            str = file1.toURI().toURL().getFile();
            str = RequestLineUtil.processDocEntry(str).getDoc();
            File file2 = new File(str2 + str);
            if (file2.exists()) {
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
                stringBuffer.append("<h3>");
                stringBuffer.append("<a href=\"");
                stringBuffer.append(str1);
                stringBuffer.append("doc-upload");
                stringBuffer.append("\" target=\"_self\">");
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
            try {
                fileItem.write(file2);
                System.out.println("Save file to : " + file2.getAbsolutePath());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        response.setHeader(LOCATION, DocListHandler.NAME);
        response.setStatus(SC_FOUND);
    }

    public void doGet(HttpRequest request, HttpResponse response){
        String str = RequestLineUtil.getRequestURI(request, response);
        StringBuffer stringBuffer = new StringBuffer();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setStatus(200);
        stringBuffer.append("<!DOCTYPE html>");
        stringBuffer.append("<html>");
        stringBuffer.append("<head>");
        stringBuffer.append("<meta charset=\"UTF-8\">");
        stringBuffer.append("<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">");
        stringBuffer.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,Chrome=1\" />");
        stringBuffer.append("<title>Index</title>");
        stringBuffer.append("</head>");
        stringBuffer.append("<body>");
        stringBuffer.append("<h3>Doc Upload:</h3>");
        stringBuffer.append("  <form action=\"");
        stringBuffer.append(str);
        stringBuffer.append("doc-upload");
        stringBuffer.append("\" method=\"POST\" enctype=\"multipart/form-data\">");
        stringBuffer.append("    <label for=\"file\">File:</label>");
        stringBuffer.append("    <input id=\"file\" name=\"file\" type=\"file\"/>");
        stringBuffer.append("    <button type=\"submit\">Upload</button>");
        stringBuffer.append("  </form>");
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
    }
}
