package com.app.musicapp.view.fragment.searchresult;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.PlaylistAdapter;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;

import java.util.*;

public class SearchPlaylistTabFragment extends Fragment {

    private static final String ARG_PLAYLISTS = "playlists";
    private List<Object> playlistResults;

    public static SearchPlaylistTabFragment newInstance(List<Object> playlists) {
        SearchPlaylistTabFragment fragment = new SearchPlaylistTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLAYLISTS, new ArrayList<>(playlists));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlistResults = (List<Object>) getArguments().getSerializable(ARG_PLAYLISTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_playlist_tab, container, false);

        ListView listView = view.findViewById(R.id.list_view_playlist_results);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(getContext(), playlistResults);
        listView.setAdapter(playlistAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            List<Object> selectedPlaylist = new ArrayList<>();
            selectedPlaylist.add(playlistResults.get(position));
            PlaylistPageFragment playlistFragment = PlaylistPageFragment.newInstance(selectedPlaylist);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, playlistFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}