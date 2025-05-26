package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.AlbumAdapter;
import com.app.musicapp.model.Album;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.Track;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumsFragment extends Fragment {

    private ImageView ivBack;
    private EditText etSearch;
    private ListView listViewAlbums;
    private AlbumAdapter albumAdapter;
    private List<Album> albumList;
    private List<Album> filteredAlbumList;

    public AlbumsFragment() {
        // Required empty public constructor
    }
    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        // Ánh xạ views
        listViewAlbums = view.findViewById(R.id.listViewAlbums);
        etSearch = view.findViewById(R.id.et_search);
        ivBack = view.findViewById(R.id.iv_back);

        // Khởi tạo danh sách album
        albumList = new ArrayList<>();
        filteredAlbumList = new ArrayList<>();
        mockAlbumData();

        // Thiết lập adapter
        filteredAlbumList.addAll(albumList);
        albumAdapter = new AlbumAdapter(getContext(), filteredAlbumList);
        listViewAlbums.setAdapter(albumAdapter);

        // Xử lý nút Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Hiển thị lại ViewPager và BottomNavigationView
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                viewPager.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Xử lý tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAlbums(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
    private void mockAlbumData() {
        List<Track> tracks1 = new ArrayList<>();
        List<Track> tracks2 = new ArrayList<>();
        List<Track> tracks3 = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            tracks1.add(new Track("track" + i, "Track " + i, "", "", "", LocalDateTime.now(), "", "3:00", "public", 0, new Genre(), new ArrayList<>()));
        }
        for (int i = 0; i < 11; i++) {
            tracks2.add(new Track("track" + i, "Track " + i, "", "", "", LocalDateTime.now(), "", "3:00", "public", 0, new Genre(), new ArrayList<>()));
        }
        for (int i = 0; i < 13; i++) {
            tracks3.add(new Track("track" + i, "Track " + i, "", "", "", LocalDateTime.now(), "", "3:00", "public", 0, new Genre(), new ArrayList<>()));
        }

        Album album1 = new Album("Tiên Tiên - Chill With Me", "All Vvpop & Nhạc Việt Nam mới nhất", "", "album", new ArrayList<>(), "", "public", "", "", "", "album1", LocalDateTime.now(), tracks1, new Genre());
        Album album2 = new Album("25", "Adele", "", "album", new ArrayList<>(), "", "public", "", "", "", "album2", LocalDateTime.now(), tracks2, new Genre());
        Album album3 = new Album("Soul Of The Forest #4 | Trung Qu...", "The Bros", "", "album", new ArrayList<>(), "", "public", "", "", "", "album3", LocalDateTime.now(), tracks3, new Genre());

        albumList.add(album1);
        albumList.add(album2);
        albumList.add(album3);
    }

    private void filterAlbums(String query) {
        filteredAlbumList.clear();
        if (query.isEmpty()) {
            filteredAlbumList.addAll(albumList);
        } else {
            filteredAlbumList.addAll(albumList.stream()
                    .filter(album -> album.getAlbumTitle().toLowerCase().contains(query.toLowerCase()) ||
                            album.getMainArtists().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        albumAdapter.notifyDataSetChanged();
    }
}