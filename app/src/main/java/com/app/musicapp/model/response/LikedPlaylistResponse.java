package com.app.musicapp.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LikedPlaylistResponse implements Serializable {
    private String id;
    private String userId;
    private LocalDateTime likedAt;
    private PlaylistResponse playlistResponse;

    public LikedPlaylistResponse(String id, String userId, LocalDateTime likedAt, PlaylistResponse playlistResponse) {
        this.id = id;
        this.userId = userId;
        this.likedAt = likedAt;
        this.playlistResponse = playlistResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }

    public PlaylistResponse getPlaylistResponse() {
        return playlistResponse;
    }

    public void setPlaylistResponse(PlaylistResponse playlistResponse) {
        this.playlistResponse = playlistResponse;
    }
}
