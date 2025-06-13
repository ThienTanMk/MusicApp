package com.app.musicapp.model.response;

import com.app.musicapp.model.DailyPlay;
import com.app.musicapp.model.UserStatistic;

import java.io.Serializable;
import java.util.List;

public class PlayResponse implements Serializable {
    private List<DailyPlay> dailyPlays;
    private List<UserStatistic> topListenerIds;
}
