package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaylistApiService {
    @GET("/api/user-library/playlist/you/all")
    Call<ApiResponse<List<PlaylistResponse>>> getAllPlaylists();

    @GET("/api/user-library/playlist/you/liked")
    Call<ApiResponse<List<PlaylistResponse>>> getLikedPlaylists();

    @GET("/api/user-library/playlist/you/created")
    Call<ApiResponse<List<PlaylistResponse>>> getCreatedPlaylists();

    @GET("/api/user-library/playlist/users/{userId}")
    Call<ApiResponse<List<PlaylistResponse>>> getPlaylistsByUserId(@Path("userId") String userId);

    @GET("/api/user-library/playlist/{id}")
    Call<ApiResponse<PlaylistResponse>> getPlaylistById(@Path("id") String id);

    @GET("/api/user-library/playlist/bulk")
    Call<ApiResponse<List<PlaylistResponse>>> getPlaylistsByIds(@Query("playlist_ids") List<String> ids);

    @DELETE("/api/user-library/playlist/{id}")
    Call<ApiResponse<String>> deletePlaylistByIdV1(@Path("id") String id);

    @Multipart
    @PUT("/api/user-library/playlist")
    Call<ApiResponse<PlaylistResponse>> updatePlaylistInfoV1(
            @Part MultipartBody.Part image,
            @Part("playlist") RequestBody updatePlaylistInfoRequest
    );

}
