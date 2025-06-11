package com.app.musicapp.adapter.playlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrackToPlaylistAdapter extends RecyclerView.Adapter<TrackToPlaylistAdapter.ViewHolder> {

    private static final String TAG = "TrackToPlaylistAdapter";

    private final Context context;
    private List<PlaylistResponse> playlists;
    private OnPlaylistSelectedListener listener;
    private TrackResponse trackToAdd;
    private Set<Integer> selectedPositions;
    private Set<Integer> initialTrackPositions;
    private Set<Integer> manuallyUnselectedPositions;

    public TrackToPlaylistAdapter(Context context, List<PlaylistResponse> playlists, TrackResponse trackToAdd) {
        this.context = context;
        this.playlists = playlists;
        this.trackToAdd = trackToAdd;
        this.selectedPositions = new HashSet<>();
        this.initialTrackPositions = new HashSet<>();
        this.manuallyUnselectedPositions = new HashSet<>();
        
        Log.d(TAG, "Constructor - Track to add ID: " + (trackToAdd != null ? trackToAdd.getId() : "null"));
        Log.d(TAG, "Constructor - Number of playlists: " + (playlists != null ? playlists.size() : 0));
        
        // Initialize positions for playlists that contain the track
        if (playlists != null && trackToAdd != null) {
            for (int i = 0; i < playlists.size(); i++) {
                PlaylistResponse playlist = playlists.get(i);
                Log.d(TAG, "Checking playlist " + i + " - ID: " + (playlist != null ? playlist.getId() : "null"));
                
                if (playlist != null && checkIfTrackExists(playlist)) {
                    initialTrackPositions.add(i);
                    selectedPositions.add(i);
                    Log.d(TAG, "Constructor: Track exists in playlist at position " + i);
                }
            }
        }
        
        Log.d(TAG, "Constructor finished - Selected positions: " + selectedPositions);
        Log.d(TAG, "Constructor finished - Initial track positions: " + initialTrackPositions);
    }

    public void setOnPlaylistSelectedListener(OnPlaylistSelectedListener listener) {
        this.listener = listener;
    }

    public void updateData(List<PlaylistResponse> newPlaylists) {
        Log.d(TAG, "updateData called with " + (newPlaylists != null ? newPlaylists.size() : 0) + " playlists");
        Log.d(TAG, "Track to add ID: " + (trackToAdd != null ? trackToAdd.getId() : "null"));
        
        Set<Integer> oldSelectedPositions = new HashSet<>(selectedPositions);
        Set<Integer> oldInitialPositions = new HashSet<>(initialTrackPositions);
        Set<Integer> oldManuallyUnselected = new HashSet<>(manuallyUnselectedPositions);
        
        this.playlists = newPlaylists;
        selectedPositions.clear();
        initialTrackPositions.clear();
        
        // Check each playlist for the track
        if (newPlaylists != null && trackToAdd != null) {
            for (int i = 0; i < newPlaylists.size(); i++) {
                PlaylistResponse playlist = newPlaylists.get(i);
                Log.d(TAG, "Checking playlist " + i + " - " + playlist.getTitle());
                
                boolean hasTrack = checkIfTrackExists(playlist);
                Log.d(TAG, "Playlist " + playlist.getTitle() + " has track: " + hasTrack);
                
                if (hasTrack) {
                    initialTrackPositions.add(i);
                    // Don't select if it was manually unselected before
                    if (!oldManuallyUnselected.contains(i)) {
                        selectedPositions.add(i);
                        Log.d(TAG, "Added position " + i + " to selectedPositions");
                    } else {
                        Log.d(TAG, "Position " + i + " was manually unselected before, not selecting");
                    }
                } else if (oldSelectedPositions.contains(i) && !oldInitialPositions.contains(i)) {
                    // Keep only manual selections that weren't from initial track existence
                    selectedPositions.add(i);
                    Log.d(TAG, "Kept manual selection for position " + i);
                }
            }
        }
        
        Log.d(TAG, "After update - Selected positions: " + selectedPositions);
        Log.d(TAG, "After update - Initial positions: " + initialTrackPositions);
        Log.d(TAG, "After update - Manually unselected: " + manuallyUnselectedPositions);
        
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistResponse playlist = playlists.get(position);
        
        holder.tvPlaylistName.setText(playlist.getTitle());
        holder.tvPlaylistInfo.setText(String.format("%d • Playlist • %d Tracks", 
            123,
            playlist.getPlaylistTrackResponses() != null ? playlist.getPlaylistTrackResponses().size() : 
            (playlist.getPlaylistTracks() != null ? playlist.getPlaylistTracks().size() : 0)));
        
        if (playlist.getImagePath() != null) {
            Glide.with(context)
                .load(UrlHelper.getCoverImageUrl(playlist.getImagePath()))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.ivPlaylistCover);
        } else {
            holder.ivPlaylistCover.setImageResource(R.drawable.logo);
        }

        final int pos = position;
        boolean isInitialTrack = initialTrackPositions.contains(pos);
        boolean isSelected = selectedPositions.contains(pos);
        boolean isManuallyUnselected = manuallyUnselectedPositions.contains(pos);
        
        Log.d(TAG, String.format("onBind position %d - Title: %s, Initial: %b, Selected: %b, ManuallyUnselected: %b", 
            pos, playlist.getTitle(), isInitialTrack, isSelected, isManuallyUnselected));

        // Show radio button and enable interaction for all playlists
        holder.rbSelected.setVisibility(View.VISIBLE);
        holder.rbSelected.setChecked(isSelected);
        holder.itemView.setEnabled(true);
        holder.rbSelected.setEnabled(true);
        
        holder.rbSelected.setOnClickListener(v -> {
            toggleSelection(pos);
            if (listener != null) {
                listener.onPlaylistSelected(playlist);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            toggleSelection(pos);
            if (listener != null) {
                listener.onPlaylistSelected(playlist);
            }
        });
    }

    private void toggleSelection(int position) {
        boolean wasSelected = selectedPositions.contains(position);
        boolean isInitialTrack = initialTrackPositions.contains(position);
        
        Log.d(TAG, String.format("Toggling position %d - Was selected: %b, Is initial track: %b", 
            position, wasSelected, isInitialTrack));
        
        if (wasSelected) {
            selectedPositions.remove(position);
            if (isInitialTrack) {
                manuallyUnselectedPositions.add(position);
                Log.d(TAG, "Added position " + position + " to manually unselected");
            }
        } else {
            selectedPositions.add(position);
            manuallyUnselectedPositions.remove(position);
            Log.d(TAG, "Removed position " + position + " from manually unselected");
        }
        
        Log.d(TAG, "After toggle - Selected positions: " + selectedPositions);
        Log.d(TAG, "After toggle - Manually unselected: " + manuallyUnselectedPositions);
        
        notifyItemChanged(position);
    }

    public List<PlaylistResponse> getSelectedPlaylists() {
        List<PlaylistResponse> selected = new ArrayList<>();
        for (Integer position : selectedPositions) {
            selected.add(playlists.get(position));
        }
        return selected;
    }

    public boolean checkIfTrackExists(PlaylistResponse playlist) {
        if (trackToAdd == null || playlist == null) {
            Log.d(TAG, "checkIfTrackExists: trackToAdd or playlist is null");
            return false;
        }

        // Check in playlistTrackResponses
        if (playlist.getPlaylistTrackResponses() != null) {
            for (TrackResponse track : playlist.getPlaylistTrackResponses()) {
                if (track != null && track.getId() != null && 
                    track.getId().equals(trackToAdd.getId())) {
                    Log.d(TAG, "Track found in playlistTrackResponses - Track ID: " + track.getId());
                    return true;
                }
            }
        }

        // Check in playlistTracks
        if (playlist.getPlaylistTracks() != null) {
            for (TrackResponse track : playlist.getPlaylistTracks()) {
                if (track != null && track.getId() != null && 
                    track.getId().equals(trackToAdd.getId())) {
                    Log.d(TAG, "Track found in playlistTracks - Track ID: " + track.getId());
                    return true;
                }
            }
        }

        Log.d(TAG, "Track not found in playlist: " + playlist.getId());
        return false;
    }

    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size() : 0;
    }

    public interface OnPlaylistSelectedListener {
        void onPlaylistSelected(PlaylistResponse playlist);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaylistCover;
        TextView tvPlaylistName;
        TextView tvPlaylistInfo;
        RadioButton rbSelected;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaylistCover = itemView.findViewById(R.id.iv_playlist_cover);
            tvPlaylistName = itemView.findViewById(R.id.tv_playlist_name);
            tvPlaylistInfo = itemView.findViewById(R.id.tv_playlist_info);
            rbSelected = itemView.findViewById(R.id.rb_selected);
        }
    }
} 