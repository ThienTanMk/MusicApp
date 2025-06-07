package com.app.musicapp.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.app.musicapp.R;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.view.fragment.playlist.PlaylistOptionsBottomSheet;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<PlaylistResponse> {
    private List<PlaylistResponse> playlists; // Danh sách chứa cả Playlist và LikedPlaylist
    public PlaylistAdapter(@NonNull Context context, @NonNull List<PlaylistResponse> playlists) {
        super(context, 0, playlists);
        this.playlists = playlists != null ? playlists : new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_playlist, parent, false);
        }

        // Ánh xạ các view
        ImageView ivPlaylistImage = convertView.findViewById(R.id.iv_playlist_image);
        TextView tvPlaylistTitle = convertView.findViewById(R.id.tv_playlist_title);
        TextView tvPlaylistArtist = convertView.findViewById(R.id.tv_artist);
        TextView tvLikeCount = convertView.findViewById(R.id.tv_like_count);
        TextView tvTrackCount = convertView.findViewById(R.id.tv_track_count);
        ImageView ivMenu = convertView.findViewById(R.id.iv_menu);

        // Lấy đối tượng tại vị trí position
        PlaylistResponse playlistResponse = playlists.get(position);

        // Hiển thị thông tin playlist
        tvPlaylistTitle.setText(playlistResponse.getTitle() != null ? playlistResponse.getTitle() : "Untitled");
        tvPlaylistArtist.setText(playlistResponse.getUserId() != null ? playlistResponse.getUserId() : "Unknown User");
        int trackCount = (playlistResponse.getPlaylistTrackResponses() != null) ? playlistResponse.getPlaylistTrackResponses().size() : 0;
        tvTrackCount.setText(trackCount + " Tracks");
        tvLikeCount.setText(String.valueOf((int) (Math.random() * 1000)));

        try {
            String imagePath = playlistResponse.getImagePath();
            String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
            if (!resourceName.isEmpty()) {
                Resources resources = getContext().getResources();
                int resourceId = resources.getIdentifier(resourceName, "drawable", getContext().getPackageName());
                ivPlaylistImage.setImageResource(resourceId != 0 ? resourceId : R.drawable.logo);
            } else {
                ivPlaylistImage.setImageResource(R.drawable.logo);
            }
        } catch (Exception e) {
            ivPlaylistImage.setImageResource(R.drawable.logo);
        }

        // Xử lý sự kiện bấm vào nút More
        ivMenu.setOnClickListener(v -> {
            PlaylistOptionsBottomSheet bottomSheet = PlaylistOptionsBottomSheet.newInstance(playlistResponse);
            Log.d("PlaylistAdapter", "Opening bottom sheet for item " + position + ", isLiked=" +
                    (playlistResponse.getIsLiked() != null ? playlistResponse.getIsLiked() : "null"));
            if (getContext() instanceof FragmentActivity) {
                bottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), bottomSheet.getTag());
            } else {
                Log.e("PlaylistAdapter", "Context is not FragmentActivity");
            }
        });

        convertView.setOnClickListener(v -> {
            List<PlaylistResponse> selectedPlaylist = new ArrayList<>();
            selectedPlaylist.add(playlistResponse);
            PlaylistPageFragment playlistPageFragment = PlaylistPageFragment.newInstance(selectedPlaylist);
            if (getContext() instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) getContext();
                View mainView = activity.findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                if (viewPager != null && fragmentContainer != null) {
                    viewPager.setVisibility(View.GONE);
                    fragmentContainer.setVisibility(View.VISIBLE);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, playlistPageFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return convertView;
    }
}
