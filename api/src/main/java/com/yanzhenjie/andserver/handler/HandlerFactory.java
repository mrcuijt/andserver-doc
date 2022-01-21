package com.yanzhenjie.andserver.handler;

import com.yanzhenjie.andserver.DispatcherHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerFactory {

    private static final Map<String, DispatcherHandler> services = new ConcurrentHashMap<>();

    private static final Set<String> handlers = new HashSet<>();

    public static DispatcherHandler getByHandlerName(String paramString) {
        return services.get(paramString);
    }

    public static void register(String paramString, DispatcherHandler paramAbstractHandler) {
        if (paramAbstractHandler == null)
            throw new RuntimeException("HandlerName can't be null");
        services.put(paramString, paramAbstractHandler);
        handlers.add(paramString);
    }

    public static Set<String> getRegisterHander() {
        return handlers;
    }

    public static String getHandlerName(String paramString) {
        for (String str : handlers) {
            if (paramString.indexOf(str) != -1)
                return str;
        }
        return "default";
    }

    public static DispatcherHandler getDefaultHandler() {
        return services.get("default");
    }
}
