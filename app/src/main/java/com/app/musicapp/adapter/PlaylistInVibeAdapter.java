package com.app.musicapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.Playlist;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class PlaylistInVibeAdapter extends RecyclerView.Adapter<PlaylistInVibeAdapter.ViewHolder>{
    private Context context;
    private List<Playlist> playlists;

    public PlaylistInVibeAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistInVibeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_playlistinvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistInVibeAdapter.ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.tvPlaylistTitle.setText(playlist.getTitle());
        // Lấy tag đầu tiên làm thông tin
        if (!playlist.getPlaylistTags().isEmpty()) {
            holder.tvPlaylistArtist.setText(playlist.getPlaylistTags().get(0).getName());
        } else {
            holder.tvPlaylistArtist.setText("No Category");
        }
        try {
            String imagePath = playlist.getImagePath();
            String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
            if (!resourceName.isEmpty()) {
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier(resourceName, "drawable", context.getPackageName());
                if (resourceId != 0) {
                    holder.ivPlaylistImage.setImageResource(resourceId);
                } else {
                    holder.ivPlaylistImage.setImageResource(R.drawable.logo);
                }
            } else {
                holder.ivPlaylistImage.setImageResource(R.drawable.logo);
            }
        } catch (Exception e) {
            holder.ivPlaylistImage.setImageResource(R.drawable.logo);
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaylistImage;
        TextView tvPlaylistTitle;
        TextView tvPlaylistArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaylistImage = itemView.findViewById(R.id.iv_playlist_image);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistArtist = itemView.findViewById(R.id.tv_playlist_artist);
        }
    }
}
