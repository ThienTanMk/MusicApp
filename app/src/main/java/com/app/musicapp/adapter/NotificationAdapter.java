package com.app.musicapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.NotificationResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends BaseAdapter {
    List<NotificationResponse> notifications;
    Context context;

    public NotificationAdapter(List<NotificationResponse> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static String getTimeAgo(String createdAtStr) {
        try {
            LocalDateTime createdAt;

            if (createdAtStr.contains("+")) {
                // ✅ Nếu có timezone offset
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(createdAtStr);
                createdAt = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            } else {
                // ✅ Nếu không có timezone
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            }

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(createdAt, now);

            long minutes = duration.toMinutes();
            long hours = duration.toHours();
            long days = duration.toDays();

            if (minutes < 1) return "Vừa xong";
            else if (minutes < 60) return minutes + " phút trước";
            else if (hours < 24) return hours + " giờ trước";
            else if (days == 1) return "Hôm qua";
            else if (days <= 7) return days + " ngày trước";
            else return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "Không rõ thời gian";
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.list_notification_item, null);

        NotificationResponse notification = notifications.get(position);
        TextView textMessage = view.findViewById(R.id.text_message);
        TextView textTime = view.findViewById(R.id.text_time);
        textMessage.setText(notification.getMessage());

        String t = getTimeAgo(notification.getCreatedAt());
        textTime.setText(t);
        loadNotificationDetail(notification, view);
        return view;
    }
    private void loadNotificationDetail(NotificationResponse notification, View view) {
        ImageView imageAvatar = view.findViewById(R.id.image_avatar);
        ImageView imageTrackCover = view.findViewById(R.id.image_track_cover);

        ApiClient.getUserProfileApiService().getUserProfile(notification.getSenderId()).enqueue(new Callback<ApiResponse<ProfileWithCountFollowResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<ProfileWithCountFollowResponse>> call, Response<ApiResponse<ProfileWithCountFollowResponse>> response) {
                try {
                    Glide.with(context)
                            .load(UrlHelper.getAvatarImageUrl(response.body().getData().getAvatar()))
                            .placeholder(R.drawable.logo)
                            .into(imageAvatar);
                } catch (Exception e){}
            }
            @Override
            public void onFailure(Call<ApiResponse<ProfileWithCountFollowResponse>> call, Throwable t) {}
        });
        ApiClient.getTrackApiService().getTrackById(notification.getTrackId()).enqueue(new Callback<ApiResponse<TrackResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TrackResponse>> call, Response<ApiResponse<TrackResponse>> response) {
                try {
                    Glide.with(context)
                            .load(UrlHelper.getCoverImageUrl(response.body().getData().getCoverImageName()))
                            .placeholder(R.drawable.logo)
                            .into(imageTrackCover);
                } catch (Exception e) {}
            }

            @Override
            public void onFailure(Call<ApiResponse<TrackResponse>> call, Throwable t) {}
        });
    }
}
