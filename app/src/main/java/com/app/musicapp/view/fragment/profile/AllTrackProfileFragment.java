package com.app.musicapp.view.fragment.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTrackProfileFragment extends Fragment {
    private List<TrackResponse> trackResponse;
    private ImageView ivBack;
    private ImageButton btnPlay, btnShuffle;
    private TrackAdapter trackAdapter;
    private ListView lvTrack;
    private String userId;

    public AllTrackProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_track_profile, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            Log.d("AllTrackProfileFragment", "Received userId: " + userId);
        }

        init(view);
        loadData();

        return view;
    }
    private void init(View view){
        ivBack = view.findViewById(R.id.iv_back);
        btnPlay = view.findViewById(R.id.btn_play);
        btnShuffle = view.findViewById(R.id.btn_shuffle);
        lvTrack = view.findViewById(R.id.lvTrackProfile);

        trackResponse = new ArrayList<>();
        trackAdapter = new TrackAdapter(this, trackResponse);
        lvTrack.setAdapter(trackAdapter);
        Log.d("AllTrackProfileFragment", "Adapter set to ListView");

        ivBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnPlay.setOnClickListener(v -> {
            // TODO: Thêm logic phát nhạc
        });

        btnShuffle.setOnClickListener(v -> {
            // TODO: Thêm logic phát ngẫu nhiên
        });
    }
    private void loadData() {
        if (userId == null) {
            Log.e("AllTrackProfileFragment", "userId is null");
            return;
        }

        Log.d("AllTrackProfileFragment", "Loading tracks for userId: " + userId);
        ApiClient.getTrackApiService().getTracksByUserId(userId).enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trackResponse.clear();
                    trackResponse.addAll(response.body().getData());
                    Log.d("AllTrackProfileFragment", "Tracks loaded: " + trackResponse.size());
                    trackAdapter.updateTracks(trackResponse);
                } else {
                    Log.e("AllTrackProfileFragment", "Failed to load tracks: " + response.message() + ", code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                Log.e("AllTrackProfileFragment", "Error loading tracks: " + t.getMessage());
            }
        });
    }
}