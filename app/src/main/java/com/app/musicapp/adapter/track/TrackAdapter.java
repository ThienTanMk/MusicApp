package com.app.musicapp.adapter.track;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;
import com.app.musicapp.view.activity.MainActivity;
import com.app.musicapp.view.fragment.track.SongOptionsBottomSheet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private List<TrackResponse> trackResponseList;
    private int layoutType;

    private View playingItem;
    private MusicService musicService;
    private String currentDisplayname;


    private Map<String,View> trackIdToView;

    public TrackAdapter(Fragment fragment, List<TrackResponse> trackResponseList) {
        this.fragment = fragment;
        this.trackResponseList = new ArrayList<>(trackResponseList);
        this.context = fragment.getContext();
        this.inflater = LayoutInflater.from(fragment.getContext());
        if(fragment.getContext() instanceof MainActivity){
            musicService = ((MainActivity) fragment.getContext()).getMusicService();
        }
        if(musicService!=null&&musicService.getMusicViewModel()!=null){
            musicService.getMusicViewModel().getIsPlaying().observe(fragment.getViewLifecycleOwner(),isPlaying -> {
                changeCurrentPlayedView();
            });
            musicService.getMusicViewModel().getCurrentTrack().observe(fragment.getViewLifecycleOwner(),trackResponse -> {
               changeCurrentPlayedView();
            });
        }
        trackIdToView = new HashMap<>();
    }
    // Phương thức mới để cập nhật danh sách
    public void updateTracks(List<TrackResponse> newTracks) {
        this.trackResponseList.clear();
        this.trackResponseList.addAll(newTracks);
        Log.d("TrackAdapter", "Updated tracks: " + trackResponseList.size());
        notifyDataSetChanged();
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

    // receipt from outside
    public void changeCurrentPlayedView(){

        if(musicService==null) return;
        if(trackResponseList==null|| trackResponseList.isEmpty())return;
        TrackResponse trackResponse = musicService.getCurrentTrack();
        if(trackResponse==null) return;
        if(!trackIdToView.containsKey(trackResponse.getId())){

            if(playingItem!=null){
                ViewHolder holder = (ViewHolder) playingItem.getTag();
                holder.tvTrackArtist.setText(currentDisplayname);
                holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_background));
            }
            return;
        }

        int index = -1;
        for(int i =0;i< trackResponseList.size();i++){
            if(trackResponseList.get(i).getId().equals(trackResponse.getId())){
                index = i;
                break;
            }
        }


        currentDisplayname = trackResponse.getUser().getDisplayName();

        View view = trackIdToView.get(trackResponse.getId());
        ViewHolder holder = (ViewHolder) view.getTag();
        if(musicService.isPlaying()){
            holder.tvTrackArtist.setText("Now playing");
        }
        else{
            holder.tvTrackArtist.setText("Pause");
        }
        holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));

        if(playingItem!=null&&playingItem!=view){
            ViewHolder temp_holder = (ViewHolder) playingItem.getTag();
            temp_holder.tvTrackArtist.setText(currentDisplayname);
            temp_holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_background));
        }

        playingItem = view;
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
        TrackResponse trackResponse = trackResponseList.get(i);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService==null) return;
                musicService.setNextUpItems(trackResponseList);
                musicService.playMusicAtIndex(i);
                currentDisplayname = trackResponse.getUser().getDisplayName();
                holder.tvTrackArtist.setText("Now playing");
                holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));

                if(playingItem!=null&&playingItem!=v){
                    ViewHolder temp_holder = (ViewHolder) playingItem.getTag();
                    temp_holder.tvTrackArtist.setText(currentDisplayname);
                    temp_holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_background));
                }

                playingItem = v;
            }
        });

        // Gán dữ liệu
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

        // xu ly nghe nhac
        if(musicService!=null){
            if(musicService.getCurrentTrack()!=null){
                if(musicService.getCurrentTrack().getId().equals(trackResponse.getId())){
                    currentDisplayname = trackResponse.getUser().getDisplayName();

                    playingItem = view;
                    if(musicService.isPlaying()){
                        holder.tvTrackArtist.setText("Now playing");
                    }
                    else{
                        holder.tvTrackArtist.setText("Pause");
                    }
                    holder.tvTrackArtist.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));

                }
            }
        }

        trackIdToView.putIfAbsent(trackResponse.getId(),view);
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
