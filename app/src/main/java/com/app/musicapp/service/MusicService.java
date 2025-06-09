    package com.app.musicapp.service;

    import android.app.Notification;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.Service;
    import android.content.Intent;
    import android.media.AudioManager;
    import android.media.MediaPlayer;
    import android.os.Binder;
    import android.os.Build;
    import android.os.Handler;
    import android.os.IBinder;
    import android.util.Log;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.app.musicapp.api.ApiClient;
    import com.app.musicapp.helper.UrlHelper;
    import com.app.musicapp.model.response.ApiResponse;
    import com.app.musicapp.model.response.TrackResponse;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class MusicService extends Service {
        private List<TrackResponse> nextUpItems;
        private MediaPlayer mediaPlayer;
        private final Handler handler = new Handler();

        private String playBackMode;
        private int currentIndex;

        // nhận yêu cầu tiếp tục bài nhạc
        public static final String ACTION_PLAY = "ACTION_PLAY";
        // nhận yêu cầu ngừng phát
        public static final String ACTION_PAUSE = "ACTION_PAUSE";
        // phát update seek bar từ bên ngoài
        public static final String ACTION_UPDATE_SEEKBAR_PROGRESS = "ACTION_UPDATE_SEEKBAR_PROGRESS";

        // PHÁT THÔNG BÁO THAY ĐỔI BÀI NHẠC KHÁC RA NGOÀI
        // VÍ DỤ CLICK LIKE TRONG DANH SÁCH NHẠC -> ACTIVITY MUSIC PLAYER HAY NOTICATION CŨNG PHẢI THAY ĐỔI
        public static final String ACTION_CHANGED_CURRENT_TRACK="ACTION_CHANGED_CURRENT_TRACK";

        public static final String REPEAT_ONE = "REPEAT_ONE";
        public static final String REPEAT_ALL = "REPEAT_ALL";
        public static final String SHUFFLE = "SHUFFLE";

        public static final String PLAY_ONCE = "PLAY_ONCE";


        // bind serivce ko dung
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("start command","start command on music service");
            return super.onStartCommand(intent, flags, startId);
        }


        //TEST DATA
        private void getLikedTrack(){
            ApiClient.getApiService().getLikedTrack().enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                    if(response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                        nextUpItems = response.body().getData();
                        currentIndex = 0;
                        sendTrackInfo();
                    }
                    else{
                        currentIndex = -1;
                    }

                }
                @Override
                public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {}
            });

        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.currentIndex = -1;
            this.playBackMode = PLAY_ONCE;

            // tạo notification
            createNotificationChannel();

            getLikedTrack();
        }
        public TrackResponse getCurrentTrack(){
            if(this.nextUpItems==null||this.nextUpItems.isEmpty()) {
                Toast.makeText(getBaseContext(),"Can not play now. Track is empty",Toast.LENGTH_SHORT).show();
                return null;
            }
            return this.nextUpItems.get(currentIndex);
        }
        public List<TrackResponse> getNextUpItems(){
            return this.nextUpItems;
        }

        public void setNextUpItems(List<TrackResponse> trackResponses) {
            this.nextUpItems = trackResponses;
        }
        public int getCurrentIndex(){
            return currentIndex;
        }

        public void updateProgressTime(int progress){
            if(mediaPlayer==null) return;
            mediaPlayer.seekTo(progress*1000);
            startSeekbarUpdate();
        }
        private void sendTrackInfo(){
            Intent intent = new Intent(ACTION_CHANGED_CURRENT_TRACK);
            sendBroadcast(intent);
        }
        public String getPlayBackMode(){
            return playBackMode;
        }
        public void setPlayBackMode(String playBackMode){
            this.playBackMode = playBackMode;
            Intent intent = new Intent(playBackMode);
            sendBroadcast(intent);
        }
        private void determineNextSong(){
            if(playBackMode.equals(REPEAT_ONE)){
                playCurrentMusic();
            }
            else if(playBackMode.equals(REPEAT_ALL)){
                if(currentIndex==nextUpItems.size()-1){
                    currentIndex=-1;
                }
                currentIndex ++;
                playMusicAtIndex(currentIndex);
            }
            else if(playBackMode.equals(SHUFFLE)){
                Random random = new Random();
                currentIndex = random.nextInt(nextUpItems.size());
                playMusicAtIndex(currentIndex);
            }
            else{
                if(currentIndex==nextUpItems.size()-1){
                    sendPauseAction();
                    return;
                }
                currentIndex++;
                playMusicAtIndex(currentIndex);
            }
        }
        public void playNext(){
            if(this.currentIndex==nextUpItems.size()-1){
                if(this.playBackMode.equals(REPEAT_ALL))
                    currentIndex = -1;
                else return;
            }
            currentIndex++;
            if(!isPlaying()){
                changeMediaPlayerResource(UrlHelper.getAudioUrl(getCurrentTrack().getFileName()));
            }
            else
                playMusicAtIndex(currentIndex);
        }
        public void playBack(){
            if(this.currentIndex==0) return;
            currentIndex --;
            if(!isPlaying()){
                changeMediaPlayerResource(UrlHelper.getAudioUrl(getCurrentTrack().getFileName()));
            }
            else
                playMusicAtIndex(currentIndex);
        }

        private void changeMediaPlayerResource(String url){
            try{
                if(mediaPlayer==null){
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    Intent intent = new Intent(ACTION_UPDATE_SEEKBAR_PROGRESS);
                    intent.putExtra("current_position", mediaPlayer.getCurrentPosition());
                    intent.putExtra("duration", 0);
                    sendBroadcast(intent);
                    sendTrackInfo();
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        // nghe nhac + gui start + gui track info + update seek bar + pause
        private void playUrl(String url) {
            
            try {
                if(mediaPlayer==null){
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                else  {
                    mediaPlayer.reset();
                }

                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    sendPlayAction();
                    sendTrackInfo();
                    updateNotification("Playing music");
                    startSeekbarUpdate();
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    determineNextSong();
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    sendPauseAction();
                    return true;
                });

            } catch (IOException e) {
                e.printStackTrace();
                stopSelf();
            }
        }
        private void sendPauseAction(){
            Intent intent = new Intent(ACTION_PAUSE);
            sendBroadcast(intent);
        }
        private void sendPlayAction(){
            Intent intent = new Intent(ACTION_PLAY);
            sendBroadcast(intent);
        }
        public boolean isPlaying(){
            if(this.mediaPlayer==null) return false;
            return this.mediaPlayer.isPlaying();
        }

        public void playMusicAtIndex(int index){
            if(index>nextUpItems.size()) {
                Toast.makeText(getBaseContext(),"Can not play now",Toast.LENGTH_SHORT).show();
            }
            currentIndex = index;
            TrackResponse track = this.nextUpItems.get(currentIndex);
            playUrl(UrlHelper.getAudioUrl(track.getFileName()));
        }

        public void pauseCurrentMusic() {
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) return;
            mediaPlayer.pause();
            updateNotification("Paused");
            sendPauseAction();
        }

        // check next up tracks and send play + track 
        public void playCurrentMusic(){
            if(nextUpItems==null||nextUpItems.isEmpty()||currentIndex==-1||currentIndex>=nextUpItems.size()){
                Toast.makeText(getBaseContext(),"Can not play now",Toast.LENGTH_SHORT).show();
                return;
            }
            var track = nextUpItems.get(currentIndex);
            if(mediaPlayer==null){
                playUrl(UrlHelper.getAudioUrl(track.getFileName()));
                return;
            }
            mediaPlayer.start();
            sendPlayAction();
            sendTrackInfo();
            startSeekbarUpdate();
            updateNotification("Play");
        }

        private void startSeekbarUpdate() {
            handler.postDelayed(updateRunnable, 1000);
        }

        private void stopSeekbarUpdate() {
            handler.removeCallbacks(updateRunnable);
        }

        private final Runnable updateRunnable = new Runnable() {
            private boolean isStop = false;
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isStop = false;
                    // Gửi broadcast cập nhật progress nếu cần (ví dụ)
                    Intent intent = new Intent(ACTION_UPDATE_SEEKBAR_PROGRESS);
                    intent.putExtra("current_position", mediaPlayer.getCurrentPosition());
                    intent.putExtra("duration", mediaPlayer.getDuration());
                    sendBroadcast(intent);
                    handler.postDelayed(this, 1000);
                }
                else if(!isStop){
                    isStop=true;
                    Intent intent = new Intent(ACTION_UPDATE_SEEKBAR_PROGRESS);
                    intent.putExtra("current_position", mediaPlayer.getCurrentPosition());
                    intent.putExtra("duration", mediaPlayer.getDuration());
                    sendBroadcast(intent);
                }
            }
        };

        private Notification buildNotification(String contentText) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "music_channel")
                    .setContentTitle("Music Player")
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            return builder.build();
        }

        private void updateNotification(String contentText) {
            Notification notification = buildNotification(contentText);
            NotificationManagerCompat.from(this).notify(1, notification);
        }

        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "music";
                String description = "abc";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("music_channel", name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopSeekbarUpdate();

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        public class LocalBinder extends Binder {
            public  MusicService getService() {
                // Return this instance of LocalService so clients can call public methods.
                return MusicService.this;
            }
        }
        // Binder
        private final IBinder binder = new LocalBinder();
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return binder;
        }

    }
