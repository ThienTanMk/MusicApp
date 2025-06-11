package com.app.musicapp.view.fragment.playlist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.playlist.TrackToPlaylistAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.request.UpdatePlaylistInfoRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTrackToPlaylistFragment extends Fragment {

    private static final String TAG = "AddTrackToPlaylist";
    private EditText etSearch;
    private RecyclerView rvPlaylists;
    private TrackToPlaylistAdapter adapter;
    private List<PlaylistResponse> playlists;
    private TrackResponse trackToAdd;
    private PlaylistResponse selectedPlaylist;


    public static AddTrackToPlaylistFragment newInstance(TrackResponse track) {
        AddTrackToPlaylistFragment fragment = new AddTrackToPlaylistFragment();
        Bundle args = new Bundle();
        args.putSerializable("track", track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackToAdd = (TrackResponse) getArguments().getSerializable("track");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_track_to_playlist, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadPlaylists();
        
        return view;
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.et_search);
        rvPlaylists = view.findViewById(R.id.rv_playlists);
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> saveSelectedPlaylists());
    }

    private void setupRecyclerView() {
        playlists = new ArrayList<>();
        adapter = new TrackToPlaylistAdapter(requireContext(), playlists, trackToAdd);
        adapter.setOnPlaylistSelectedListener(playlist -> {
            selectedPlaylist = playlist;
        });
        
        rvPlaylists.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPlaylists.setAdapter(adapter);
    }

    private void setupListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlaylists(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadPlaylists() {
        String userId = SharedPreferencesManager.getInstance(requireContext()).getUserId();
        ApiClient.getPlaylistService().getPlaylistsByUserId(userId).enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Response<ApiResponse<List<PlaylistResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<PlaylistResponse> newPlaylists = response.body().getData();
                    Log.d(TAG, "Loaded " + newPlaylists.size() + " playlists");
                    playlists.clear();
                    playlists.addAll(newPlaylists);
                    adapter.updateData(newPlaylists);
                } else {
                    Toast.makeText(requireContext(), "Failed to load playlists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterPlaylists(String query) {
        if (query.isEmpty()) {
            adapter.updateData(playlists);
            return;
        }

        List<PlaylistResponse> filteredList = new ArrayList<>();
        for (PlaylistResponse playlist : playlists) {
            if (playlist.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(playlist);
            }
        }
        adapter.updateData(filteredList);
    }

    private void saveSelectedPlaylists() {
        List<PlaylistResponse> selectedPlaylists = adapter.getSelectedPlaylists();
        List<PlaylistResponse> allPlaylists = new ArrayList<>(playlists);
        AtomicInteger totalOperations = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        // Handle both selected and unselected playlists
        for (PlaylistResponse playlist : allPlaylists) {
            boolean isSelected = selectedPlaylists.stream()
                .anyMatch(selected -> selected.getId().equals(playlist.getId()));
            
            // Initialize track lists if null
            if (playlist.getPlaylistTracks() == null) {
                playlist.setPlaylistTracks(new ArrayList<>());
            }
            if (playlist.getPlaylistTrackResponses() == null) {
                playlist.setPlaylistTrackResponses(new ArrayList<>());
            }

            // Check if track exists in either list
            boolean trackExists = adapter.checkIfTrackExists(playlist);
            
            // Only update if there's a change needed
            boolean needsUpdate = false;
            
            if (isSelected && !trackExists) {
                // Add track if selected and not exists
                playlist.getPlaylistTracks().add(trackToAdd);
                needsUpdate = true;
                Log.d(TAG, "Adding track to playlist: " + playlist.getId());
            } else if (!isSelected && trackExists) {
                // Remove track if not selected but exists
                playlist.getPlaylistTracks().removeIf(track -> 
                    track != null && track.getId() != null && 
                    track.getId().equals(trackToAdd.getId()));
                playlist.getPlaylistTrackResponses().removeIf(track -> 
                    track != null && track.getId() != null && 
                    track.getId().equals(trackToAdd.getId()));
                needsUpdate = true;
                Log.d(TAG, "Removing track from playlist: " + playlist.getId());
            }

            if (!needsUpdate) {
                Log.d(TAG, "No changes needed for playlist: " + playlist.getId());
                continue;
            }

            totalOperations.incrementAndGet();

            // Create UpdatePlaylistInfoRequest
            UpdatePlaylistInfoRequest request = new UpdatePlaylistInfoRequest(
                playlist.getTitle(),
                playlist.getPrivacy(),
                playlist.getPlaylistTracks().stream()
                    .filter(track -> track != null && track.getId() != null)
                    .map(TrackResponse::getId)
                    .collect(Collectors.toList()),
                playlist.getId(),
                playlist.getReleaseDate(),
                playlist.getDescription(),
                playlist.getGenre() != null ? playlist.getGenre().getId() : null,
                playlist.getPlaylistTags() != null ? 
                    playlist.getPlaylistTags().stream()
                        .map(tag -> tag.getId())
                        .collect(Collectors.toList()) : 
                    new ArrayList<>()
            );

            // Convert request to JSON using GsonBuilder with LocalDateTimeAdapter
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
            String jsonRequest = gson.toJson(request);
            Log.d(TAG, "Request JSON: " + jsonRequest);
            RequestBody playlistBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest);

            // Make API call
            Log.d(TAG, "Making API call to update playlist: " + playlist.getId());
            ApiClient.getPlaylistService().updatePlaylistInfoV1(null, playlistBody).enqueue(new Callback<ApiResponse<PlaylistResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Response<ApiResponse<PlaylistResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (successCount.incrementAndGet() == totalOperations.get()) {
                            Toast.makeText(requireContext(), "Successfully updated playlists", Toast.LENGTH_SHORT).show();
                            requireActivity().onBackPressed();
                        }
                    } else {
                        Log.e(TAG, "API call failed for playlist: " + playlist.getId());
                        Log.e(TAG, "Error code: " + response.code());
                        if (response.errorBody() != null) {
                            try {
                                Log.e(TAG, "Error body: " + response.errorBody().string());
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }
                        }
                        Toast.makeText(requireContext(), "Failed to update playlist: " + playlist.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Network error for playlist: " + playlist.getId(), t);
                    Log.e(TAG, "Error message: " + t.getMessage());
                    Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (totalOperations.get() == 0) {
            Toast.makeText(requireContext(), "No changes to save", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }
}