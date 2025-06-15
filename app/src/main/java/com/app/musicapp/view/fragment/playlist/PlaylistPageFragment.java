package com.app.musicapp.view.fragment.playlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackRVAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.interfaces.OnLikeChangeListener;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.activity.SignIn;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;
import com.bumptech.glide.Glide;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class PlaylistPageFragment extends Fragment implements OnLikeChangeListener, SongOptionsBottomSheet.TrackOptionsListener {
    private List<PlaylistResponse> playlist;
    private PlaylistResponse playlistResponseData;
    private ImageView ivLike;
    private TextView tvLikeCount;
    private TrackRVAdapter trackAdapter;
    private List<TrackResponse> tracks;
    private RecyclerView rvTracks;
    private TextView tvNumOfTracks;
    private TextView tvTotalDuration;

    public static PlaylistPageFragment newInstance(List<PlaylistResponse> playlist) {
        PlaylistPageFragment fragment = new PlaylistPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("playlist", (java.io.Serializable) playlist);
        fragment.setArguments(args);
        return fragment;
    }
    public static PlaylistPageFragment newInstance(PlaylistResponse playlist) {
        PlaylistPageFragment fragment = new PlaylistPageFragment();
        Bundle args = new Bundle();
        ArrayList<PlaylistResponse> playlistList = new ArrayList<>();
        playlistList.add(playlist);
        args.putSerializable("playlist", playlistList);
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
        tvNumOfTracks = view.findViewById(R.id.tv_num_of_tracks);
        tvTotalDuration = view.findViewById(R.id.tv_total_duration);
        ivLike = view.findViewById(R.id.iv_like);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        ImageView ivMenu = view.findViewById(R.id.iv_menu);
        TextView tvDescription = view.findViewById(R.id.tv_description);
        rvTracks = view.findViewById(R.id.rv_tracks);


        playlistResponseData = (playlist != null && !playlist.isEmpty()) ? playlist.get(0) : null;

        if (playlistResponseData != null) {
            tvPlaylistTitleHeader.setText("Playlist " + (playlistResponseData.getCreatedAt() != null ? playlistResponseData.getCreatedAt().getYear() : "Unknown"));
            tvPlaylistTitle.setText(playlistResponseData.getTitle() != null ? playlistResponseData.getTitle() : "Untitled");
            tvPlaylistArtists.setText(playlistResponseData.getUser().getDisplayName() != null ? playlistResponseData.getUser().getDisplayName()  : "Unknown User");

            Glide.with(getContext())
                    .load(playlistResponseData.getImagePath() != null ? UrlHelper.getCoverImageUrl( playlistResponseData.getImagePath()) : R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .into(ivPlaylistCover);

            tvPlaylistType.setText("Playlist");
            tvCreatedAt.setText(playlistResponseData.getCreatedAt() != null ? " · " + playlistResponseData.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")) : " · Unknown");
            int trackCount = playlistResponseData.getPlaylistTracks() != null ? playlistResponseData.getPlaylistTracks().size() : 0;
            tvNumOfTracks.setText(" · " + trackCount + " Tracks");
            long totalDurationSeconds = calculateTotalDuration(playlistResponseData.getPlaylistTracks());
            String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
            tvTotalDuration.setText(" · " + duration);

            tvDescription.setText(playlistResponseData.getDescription() != null ? playlistResponseData.getDescription() : "No description");

            rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
            setupRecyclerView();

            if (playlistResponseData.getIsLiked() != null && playlistResponseData.getIsLiked()) {
                ivLike.setVisibility(View.VISIBLE);
                tvLikeCount.setVisibility(View.VISIBLE);
                updateLikeUI();
                fetchLikeCount();
            } else {
                ivLike.setVisibility(View.GONE);
                tvLikeCount.setVisibility(View.GONE);
            }
        } else {
            Log.w("PlaylistPageFragment", "No playlist data available");
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> toggleLikeStatus());
        ivMenu.setOnClickListener(v -> {
            if (playlistResponseData != null) {
                PlaylistOptionsBottomSheet bottomSheet = PlaylistOptionsBottomSheet.newInstance(playlistResponseData);
                bottomSheet.setTargetFragment(this, 0);
                bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
            } else {
                Log.w("PlaylistPageFragment", "Cannot open bottom sheet: playlistResponseData is null");
                Toast.makeText(getContext(), "No playlist data available", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void setupRecyclerView() {
        tracks = playlistResponseData.getPlaylistTracks() != null ? 
            new ArrayList<>(playlistResponseData.getPlaylistTracks()) : 
            new ArrayList<>();
            
        trackAdapter = new TrackRVAdapter(this, tracks);
        trackAdapter.setOnTrackOptionsClickListener(track -> {
            SongOptionsBottomSheet bottomSheet = SongOptionsBottomSheet.newInstance(track, playlistResponseData);
            bottomSheet.setTrackOptionsListener(this);
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        });
        rvTracks.setAdapter(trackAdapter);
    }
    private void updateLikeUI() {
        if (playlistResponseData != null && ivLike != null ) {
            boolean isLiked = playlistResponseData.getIsLiked() != null && playlistResponseData.getIsLiked();
            ivLike.setImageResource(R.drawable.ic_favorite);
            ivLike.setColorFilter(getResources().getColor(isLiked ? R.color.like_active : R.color.like_inactive));
        }
    }
    private void fetchLikeCount() {
        if (playlistResponseData == null || playlistResponseData.getId() == null || playlistResponseData.getIsLiked() == null || !playlistResponseData.getIsLiked()) {
            Log.e("PlaylistPageFragment", "fetchLikeCount: Invalid playlist or null ID");
            tvLikeCount.setVisibility(View.GONE);
            return;
        }

        Log.d("PlaylistPageFragment", "Fetching like count for playlistId=" + playlistResponseData.getId());
        ApiClient.getLikedPlaylistService().getLikedCount(playlistResponseData.getId()).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Integer>> call, @NonNull Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Integer likeCount = response.body().getData();
                    tvLikeCount.setText(String.valueOf(likeCount != null ? likeCount : 0));
                    Log.d("PlaylistPageFragment", "Like count fetched: " + likeCount);
                } else if (response.body() != null && response.body().getCode() == 1401) {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), SignIn.class));
                    requireActivity().finish();
                } else {
                    Log.e("PlaylistPageFragment", "Failed to fetch like count: " + (response.body() != null ? response.body().getMessage() : "Unknown"));
                    tvLikeCount.setText("0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Integer>> call, @NonNull Throwable t) {
                Log.e("PlaylistPageFragment", "Network error fetching like count: " + t.getMessage());
                tvLikeCount.setText("0");
                try {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }
    private void toggleLikeStatus() {
        if (playlistResponseData == null || playlistResponseData.getId() == null) {
            Log.e("PlaylistPageFragment", "toggleLikeStatus: Invalid playlist or null ID");
            Toast.makeText(getContext(), "Cannot perform action", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCurrentlyLiked = playlistResponseData.getIsLiked() != null && playlistResponseData.getIsLiked();
        Call<ApiResponse<Boolean>> call = isCurrentlyLiked ?
                ApiClient.getLikedPlaylistService().unlikePlaylist(playlistResponseData.getId()) :
                ApiClient.getLikedPlaylistService().likePlaylist(playlistResponseData.getId());

        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    boolean newIsLiked = !isCurrentlyLiked;
                    playlistResponseData.setIsLiked(newIsLiked);
                    updateLikeUI();
                    fetchLikeCount(); // Cập nhật số lượt thích sau khi like/unlike
                    Toast.makeText(getContext(), newIsLiked ? "Đã thích playlist" : "Đã bỏ thích", Toast.LENGTH_SHORT).show();

                    // Thông báo cho PlaylistsFragment
                    Intent intent = new Intent("PLAYLIST_LIKE_STATUS_CHANGED");
                    intent.putExtra("playlistId", playlistResponseData.getId());
                    intent.putExtra("isLiked", newIsLiked);
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
                } else if (response.body() != null && response.body().getCode() == 1401) {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), SignIn.class));
                    requireActivity().finish();
                } else {
                    Log.e("PlaylistPageFragment", "API error: " + (response.body() != null ? response.body().getMessage() : "Unknown"));
                    Toast.makeText(getContext(), "Cannot toggle like status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Throwable t) {
                Log.e("PlaylistPageFragment", "Network error: " + t.getMessage());
               try {
                   Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
               } catch (Exception e) {
               }
            }
        });
    }
    private long calculateTotalDuration(List<TrackResponse> trackResponses) {
        long totalSeconds = 0;
        try {
            if (trackResponses != null) {
                for (TrackResponse trackResponse : trackResponses) {
                    if(trackResponse.getDuration()==null)continue;;
                    String[] timeParts = trackResponse.getDuration().split(":");
                    if (timeParts.length == 2) {
                        totalSeconds += Integer.parseInt(timeParts[0]) * 60 + Integer.parseInt(timeParts[1]);
                    }
                }
            }
        }
        catch (Exception ex){

        }
        return totalSeconds;
    }

    @Override
    public void onLikeChanged(String id, boolean isLiked) {
        if (playlistResponseData != null && playlistResponseData.getId().equals(id)) {
            playlistResponseData.setIsLiked(isLiked);
            updateLikeUI();
            fetchLikeCount(); // Cập nhật số lượt thích sau callback

            // Thông báo cho PlaylistsFragment
            Intent intent = new Intent("PLAYLIST_LIKE_STATUS_CHANGED");
            intent.putExtra("playlistId", id);
            intent.putExtra("isLiked", isLiked);
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onTrackRemovedFromPlaylist(TrackResponse track) {
        // Remove track from adapter with animation
        trackAdapter.removeTrack(track);
        
        // Update local data
        tracks.remove(track);
        playlistResponseData.setPlaylistTracks(tracks);
        
        // Update playlist info UI
        updatePlaylistInfo();
    }

    private void updatePlaylistInfo() {
        if (tvNumOfTracks == null || tvTotalDuration == null) return;

        // Update track count
        int trackCount = tracks.size();
        tvNumOfTracks.setText(String.format("%d tracks", trackCount));
        
        // Recalculate total duration
        int totalDurationSeconds = 0;
        for (TrackResponse track : tracks) {
            String duration = track.getDuration();
            if (duration != null) {
                String[] parts = duration.split(":");
                if (parts.length == 2) {
                    try {
                        totalDurationSeconds += Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        Log.e("PlaylistPageFragment", "Error parsing duration: " + duration, e);
                    }
                }
            }
        }
        
        // Format duration
        int hours = totalDurationSeconds / 3600;
        int minutes = (totalDurationSeconds % 3600) / 60;
        
        String durationText;
        if (hours > 0) {
            durationText = String.format("%dh %dm", hours, minutes);
        } else {
            durationText = String.format("%dm", minutes);
        }
            
        tvTotalDuration.setText(" · " + durationText);
        
        // If no tracks left, show empty state
        if (trackCount == 0) {
            Toast.makeText(requireContext(), "Playlist is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTrackDeleted(TrackResponse track) {
    }
}