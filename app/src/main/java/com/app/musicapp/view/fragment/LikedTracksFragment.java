package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.Tag;
import com.app.musicapp.model.Track;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LikedTracksFragment extends Fragment {

    private ListView listViewLikedTracks;
    private TrackAdapter trackAdapter;
    private List<Track> likedTracks;

    public LikedTracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_tracks, container, false);

        // Ánh xạ ListView
        listViewLikedTracks = view.findViewById(R.id.listViewLikedTracks);

        likedTracks = new ArrayList<>();
        likedTracks.add(new Track(
                "1",
                "This Weight Burdens Me",
                "Freddy River",
                "Description 1",
                "cover1.jpg",
                LocalDateTime.now(),
                "Freddy River",
                "3:04",
                "public",
                1200, // 1.2K
                new Genre("1","Rock",LocalDateTime.now()),
                Arrays.asList(new Tag("tag67", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        likedTracks.add(new Track(
                "2",
                "Lùa Chọn Của Em",
                "Nguyễn Minh Hiền (inZow)",
                "Description 2",
                "cover2.jpg",
                LocalDateTime.now(),
                "Nguyễn Minh Hiền (inZow)",
                "3:27",
                "public",
                138000, // 138K
                new Genre("2","Pop",LocalDateTime.now()),
                Arrays.asList(new Tag("tag9", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        likedTracks.add(new Track(
                "3",
                "Dựt Còn Mưa (Remake) - Tobiee",
                "youngtobieeasick",
                "Description 3",
                "cover3.jpg",
                LocalDateTime.now(),
                "youngtobieeasick",
                "3:14",
                "public",
                681000, // 681K
                new Genre("3","C",LocalDateTime.now()),
                Arrays.asList(new Tag("tag8", "gentlebad", LocalDateTime.now(), "user3"))
        ));
        likedTracks.add(new Track(
                "4",
                "dưới cơn mưa - obito lena",
                "NhânGreen",
                "Description 4",
                "cover4.jpg",
                LocalDateTime.now(),
                "NhânGreen",
                "3:15",
                "public",
                110000, // 110K
                new Genre("4","D",LocalDateTime.now()),
                Arrays.asList(new Tag("tag1233", "gentlebad", LocalDateTime.now(), "user3"))
        ));

        // Khởi tạo và gắn adapter vào ListView
        trackAdapter = new TrackAdapter(this, likedTracks);
        listViewLikedTracks.setAdapter(trackAdapter);

        // Xử lý sự kiện bấm nút Back
        ImageView ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            // Hiển thị lại ViewPager và BottomNavigationView
            View mainView = requireActivity().findViewById(R.id.main);
            View viewPager = mainView.findViewById(R.id.view_pager);
            View fragmentContainer = mainView.findViewById(R.id.fragment_container);

            viewPager.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);

            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}