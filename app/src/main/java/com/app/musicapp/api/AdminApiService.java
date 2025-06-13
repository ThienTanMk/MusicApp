package com.app.musicapp.api;

import com.app.musicapp.model.request.GenreRequest;
import com.app.musicapp.model.request.TagRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentStatisticResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.LikeResponse;
import com.app.musicapp.model.response.PlayResponse;
import com.app.musicapp.model.response.TagResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    @POST("/api/music-service/genres")
    Call<ApiResponse<GenreResponse>> createGenre(@Body GenreRequest request);

    @DELETE("/api/music-service/genres/{genreId}")
    Call<ApiResponse<String>> deleteGenre(@Path("genreId") String genreId);

    // Tạo Tag
    @POST("/api/music-service/auth/tags")
    Call<ApiResponse<TagResponse>> createTag(@Body TagRequest request);

    // Xoá Tag
    @DELETE("/api/music-service/auth/tags/{tagId}")
    Call<ApiResponse<String>> deleteTag(@Path("tagId") String tagId);
}
