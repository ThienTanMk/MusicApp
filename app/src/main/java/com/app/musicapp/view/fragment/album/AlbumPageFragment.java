package com.app.musicapp.view.fragment.album;

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
import com.app.musicapp.model.Album;
import com.app.musicapp.model.Track;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class AlbumPageFragment extends Fragment {
    private Album album;

    public static AlbumPageFragment newInstance(Album album) {
        AlbumPageFragment fragment = new AlbumPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", album);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPageFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable("album");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_page, container, false);

        // Ánh xạ view
        ImageView ivBack = view.findViewById(R.id.iv_back);
        TextView tvAlbumTitleHeader = view.findViewById(R.id.tv_album_title_header);
        ImageView ivAlbumCover = view.findViewById(R.id.iv_album_cover);
        TextView tvAlbumTitle = view.findViewById(R.id.tv_album_title);
        TextView tvAlbumArtists = view.findViewById(R.id.tv_album_artists);
        TextView tvAlbumType = view.findViewById(R.id.tv_album_type);
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

        if (album != null) {
            tvAlbumTitleHeader.setText("Album " + album.getCreatedAt().getYear());
            tvAlbumTitle.setText(album.getAlbumTitle());
            tvAlbumArtists.setText(album.getMainArtists());
            try {
                String imagePath = album.getImagePath();
                String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
                if (!resourceName.isEmpty()) {
                    int resourceId = getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
                    if (resourceId != 0) {
                        ivAlbumCover.setImageResource(resourceId);
                    } else {
                        ivAlbumCover.setImageResource(R.drawable.logo);
                    }
                } else {
                    ivAlbumCover.setImageResource(R.drawable.logo);
                }
            } catch (Exception e) {
                ivAlbumCover.setImageResource(R.drawable.logo);
                e.printStackTrace();
            }

            tvAlbumType.setText("Album");
            tvCreatedAt.setText(" · " + album.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")));
            int trackCount = album.getTracks() != null ? album.getTracks().size() : 0;
            tvNumOfTracks.setText(" · " + trackCount + " Tracks");
            long totalDurationSeconds = calculateTotalDuration(album.getTracks());
            String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
            tvTotalDuration.setText(" · " + duration);
            tvLikeCount.setText("210");
            tvDescription.setText(album.getDescription() != null ? album.getDescription() : "No description");
            rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
            TrackRVAdapter trackAdapter = new TrackRVAdapter(this, album.getTracks() != null ? album.getTracks() : new ArrayList<>());
            rvTracks.setAdapter(trackAdapter);
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> {
            // Logic thích album
        });
        ivMenu.setOnClickListener(v -> {
            AlbumOptionsBottomSheet bottomSheet = AlbumOptionsBottomSheet.newInstance(album);
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        });
        ivPlay.setOnClickListener(v -> {
            // Logic phát album
        });
        tvShowMore.setOnClickListener(v -> {
            // Mở rộng description
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