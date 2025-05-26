package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Follows implements Serializable {
    private String id;
    private UserProfileResponse follower; // Người theo dõi
    private ProfileWithCountFollowResponse following; // Người được theo dõi
    private LocalDateTime followedAt;

    // Constructor không tham số (yêu cầu cho Serializable)
    public Follows() {}

    // Constructor đầy đủ
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
