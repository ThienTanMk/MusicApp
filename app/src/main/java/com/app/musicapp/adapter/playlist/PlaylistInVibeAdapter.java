package com.app.musicapp.adapter.playlist;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TagResponse;

import java.util.ArrayList;
import java.util.List;

public class PlaylistInVibeAdapter extends RecyclerView.Adapter<PlaylistInVibeAdapter.ViewHolder>{
    private Context context;
    private List<PlaylistResponse> playlistResponses;
    public interface OnPlaylistClickListener {
        void onPlaylistClick(PlaylistResponse playlist);
    }
    private OnPlaylistClickListener listener;

    public void setOnPlaylistClickListener(OnPlaylistClickListener listener) {
        this.listener = listener;
    }
    public PlaylistInVibeAdapter(Context context, List<PlaylistResponse> playlistResponses) {
        this.context = context;
       // this.playlistResponses = playlistResponses != null ? playlistResponses : new ArrayList<>();
        this.playlistResponses = new ArrayList<>(playlistResponses);
    }
    public void updateData(List<PlaylistResponse> newPlaylists) {
        this.playlistResponses.clear();
        this.playlistResponses.addAll(newPlaylists);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PlaylistInVibeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_playlistinvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistInVibeAdapter.ViewHolder holder, int position) {
        PlaylistResponse playlistResponse = playlistResponses.get(position);
        holder.tvPlaylistTitle.setText(playlistResponse.getTitle());
        // Lấy tag đầu tiên làm thông tin
        List<TagResponse> tags = playlistResponse.getPlaylistTags();
        if (tags != null && !tags.isEmpty()) {
            holder.tvPlaylistArtist.setText(tags.get(0).getName());
        } else {
            holder.tvPlaylistArtist.setText("No Category");
        }
        try {
            String imagePath = playlistResponse.getImagePath();
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
        holder.itemView.setOnClickListener(v -> {
            Log.d("PlaylistInVibeAdapter", "Item clicked: " + playlistResponse.getTitle());
            if (listener != null) {
                listener.onPlaylistClick(playlistResponse);
            }
        });

    }

    @Override
    public int getItemCount() {
        return playlistResponses.size();
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
