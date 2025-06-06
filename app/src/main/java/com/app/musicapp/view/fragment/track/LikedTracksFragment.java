package com.app.musicapp.view.fragment.track;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
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

public class LikedTracksFragment extends Fragment {

    private ListView listViewLikedTracks;
    private TrackAdapter trackAdapter;
    private List<TrackResponse> likedTrackResponses;
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
        likedTrackResponses = new ArrayList<>();
        trackAdapter = new TrackAdapter(this, likedTrackResponses);
        listViewLikedTracks.setAdapter(trackAdapter);
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().startForegroundService(intent);
        }
        listViewLikedTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicService.setNextUpItems(likedTrackResponses);
                if (Objects.equals(musicService.getCurrentTrack().getId(), likedTrackResponses.get(i).getId())) {
                    musicService.playCurrentMusic();
                } else {
                    musicService.playMusicAtIndex(i);
                }
                Intent intent = new Intent(getContext(), MusicPlayer.class);
                startActivity(intent);
            }
        });
        // Gọi API lấy danh sách bài hát yêu thích
        loadLikedTracks();

        // Xử lý nút back
        ImageView ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            View mainView = requireActivity().findViewById(R.id.main);
            View viewPager = mainView.findViewById(R.id.view_pager);
            View fragmentContainer = mainView.findViewById(R.id.fragment_container);

            viewPager.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);

            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void loadLikedTracks() {
        ApiClient.getApiService().getLikedTrack().enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                likedTrackResponses.clear();
                likedTrackResponses.addAll(response.body().getData());
                trackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
