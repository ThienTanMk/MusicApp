package com.app.musicapp.model.request;

import java.io.Serializable;

public class  GenreRequest implements Serializable {
    private String name;

    public GenreRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}