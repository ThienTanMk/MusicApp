package com.app.musicapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class DailyPlay implements Serializable {
    private LocalDate date;
    private Integer playCount;
    private Map<String, Integer> detailPlay;

    public DailyPlay(LocalDate date, Integer playCount, Map<String, Integer> detailPlay) {
        this.date = date;
        this.playCount = playCount;
        this.detailPlay = detailPlay;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Map<String, Integer> getDetailPlay() {
        return detailPlay;
    }

    public void setDetailPlay(Map<String, Integer> detailPlay) {
        this.detailPlay = detailPlay;
    }
}
