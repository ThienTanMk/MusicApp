package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.AlbumRVAdapter;
import com.app.musicapp.adapter.PlayListRVAdapter;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.app.musicapp.view.fragment.track.LikedTracksFragment;

import java.time.LocalDateTime;
import java.util.*;

public class HomePageFragment extends Fragment {
    private ImageView ivUpload, ivNoti, ivRandom;
    private LinearLayout llYourLikes;
    private RecyclerView rvPlaylist, rvPlaylistLike, rvAlbum;
    private List<Object> userPlaylists = new ArrayList<>();
    private List<Object> likedPlaylists = new ArrayList<>();
    private List<AlbumResponse> likedAlbumResponses = new ArrayList<>();

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Ánh xạ view
        ivUpload = view.findViewById(R.id.iv_upload);
        ivNoti = view.findViewById(R.id.iv_noti);
        ivRandom = view.findViewById(R.id.iv_random);
        llYourLikes = view.findViewById(R.id.ll_your_likes);
        rvPlaylist = view.findViewById(R.id.rv_playlist);
        rvPlaylistLike = view.findViewById(R.id.rv_playlist_like);
        rvAlbum = view.findViewById(R.id.rv_album);

        initializeSampleData();

        // Thiết lập RecyclerView cho userPlaylists
        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        PlayListRVAdapter playlistAdapter = new PlayListRVAdapter(userPlaylists, item -> {
            PlaylistResponse playlistResponse = (item instanceof PlaylistResponse) ? (PlaylistResponse) item : ((LikedPlaylistResponse) item).getPlaylist();
            Toast.makeText(getContext(), "Clicked: " + playlistResponse.getTitle(), Toast.LENGTH_SHORT).show();
            List<Object> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylist.setAdapter(playlistAdapter);

        // Thiết lập RecyclerView cho likedPlaylists
        rvPlaylistLike.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        PlayListRVAdapter likedPlaylistAdapter = new PlayListRVAdapter(likedPlaylists, item -> {
            PlaylistResponse playlistResponse = (item instanceof PlaylistResponse) ? (PlaylistResponse) item : ((LikedPlaylistResponse) item).getPlaylist();
            Toast.makeText(getContext(), "Clicked: " + playlistResponse.getTitle(), Toast.LENGTH_SHORT).show();
            List<Object> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylistLike.setAdapter(likedPlaylistAdapter);

        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
//        AlbumRVAdapter albumAdapter = new AlbumRVAdapter(likedAlbumResponses, album -> {
//            Toast.makeText(getContext(), "Clicked: " + album.getAlbumTitle(), Toast.LENGTH_SHORT).show();
//            navigateToAlbumPage(album);
//        });
       // rvAlbum.setAdapter(albumAdapter);

        // Xử lý sự kiện
        ivUpload.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Upload clicked", Toast.LENGTH_SHORT).show();
        });

        ivNoti.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Notification clicked", Toast.LENGTH_SHORT).show();
        });

        llYourLikes.setOnClickListener(v -> {
            navigateToFragment(new LikedTracksFragment());
        });

        return view;
    }
    private void navigateToFragment(Fragment fragment) {
        View container = requireActivity().findViewById(R.id.fragment_container);
        View viewPager = requireActivity().findViewById(R.id.view_pager);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
            if (viewPager != null) {
                viewPager.setVisibility(View.GONE);
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void navigateToPlaylistPage(List<Object> playlistList) {
        navigateToFragment(PlaylistPageFragment.newInstance(playlistList));
    }
    private void navigateToAlbumPage(AlbumResponse albumResponse) {
        //navigateToFragment(AlbumPageFragment.newInstance(albumResponse));
    }
    private void initializeSampleData() {
        GenreResponse rockGenreResponse = new GenreResponse("1", "Rock", LocalDateTime.now());
        List<TrackResponse> playlistTracks1 = new ArrayList<>();
        List<TagResponse> playlistTags1 = new ArrayList<>();
        playlistTags1.add(new TagResponse("1", "chill", LocalDateTime.now(), "user123"));

        TrackResponse trackResponse1 = new TrackResponse("1", "Song 1", "A great song", "song1.mp3", "cover1.jpg",
                LocalDateTime.now(), "user123", "3:30", "public", 1000, rockGenreResponse, new ArrayList<>());
        playlistTracks1.add(trackResponse1);

        PlaylistResponse playlistResponse1 = new PlaylistResponse("1", "My Playlist 1", LocalDateTime.now(), "A chill playlist", "public",
                "user123", rockGenreResponse, "path/to/playlist1.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1,false,null);
        PlaylistResponse playlistResponse2 = new PlaylistResponse("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenreResponse, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1,false,null);
        PlaylistResponse playlistResponse3 = new PlaylistResponse("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenreResponse, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1,false,null);
        PlaylistResponse playlistResponse4 = new PlaylistResponse("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenreResponse, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1,false,null);
        userPlaylists.add(playlistResponse1);
        userPlaylists.add(playlistResponse2);
        userPlaylists.add(playlistResponse3);
        userPlaylists.add(playlistResponse4);

        likedPlaylists.add(new LikedPlaylistResponse("3", "user123", LocalDateTime.now(), playlistResponse1));
        likedPlaylists.add(new LikedPlaylistResponse("4", "user123", LocalDateTime.now(), playlistResponse2));
        likedPlaylists.add(new LikedPlaylistResponse("4", "user123", LocalDateTime.now(), playlistResponse2));
        likedPlaylists.add(new LikedPlaylistResponse("4", "user123", LocalDateTime.now(), playlistResponse2));

        List<TrackResponse> albumTracks1 = new ArrayList<>();
        albumTracks1.add(trackResponse1);
        List<TagResponse> albumTags1 = new ArrayList<>();
        albumTags1.add(new TagResponse("2", "pop", LocalDateTime.now(), "user123"));

        likedAlbumResponses.add(new AlbumResponse(
                "Album 1", "Artist 1", "1", "Single", albumTags1, "A pop album", "public",
                "http://album1.com", "path/to/album1.jpg", "user123", "5", LocalDateTime.now(), albumTracks1, rockGenreResponse
        ));
        likedAlbumResponses.add(new AlbumResponse(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenreResponse
        ));
        likedAlbumResponses.add(new AlbumResponse(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenreResponse
        ));
        likedAlbumResponses.add(new AlbumResponse(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenreResponse
        ));
    }
}