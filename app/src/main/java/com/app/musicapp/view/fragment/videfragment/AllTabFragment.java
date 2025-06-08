package com.app.musicapp.view.fragment.videfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.AlbumInVibeAdapter;
import com.app.musicapp.adapter.PlaylistInVibeAdapter;
import com.app.musicapp.adapter.TrackInVibeAdapter;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.searchpage.VibeDetailFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllTabFragment extends Fragment {
    private static final String TAG = "AllTabFragment";
    private List<TrackResponse> trackResponseList = new ArrayList<>();
    private List<PlaylistResponse> playlistsList = new ArrayList<>();
    private List<AlbumResponse> albumsList = new ArrayList<>();
    private VibeDetailFragment.VibeTabNavigator tabNavigator;

    public AllTabFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);

        // Khởi tạo RecyclerView
        RecyclerView recyclerViewTrending = view.findViewById(R.id.recyclerViewTrending);
        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTrending.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        RecyclerView recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists);
        recyclerViewPlaylists.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewPlaylists.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        recyclerViewPlaylists.setHasFixedSize(true);
        recyclerViewPlaylists.setNestedScrollingEnabled(false);
        RecyclerView recyclerViewAlbums = view.findViewById(R.id.recyclerViewAlbums);
        recyclerViewAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewAlbums.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        recyclerViewAlbums.setHasFixedSize(true);
        recyclerViewAlbums.setNestedScrollingEnabled(false);
        // Gán adapter
        recyclerViewTrending.setAdapter(new TrackInVibeAdapter(this, trackResponseList.subList(0, Math.min(4, trackResponseList.size()))));
        recyclerViewPlaylists.setAdapter(new PlaylistInVibeAdapter(getContext(),playlistsList.subList(0, Math.min(4, playlistsList.size()))));
        recyclerViewAlbums.setAdapter(new AlbumInVibeAdapter(getContext(), albumsList.subList(0, Math.min(4, albumsList.size()))));

        mockDataTrack();
        mockDataPlaylists();
        mockDataAlbums();
        TextView seeAllTrending = view.findViewById(R.id.seeAllTrending);
        seeAllTrending.setOnClickListener(v -> {
            Log.d(TAG, "See all Trending clicked");
            navigateToTab(1); // Tab Trending
        });

        TextView seeAllPlaylists = view.findViewById(R.id.seeAllPlaylists);
        seeAllPlaylists.setOnClickListener(v -> {
            Log.d(TAG, "See all Playlists clicked");
            navigateToTab(2); // Tab Playlists
        });

        TextView seeAllAlbums = view.findViewById(R.id.seeAllAlbums);
        seeAllAlbums.setOnClickListener(v -> {
            Log.d(TAG, "See all Albums clicked");
            navigateToTab(3); // Tab Albums
        });

        // Thiết lập tabNavigator từ parent fragment
        if (getParentFragment() instanceof VibeDetailFragment) {
            VibeDetailFragment parent = (VibeDetailFragment) getParentFragment();
            parent.setTabNavigator(position -> {
                Log.d(TAG, "Navigating to tab position: " + position);
                parent.navigateToTab(position);
            });
            tabNavigator = parent::navigateToTab;
        } else {
            Log.e(TAG, "Parent fragment is not an instance of VibeDetailFragment");
        }

        return view;
    }
    private void navigateToTab(int position) {
        if (tabNavigator != null) {
            Log.d(TAG, "Navigating to tab position: " + position);
            tabNavigator.navigateToTab(position);
        } else {
            Log.e(TAG, "tabNavigator is null, cannot navigate to tab");
        }
    }
    private void mockDataTrack() {
        trackResponseList.add(new TrackResponse(
                "1", "This Weight Burdens Me", "Freddy River", "Description 1", "cover1.jpg",
                LocalDateTime.now(), "Freddy River", "3:04", "public", 1200,
                new GenreResponse("1", "Rock", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag67", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackResponseList.add(new TrackResponse(
                "2", "Lùa Chọn Của Em", "Nguyễn Minh Hiền (inZow)", "Description 2", "cover2.jpg",
                LocalDateTime.now(), "Nguyễn Minh Hiền (inZow)", "3:27", "public", 138000,
                new GenreResponse("2", "Pop", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag9", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackResponseList.add(new TrackResponse(
                "3", "Dựt Còn Mưa (Remake) - Tobiee", "youngtobieeasick", "Description 3", "cover3.jpg",
                LocalDateTime.now(), "youngtobieeasick", "3:14", "public", 681000,
                new GenreResponse("3", "C", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag8", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackResponseList.add(new TrackResponse(
                "4", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new GenreResponse("4", "D", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackResponseList.add(new TrackResponse(
                "5", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new GenreResponse("4", "D", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackResponseList.add(new TrackResponse(
                "6", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new GenreResponse("4", "D", LocalDateTime.now()),
                Arrays.asList(new TagResponse("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));

    }
    private void mockDataPlaylists() {
        List<TrackResponse> playlistTracks1 = new ArrayList<>();
        playlistTracks1.add(trackResponseList.get(0));
        playlistTracks1.add(trackResponseList.get(1));

        playlistsList.add(new PlaylistResponse(
                "p1", "Chill Vibes", LocalDateTime.now(), "Chill playlist", "public",
                "user4", new GenreResponse("5", "Chill", LocalDateTime.now()), "cover5.jpg",
                LocalDateTime.now(), playlistTracks1,
                Arrays.asList(new TagResponse("tag10", "chillzone", LocalDateTime.now(), "user4")),false,null
        ));

        List<TrackResponse> playlistTracks2 = new ArrayList<>();
        playlistTracks2.add(trackResponseList.get(2));
        playlistTracks2.add(trackResponseList.get(3));

        playlistsList.add(new PlaylistResponse(
                "p2", "Party Mix", LocalDateTime.now(), "Party playlist", "public",
                "user4", new GenreResponse("6", "Party", LocalDateTime.now()), "cover6.jpg",
                LocalDateTime.now(), playlistTracks2,
                Arrays.asList(new TagResponse("tag11", "partyzone", LocalDateTime.now(), "user4")),false,null
        ));
        List<TrackResponse> playlistTracks3 = new ArrayList<>();
        playlistTracks3.add(trackResponseList.get(2));
        playlistTracks3.add(trackResponseList.get(3));

        playlistsList.add(new PlaylistResponse(
                "p2", "Party Mix", LocalDateTime.now(), "Party playlist", "public",
                "user4", new GenreResponse("6", "Party", LocalDateTime.now()), "cover6.jpg",
                LocalDateTime.now(), playlistTracks2,
                Arrays.asList(new TagResponse("tag11", "partyzone", LocalDateTime.now(), "user4")),false,null
        ));
        List<TrackResponse> playlistTracks4 = new ArrayList<>();
        playlistTracks4.add(trackResponseList.get(0));
        playlistTracks4.add(trackResponseList.get(1));

        playlistsList.add(new PlaylistResponse(
                "p2", "Party Mix", LocalDateTime.now(), "Party playlist", "public",
                "user4", new GenreResponse("6", "Party", LocalDateTime.now()), "cover6.jpg",
                LocalDateTime.now(), playlistTracks2,
                Arrays.asList(new TagResponse("tag11", "partyzone", LocalDateTime.now(), "user4")),false,null
        ));
        List<TrackResponse> playlistTracks5 = new ArrayList<>();
        playlistTracks5.add(trackResponseList.get(0));
        playlistTracks5.add(trackResponseList.get(1));

        playlistsList.add(new PlaylistResponse(
                "p2", "Party Mix", LocalDateTime.now(), "Party playlist", "public",
                "user4", new GenreResponse("6", "Party", LocalDateTime.now()), "cover6.jpg",
                LocalDateTime.now(), playlistTracks2,
                Arrays.asList(new TagResponse("tag11", "partyzone", LocalDateTime.now(), "user4")),false,null
        ));
        List<TrackResponse> playlistTracks6 = new ArrayList<>();
        playlistTracks6.add(trackResponseList.get(0));
        playlistTracks6.add(trackResponseList.get(1));

        playlistsList.add(new PlaylistResponse(
                "p1", "Chill Vibes", LocalDateTime.now(), "Chill playlist", "public",
                "user4", new GenreResponse("5", "Chill", LocalDateTime.now()), "cover5.jpg",
                LocalDateTime.now(), playlistTracks1,
                Arrays.asList(new TagResponse("tag10", "chillzone", LocalDateTime.now(), "user4")),false,null
        ));
    }
    private void mockDataAlbums() {
        List<TrackResponse> albumTracks1 = new ArrayList<>();
        albumTracks1.add(trackResponseList.get(0));
        albumTracks1.add(trackResponseList.get(1));

        albumsList.add(new AlbumResponse(
                "Rock Legends", "Rock Band", "7", "album",
                Arrays.asList(new TagResponse("tag12", "rockzone", LocalDateTime.now(), "user5")),
                "Rock album", "public", "link1", "cover7.jpg", "user5", "a1",
                LocalDateTime.now(), albumTracks1, new GenreResponse("7", "Rock", LocalDateTime.now())
        ));

        List<TrackResponse> albumTracks2 = new ArrayList<>();
        albumTracks2.add(trackResponseList.get(2));
        albumTracks2.add(trackResponseList.get(3));

        albumsList.add(new AlbumResponse(
                "Pop Hits", "Pop Star", "8", "album",
                Arrays.asList(new TagResponse("tag13", "popzone", LocalDateTime.now(), "user5")),
                "Pop album", "public", "link2", "cover8.jpg", "user5", "a2",
                LocalDateTime.now(), albumTracks2, new GenreResponse("8", "Pop", LocalDateTime.now())
        ));
    }

}