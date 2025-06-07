package com.app.musicapp.view.fragment.playlist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlaylistOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_PLAYLIST = "playlist";
    private PlaylistResponse playlistResponse;
    private ImageView ivLikeImage;
    private TextView tvLikeText;

    public static PlaylistOptionsBottomSheet newInstance(PlaylistResponse playlist) {
        PlaylistOptionsBottomSheet fragment = new PlaylistOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLAYLIST, playlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlistResponse = (PlaylistResponse) getArguments().getSerializable(ARG_PLAYLIST);
            Log.d("PlaylistOptionsBottomSheet", "onCreate: playlistTitle=" +
                    (playlistResponse != null && playlistResponse.getTitle() != null ? playlistResponse.getTitle() : "null") +
                    ", isLiked=" + (playlistResponse != null && playlistResponse.getIsLiked() != null ? playlistResponse.getIsLiked() : "null"));
        } else {
            Log.e("PlaylistOptionsBottomSheet", "onCreate: Arguments null");
        }
    }
    private void showToast(String message) {
        Context context = getContext();
        if (context != null && isAdded()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    private void updateLikeUI() {
        if (playlistResponse != null && isAdded()) {
            boolean isLiked = playlistResponse.getIsLiked() != null && playlistResponse.getIsLiked();
            Log.d("PlaylistOptionsBottomSheet", "updateLikeUI: isLiked=" + isLiked);
            ivLikeImage.setImageResource(R.drawable.ic_favorite);
            ivLikeImage.setColorFilter(ContextCompat.getColor(requireContext(),
                    isLiked ? R.color.like_active : R.color.like_inactive));
            tvLikeText.setText(isLiked ? "Bỏ thích" : "Thích");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean isLiked = playlistResponse != null && playlistResponse.getIsLiked() != null && playlistResponse.getIsLiked();
        int layoutResId = isLiked ? R.layout.bottom_sheet_liked_playlist_options : R.layout.bottom_sheet_user_playlist;
        View view = inflater.inflate(layoutResId, container, false);

        ImageView ivPlaylistImage = view.findViewById(R.id.iv_playlist_image);
        TextView tvPlaylistTitle = view.findViewById(R.id.tv_playlist_title);
        TextView tvUserPlaylist = view.findViewById(R.id.tv_user_playlist);

        if (playlistResponse != null) {
            if (playlistResponse.getImagePath() != null) {
                Context context = getContext();
                if (context != null) {
                    RequestOptions requestOptions = new RequestOptions()
                            .transform(new RoundedCorners(16))
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo);
                    Glide.with(context)
                            .load(UrlHelper.getCoverImageUrl(playlistResponse.getImagePath()))
                            .apply(requestOptions)
                            .into(ivPlaylistImage);
                } else {
                    ivPlaylistImage.setImageResource(R.drawable.logo);
                }
            } else {
                ivPlaylistImage.setImageResource(R.drawable.logo);
            }

            tvPlaylistTitle.setText(playlistResponse.getTitle() != null ? playlistResponse.getTitle() : "Untitled");
            tvUserPlaylist.setText(playlistResponse.getUserId() != null ? playlistResponse.getUserId() : "Unknown User");

            if (isLiked) {
                //liked playlist
                LinearLayout optionLiked = view.findViewById(R.id.option_liked);
                LinearLayout optionArtistProfile = view.findViewById(R.id.option_artist_profile);
                LinearLayout optionPlayNext = view.findViewById(R.id.option_play_next);
                ivLikeImage = optionLiked.findViewById(R.id.iv_like);
                tvLikeText = optionLiked.findViewById(R.id.tv_like_text);

                if (ivLikeImage != null && tvLikeText != null) {
                    updateLikeUI();
                } else {
                    Log.e("PlaylistOptionsBottomSheet", "ivLikeImage or tvLikeText is null");
                }

                optionLiked.setOnClickListener(v -> {
                    showToast("Chức năng thích chưa được triển khai");
                });

                optionArtistProfile.setOnClickListener(v -> {
                    showToast("Chuyển đến hồ sơ người dùng: " + playlistResponse.getUserId());
                    dismiss();
                });

                optionPlayNext.setOnClickListener(v -> {
                    showToast("Phát tiếp theo: " + playlistResponse.getTitle());
                    dismiss();
                });
            } else {
                // user playlist
                LinearLayout layoutEdit = view.findViewById(R.id.layout_edit);
                LinearLayout layoutMakePublic = view.findViewById(R.id.layout_make_public);
                LinearLayout layoutAddMusic = view.findViewById(R.id.layout_add_music);
                LinearLayout layoutDelete = view.findViewById(R.id.layout_delete);
                LinearLayout optionPlayNext = view.findViewById(R.id.option_play_next);

                layoutEdit.setOnClickListener(v -> {
                    showToast("Chỉnh sửa playlist: " + playlistResponse.getTitle());
                    dismiss();
                });

                layoutMakePublic.setOnClickListener(v -> {
                    showToast("Đặt chế độ công khai: " + playlistResponse.getTitle());
                    dismiss();
                });

                layoutAddMusic.setOnClickListener(v -> {
                    showToast("Thêm nhạc vào playlist: " + playlistResponse.getTitle());
                    dismiss();
                });

                layoutDelete.setOnClickListener(v -> {
                    showToast("Xóa playlist: " + playlistResponse.getTitle());
                    dismiss();
                });

                optionPlayNext.setOnClickListener(v -> {
                    showToast("Phát tiếp theo: " + playlistResponse.getTitle());
                    dismiss();
                });
            }
        } else {
            Log.e("PlaylistOptionsBottomSheet", "playlistResponse is null");
            showToast("Không thể hiển thị thông tin playlist");
            dismiss();
        }

        return view;
    }
}
