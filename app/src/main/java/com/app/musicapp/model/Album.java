package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Album implements Serializable {
    private String albumTitle;
    private String mainArtists;
    private String genreId;
    private String albumType;
    private List<Tag> tags;
    private String description;
    private String privacy;
    private String albumLink;
    private String imagePath;
    private String userId;
    private String id;
    private LocalDateTime createdAt;
    private List<Track> tracks;
    private Genre genre;

    public Album() {
    }

    public Album(String albumTitle, String mainArtists, String genreId, String albumType, List<Tag> tags, String description, String privacy, String albumLink, String imagePath, String userId, String id, LocalDateTime createdAt, List<Track> tracks, Genre genre) {
        this.albumTitle = albumTitle;
        this.mainArtists = mainArtists;
        this.genreId = genreId;
        this.albumType = albumType;
        this.tags = tags;
        this.description = description;
        this.privacy = privacy;
        this.albumLink = albumLink;
        this.imagePath = imagePath;
        this.userId = userId;
        this.id = id;
        this.createdAt = createdAt;
        this.tracks = tracks;
        this.genre = genre;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getMainArtists() {
        return mainArtists;
    }

    public void setMainArtists(String mainArtists) {
        this.mainArtists = mainArtists;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public String getAlbumLink() {
        return albumLink;
    }

    public void setAlbumLink(String albumLink) {
        this.albumLink = albumLink;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
