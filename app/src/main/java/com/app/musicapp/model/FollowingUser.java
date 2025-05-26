package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FollowingUser implements Serializable {
    private String id; // ID của Follows
    private String name; // displayName từ ProfileWithCountFollowResponse
    private String location; // Không có trong dữ liệu, để mặc định
    private int followersCount; // followerCount từ ProfileWithCountFollowResponse
    private String avatarUrl; // avatar từ ProfileWithCountFollowResponse
    private LocalDateTime followedAt; // Thời gian theo dõi từ Follows
    private boolean isFollowing; // Trạng thái theo dõi

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
