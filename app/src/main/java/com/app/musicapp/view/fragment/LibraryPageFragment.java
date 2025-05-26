package com.app.musicapp.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.adapter.LibraryListAdapter;
import com.app.musicapp.adapter.TrackAdapter;
import com.app.musicapp.model.Genre;
import com.app.musicapp.model.ListView.LibraryList;
import com.app.musicapp.model.Tag;
import com.app.musicapp.model.Track;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryPageFragment extends Fragment {

    private ListView listViewMenu;
    private ListView listViewHistory;
    private LibraryListAdapter menuAdapter;
    private TrackAdapter historyAdapter;
    private List<LibraryList> libraryLists;
    private List<Track> trackList;
    public LibraryPageFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_page, container, false);

        // Khởi tạo ListView
        listViewMenu = view.findViewById(R.id.listViewMenu);

        // Tạo danh sách dữ liệu (dựa trên ảnh)
        libraryLists = new ArrayList<>();
        libraryLists.add(new LibraryList("Liked tracks"));
        libraryLists.add(new LibraryList("Playlists"));
        libraryLists.add(new LibraryList("Albums"));
        libraryLists.add(new LibraryList("Following"));
        libraryLists.add(new LibraryList("Your insights"));
        libraryLists.add(new LibraryList("Your uploads"));

        // Khởi tạo và gắn adapter vào ListView
        menuAdapter = new LibraryListAdapter(requireContext(), libraryLists);
        listViewMenu.setAdapter(menuAdapter);

        // Xử lý sự kiện click trên ListView
        listViewMenu.setOnItemClickListener((parent, v, position, id) -> {
            LibraryList selectedItem = libraryLists.get(position);
            String itemName = selectedItem.getName();

            // Ẩn ViewPager và BottomNavigationView, hiển thị fragment_container
            View mainView = requireActivity().findViewById(R.id.main);
            View viewPager = mainView.findViewById(R.id.view_pager);
            View bottomNavigation = mainView.findViewById(R.id.bottom_navigation);
            View fragmentContainer = mainView.findViewById(R.id.fragment_container);

            viewPager.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            // Điều hướng đến Fragment tương ứng
            switch (itemName) {
                case "Liked tracks":
                    LikedTracksFragment likedTracksFragment = new LikedTracksFragment();
                    transaction.replace(R.id.fragment_container, likedTracksFragment);
                    break;
                case "Playlists":
                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                    transaction.replace(R.id.fragment_container, playlistsFragment);
                    break;
                case "Albums":
                    AlbumsFragment albumsFragment = AlbumsFragment.newInstance();
                    transaction.replace(R.id.fragment_container, albumsFragment);
                    break;
                case "Following":
                    FollowingFragment followingFragment = FollowingFragment.newInstance();
                    transaction.replace(R.id.fragment_container, followingFragment);
                    break;
                case "Your uploads":
                    UploadsFragment uploadsFragment = UploadsFragment.newInstance();
                    transaction.replace(R.id.fragment_container, uploadsFragment);
                    break;
                default:
                    Toast.makeText(requireContext(), "Clicked: " + itemName, Toast.LENGTH_SHORT).show();
                    return;

            }

            transaction.addToBackStack(null);
            transaction.commit();
        });
        // Khởi tạo ListView cho Listening history
        listViewHistory = view.findViewById(R.id.listViewHistory);

        trackList = new ArrayList<>();
        trackList.add(new Track(
                "1", // id
                "Khóc Cùng Em", // title
                "Description 1", // description
                "file1.mp3", // fileName
                "cover1.jpg", // coverImageName
                LocalDateTime.now(), // createdAt
                "Mr.Siro X Gray", // userId
                "3:10", // duration
                "public", // privacy
                67, // countPlay
                new Genre("1","Pop",LocalDateTime.now()), // genre
                Arrays.asList(new Tag("tag3", "gentlebad", LocalDateTime.now(), "user3")) // tags
        ));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            trackList.add(new Track(
                    "2", // id
                    "(slowed) Khiếp Chông Chung", // title
                    "Description 2", // description
                    "file2.mp3", // fileName
                    "cover2.jpg", // coverImageName
                    LocalDateTime.now(), // createdAt
                    "qwrld s.simp", // userId
                    "4:28", // duration
                    "public", // privacy
                    165000, // countPlay
                    new Genre("2","Hip Hop",LocalDateTime.now()), // genre
                    Arrays.asList(new Tag("tag3", "gentlebad", LocalDateTime.now(), "user3")) // tags
            ));
        }
        trackList.add(new Track(
                "3", // id
                "Đế Anh Lừng Thiền Linh Hương", // title
                "Description 3", // description
                "file3.mp3", // fileName
                "cover3.jpg", // coverImageName
                LocalDateTime.now(), // createdAt
                "Trương Anh 2", // userId
                "4:34", // duration
                "public", // privacy
                2400000, // countPlay
                new Genre("3","Ballad",LocalDateTime.now()), // genre
                Arrays.asList(new Tag("tag3", "gentlebad", LocalDateTime.now(), "user3")) // tags
        ));

        // Khởi tạo và gắn adapter vào ListView history
        historyAdapter = new TrackAdapter(this, trackList);
        listViewHistory.setAdapter(historyAdapter);
        return view;
    }
}