package com.app.musicapp.model.request;

import java.io.Serializable;
import java.time.LocalDate;

public class ProfileUpdateRequest implements Serializable {
    private String displayName;

    public ProfileUpdateRequest(String displayName) {

        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
