package com.app.musicapp.model;

import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.UserProfileResponse;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Follows implements Serializable {
    private String id;
    private UserProfileResponse follower;
    private ProfileWithCountFollowResponse following;
    private LocalDateTime followedAt;

    public Follows() {}

    public Follows(String id, UserProfileResponse follower, ProfileWithCountFollowResponse following, LocalDateTime followedAt) {
        this.id = id;
        this.follower = follower;
        this.following = following;
        this.followedAt = followedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfileResponse getFollower() {
        return follower;
    }

    public void setFollower(UserProfileResponse follower) {
        this.follower = follower;
    }

    public ProfileWithCountFollowResponse getFollowing() {
        return following;
    }

    public void setFollowing(ProfileWithCountFollowResponse following) {
        this.following = following;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

}
