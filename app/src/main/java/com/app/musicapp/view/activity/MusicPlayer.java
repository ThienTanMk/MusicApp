package com.app.musicapp.view.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.Track;
import com.app.musicapp.service.MusicService;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MusicPlayer extends AppCompatActivity {
    TextView songName, artistName, durationPlayed, durationTotal, headerTrackName;
    ImageView coverArt, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;

    LinearLayout comment;
    ImageView nextUp;
    private boolean isPlaying = false;
    private Track currentTrack;
    MusicService  musicService;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MusicService.ACTION_UPDATE_SEEKBAR_PROGRESS.equals(action)){
                int currentPosition = intent.getIntExtra("current_position",0)/1000;
                int duration = intent.getIntExtra("duration",0)/1000;
                seekBar.setProgress(currentPosition);
                durationPlayed.setText(formattedTime(currentPosition));
                if(seekBar.getMax()!=duration){
                    seekBar.setMax(duration);
                    durationTotal.setText(formattedTime(duration));
                }

            }else if(MusicService.ACTION_CHANGED_CURRENT_TRACK.equals(action)){
                Log.i("TAG", "onReceive: a");
                currentTrack = musicService.getCurrentTrack();
                songName.setText(currentTrack.getTitle());
                artistName.setText(currentTrack.getArtist());
            }
            else if(MusicService.ACTION_PLAY.equals(action)&&!isPlaying){
                playPauseBtn.setImageResource(R.drawable.play_icon);
                isPlaying=true;
            }
            else if(MusicService.ACTION_PAUSE.equals(action)&&isPlaying){
                playPauseBtn.setImageResource(R.drawable.pause_icon);
                isPlaying=false;
            }
            else if(MusicService.PLAY_ONCE.equals(action)||MusicService.REPEAT_ALL.equals(action)||MusicService.REPEAT_ONE.equals(action)||MusicService.SHUFFLE.equals(action)){
                updatePlayBackModeBtn(action);
            }
        }
    };

    private void updatePlayBackModeBtn(String action){
        repeatBtn.setColorFilter(this.getResources().getColor(R.color.white));
        repeatBtn.setImageResource(R.drawable.repeat_icon);
        shuffleBtn.setColorFilter(this.getResources().getColor(R.color.white));

        if(MusicService.REPEAT_ONE.equals(action)){
            repeatBtn.setImageResource(R.drawable.repeat_one_icon);
            repeatBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
        }
        else if(MusicService.REPEAT_ALL.equals(action)){
            repeatBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
        }
        else if(MusicService.SHUFFLE.equals(action)){
            shuffleBtn.setColorFilter(this.getResources().getColor(R.color.soundcloud));
        }
    }
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            currentTrack = musicService.getCurrentTrack();
            songName.setText(currentTrack.getTitle());
            headerTrackName.setText(currentTrack.getTitle());
            artistName.setText(currentTrack.getArtist());
            Glide.with(MusicPlayer.this).load(UrlHelper.getCoverImageUrl(currentTrack.getCoverImageName())).into(coverArt);
            updatePlayBackModeBtn(musicService.getPlayBackMode());
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

        IntentFilter filter = new IntentFilter(MusicService.ACTION_UPDATE_SEEKBAR_PROGRESS);
        filter.addAction(MusicService.ACTION_CHANGED_CURRENT_TRACK);
        filter.addAction(MusicService.ACTION_PLAY);
        filter.addAction(MusicService.ACTION_PAUSE);
        filter.addAction(MusicService.PLAY_ONCE);
        filter.addAction(MusicService.REPEAT_ALL);
        filter.addAction(MusicService.REPEAT_ONE);
        filter.addAction(MusicService.SHUFFLE);
        registerReceiver(receiver, filter,Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startForegroundService(intent);
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

    private void initViews() {
        songName = findViewById(R.id.track_name);
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
        headerTrackName = findViewById(R.id.tv_track_name_header);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService==null) return;
                if(!isPlaying){
                    musicService.playCurrentMusic();
                    playPauseBtn.setImageResource(R.drawable.play_icon);
                }
                else{
                    musicService.pauseCurrentMusic();
                    playPauseBtn.setImageResource(R.drawable.pause_icon);
                }
                isPlaying = !isPlaying;
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayer.this,CommentActivity.class);
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
                String action = MusicService.SHUFFLE;
                if(musicService.getPlayBackMode().equals(MusicService.SHUFFLE))
                    action = MusicService.PLAY_ONCE;
                musicService.setPlayBackMode(action);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}