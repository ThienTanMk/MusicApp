package com.app.musicapp.model;

import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class AlbumResponse implements Serializable {
    private String albumTitle;
    private String mainArtists;
    private String genreId;
    private String albumType;
    private List<TagResponse> tagResponses;
    private String description;
    private String privacy;
    private String albumLink;
    private String imagePath;
    private String userId;
    private String id;
    private LocalDateTime createdAt;
    private List<TrackResponse> trackResponses;
    private GenreResponse genreResponse;
    private Boolean isLiked = false;
    public AlbumResponse() {
    }

    public AlbumResponse(String albumTitle, String mainArtists, String genreId, String albumType, List<TagResponse> tagResponses, String description, String privacy, String albumLink, String imagePath, String userId, String id, LocalDateTime createdAt, List<TrackResponse> trackResponses, GenreResponse genreResponse) {
        this.albumTitle = albumTitle;
        this.mainArtists = mainArtists;
        this.genreId = genreId;
        this.albumType = albumType;
        this.tagResponses = tagResponses;
        this.description = description;
        this.privacy = privacy;
        this.albumLink = albumLink;
        this.imagePath = imagePath;
        this.userId = userId;
        this.id = id;
        this.createdAt = createdAt;
        this.trackResponses = trackResponses;
        this.genreResponse = genreResponse;
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

    public List<TagResponse> getTags() {
        return tagResponses;
    }

    public void setTags(List<TagResponse> tagResponses) {
        this.tagResponses = tagResponses;
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

    public List<TrackResponse> getTracks() {
        return trackResponses;
    }

    public void setTracks(List<TrackResponse> trackResponses) {
        this.trackResponses = trackResponses;
    }

    public GenreResponse getGenre() {
        return genreResponse;
    }

    public void setGenre(GenreResponse genreResponse) {
        this.genreResponse = genreResponse;
    }
}
