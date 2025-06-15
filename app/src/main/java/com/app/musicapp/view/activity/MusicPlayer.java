package com.app.musicapp.view.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.AddFollowRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPlayer extends AppCompatActivity {
    TextView trackTitle, artistName, durationPlayed, durationTotal, headerTrackTitle, commentCount, likeCount;
    ImageView coverArt, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn, likeBtn,followBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    LinearLayout comment;
    ImageView nextUp;
    MusicService  musicService;
    private boolean isLiked = false, isFollowing = false;

    private String convertIntToString(Integer value){
        if(value<1000)
            return String.valueOf(value);
        Double val = value/1000.0;
        val = Math.floor(val*10)/10;
        return String.valueOf(val)+"k";
    }
    private void updateCommentCount(String trackId){
        ApiClient.getCommentService().getCommentCountByTrackId(trackId).enqueue(new Callback<ApiResponse<Integer>>() {

            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if(response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                    Integer count = response.body().getData();
                    commentCount.setText(convertIntToString(count));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {

            }
        });
    }
    private void updateFollow(String userId){
        String logInUserId = SharedPreferencesManager.getInstance(getBaseContext()).getUserId();
        if(logInUserId.equals(userId)){
            followBtn.setColorFilter(R.color.gray);
            followBtn.setImageResource(R.drawable.ic_person);
            isFollowing = true;
            return;
        }
        ApiClient.getUserProfileApiService().isFollowing(logInUserId,userId).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful()&&response.body().getData()){
                    followBtn.setImageResource(R.drawable.ic_person);
                    isFollowing = true;
                }
                else{
                    isFollowing = false;
                    followBtn.setImageResource(R.drawable.user_plus_icon);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {

            }
        });
    }
    private void updateLikeCount(String trackId){
        ApiClient.getLikedTrackService().getTrackLikeCount(trackId).enqueue(new Callback<ApiResponse<Integer>>() {

            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if(response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                    Integer count = response.body().getData();
                    likeCount.setText(convertIntToString(count));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                likeCount.setText(convertIntToString(0));
            }
        });
        ApiClient.getLikedTrackService().isLiked(trackId).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                    isLiked = response.body().getData();
                    if(isLiked){
                            likeBtn.setImageResource(R.drawable.red_heart_icon);
                    }
                    else{
                        likeBtn.setImageResource(R.drawable.heart_icon);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {

            }
        });
    }
    private void updatePlayBackModeBtn(String action){
        if(action==null)return;
        repeatBtn.setColorFilter(this.getResources().getColor(R.color.white));
        repeatBtn.setImageResource(R.drawable.repeat_icon);

        if(MusicService.REPEAT_ONE.equals(action)){
            repeatBtn.setImageResource(R.drawable.repeat_one_icon);
            repeatBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
        }
        else if(MusicService.REPEAT_ALL.equals(action)){
            repeatBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
        }
    }

    private void updateTrackInfo(TrackResponse currentTrackResponse){
        if(currentTrackResponse==null) return;
        trackTitle.setText(currentTrackResponse.getTitle());
        headerTrackTitle.setText(currentTrackResponse.getTitle());
        artistName.setText(currentTrackResponse.getUser().getDisplayName());

        Glide.with(MusicPlayer.this)
                .load(UrlHelper.getCoverImageUrl(currentTrackResponse.getCoverImageName()))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(coverArt);

        updateCommentCount(currentTrackResponse.getId());
        updateLikeCount(currentTrackResponse.getId());
        updateFollow(currentTrackResponse.getUserId());

        if(musicService.isPlaying()) playPauseBtn.setImageResource(R.drawable.play_icon);
        else playPauseBtn.setImageResource(R.drawable.pause_icon);
        if(musicService.isShuffle()){
            shuffleBtn.setColorFilter(R.color.soundcloud);
        }
        else{
            shuffleBtn.setColorFilter(R.color.white);
        }
        updatePlayBackModeBtn(musicService.getPlayBackMode());
    }
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            updateTrackInfo(musicService.getCurrentTrack());
            setListener();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_player);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private String formattedTime(int mCurrentPosition) {
        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalout = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalout;
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        updatePlayBackModeBtn(musicService.getPlayBackMode());
    }
//===========================================================================================

    private void setListener(){
        musicService.getMusicViewModel().getProgress().observe(this,progress->{
            seekBar.setProgress(progress);
            durationPlayed.setText(formattedTime(progress/1000));
        });
        musicService.getMusicViewModel().getDuration().observe(this,duration->{
            seekBar.setMax(duration);
            durationTotal.setText(formattedTime(duration/1000));
        });
        musicService.getMusicViewModel().getIsPlaying().observe(this,isPlaying->{
            if(isPlaying){
                playPauseBtn.setImageResource(R.drawable.play_icon);
            }
            else{
                playPauseBtn.setImageResource(R.drawable.pause_icon);
            }
        });
        musicService.getMusicViewModel().getIsShuffle().observe(this,isShuffle->{
            Log.i("a",String.valueOf(isShuffle));
            if(isShuffle){
                shuffleBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
            }
            else {
                shuffleBtn.setColorFilter(this.getResources().getColor(R.color.white));
            }
        });
        musicService.getMusicViewModel().getPlayBackMode().observe(this,action->{
            if(action==null)return;
            updatePlayBackModeBtn(action);
        });
        musicService.getMusicViewModel().getCurrentTrack().observe(this,track->{
            if(track==null)return;
            updateTrackInfo(track);
        });
    }
    private void initViews() {
        trackTitle = findViewById(R.id.track_title);
        artistName = findViewById(R.id.artist);
        durationPlayed = findViewById(R.id.durationPlayed);
        durationTotal = findViewById(R.id.durationTotal);
        coverArt = findViewById(R.id.cover_image);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        repeatBtn = findViewById(R.id.repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBar);
        comment = findViewById(R.id.commentWrapper);
        nextUp = findViewById(R.id.nextUp);
        commentCount = findViewById(R.id.text_comment_count);
        likeCount = findViewById(R.id.text_like_count);
        likeBtn = findViewById(R.id.image_like_btn);
        followBtn = findViewById(R.id.image_follow);
        headerTrackTitle = findViewById(R.id.text_track_title_header);
        followBtn.setOnClickListener(v->{
            var track = musicService.getCurrentTrack();
            track.getUser().getUserId();
        });

        likeBtn.setOnClickListener(v->{
            var track = musicService.getCurrentTrack();
            if(isLiked){
                ApiClient.getLikedTrackService().unlikeTrack(track.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                        likeBtn.setImageResource(R.drawable.heart_icon);
                        track.setLiked(false);
                        updateLikeCount(track.getId());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {}
                });
            }
            else{
                ApiClient.getLikedTrackService().likeTrack(track.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                        likeBtn.setImageResource(R.drawable.red_heart_icon);
                        track.setLiked(true);
                        updateLikeCount(track.getId());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {}
                });
            }

        });

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService==null) {
                    Toast.makeText(getBaseContext(),"Can not play now",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!musicService.isPlaying()){
                    musicService.playCurrentMusic();
                    playPauseBtn.setImageResource(R.drawable.play_icon);
                }
                else{
                    musicService.pauseCurrentMusic();
                    playPauseBtn.setImageResource(R.drawable.pause_icon);
                }

            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService==null){
                    Toast.makeText(getBaseContext(),"Can not open comment. Try again later",Toast.LENGTH_SHORT).show();
                    return;
                }
                var currentTrack = musicService.getCurrentTrack();
                Intent intent = new Intent(MusicPlayer.this,CommentActivity.class);
                intent.putExtra("track_id",currentTrack.getId());
                intent.putExtra("track_title",currentTrack.getTitle());
                intent.putExtra("track_artist",currentTrack.getUser()==null?"loading":currentTrack.getUser().getDisplayName());
                intent.putExtra("track_cover",currentTrack.getCoverImageName());
                startActivity(intent);
            }
        });
        nextUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayer.this, NextUpActivity.class);
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer.this.finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    musicService.updateProgressTime(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = MusicService.PLAY_ONCE;
                if(musicService.getPlayBackMode().equals(MusicService.PLAY_ONCE))
                    action = MusicService.REPEAT_ONE;
                else if(musicService.getPlayBackMode().equals(MusicService.REPEAT_ONE))
                    action = MusicService.REPEAT_ALL;
                else if(musicService.getPlayBackMode().equals(MusicService.REPEAT_ALL))
                    action = MusicService.PLAY_ONCE;

                musicService.setPlayBackMode(action);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.setShuffle(!musicService.isShuffle());
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.playNext();
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicService.playBack();
            }
        });
    }

    private void toggleFollow(String userId){
        String logInUserId = SharedPreferencesManager.getInstance(getBaseContext()).getUserId();
        if(logInUserId.equals(userId)) return;
        if(isFollowing)
        ApiClient.getUserService().unfollow(userId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful()){
                    isFollowing = false;
                    followBtn.setImageResource(R.drawable.user_plus_icon);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });
        else ApiClient.getUserService().follow(new AddFollowRequest(userId)).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                isFollowing = true;
                followBtn.setImageResource(R.drawable.ic_person);
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {};
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(connection);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}