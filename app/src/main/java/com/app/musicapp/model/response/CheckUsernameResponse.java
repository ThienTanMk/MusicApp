package com.app.musicapp.model.response;

public class CheckUsernameResponse {
    boolean existed;
    public CheckUsernameResponse(boolean existed) {
        this.existed = existed;
    }
    public boolean isExisted() {
        return existed;
    }
    public void setExisted(boolean existed) {
        this.existed = existed;
    }
}
