package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.model.Track;
import com.app.musicapp.view.fragment.LibraryPageFragment;
import com.app.musicapp.view.fragment.SongOptionsBottomSheet;

import java.util.List;

public class TrackAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private List<Track> trackList;
    private int layoutType;
    public TrackAdapter(Fragment fragment, List<Track> trackList) {
        this.fragment = fragment;
        this.trackList = trackList;
        this.context = fragment.getContext();
        this.inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int i) {
        return trackList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_track, viewGroup, false);

            holder = new ViewHolder();
            holder.ivTrackImage = view.findViewById(R.id.iv_track_image);
            holder.tvTrackTitle = view.findViewById(R.id.tv_track_title);
            holder.tvTrackArtist = view.findViewById(R.id.tv_track_artist);
            holder.tvPlayCount = view.findViewById(R.id.tv_play_count);
            holder.tvDuration = view.findViewById(R.id.tv_duration);
            holder.ivMenu = view.findViewById(R.id.iv_menu);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Gán dữ liệu
        Track track = trackList.get(i);
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

        return view;
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

    static class ViewHolder {
        ImageView ivTrackImage;
        TextView tvTrackTitle;
        TextView tvTrackArtist;
        TextView tvPlayCount;
        TextView tvDuration;
        ImageView ivMenu;
    }
}
