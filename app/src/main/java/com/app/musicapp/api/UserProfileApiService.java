package com.app.musicapp.api;

import com.app.musicapp.model.request.ProfileUpdateRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.UploadAvatarResponse;
import com.app.musicapp.model.response.UploadCoverResponse;
import com.app.musicapp.model.response.UserProfileResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
            @Query("userIds") List<String> ids
    );

    @GET("/api/profile/users/search-by-email")
    Call<ApiResponse<UserProfileResponse>> getUserProfileByEmail(
            @Query("email") String email
    );

    @GET("/api/profile/users/top-followed")
    Call<ApiResponse<List<ProfileWithCountFollowResponse>>> getTopFollowedUsers(
            @Query("limit") int limit
    );

    @PUT("/api/profile/auth/users/update-my-info")
    Call<ApiResponse<UserProfileResponse>> updateUserProfile(
            @Body ProfileUpdateRequest request
    );

    @Multipart
    @POST("/api/profile/auth/users/upload-avatar")
    Call<ApiResponse<UploadAvatarResponse>> uploadAvatar(
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("/api/profile/auth/users/upload-cover")
    Call<ApiResponse<UploadCoverResponse>> uploadCover(
            @Part MultipartBody.Part cover
    );

    @GET("/api/profile/follows/is-following")
    Call<ApiResponse<Boolean>> isFollowing(
            @Query("followerId") String followerId,
            @Query("followingId") String followingId
    );
}
