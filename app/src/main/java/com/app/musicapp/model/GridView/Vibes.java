package com.app.musicapp.model.GridView;

import java.io.Serializable;

public class Vibes implements Serializable {
    private int imageResId;
    private String title;

    public Vibes(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}

