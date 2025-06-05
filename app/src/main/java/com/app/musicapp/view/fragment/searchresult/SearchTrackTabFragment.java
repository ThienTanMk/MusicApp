package com.app.musicapp.view.fragment.searchresult;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.model.Track;

import java.util.*;


public class SearchTrackTabFragment extends Fragment {
    private static final String ARG_TRACKS = "tracks";
    private List<Track> trackResults;

    public static SearchTrackTabFragment newInstance(List<Track> tracks) {
        SearchTrackTabFragment fragment = new SearchTrackTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACKS, new ArrayList<>(tracks));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackResults = (List<Track>) getArguments().getSerializable(ARG_TRACKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_track_tab, container, false);

        ListView listView = view.findViewById(R.id.list_view_track_results);
        TrackAdapter trackAdapter = new TrackAdapter(SearchTrackTabFragment.this, trackResults);
        listView.setAdapter(trackAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Toast.makeText(getContext(), "Clicked: " + trackResults.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}