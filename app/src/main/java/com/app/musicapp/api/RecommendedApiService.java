package com.app.musicapp.api;

import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecommendedApiService {
    //lấy id genre tương ứng khi click vào genre
    @GET("api/music-service/genres/{id}")
    Call<ApiResponse<GenreResponse>> getGenreById(@Path("id") String id);
    //lấy track theo id genre đã lấy ở trên
    @GET("/api/user-library/recommended/{genre_id}/trending/tracks")
    Call<ApiResponse<List<TrackResponse>>> getTracksByGenre(
            @Path("genre_id") String genreId
    );
    //lấy album theo id genre đã lấy ở trên
    @GET("/api/user-library/album/{genre_id}/bulk")
    Call<ApiResponse<List<AlbumResponse>>> getAlbumsByGenre(
        @Path("genre_id") String genreId
    );
    //lấy playlist theo id genre đã lấy ở trên
    @GET("/api/user-library/playlist/{genre_id}/bulk")
    Call<ApiResponse<List<PlaylistResponse>>> getPlaylistsByGenre(
            @Path("genre_id") String genreId
    );
}
