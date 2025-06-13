package com.app.musicapp.model;

import com.app.musicapp.model.response.TrackResponse;

import java.io.Serializable;

public class TopTrack implements Serializable {
    private TrackResponse track;
    private Integer playCount;

    public TopTrack(TrackResponse track, Integer playCount) {
        this.track = track;
        this.playCount = playCount;
    }

    public TrackResponse getTrack() {
        return track;
    }

    public void setTrack(TrackResponse track) {
        this.track = track;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }
}
