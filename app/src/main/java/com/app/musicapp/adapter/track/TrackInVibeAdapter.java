package com.app.musicapp.adapter.track;

import android.view.*;
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

public class TrackInVibeAdapter extends RecyclerView.Adapter<TrackInVibeAdapter.ViewHolder>{
    private Fragment fragment;
    private List<TrackResponse> trackResponseList;

    public TrackInVibeAdapter(Fragment fragment, List<TrackResponse> trackResponseList) {
        this.fragment = fragment;
        this.trackResponseList = new ArrayList<>(trackResponseList);
    }
    public void updateData(List<TrackResponse> newTracks) {
        this.trackResponseList.clear();
        this.trackResponseList.addAll(newTracks);
        notifyDataSetChanged();
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

        TrackResponse trackResponse = trackResponseList.get(position);
        holder.tvTrackTitle.setText(trackResponse.getTitle());
        holder.tvTrackArtist.setText(trackResponse.getUser().getDisplayName() != null ? trackResponse.getUser().getDisplayName(): "Unknow User");

        if (trackResponse.getCoverImageName() != null) {
            Glide.with(fragment)
                    .load(UrlHelper.getCoverImageUrl(trackResponse.getCoverImageName()))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivTrackImage);
        } else {
            holder.ivTrackImage.setImageResource(R.drawable.logo);
        }

        holder.ivMenu.setOnClickListener(v -> {
            Toast.makeText(fragment.getContext(), "Menu clicked for: " + trackResponse.getTitle(), Toast.LENGTH_SHORT).show();
            SongOptionsBottomSheet bottomSheet = SongOptionsBottomSheet.newInstance(trackResponse);
            bottomSheet.show(fragment.getParentFragmentManager(), bottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return trackResponseList.size();
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
