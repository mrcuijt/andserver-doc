package com.yanzhenjie.andserver.sample.server;

import android.content.Context;

import com.yanzhenjie.andserver.server.BasicServer;
import com.yanzhenjie.andserver.server.WebServer;

import org.apache.httpcore.protocol.HttpRequestHandler;
//import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//import org.eclipse.jetty.server.handler.DefaultHandler;
//import org.eclipse.jetty.server.handler.HandlerWrapper;
//
//import science.mrcuijt.webdoc.conf.WebDocConfig;
//import science.mrcuijt.webdoc.server.handler.DocDeleteHandler;
//import science.mrcuijt.webdoc.server.handler.DocListHandler;
//import science.mrcuijt.webdoc.server.handler.DocUploadHandler;
//import science.mrcuijt.webdoc.server.handler.DocViewHandler;
//import science.mrcuijt.webdoc.server.handler.HandlerFactory;
//import science.mrcuijt.webdoc.server.handler.IcoHandler;
//import science.mrcuijt.webdoc.server.handler.RedirectHandler;
//import science.mrcuijt.webdoc.server.handler.ResponseHandler;

public class JettyServer extends SimpleBasicServer<JettyServer.Builder> {

    public static JettyServer.Builder newBuilder(Context context, String group) {
        return new JettyServer.Builder(context, group);
    }

    private Context mContext;
    private String mGroup;

    private JettyServer(JettyServer.Builder builder) {
        super(builder);
        this.mContext = builder.context;
        this.mGroup = builder.group;
//        registHandler();
    }

    @Override
    protected HttpRequestHandler requestHandler() {
        return null;
    }

    public static class Builder extends SimpleBasicServer.Builder<JettyServer.Builder, JettyServer>
            implements com.yanzhenjie.andserver.Server.Builder<JettyServer.Builder, JettyServer> {

        private Context context;
        private String group;

        private Builder(Context context, String group) {
            this.context = context;
            this.group = group;
        }

        @Override
        public JettyServer build() {
            return new JettyServer(this);
        }
    }

//    public static void main(String[] paramArrayOfString) throws Exception {
//        //WebDocConfig.init();
//        WebDocConfig.setPort(8080);
//        WebDocConfig.setBasePath("");
//        registHandler();
//        Server server = new Server(WebDocConfig.getPort());
//        HandlerWrapper handlerWrapper = new HandlerWrapper();
//        ResponseHandler responseHandler = new ResponseHandler();
//        handlerWrapper.setHandler((Handler)responseHandler);
//        RedirectHandler redirectHandler = new RedirectHandler();
//        server.setHandler((Handler)handlerWrapper);
//        server.start();
//        server.join();
//    }
//
//    private static void registHandler() {
//        HandlerFactory handlerFactory = new HandlerFactory();
//        DefaultHandler defaultHandler = new DefaultHandler();
//        DocListHandler docListHandler = new DocListHandler();
//        DocViewHandler docViewHandler = new DocViewHandler();
//        DocUploadHandler docUploadHandler = new DocUploadHandler();
//        DocDeleteHandler docDeleteHandler = new DocDeleteHandler();
//        IcoHandler icoHandler = new IcoHandler();
//        HandlerFactory.register("default", (AbstractHandler)defaultHandler);
//        HandlerFactory.register("doc-list", (AbstractHandler)docListHandler);
//        HandlerFactory.register("doc-view", (AbstractHandler)docViewHandler);
//        HandlerFactory.register("doc-upload", (AbstractHandler)docUploadHandler);
//        HandlerFactory.register("doc-delete", (AbstractHandler)docDeleteHandler);
//        HandlerFactory.register("favicon.ico", (AbstractHandler)icoHandler);
//    }


}
