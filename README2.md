# andserver-doc

#### 程序入口类

```java
import com.yanzhenjie.andserver.sample.MainActivity;
```

主要类

 - com.yanzhenjie.andserver.sample.ServerManager

 - com.yanzhenjie.andserver.sample.CoreService

 - com.yanzhenjie.andserver.Server

 - com.yanzhenjie.andserver.AndServer
 
 - com.yanzhenjie.andserver.server.WebServer
 
 - com.yanzhenjie.andserver.server.BasicServer

由 ServerManager 负责启动 AndServer 服务器，并以后台服务方式注册在 Android 系统中。

**ServerManager.java**

```java
import com.yanzhenjie.andserver.sample.ServerManager;

public class ServerManager {
    public ServerManager(MainActivity activity) {
        this.mActivity = activity;
        mService = new Intent(activity, CoreService.class);
    }
}
```

**CoreService.java**

由 CoreService 维护服务器的状态。

AndServer --> |WebServer extends BasicServer|

```java
import com.yanzhenjie.andserver.sample.CoreService;

public class CoreService {

    private Server mServer;

    @Override
    public void onCreate() {
        mServer = AndServer.webServer(this)
            .port(8080)
            .timeout(10, TimeUnit.SECONDS)
            .listener(new Server.ServerListener() {
                @Override
                public void onStarted() {
                    InetAddress address = NetUtils.getLocalIPAddress();
                    ServerManager.onServerStart(CoreService.this, address.getHostAddress());
                }
    
                @Override
                public void onStopped() {
                    ServerManager.onServerStop(CoreService.this);
                }

                @Override
                public void onException(Exception e) {
                    e.printStackTrace();
                    ServerManager.onServerError(CoreService.this, e.getMessage());
                }
            })
            .build();
    }

    /**
     * Start server.
     */
    private void startServer() {
        mServer.startup();
    }

}
```

**BasicServer**

由 BasicServer 注册处理请求的 Handler，通过 DispatcherHandler 完成。

```java
import com.yanzhenjie.andserver.server.BasicServer;

public class BasicServer{
    @Override
    protected HttpRequestHandler requestHandler() {
        DispatcherHandler.registHandler(mContext);
        WebDocConfig.setBasePath("manga");
        DispatcherHandler handler = new DispatcherHandler(mContext);
        ComponentRegister register = new ComponentRegister(mContext);
        try {
            register.register(handler, mGroup);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return handler;
    }
}
```
