package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;
import com.bumptech.glide.Glide;

import java.util.List;

public class TrackAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private List<TrackResponse> trackResponseList;
    private int layoutType;
    public TrackAdapter(Fragment fragment, List<TrackResponse> trackResponseList) {
        this.fragment = fragment;
        this.trackResponseList = trackResponseList;
        this.context = fragment.getContext();
        this.inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public int getCount() {
        return trackResponseList.size();
    }

    @Override
    public Object getItem(int i) {
        return trackResponseList.get(i);
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
        TrackResponse trackResponse = trackResponseList.get(i);
        holder.tvTrackTitle.setText(trackResponse.getTitle() != null ? trackResponse.getTitle() : "Unknown Track");
        ProfileWithCountFollowResponse user = trackResponse.getUser();
        if (user != null && user.getDisplayName() != null) {
            holder.tvTrackArtist.setText(user.getDisplayName());
        } else {
//            Log.w("TrackAdapter", "User or displayName is null for track: " +
//                    (trackResponse.getTitle() != null ? trackResponse.getTitle() : "null"));
            holder.tvTrackArtist.setText("Unknown Artist");
        }
        holder.tvPlayCount.setText(formatPlayCount(trackResponse.getCountPlay()));
        holder.tvDuration.setText(trackResponse.getDuration() != null ? trackResponse.getDuration() : "0:00");
        if (trackResponse.getCoverImageName() !=null) {
            Glide.with(context)
                    .load(UrlHelper.getCoverImageUrl(trackResponse.getCoverImageName())) // URL ảnh từ API
                    .placeholder(R.drawable.logo) // ảnh hiển thị trong khi tải
                    .error(R.drawable.logo) // ảnh hiển thị nếu lỗi
                    .into(holder.ivTrackImage);
        } else {
            holder.ivTrackImage.setImageResource(R.drawable.logo);
        }

        // Xử lý sự kiện click cho nút menu
        holder.ivMenu.setOnClickListener(v -> {
            SongOptionsBottomSheet bottomSheet = SongOptionsBottomSheet.newInstance(trackResponse);
            if (fragment instanceof SongOptionsBottomSheet.TrackOptionsListener) {
                bottomSheet.setTrackOptionsListener((SongOptionsBottomSheet.TrackOptionsListener) fragment);
            }
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
