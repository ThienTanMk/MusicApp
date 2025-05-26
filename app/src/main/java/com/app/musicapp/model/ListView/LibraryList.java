package com.app.musicapp.model.ListView;

import java.io.Serializable;

public class LibraryList implements Serializable {
    private String name;

    public LibraryList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
