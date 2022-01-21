package com.yanzhenjie.andserver.handler;

import android.content.Context;

import com.yanzhenjie.andserver.DispatcherHandler;
import com.yanzhenjie.andserver.framework.body.StreamBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.util.DateUtil;
import com.yanzhenjie.andserver.util.MediaType;
import com.yanzhenjie.andserver.util.RequestLineUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DefaultHandler extends DispatcherHandler {

    public static final String NAME = "default";

    private final Context mContext;

    public DefaultHandler(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response){
        String str = RequestLineUtil.getRequestURI(request, response);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer handles = new StringBuffer();
        for (String str1 : HandlerFactory.getRegisterHander()) {
            handles.append("    <li><a href=\"");
            handles.append(str);
            handles.append(str1);
            handles.append("/\" target=\"_self\">");
            handles.append(str1);
            handles.append("</a></li>\r\n");
        }
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
        stringBuffer.append("<h3>Active Handler:</h3>");
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
    }
}
