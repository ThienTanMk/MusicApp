package com.app.musicapp.model;

import java.io.Serializable;

public class UserStatistic implements Serializable {
    private String userId;
    private Integer count;

    public UserStatistic(String userId, Integer count) {
        this.userId = userId;
        this.count = count;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
