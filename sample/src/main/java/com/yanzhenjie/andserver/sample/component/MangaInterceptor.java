package com.yanzhenjie.andserver.sample.component;

import androidx.annotation.NonNull;

import com.yanzhenjie.andserver.annotation.Interceptor;
import com.yanzhenjie.andserver.error.HttpException;
import com.yanzhenjie.andserver.framework.HandlerInterceptor;
import com.yanzhenjie.andserver.framework.handler.MethodHandler;
import com.yanzhenjie.andserver.framework.handler.RequestHandler;
import com.yanzhenjie.andserver.framework.mapping.Addition;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;

@Interceptor
public class MangaInterceptor implements HandlerInterceptor {

    public static final String MANGA_ATTRIBUTE = "MANGA.CONTENT";

    @Override
    public boolean onIntercept(@NonNull HttpRequest request, @NonNull HttpResponse response,
                               @NonNull RequestHandler handler) {
        if (handler instanceof MethodHandler) {
            MethodHandler methodHandler = (MethodHandler) handler;
            Addition addition = methodHandler.getAddition();

        }
        return false;
    }
}
