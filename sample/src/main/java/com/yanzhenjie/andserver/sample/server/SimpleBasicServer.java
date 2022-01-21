package com.yanzhenjie.andserver.sample.server;

import androidx.annotation.NonNull;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.SSLSocketInitializer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.server.BasicServer;
import com.yanzhenjie.andserver.util.Executors;

import org.apache.httpcore.ExceptionLogger;
import org.apache.httpcore.config.SocketConfig;
import org.apache.httpcore.impl.bootstrap.HttpServer;
import org.apache.httpcore.impl.bootstrap.SSLServerSetupHandler;
import org.apache.httpcore.impl.bootstrap.ServerBootstrap;
import org.apache.httpcore.protocol.HttpRequestHandler;
//import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.server.handler.HandlerWrapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;

//import science.mrcuijt.webdoc.conf.WebDocConfig;
//import science.mrcuijt.webdoc.server.handler.RedirectHandler;
//import science.mrcuijt.webdoc.server.handler.ResponseHandler;

public abstract class SimpleBasicServer <T extends SimpleBasicServer.Builder> implements Server {

    static final int BUFFER = 8 * 1024;

    protected final InetAddress mInetAddress;
    protected final int mPort;
    protected final int mTimeout;
    protected final ServerSocketFactory mSocketFactory;
    protected final SSLContext mSSLContext;
    protected final SSLSocketInitializer mSSLSocketInitializer;
    protected final Server.ServerListener mListener;

    private Server mHttpServer;
    protected boolean isRunning;

    SimpleBasicServer(T builder) {
        this.mInetAddress = builder.inetAddress;
        this.mPort = builder.port;
        this.mTimeout = builder.timeout;
        this.mSocketFactory = builder.mSocketFactory;
        this.mSSLContext = builder.sslContext;
        this.mSSLSocketInitializer = builder.mSSLSocketInitializer;
        this.mListener = builder.listener;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void startup() {
        if (isRunning) {
            return;
        }
//
//        Executors.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    WebDocConfig.setPort(8080);
//                    WebDocConfig.setBasePath("");
//                    mHttpServer = new org.eclipse.jetty.server.Server(WebDocConfig.getPort());
//                    HandlerWrapper handlerWrapper = new HandlerWrapper();
//                    ResponseHandler responseHandler = new ResponseHandler();
//                    handlerWrapper.setHandler((Handler)responseHandler);
//                    RedirectHandler redirectHandler = new RedirectHandler();
//                    mHttpServer.setHandler((Handler)handlerWrapper);
//
//                    mHttpServer.start();
//
//                    isRunning = true;
//
//                    Executors.getInstance().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mListener != null) {
//                                mListener.onStarted();
//                            }
//                        }
//                    });
//                    Runtime.getRuntime().addShutdownHook(new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                mHttpServer.stop();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                } catch (final Exception e) {
//                    Executors.getInstance().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mListener != null) {
//                                mListener.onException(e);
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    /**
     * Assigns {@link HttpRequestHandler} instance.
     */
    protected abstract HttpRequestHandler requestHandler();

    /**
     * Quit the server.
     */
    @Override
    public void shutdown() {
        if (!isRunning) {
            return;
        }

        Executors.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (mHttpServer != null) {
                    try {
//                        mHttpServer.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isRunning = false;
                    Executors.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onStopped();
                            }
                        }
                    });
                }
            }
        });
    }

    private static final class SSLSetup implements SSLServerSetupHandler {

        private final SSLSocketInitializer mInitializer;

        public SSLSetup(@NonNull SSLSocketInitializer initializer) {
            this.mInitializer = initializer;
        }

        @Override
        public void initialize(SSLServerSocket socket) throws SSLException {
            mInitializer.onCreated(socket);
        }
    }

    @Override
    public InetAddress getInetAddress() {
        if (isRunning) {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("The server has not been started yet.");
    }

    @Override
    public int getPort() {
        if (isRunning) {
//            return WebDocConfig.getPort();
        }
        throw new IllegalStateException("The server has not been started yet.");
    }

    protected abstract static class Builder<T extends SimpleBasicServer.Builder, S extends SimpleBasicServer> {

        InetAddress inetAddress;
        int port;
        int timeout;
        ServerSocketFactory mSocketFactory;
        SSLContext sslContext;
        SSLSocketInitializer mSSLSocketInitializer;
        Server.ServerListener listener;

        Builder() {
        }

        public T inetAddress(InetAddress inetAddress) {
            this.inetAddress = inetAddress;
            return (T) this;
        }

        public T port(int port) {
            this.port = port;
            return (T) this;
        }

        public T timeout(int timeout, TimeUnit timeUnit) {
            long timeoutMs = timeUnit.toMillis(timeout);
            this.timeout = (int) Math.min(timeoutMs, Integer.MAX_VALUE);
            return (T) this;
        }

        public T serverSocketFactory(ServerSocketFactory factory) {
            this.mSocketFactory = factory;
            return (T) this;
        }

        public T sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return (T) this;
        }

        public T sslSocketInitializer(SSLSocketInitializer initializer) {
            this.mSSLSocketInitializer = initializer;
            return (T) this;
        }

        public T listener(Server.ServerListener listener) {
            this.listener = listener;
            return (T) this;
        }

        public abstract S build();
    }
}
