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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.app.musicapp.R;
import com.app.musicapp.adapter.HomePageAdapter;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.MusicViewModel;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewpager;
    BottomNavigationView mBottomNavigationView;

    ImageView likeBtn, playBtn, nextBtn, preBtn, trackCover;

    LinearLayout miniPlayer;
    TextView artistName, trackTitle;

    MusicService  musicService;
    private Boolean isBound = false;

    private void updateTrackInfo(TrackResponse track){
        if (isFinishing() || isDestroyed()) return;
        if(track==null) return;
        artistName.setText(track.getUser().getDisplayName());
        trackTitle.setText(track.getTitle());
        Glide.with(this)
                .load(UrlHelper.getCoverImageUrl(track.getCoverImageName()))
                .placeholder(R.drawable.logo)
                .into(trackCover);
        if(musicService.isPlaying())
            playBtn.setImageResource(R.drawable.play_icon);
        else
            playBtn.setImageResource(R.drawable.pause_icon);

        miniPlayer.setVisibility(View.VISIBLE);
    }
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;

            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();

            setListener();

            // music service ko co nhac
            if(musicService.getNextUpItems()==null||musicService.getNextUpItems().isEmpty()) return;

            miniPlayer.setVisibility(View.VISIBLE);

            updateTrackInfo(musicService.getCurrentTrack());
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        likeBtn = findViewById(R.id.image_like_btn);
        playBtn = findViewById(R.id.image_play_btn);
        nextBtn = findViewById(R.id.image_next_btn);
        preBtn = findViewById(R.id.image_pre_btn);
        trackCover = findViewById(R.id.image_track_cover);
        miniPlayer = findViewById(R.id.mini_player);
        miniPlayer.setVisibility(View.GONE);

        miniPlayer.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
            startActivity(intent);
        });
        playBtn.setOnClickListener(v->{
            if(musicService==null) return;
            if(musicService.isPlaying())
                musicService.pauseCurrentMusic();
            else musicService.playCurrentMusic();
        });
        preBtn.setOnClickListener(v->{
            if(musicService==null) return;
            musicService.playBack();
        });
        nextBtn.setOnClickListener(v->{
            if(musicService==null) return;
            musicService.playNext();
        });


        artistName = findViewById(R.id.text_track_artist);
        trackTitle = findViewById(R.id.text_track_title);

        mViewpager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        HomePageAdapter viewpagerAdater = new HomePageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewpager.setAdapter(viewpagerAdater);
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.search).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.library).setChecked(true);
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Kiểm tra nếu fragment_container đang hiển thị
                View fragmentContainer = findViewById(R.id.fragment_container);
                if (fragmentContainer.getVisibility() == View.VISIBLE) {
                    // Hiển thị lại ViewPager và ẩn fragment_container
                    mViewpager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                    // Xóa fragment trong fragment_container
                    getSupportFragmentManager().popBackStack();
                }

                switch (item.getItemId()) {
                    case R.id.home:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.search:
                        mViewpager.setCurrentItem(1);
                        break;
                    case R.id.library:
                        mViewpager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFm =  getSupportFragmentManager().findFragmentById((R.id.fragment_container));
            View fmContainter = findViewById(R.id.fragment_container);
            if(currentFm == null){
                mViewpager.setVisibility(View.VISIBLE);
                fmContainter.setVisibility(View.GONE);
            }else {
                mViewpager.setVisibility(View.GONE);
                fmContainter.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setListener(){
        musicService.getMusicViewModel().getCurrentTrack().observe(this,track->{
            if(track==null) return;
            updateTrackInfo(track);
        });
        musicService.getMusicViewModel().getIsPlaying().observe(this,isPlaying->{
            if(isPlaying){
                playBtn.setImageResource(R.drawable.play_icon);
            }
            else{
                playBtn.setImageResource(R.drawable.pause_icon);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    public void setViewPagerPosition(int position) {
        if (mViewpager != null) {
            mViewpager.setCurrentItem(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(isBound)
                unbindService(connection);
        }
        catch (Exception ex){

        }
    }

    public MusicService getMusicService(){
        return musicService;
    }
}