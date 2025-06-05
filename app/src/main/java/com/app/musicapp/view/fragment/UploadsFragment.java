package com.app.musicapp.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.widget.SearchView;
import com.app.musicapp.R;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.adapter.UploadsAdapter;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.Tag;
import com.app.musicapp.model.Track;

import java.time.LocalDateTime;
import java.util.*;


public class UploadsFragment extends Fragment {

    private ListView listViewUploads;
    private ImageView ivBack;
    private SearchView searchView;
    private UploadsAdapter uploadsAdapter;
    private List<Track> trackList;
    private List<Track> filteredTrackList;

    public UploadsFragment() {
        // Required empty public constructor
    }
    public static UploadsFragment newInstance() {
        return new UploadsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackList = new ArrayList<>();
        filteredTrackList = new ArrayList<>();
        mockTrackData();
        filteredTrackList.addAll(trackList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uploads, container, false);

        listViewUploads = view.findViewById(R.id.listViewUploads);
        ivBack = view.findViewById(R.id.iv_back);
        searchView = view.findViewById(R.id.search_view);
        // Bỏ trạng thái iconified (ẩn) mặc định
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search in your uploads");

        // Lấy EditText trong SearchView để chỉnh màu chữ
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(Color.WHITE);
        searchEditText.setTextColor(Color.WHITE);

        // Bấm vào SearchView là focus + mở bàn phím
        searchView.setOnClickListener(v -> {
            searchView.setIconified(false);
            searchView.requestFocus();

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        uploadsAdapter = new UploadsAdapter(this, filteredTrackList);
        listViewUploads.setAdapter(uploadsAdapter);

        // Xử lý nút Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                if (viewPager != null && fragmentContainer != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTracks(newText);
                return true;
            }
        });

        return view;
    }

    private void mockTrackData() {
        if (trackList == null) {
            trackList = new ArrayList<>();
        }
        trackList.add(new Track(
                "1",
                "output_audio",
                "A sample track description",
                "output_audio.mp3",
                "cover_image_1.jpg",
                LocalDateTime.now(),
                "user123",
                "0:05",
                "public",
                10,
                new Genre("1","Rock",LocalDateTime.now()),
                List.of(new Tag("1", "pop", LocalDateTime.now(), "user123"))
        ));
        trackList.add(new Track(
                "2",
                "Song 2",
                "Another track by Artist 2",
                "song2.mp3",
                "cover_image_2.jpg",
                LocalDateTime.now(),
                "user123",
                "3:45",
                "private",
                5,
                new Genre("1","Rock",LocalDateTime.now()),
                List.of(new Tag("2", "rock", LocalDateTime.now(), "user123"))
        ));
    }
    private void filterTracks(String query) {
        filteredTrackList.clear();
        if (query.isEmpty()) {
            filteredTrackList.addAll(trackList);
        } else {
            for (Track track : trackList) {
                if (track.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        track.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredTrackList.add(track);
                }
            }
        }
        uploadsAdapter.notifyDataSetChanged();
    }
}