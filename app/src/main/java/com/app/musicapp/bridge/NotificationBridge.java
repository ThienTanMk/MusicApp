package com.app.musicapp.bridge;

import com.app.musicapp.model.NotificationViewModel;
import com.app.musicapp.model.response.NotificationResponse;

public class NotificationBridge {
    private static NotificationViewModel viewModel;

    public static void setViewModel(NotificationViewModel vm) {
        viewModel = vm;
    }

    public static void pushMessage(NotificationResponse notification) {
        if (viewModel != null) {
            viewModel.setNotification(notification);
        }
    }
}
