package com.app.musicapp.adapter.album;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.AlbumResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AlbumInVibeAdapter extends RecyclerView.Adapter<AlbumInVibeAdapter.ViewHolder> {
    private Context context;
    private List<AlbumResponse> albumResponses;

    public AlbumInVibeAdapter(Context context, List<AlbumResponse> albumResponses) {
        this.context = context;
        this.albumResponses = new ArrayList<>(albumResponses) ;
    }
    public interface OnAlbumClickListener {
        void onAlbumClick(AlbumResponse album);
    }
    private OnAlbumClickListener listener;

    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.listener = listener;
    }
    public void updateData(List<AlbumResponse> newAlbums) {
        this.albumResponses.clear();
        this.albumResponses.addAll(newAlbums);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AlbumInVibeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_albuminvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumInVibeAdapter.ViewHolder holder, int position) {
        AlbumResponse albumResponse = albumResponses.get(position);
        // Hiển thị thông tin album
        holder.tvAlbumTitle.setText(albumResponse.getAlbumTitle());
        if (!albumResponse.getMainArtists().isEmpty()) {
            holder.tvAlbumArtist.setText(albumResponse.getMainArtists());
        } else {
            holder.tvAlbumArtist.setText("No Artist");
        }

        Glide.with(holder.itemView.getContext())
                .load(albumResponse.getImagePath() != null ? UrlHelper.getCoverImageUrl(albumResponse.getImagePath()) : R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.ivAlbumImage);
        holder.itemView.setOnClickListener(v -> {
            Log.d("AlbumInVibeAdapter", "Item clicked: " + albumResponse.getAlbumTitle());
            if (listener != null) {
                listener.onAlbumClick(albumResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumResponses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumImage;
        TextView tvAlbumTitle;
        TextView tvAlbumArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAlbumImage = itemView.findViewById(R.id.iv_album_image);
            tvAlbumTitle = itemView.findViewById(R.id.tv_album_title);
            tvAlbumArtist = itemView.findViewById(R.id.tv_album_artist);
            if (ivAlbumImage == null || tvAlbumTitle == null || tvAlbumArtist == null) {
                throw new IllegalStateException("One or more views are null in ViewHolder");
            }
        }
    }
}
