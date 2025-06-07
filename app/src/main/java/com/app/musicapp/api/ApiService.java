package com.app.musicapp.api;

import com.app.musicapp.model.request.LoginRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LoginResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

import java.util.List;

public interface ApiService {
    @GET("/api/tracks")
    Call<List<TrackResponse>> getTracks();

    @POST("api/identity/authenticate/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);


    @GET("api/user-library/liked/all")
    Call<ApiResponse<List<TrackResponse>>> getLikedTrack();

    @GET("api/user-library/playlist/you/all")
    Call<ApiResponse<List<PlaylistResponse>>> getAllPlaylist();
}
