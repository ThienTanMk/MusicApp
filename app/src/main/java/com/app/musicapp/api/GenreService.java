package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GenreService {
    @GET("api/music-service/genres")
    Call<ApiResponse<List<GenreResponse>>> getGenres();

    @GET("api/music-service/genres/all")
    Call<ApiResponse<List<GenreResponse>>> getAllGenres();

    @GET("api/music-service/genres/bulk")
    Call<ApiResponse<List<GenreResponse>>> getGenresByIds(@Query("ids") List<String> ids);

    @GET("api/music-service/genres/{id}")
    Call<ApiResponse<GenreResponse>> getGenreById(@Path("id") String id);
} 