package com.app.musicapp.adapter.track;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class TrackRVAdapter extends RecyclerView.Adapter<TrackRVAdapter.ViewHolder> {
    private Fragment fragment;
    private List<TrackResponse> trackResponseList;
    private OnTrackClickListener onTrackClickListener;
    private OnTrackOptionsClickListener onTrackOptionsClickListener;

    public interface OnTrackClickListener {
        void onTrackClick(TrackResponse track);
    }

    public interface OnTrackOptionsClickListener {
        void onTrackOptionsClick(TrackResponse track);
    }

    public TrackRVAdapter(Fragment fragment, List<TrackResponse> trackResponseList) {
        this.fragment = fragment;
        this.trackResponseList = new ArrayList<>(trackResponseList);
    }

    public void setOnTrackClickListener(OnTrackClickListener listener) {
        this.onTrackClickListener = listener;
    }

    public void setOnTrackOptionsClickListener(OnTrackOptionsClickListener listener) {
        this.onTrackOptionsClickListener = listener;
    }

    public void updateData(List<TrackResponse> newTracks) {
        this.trackResponseList.clear();
        if (newTracks != null) {
            this.trackResponseList.addAll(newTracks);
        }
        notifyDataSetChanged();
    }

    public void removeTrack(TrackResponse track) {
        int position = trackResponseList.indexOf(track);
        if (position != -1) {
            trackResponseList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @NonNull
    @Override
    public TrackRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackRVAdapter.ViewHolder holder, int position) {
        TrackResponse trackResponse = trackResponseList.get(position);
        holder.tvTrackTitle.setText(trackResponse.getTitle());
        holder.tvTrackArtist.setText(trackResponse.getUser().getDisplayName());
        holder.tvPlayCount.setText(formatPlayCount(trackResponse.getCountPlay()));
        holder.tvDuration.setText(trackResponse.getDuration());

        if (trackResponse.getCoverImageName() != null) {
            Glide.with(fragment)
                    .load(UrlHelper.getCoverImageUrl(trackResponse.getCoverImageName()))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivTrackImage);
        } else {
            holder.ivTrackImage.setImageResource(R.drawable.logo);
        }

        // Add click listener for the whole item
        holder.itemView.setOnClickListener(v -> {
            if (onTrackClickListener != null) {
                onTrackClickListener.onTrackClick(trackResponse);
            }
        });

        // Handle menu click
        holder.ivMenu.setOnClickListener(v -> {
            if (onTrackOptionsClickListener != null) {
                onTrackOptionsClickListener.onTrackOptionsClick(trackResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackResponseList.size();
    }
    private String formatPlayCount(int count) {
        if (count >= 1_000_000) {
            return String.format("%.1fM", count / 1_000_000.0);
        } else if (count >= 1_000) {
            return String.format("%dK", count / 1_000);
        } else {
            return String.valueOf(count);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackImage;
        TextView tvTrackTitle;
        TextView tvTrackArtist;
        TextView tvPlayCount;
        TextView tvDuration;
        ImageView ivMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTrackImage = itemView.findViewById(R.id.iv_track_image);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvTrackArtist = itemView.findViewById(R.id.tv_track_artist);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }
    }
}
