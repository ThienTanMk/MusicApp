package com.app.musicapp.api;

import android.content.Context;

import com.app.musicapp.helper.LocalDateAdapter;
import com.app.musicapp.helper.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Replace 192.168.1.x with your computer's actual IP address
    private static final String BASE_URL = "http://192.168.1.4:8888/";
    private static Retrofit retrofit = null;
    private static Context context;

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create Gson instance with LocalDateTime adapter
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            // Create OkHttpClient with interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(ApiService.class);
    }

    public static LikedTrackApiService getLikedTrackService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(LikedTrackApiService.class);
    }

    public static AlbumApiService getAlbumService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(AlbumApiService.class);
    }
    public static CommentApiService getCommentService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(CommentApiService.class);
    }
    public static UserService getUserService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(UserService.class);
    }
    public static PlaylistApiService getPlaylistService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(PlaylistApiService.class);
    }
    public static LikedPlaylistApiService getLikedPlaylistService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(LikedPlaylistApiService.class);
    }
    public static TrackApiService getTrackApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(TrackApiService.class);
    }
   public static HistoryApiService getHistoryApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(HistoryApiService.class);
    }
    public static GenreService getGenreService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(GenreService.class);
    }
    public static TagApiService getTagService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(TagApiService.class);
    }

    public static IdentityApiService getIdentityService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(IdentityApiService.class);

    }

    public static SearchApiService getSearchApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(SearchApiService.class);
    }
    public static RecommendedApiService getRecommendedApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(RecommendedApiService.class);
    }
    public static UserProfileApiService getUserProfileApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(UserProfileApiService.class);
    }
    public static StatisticApiService getStatisticApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(StatisticApiService.class);
    }
    public static AdminApiService getAdminApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create(AdminApiService.class);
    }
    public static NotificationApiService getNotificationApiService() {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with context first");
        }
        return getClient().create( NotificationApiService.class);
    }

    // Call this when you need to clear the retrofit instance (e.g., on logout)
    public static void clearInstance() {
        retrofit = null;
    }
}