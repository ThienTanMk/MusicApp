package com.app.musicapp.api;

import com.app.musicapp.model.request.AddFollowRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PageFollowResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("/api/profile/follows/get-followers/{userId}")
    Call<ApiResponse<PageFollowResponse>> getFollowers(@Path("userId") String userId, @Query("page") int page, @Query("size") int size );
    @GET("/api/profile/follows/get-followings/{userId}")
    Call<ApiResponse<PageFollowResponse>> getFollowings(@Path("userId") String userId, @Query("page") int page, @Query("size") int size );
    @POST("/api/profile/auth/follows")
    Call<ApiResponse<Object>> follow(@Body AddFollowRequest request);

    @DELETE("/api/profile/auth/follows/unfollow/{userId}")
    Call<ApiResponse<Object>> unfollow(@Path("userId") String userId);

}
