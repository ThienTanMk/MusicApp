package com.app.musicapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.Track;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;

import java.util.List;

public class TrackRVAdapter extends RecyclerView.Adapter<TrackRVAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Track> trackList;

    public TrackRVAdapter(Fragment fragment, List<Track> trackList) {
        this.fragment = fragment;
        this.trackList = trackList;
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
        Track track = trackList.get(position);
        holder.tvTrackTitle.setText(track.getTitle());
        holder.tvTrackArtist.setText(track.getUserId());
        holder.tvPlayCount.setText(formatPlayCount(track.getCountPlay()));
        holder.tvDuration.setText(track.getDuration());
        holder.ivTrackImage.setImageResource(R.drawable.logo);

        // Xử lý sự kiện click cho nút menu (ba chấm)
        holder.ivMenu.setOnClickListener(v -> {
            Toast.makeText(fragment.getContext(), "Menu clicked for: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            SongOptionsBottomSheet bottomSheet = SongOptionsBottomSheet.newInstance(track);
            bottomSheet.show(fragment.getParentFragmentManager(), bottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
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
