package com.yanzhenjie.andserver.sample.server;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yanzhenjie.andserver.BuildConfig;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.server.ProxyServer;
import com.yanzhenjie.andserver.server.WebServer;

public class DemoServer {

    public static final String TAG = "AndServer";
    public static final String INFO = String.format("AndServer/%1$s", BuildConfig.PROJECT_VERSION);

    /**
     * Create a builder for the web server.
     *
     * @return {@link Server.Builder}.
     */
    @NonNull
    public static JettyServer.Builder webServer(@NonNull Context context) {
        return JettyServer.newBuilder(context, "default");
    }

    /**
     * Create a builder for the web server.
     *
     * @param group group name.
     *
     * @return {@link Server.Builder}.
     */
    @NonNull
    public static JettyServer.Builder webServer(@NonNull Context context,
                                           @NonNull String group) {
        return JettyServer.newBuilder(context, group);
    }

    /**
     * Create a builder for the reverse proxy server.
     *
     * @return {@link Server.ProxyBuilder}.
     */
    @NonNull
    public static Server.ProxyBuilder proxyServer() {
        return ProxyServer.newBuilder();
    }

    /**
     * @deprecated use {@link #webServer(Context)} instead.
     */
    @NonNull
    @Deprecated
    public static Server.Builder serverBuilder(@NonNull Context context) {
        return webServer(context);
    }

    /**
     * @deprecated use {@link #webServer(Context, String)} instead.
     */
    @NonNull
    @Deprecated
    public static Server.Builder serverBuilder(@NonNull Context context, @NonNull String group) {
        return webServer(context, group);
    }
}
