package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.musicapp.R;
import com.app.musicapp.model.TopTrack;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;
import java.util.List;

public class TopTrackAdapter extends RecyclerView.Adapter<TopTrackAdapter.TopTrackViewHolder> {
    private final List<TopTrack> tracks;
    private final Context context;

    public TopTrackAdapter(Context context, List<TopTrack> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TopTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_track, parent, false);
        return new TopTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTrackViewHolder holder, int position) {
        TopTrack topTrack = tracks.get(position);
        TrackResponse track = topTrack.getTrack();
        holder.tvTitle.setText(track.getTitle());
        holder.tvPlayCount.setText(topTrack.getPlayCount() + " plays");
        if (track.getCoverImageName() != null && !track.getCoverImageName().isEmpty()) {
            Glide.with(context)
                .load(com.app.musicapp.helper.UrlHelper.getCoverImageUrl(track.getCoverImageName()))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.imgCover);
        } else {
            holder.imgCover.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    static class TopTrackViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, imgPlayIcon;
        TextView tvTitle, tvPlayCount;
        TopTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imgPlayIcon = itemView.findViewById(R.id.img_play_icon);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
        }
    }
} 