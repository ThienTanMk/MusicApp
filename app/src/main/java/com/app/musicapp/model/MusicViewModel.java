package com.app.musicapp.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.musicapp.model.response.TrackResponse;

public class MusicViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private final MutableLiveData<TrackResponse> currentTrack = new MutableLiveData<>();
    private final MutableLiveData<Integer> progress = new MutableLiveData<>();
    public void setPlaying(boolean playing) {
        isPlaying.setValue(playing);
    }

    public MutableLiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    public void setCurrentTrack(TrackResponse track) {
        currentTrack.setValue(track);
    }

    public MutableLiveData<TrackResponse> getCurrentTrack() {
        return currentTrack;
    }
    public void setProgress(int progressValue) {
        progress.setValue(progressValue);
    }
    public MutableLiveData<Integer> getProgress() {
        return progress;
    }
}
