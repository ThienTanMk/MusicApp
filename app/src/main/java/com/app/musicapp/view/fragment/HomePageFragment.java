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
import com.app.musicapp.model.Album;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.LikedPlaylist;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.model.Tag;
import com.app.musicapp.model.Track;
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
    private List<Album> likedAlbums = new ArrayList<>();

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
            Playlist playlist = (item instanceof Playlist) ? (Playlist) item : ((LikedPlaylist) item).getPlaylist();
            Toast.makeText(getContext(), "Clicked: " + playlist.getTitle(), Toast.LENGTH_SHORT).show();
            List<Object> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylist.setAdapter(playlistAdapter);

        // Thiết lập RecyclerView cho likedPlaylists
        rvPlaylistLike.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        PlayListRVAdapter likedPlaylistAdapter = new PlayListRVAdapter(likedPlaylists, item -> {
            Playlist playlist = (item instanceof Playlist) ? (Playlist) item : ((LikedPlaylist) item).getPlaylist();
            Toast.makeText(getContext(), "Clicked: " + playlist.getTitle(), Toast.LENGTH_SHORT).show();
            List<Object> playlistList = new ArrayList<>();
            playlistList.add(item);
            navigateToPlaylistPage(playlistList);
        });
        rvPlaylistLike.setAdapter(likedPlaylistAdapter);

        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        AlbumRVAdapter albumAdapter = new AlbumRVAdapter(likedAlbums, album -> {
            Toast.makeText(getContext(), "Clicked: " + album.getAlbumTitle(), Toast.LENGTH_SHORT).show();
            navigateToAlbumPage(album);
        });
        rvAlbum.setAdapter(albumAdapter);

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
    private void navigateToAlbumPage(Album album) {
        navigateToFragment(AlbumPageFragment.newInstance(album));
    }
    private void initializeSampleData() {
        Genre rockGenre = new Genre("1", "Rock", LocalDateTime.now());
        List<Track> playlistTracks1 = new ArrayList<>();
        List<Tag> playlistTags1 = new ArrayList<>();
        playlistTags1.add(new Tag("1", "chill", LocalDateTime.now(), "user123"));

        Track track1 = new Track("1", "Song 1", "A great song", "song1.mp3", "cover1.jpg",
                LocalDateTime.now(), "user123", "3:30", "public", 1000, rockGenre, new ArrayList<>());
        playlistTracks1.add(track1);

        Playlist playlist1 = new Playlist("1", "My Playlist 1", LocalDateTime.now(), "A chill playlist", "public",
                "user123", rockGenre, "path/to/playlist1.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1);
        Playlist playlist2 = new Playlist("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenre, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1);
        Playlist playlist3 = new Playlist("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenre, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1);
        Playlist playlist4 = new Playlist("2", "My Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user123", rockGenre, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1);
        userPlaylists.add(playlist1);
        userPlaylists.add(playlist2);
        userPlaylists.add(playlist3);
        userPlaylists.add(playlist4);

        likedPlaylists.add(new LikedPlaylist("3", "user123", LocalDateTime.now(), playlist1));
        likedPlaylists.add(new LikedPlaylist("4", "user123", LocalDateTime.now(), playlist2));
        likedPlaylists.add(new LikedPlaylist("4", "user123", LocalDateTime.now(), playlist2));
        likedPlaylists.add(new LikedPlaylist("4", "user123", LocalDateTime.now(), playlist2));

        List<Track> albumTracks1 = new ArrayList<>();
        albumTracks1.add(track1);
        List<Tag> albumTags1 = new ArrayList<>();
        albumTags1.add(new Tag("2", "pop", LocalDateTime.now(), "user123"));

        likedAlbums.add(new Album(
                "Album 1", "Artist 1", "1", "Single", albumTags1, "A pop album", "public",
                "http://album1.com", "path/to/album1.jpg", "user123", "5", LocalDateTime.now(), albumTracks1, rockGenre
        ));
        likedAlbums.add(new Album(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenre
        ));
        likedAlbums.add(new Album(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenre
        ));
        likedAlbums.add(new Album(
                "Album 2", "Artist 2", "2", "Album", albumTags1, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user123", "6", LocalDateTime.now(), albumTracks1, rockGenre
        ));
    }
}