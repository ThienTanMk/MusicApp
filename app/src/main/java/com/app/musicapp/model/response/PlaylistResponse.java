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
    private GenreResponse genreResponse;
    private String imagePath;
    private LocalDateTime createdAt;
    private List<TrackResponse> playlistTrackResponses;
    private List<TagResponse> playlistTagResponses;
    private Boolean isLiked;
    ProfileWithCountFollowResponse user;

    public PlaylistResponse(String id, String title, LocalDateTime releaseDate, String description, String privacy, String userId, GenreResponse genreResponse, String imagePath, LocalDateTime createdAt, List<TrackResponse> playlistTrackResponses, List<TagResponse> playlistTagResponses, Boolean isLiked , ProfileWithCountFollowResponse user) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.privacy = privacy;
        this.userId = userId;
        this.genreResponse = genreResponse;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.playlistTrackResponses = playlistTrackResponses;
        this.playlistTagResponses = playlistTagResponses;
        this.isLiked = isLiked;
        this.user = user;
    }

    public GenreResponse getGenreResponse() {
        return genreResponse;
    }

    public void setGenreResponse(GenreResponse genreResponse) {
        this.genreResponse = genreResponse;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isliked) {
        this.isLiked = isliked;
    }

    public List<TrackResponse> getPlaylistTrackResponses() {
        return playlistTrackResponses;
    }

    public void setPlaylistTrackResponses(List<TrackResponse> playlistTrackResponses) {
        this.playlistTrackResponses = playlistTrackResponses;
    }

    public List<TagResponse> getPlaylistTagResponses() {
        return playlistTagResponses;
    }

    public void setPlaylistTagResponses(List<TagResponse> playlistTagResponses) {
        this.playlistTagResponses = playlistTagResponses;
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
        return genreResponse;
    }

    public void setGenre(GenreResponse genreResponse) {
        this.genreResponse = genreResponse;
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
        return playlistTrackResponses;
    }

    public void setPlaylistTracks(List<TrackResponse> playlistTrackResponses) {
        this.playlistTrackResponses = playlistTrackResponses;
    }

    public List<TagResponse> getPlaylistTags() {
        return playlistTagResponses;
    }

    public void setPlaylistTags(List<TagResponse> playlistTagResponses) {
        this.playlistTagResponses = playlistTagResponses;
    }
}
