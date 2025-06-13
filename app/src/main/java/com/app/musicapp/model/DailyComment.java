package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class DailyComment implements Serializable {
    private LocalDate date;
    private Integer commentCount;
    private Map<String, Integer> detailComment;

    public DailyComment(LocalDate date, Integer commentCount, Map<String, Integer> detailComment) {
        this.date = date;
        this.commentCount = commentCount;
        this.detailComment = detailComment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Map<String, Integer> getDetailComment() {
        return detailComment;
    }

    public void setDetailComment(Map<String, Integer> detailComment) {
        this.detailComment = detailComment;
    }
}
