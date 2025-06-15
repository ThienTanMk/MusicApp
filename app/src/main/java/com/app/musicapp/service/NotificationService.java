    package com.app.musicapp.service;

    import android.Manifest;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.content.pm.PackageManager;
    import android.os.Build;
    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.core.app.ActivityCompat;
    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.app.musicapp.R;
    import com.app.musicapp.api.ApiClient;
    import com.app.musicapp.bridge.NotificationBridge;
    import com.app.musicapp.model.NotificationViewModel;
    import com.app.musicapp.model.response.NotificationResponse;
    import com.google.firebase.messaging.FirebaseMessagingService;
    import com.google.firebase.messaging.RemoteMessage;

    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class NotificationService extends FirebaseMessagingService {


        @Override
        public void onCreate() {
            super.onCreate();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "default_channel_id",         // ID trùng với ID trong NotificationCompat.Builder
                        "Kênh thông báo mặc định",     // Tên hiển thị cho người dùng
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }

        }

        @Override
        public void onMessageReceived(@NonNull RemoteMessage message) {
            super.onMessageReceived(message);
            Map<String,String> payloadData = message.getData();
            String title = message.getData().get("title");
            String body = message.getData().get("body");
            String type = message.getData().get("type");
            String createdAt = message.getData().get("createdAt");
            String commentId = message.getData().get("commentId");
            String trackId = message.getData().get("trackId");
            String senderId = message.getData().get("senderId");

            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setCommentId(commentId);
            notificationResponse.setCreatedAt(createdAt);
            notificationResponse.setSenderId(senderId);
            notificationResponse.setTrackId(trackId);
            notificationResponse.setMessage(body);
            notificationResponse.setType(type);
            notificationResponse.setRead(false);
            NotificationBridge.pushMessage(notificationResponse);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel_id")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.notify(1001, builder.build());
        }

        @Override
        public void onNewToken(@NonNull String token) {
            super.onNewToken(token);
            sendTokenServer(token);
            Log.i("Notification","Send token");
        }
        private void sendTokenServer(String token) {
            ApiClient.getNotificationApiService().sendToken(token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }
