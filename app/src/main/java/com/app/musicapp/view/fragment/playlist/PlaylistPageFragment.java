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
import com.app.musicapp.model.LikedPlaylist;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.model.Track;
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
            Playlist playlistData;
            if (item instanceof Playlist) {
                playlistData = (Playlist) item;
            } else if (item instanceof LikedPlaylist) {
                playlistData = ((LikedPlaylist) item).getPlaylist();
            } else {
                playlistData = null;
            }

            if (playlistData != null) {
                tvPlaylistTitleHeader.setText("Playlist " + playlistData.getCreatedAt().getYear());
                tvPlaylistTitle.setText(playlistData.getTitle());
                tvPlaylistArtists.setText(playlistData.getUserId());

                Glide.with(ivPlaylistCover.getContext())
                        .load(playlistData.getImagePath())
                        .placeholder(R.drawable.logo)
                        .into(ivPlaylistCover);

                tvPlaylistType.setText("Playlist");
                tvCreatedAt.setText(" · " + playlistData.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")));
                int trackCount = playlistData.getPlaylistTracks() != null ? playlistData.getPlaylistTracks().size() : 0;
                tvNumOfTracks.setText(" · " + trackCount + " Tracks");
                long totalDurationSeconds = calculateTotalDuration(playlistData.getPlaylistTracks());
                String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
                tvTotalDuration.setText(" · " + duration);

                tvLikeCount.setText("210");
                tvDescription.setText(playlistData.getDescription() != null ? playlistData.getDescription() : "No description");

                rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
                TrackRVAdapter trackAdapter = new TrackRVAdapter(this, playlistData.getPlaylistTracks() != null ? playlistData.getPlaylistTracks() : new ArrayList<>());
                rvTracks.setAdapter(trackAdapter);
            }
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked: " + (playlist != null && !playlist.isEmpty() ? ((Playlist) playlist.get(0)).getTitle() : "Unknown"), Toast.LENGTH_SHORT).show();
        });
        ivMenu.setOnClickListener(v -> {
            // Mở bottom sheet theo logic của PlaylistAdapter
            if (playlist != null && !playlist.isEmpty()) {
                Object item = playlist.get(0);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                View bottomSheetView;
                if (item instanceof Playlist) {
                    bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_user_playlist, null);
                } else {
                    bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_liked_playlist_options, null);
                }

                // Ánh xạ các view trong bottom sheet
                ImageView ivPlaylistImageSheet = bottomSheetView.findViewById(R.id.iv_playlist_image);
                TextView tvPlaylistTitleSheet = bottomSheetView.findViewById(R.id.tv_playlist_title);
                TextView tvPlaylistDescriptionSheet = bottomSheetView.findViewById(R.id.tv_user_playlist);

                Playlist playlistData = (item instanceof Playlist) ? (Playlist) item : ((LikedPlaylist) item).getPlaylist();
                tvPlaylistTitleSheet.setText(playlistData.getTitle());
                tvPlaylistDescriptionSheet.setText(playlistData.getDescription());
                Glide.with(ivPlaylistImageSheet.getContext())
                        .load(playlistData.getImagePath())
                        .placeholder(R.drawable.logo)
                        .into(ivPlaylistImageSheet);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        ivPlay.setOnClickListener(v -> {
            // Logic phát playlist
            Toast.makeText(getContext(), "Playing: " + (playlist != null && !playlist.isEmpty() ? ((Playlist) playlist.get(0)).getTitle() : "Unknown"), Toast.LENGTH_SHORT).show();
        });
        tvShowMore.setOnClickListener(v -> {
            // Mở rộng description
            Toast.makeText(getContext(), "Show more clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private long calculateTotalDuration(List<Track> tracks) {
        long totalSeconds = 0;
        if (tracks != null) {
            for (Track track : tracks) {
                String[] timeParts = track.getDuration().split(":");
                if (timeParts.length == 2) {
                    totalSeconds += Integer.parseInt(timeParts[0]) * 60 + Integer.parseInt(timeParts[1]);
                }
            }
        }
        return totalSeconds;
    }
}