package com.app.musicapp.view.fragment.track;

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
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_TRACK = "track";
    private TrackResponse trackResponse;
    private TextView tvLikeText;
    private ImageView ivLikeIcon;
    private boolean isLiked = false;

    public static SongOptionsBottomSheet newInstance(TrackResponse trackResponse) {
        SongOptionsBottomSheet fragment = new SongOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACK, trackResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackResponse = (TrackResponse) getArguments().getSerializable(ARG_TRACK);
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
            Toast.makeText(getContext(), "Added to playlist: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        goToArtistOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Going to artist profile: " + trackResponse.getUserId(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        viewCommentsOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing comments for: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        behindTrackOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing behind the track: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        reportOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reported: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
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
}
