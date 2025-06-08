package com.app.musicapp.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackRequest {
    private String id;
    private String title;
    private String description;
    private String genreId;
    private List<String> tagIds;
    private  String privacy;
    private String userId;
    private int countPlay;

    public TrackRequest() {
    }
    public TrackRequest(String title, String description, String genreId, List<String> tagIds, String privacy, String userId, int countPlay) {
        this.title = title;
        this.description = description;
        this.genreId = genreId;
        this.tagIds = tagIds;
        this.privacy = privacy;
        this.userId = userId;
        this.countPlay = countPlay;
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

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
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

    public int getCountPlay() {
        return countPlay;
    }

    public void setCountPlay(int countPlay) {
        this.countPlay = countPlay;
    }
} 