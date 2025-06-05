package com.app.musicapp.adapter;

import android.view.*;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.Track;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;

import java.util.List;

public class TrackInVibeAdapter extends RecyclerView.Adapter<TrackInVibeAdapter.ViewHolder>{
    private Fragment fragment;
    private List<Track> trackList;

    public TrackInVibeAdapter(Fragment fragment, List<Track> trackList) {
        this.fragment = fragment;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trackinvibe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Track track = trackList.get(position);
        holder.tvTrackTitle.setText(track.getTitle());
        holder.tvTrackArtist.setText(track.getUserId());

        holder.ivTrackImage.setImageResource(R.drawable.logo);

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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackImage;
        TextView tvTrackTitle;
        TextView tvTrackArtist;
        ImageView ivMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTrackImage = itemView.findViewById(R.id.iv_track_image);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvTrackArtist = itemView.findViewById(R.id.tv_track_artist);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }
    }
}
