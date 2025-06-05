package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.adapter.TrackInVibeAdapter;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.Tag;
import com.app.musicapp.model.Track;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrendingTabFragment extends Fragment {
    private List<Track> trackList = new ArrayList<>();

    public TrendingTabFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending_tab, container, false);

        ListView lvTrack = view.findViewById(R.id.listViewTrending);
        mockDataTrack();
        lvTrack.setAdapter(new TrackAdapter(this, trackList));
        return view;
    }
    private void mockDataTrack() {
        trackList.add(new Track(
                "1", "This Weight Burdens Me", "Freddy River", "Description 1", "cover1.jpg",
                LocalDateTime.now(), "Freddy River", "3:04", "public", 1200,
                new Genre("1", "Rock", LocalDateTime.now()),
                Arrays.asList(new Tag("tag67", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackList.add(new Track(
                "2", "Lùa Chọn Của Em", "Nguyễn Minh Hiền (inZow)", "Description 2", "cover2.jpg",
                LocalDateTime.now(), "Nguyễn Minh Hiền (inZow)", "3:27", "public", 138000,
                new Genre("2", "Pop", LocalDateTime.now()),
                Arrays.asList(new Tag("tag9", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackList.add(new Track(
                "3", "Dựt Còn Mưa (Remake) - Tobiee", "youngtobieeasick", "Description 3", "cover3.jpg",
                LocalDateTime.now(), "youngtobieeasick", "3:14", "public", 681000,
                new Genre("3", "C", LocalDateTime.now()),
                Arrays.asList(new Tag("tag8", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackList.add(new Track(
                "4", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new Genre("4", "D", LocalDateTime.now()),
                Arrays.asList(new Tag("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackList.add(new Track(
                "5", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new Genre("4", "D", LocalDateTime.now()),
                Arrays.asList(new Tag("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        trackList.add(new Track(
                "6", "dưới cơn mưa - obito lena", "NhânGreen", "Description 4", "cover4.jpg",
                LocalDateTime.now(), "NhânGreen", "3:15", "public", 110000,
                new Genre("4", "D", LocalDateTime.now()),
                Arrays.asList(new Tag("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));

    }
}