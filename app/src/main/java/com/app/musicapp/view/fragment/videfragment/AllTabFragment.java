package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.adapter.TrackInVibeAdapter;
import com.app.musicapp.model.Track;

import java.util.ArrayList;
import java.util.List;

public class AllTabFragment extends Fragment {

    public AllTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Track> trackList = new ArrayList<>();
        recyclerView.setAdapter(new TrackInVibeAdapter(this, trackList));

        return view;
    }
}