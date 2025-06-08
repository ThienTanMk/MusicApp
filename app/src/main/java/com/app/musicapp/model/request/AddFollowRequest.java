package com.app.musicapp.model.request;

public class AddFollowRequest {
    String followingId;

    public AddFollowRequest(String followingId) {
        this.followingId = followingId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}
