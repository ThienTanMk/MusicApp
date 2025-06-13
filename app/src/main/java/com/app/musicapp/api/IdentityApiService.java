package com.app.musicapp.api;

import com.app.musicapp.model.request.UserCreationRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CheckUsernameResponse;
import com.app.musicapp.model.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IdentityApiService {
    @POST("/api/identity/users/registration")
    Call<ApiResponse<UserResponse>> signUp(@Body UserCreationRequest request);

    @GET("/api/identity/authenticate/check-username/{username}")
    Call<ApiResponse<CheckUsernameResponse>> checkUsername(@Path("username") String username);

    @GET("/api/identity/users/my-info")
    Call<ApiResponse<UserResponse>> getUserinfo();
}
