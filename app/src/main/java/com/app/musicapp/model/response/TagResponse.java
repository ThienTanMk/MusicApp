package com.app.musicapp.model.response;

import com.app.musicapp.interfaces.BaseManageItem;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TagResponse implements Serializable, BaseManageItem {
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private String userId;

    public TagResponse(String id, String name, LocalDateTime createdAt, String userId) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
