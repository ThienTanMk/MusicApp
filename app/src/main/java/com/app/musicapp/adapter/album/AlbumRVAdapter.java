package com.app.musicapp.adapter.album;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.response.AlbumResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumRVAdapter extends RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>{
    private List<AlbumResponse> albumList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AlbumResponse album);
    }

    public AlbumRVAdapter(List<AlbumResponse> albumList, OnItemClickListener listener) {
        this.albumList = albumList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public AlbumRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_albuminvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRVAdapter.ViewHolder holder, int position) {
        AlbumResponse album = albumList.get(position);
        holder.tvAlbumTitle.setText(album.getAlbumTitle());
        holder.tvAlbumArtist.setText(album.getMainArtists());
        Glide.with(holder.itemView.getContext()).load(album.getImagePath()).placeholder(R.drawable.logo).into(holder.ivAlbumImage);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(album));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumImage;
        TextView tvAlbumTitle;
        TextView tvAlbumArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAlbumImage = itemView.findViewById(R.id.iv_album_image);
            tvAlbumTitle = itemView.findViewById(R.id.tv_album_title);
            tvAlbumArtist = itemView.findViewById(R.id.tv_album_artist);
        }
    }
}
