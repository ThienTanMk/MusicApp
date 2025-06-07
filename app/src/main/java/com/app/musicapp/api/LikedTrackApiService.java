package com.app.musicapp.api;

import com.app.musicapp.model.ApiResponse;
import com.app.musicapp.model.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikedTrackApiService {
    @GET("/api/user-library/liked/all")
    Call<ApiResponse<List<Track>>> getAllLikedTracks();

    @GET("/api/user-library/liked/count/{trackId}")
    Call<ApiResponse<Integer>> getTrackLikeCount(@Path("trackId") String trackId);

    @GET("/api/user-library/liked/is_liked/{trackId}")
    Call<ApiResponse<Boolean>> isLiked(@Path("trackId") String trackId);

    @POST("/api/user-library/liked/like/{trackId}")
    Call<ApiResponse<Boolean>> likeTrack(@Path("trackId") String trackId);

    @POST("/api/user-library/liked/unlike/{trackId}")
    Call<ApiResponse<Boolean>> unlikeTrack(@Path("trackId") String trackId);

    @GET("/api/user-library/liked/users/{trackId}")
    Call<ApiResponse<List<String>>> getUserIdsLikedTrack(@Path("trackId") String trackId);
} 