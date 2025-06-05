package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.musicapp.R;
import com.app.musicapp.adapter.AlbumInVibeAdapter;
import com.app.musicapp.model.Album;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumsTabFragment extends Fragment {
    private RecyclerView recyclerViewAlbums;
    private AlbumInVibeAdapter adapter;
    private List<Album> albums = new ArrayList<>();
    public AlbumsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums_tab, container, false);

        recyclerViewAlbums = view.findViewById(R.id.recyclerViewAlbums);
        recyclerViewAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        adapter = new AlbumInVibeAdapter(getContext(), albums);
        recyclerViewAlbums.setAdapter(adapter);
        mockData();
        return view;
    }
    private void mockData() {
        List<Album> mockAlbums = new ArrayList<>();
        mockAlbums.add(new Album(
                "Rock Legends", "Rock Band", "a1", "album",
                Arrays.asList(new Tag("tag1", "Rock", LocalDateTime.now(), "user5")),
                "Rock album", "public", "link1", "cover1", "user5", "a1",
                LocalDateTime.now(), new ArrayList<>(), new Genre("1", "Rock", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Pop Hits", "Pop Star", "a2", "album",
                Arrays.asList(new Tag("tag2", "Pop", LocalDateTime.now(), "user5")),
                "Pop album", "public", "link2", "cover2", "user5", "a2",
                LocalDateTime.now(), new ArrayList<>(), new Genre("2", "Pop", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        )); mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbums.add(new Album(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new Tag("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new Genre("3", "Jazz", LocalDateTime.now())
        ));
        albums.addAll(mockAlbums);
        adapter.notifyDataSetChanged();
    }
}