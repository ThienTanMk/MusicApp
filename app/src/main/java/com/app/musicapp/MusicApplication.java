package com.app.musicapp;

import android.app.Application;
import com.app.musicapp.api.ApiClient;

public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(this);
    }
} 