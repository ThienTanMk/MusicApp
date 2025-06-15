package com.app.musicapp.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.musicapp.model.response.NotificationResponse;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<NotificationResponse> notification = new MutableLiveData<>();
    public void setNotification(NotificationResponse notification) {
        this.notification.postValue(notification);
    }
    public MutableLiveData<NotificationResponse> getNotification() {
        return notification;
    }
}
