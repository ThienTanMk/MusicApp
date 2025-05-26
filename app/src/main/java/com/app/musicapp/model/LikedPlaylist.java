package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LikedPlaylist implements Serializable {
    private String id;
    private String userId;
    private LocalDateTime likedAt;
    private Playlist playlist;

    public LikedPlaylist(String id, String userId, LocalDateTime likedAt, Playlist playlist) {
        this.id = id;
        this.userId = userId;
        this.likedAt = likedAt;
        this.playlist = playlist;
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

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
