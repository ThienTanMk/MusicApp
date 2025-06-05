package com.app.musicapp.adapter;

import android.view.*;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.LikedPlaylist;
import com.app.musicapp.model.Playlist;
import com.bumptech.glide.Glide;

import java.util.List;

public class PlayListRVAdapter extends RecyclerView.Adapter<PlayListRVAdapter.ViewHolder>{
    private List<Object> playlist;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    public PlayListRVAdapter(List<Object> playlist, OnItemClickListener listener) {
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
        Object item = playlist.get(position);
        if (item instanceof Playlist) {
            Playlist playlist = (Playlist) item;
            holder.tvPlaylistTitle.setText(playlist.getTitle());
            holder.tvPlaylistArtist.setText(playlist.getUserId());
            Glide.with(holder.itemView.getContext()).load(playlist.getImagePath()).placeholder(R.drawable.logo).into(holder.ivPlaylistImage);
        } else if (item instanceof LikedPlaylist) {
            LikedPlaylist likedPlaylist = (LikedPlaylist) item;
            holder.tvPlaylistTitle.setText(likedPlaylist.getPlaylist().getTitle());
            holder.tvPlaylistArtist.setText(likedPlaylist.getPlaylist().getUserId());
            Glide.with(holder.itemView.getContext()).load(likedPlaylist.getPlaylist().getImagePath()).placeholder(R.drawable.logo).into(holder.ivPlaylistImage);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }


    @Override
    public int getItemCount() {
        return playlist.size();
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
