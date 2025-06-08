package com.app.musicapp.view.fragment.album;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;
import com.app.musicapp.view.activity.MusicPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.app.musicapp.interfaces.OnLikeChangeListener;

import java.util.List;

public class AlbumOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_ALBUM = "album";
    private AlbumResponse album;
    private ImageView ivLikeIcon;
    private TextView tvLikeText;
    private OnLikeChangeListener likeChangeListener;
    private MusicService musicService;
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
    public void setOnLikeChangeListener(OnLikeChangeListener listener) {
        this.likeChangeListener = listener;
    }

    public static AlbumOptionsBottomSheet newInstance(AlbumResponse album) {
        AlbumOptionsBottomSheet fragment = new AlbumOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            album = (AlbumResponse) getArguments().getSerializable(ARG_ALBUM);
        }
    }

    private void showToast(String message) {
        Context context = getContext();
        if (context != null && isAdded()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLikeUI() {
        if (album != null && isAdded()) {
            // Update icon
            ivLikeIcon.setImageResource(R.drawable.ic_favorite);
            ivLikeIcon.setColorFilter(ContextCompat.getColor(requireContext(), album.getIsLiked() ? R.color.like_active : R.color.like_inactive));
            
            // Update text
            tvLikeText.setText(album.getIsLiked() ? "Bỏ thích" : "Thích");
        }
    }

    private void handleLikeUnlike() {
        if (album == null || album.getId() == null) {
            showToast("Không thể thực hiện thao tác này");
            return;
        }

        if (album.getIsLiked()) {
            // Unlike album
            ApiClient.getAlbumService().unlikeAlbum(album.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật trạng thái local
                        album.setIsLiked(false);
                        // Cập nhật UI bottom sheet
                        updateLikeUI();
                        // Thông báo cho adapter cập nhật
                        if (likeChangeListener != null) {
                            likeChangeListener.onLikeChanged(album.getId(), false);
                        }
                        showToast("Đã bỏ thích album");
                    } else {
                        showToast("Không thể bỏ thích album");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    showToast("Lỗi kết nối: " + t.getMessage());
                }
            });
        } else {
            // Like album
            ApiClient.getAlbumService().likeAlbum(album.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật trạng thái local
                        album.setIsLiked(true);
                        // Cập nhật UI bottom sheet
                        updateLikeUI();
                        // Thông báo cho adapter cập nhật
                        if (likeChangeListener != null) {
                            likeChangeListener.onLikeChanged(album.getId(), true);
                        }
                        showToast("Đã thích album");
                    } else {
                        showToast("Không thể thích album");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    showToast("Lỗi kết nối: " + t.getMessage());
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_liked_album_options, container, false);

        // Ánh xạ view
        ImageView ivAlbumImage = view.findViewById(R.id.iv_album_image);
        TextView tvAlbumTitle = view.findViewById(R.id.tv_album_title);
        TextView tvUserAlbum = view.findViewById(R.id.tv_user_album);
        LinearLayout optionLiked = view.findViewById(R.id.option_liked);
        LinearLayout optionArtistProfile = view.findViewById(R.id.option_artist_profile);
        LinearLayout optionPlayNext = view.findViewById(R.id.option_play_next);
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        getContext().startForegroundService(intent);
        // Ánh xạ view cho like option
        ivLikeIcon = optionLiked.findViewById(R.id.iv_like_icon);
        tvLikeText = optionLiked.findViewById(R.id.tv_like_text);

        // Đặt dữ liệu
        if (album != null) {
            // Load album image using Glide
            if (album.getImagePath() != null) {
                Context context = getContext();
                if (context != null) {
                    RequestOptions requestOptions = new RequestOptions()
                        .transform(new RoundedCorners(16)) // Bo góc 16dp
                        .placeholder(R.drawable.logo) // Ảnh placeholder khi đang load
                        .error(R.drawable.logo); // Ảnh hiển thị khi lỗi

                    Glide.with(context)
                        .load(UrlHelper.getCoverImageUrl(album.getImagePath()))
                        .apply(requestOptions)
                        .into(ivAlbumImage);
                } else {
                    ivAlbumImage.setImageResource(R.drawable.logo);
                }
            } else {
                ivAlbumImage.setImageResource(R.drawable.logo);
            }

            tvAlbumTitle.setText(album.getAlbumTitle());
            tvUserAlbum.setText(album.getMainArtists());

            // Update initial like UI state
            updateLikeUI();

            optionLiked.setOnClickListener(v -> {
                handleLikeUnlike();
            });

            optionArtistProfile.setOnClickListener(v -> {
                showToast("Go to artist profile: " + album.getMainArtists());
                dismiss();
                // Thêm logic điều hướng đến profile nghệ sĩ
            });

            optionPlayNext.setOnClickListener(v -> {
                List<TrackResponse> tracks = musicService.getNextUpItems();
                tracks.addAll(album.getTracks());
                musicService.setNextUpItems(tracks);
                Intent musicIntent = new Intent(getContext(), MusicPlayer.class);
                startActivity(musicIntent);
                dismiss();
            });
        }
        return view;
    }
}
