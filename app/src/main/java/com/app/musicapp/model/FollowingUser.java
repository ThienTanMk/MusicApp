package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FollowingUser implements Serializable {
    private String id;
    private String name;
    private String location;
    private int followersCount;
    private String avatarUrl;
    private LocalDateTime followedAt;
    private boolean isFollowing;

    public FollowingUser(String id, String name, String location, int followersCount, String avatarUrl, LocalDateTime followedAt, boolean isFollowing) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.followersCount = followersCount;
        this.avatarUrl = avatarUrl;
        this.followedAt = followedAt;
        this.isFollowing = isFollowing;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
