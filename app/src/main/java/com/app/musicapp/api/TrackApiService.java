package com.app.musicapp.api;

import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.List;

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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrackApiService {
    @POST("api/music-service/tracks/plays-cout/{id}")
    Call<ApiResponse<String>> updatePlayCount(@Path("id") String id);

    @Multipart
    @POST("api/music-service/tracks")
    Call<ApiResponse<TrackResponse>> uploadTrack(
            @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part trackAudio,
            @Part("track") RequestBody trackRequest
    );

    @Multipart
    @POST("api/music-service/tracks/bulk")
    Call<ApiResponse<List<TrackResponse>>> uploadTracks(
            @Part List<MultipartBody.Part> trackFiles,
            @Part("trackRequests") RequestBody trackRequests
    );

    @DELETE("api/music-service/tracks/{trackId}")
    Call<ApiResponse<String>> deleteTrack(@Path("trackId") String trackId);

    @GET("api/music-service/tracks/{id}")
    Call<ApiResponse<TrackResponse>> getTrackById(@Path("id") String id);

    @GET("api/music-service/tracks/users/{user_id}")
    Call<ApiResponse<List<TrackResponse>>> getTracksByUserId(@Path("user_id") String userId);
//search
    @GET("api/music-service/tracks/bulk")
    Call<ApiResponse<List<TrackResponse>>> getTracksByIds(@Query("ids") List<String> ids);

    @GET("api/music-service/tracks/by-genre")
    Call<ApiResponse<List<TrackResponse>>> getTracksByGenre(
            @Query("genreId") String genreId,
            @Query("limit") int limit
    );

    @Multipart
    @PUT("api/music-service/tracks/{trackId}")
    Call<ApiResponse<TrackResponse>> updateTrack(
            @Path("trackId") String trackId,
            @Part("meta-data") RequestBody request,
            @Part MultipartBody.Part imageFile,
            @Part MultipartBody.Part trackFile
    );
}