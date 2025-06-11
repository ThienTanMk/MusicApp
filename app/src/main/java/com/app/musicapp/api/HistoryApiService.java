package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HistoryApiService {
    @GET("/api/user-library/history/all")
    Call<ApiResponse<List<TrackResponse>>> getAllHistory();
    @DELETE("/api/user-library/history/all")
    Call<ApiResponse<String>> deleteAllHistory();

    @POST("/api/user-library/history")
    Call<ApiResponse<Object>> listenTrack(@Query("track_id")String trackId);
}
