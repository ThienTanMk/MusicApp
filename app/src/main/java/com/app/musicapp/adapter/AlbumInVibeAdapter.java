package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.AlbumResponse;

import java.util.List;

public class AlbumInVibeAdapter extends RecyclerView.Adapter<AlbumInVibeAdapter.ViewHolder> {
    private Context context;
    private List<AlbumResponse> albumResponses;

    public AlbumInVibeAdapter(Context context, List<AlbumResponse> albumResponses) {
        this.context = context;
        this.albumResponses = albumResponses;
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

        try {
            String imagePath = albumResponse.getImagePath();
            String resourceName = imagePath != null ? imagePath.replace(".jpg", "") : "";
            if (!resourceName.isEmpty()) {
                int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
                if (resourceId != 0) {
                    holder.ivAlbumImage.setImageResource(resourceId);
                } else {
                    holder.ivAlbumImage.setImageResource(R.drawable.logo);
                }
            } else {
                holder.ivAlbumImage.setImageResource(R.drawable.logo);
            }
        } catch (Exception e) {
            holder.ivAlbumImage.setImageResource(R.drawable.logo);
            e.printStackTrace();
        }

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
