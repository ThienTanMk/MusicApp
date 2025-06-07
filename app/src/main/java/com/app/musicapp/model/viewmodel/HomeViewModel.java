package com.app.musicapp.model.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.musicapp.api.ApiClient;
import com.app.musicapp.api.ApiService;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;

import java.io.IOException;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<PlaylistResponse>> userPlaylists = new MutableLiveData<>();
    private MutableLiveData<List<LikedPlaylistResponse>> likedPlaylists = new MutableLiveData<>();
    private MutableLiveData<List<AlbumResponse>> likedAlbums = new MutableLiveData<>();
    private ApiService apiService;

    public HomeViewModel(Context context) {
        ApiClient.init(context);
        this.apiService = ApiClient.getApiService();
    }

    public LiveData<List<PlaylistResponse>> getUserPlaylists() {
        return userPlaylists;
    }

    public LiveData<List<LikedPlaylistResponse>> getLikedPlaylists() {
        return likedPlaylists;
    }

    public LiveData<List<AlbumResponse>> getLikedAlbums() {
        return likedAlbums;
    }

//    public void loadUserPlaylists() {
//        apiService.getAllPlaylist().enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<PlaylistResponse>>> call, Response<ApiResponse<List<PlaylistResponse>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<PlaylistResponse> data = response.body().getData();
//                    if (data != null) {
//                        userPlaylists.setValue(data);
//                        Log.d("HomeViewModel", "Loaded " + data.size() + " user playlists");
//                    } else {
//                        userPlaylists.setValue(new ArrayList<>());
//                        Log.e("HomeViewModel", "User playlists data is null");
//                    }
//                } else {
//                    userPlaylists.setValue(new ArrayList<>());
//                    Log.e("HomeViewModel", "Failed to load user playlists: HTTP " + response.code() + ", Message: " + response.message());
//                    if (response.errorBody() != null) {
//                        try {
//                            Log.e("HomeViewModel", "Error body: " + response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<PlaylistResponse>>> call, Throwable t) {
//                userPlaylists.setValue(new ArrayList<>());
//                Log.e("HomeViewModel", "Network error loading user playlists: " + t.getMessage());
//            }
//        });
//    }
//
//    public void loadLikedPlaylists() {
//        apiService.getAllLikedPlaylist().enqueue(new Callback<ApiResponse<List<LikedPlaylistResponse>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<LikedPlaylistResponse>>> call, Response<ApiResponse<List<LikedPlaylistResponse>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<LikedPlaylistResponse> data = response.body().getData();
//                    if (data != null) {
//                        likedPlaylists.setValue(data);
//                        Log.d("HomeViewModel", "Loaded " + data.size() + " liked playlists");
//                    } else {
//                        likedPlaylists.setValue(new ArrayList<>());
//                        Log.e("HomeViewModel", "Liked playlists data is null");
//                    }
//                } else {
//                    likedPlaylists.setValue(new ArrayList<>());
//                    Log.e("HomeViewModel", "Failed to load liked playlists: HTTP " + response.code() + ", Message: " + response.message());
//                    if (response.errorBody() != null) {
//                        try {
//                            Log.e("HomeViewModel", "Error body: " + response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<LikedPlaylistResponse>>> call, Throwable t) {
//                likedPlaylists.setValue(new ArrayList<>());
//                Log.e("HomeViewModel", "Network error loading liked playlists: " + t.getMessage());
//            }
//        });
//    }

//    public void loadLikedAlbums(String userId) {
//        apiService.getLikedAlbums(userId).enqueue(new Callback<ApiResponse<List<Album>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<Album>>> call, Response<ApiResponse<List<Album>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Album> data = response.body().getData();
//                    if (data != null) {
//                        likedAlbums.setValue(data);
//                        Log.d("HomeViewModel", "Loaded " + data.size() + " liked albums");
//                    } else {
//                        likedAlbums.setValue(new ArrayList<>());
//                        Log.e("HomeViewModel", "Liked albums data is null");
//                    }
//                } else {
//                    likedAlbums.setValue(new ArrayList<>());
//                    Log.e("HomeViewModel", "Failed to load liked albums: HTTP " + response.code() + ", Message: " + response.message());
//                    if (response.errorBody() != null) {
//                        try {
//                            Log.e("HomeViewModel", "Error body: " + response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<Album>>> call, Throwable t) {
//                likedAlbums.setValue(new ArrayList<>());
//                Log.e("HomeViewModel", "Network error loading liked albums: " + t.getMessage());
//            }
//        });
//    }
}
