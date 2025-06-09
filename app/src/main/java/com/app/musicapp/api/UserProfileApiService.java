package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserProfileApiService {
    @GET("/api/profile/users/{userId}")
    Call<ApiResponse<ProfileWithCountFollowResponse>> getUserProfile(
            @Path("userId") String userId
    );
//search
    @GET("/api/profile/users/bulk")
    Call<ApiResponse<List<ProfileWithCountFollowResponse>>> getUserProfilesByIds(
            @Query("ids") List<String> ids
    );

    @GET("/api/profile/users/search-by-email")
    Call<ApiResponse<UserProfileResponse>> getUserProfileByEmail(
            @Query("email") String email
    );

    @GET("/api/profile/users/top-followed")
    Call<ApiResponse<List<ProfileWithCountFollowResponse>>> getTopFollowedUsers(
            @Query("limit") int limit
    );
}
