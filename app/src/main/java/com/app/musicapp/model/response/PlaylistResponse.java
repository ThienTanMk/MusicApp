package com.app.musicapp.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



public class PlaylistResponse implements Serializable {
    private String id;
    private String title;
    private LocalDateTime releaseDate;
    private String description;
    private String privacy;
    private String userId;
    private GenreResponse genre;
    private String imagePath;
    private LocalDateTime createdAt;
    private List<TrackResponse> playlistTracks;
    private List<TagResponse> playlistTags;
    private Boolean isLiked;
    private ProfileWithCountFollowResponse user;

    public PlaylistResponse(String id, String title, LocalDateTime releaseDate, String description, String privacy, String userId, GenreResponse genreResponse, String imagePath, LocalDateTime createdAt, List<TrackResponse> playlistTrackResponses, List<TagResponse> playlistTagResponses, Boolean isLiked , ProfileWithCountFollowResponse user) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.privacy = privacy;
        this.userId = userId;
        this.genre = genreResponse;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.playlistTracks = playlistTrackResponses;
        this.playlistTags = playlistTagResponses;
        this.isLiked = isLiked;
        this.user = user;
    }

    public GenreResponse getGenreResponse() {
        return genre;
    }

    public void setGenreResponse(GenreResponse genreResponse) {
        this.genre = genreResponse;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isliked) {
        this.isLiked = isliked;
    }

    public List<TrackResponse> getPlaylistTrackResponses() {
        return playlistTracks;
    }

    public void setPlaylistTrackResponses(List<TrackResponse> playlistTrackResponses) {
        this.playlistTracks = playlistTrackResponses;
    }

    public List<TagResponse> getPlaylistTagResponses() {
        return playlistTags;
    }

    public void setPlaylistTagResponses(List<TagResponse> playlistTagResponses) {
        this.playlistTags = playlistTagResponses;
    }

    public ProfileWithCountFollowResponse getUser() {
        return user;
    }

    public void setUser(ProfileWithCountFollowResponse user) {
        this.user = user;
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

    public GenreResponse getGenre() {
        return genre;
    }

    public void setGenre(GenreResponse genreResponse) {
        this.genre = genreResponse;
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

    public List<TrackResponse> getPlaylistTracks() {
        return playlistTracks;
    }

    public void setPlaylistTracks(List<TrackResponse> playlistTrackResponses) {
        this.playlistTracks = playlistTrackResponses;
    }

    public List<TagResponse> getPlaylistTags() {
        return playlistTags;
    }

    public void setPlaylistTags(List<TagResponse> playlistTagResponses) {
        this.playlistTags = playlistTagResponses;
    }
}
