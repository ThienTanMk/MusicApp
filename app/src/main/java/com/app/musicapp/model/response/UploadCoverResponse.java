package com.app.musicapp.model.response;

import java.io.Serializable;

public class UploadCoverResponse implements Serializable {
    private String coverName;
    public UploadCoverResponse(String coverName) {
        this.coverName = coverName;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }
}
