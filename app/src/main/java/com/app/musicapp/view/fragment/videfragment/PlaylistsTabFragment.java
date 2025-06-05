package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.musicapp.R;
import com.app.musicapp.adapter.PlaylistInVibeAdapter;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.model.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistsTabFragment extends Fragment {
    private RecyclerView recyclerViewPlaylists;
    private PlaylistInVibeAdapter adapter;
    private List<Playlist> playlists = new ArrayList<>();

    public PlaylistsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists_tab, container, false);

        recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists);
        recyclerViewPlaylists.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        adapter = new PlaylistInVibeAdapter(getContext(), playlists);
        recyclerViewPlaylists.setAdapter(adapter);

        mockData();

        return view;
    }

    private void mockData() {
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist(
                "p1", "Hip Hop Reset", LocalDateTime.now(), "Hip Hop playlist", "public",
                "user4", new com.app.musicapp.model.Genre("5", "Hip Hop", LocalDateTime.now()), "cover1",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag10", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p2", "Trap Leg Day", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("6", "Trap", LocalDateTime.now()), "cover2",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag11", "Trending Music", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));
        mockPlaylists.add(new Playlist(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new com.app.musicapp.model.Genre("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new Tag("tag12", "Discovery Playlists", LocalDateTime.now(), "user4"))
        ));

        playlists.addAll(mockPlaylists);
        adapter.notifyDataSetChanged();
    }
}