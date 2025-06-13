package com.app.musicapp.view.fragment.album;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackRVAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumPageFragment extends Fragment {
    private AlbumResponse album;
    private ImageView ivLike;
    TextView tvLikeCount;
    private TrackRVAdapter trackAdapter;
    public static AlbumPageFragment newInstance(AlbumResponse album) {
        AlbumPageFragment fragment = new AlbumPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", (Serializable) album);
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
            album = (AlbumResponse) getArguments().getSerializable("album");
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
        ivLike = view.findViewById(R.id.iv_like);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        ImageView ivMenu = view.findViewById(R.id.iv_menu);
        ImageView ivPlay = view.findViewById(R.id.iv_play);
        TextView tvDescription = view.findViewById(R.id.tv_description);
        TextView tvShowMore = view.findViewById(R.id.tv_show_more);
        RecyclerView rvTracks = view.findViewById(R.id.rv_tracks);
        if (album != null) {
            tvAlbumTitleHeader.setText("Album " + album.getCreatedAt().getYear());
            tvAlbumTitle.setText(album.getAlbumTitle());
            tvAlbumArtists.setText(album.getMainArtists());
            getLikeCount();
            // Load album image using Glide
            if (album.getImagePath() != null) {
                Context context = getContext();
                if (context != null) {
                    RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo);

                    Glide.with(context)
                        .load(UrlHelper.getCoverImageUrl(album.getImagePath()))
                        .apply(requestOptions)
                        .into(ivAlbumCover);
                } else {
                    ivAlbumCover.setImageResource(R.drawable.logo);
                }
            } else {
                ivAlbumCover.setImageResource(R.drawable.logo);
            }

            tvAlbumType.setText("Album");
            tvCreatedAt.setText(" · " + album.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy")));
            int trackCount = album.getTracks() != null ? album.getTracks().size() : 0;
            tvNumOfTracks.setText(" · " + trackCount + " Tracks");
            long totalDurationSeconds = calculateTotalDuration(album.getTracks());
            String duration = String.format("%d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60);
            tvTotalDuration.setText(" · " + duration);

            tvDescription.setText(album.getDescription() != null ? album.getDescription() : "No description");
            rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
            trackAdapter = new TrackRVAdapter(this, album.getTracks() != null ? album.getTracks() : new ArrayList<>());
            trackAdapter.setOnTrackClickListener(track -> {
            });
            rvTracks.setAdapter(trackAdapter);
        }

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        ivLike.setOnClickListener(v -> handleLikeUnlike());
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

        updateLikeUI();

        return view;
    }



    private void handleLikeUnlike() {
        if (album == null) return;

        if (album.getIsLiked()) {
            // Unlike album
            ApiClient.getAlbumService().unlikeAlbum(album.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        album.setIsLiked(false);
                        updateLikeUI();
                        getLikeCount();
                    } else {
                        Toast.makeText(getContext(), "Không thể bỏ thích album", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Like album
            ApiClient.getAlbumService().likeAlbum(album.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        album.setIsLiked(true);
                        updateLikeUI();
                        getLikeCount();
                        Toast.makeText(getContext(), "Đã thích album", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không thể thích album", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateLikeUI() {
        if (album != null) {
            // Update like icon color
            ivLike.setImageResource(R.drawable.ic_favorite);
            ivLike.setColorFilter(
                ContextCompat.getColor(requireContext(),
                    album.getIsLiked() ? R.color.like_active : R.color.like_inactive)
            );
        }
    }
    private long calculateTotalDuration(List<TrackResponse> trackResponses) {
        long totalSeconds = 0;
        if (trackResponses != null) {
            for (TrackResponse trackResponse : trackResponses) {
                String[] timeParts = trackResponse.getDuration().split(":");
                if (timeParts.length == 2) {
                    totalSeconds += Integer.parseInt(timeParts[0]) * 60 + Integer.parseInt(timeParts[1]);
                }
            }
        }
        return totalSeconds;
    }
    private void getLikeCount() {
        ApiClient.getAlbumService().getAlbumLikeCount(album.getId()).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() !=null) {
                    tvLikeCount.setText(String.valueOf(response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {

            }
        });
    }
}