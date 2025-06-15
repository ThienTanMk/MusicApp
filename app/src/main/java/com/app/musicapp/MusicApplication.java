package com.app.musicapp;

import android.app.Application;
import com.app.musicapp.api.ApiClient;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;

public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(this);
        FirebaseApp.initializeApp(this);
    }
} 