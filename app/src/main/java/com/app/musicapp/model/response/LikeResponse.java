package com.app.musicapp.model.response;

import com.app.musicapp.model.DailyLike;
import com.app.musicapp.model.UserStatistic;

import java.io.Serializable;
import java.util.List;

public class LikeResponse implements Serializable {
    private List<DailyLike> dailyLikes;
    private List<UserStatistic> whoLiked;

    public LikeResponse(List<DailyLike> dailyLikes, List<UserStatistic> whoLiked) {
        this.dailyLikes = dailyLikes;
        this.whoLiked = whoLiked;
    }

    public List<DailyLike> getDailyLikes() {
        return dailyLikes;
    }

    public void setDailyLikes(List<DailyLike> dailyLikes) {
        this.dailyLikes = dailyLikes;
    }

    public List<UserStatistic> getWhoLiked() {
        return whoLiked;
    }

    public void setWhoLiked(List<UserStatistic> whoLiked) {
        this.whoLiked = whoLiked;
    }
}
