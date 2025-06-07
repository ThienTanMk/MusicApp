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
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TagResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistsTabFragment extends Fragment {
    private RecyclerView recyclerViewPlaylists;
    private PlaylistInVibeAdapter adapter;
    private List<PlaylistResponse> playlistResponses = new ArrayList<>();

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
        adapter = new PlaylistInVibeAdapter(getContext(), playlistResponses);
        recyclerViewPlaylists.setAdapter(adapter);

        mockData();

        return view;
    }

    private void mockData() {
        List<PlaylistResponse> mockPlaylistResponses = new ArrayList<>();
        mockPlaylistResponses.add(new PlaylistResponse(
                "p1", "Hip Hop Reset", LocalDateTime.now(), "Hip Hop playlist", "public",
                "user4", new GenreResponse("5", "Hip Hop", LocalDateTime.now()), "cover1",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag10", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p2", "Trap Leg Day", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("6", "Trap", LocalDateTime.now()), "cover2",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag11", "Trending Music", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));
        mockPlaylistResponses.add(new PlaylistResponse(
                "p3", "Trap Party", LocalDateTime.now(), "Trap playlist", "public",
                "user4", new GenreResponse("7", "Trap", LocalDateTime.now()), "cover3",
                LocalDateTime.now(), new ArrayList<>(),
                Arrays.asList(new TagResponse("tag12", "Discovery Playlists", LocalDateTime.now(), "user4")),false,null
        ));

        playlistResponses.addAll(mockPlaylistResponses);
        adapter.notifyDataSetChanged();
    }
}