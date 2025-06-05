package com.app.musicapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.FragmentActivity;

import com.app.musicapp.R;
import com.app.musicapp.model.LikedPlaylist;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Object> {
    private List<Object> playlists; // Danh sách chứa cả Playlist và LikedPlaylist

    public PlaylistAdapter(@NonNull Context context, @NonNull List<Object> playlists) {
        super(context, 0, playlists);
        this.playlists = playlists;
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
        Object item = playlists.get(position);
        Playlist playlist;

        // Kiểm tra xem item là Playlist hay LikedPlaylist
        if (item instanceof Playlist) {
            playlist = (Playlist) item;
        } else if (item instanceof LikedPlaylist) {
            playlist = ((LikedPlaylist) item).getPlaylist();
        } else {
            return convertView;
        }

        // Hiển thị thông tin playlist
        tvPlaylistTitle.setText(playlist.getTitle());
        tvPlaylistArtist.setText(playlist.getUserId());
        tvTrackCount.setText(playlist.getPlaylistTracks().size() + " Tracks");
        tvLikeCount.setText(String.valueOf((int) (Math.random() * 1000)));

        try {
            String imagePath = playlist.getImagePath();
            String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
            if (!resourceName.isEmpty()) {
                Resources resources = getContext().getResources();
                int resourceId = resources.getIdentifier(resourceName, "drawable", getContext().getPackageName());
                if (resourceId != 0) {
                    ivPlaylistImage.setImageResource(resourceId);
                } else {
                    ivPlaylistImage.setImageResource(R.drawable.logo);
                }
            } else {
                ivPlaylistImage.setImageResource(R.drawable.logo);
            }
        } catch (Exception e) {
            ivPlaylistImage.setImageResource(R.drawable.logo);
            e.printStackTrace();
        }

        // Xử lý sự kiện bấm vào nút More
        ivMenu.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View bottomSheetView;
            if (item instanceof Playlist) {
                // Playlist tự tạo
                bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_user_playlist, null);
            } else {
                // Playlist thích
                bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_liked_playlist_options, null);
            }

            // Ánh xạ các view trong bottom sheet
            ImageView ivPlaylistImageSheet = bottomSheetView.findViewById(R.id.iv_playlist_image);
            TextView tvPlaylistTitleSheet = bottomSheetView.findViewById(R.id.tv_playlist_title);
            TextView tvPlaylistDescriptionSheet = bottomSheetView.findViewById(R.id.tv_user_playlist);

            // Hiển thị thông tin playlist trong bottom sheet
            tvPlaylistTitleSheet.setText(playlist.getTitle());
            tvPlaylistDescriptionSheet.setText(playlist.getDescription());
            try {
                String imagePath = playlist.getImagePath();
                String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
                if (!resourceName.isEmpty()) {
                    Resources resources = getContext().getResources();
                    int resourceId = resources.getIdentifier(resourceName, "drawable", getContext().getPackageName());
                    if (resourceId != 0) {
                        ivPlaylistImageSheet.setImageResource(resourceId);
                    } else {
                        ivPlaylistImageSheet.setImageResource(R.drawable.logo);
                    }
                } else {
                    ivPlaylistImageSheet.setImageResource(R.drawable.logo);
                }
            } catch (Exception e) {
                ivPlaylistImageSheet.setImageResource(R.drawable.logo);
                e.printStackTrace();
            }
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });
        convertView.setOnClickListener(v -> {
            List<Object> selectedPlaylist = new ArrayList<>();
            selectedPlaylist.add(item); // Tạo danh sách chỉ chứa playlist được chọn
            PlaylistPageFragment playlistPageFragment = PlaylistPageFragment.newInstance(selectedPlaylist);
            // Điều hướng đến PlaylistPageFragment
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
