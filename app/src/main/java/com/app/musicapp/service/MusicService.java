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

    import androidx.annotation.Nullable;
    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.app.musicapp.helper.UrlHelper;
    import com.app.musicapp.model.response.TrackResponse;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;

    public class MusicService extends Service {
        private List<TrackResponse> nextUpItems;
        private MediaPlayer mediaPlayer;
        private final Handler handler = new Handler();

        private String playBackMode = PLAY_ONCE;
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


        public static final String REPEAT_ONE = "ACTION_REPEAT_ONE";
        public static final String REPEAT_ALL = "ACTION_REPEAT_ALL";
        public static final String SHUFFLE = "ACION_SHUFFLE";

        public static final String PLAY_ONCE = "ACTION_PLAY_ONCE";

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            createNotificationChannel();
            startForeground(1, buildNotification("Music Service Ready"));
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            nextUpItems = new ArrayList<>();
            nextUpItems.add(new TrackResponse("id1","Hello","description","1748680692music.mp3","1748804121data-uri.jpeg",null,"user1","123","1:0","private",1,null,null));
            nextUpItems.add(new TrackResponse("id1","Hello","description","1748680692music.mp3","1748681566data-uri.png",null,"user1","abc","1:0","private",1,null,null));
            nextUpItems.add(new TrackResponse("id1","Hello","description","1748680692music.mp3","1748804121data-uri.jpeg",null,"user1","def","1:0","private",1,null,null));
            nextUpItems.add(new TrackResponse("id1","Hello","description","1748680692music.mp3","1748681566data-uri.png",null,"user1","ghk","1:0","private",1,null,null));
            currentIndex = 0;

        }
        public TrackResponse getCurrentTrack(){
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
            TrackResponse trackResponse = this.nextUpItems.get(currentIndex);
            Intent intent = new Intent(ACTION_CHANGED_CURRENT_TRACK);
            intent.putExtra("track_info", trackResponse);
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
                if(currentIndex==nextUpItems.size()-1)return;
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
            playMusicAtIndex(currentIndex);
        }
        public void playBack(){
            if(this.currentIndex==0) return;
            currentIndex --;
            playMusicAtIndex(currentIndex);
        }
        private void playUrl(String url) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    updateNotification("Playing music");
                    startSeekbarUpdate();
                });

                sendTrackInfo();
                mediaPlayer.setOnCompletionListener(mp -> {
                    determineNextSong();
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
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
            if(index>nextUpItems.size()) throw new ArrayIndexOutOfBoundsException("Index is out of size");
            currentIndex = index;
            playUrl(UrlHelper.getAudioUrl(nextUpItems.get(currentIndex).getFileName()));
            sendPlayAction();
            sendTrackInfo();
        }

        public void pauseCurrentMusic() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                updateNotification("Paused");
            }
            sendPauseAction();
        }

        public void playCurrentMusic(){
            sendPlayAction();
            if(mediaPlayer==null){
                playUrl(UrlHelper.getAudioUrl(nextUpItems.get(currentIndex).getFileName()));
                return;
            }
            mediaPlayer.start();
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
