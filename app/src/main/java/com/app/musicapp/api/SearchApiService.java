package com.app.musicapp.api;

import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApiService {
    @GET("/api/search-service/playlists")
    Call<ApiResponse<List<String>>> searchPlaylist(@Query("q") String query);
    @GET("/api/search-service/albums")
    Call<ApiResponse<List<String>>> searchAlbum(@Query("q") String query);
    @GET("/api/search-service/tracks")
    Call<ApiResponse<List<String>>> searchTrack(@Query("q") String query);
    @GET("/api/search-service/users")
    Call<ApiResponse<List<String>>> searchUser(@Query("q") String query);
    @GET("/api/user-library/playlist/bulk")
    Call<ApiResponse<List<PlaylistResponse>>> getPlaylistsByIds(@Query("playlist_ids") List<String> ids);
    @GET("/api/user-library/album/bulk")
    Call<ApiResponse<List<AlbumResponse>>> getAlbumsByIds(@Query("album_ids") List<String> ids);
    @GET("/api/profile/users/bulk")
    Call<ApiResponse<List<ProfileWithCountFollowResponse>>> getUserProfilesByIds(@Query("userIds") List<String> ids);
    @GET("/api/music-service/tracks/bulk")
    Call<ApiResponse<List<TrackResponse>>> getTracksByIds(@Query("ids") List<String> ids);
}
