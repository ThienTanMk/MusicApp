package com.app.musicapp.model.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UpdatePlaylistInfoRequest extends AddPlaylistRequest implements Serializable {
    private String id;
    private LocalDateTime releaseDate;
    private String description;
    private List<String> tagIds;

    public UpdatePlaylistInfoRequest(String title, String privacy, List<String> trackIds, String id, LocalDateTime releaseDate, String description, List<String> tagIds) {
        super(title, privacy, trackIds);
        this.id = id;
        this.releaseDate = releaseDate;
        this.description = description;
        this.tagIds = tagIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }
}
