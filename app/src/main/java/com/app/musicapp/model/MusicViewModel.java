package com.app.musicapp.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.musicapp.model.response.TrackResponse;

public class MusicViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private final MutableLiveData<TrackResponse> currentTrack = new MutableLiveData<>();
    private final MutableLiveData<Integer> progress = new MutableLiveData<>();
    private final MutableLiveData<Integer> duration = new MutableLiveData<>();

    private final MutableLiveData<String> playBackMode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isShuffle = new MutableLiveData<>();
    public void setShuffle(boolean isShuffle){
        this.isShuffle.postValue(isShuffle);
    }
    public MutableLiveData<Boolean> getIsShuffle() {
        return isShuffle;
    }
    public void setPlayBackMode(String playBackMode){
        this.playBackMode.postValue(playBackMode);
    }
    public MutableLiveData<String> getPlayBackMode(){
        return playBackMode;
    }

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
    public MutableLiveData<Integer> getDuration() {
        return duration;
    }
    public void setDuration(int duration){
        this.duration.postValue(duration);
    }
}
