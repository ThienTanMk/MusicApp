package com.app.musicapp.api;
import com.app.musicapp.model.LoginResponse;
import com.app.musicapp.model.Track;
import com.app.musicapp.model.LoginRequest;
import com.app.musicapp.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

import java.util.List;

public interface ApiService {
    @GET("/api/tracks")
    Call<List<Track>> getTracks();

    @POST("api/identity/authenticate/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);


    @GET("api/user-library/liked/all")
    Call<ApiResponse<List<Track>>> getLikedTrack();
}
