package com.app.musicapp.model.response;

public class UploadAvatarResponse {
    private String avatarName;

    public UploadAvatarResponse(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }
}
