package com.app.musicapp.model.response;

import java.util.List;

public class PageFollowResponse {
    private List<ProfileWithCountFollowResponse> content;

    public PageFollowResponse(List<ProfileWithCountFollowResponse> content) {
        this.content = content;
    }

    public List<ProfileWithCountFollowResponse> getContent() {
        return content;
    }

    public void setContent(List<ProfileWithCountFollowResponse> content) {
        this.content = content;
    }
}
