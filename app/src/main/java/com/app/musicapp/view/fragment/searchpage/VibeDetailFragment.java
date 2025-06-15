package com.app.musicapp.view.fragment.searchpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.adapter.search.VibePagerAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.api.RecommendedApiService;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.videfragment.AlbumsTabFragment;
import com.app.musicapp.view.fragment.videfragment.AllTabFragment;
import com.app.musicapp.view.fragment.videfragment.PlaylistsTabFragment;
import com.app.musicapp.view.fragment.videfragment.TrendingTabFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VibeDetailFragment extends Fragment {
    private static final String ARG_VIBE_TITLE = "vibeTitle";
    private static final String ARG_IMAGE_RES_ID = "imageResId";
    private static final String TAG = "VibeDetailFragment";

    private String vibeTitle;
    private int imageResId;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private VibePagerAdapter adapter;
    private RecommendedApiService apiService;

    private List<TrackResponse> tracks = new ArrayList<>();
    private List<PlaylistResponse> playlists = new ArrayList<>();
    private List<AlbumResponse> albums = new ArrayList<>();

    public interface VibeTabNavigator {
        void navigateToTab(int position);
    }

    private VibeTabNavigator tabNavigator;

    public VibeDetailFragment() {
        // Required empty public constructor
    }

    public static VibeDetailFragment newInstance(String vibeTitle, int imageResId) {
        VibeDetailFragment fragment = new VibeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIBE_TITLE, vibeTitle);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vibeTitle = getArguments().getString(ARG_VIBE_TITLE);
            imageResId = getArguments().getInt(ARG_IMAGE_RES_ID);
        }
        try {
            apiService = ApiClient.getClient().create(RecommendedApiService.class);
            Log.d(TAG, "RecommendedApiService initialized successfully");
        } catch (IllegalStateException e) {
            Log.e(TAG, "ApiClient not initialized: " + e.getMessage());
            Toast.makeText(getContext(), "Lỗi khởi tạo API", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vibe_detail, container, false);

        ImageView ivBack = view.findViewById(R.id.ivBack);
        TextView tvVibeTitle = view.findViewById(R.id.vibeTitle);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tvVibeTitle.setText(vibeTitle);
        view.findViewById(R.id.rlBackground).setBackgroundResource(imageResId);

        adapter = new VibePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("All"); break;
                case 1: tab.setText("Trending"); break;
                case 2: tab.setText("Playlists"); break;
                case 3: tab.setText("Albums"); break;
            }
        }).attach();

        ivBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            View mainViewPager = requireActivity().findViewById(R.id.view_pager);
            View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
            if (mainViewPager != null && fragmentContainer != null) {
                mainViewPager.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
            }
        });
        fetchGenreId();

        return view;
    }

    public void setTabNavigator(VibeTabNavigator navigator) {
        this.tabNavigator = navigator;
    }

    public void navigateToTab(int position) {
        if (viewPager != null) {
            viewPager.setCurrentItem(position, true);
        }
    }

    private void fetchGenreId() {
        if (apiService == null) {
            Log.e(TAG, "apiService is null");
            Toast.makeText(getContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Fetching genres for vibeTitle=" + vibeTitle);
        // Ánh xạ vibeTitle sang genreId
        String genreId = mapVibeTitleToGenreId(vibeTitle);
        if (genreId == null) {
            Toast.makeText(getContext(), "Không tìm thấy thể loại", Toast.LENGTH_SHORT).show();
            return;
        }
        apiService.getGenreById(genreId).enqueue(new Callback<ApiResponse<GenreResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GenreResponse>> call, Response<ApiResponse<GenreResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    String actualGenreId = response.body().getData().getId();
                    fetchDataByGenre(actualGenreId);
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy thể loại";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "No genre found for vibeTitle=" + vibeTitle);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<GenreResponse>> call, Throwable t) {
                try{
                    Toast.makeText(getContext(), "Lỗi khi lấy thể loại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });
    }

    private String mapVibeTitleToGenreId(String vibeTitle) {
        // Ánh xạ tĩnh từ vibeTitle sang genreId
        switch (vibeTitle) {
            case "Hip Hop & Rap": return "1";
            case "Electronic": return "2";
            case "Ballad": return "3";
            case "Study": return "4";
            case "Pop": return "5";
            case "Chill": return "6";
            case "Indie": return "7";
            case "Rock": return "8";
            default: return null;
        }
    }

    private void fetchDataByGenre(String genreId) {
        apiService.getTracksByGenre(genreId).enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    tracks.clear();
                    tracks.addAll(response.body().getData());
                    updateAdapter();
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được bài hát";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "No track found for vibeTitle=" + vibeTitle);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                try{
                    Toast.makeText(getContext(), "Lỗi khi lấy bài hát: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });

        apiService.getPlaylistsByGenre(genreId).enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlaylistResponse>>> call, Response<ApiResponse<List<PlaylistResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    playlists.clear();
                    playlists.addAll(response.body().getData());
                    updateAdapter();
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được playlist";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "No playlist found for vibeTitle=" + vibeTitle);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlaylistResponse>>> call, Throwable t) {
                try{
                    Toast.makeText(getContext(), "Lỗi khi lấy playlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }
        });

        apiService.getAlbumsByGenre(genreId).enqueue(new Callback<ApiResponse<List<AlbumResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AlbumResponse>>> call, Response<ApiResponse<List<AlbumResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    albums.clear();
                    albums.addAll(response.body().getData());
                    updateAdapter();
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được album";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "No album found for vibeTitle=" + vibeTitle);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AlbumResponse>>> call, Throwable t) {
                try {
                    Toast.makeText(getContext(), "Lỗi khi lấy album: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }
        });
    }

    private void updateAdapter() {
        Fragment allFragment = getChildFragmentManager().findFragmentByTag("f0"); // Tab 0: All
        if (allFragment instanceof AllTabFragment) {
            ((AllTabFragment) allFragment).updateData(tracks, playlists, albums);
        }

        Fragment playlistsFragment = getChildFragmentManager().findFragmentByTag("f2"); // Tab 2: Playlists
        if (playlistsFragment instanceof PlaylistsTabFragment) {
            ((PlaylistsTabFragment) playlistsFragment).updateData(playlists);
        }

        Fragment albumsFragment = getChildFragmentManager().findFragmentByTag("f3"); // Tab 3: Albums
        if (albumsFragment instanceof AlbumsTabFragment) {
            ((AlbumsTabFragment) albumsFragment).updateData(albums);
        }
    }

    public List<TrackResponse> getTracks() {
        return tracks;
    }

    public List<PlaylistResponse> getPlaylists() {
        return playlists;
    }

    public List<AlbumResponse> getAlbums() {
        return albums;
    }
}