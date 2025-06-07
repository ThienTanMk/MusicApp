package com.app.musicapp.api;

import com.app.musicapp.model.ApiResponse;
import com.app.musicapp.model.AlbumResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumApiService {
    // Public endpoints
    @GET("/api/user-library/album/bulk")
    Call<ApiResponse<List<AlbumResponse>>> getAlbumsByIds(@Query("album_ids") List<String> ids);

    @GET("/api/user-library/album/find-by-album-id/{albumId}")
    Call<ApiResponse<AlbumResponse>> getAlbumById(@Path("albumId") String albumId);

    @GET("/api/user-library/album/{albumLink}")
    Call<ApiResponse<AlbumResponse>> getAlbumByLink(@Path("albumLink") String albumLink);

    @GET("/api/user-library/album/find-by-user-id/{userId}")
    Call<ApiResponse<List<AlbumResponse>>> getAlbumsByUserId(@Path("userId") String userId);

    @GET("/api/user-library/album/liked-albums/{userId}")
    Call<ApiResponse<List<AlbumResponse>>> getLikedAlbums(@Path("userId") String userId);

    @GET("/api/user-library/album/get-liked-created-album/{userId}")
    Call<ApiResponse<List<AlbumResponse>>> getCreatedAndLikedAlbums(@Path("userId") String userId);

    @GET("/api/user-library/album/count/{albumId}")
    Call<ApiResponse<Integer>> getAlbumLikeCount(@Path("albumId") String albumId);

    @GET("/api/user-library/album/users/{albumId}")
    Call<ApiResponse<List<String>>> getUsersWhoLikedAlbum(@Path("albumId") String albumId);

    // Authenticated endpoints
    @Multipart
    @POST("/api/user-library/auth/album/add-album")
    Call<ApiResponse<AlbumResponse>> createAlbum(
            @Part("meta-data") RequestBody albumRequest,
            @Part MultipartBody.Part coverAlbum,
            @Part("track-request") RequestBody trackRequests,
            @Part List<MultipartBody.Part> trackFiles
    );

    @DELETE("/api/user-library/auth/album/{albumId}")
    Call<ApiResponse<String>> deleteAlbum(@Path("albumId") String albumId);

    @Multipart
    @PUT("/api/user-library/auth/album/{albumId}")
    Call<ApiResponse<AlbumResponse>> updateAlbum(
            @Path("albumId") String albumId,
            @Part("meta-data") RequestBody albumRequest,
            @Part MultipartBody.Part coverAlbum
    );

    @POST("/api/user-library/auth/album/like/{albumId}")
    Call<ApiResponse<Void>> likeAlbum(@Path("albumId") String albumId);

    @DELETE("/api/user-library/auth/album/unlike/{albumId}")
    Call<ApiResponse<Void>> unlikeAlbum(@Path("albumId") String albumId);

    @POST("/api/user-library/auth/album/add-track")
    Call<ApiResponse<Void>> addTrackToAlbum(@Body RequestBody request);
} 