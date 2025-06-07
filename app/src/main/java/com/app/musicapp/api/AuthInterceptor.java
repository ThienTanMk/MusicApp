package com.app.musicapp.api;

import android.content.Context;
import com.app.musicapp.helper.SharedPreferencesManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final SharedPreferencesManager preferencesManager;

    public AuthInterceptor(Context context) {
        this.preferencesManager = SharedPreferencesManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = preferencesManager.getToken();

        // If token is not available, proceed with original request
        if (token == null) {
            return chain.proceed(originalRequest);
        }

        // Add token to request header
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
} 