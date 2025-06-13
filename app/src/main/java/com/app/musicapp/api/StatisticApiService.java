package com.app.musicapp.api;

import com.app.musicapp.model.TopTrack;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentStatisticResponse;
import com.app.musicapp.model.response.LikeResponse;
import com.app.musicapp.model.response.PlayResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatisticApiService {
    @GET("/api/user-library/statistic/plays")
    Call<ApiResponse<PlayResponse>> getPlays(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/likes")
    Call<ApiResponse<LikeResponse>> getLiked(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/comments")
    Call<ApiResponse<CommentStatisticResponse>> getCommentStatistic(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/top-tracks")
    Call<ApiResponse<List<TopTrack>>> getTopTracks(
            @Query("user_id") String userId,
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/plays/all")
    Call<ApiResponse<PlayResponse>> getAllPlays(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );
    @GET("/api/user-library/statistic/comments/all")
    Call<ApiResponse<CommentStatisticResponse>> getAllComments(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/likes/all")
    Call<ApiResponse<LikeResponse>> getAllLiked(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    @GET("/api/user-library/statistic/top-tracks/all")
    Call<ApiResponse<List<TopTrack>>> getAllTopTracks(
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );
}
