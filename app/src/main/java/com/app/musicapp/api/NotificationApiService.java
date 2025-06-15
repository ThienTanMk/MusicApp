package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.NotificationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationApiService {
    @POST("/api/notification-service/notifications/token/{token}")
    Call<Void> sendToken(@Path("token") String token);
    @GET("/api/notification-service/notifications/all")
    Call<ApiResponse<List<String>>> getAll();
    @GET("/api/notification-service/notifications/bulk")
    Call<ApiResponse<List<NotificationResponse>>> getNotifications(@Query("ids")List<String> ids);
}
