package com.app.musicapp.view.fragment.track;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.UpdatePlaylistInfoRequest;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.util.LocalDateTimeAdapter;
import com.app.musicapp.view.activity.CommentActivity;
import com.app.musicapp.view.fragment.UploadTrackFragment;
import com.app.musicapp.view.fragment.playlist.AddTrackToPlaylistFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;

public class SongOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_TRACK = "track";
    private static final String ARG_PLAYLIST = "playlist";
    private TrackResponse trackResponse;
    private PlaylistResponse playlist;
    private TextView tvLikeText;
    private ImageView ivLikeIcon;
    private boolean isLiked = false;
    private TrackOptionsListener optionsListener;

    public void setTrackOptionsListener(TrackOptionsListener listener) {
        this.optionsListener = listener;
    }

    public static SongOptionsBottomSheet newInstance(TrackResponse trackResponse) {
        return newInstance(trackResponse, null);
    }

    public static SongOptionsBottomSheet newInstance(TrackResponse trackResponse, PlaylistResponse playlist) {
        SongOptionsBottomSheet fragment = new SongOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACK, trackResponse);
        if (playlist != null) {
            args.putSerializable(ARG_PLAYLIST, playlist);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackResponse = (TrackResponse) getArguments().getSerializable(ARG_TRACK);
            playlist = (PlaylistResponse) getArguments().getSerializable(ARG_PLAYLIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_song_options, container, false);
        ImageView ivSongCover = view.findViewById(R.id.iv_song_cover);
        TextView tvSongTitle = view.findViewById(R.id.tv_song_title);
        TextView tvUserSong = view.findViewById(R.id.tv_user_song);

        // Initialize like views
        LinearLayout likeOption = view.findViewById(R.id.option_like);
        tvLikeText = view.findViewById(R.id.tv_like_text);
        ivLikeIcon = view.findViewById(R.id.iv_like_icon);

        if (trackResponse != null) {
            tvSongTitle.setText(trackResponse.getTitle());
            tvUserSong.setText(trackResponse.getUser().getDisplayName());

            // Load song cover image using Glide
            RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .transform(new RoundedCorners(8)); // 8dp corner radius

            String coverImageUrl = trackResponse.getCoverImageName() != null
                ? UrlHelper.getCoverImageUrl(trackResponse.getCoverImageName())
                : null;

            Glide.with(requireContext())
                .load(coverImageUrl)
                .apply(requestOptions)
                .into(ivSongCover);

            // Check if track is liked
            checkLikeStatus();
        }

        LinearLayout addToPlaylistOption = view.findViewById(R.id.option_add_to_playlist);
        LinearLayout goToArtistOption = view.findViewById(R.id.option_go_to_artist);
        LinearLayout viewCommentsOption = view.findViewById(R.id.option_view_comments);
        LinearLayout behindTrackOption = view.findViewById(R.id.option_behind_track);
        LinearLayout reportOption = view.findViewById(R.id.option_report);

        likeOption.setOnClickListener(v -> {
            if (isLiked) {
                unlikeTrack();
            } else {
                likeTrack();
            }
        });

        addToPlaylistOption.setOnClickListener(v -> {
            AddTrackToPlaylistFragment fragment = AddTrackToPlaylistFragment.newInstance(trackResponse);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
                dismiss();
            }
        });

        goToArtistOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Going to artist profile: " + trackResponse.getUserId(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        viewCommentsOption.setOnClickListener(v -> {
            if(trackResponse==null) return;
            Intent intent = new Intent(getContext(), CommentActivity.class);
            intent.putExtra("track_id",trackResponse.getId());
            intent.putExtra("track_title",trackResponse.getTitle());
            intent.putExtra("track_artist",trackResponse.getUser()==null?"loading":trackResponse.getUser().getDisplayName());
            intent.putExtra("track_cover",trackResponse.getCoverImageName());
            startActivity(intent);
        });

        behindTrackOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing behind the track: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        reportOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reported: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        // Add remove from playlist option
        LinearLayout removeFromPlaylistOption = view.findViewById(R.id.option_remove_from_playlist);
        if (removeFromPlaylistOption != null) {
            if (playlist != null) {
                removeFromPlaylistOption.setVisibility(View.VISIBLE);
                removeFromPlaylistOption.setOnClickListener(v -> {
                    showRemoveFromPlaylistConfirmDialog();
                });
            } else {
                removeFromPlaylistOption.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        String currentUserId = SharedPreferencesManager.getInstance(requireContext()).getUserId();
        View editOption = view.findViewById(R.id.option_edit_track);
        View deleteOption = view.findViewById(R.id.option_delete_track);
        
        // Chỉ hiển thị option edit và delete nếu là track của user hiện tại
        if (trackResponse.getUserId().equals(currentUserId)) {
            editOption.setVisibility(View.VISIBLE);
            deleteOption.setVisibility(View.VISIBLE);
            
            editOption.setOnClickListener(v -> {
                showEditTrackFragment();
                dismiss();
            });

            deleteOption.setOnClickListener(v -> {
                showDeleteConfirmDialog();
            });
        } else {
            editOption.setVisibility(View.GONE);
            deleteOption.setVisibility(View.GONE);
        }
    }

    private void checkLikeStatus() {
        ApiClient.getLikedTrackService().isLiked(trackResponse.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    isLiked = response.body().getData();
                    updateLikeUI();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kiểm tra trạng thái like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeTrack() {
        ApiClient.getLikedTrackService().likeTrack(trackResponse.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    isLiked = true;
                    updateLikeUI();
                    Toast.makeText(getContext(), "Đã thích bài hát: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Không thể thích bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unlikeTrack() {
        ApiClient.getLikedTrackService().unlikeTrack(trackResponse.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    isLiked = false;
                    updateLikeUI();
                    Toast.makeText(getContext(), "Đã bỏ thích bài hát: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Không thể bỏ thích bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLikeUI() {
        if (isLiked) {
            tvLikeText.setText("Bỏ thích");
        } else {
            tvLikeText.setText("Thích");
        }
        ivLikeIcon.setColorFilter(ContextCompat.getColor(requireContext(), isLiked ? R.color.like_active : R.color.like_inactive));
    }

    private void showEditTrackFragment() {
        UploadTrackFragment editFragment = new UploadTrackFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEdit", true);
        args.putSerializable("track", trackResponse);
        editFragment.setArguments(args);
        
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showDeleteConfirmDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Track")
            .setMessage("Are you sure you want to delete this track? This action cannot be undone.")
            .setPositiveButton("Delete", (dialog, which) -> {
                deleteTrack();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteTrack() {
        ApiClient.getTrackApiService().deleteTrack(trackResponse.getId())
            .enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), "Track deleted successfully", Toast.LENGTH_SHORT).show();
                        // Refresh the track list
                        if(optionsListener != null) {
                            optionsListener.onTrackDeleted(trackResponse);
                        }
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete track", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void showRemoveFromPlaylistConfirmDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Remove from Playlist")
            .setMessage("Are you sure you want to remove this track from the playlist?")
            .setPositiveButton("Remove", (dialog, which) -> {
                removeTrackFromPlaylist();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void removeTrackFromPlaylist() {
        if (playlist == null || trackResponse == null) return;

        // Create updated track list without the track to be removed
        List<TrackResponse> updatedTracks = new ArrayList<>(playlist.getPlaylistTracks());
        updatedTracks.remove(trackResponse);

        // Create update request
        UpdatePlaylistInfoRequest request = new UpdatePlaylistInfoRequest(
            playlist.getTitle(),
            playlist.getPrivacy(),
            updatedTracks.stream().map(TrackResponse::getId).collect(Collectors.toList()),
            playlist.getId(),
            playlist.getReleaseDate(),
            playlist.getDescription(),
            playlist.getGenre() != null ? playlist.getGenre().getId() : null,
            playlist.getPlaylistTags() != null ? playlist.getPlaylistTags().stream().map(TagResponse::getId).collect(Collectors.toList()) : new ArrayList<>()
        );

        // Convert request to JSON using GsonBuilder with LocalDateTimeAdapter
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
        String jsonRequest = gson.toJson(request);
        RequestBody playlistBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest);

        // Call API
        ApiClient.getPlaylistService().updatePlaylistInfoV1(null, playlistBody)
            .enqueue(new Callback<ApiResponse<PlaylistResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<PlaylistResponse>> call,
                                    @NonNull Response<ApiResponse<PlaylistResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), "Track removed from playlist", Toast.LENGTH_SHORT).show();
                        if (optionsListener != null) {
                            optionsListener.onTrackRemovedFromPlaylist(trackResponse);
                        }
                        dismiss();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Toast.makeText(getContext(), "Failed to remove track: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Failed to remove track", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    public interface TrackOptionsListener {
        void onTrackDeleted(TrackResponse track);
        void onTrackRemovedFromPlaylist(TrackResponse track);
    }
}
