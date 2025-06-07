package com.app.musicapp.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GenreResponse implements Serializable {
    private String id;
    private String name;
    private LocalDateTime createdAt;

    public GenreResponse() {
    }

    public GenreResponse(String id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
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
}
