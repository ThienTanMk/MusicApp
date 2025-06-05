package com.app.musicapp.view.fragment.album;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.musicapp.R;
import com.app.musicapp.model.Album;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlbumOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_ALBUM = "album";
    private Album album;

    public static AlbumOptionsBottomSheet newInstance(Album album) {
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
            album = (Album) getArguments().getSerializable(ARG_ALBUM);
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

        // Đặt dữ liệu
        if (album != null) {
            ivAlbumImage.setImageResource(R.drawable.logo);
            tvAlbumTitle.setText(album.getAlbumTitle());
            tvUserAlbum.setText(album.getMainArtists());

            optionLiked.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Liked: " + album.getAlbumTitle(), Toast.LENGTH_SHORT).show();
                dismiss(); // Đóng bottom sheet
            });

            optionArtistProfile.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Go to artist profile: " + album.getMainArtists(), Toast.LENGTH_SHORT).show();
                dismiss(); // Đóng bottom sheet
                // Thêm logic điều hướng đến profile nghệ sĩ
            });

            optionPlayNext.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Play Next: " + album.getAlbumTitle(), Toast.LENGTH_SHORT).show();
                dismiss(); // Đóng bottom sheet
                // Thêm logic phát album tiếp theo
            });
        }
        return view;
    }
}
