package com.app.musicapp.view.fragment.track;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.service.MusicService;
import com.app.musicapp.view.activity.MusicPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedTracksFragment extends Fragment implements SongOptionsBottomSheet.TrackOptionsListener {

    private ListView listViewLikedTracks;
    private TrackAdapter trackAdapter;
    private List<TrackResponse> likedTrackResponses;
    private List<TrackResponse> filteredTrackResponses;
    private SearchView searchView;
    private MusicService musicService;
    public LikedTracksFragment() {}
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_tracks, container, false);

        listViewLikedTracks = view.findViewById(R.id.listViewLikedTracks);
        searchView = view.findViewById(R.id.search_view);
        ImageView ivBack = view.findViewById(R.id.iv_back);
        likedTrackResponses = new ArrayList<>();
        filteredTrackResponses = new ArrayList<>();
        
        // Setup back button
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                if (viewPager != null && fragmentContainer != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Setup SearchView
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search in your liked tracks");
        
        // Style SearchView's EditText
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(Color.parseColor("#BBBBBB"));
        searchEditText.setTextColor(Color.WHITE);

        // Change search icon color to white
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(Color.WHITE);

        // Change close icon color to white
        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeIcon != null) {
            closeIcon.setColorFilter(Color.WHITE);
        }


        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTracks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTracks(newText);
                return true;
            }
        });

        trackAdapter = new TrackAdapter(this, filteredTrackResponses);
        listViewLikedTracks.setAdapter(trackAdapter);
        
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().startForegroundService(intent);
        }
        
        listViewLikedTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicService.setNextUpItems(filteredTrackResponses);
                if (Objects.equals(musicService.getCurrentTrack().getId(), filteredTrackResponses.get(i).getId())) {
                    musicService.playCurrentMusic();
                } else {
                    musicService.playMusicAtIndex(i);
                }
                Intent intent = new Intent(getContext(), MusicPlayer.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLikedTracks();
    }

    private void loadLikedTracks() {
        ApiClient.getApiService().getLikedTrack().enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                likedTrackResponses.clear();
                likedTrackResponses.addAll(response.body().getData());
                filteredTrackResponses.clear();
                filteredTrackResponses.addAll(likedTrackResponses);
                trackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                System.out.println(t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void filterTracks(String query) {
        filteredTrackResponses.clear();
        
        if (query == null || query.trim().isEmpty()) {
            filteredTrackResponses.addAll(likedTrackResponses);
        } else {
            String searchQuery = query.toLowerCase().trim();
            for (TrackResponse track : likedTrackResponses) {
                if (track.getTitle() != null && track.getTitle().toLowerCase().contains(searchQuery) ||
                    track.getDescription() != null && track.getDescription().toLowerCase().contains(searchQuery) ||
                    (track.getGenre() != null && track.getGenre().getName() != null && 
                     track.getGenre().getName().toLowerCase().contains(searchQuery))) {
                    filteredTrackResponses.add(track);
                }
            }
        }
        
        if (trackAdapter != null) {
            trackAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTrackDeleted(TrackResponse track) {
        loadLikedTracks();
    }
}
