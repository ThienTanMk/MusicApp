package com.app.musicapp.helper;

public class UrlHelper {
    private final static String BASE_URL = "http://192.168.1.116:8888/api";
    public static String getAudioUrl(String audioName){
        return BASE_URL+ "/file-service/audios/"+audioName;
    }
    public static String getCoverImageUrl(String imageName){
        return BASE_URL+ "/file-service/images/covers/"+imageName;
    }
    public static String getAvatarImageUrl(String imageName) {
        return BASE_URL+ "/file-service/images/avatars/"+imageName;
    }
}
