package com.app.musicapp.api;

import android.content.Context;
import android.content.Intent;

import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.view.activity.SignIn;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final SharedPreferencesManager preferencesManager;
    private Context context;
    public AuthInterceptor(Context context) {
        this.context = context;
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

        Response response =  chain.proceed(newRequest);
        if(response.code() == 401){
            logoutAndRedirect();
        }
        return  response;
    }
    private void logoutAndRedirect() {
        preferencesManager.clearSession(); // Xoá token và dữ liệu người dùng

        Intent intent = new Intent(context, SignIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
} 