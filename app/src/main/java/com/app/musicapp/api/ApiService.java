package com.app.musicapp.api;
import com.app.musicapp.model.Track;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ApiService {
    @GET("/api/tracks")
    Call<List<Track>> getTracks();
}
