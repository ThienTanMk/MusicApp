package com.app.musicapp.model.request;

public class RepliedCommentRequest {
    String content;

    public RepliedCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
