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
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.TagResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumsTabFragment extends Fragment {
    private RecyclerView recyclerViewAlbums;
    private AlbumInVibeAdapter adapter;
    private List<AlbumResponse> albumResponses = new ArrayList<>();
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
        adapter = new AlbumInVibeAdapter(getContext(), albumResponses);
        recyclerViewAlbums.setAdapter(adapter);
        mockData();
        return view;
    }
    private void mockData() {
        List<AlbumResponse> mockAlbumResponses = new ArrayList<>();
        mockAlbumResponses.add(new AlbumResponse(
                "Rock Legends", "Rock Band", "a1", "album",
                Arrays.asList(new TagResponse("tag1", "Rock", LocalDateTime.now(), "user5")),
                "Rock album", "public", "link1", "cover1", "user5", "a1",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("1", "Rock", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Pop Hits", "Pop Star", "a2", "album",
                Arrays.asList(new TagResponse("tag2", "Pop", LocalDateTime.now(), "user5")),
                "Pop album", "public", "link2", "cover2", "user5", "a2",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("2", "Pop", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        )); mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        ));
        mockAlbumResponses.add(new AlbumResponse(
                "Jazz Classics", "Jazz Master", "a3", "album",
                Arrays.asList(new TagResponse("tag3", "Jazz", LocalDateTime.now(), "user5")),
                "Jazz album", "public", "link3", "cover3", "user5", "a3",
                LocalDateTime.now(), new ArrayList<>(), new GenreResponse("3", "Jazz", LocalDateTime.now())
        ));
        albumResponses.addAll(mockAlbumResponses);
        adapter.notifyDataSetChanged();
    }
}