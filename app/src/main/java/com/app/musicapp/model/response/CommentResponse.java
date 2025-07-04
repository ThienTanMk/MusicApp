package com.app.musicapp.model.response;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {
    String id;
    String trackId;
    String userId;
    String repliedUserId;
    Boolean isLiked;
    String content;
    LocalDateTime commentAt;
    int likeCount;
    List<CommentResponse> replies;

    UserProfileResponse user;

    public CommentResponse() {
    }

    public CommentResponse(String id, String trackId, String userId, String repliedUserId, Boolean isLiked, String content, LocalDateTime commentAt, int likeCount, List<CommentResponse> replies) {
        this.id = id;
        this.trackId = trackId;
        this.userId = userId;
        this.repliedUserId = repliedUserId;
        this.isLiked = isLiked;
        this.content = content;
        this.commentAt = commentAt;
        this.likeCount = likeCount;
        this.replies = replies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRepliedUserId() {
        return repliedUserId;
    }

    public void setRepliedUserId(String repliedUserId) {
        this.repliedUserId = repliedUserId;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(LocalDateTime commentAt) {
        this.commentAt = commentAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<CommentResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponse> replies) {
        this.replies = replies;
    }

    public UserProfileResponse getUser() { return user; }
//
    public void setUser(UserProfileResponse user) { this.user = user;}
}
