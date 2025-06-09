package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TagResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TagApiService {
    @GET("api/music-service/tags")
    Call<ApiResponse<List<TagResponse>>> getTags();

    @GET("api/music-service/tags/bulk")
    Call<ApiResponse<List<TagResponse>>> getTagsByIds(@Query("ids") List<String> ids);
} 