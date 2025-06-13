package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class DailyLike implements Serializable {
    private LocalDate date;
    private Integer likedCount;
    private Map<String, Integer> detailLiked;

    public DailyLike(LocalDate date, Integer likedCount, Map<String, Integer> detailLiked) {
        this.date = date;
        this.likedCount = likedCount;
        this.detailLiked = detailLiked;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Integer likedCount) {
        this.likedCount = likedCount;
    }

    public Map<String, Integer> getDetailLiked() {
        return detailLiked;
    }

    public void setDetailLiked(Map<String, Integer> detailLiked) {
        this.detailLiked = detailLiked;
    }
}
