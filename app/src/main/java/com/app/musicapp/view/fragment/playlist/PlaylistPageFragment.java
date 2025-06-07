package com.app.musicapp.view.fragment.playlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Object> playlist; // Danh sách chứa cả Playlist và LikedPlaylist

    public static PlaylistPageFragment newInstance(List<Object> playlist) {
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
            playlist = (List<Object>) getArguments().getSerializable("playlist");
        }
    }
    public PlaylistPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist_page, container, false);

        // Ánh xạ view
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

        // Đặt dữ liệu lấy playlist đầu tiên nếu danh sách không rỗng
        if (playlist != null && !playlist.isEmpty()) {
            Object item = playlist.get(0);
            PlaylistResponse playlistResponseData;
            if (item instanceof PlaylistResponse) {
                playlistResponseData = (PlaylistResponse) item;
            } else if (item instanceof LikedPlaylistResponse) {
                playlistResponseData = ((LikedPlaylistResponse) item).getPlaylist();
            } else {
                playlistResponseData = null;
            }

            if (playlistResponseData != null) {
                tvPlaylistTitleHeader.setText("Playlist " + playlistResponseData.getCreatedAt().getYear());
                tvPlaylistTitle.setText(playlistResponseData.getTitle());
                tvPlaylistArtists.setText(playlistResponseData.getUserId());

                Glide.with(ivPlaylistCover.getContext())
                        .load(playlistResponseData.getImagePath())
                        .placeholder(R.drawable.logo)
                        .into(ivPlaylistCover);

                tvPlaylistType.setText("Playlist");
                tvCreatedAt.setText(" · " + playlistResponseData.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")));
                int trackCount = playlistResponseData.getPlaylistTracks() != null ? playlistResponseData.getPlaylistTracks().size() : 0;
                tvNumOfTracks.setText(" · " + trackCount + " Tracks");
                long totalDurationSeconds = calculateTotalDuration(playlistResponseData.getPlaylistTracks());
                String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
                tvTotalDuration.setText(" · " + duration);

                tvLikeCount.setText("210");
                tvDescription.setText(playlistResponseData.getDescription() != null ? playlistResponseData.getDescription() : "No description");

                rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
                TrackRVAdapter trackAdapter = new TrackRVAdapter(this, playlistResponseData.getPlaylistTracks() != null ? playlistResponseData.getPlaylistTracks() : new ArrayList<>());
                rvTracks.setAdapter(trackAdapter);
            }
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked: " + (playlist != null && !playlist.isEmpty() ? ((PlaylistResponse) playlist.get(0)).getTitle() : "Unknown"), Toast.LENGTH_SHORT).show();
        });
        ivMenu.setOnClickListener(v -> {
            // Mở bottom sheet theo logic của PlaylistAdapter
            if (playlist != null && !playlist.isEmpty()) {
                Object item = playlist.get(0);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                View bottomSheetView;
                if (item instanceof PlaylistResponse) {
                    bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_user_playlist, null);
                } else {
                    bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_liked_playlist_options, null);
                }

                // Ánh xạ các view trong bottom sheet
                ImageView ivPlaylistImageSheet = bottomSheetView.findViewById(R.id.iv_playlist_image);
                TextView tvPlaylistTitleSheet = bottomSheetView.findViewById(R.id.tv_playlist_title);
                TextView tvPlaylistDescriptionSheet = bottomSheetView.findViewById(R.id.tv_user_playlist);

                PlaylistResponse playlistResponseData = (item instanceof PlaylistResponse) ? (PlaylistResponse) item : ((LikedPlaylistResponse) item).getPlaylist();
                tvPlaylistTitleSheet.setText(playlistResponseData.getTitle());
                tvPlaylistDescriptionSheet.setText(playlistResponseData.getDescription());
                Glide.with(ivPlaylistImageSheet.getContext())
                        .load(playlistResponseData.getImagePath())
                        .placeholder(R.drawable.logo)
                        .into(ivPlaylistImageSheet);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
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