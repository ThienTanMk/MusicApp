package com.app.musicapp.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TrackResponse implements Serializable {
    private String id;
    private String title;
    private String description;
    private String fileName;
    private String coverImageName;
    private LocalDateTime createdAt;
    private String userId;
    private String artist;
    private String duration;
    private String privacy;
    private int countPlay;
    private GenreResponse genre;
    private List<TagResponse> tagResponses;
    private ProfileWithCountFollowResponse user;
    public TrackResponse(String id, String title, String description, String fileName, String coverImageName, LocalDateTime createdAt, String userId, String duration, String privacy, int countPlay, GenreResponse genreResponse, List<TagResponse> tagResponses) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.coverImageName = coverImageName;
        this.createdAt = createdAt;
        this.userId = userId;
        this.duration = duration;
        this.privacy = privacy;
        this.countPlay = countPlay;
        this.genre = genreResponse;
        this.tagResponses = tagResponses;
    }

    public TrackResponse(String id, String title, String description, String fileName, String coverImageName, LocalDateTime createdAt, String userId, String artist, String duration, String privacy, int countPlay, GenreResponse genreResponse, List<TagResponse> tagResponses) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.coverImageName = coverImageName;
        this.createdAt = createdAt;
        this.userId = userId;
        this.artist = artist;
        this.duration = duration;
        this.privacy = privacy;
        this.countPlay = countPlay;
        this.genre = genreResponse;
        this.tagResponses = tagResponses;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCoverImageName() {
        return coverImageName;
    }

    public void setCoverImageName(String coverImageName) {
        this.coverImageName = coverImageName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getCountPlay() {
        return countPlay;
    }

    public void setCountPlay(int countPlay) {
        this.countPlay = countPlay;
    }

    public GenreResponse getGenre() {
        return genre;
    }

    public void setGenre(GenreResponse genreResponse) {
        this.genre = genreResponse;
    }

    public List<TagResponse> getTags() {
        return tagResponses;
    }

    public void setTags(List<TagResponse> tagResponses) {
        this.tagResponses = tagResponses;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setUser(ProfileWithCountFollowResponse user) {
        this.user = user;
    }

    public ProfileWithCountFollowResponse getUser() {
        return this.user;
    }
}
