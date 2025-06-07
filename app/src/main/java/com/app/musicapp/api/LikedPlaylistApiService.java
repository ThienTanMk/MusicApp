package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface LikedPlaylistApiService {
    // [GET] /playlist/liked/all
    @GET("/playlist/liked/all")
    Call<ApiResponse<List<LikedPlaylistResponse>>> getAllLikedPlaylists();

    // [GET] /playlist/liked/is_liked/{playlistId}
    @GET("/playlist/liked/is_liked/{playlistId}")
    Call<ApiResponse<Boolean>> isLiked(@Path("playlistId") String playlistId);

    // [POST] /playlist/liked/like/{playlistId}
    @POST("/playlist/liked/like/{playlistId}")
    Call<ApiResponse<Boolean>> likePlaylist(@Path("playlistId") String playlistId);

    // [POST] /playlist/liked/unlike/{playlistId}
    @POST("/playlist/liked/unlike/{playlistId}")
    Call<ApiResponse<Boolean>> unlikePlaylist(@Path("playlistId") String playlistId);

    // [GET] /playlist/liked/count/{playlistId}
    @GET("/playlist/liked/count/{playlistId}")
    Call<ApiResponse<Integer>> getLikedCount(@Path("playlistId") String playlistId);

    // [GET] /playlist/liked/users/{playlistId}
    @GET("/playlist/liked/users/{playlistId}")
    Call<ApiResponse<List<String>>> getUserIdsLikedPlaylist(@Path("playlistId") String playlistId);
}
