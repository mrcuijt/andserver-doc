package com.yanzhenjie.andserver.http;

import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;

public class ApacheRequestContext implements RequestContext {

    HttpRequest request;

    public ApacheRequestContext(HttpRequest request){
        this.request = request;
    }

    @Override
    public String getCharacterEncoding() {
        return request.getBody().contentEncoding();
    }

    @Override
    public String getContentType() {
        String type = request.getBody().contentType().getType();
        if("multipart".equals(type)){
            return "multipart/form-data";
        }
        return request.getBody().contentType().getType();
    }

    @Override
    public int getContentLength() {
        return new Long(request.getContentLength()).intValue();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return request.getBody().stream();
    }
}
