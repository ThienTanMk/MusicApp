package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentStatisticResponse;
import com.app.musicapp.model.response.LikeResponse;
import com.app.musicapp.model.response.PlayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdminApiService {
    @GET("/api/admin-service/statistic/plays")
    Call<ApiResponse<PlayResponse>> getAllPlays(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/admin-service/statistic/likes")
    Call<ApiResponse<LikeResponse>> getAllLiked(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/admin-service/statistic/comments")
    Call<ApiResponse<CommentStatisticResponse>> getAllComments(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );
}
