package com.app.musicapp.view.fragment.playlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.interfaces.OnLikeChangeListener;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.view.activity.SignIn;
import com.app.musicapp.view.fragment.profile.UserProfileFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_PLAYLIST = "playlist";
    private PlaylistResponse playlistResponse;
    private ImageView ivLikeImage;
    private TextView tvLikeText;
    private PlaylistOptionsListener playlistOptionsListener;

    public void setPlaylistOptionsListener(PlaylistOptionsListener listener) {
        this.playlistOptionsListener = listener;
    }
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
        if (playlistResponse != null && isAdded() && ivLikeImage != null && tvLikeText != null) {
            boolean isLiked = playlistResponse.getIsLiked() != null && playlistResponse.getIsLiked();
            ivLikeImage.setImageResource(R.drawable.ic_favorite);
            ivLikeImage.setColorFilter(ContextCompat.getColor(requireContext(),
                    isLiked ? R.color.like_active : R.color.like_inactive));
            tvLikeText.setText(isLiked ? "Bỏ thích" : "Thích");
        } else {
            Log.w("PlaylistOptionsBottomSheet", "updateLikeUI: No State");
        }
    }

    private void checkLikeStatus() {
        if (playlistResponse == null || playlistResponse.getId() == null) {
            Log.e("PlaylistOptionsBottomSheet", "checkLikeStatus: Invalid playlist or playlistId");
            showToast("Không thể kiểm tra trạng thái thích");
            return;
        }

        ApiClient.getLikedPlaylistService().isLiked(playlistResponse.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiResponse = response.body();
                    if (apiResponse.getCode() == 200 && apiResponse.getData() != null) {
                        playlistResponse.setIsLiked(apiResponse.getData());
                        Log.d("PlaylistOptionsBottomSheet", "checkLikeStatus: isLiked=" + apiResponse.getData());
                        updateLikeUI();
                    } else if (apiResponse.getCode() == 1401) {
                        Log.e("PlaylistOptionsBottomSheet", "Unauthorized: Please log in again");
                        showToast("Vui lòng đăng nhập lại");
                        startActivity(new Intent(requireContext(), SignIn.class));
                        requireActivity().finish();
                    } else {
                        Log.e("PlaylistOptionsBottomSheet", "API error: Code=" + apiResponse.getCode() + ", Message=" + apiResponse.getMessage());
                        showToast("Lỗi: " + apiResponse.getMessage());
                    }
                } else {
                    showToast("Không thể kiểm tra trạng thái thích. Mã lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Throwable t) {
                Log.e("PlaylistOptionsBottomSheet", "Network error: " + t.getMessage());
                showToast("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void toggleLikeStatus() {
        if (playlistResponse == null || playlistResponse.getId() == null) {
            Log.e("PlaylistOptionsBottomSheet", "toggleLikeStatus: Invalid playlist or playlistId");
            showToast("Không thể thực hiện hành động thích");
            return;
        }

        boolean isCurrentlyLiked = playlistResponse.getIsLiked() != null && playlistResponse.getIsLiked();
        Call<ApiResponse<Boolean>> apiCall = isCurrentlyLiked ?
                ApiClient.getLikedPlaylistService().unlikePlaylist(playlistResponse.getId()) :
                ApiClient.getLikedPlaylistService().likePlaylist(playlistResponse.getId());

        apiCall.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        boolean newIsLiked = !isCurrentlyLiked; // unlike -> false, like -> true
                        playlistResponse.setIsLiked(newIsLiked);
                        Log.d("PlaylistOptionsBottomSheet", "toggleLikeStatus: isLiked=" + newIsLiked);
                        showToast(newIsLiked ? "Đã thích playlist" : "Đã bỏ thích playlist");
                        updateLikeUI();
                        if (getTargetFragment() instanceof OnLikeChangeListener) {
                            ((OnLikeChangeListener) getTargetFragment()).onLikeChanged(
                                    playlistResponse.getId(), newIsLiked);
                        } else if (getParentFragment() instanceof OnLikeChangeListener) {
                            ((OnLikeChangeListener) getParentFragment()).onLikeChanged(
                                    playlistResponse.getId(), newIsLiked);
                        }


                    } else if (apiResponse.getCode() == 1401) {
                        Log.e("PlaylistOptionsBottomSheet", "Unauthorized: Please log in again");
                        showToast("Vui lòng đăng nhập lại");
                        startActivity(new Intent(requireContext(), SignIn.class));
                        requireActivity().finish();
                    } else {
                        Log.e("PlaylistOptionsBottomSheet", "API error: Code=" + apiResponse.getCode() + ", Message=" + apiResponse.getMessage());
                        showToast("Lỗi: " + apiResponse.getMessage());
                    }
                } else {
                    showToast("Không thể thực hiện hành động thích. Mã lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Throwable t) {
                Log.e("PlaylistOptionsBottomSheet", "Network error: " + t.getMessage());
                showToast("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public interface PlaylistOptionsListener {
        void onPlaylistDeleted(PlaylistResponse playlist);
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
                    checkLikeStatus();
                } else {
                    Log.e("PlaylistOptionsBottomSheet", "ivLikeImage or tvLikeText is null");
                    showToast("Lỗi giao diện, vui lòng kiểm tra lại");
                }

                optionLiked.setOnClickListener(v -> toggleLikeStatus());

                optionArtistProfile.setOnClickListener(v -> {
                    UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(playlistResponse.getUserId(), "playlist");
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, userProfileFragment)
                                .addToBackStack(null)
                                .commit();
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
                LinearLayout layoutDelete = view.findViewById(R.id.layout_delete);
                LinearLayout optionPlayNext = view.findViewById(R.id.option_play_next);

                layoutEdit.setOnClickListener(v -> {
                    EditPlaylistFragment editFragment = EditPlaylistFragment.newInstance(playlistResponse);
                    dismiss();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, editFragment)
                            .addToBackStack(null)
                            .commit();
                });

                layoutMakePublic.setOnClickListener(v -> {
                    showToast("Đặt chế độ công khai: " + playlistResponse.getTitle());
                    dismiss();
                });

                layoutDelete.setOnClickListener(v -> {
                    if (playlistResponse == null || playlistResponse.getId() == null) {
                        Log.e("PlaylistOptionsBottomSheet", "deletePlaylist: Invalid playlist or playlistId");
                        showToast("Không thể xóa playlist");
                        return;
                    }

                    // Show confirmation dialog
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Delete Playlist")
                            .setMessage("Are you sure you want to delete this playlist?")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                // Call delete API after confirmation
                                ApiClient.getPlaylistService().deletePlaylistByIdV1(playlistResponse.getId())
                                        .enqueue(new Callback<ApiResponse<String>>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ApiResponse<String>> call,
                                                                @NonNull Response<ApiResponse<String>> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    showToast("Đã xóa playlist: " + playlistResponse.getTitle());
                                                    // Broadcast the change
                                                    Intent intent = new Intent("PLAYLIST_DELETED");
                                                    intent.putExtra("playlistId", playlistResponse.getId());
                                                    playlistOptionsListener.onPlaylistDeleted(playlistResponse);
                                                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
                                                    dismiss();
                                                } else if (response.body() != null && response.body().getCode() == 1401) {
                                                    Log.e("PlaylistOptionsBottomSheet", "Unauthorized: Please log in again");
                                                    showToast("Vui lòng đăng nhập lại");
                                                    startActivity(new Intent(requireContext(), SignIn.class));
                                                    requireActivity().finish();
                                                } else {
                                                    Log.e("PlaylistOptionsBottomSheet", "API error: Code=" + (response.body() != null ? response.body().getCode() : "Unknown") + ", Message=" + (response.body() != null ? response.body().getMessage() : "Unknown"));
                                                    showToast("Không thể xóa playlist. Mã lỗi: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ApiResponse<String>> call, @NonNull Throwable t) {
                                                Log.e("PlaylistOptionsBottomSheet", "Network error: " + t.getMessage());
                                                showToast("Lỗi mạng: " + t.getMessage());
                                            }
                                        });
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
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
