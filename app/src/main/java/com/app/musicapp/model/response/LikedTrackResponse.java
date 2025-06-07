package com.app.musicapp.model.response;

import java.io.Serializable;

public class LikedTrackResponse implements Serializable {
    private String id;
    private String trackId;
    private String userId;

    public LikedTrackResponse(String id, String trackId, String userId) {
        this.id = id;
        this.trackId = trackId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
