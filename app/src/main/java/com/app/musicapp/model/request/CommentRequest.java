package com.app.musicapp.model.request;

public class CommentRequest {
    String trackId;
    String content;

    public CommentRequest(String trackId, String content) {
        this.trackId = trackId;
        this.content = content;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
