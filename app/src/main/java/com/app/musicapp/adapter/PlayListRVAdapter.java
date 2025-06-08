package com.app.musicapp.adapter;

import android.view.*;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class PlayListRVAdapter extends RecyclerView.Adapter<PlayListRVAdapter.ViewHolder>{
    private List<PlaylistResponse> playlist;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(PlaylistResponse item);
    }

    public PlayListRVAdapter(List<PlaylistResponse> playlist, OnItemClickListener listener) {
        this.playlist = playlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playlistinvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListRVAdapter.ViewHolder holder, int position) {
        PlaylistResponse playlistResponse = playlist.get(position);
        holder.tvPlaylistTitle.setText(playlistResponse.getTitle() != null ? playlistResponse.getTitle() : "Untitled");
        holder.tvPlaylistArtist.setText(playlistResponse.getUserId() != null ? playlistResponse.getUserId() : "Unknown User");
        Glide.with(holder.itemView.getContext())
                .load(playlistResponse.getImagePath() != null ? playlistResponse.getImagePath() : R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.ivPlaylistImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(playlistResponse));
    }


    @Override
    public int getItemCount() {
        return playlist != null ? playlist.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivPlaylistImage;
    TextView tvPlaylistTitle;
    TextView tvPlaylistArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPlaylistImage = itemView.findViewById(R.id.iv_playlist_image);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistArtist = itemView.findViewById(R.id.tv_playlist_artist);
        }
    }
}
