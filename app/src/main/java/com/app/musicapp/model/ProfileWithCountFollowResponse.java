package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDate;

public class ProfileWithCountFollowResponse implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String displayName;
    private LocalDate dob;
    private Boolean gender;
    private String email;
    private String cover;
    private String avatar;
    private String userId;
    private int followerCount;
    private int followingCount;

    public ProfileWithCountFollowResponse() {
    }

    public ProfileWithCountFollowResponse(String id, String firstName, String lastName, String displayName, LocalDate dob, Boolean gender, String email, String cover, String avatar, String userId, int followerCount, int followingCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.cover = cover;
        this.avatar = avatar;
        this.userId = userId;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
}
