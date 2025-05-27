package com.app.musicapp.api;

import android.content.Context;

import com.app.musicapp.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Replace 192.168.1.x with your computer's actual IP address
    private static final String BASE_URL = "http://192.168.110.196:8888/";
    private static Retrofit retrofit = null;
    private static Context context;

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create Gson instance with LocalDateTime adapter
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            // Create OkHttpClient with interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(ApiService.class);
    }

    // Call this when you need to clear the retrofit instance (e.g., on logout)
    public static void clearInstance() {
        retrofit = null;
    }
}
