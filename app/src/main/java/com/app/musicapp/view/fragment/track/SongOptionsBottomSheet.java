package com.app.musicapp.view.fragment.track;

import android.content.res.Resources;
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

import com.app.musicapp.R;
import com.app.musicapp.model.Track;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SongOptionsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_TRACK = "track";
    private Track track;

    public static SongOptionsBottomSheet newInstance(Track track) {
        SongOptionsBottomSheet fragment = new SongOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACK, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            track = (Track) getArguments().getSerializable(ARG_TRACK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_song_options, container, false);
        ImageView ivSongCover = view.findViewById(R.id.iv_song_cover);
        TextView tvSongTitle = view.findViewById(R.id.tv_song_title);
        TextView tvUserSong = view.findViewById(R.id.tv_user_song);

        if (track != null) {
            tvSongTitle.setText(track.getTitle());
            tvUserSong.setText(track.getUserId());
            try {
                String coverImageName = track.getCoverImageName();
                String resourceName = coverImageName != null ? coverImageName.replace(".jpg", "") : "";
                if (!resourceName.isEmpty()) {
                    Resources resources = getContext().getResources();
                    int resourceId = resources.getIdentifier(resourceName, "drawable", getContext().getPackageName());
                    if (resourceId != 0) {
                        ivSongCover.setImageResource(resourceId);
                    } else {
                        ivSongCover.setImageResource(R.drawable.logo);
                    }
                } else {
                    ivSongCover.setImageResource(R.drawable.logo);
                }
            } catch (Exception e) {
                ivSongCover.setImageResource(R.drawable.logo);
                e.printStackTrace();
            }
        }
        LinearLayout likeOption = view.findViewById(R.id.option_like);
        LinearLayout addToPlaylistOption = view.findViewById(R.id.option_add_to_playlist);
        LinearLayout goToArtistOption = view.findViewById(R.id.option_go_to_artist);
        LinearLayout viewCommentsOption = view.findViewById(R.id.option_view_comments);
        LinearLayout behindTrackOption = view.findViewById(R.id.option_behind_track);
        LinearLayout reportOption = view.findViewById(R.id.option_report);

        likeOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        addToPlaylistOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added to playlist: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        goToArtistOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Going to artist profile: " + track.getUserId(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        viewCommentsOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing comments for: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        behindTrackOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing behind the track: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        reportOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reported: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }
}
