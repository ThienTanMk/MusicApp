package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Playlist implements Serializable {
    private String id;
    private String title;
    private LocalDateTime releaseDate;
    private String description;
    private String privacy;
    private String userId;
    private Genre genre;
    private String imagePath;
    private LocalDateTime createdAt;
    private List<Track> playlistTracks;
    private List<Tag> playlistTags;

    public Playlist(String id, String title, LocalDateTime releaseDate, String description, String privacy, String userId, Genre genre, String imagePath, LocalDateTime createdAt, List<Track> playlistTracks, List<Tag> playlistTags) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.privacy = privacy;
        this.userId = userId;
        this.genre = genre;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.playlistTracks = playlistTracks;
        this.playlistTags = playlistTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Track> getPlaylistTracks() {
        return playlistTracks;
    }

    public void setPlaylistTracks(List<Track> playlistTracks) {
        this.playlistTracks = playlistTracks;
    }

    public List<Tag> getPlaylistTags() {
        return playlistTags;
    }

    public void setPlaylistTags(List<Tag> playlistTags) {
        this.playlistTags = playlistTags;
    }
}
