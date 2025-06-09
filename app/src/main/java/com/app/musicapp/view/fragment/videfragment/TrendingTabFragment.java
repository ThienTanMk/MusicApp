package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.searchpage.VibeDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class TrendingTabFragment extends Fragment {
    private ListView lvTrack;
    public TrendingTabFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending_tab, container, false);

        lvTrack = view.findViewById(R.id.listViewTrending);

        // Lấy dữ liệu từ VibeDetailFragment
        VibeDetailFragment parent = (VibeDetailFragment) getParentFragment();
        List<TrackResponse> tracks = parent != null ? parent.getTracks() : new ArrayList<>();

        lvTrack.setAdapter(new TrackAdapter(this, tracks.subList(0,Math.min(20,tracks.size()))));
        return view;
    }
}