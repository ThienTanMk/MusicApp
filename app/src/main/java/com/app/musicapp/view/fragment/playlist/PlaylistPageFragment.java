package com.app.musicapp.view.fragment.playlist;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackRVAdapter;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaylistPageFragment extends Fragment {
    private List<PlaylistResponse> playlist;

    public static PlaylistPageFragment newInstance(List<PlaylistResponse> playlist) {
        PlaylistPageFragment fragment = new PlaylistPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("playlist", (java.io.Serializable) playlist);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlist = (List<PlaylistResponse>) getArguments().getSerializable("playlist");
        }
    }
    public PlaylistPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_page, container, false);

        ImageView ivBack = view.findViewById(R.id.iv_back);
        TextView tvPlaylistTitleHeader = view.findViewById(R.id.tv_playlist_title_header);
        ImageView ivPlaylistCover = view.findViewById(R.id.iv_playlist_cover);
        TextView tvPlaylistTitle = view.findViewById(R.id.tv_playlist_title);
        TextView tvPlaylistArtists = view.findViewById(R.id.tv_playlist_artist);
        TextView tvPlaylistType = view.findViewById(R.id.tv_playlist_type);
        TextView tvCreatedAt = view.findViewById(R.id.tv_created_at);
        TextView tvNumOfTracks = view.findViewById(R.id.tv_num_of_tracks);
        TextView tvTotalDuration = view.findViewById(R.id.tv_total_duration);
        ImageView ivLike = view.findViewById(R.id.iv_like);
        TextView tvLikeCount = view.findViewById(R.id.tv_like_count);
        ImageView ivMenu = view.findViewById(R.id.iv_menu);
        ImageView ivPlay = view.findViewById(R.id.iv_play);
        TextView tvDescription = view.findViewById(R.id.tv_description);
        TextView tvShowMore = view.findViewById(R.id.tv_show_more);
        RecyclerView rvTracks = view.findViewById(R.id.rv_tracks);

        final PlaylistResponse playlistResponseData = (playlist != null && !playlist.isEmpty()) ? playlist.get(0) : null;

        if (playlistResponseData != null) {
            tvPlaylistTitleHeader.setText("Playlist " + (playlistResponseData.getCreatedAt() != null ? playlistResponseData.getCreatedAt().getYear() : "Unknown"));
            tvPlaylistTitle.setText(playlistResponseData.getTitle() != null ? playlistResponseData.getTitle() : "Untitled");
            tvPlaylistArtists.setText(playlistResponseData.getUserId() != null ? playlistResponseData.getUserId() : "Unknown User");

            Glide.with(ivPlaylistCover.getContext())
                    .load(playlistResponseData.getImagePath() != null ? playlistResponseData.getImagePath() : R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .into(ivPlaylistCover);

            tvPlaylistType.setText("Playlist");
            tvCreatedAt.setText(playlistResponseData.getCreatedAt() != null ? " · " + playlistResponseData.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")) : " · Unknown");
            int trackCount = playlistResponseData.getPlaylistTracks() != null ? playlistResponseData.getPlaylistTracks().size() : 0;
            tvNumOfTracks.setText(" · " + trackCount + " Tracks");
            long totalDurationSeconds = calculateTotalDuration(playlistResponseData.getPlaylistTracks());
            String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
            tvTotalDuration.setText(" · " + duration);

            tvLikeCount.setText("210");
            tvDescription.setText(playlistResponseData.getDescription() != null ? playlistResponseData.getDescription() : "No description");

            rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
            TrackRVAdapter trackAdapter = new TrackRVAdapter(this, playlistResponseData.getPlaylistTracks() != null ?
                        playlistResponseData.getPlaylistTracks() : new ArrayList<>());
            rvTracks.setAdapter(trackAdapter);
        } else {
            Log.w("PlaylistPageFragment", "No playlist data available");
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked: " + (playlist != null && !playlist.isEmpty() ? ((PlaylistResponse) playlist.get(0)).getTitle() : "Unknown"), Toast.LENGTH_SHORT).show();
        });
        ivMenu.setOnClickListener(v -> {
            if (playlistResponseData != null) {
                PlaylistOptionsBottomSheet bottomSheet = PlaylistOptionsBottomSheet.newInstance(playlistResponseData);
                bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
            } else {
                Log.w("PlaylistPageFragment", "Cannot open bottom sheet: playlistResponseData is null");
                Toast.makeText(getContext(), "No playlist data available", Toast.LENGTH_SHORT).show();
            }
        });
        ivPlay.setOnClickListener(v -> {
            // Logic phát playlist
            Toast.makeText(getContext(), "Playing: " + (playlist != null && !playlist.isEmpty() ? ((PlaylistResponse) playlist.get(0)).getTitle() : "Unknown"), Toast.LENGTH_SHORT).show();
        });
        tvShowMore.setOnClickListener(v -> {
            // Mở rộng description
            Toast.makeText(getContext(), "Show more clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private long calculateTotalDuration(List<TrackResponse> trackResponses) {
        long totalSeconds = 0;
        if (trackResponses != null) {
            for (TrackResponse trackResponse : trackResponses) {
                String[] timeParts = trackResponse.getDuration().split(":");
                if (timeParts.length == 2) {
                    totalSeconds += Integer.parseInt(timeParts[0]) * 60 + Integer.parseInt(timeParts[1]);
                }
            }
        }
        return totalSeconds;
    }

}