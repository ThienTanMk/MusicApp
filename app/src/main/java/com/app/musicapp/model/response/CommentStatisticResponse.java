package com.app.musicapp.model.response;

import com.app.musicapp.model.DailyComment;
import com.app.musicapp.model.UserStatistic;

import java.io.Serializable;
import java.util.List;

public class CommentStatisticResponse implements Serializable {
    private List<DailyComment> dailyComments;
    private List<UserStatistic> whoCommented;

    public CommentStatisticResponse(List<DailyComment> dailyComments, List<UserStatistic> whoCommented) {
        this.dailyComments = dailyComments;
        this.whoCommented = whoCommented;
    }

    public List<DailyComment> getDailyComments() {
        return dailyComments;
    }

    public void setDailyComments(List<DailyComment> dailyComments) {
        this.dailyComments = dailyComments;
    }

    public List<UserStatistic> getWhoCommented() {
        return whoCommented;
    }

    public void setWhoCommented(List<UserStatistic> whoCommented) {
        this.whoCommented = whoCommented;
    }
}
