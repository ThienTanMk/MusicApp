package com.app.musicapp.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.musicapp.R;
import com.app.musicapp.adapter.NextUpAdapter;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;

import java.util.List;

public class NextUpActivity extends AppCompatActivity {
    private ImageView closeButton;
    ListView nextUpListView;
    List<TrackResponse> nextUpItems;
    NextUpAdapter nextUpAdapter;
    MusicService musicService;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            nextUpListView = findViewById(R.id.next_up_listview);
            nextUpItems = musicService.getNextUpItems();
            nextUpAdapter = new NextUpAdapter(nextUpItems,NextUpActivity.this,musicService);
            nextUpListView.setAdapter(nextUpAdapter);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.initView();
    }
    private void initView(){
        this.closeButton = findViewById(R.id.image_nextup_close_button);
        this.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextUpActivity.this.finish();
            }
        });
    }
}