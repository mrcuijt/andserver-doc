package com.yanzhenjie.andserver.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.sample.server.DemoServer;
import com.yanzhenjie.andserver.sample.server.JettyServer;
import com.yanzhenjie.andserver.sample.util.NetUtils;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class SimpleService extends Service {

    private JettyServer mServer;

    @Override
    public void onCreate() {
        mServer = DemoServer.webServer(this)
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
//                .listener(new Server.ServerListener() {
//                    @Override
//                    public void onStarted() {
//                        InetAddress address = NetUtils.getLocalIPAddress();
//                        ServerManager.onServerStart(SimpleService.this, address.getHostAddress());
//                    }
//
//                    @Override
//                    public void onStopped() {
//                        ServerManager.onServerStop(SimpleService.this);
//                    }
//
//                    @Override
//                    public void onException(Exception e) {
//                        e.printStackTrace();
//                        ServerManager.onServerError(SimpleService.this, e.getMessage());
//                    }
//                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopServer();
        super.onDestroy();
    }

    /**
     * Start server.
     */
    private void startServer() {
        mServer.startup();
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        mServer.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
