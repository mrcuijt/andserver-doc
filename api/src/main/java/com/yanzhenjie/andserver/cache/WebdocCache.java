package com.yanzhenjie.andserver.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebdocCache {
    private static final Map<String, List<String>> cache = new HashMap<>();

    public static List<String> getCache(String paramString) {
        if (cache.containsKey(paramString))
            return cache.get(paramString);
        return Collections.emptyList();
    }

    public static void addCache(String paramString, List<String> paramList) {
        cache.put(paramString, paramList);
    }
}
