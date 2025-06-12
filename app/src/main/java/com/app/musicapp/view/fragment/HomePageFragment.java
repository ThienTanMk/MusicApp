package com.app.musicapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.album.AlbumRVAdapter;
import com.app.musicapp.adapter.playlist.PlayListRVAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.view.activity.SignIn;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.app.musicapp.view.fragment.track.LikedTracksFragment;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageFragment extends Fragment {
//    private ImageView ivUpload, ivNoti, ivRandom;
    private LinearLayout llYourLikes;
    private View progressBar;
    private RecyclerView rvPlaylist, rvPlaylistLike, rvAlbum;
    private List<PlaylistResponse> userPlaylists = new ArrayList<>();
    private List<PlaylistResponse> likedPlaylists = new ArrayList<>();
    private List<AlbumResponse> likedAlbumResponses = new ArrayList<>();
    private PlayListRVAdapter playListRVAdapter, likedPlaylistRVAdapter;
    private AlbumRVAdapter albumRVAdapter;
    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Ánh xạ view
//        ivUpload = view.findViewById(R.id.iv_upload);
//        ivNoti = view.findViewById(R.id.iv_noti);
//        ivRandom = view.findViewById(R.id.iv_random);
        llYourLikes = view.findViewById(R.id.ll_your_likes);
        rvPlaylist = view.findViewById(R.id.rv_playlist);
        rvPlaylistLike = view.findViewById(R.id.rv_playlist_like);
        rvAlbum = view.findViewById(R.id.rv_album);
        progressBar = view.findViewById(R.id.progressBar);


        // Thiết lập RecyclerView cho userPlaylists
        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        playListRVAdapter = new PlayListRVAdapter(userPlaylists, item -> {
            List<PlaylistResponse> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylist.setAdapter(playListRVAdapter);

        // Thiết lập RecyclerView cho likedPlaylists
        rvPlaylistLike.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        likedPlaylistRVAdapter = new PlayListRVAdapter(likedPlaylists, item -> {
            Toast.makeText(getContext(), "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            List<PlaylistResponse> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylistLike.setAdapter(likedPlaylistRVAdapter);

        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        albumRVAdapter = new AlbumRVAdapter(likedAlbumResponses, album -> {
            Toast.makeText(getContext(), "Clicked: " + album.getAlbumTitle(), Toast.LENGTH_SHORT).show();
            navigateToAlbumPage(album);
        });
        rvAlbum.setAdapter(albumRVAdapter);

        fetchCreatedPlaylists();
        fetchLikedPlaylists();
        fetchLikedAlbums();


        llYourLikes.setOnClickListener(v -> {
            navigateToFragment(new LikedTracksFragment());
        });

        return view;
    }
    private void fetchCreatedPlaylists() {
        progressBar.setVisibility(View.VISIBLE);
//        PlaylistApiService apiService = ApiClient.getPlaylistService();
//        Call<ApiResponse<List<PlaylistResponse>>> call = apiService.getCreatedPlaylists();
//        call.enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
        ApiClient.getPlaylistService().getCreatedPlaylists().enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Response<ApiResponse<List<PlaylistResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("HomePageFragment", "Created Playlists API Response: " + response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<PlaylistResponse>> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        List<PlaylistResponse> playlists = apiResponse.getData();
                        userPlaylists.clear();
                        if (playlists != null) {
                            userPlaylists.addAll(playlists.subList(0, Math.min(8, playlists.size())));
                        } else {
                            Log.d("HomePageFragment", "No created playlists returned");
                        }
                        playListRVAdapter.notifyDataSetChanged();
                    } else if (apiResponse.getCode() == 1401) {
                        String errorMsg = "Unauthorized: Please log in again";
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("HomePageFragment", errorMsg);
                        startActivity(new Intent(getContext(), SignIn.class));
                        requireActivity().finish();
                    } else {
                        String errorMsg = "API error: Code " + apiResponse.getCode() + ", Message: " + apiResponse.getMessage();
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("HomePageFragment", errorMsg);
                    }
                } else {
                    String errorMsg = "Failed to fetch created playlists, HTTP Code: " + response.code();
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("HomePageFragment", errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("HomePageFragment", "Network error: " + t.getMessage());
            }
        });
    }

    private void fetchLikedPlaylists() {
        progressBar.setVisibility(View.VISIBLE);
//        PlaylistApiService apiService = ApiClient.getPlaylistService();
//        Call<ApiResponse<List<PlaylistResponse>>> call = apiService.getLikedPlaylists();
//        call.enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
        ApiClient.getPlaylistService().getLikedPlaylists().enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Response<ApiResponse<List<PlaylistResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("HomePageFragment", "Liked Playlists API Response: " + response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<PlaylistResponse>> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        List<PlaylistResponse> playlists = apiResponse.getData();
                        likedPlaylists.clear();
                        if (playlists != null) {
                            likedPlaylists.addAll(playlists.subList(0, Math.min(8, playlists.size())));
                        } else {
                            Log.d("HomePageFragment", "No liked playlists returned");
                        }
                        likedPlaylistRVAdapter.notifyDataSetChanged();
                    } else if (apiResponse.getCode() == 1401) {
                        String errorMsg = "Unauthorized: Please log in again";
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("HomePageFragment", errorMsg);
                        startActivity(new Intent(getContext(), SignIn.class));
                        requireActivity().finish();
                    } else {
                        String errorMsg = "API error: Code " + apiResponse.getCode() + ", Message: " + apiResponse.getMessage();
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("HomePageFragment", errorMsg);
                    }
                } else {
                    String errorMsg = "Failed to fetch liked playlists, HTTP Code: " + response.code();
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("HomePageFragment", errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("HomePageFragment", "Network error: " + t.getMessage());
            }
        });
    }

    private void fetchLikedAlbums() {
        progressBar.setVisibility(View.VISIBLE);
        String userId = getCurrentUserId();
        if (userId == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Please log in.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(requireContext(), SignIn.class));
            requireActivity().finish();
            return;
        }
        Log.d("HomePageFragment", "Fetching liked albums for userId: " + userId);
        ApiClient.getAlbumService().getLikedAlbums(userId).enqueue(new Callback<ApiResponse<List<AlbumResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<AlbumResponse>>> call, @NonNull Response<ApiResponse<List<AlbumResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    likedAlbumResponses.clear();
                    List<AlbumResponse> albums = response.body().getData();
                    if (albums != null) {
                        likedAlbumResponses.addAll(albums.subList(0, Math.min(8, albums.size())));
                    }
                    albumRVAdapter.notifyDataSetChanged();
                } else if (response.body() != null && response.body().getCode() == 1401) {
                    Toast.makeText(requireContext(), "Unauthorized: Please log in again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(requireContext(), SignIn.class));
                    requireActivity().finish();
                } else {
                    String errorMsg = response.body() != null && response.body().getMessage() != null
                            ? response.body().getMessage()
                            : "Failed to fetch liked albums";
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("HomePageFragment", "Error: HTTP " + response.code() + ", Message: " + errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<AlbumResponse>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("HomePageFragment", "Network error: " + t.getMessage());
            }
        });
    }

    private String getCurrentUserId() {
        return SharedPreferencesManager.getInstance(requireContext()).getUserId();
    }
    private void navigateToFragment(Fragment fragment) {
        View container = requireActivity().findViewById(R.id.fragment_container);
        View viewPager = requireActivity().findViewById(R.id.view_pager);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
            if (viewPager != null) {
                viewPager.setVisibility(View.GONE);
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void navigateToPlaylistPage(List<PlaylistResponse> playlistList) {
        navigateToFragment(PlaylistPageFragment.newInstance(playlistList));
    }
    private void navigateToAlbumPage(AlbumResponse albumResponse) {
        navigateToFragment(AlbumPageFragment.newInstance(albumResponse));
    }

}