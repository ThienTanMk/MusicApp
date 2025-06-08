package com.app.musicapp.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.widget.SearchView;
import com.app.musicapp.R;
import com.app.musicapp.adapter.UploadsAdapter;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.app.musicapp.api.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.app.musicapp.R;

public class UploadsFragment extends Fragment implements SongOptionsBottomSheet.TrackOptionsListener {

    private ListView listViewUploads;
    private ImageView ivBack;
    private SearchView searchView;
    private UploadsAdapter uploadsAdapter;
    private List<TrackResponse> trackResponseList;
    private List<TrackResponse> filteredTrackResponseList;

    public UploadsFragment() {
        // Required empty public constructor
    }
    public static UploadsFragment newInstance() {
        return new UploadsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackResponseList = new ArrayList<>();
        filteredTrackResponseList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploads, container, false);

        listViewUploads = view.findViewById(R.id.listViewUploads);
        ivBack = view.findViewById(R.id.iv_back);
        searchView = view.findViewById(R.id.search_view);
        
        // Setup SearchView
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search in your uploads");
        
        // Style SearchView's EditText
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(Color.WHITE);
        searchEditText.setTextColor(Color.WHITE);

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

        // Setup other views
        FloatingActionButton fabUpload = view.findViewById(R.id.fab_upload);
        fabUpload.setOnClickListener(v -> showUploadFragment());
        
        uploadsAdapter = new UploadsAdapter(this, filteredTrackResponseList);
        listViewUploads.setAdapter(uploadsAdapter);

        setupBackButton();
        
        return view;
    }

    private void setupBackButton() {
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserTracks();
    }

    private void loadUserTracks() {
        String userId = SharedPreferencesManager.getInstance(requireContext()).getUserId();
        ApiClient.getTrackApiService()
                .getTracksByUserId(userId)
                .enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            trackResponseList.clear();
                            trackResponseList.addAll(response.body().getData());
                            filteredTrackResponseList.clear();
                            filteredTrackResponseList.addAll(trackResponseList);
                            uploadsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to load tracks", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error loading tracks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterTracks(String query) {
        filteredTrackResponseList.clear();
        
        if (query == null || query.trim().isEmpty()) {
            filteredTrackResponseList.addAll(trackResponseList);
        } else {
            String searchQuery = query.toLowerCase().trim();
            for (TrackResponse track : trackResponseList) {
                if (track.getTitle() != null && track.getTitle().toLowerCase().contains(searchQuery) ||
                    track.getDescription() != null && track.getDescription().toLowerCase().contains(searchQuery) ||
                    (track.getGenre() != null && track.getGenre().getName() != null && 
                     track.getGenre().getName().toLowerCase().contains(searchQuery))) {
                    filteredTrackResponseList.add(track);
                }
            }
        }
        
        if (uploadsAdapter != null) {
            uploadsAdapter.notifyDataSetChanged();
        }
    }

    private void showUploadFragment() {
        UploadTrackFragment uploadFragment = new UploadTrackFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, uploadFragment)
                .addToBackStack("UploadsFragment")
                .commit();
    }

    @Override
    public void onTrackDeleted(TrackResponse track) {
        loadUserTracks();
    }
}