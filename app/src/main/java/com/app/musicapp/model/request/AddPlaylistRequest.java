package com.app.musicapp.model.request;

import java.io.Serializable;
import java.util.List;

public class AddPlaylistRequest implements Serializable {
    private String title;
    private String privacy;
    private List<String> trackIds;

    public AddPlaylistRequest() {
    }

    public AddPlaylistRequest(String title, String privacy, List<String> trackIds) {
        this.title = title;
        this.privacy = privacy;
        this.trackIds = trackIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public List<String> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<String> trackIds) {
        this.trackIds = trackIds;
    }
}
